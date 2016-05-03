package hk.ust.ipam.bugtriage.bugzie.result;

import java.util.Collections;
import java.util.List;

/**
 * Created by jeehoonyoo on 15/3/16.
 */
public class PredictionResult implements Comparable<PredictionResult> {
    private String assignee;
    private double score;

    public PredictionResult(String assignee, double score) {
        this.assignee = assignee;
        this.score = score;
    }

    public String getAssignee() {
        return assignee;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int compareTo(PredictionResult other) {
        if (this.score > other.score)
            return -1;
        else if (this.score < other.score)
            return 1;
        else
            return 0;
    }

    public String toCSVString() {
        return this.assignee + "," + this.score;
    }

    public static String toCSVString(List<PredictionResult> results) {
        Collections.sort(results);

        String str = null;
        for (PredictionResult result: results) {
            if (str == null)
                str = result.toCSVString();
            else
                str += "," + result.toCSVString();
        }

        return str;
    }

    public static String toCSVStringResultsOnly(List<PredictionResult> results) {
        Collections.sort(results);

        String str = null;
        for (PredictionResult result: results) {
            if (str == null)
                str = result.getAssignee();
            else
                str += "," + result.getAssignee();
        }

        return str;
    }

    public static String toCSVStringResultsOnly(List<PredictionResult> results, int noCandidates) {
        Collections.sort(results);

        int cnt = 0;
        String str = null;
        for (PredictionResult result: results) {
            if (cnt >= noCandidates)
                break;

            if (str == null)
                str = result.getAssignee();
            else
                str += "," + result.getAssignee();
            cnt++;
        }

        return str;
    }
}
