package chatbot;

import java.net.URI;
import java.net.http.*;
import java.util.List;

/**
 * Handles communication with the Anthropic Claude API.
 */
public class AnthropicClient {
    /** Your Anthropic API key loaded from .env. */
    private String apiKey;
    /** The Claude model to use. */
    private String model = "claude-haiku-4-5";

    /**
     * Creates a new client, loading the API key from .env file.
     */
    public AnthropicClient() {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.FileReader(".env")
            );
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ANTHROPIC_API_KEY=")) {
                    this.apiKey = line.substring("ANTHROPIC_API_KEY=".length()).trim();
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not load .env file: " + e.getMessage());
        }
    }

    /**
     * Sends the conversation history to Claude and returns the reply.
     * @param messages the full conversation so far
     * @return Claude's response as a plain string
     */
    public String sendMessage(List<ChatMessage> messages) {
        try {
            StringBuilder messagesJson = new StringBuilder("[");
            for (int i = 0; i < messages.size(); i++) {
                ChatMessage m = messages.get(i);
                messagesJson.append("{\"role\":\"").append(m.getRole())
                        .append("\",\"content\":\"")
                        .append(m.getContent().replace("\"", "\\\"").replace("\n", "\\n"))
                        .append("\"}");
                if (i < messages.size() - 1) messagesJson.append(",");
            }
            messagesJson.append("]");

            String body = "{\"model\":\"" + model + "\","
                    + "\"max_tokens\":1024,"
                    + "\"messages\":" + messagesJson + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.anthropic.com/v1/messages"))
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String resp = response.body();
            int start = resp.indexOf("\"text\":\"") + 8;
            int end   = resp.indexOf("\"", start);
            return resp.substring(start, end).replace("\\n", "\n");

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}