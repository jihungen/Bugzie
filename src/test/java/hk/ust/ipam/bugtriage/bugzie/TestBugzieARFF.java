package hk.ust.ipam.bugtriage.bugzie;

import hk.ust.ipam.bugtriage.bugzie.arff.ARFFUtil;
import hk.ust.ipam.bugtriage.bugzie.attribute.AttributeUtil;
import hk.ust.ipam.bugtriage.bugzie.result.PredictionResult;
import org.junit.Test;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by jeehoonyoo on 3/5/16.
 */
public class TestBugzieARFF {
    @Test
    public void testBugzieARFF() {
        String trainingFile     = "src/test/resources/freedesktop_train.arff";
        String testFile         = "src/test/resources/freedesktop_test.arff";

        String className = "attribute_assignee";
        String idxAttrName = "attribute_id";
        Set<Integer> idxTermSet = AttributeUtil.parseIdxString("1-4,7-1010");

        int termCacheSize = 20;
        double assigneeCachePercent = 1.0f;

        Instances trainingData = ARFFUtil.readData(trainingFile, className);
        Instances testData = ARFFUtil.readData(testFile, className);

        List<String> resultStrings = predictAssignee(trainingData, testData, idxTermSet, idxAttrName, termCacheSize,
                assigneeCachePercent, 10);
        for (String curr: resultStrings)
            System.out.println(curr);
    }

    public static List<String> predictAssignee(Instances trainingData, Instances testData,
                                               Set<Integer> idxTermSet, String idxAttrName,
                                               int termCacheSize, double assigneeCachePercent,
                                               int noCandidates) {
        BugzieARFF bugzieARFF = new BugzieARFF(termCacheSize, assigneeCachePercent, idxTermSet);
        bugzieARFF.train(trainingData);

        Attribute idxAttr = testData.attribute(idxAttrName);
        if (idxAttr == null)
            return null;

        List<String> resultStr = new ArrayList<String>();
        for (int i = 0; i < testData.size(); i++) {
            Instance currInst = testData.get(i);
            List<PredictionResult> results = bugzieARFF.testAndUpdate(currInst);

            String currResultStr = (int)currInst.value(idxAttr) + "," +
                    PredictionResult.toCSVStringResultsOnly(results, noCandidates);
            resultStr.add(currResultStr);
        }

        return resultStr;
    }
}
