package ru.yandex.practicum.tracker.server.kv.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class KVTaskClient {

    HttpClient client;
    String url;
    String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                apiToken = jsonElement.toString();
            } else {
                System.out.println("Ошибка, сервер вернул код" + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка во время выполнения запроса. Проверьте адрес и повторите попытку");
        }
    }

    /**
     * Метод для сохранения состояния менеджера задач на сервер
     *
     * @param key
     * @param jsonManager
     */
    public void put(String key, String jsonManager) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonManager))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Ошибка, сервер вернул код" + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка во время выполнения запроса. Проверьте адрес и повторите попытку");
        }
    }

    /**
     * Метод для восстановления состояния менеджера задач с сервера
     *
     * @param key
     * @return
     */
    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();
        Optional<String> manager;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                manager = Optional.of(jsonElement.toString());

            } else {
                System.out.println("Ошибка, сервер вернул код" + response.statusCode());
                manager = Optional.empty();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка во время выполнения запроса. Проверьте адрес и повторите попытку");
            manager = Optional.empty();

        }

        return manager.orElse("{}");

    }
}

