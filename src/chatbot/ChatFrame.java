package chatbot;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI window for the Claude chatbot.
 */
public class ChatFrame extends JFrame {
    /** Displays the full conversation. */
    private JTextArea chatArea;
    /** Input field for the user's message. */
    private JTextField inputField;
    /** Sends messages to the Anthropic API. */
    private AnthropicClient client;
    /** Stores the conversation history. */
    private ConversationHistory history;

    /**
     * Builds the chat window.
     * @param client  the Anthropic API client
     * @param history the conversation history
     */
    public ChatFrame(AnthropicClient client, ConversationHistory history) {
        this.client  = client;
        this.history = history;

        setTitle("Claude Chatbot");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton sendBtn = new JButton("Send");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn,    BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        add(inputPanel, BorderLayout.SOUTH);

        sendBtn.addActionListener(e -> onSend());
        inputField.addActionListener(e -> onSend());

        setVisible(true);
    }

    /**
     * Handles the send action: adds user message, calls API, displays reply.
     */
    public void onSend() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        appendMessage("You", text);
        inputField.setText("");

        history.add(new ChatMessage("user", text));

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            protected String doInBackground() {
                return client.sendMessage(history.getMessages());
            }
            protected void done() {
                try {
                    String reply = get();
                    history.add(new ChatMessage("assistant", reply));
                    appendMessage("Claude", reply);
                } catch (Exception e) {
                    appendMessage("Error", e.getMessage());
                }
            }
        };
        worker.execute();
    }

    /**
     * Appends a labeled message to the chat display.
     * @param sender  the name shown before the message
     * @param message the message text
     */
    public void appendMessage(String sender, String message) {
        chatArea.append(sender + ": " + message + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}