package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskHandler implements HttpHandler {
    private static final int STATUS_OK = 200;
    private static final int STATUS_NOT_FOUND = 404;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final TaskManager manager;
    private final Gson gson;

    public HttpTaskHandler(TaskManager manager) {
        this.manager = manager;
        gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String method = exchange.getRequestMethod();
        String response = null;

        switch (method) {
            case "GET":
                if (path.endsWith("/tasks/")) {
                    response = gson.toJson(manager.getPrioritizedTasks());
                    exchange.sendResponseHeaders(STATUS_OK, 0);
                }

                if (path.endsWith("/history")) {
                    response = gson.toJson(manager.getHistory());
                    exchange.sendResponseHeaders(STATUS_OK, 0);
                }

                if (path.contains("/subtask/epic")) {
                    Long id = getIdFromUrlParameter(exchange);
                    try {
                        response = gson.toJson(manager.getEpicById(id).getSubtask().values());
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        exchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
                    }
                }

                if (path.endsWith("/task/") && uri.getRawQuery() == null) {
                    response = gson.toJson(manager.getTasksList());
                    exchange.sendResponseHeaders(STATUS_OK, 0);
                }

                if (path.contains("/task/") && uri.getRawQuery() != null) {
                    Long id = getIdFromUrlParameter(exchange);
                    try {
                        response = gson.toJson(manager.getTaskById(id));
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        exchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
                    }
                }

                if (path.endsWith("/subtask/") && uri.getRawQuery() == null) {
                    response = gson.toJson(manager.getSubtasksList());
                    exchange.sendResponseHeaders(STATUS_OK, 0);
                }

                if (path.contains("/subtask/") && uri.getRawQuery() != null) {
                    Long id = getIdFromUrlParameter(exchange);
                    try {
                        response = gson.toJson(manager.getSubtaskById(id));
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        exchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
                    }
                }

                if (path.endsWith("/epic/") && uri.getRawQuery() == null) {
                    response = gson.toJson(manager.getEpicsList());
                    exchange.sendResponseHeaders(STATUS_OK, 0);
                }

                if (path.contains("/epic/") && uri.getRawQuery() != null) {
                    Long id = getIdFromUrlParameter(exchange);
                    try {
                        response = gson.toJson(manager.getSubtaskById(id));
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        exchange.sendResponseHeaders(STATUS_NOT_FOUND, 0);
                    }
                }
                break;
            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

                if (path.contains("/task/")) {
                    Task task = gson.fromJson(body, Task.class);
                    if (task.getId() == null) {
                        try {
                            manager.addTask(task);
                            exchange.sendResponseHeaders(STATUS_OK, 0);
                        } catch (ManagerSaveException | IntersectionTimeException | InterruptedException e) {
                            exchange.sendResponseHeaders(STATUS_BAD_REQUEST, 0);
                        }
                    } else {
                        try {
                            manager.updateTask(task);
                            exchange.sendResponseHeaders(STATUS_OK, 0);
                        } catch (IntersectionTimeException | ManagerSaveException | InterruptedException e) {
                            exchange.sendResponseHeaders(STATUS_BAD_REQUEST, 0);
                        }
                    }
                }

                if (path.contains("/subtask/")) {
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (subtask.getId() == null) {
                        try {
                            manager.addSubtask(subtask);
                            exchange.sendResponseHeaders(STATUS_OK, 0);
                        } catch (ManagerSaveException | IntersectionTimeException | InterruptedException e) {
                            exchange.sendResponseHeaders(STATUS_BAD_REQUEST, 0);
                        }
                    } else {
                        try {
                            manager.updateSubtask(subtask);
                        } catch (IntersectionTimeException | ManagerSaveException | InterruptedException e) {
                            exchange.sendResponseHeaders(STATUS_BAD_REQUEST, 0);
                        }
                    }
                }

                if (path.contains("/epic/")) {
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (epic.getId() == null) {
                        try {
                            manager.addEpic(epic);
                            exchange.sendResponseHeaders(STATUS_OK, 0);
                        } catch (ManagerSaveException | IntersectionTimeException | InterruptedException e) {
                            exchange.sendResponseHeaders(STATUS_BAD_REQUEST, 0);
                        }
                    } else {
                        try {
                            manager.updateEpic(epic);
                            exchange.sendResponseHeaders(STATUS_OK, 0);
                        } catch (IntersectionTimeException | ManagerSaveException | InterruptedException e) {
                            exchange.sendResponseHeaders(STATUS_BAD_REQUEST, 0);
                        }
                    }
                }
                break;
            case "DELETE":
                if (path.endsWith("/task/") && uri.getRawQuery() == null) {
                    try {
                        manager.deleteTasks();
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (path.contains("/task/") && uri.getRawQuery() != null) {
                    Long id = getIdFromUrlParameter(exchange);
                    try {
                        manager.deleteTaskById(id);
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (path.endsWith("/subtask/)") && uri.getRawQuery() == null) {
                    try {
                        manager.deleteSubtasks();
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (path.contains("/subtask/") && uri.getRawQuery() != null) {
                    Long id = getIdFromUrlParameter(exchange);
                    try {
                        manager.deleteSubtaskById(id);
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (path.endsWith("/epic/)") && uri.getRawQuery() == null) {
                    try {
                        manager.deleteEpics();
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (path.contains("/epic/") && uri.getRawQuery() != null) {
                    Long id = getIdFromUrlParameter(exchange);
                    try {
                        manager.deleteEpicById(id);
                        exchange.sendResponseHeaders(STATUS_OK, 0);
                    } catch (ManagerSaveException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                exchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, 0);
        }

        try (OutputStream os = exchange.getResponseBody()) {
            if (response != null) {
                os.write(response.getBytes());
            }
        }
    }

    private Long getIdFromUrlParameter(HttpExchange exchange) {
        return Long.parseLong(exchange.getRequestURI().getRawQuery().split("\\=")[1]);
    }
}
