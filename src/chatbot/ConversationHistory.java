package chatbot;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the full conversation history for multi-turn chat.
 */
public class ConversationHistory {
    /** List of all messages exchanged so far. */
    private List<ChatMessage> messages = new ArrayList<>();

    /**
     * Adds a message to the history.
     * @param msg the message to add
     */
    public void add(ChatMessage msg) {
        messages.add(msg);
    }

    /**
     * Returns all messages in the conversation.
     * @return list of messages
     */
    public List<ChatMessage> getMessages() {
        return messages;
    }
}