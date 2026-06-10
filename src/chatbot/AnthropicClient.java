package chatbot;

import java.net.URI;
import java.net.http.*;
import java.util.List;

/**
 * Handles communication with the Anthropic Claude API.
 */
public class AnthropicClient {
    /** Your Anthropic API key. */
    private String apiKey;
    /** The Claude model to use. */
    private String model = "claude-haiku-4-5";

    /**
     * Creates a new client with the given API key.
     * @param apiKey your Anthropic API key
     */
    public AnthropicClient(String apiKey) {
        this.apiKey = apiKey;
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