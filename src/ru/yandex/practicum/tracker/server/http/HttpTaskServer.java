package ru.yandex.practicum.tracker.server.http;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.service.managers.Managers;
import ru.yandex.practicum.tracker.service.managers.task.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    private static TaskManager manager;

    /**
     * Метод для запуска сервера
     *
     * @throws IOException
     */
    public static void start() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();
        manager = Managers.getDefaultFileBacked();

    }

    public static void main(String[] args) throws IOException {
        start();
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI(), exchange.getRequestMethod());
            switch (endpoint) {
                case GET_TASKS:
                    handleGetTasks(exchange);
                    return;
                case GET_SUBTASKS:
                    handleGetSubTasks(exchange);
                    return;
                case GET_EPICS:
                    handleGetEpics(exchange);
                    return;
                case GET_ALL:
                    handleGetAll(exchange);
                    return;
                case GET_TASK_BY_ID:
                    handleGetTaskById(exchange);
                    return;
                case GET_SUBTASK_BY_ID:
                    handleGetSubTaskById(exchange);
                    return;
                case GET_EPIC_BY_ID:
                    handleGetEpicById(exchange);
                    return;
                case CREATE_TASK:
                    handleCreateTask(exchange);
                    return;
                case CREATE_SUBTASK:
                    handleCreateSubTask(exchange);
                    return;
                case CREATE_EPIC:
                    handleCreateEpic(exchange);
                    return;
                case DELETE_TASKS:
                    handleDeleteTasks(exchange);
                    return;
                case DELETE_EPICS:
                    handleDeleteEpics(exchange);
                    return;
                case DELETE_SUBTASKS:
                    handleDeleteSubTasks(exchange);
                    return;
                case UPDATE_TASK:
                    handleUpdateTask(exchange);
                    return;
                case UPDATE_SUBTASK:
                    handleUpdateSubTask(exchange);
                    return;
                case UPDATE_EPIC:
                    handleUpdateEpic(exchange);
                    return;
                case DELETE_TASK_BY_ID:
                    handleDeleteTaskById(exchange);
                    return;
                case DELETE_EPIC_BY_ID:
                    handleDeleteEpicById(exchange);
                    return;
                case DELETE_SUBTASK_BY_ID:
                    handleDeleteSubTaskById(exchange);
                    return;
                case GET_EPIC_SUBS:
                    handleGetEpicSubs(exchange);
                    return;
                case GET_HISTORY:
                    handleGetHistory(exchange);
                    return;
                case GET_PRIORITY:
                    handleGetPriority(exchange);
                    return;
                default:
                    writeResponse(exchange, "Несуществующий эндпоинт", 404);

            }

        }

        /**
         * Метод для получения эндпоинта по пути и методу запроса
         *
         * @param url
         * @param method
         * @return Endpoint
         */
        private Endpoint getEndpoint(URI url, String method) {
            String[] parts = url.getPath().split("/");
            Optional<String> params;
            if (url.getRawQuery() != null) {
                params = Optional.of(url.getRawQuery());
            } else {
                params = Optional.empty();
            }

            if (parts.length == 2) {
                return Endpoint.GET_ALL;
            } else {
                switch (method) {
                    case "GET":
                        if (parts.length == 3 && params.isEmpty()) {
                            if (parts[2].equals("task")) {
                                return Endpoint.GET_TASKS;
                            } else if (parts[2].equals("subtask")) {
                                return Endpoint.GET_SUBTASKS;
                            } else if (parts[2].equals("epic")) {
                                return Endpoint.GET_EPICS;
                            } else if (parts[2].equals("history")) {
                                return Endpoint.GET_HISTORY;
                            } else if (parts[2].equals("priority")) {
                                return Endpoint.GET_PRIORITY;
                            } else {
                                return Endpoint.UNKNOWN;
                            }
                        } else if (parts.length == 3 && params.isPresent()) {
                            if (parts[2].equals("task")) {
                                return Endpoint.GET_TASK_BY_ID;
                            } else if (parts[2].equals("subtask")) {
                                return Endpoint.GET_SUBTASK_BY_ID;
                            } else if (parts[2].equals("epic")) {
                                return Endpoint.GET_EPIC_BY_ID;
                            } else {
                                return Endpoint.UNKNOWN;
                            }
                        } else if (parts.length == 4 && params.isPresent()) {
                            return Endpoint.GET_EPIC_SUBS;
                        } else {
                            return Endpoint.UNKNOWN;
                        }
                    case "POST":
                        if (parts.length == 3 && params.isEmpty()) {
                            if (parts[2].equals("task")) {
                                return Endpoint.CREATE_TASK;
                            } else if (parts[2].equals("epic")) {
                                return Endpoint.CREATE_EPIC;
                            } else if (parts[2].equals("subtask")) {
                                return Endpoint.CREATE_SUBTASK;
                            } else {
                                return Endpoint.UNKNOWN;
                            }
                        } else if (parts.length == 3 && params.isPresent()) {
                            if (parts[2].equals("task")) {
                                return Endpoint.UPDATE_TASK;
                            } else if (parts[2].equals("subtask")) {
                                return Endpoint.UPDATE_SUBTASK;
                            } else if (parts[2].equals("epic")) {
                                return Endpoint.UPDATE_EPIC;
                            } else {
                                return Endpoint.UNKNOWN;
                            }
                        } else {
                            return Endpoint.UNKNOWN;
                        }
                    case "DELETE":
                        if (parts.length == 3 && params.isEmpty()) {
                            if (parts[2].equals("task")) {
                                return Endpoint.DELETE_TASKS;
                            } else if (parts[2].equals("subtask")) {
                                return Endpoint.DELETE_SUBTASKS;
                            } else if (parts[2].equals("epic")) {
                                return Endpoint.DELETE_EPICS;
                            } else {
                                return Endpoint.UNKNOWN;
                            }
                        } else if (parts.length == 3 && params.isPresent()) {
                            if (parts[2].equals("task")) {
                                return Endpoint.DELETE_TASK_BY_ID;
                            } else if (parts[2].equals("subtask")) {
                                return Endpoint.DELETE_SUBTASK_BY_ID;
                            } else if (parts[2].equals("epic")) {
                                return Endpoint.DELETE_EPIC_BY_ID;
                            } else {
                                return Endpoint.UNKNOWN;
                            }
                        } else {
                            return Endpoint.UNKNOWN;
                        }
                    default:
                        return Endpoint.UNKNOWN;
                }
            }
        }

        /**
         * Метод для получения id запрашиваемой или обновляемой задачи
         *
         * @param exchange
         * @return Optional<Integer> id
         */
        private Optional<Integer> getIdParam(HttpExchange exchange) {
            String param = exchange.getRequestURI().getRawQuery();
            String[] parts = param.split("=");
            String id = parts[1];
            try {
                return Optional.of(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }

        /**
         * Метод для написания ответа на запрос
         *
         * @param exchange
         * @param response
         * @param responseCode
         * @throws IOException
         */
        private void writeResponse(HttpExchange exchange, String response, int responseCode) throws IOException {
            if (response.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = response.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        /**
         * Обработчик запроса всех задач, подзадач, эпиков
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetAll(HttpExchange exchange) throws IOException {
            List<Task> allTasks = new ArrayList<>();
            allTasks.addAll(manager.getTasks());
            allTasks.addAll(manager.getSubTasks());
            allTasks.addAll(manager.getEpics());
            String response = gson.toJson(allTasks);
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса задач
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetTasks(HttpExchange exchange) throws IOException {
            String response = gson.toJson(manager.getTasks());
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса подзадач
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetSubTasks(HttpExchange exchange) throws IOException {
            String response = gson.toJson(manager.getSubTasks());
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса эпиков
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetEpics(HttpExchange exchange) throws IOException {
            String response = gson.toJson(manager.getEpics());
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса задачи по ее id
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getTaskById(id.get()) == null) {
                writeResponse(exchange, "Задачи с id " + id.get() + " не найдено", 404);
                return;
            }
            String response = gson.toJson(manager.getTaskById(id.get()));
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса подзадачи по ее id
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetSubTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getSubTaskById(id.get()) == null) {
                writeResponse(exchange, "Подзадачи с id " + id.get() + " не найдено", 404);
                return;
            }
            String response = gson.toJson(manager.getSubTaskById(id.get()));
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса эпика по его id
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetEpicById(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getEpicById(id.get()) == null) {
                writeResponse(exchange, "Эпика с id " + id.get() + " не найдено", 404);
                return;
            }
            String response = gson.toJson(manager.getEpicById(id.get()));
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса подзадач конкретного эпика по его id
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetEpicSubs(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getEpicById(id.get()) == null) {
                writeResponse(exchange, "Эпика с id " + id.get() + " не найдено", 404);
                return;
            }
            if (manager.getEpicById(id.get()).getSubTasks().isEmpty()) {
                writeResponse(exchange, "У запрашиваемого эпика не найдено подзадач", 404);
            }
            String response = gson.toJson(manager.getEpicById(id.get()).getSubTasks());
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса истории
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetHistory(HttpExchange exchange) throws IOException {
            String response = gson.toJson(manager.getHistory());
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса задач в порядке приоритетности
         *
         * @param exchange
         * @throws IOException
         */
        public void handleGetPriority(HttpExchange exchange) throws IOException {
            String response = gson.toJson(manager.getPrioritizedTasks());
            writeResponse(exchange, response, 200);
        }

        /**
         * Обработчик запроса удаления всех задач
         *
         * @param exchange
         * @throws IOException
         */
        public void handleDeleteTasks(HttpExchange exchange) throws IOException {
            manager.deleteTasks();
            writeResponse(exchange, "Задачи успешно удалены", 200);
        }

        /**
         * Обработчик запроса удаления всех эпиков
         *
         * @param exchange
         * @throws IOException
         */
        public void handleDeleteEpics(HttpExchange exchange) throws IOException {
            manager.deleteEpics();
            writeResponse(exchange, "Эпики успешно удалены", 200);
        }

        /**
         * Обработчик запроса удаления всех подзадач
         *
         * @param exchange
         * @throws IOException
         */
        public void handleDeleteSubTasks(HttpExchange exchange) throws IOException {
            manager.deleteSubTasks();
            writeResponse(exchange, "Подзадачи успешно удалены", 200);
        }

        /**
         * Обработчик запроса удаления задачи по ее id
         *
         * @param exchange
         * @throws IOException
         */
        public void handleDeleteTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getTaskById(id.get()) == null) {
                writeResponse(exchange, "Задачи с id " + id.get() + " не найдено", 404);
                return;
            }
            manager.deleteTaskById(id.get());
            writeResponse(exchange, "Задача успешно удалена", 200);
        }

        /**
         * Обработчик запроса удаления подзадачи по ее id
         *
         * @param exchange
         * @throws IOException
         */
        public void handleDeleteSubTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getSubTaskById(id.get()) == null) {
                writeResponse(exchange, "Подзадачи с id " + id.get() + " не найдено", 404);
                return;
            }
            manager.deleteSubTaskById(id.get());
            writeResponse(exchange, "Подзадача успешно удалена", 200);
        }

        /**
         * Обработчик запроса удаления эпика по его id
         *
         * @param exchange
         * @throws IOException
         */
        public void handleDeleteEpicById(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getEpicById(id.get()) == null) {
                writeResponse(exchange, "Эпика с id " + id.get() + " не найдено", 404);
                return;
            }
            manager.deleteEpicById(id.get());
            writeResponse(exchange, "Эпик успешно удален", 200);
        }

        /**
         * Обработчик запроса создания задачи
         *
         * @param exchange
         * @throws IOException
         */
        public void handleCreateTask(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String request = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task task;
            try {
                task = gson.fromJson(request, Task.class);
            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            manager.createTask(task.getName(), task.getDetails(), task.getDuration().toMinutes(), task.getStartTime().get().toString());
            writeResponse(exchange, "Задача успешно создана", 201);
        }

        /**
         * Обработчик запроса создания эпика
         *
         * @param exchange
         * @throws IOException
         */
        public void handleCreateEpic(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String request = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic epic;
            try {
                epic = gson.fromJson(request, Epic.class);
            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            manager.createEpic(epic.getName());
            writeResponse(exchange, "Эпик успешно создан", 201);
        }

        /**
         * Обработчик запроса создания подзадачи
         *
         * @param exchange
         * @throws IOException
         */
        public void handleCreateSubTask(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String request = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            SubTask subTask;
            try {
                subTask = gson.fromJson(request, SubTask.class);
            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            manager.createSubTask(subTask.getEpicName(), subTask.getName(), subTask.getDuration().toMinutes(), subTask.getStartTime().get().toString());
            writeResponse(exchange, "Подзадача успешно создана", 201);
        }

        /**
         * Обработчик запроса обновления задачи
         *
         * @param exchange
         * @throws IOException
         */
        public void handleUpdateTask(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String request = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task task;
            try {
                task = gson.fromJson(request, Task.class);
            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getTaskById(id.get()) == null) {
                writeResponse(exchange, "Задачи с id " + id.get() + " не найдено", 404);
                return;
            }
            manager.updateTask(task, id.get());
            writeResponse(exchange, "Задача успешно обновлена", 201);
        }

        /**
         * Обработчик запроса обновления подзадачи
         *
         * @param exchange
         * @throws IOException
         */
        public void handleUpdateSubTask(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String request = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            SubTask subTask;
            try {
                subTask = gson.fromJson(request, SubTask.class);
            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getSubTaskById(id.get()) == null) {
                writeResponse(exchange, "Подзадачи с id " + id.get() + " не найдено", 404);
                return;
            }
            manager.updateSubTask(subTask, subTask.getEpicName(), id.get());
            writeResponse(exchange, "Подзадача успешно обновлена", 201);
        }

        /**
         * Обработчик запроса обновления эпика
         *
         * @param exchange
         * @throws IOException
         */
        public void handleUpdateEpic(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String request = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic epic;
            try {
                epic = gson.fromJson(request, Epic.class);
            } catch (JsonSyntaxException e) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            Optional<Integer> id = getIdParam(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный id", 400);
                return;
            }
            if (manager.getEpicById(id.get()) == null) {
                writeResponse(exchange, "Эпика с id " + id.get() + " не найдено", 404);
                return;
            }
            manager.updateEpic(epic, id.get());
            writeResponse(exchange, "Эпик успешно обновлен", 201);
        }
    }
}

