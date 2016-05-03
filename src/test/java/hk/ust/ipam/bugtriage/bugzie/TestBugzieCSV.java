package hk.ust.ipam.bugtriage.bugzie;

import hk.ust.ipam.bugtriage.bugzie.result.PredictionResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeehoonyoo on 3/5/16.
 */
public class TestBugzieCSV {
    @Test
    public void testBugzieCSV() {
        String trainingFile     = "src/test/resources/freedesktop_train.csv";
        String testFile         = "src/test/resources/freedesktop_test.csv";

        int idxID = 0;
        int idxAssignee = 1;
        String separator = "\t";

        int termCacheSize = 20;
        double assigneeCachePercent = 1.0f;

        List<BugzieInstance> trainingData
                = BugzieInstance.readInstances(trainingFile, separator, idxID, idxAssignee);
        List<BugzieInstance> testData
                = BugzieInstance.readInstances(testFile, separator, idxID, idxAssignee);

        List<String> resultStrings = predictAssignee(trainingData, testData, termCacheSize, assigneeCachePercent, 10);
        for (String curr: resultStrings)
            System.out.println(curr);
    }

    public static List<String> predictAssignee(List<BugzieInstance> trainingData, List<BugzieInstance> testData,
                                               int termCacheSize, double assigneeCachePercent,
                                               int noCandidates) {
        BugzieCSV bugzieCSV = new BugzieCSV(termCacheSize, assigneeCachePercent);
        bugzieCSV.train(trainingData);

        List<String> resultStr = new ArrayList<String>();
        for (int i = 0; i < testData.size(); i++) {
            BugzieInstance currInst = testData.get(i);
            List<PredictionResult> results = bugzieCSV.testAndUpdate(currInst);

            String currResultStr = currInst.getId() + "," + PredictionResult.toCSVStringResultsOnly(results, noCandidates);
            resultStr.add(currResultStr);
        }

        return resultStr;
    }
}
