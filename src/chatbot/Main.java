package chatbot;

import java.util.Scanner;

/**
 * Temporary console test for the logic layer.
 */
public class Main {
    public static void main(String[] args) {
        AnthropicClient client = new AnthropicClient("YOUR_API_KEY_HERE");
        ConversationHistory history = new ConversationHistory();
        Scanner sc = new Scanner(System.in);

        System.out.println("Type a message (or 'quit'):");
        while (true) {
            String input = sc.nextLine();
            if (input.equals("quit")) break;
            history.add(new ChatMessage("user", input));
            String reply = client.sendMessage(history.getMessages());
            history.add(new ChatMessage("assistant", reply));
            System.out.println("Claude: " + reply);
        }
    }
}