public class Message {
    private String sender;
    private String receiver;
    private String timestamp;
    private String type;

    public Message(String sender, String receiver, String timestamp, String type) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String text) {
        this.type = type;
    }
}
