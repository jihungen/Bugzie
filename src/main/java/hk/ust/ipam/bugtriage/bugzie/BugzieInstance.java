package hk.ust.ipam.bugtriage.bugzie;

import java.io.*;
import java.util.*;

/**
 * Created by jeehoonyoo on 16/4/16.
 */
public class BugzieInstance implements Comparable<BugzieInstance> {
    private int id;
    private String assignee;
    private Set<String> terms;

    public BugzieInstance(String content, String separator, int idxID, int idxAssignee) {
        this.terms = new HashSet<String>();

        String[] contents = content.split(separator);
        for (int i = 0; i < contents.length; i++) {
            String curr = contents[i];
            if (i == idxID)
                this.id = Integer.parseInt(curr);
            else if (i == idxAssignee)
                this.assignee = curr;
            else
                this.terms.add(curr);
        }
    }

    public int getId() {
        return id;
    }

    public String getAssignee() {
        return assignee;
    }

    public Set<String> getTerms() {
        return terms;
    }

    public String toString(String separator) {
        String content = "" + this.getId();
        content += separator + this.getAssignee();
        for (String term: this.getTerms())
            content += separator + term;

        return content;
    }

    @Override
    public int compareTo(BugzieInstance other) {
        return this.getId() - other.getId();
    }

    public static List<BugzieInstance> readInstances(String path, String separator, int idxID, int idxAssignee)
    {
        List<BugzieInstance> bugzieInstances = new ArrayList<BugzieInstance>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String thisLine;
            while ((thisLine = br.readLine()) != null) { // while loop begins here
                bugzieInstances.add(new BugzieInstance(thisLine, separator, idxID, idxAssignee));
            } // end while

            br.close();
        } // end try
        catch (IOException e) {
            System.err.println("Error: " + e);
        }

        return bugzieInstances;
    }

    public static void writeInstances(String path, List<BugzieInstance> data, String separator)
    {
        if (path == null)
            return;

        try {
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file, false);
            DataOutputStream dos =new DataOutputStream(fos);

            for (BugzieInstance instance: data)
                dos.write((instance.toString(separator) + "\n").getBytes());

            dos.close();
            fos.close();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }

    public static List<BugzieInstance> copy(List<BugzieInstance> data, int first, int toCopy) {
        List<BugzieInstance> bugzieInstances = new ArrayList<BugzieInstance>();
        for (int i = first; i < first + toCopy; i++)
            bugzieInstances.add(data.get(i));

        return bugzieInstances;
    }

    public static void divideTrainingAndTestDataSet(List<BugzieInstance> bugzieInstances,
                                                    String trainingFile, String testFile,
                                                    String separator, int nFolds) {
        Collections.sort(bugzieInstances);

        int size = bugzieInstances.size();
        int unit = size / nFolds;

        List<BugzieInstance> trainingInstances = BugzieInstance.copy(bugzieInstances, 0, unit);
        List<BugzieInstance> testInstances = BugzieInstance.copy(bugzieInstances, (0 + unit), unit * (nFolds - 1));

        BugzieInstance.writeInstances(trainingFile, trainingInstances, separator);
        BugzieInstance.writeInstances(testFile, testInstances, separator);
    }
}
