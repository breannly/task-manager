package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controller.exception.IntersectionTimeException;
import controller.exception.ManagerSaveException;
import controller.imanager.TaskManager;
import controller.utility.Managers;
import model.entity.Epic;
import model.entity.Subtask;
import model.entity.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class HttpTaskServer {
    private final Gson gson = new Gson();
    private final TaskManager manager = Managers.getDefaultBackedFileTaskManager();
    private static final HttpTaskServer server = new HttpTaskServer();
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new PrioritizedTasksHandler());
        httpServer.createContext("/tasks/task", new TasksHandler());
        httpServer.createContext("/tasks/epic", new EpicsHandler());
        httpServer.createContext("/tasks/subtask", new SubtasksHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.createContext("/tasks/subtask/epic", new AllTasksHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/task запроса от клиента.");
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            OutputStream out = exchange.getResponseBody();
            switch (method) {
                case "GET":
                    if (path.endsWith("/task")) {
                        exchange.sendResponseHeaders(200, 0);
                        out.write(server.getJsonTasks().getBytes());
                    } else {
                        Long id = Long.parseLong(path.split("/")[3]);
                        try {
                            exchange.sendResponseHeaders(200, 0);
                            out.write(server.getJsonTaskById(id).getBytes());
                        } catch (ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Task task = server.getTaskFromJson(body);
                    if (task.getId() == null) {
                        try {
                            server.manager.addTask(task);
                            exchange.sendResponseHeaders(200, 0);
                        } catch (IntersectionTimeException | ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        try {
                            server.manager.updateTask(task);
                            exchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException | IntersectionTimeException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("/task")) {
                        try {
                            server.manager.deleteTasks();
                            exchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        Long id = Long.parseLong(path.split("/")[3]);
                        try {
                            server.manager.deleteTaskById(id);
                        } catch (ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
            }
            out.close();
        }
    }

    private String getJsonTasks() {
        List<Task> tasks = manager.getTasksList();
        return gson.toJson(tasks);
    }

    private Task getTaskFromJson(String body) {
        return gson.fromJson(body, Task.class);
    }

    private String getJsonTaskById(Long id) throws ManagerSaveException {
        Task task = manager.getTaskById(id);
        return gson.toJson(task);
    }

    static class EpicsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            OutputStream out = exchange.getResponseBody();
            switch (method) {
                case "GET":
                    if (path.endsWith("/epic")) {
                        exchange.sendResponseHeaders(200, 0);
                        out.write(server.getJsonEpics().getBytes());
                    } else {
                        Long id = Long.parseLong(path.split("/")[3]);
                        try {
                            exchange.sendResponseHeaders(200, 0);
                            out.write(server.getJsonEpicById(id).getBytes());
                        } catch (ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Epic epic = server.getEpicFromJson(body);
                    if (epic.getId() == null) {
                        try {
                            server.manager.addEpic(epic);
                            exchange.sendResponseHeaders(200, 0);
                        } catch (IntersectionTimeException | ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        try {
                            server.manager.updateEpic(epic);
                            exchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException | IntersectionTimeException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("/epic")) {
                        try {
                            server.manager.deleteEpics();
                            exchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        Long id = Long.parseLong(path.split("/")[3]);
                        try {
                            server.manager.deleteEpicById(id);
                        } catch (ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
            }
            out.close();
        }
    }

    private String getJsonEpics() {
        List<Epic> epics = manager.getEpicsList();
        return gson.toJson(epics);
    }

    private Epic getEpicFromJson(String body) {
        return gson.fromJson(body, Epic.class);
    }

    private String getJsonEpicById(Long id) throws ManagerSaveException {
        Epic epic = manager.getEpicById(id);
        return gson.toJson(epic);
    }

    static class SubtasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            OutputStream out = exchange.getResponseBody();
            switch (method) {
                case "GET":
                    if (path.endsWith("/subtask")) {
                        exchange.sendResponseHeaders(200, 0);
                        out.write(server.getJsonSubtasks().getBytes());
                    } else {
                        Long id = Long.parseLong(path.split("/")[3]);
                        try {
                            exchange.sendResponseHeaders(200, 0);
                            out.write(server.getJsonSubtaskById(id).getBytes());
                        } catch (ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Subtask subtask = server.getSubtaskFromJson(body);
                    if (subtask.getId() == null) {
                        try {
                            server.manager.addSubtask(subtask);
                            exchange.sendResponseHeaders(200, 0);
                        } catch (IntersectionTimeException | ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        try {
                            server.manager.updateSubtask(subtask);
                            exchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException | IntersectionTimeException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("/subtask")) {
                        try {
                            server.manager.deleteSubtasks();
                            exchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        Long id = Long.parseLong(path.split("/")[3]);
                        try {
                            server.manager.deleteSubtaskById(id);
                        } catch (ManagerSaveException e) {
                            exchange.sendResponseHeaders(400, 0);
                        }
                    }
            }
            out.close();
        }
    }

    private String getJsonSubtasks() {
        List<Subtask> subtasks = manager.getSubtasksList();
        return gson.toJson(subtasks);
    }

    private String getJsonSubtaskById(Long id) throws ManagerSaveException {
        Subtask subtask = manager.getSubtaskById(id);
        return gson.toJson(subtask);
    }

    private Subtask getSubtaskFromJson(String body) {
        return gson.fromJson(body, Subtask.class);
    }

    static class AllTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/subtask/epic запроса от клиента.");

            exchange.sendResponseHeaders(200, 0);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(server.getJsonTasks().getBytes());
                outputStream.write(server.getJsonEpics().getBytes());
                outputStream.write(server.getJsonSubtasks().getBytes());
            }
        }
    }

    static class PrioritizedTasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента.");

            exchange.sendResponseHeaders(200, 0);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(server.getJsonPrioritizedTasks().getBytes());
            }
        }
    }

    private String getJsonPrioritizedTasks() {
        Set<Task> prioritizedTasks = manager.getPrioritizedTasks();
        return gson.toJson(prioritizedTasks);
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Началась обработка /tasks/history запроса от клиента.");

            exchange.sendResponseHeaders(200, 0);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(server.getJsonHistory().getBytes());
            }
        }
    }

    private String getJsonHistory() {
        List<Task> history = manager.getHistory();
        return gson.toJson(history);
    }
}
