import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Person implements Runnable {

    private BlockingQueue<Message> masterMsgQueue;
    private BlockingQueue<Message> personMsgQueue;
    private Map<String, Person> personThreadMap;
    String personName;

    public Person(String personName, Map<String, Person> personThreadMap, BlockingQueue<Message> masterMsgQueue) {
        this.personName = personName;
        this.personThreadMap = personThreadMap;
        this.masterMsgQueue = masterMsgQueue;
        this.personMsgQueue = new LinkedBlockingQueue<>();
    }

    public BlockingQueue<Message> getMasterMsgQueue() {
        return masterMsgQueue;
    }

    public Map<String, Person> getPersonThreadMap() {
        return personThreadMap;
    }

    public BlockingQueue<Message> getPersonMsgQueue() {
        return personMsgQueue;
    }

    public String getPersonName() {
        return personName;
    }

    @Override
    public void run() {
        long personThreadWaitTimeStart = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - personThreadWaitTimeStart > 5000) {
                System.out.println("\nProcess " + this.getPersonName() + " has recieved no calls for 5 second, ending...");
                break;
            }
            while (!this.getPersonMsgQueue().isEmpty()) {
                sendReplyMessage();
                personThreadWaitTimeStart = System.currentTimeMillis();
            }
        }
    }

    public void initiateMessageTransfer(List<String> receivers) {
        for (String receiverName : receivers) {
            Person receiver = this.getPersonThreadMap().get(receiverName);
            sendIntroMessage(this.getPersonName(), receiver);
        }
    }

    private void sendIntroMessage(String personName, Person receiver) {
        waitForARandomPeriod();
        long msgTimeStamp = System.currentTimeMillis();
        Message msg = new Message(personName, receiver.getPersonName(), msgTimeStamp, "intro");
        this.getMasterMsgQueue().add(msg);
        receiver.getPersonMsgQueue().add(msg);
    }

    private void sendReplyMessage() {
        waitForARandomPeriod();
        Message introMsg = null;
        try {
            introMsg = this.getPersonMsgQueue().take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message msg = new Message(this.getPersonName(), introMsg.getSender(), introMsg.getTimestamp(), "reply");
        this.getMasterMsgQueue().add(msg);
    }

    private void waitForARandomPeriod() {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
            System.out.print("");
        }
    }


}
