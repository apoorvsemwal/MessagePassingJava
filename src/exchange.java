import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class exchange {
    public static void main(String[] args) {
        Map<String, List<String>> callRecordsMap = readCallRecordsAndPrintSummary();
        BlockingQueue<Message> msgQueue = new LinkedBlockingQueue<Message>();
        spawnMasterThread(callRecordsMap);
    }

    private static void spawnMasterThread(Map<String, List<String>> callRecordsMap) {
        Master master = new Master(callRecordsMap);
        new Thread(master).start();
    }

    public static Map<String, List<String>> readCallRecordsAndPrintSummary() {
        System.out.println("** Calls to be made **");
        Map<String, List<String>> callRecords = new HashMap<>();
        File file = new File("calls.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String callRecord;
            try {
                while ((callRecord = br.readLine()) != null) {
                    callRecord = callRecord.replaceAll("[\\{\\}\\[\\].]", "");
                    List<String> personNames = new LinkedList<>(Arrays.asList(callRecord.split("\\s*,\\s*")));
                    String sender = personNames.remove(0);
                    ;
                    callRecords.put(sender, personNames);
                    System.out.println(sender + ": " + Arrays.toString(personNames.toArray()));
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        return callRecords;
    }

}
