package ru.yandex.practicum.tracker.test.server;

import com.google.gson.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tracker.model.Epic;
import ru.yandex.practicum.tracker.model.SubTask;
import ru.yandex.practicum.tracker.model.Task;
import ru.yandex.practicum.tracker.server.http.HttpTaskServer;
import ru.yandex.practicum.tracker.server.kv.server.KVServer;
import ru.yandex.practicum.tracker.service.managers.task.http.HttpTaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    static HttpTaskManager manager;
    Gson gson = new Gson();

    public static void initTask() {
        manager.createTask("task1", null, 0, "2022-09-01T12:15");
        manager.createTask("task2", null, 0, "2022-08-25T12:19");
        manager.createTask("task3", null, 0, "2022-09-04T12:15");
        manager.createEpic("Получить права");
        manager.createSubTask("Получить права", "Пройти обучение в автошколе", 1300, "2022-09-03T12:15");
        manager.createSubTask("Получить права", "сдать экзамен", 180, "2022-09-07T12:15");
        manager.createSubTask("Получить права", "отпраздновать", 120, "2022-09-11T12:15");
        manager.createEpic("kakoi-to ewe epic");
    }

    @BeforeAll
    public static void launchKV() throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();
        manager = new HttpTaskManager("http://localhost:8078");
        initTask();

    }

    @Test
    public void shouldCreateAndGetAndUpdateAndDeleteTasksAndGetHistoryAndGetPriority() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(manager.getTaskById(1));
        URI url2 = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertTrue(response.statusCode() == 201);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).header("Accept", "application/json").GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response2.body().toString());
        Task task = gson.fromJson(jsonElement, Task.class);

        assertEquals(manager.getTaskById(1), task);


        URI url3 = URI.create("http://localhost:8080/tasks/epic");
        json = gson.toJson(manager.getEpicById(4));
        URI url4 = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertTrue(response3.statusCode() == 201);
        HttpRequest request4 = HttpRequest.newBuilder().uri(url4).header("Accept", "application/json").GET().build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response4.body().toString());
        Epic epic = gson.fromJson(jsonElement, Epic.class);

        assertTrue(epic.getName().equals("Получить права"));

        URI url5 = URI.create("http://localhost:8080/tasks/subtask");
        json = gson.toJson(manager.getSubTaskById(5));
        URI url6 = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request5 = HttpRequest.newBuilder().uri(url5).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        assertTrue(response5.statusCode() == 201);
        HttpRequest request6 = HttpRequest.newBuilder().uri(url6).header("Accept", "application/json").GET().build();
        HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response6.body().toString());
        SubTask subTask = gson.fromJson(jsonElement, SubTask.class);
        assertTrue(subTask.getEpicName().equals("Получить права"));

        URI url20 = URI.create("http://localhost:8080/tasks/epic/subtask/?id=2");
        HttpRequest request20 = HttpRequest.newBuilder().uri(url20).GET().build();
        HttpResponse<String> response20 = client.send(request20, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response20.body().toString());
        List<Integer> list = gson.fromJson(jsonElement, ArrayList.class);
        assertTrue(list.size() == 1);

        URI url11 = URI.create("http://localhost:8080/tasks/task/?id=1");
        json = gson.toJson(manager.getTaskById(3));
        HttpRequest request11 = HttpRequest.newBuilder().uri(url11).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response11 = client.send(request11, HttpResponse.BodyHandlers.ofString());
        assertTrue(response11.statusCode() == 201);
        URI url12 = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request12 = HttpRequest.newBuilder().uri(url12).GET().build();
        HttpResponse<String> response12 = client.send(request12, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response12.body().toString());
        task = gson.fromJson(jsonElement, Task.class);
        assertTrue(task.getName().equals("task3"));

        URI url13 = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request13 = HttpRequest.newBuilder().uri(url13).GET().build();
        HttpResponse<String> response13 = client.send(request13, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response13.body().toString());
        list = gson.fromJson(jsonElement.getAsJsonArray(), ArrayList.class);
        assertTrue(response13.statusCode() == 200);
        assertFalse(list.isEmpty());

        URI url14 = URI.create("http://localhost:8080/tasks/priority");
        HttpRequest request14 = HttpRequest.newBuilder().uri(url14).GET().build();
        HttpResponse<String> response14 = client.send(request14, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response14.body().toString());
        list = gson.fromJson(jsonElement, ArrayList.class);
        assertTrue(response13.statusCode() == 200);
        assertFalse(list.isEmpty());

        URI url15 = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request15 = HttpRequest.newBuilder().uri(url15).DELETE().build();
        HttpResponse<String> response15 = client.send(request15, HttpResponse.BodyHandlers.ofString());
        assertTrue(response15.statusCode() == 200);

        URI url7 = URI.create("http://localhost:8080/tasks/task/?id=1111");
        URI url8 = URI.create("http://localhost:8080/tasks/subtask");
        URI url9 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request7 = HttpRequest.newBuilder().uri(url7).DELETE().build();
        HttpResponse<String> response7 = client.send(request7, HttpResponse.BodyHandlers.ofString());
        assertTrue(response7.statusCode() == 404);
        HttpRequest request8 = HttpRequest.newBuilder().uri(url8).header("Accept", "application/json").DELETE().build();
        HttpResponse<String> response8 = client.send(request8, HttpResponse.BodyHandlers.ofString());
        assertTrue(response8.statusCode() == 200);
        HttpRequest request9 = HttpRequest.newBuilder().uri(url9).header("Accept", "application/json").DELETE().build();
        HttpResponse<String> response9 = client.send(request9, HttpResponse.BodyHandlers.ofString());
        assertTrue(response9.statusCode() == 200);
        URI url10 = URI.create("http://localhost:8080/tasks/");
        HttpRequest request10 = HttpRequest.newBuilder().uri(url10).GET().build();
        HttpResponse<String> response10 = client.send(request10, HttpResponse.BodyHandlers.ofString());


        assertTrue(response10.body().length() == 2);

    }

}