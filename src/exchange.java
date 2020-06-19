import java.io.*;
import java.util.*;

public class exchange {
    public static void main(String[] args) {
        Map<String, List<String>> callRecordsMap = readCallRecordsAndPrintSummary();
        spawnPersonThreads(callRecordsMap);
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
                    String sender = personNames.remove(0);;
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

    private static void spawnPersonThreads(Map<String, List<String>> callRecordsMap) {
        System.out.println("Pending");
    }
}
