package chatbot;

/**
 * Represents a single message in the conversation.
 */
public class ChatMessage {
    /** The role of the sender: "user" or "assistant". */
    private String role;
    /** The text content of the message. */
    private String content;

    /**
     * Creates a new ChatMessage.
     * @param role    "user" or "assistant"
     * @param content the message text
     */
    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    /** @return the role */
    public String getRole()    { return role; }
    /** @return the content */
    public String getContent() { return content; }
}