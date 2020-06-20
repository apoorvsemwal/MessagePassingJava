import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Master implements Runnable {
    private BlockingQueue<Message> msgQueue;
    Map<String, List<String>> callRecordsMap;
    private Map<String, Person> personThreadMap;

    public Master(Map<String, List<String>> callRecordsMap) {
        this.callRecordsMap = callRecordsMap;
        this.msgQueue = new LinkedBlockingQueue<>();
        this.personThreadMap = new HashMap<>();
    }

    @Override
    public void run() {
        //Create and start thread for each person.
        for (Map.Entry callerWithReceivers : callRecordsMap.entrySet()) {
            String callerPersonName = (String) callerWithReceivers.getKey();
            Person callerPerson = new Person(callerPersonName, personThreadMap);
            Thread personThread = new Thread(callerPerson);
            personThreadMap.put(callerPersonName, callerPerson);
            personThread.start();
        }

        //Initiate message transfer among processes
        for (Map.Entry callerWithReceivers : callRecordsMap.entrySet()) {
            String callerPersonName = (String) callerWithReceivers.getKey();
            personThreadMap.get(callerPersonName).initiateMessageTransfer(callRecordsMap.get(callerPersonName));
        }

        long masterThreadWaitTimeStart = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - masterThreadWaitTimeStart > 10000) {
                System.out.println("\nMaster has received no replies for 10 seconds, ending...\n");
                break;
            }
            //Master acts as a consumer for thread safe message queue.
            //Person threads write messages to this queue and master picks them up.
            while (!msgQueue.isEmpty()) {
                //Take ensures that master would wait if there is no message available in the queue.
                Message msg = null;
                try {
                    msg = msgQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(msg.getReceiver() + " received " + msg.getType() + " message from " + msg.getSender() + " [" + msg.getTimestamp() + "]");
                //Initialize timer for waiting time
                masterThreadWaitTimeStart = System.currentTimeMillis();
            }
        }

    }
}
