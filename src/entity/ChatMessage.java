package entity;

public class ChatMessage {
    // Текстсообщения
    private String message;

    private ChatUser author;


    public ChatMessage(String message, ChatUser author, long timestamp) {
        super();
        this.message= message;
        this.author= author;

    }
    public String getMessage() {

        return message;
    }
    public void setMessage(String message)
    {
        this.message= message;
    }
    public ChatUser getAuthor() {

        return author;
    }
    public void setAuthor(ChatUser author) {
        this.author= author;
    }



}