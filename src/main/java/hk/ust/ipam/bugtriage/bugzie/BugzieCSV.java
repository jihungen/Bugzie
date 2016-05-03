package hk.ust.ipam.bugtriage.bugzie;

import hk.ust.ipam.bugtriage.bugzie.assignee.Assignee;
import hk.ust.ipam.bugtriage.bugzie.result.PredictionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by jeehoonyoo on 29/3/16.
 */
public class BugzieCSV extends Bugzie {
    public BugzieCSV(int termCacheSize, double assigneeCachePercent) {
        this.init(termCacheSize, assigneeCachePercent);
    }

    public void train(List<BugzieInstance> data) {
        for (BugzieInstance instance: data)
            this.updateCounterOnly(instance.getTerms(), instance.getAssignee());

        this.updateAssigneeOnly();
    }

    public List<PredictionResult> test(BugzieInstance instance) {
        List<PredictionResult> predictionResults = new ArrayList<PredictionResult>();
        Set<String> assigneeNamePool = this.getMostRecentlyActiveDevelopers();
        for (String assigneeName: assigneeNamePool) {
            Assignee assignee = this.assigneeMap.get(assigneeName);
            if (assignee == null)
                continue;

            double score = calculateAssigneeScore(instance.getTerms(), assignee);
            predictionResults.add(new PredictionResult(assigneeName, score));
        }

        return predictionResults;
    }

    public List<PredictionResult> testAndUpdate(BugzieInstance instance) {
        List<PredictionResult> predictionResults = this.test(instance);
        this.update(instance);

        return predictionResults;
    }

    // for update phase
    private void update(BugzieInstance instance) {
        Assignee assignee = addAssignee(instance.getAssignee());
        for (String term: instance.getTerms())
            addTermToCounterAndAssignee(term, assignee);
        updateAssignee(assignee);
    }


}
