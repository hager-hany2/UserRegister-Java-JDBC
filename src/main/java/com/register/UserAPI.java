package com.register;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserAPI {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        UserRepository userRepository = new UserRepository();
        userRepository.createTableIfNotExists();

        server.createContext("/users", (HttpExchange exchange) -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            String method = exchange.getRequestMethod();

            try {
                // ===================== GET =====================
            	if (method.equals("GET")) {
            	    String path = exchange.getRequestURI().getPath(); 
            	    String[] parts = path.split("/");

            	    if (parts.length == 3) { 
            	        int id = Integer.parseInt(parts[2]);
            	        JSONObject user = userRepository.getUserById(id);
            	        if (user != null) {
            	            sendResponse(exchange, 200, user.toString());
            	        } else {
            	            sendResponse(exchange, 404, "{\"message\":\"User not found\"}");
            	        }
            	    } else { 
            	        JSONArray users = userRepository.getAllUsers();
            	        sendResponse(exchange, 200, users.toString());
            	    }
            	}

                // ===================== POST =====================
                else if (method.equals("POST")) {
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    JSONObject json = new JSONObject(body);

                    String username = json.getString("username");
                    String email = json.getString("email");
                    String password = json.getString("password");

                    userRepository.save(new User(username, email, password));

                    sendResponse(exchange, 201, "{\"message\":\"User created\"}");
                }

                // ===================== PUT / PATCH =====================
                else if (method.equals("PUT") || method.equals("PATCH")) {
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    JSONObject json = new JSONObject(body);

                    int id = json.getInt("id");
                    String username = json.optString("username", null);
                    String email = json.optString("email", null);

                    boolean updated = userRepository.update(id, username, email);

                    sendResponse(exchange, 200,
                            updated ? "{\"message\":\"User updated\"}"
                                    : "{\"message\":\"User not found\"}");
                }

                // ===================== DELETE =====================
                else if (method.equals("DELETE")) {
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    JSONObject json = new JSONObject(body);

                    int id = json.getInt("id");

                    boolean deleted = userRepository.delete(id);

                    sendResponse(exchange, 200,
                            deleted ? "{\"message\":\"User deleted\"}"
                                    : "{\"message\":\"User not found\"}");
                }

                // ===================== METHOD NOT ALLOWED =====================
                else {
                    sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                }

            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Internal Server Error\"}");
            }
        });

        server.start();
        System.out.println("🚀 Server running on port 8000");
    }
//         responsive
        private static void sendResponse(HttpExchange ex, int code, String res) throws IOException {
        ex.sendResponseHeaders(code, res.getBytes().length);
        OutputStream os = ex.getResponseBody();
        os.write(res.getBytes());
        os.close();
    }
}