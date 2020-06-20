import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Master implements Runnable {
    private BlockingQueue<Message> masterMsgQueue;
    Map<String, List<String>> callRecordsMap;
    private Map<String, Person> personThreadMap;

    public Master(Map<String, List<String>> callRecordsMap) {
        this.callRecordsMap = callRecordsMap;
        this.masterMsgQueue = new LinkedBlockingQueue<>();
        this.personThreadMap = new HashMap<>();
    }

    public BlockingQueue<Message> getMasterMsgQueue() {
        return masterMsgQueue;
    }

    public Map<String, List<String>> getCallRecordsMap() {
        return callRecordsMap;
    }

    public Map<String, Person> getPersonThreadMap() {
        return personThreadMap;
    }

    @Override
    public void run() {
        //Create and start thread for each person.
        for (Map.Entry callerWithReceivers : this.getCallRecordsMap().entrySet()) {
            String callerPersonName = (String) callerWithReceivers.getKey();
            Person callerPerson = new Person(callerPersonName, this.getPersonThreadMap(), this.getMasterMsgQueue());
            Thread personThread = new Thread(callerPerson);
            this.getPersonThreadMap().put(callerPersonName, callerPerson);
            personThread.start();
        }

        //Initiate message transfer among processes
        for (Map.Entry callerWithReceivers : this.getCallRecordsMap().entrySet()) {
            String callerPersonName = (String) callerWithReceivers.getKey();
            this.getPersonThreadMap().get(callerPersonName).initiateMessageTransfer(this.getCallRecordsMap().get(callerPersonName));
        }

        long masterThreadWaitTimeStart = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - masterThreadWaitTimeStart > 10000) {
                System.out.println("\nMaster has received no replies for 10 seconds, ending...\n");
                break;
            }
            //Master acts as a consumer for thread safe message queue.
            //Person threads write messages to this queue and master picks them up.
            while (!this.getMasterMsgQueue().isEmpty()) {
                consumeAndDisplayMessagesFromQueue();
                //Initialize timer for waiting time
                masterThreadWaitTimeStart = System.currentTimeMillis();
            }
        }

    }

    private void consumeAndDisplayMessagesFromQueue() {
        Message msg = null;
        try {
            //Take ensures that master would wait if there is no message available in the queue.
            msg = this.getMasterMsgQueue().take();
            System.out.println(msg.getReceiver() + " received " + msg.getType() + " message from " + msg.getSender() + " [" + msg.getTimestamp() + "]");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
