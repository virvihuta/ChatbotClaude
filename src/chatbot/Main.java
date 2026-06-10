package chatbot;

/**
 * Entry point for the Claude Chatbot application.
 */
public class Main {
    public static void main(String[] args) {
        AnthropicClient client = new AnthropicClient();
        ConversationHistory history = new ConversationHistory();
        new ChatFrame(client, history);
    }
}