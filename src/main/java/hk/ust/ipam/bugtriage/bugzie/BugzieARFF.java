package hk.ust.ipam.bugtriage.bugzie;

import hk.ust.ipam.bugtriage.bugzie.assignee.Assignee;
import hk.ust.ipam.bugtriage.bugzie.attribute.AttributeUtil;
import hk.ust.ipam.bugtriage.bugzie.result.PredictionResult;
import hk.ust.ipam.bugtriage.bugzie.term.TermUtil;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by jeehoonyoo on 14/3/16.
 */
public class BugzieARFF extends Bugzie {
    private Set<Integer> idxToBeProcessed = null;

    public BugzieARFF(int termCacheSize, double assigneeCachePercent, Set<Integer> idxToBeProcessed) {
        this.init(termCacheSize, assigneeCachePercent);
        this.idxToBeProcessed = idxToBeProcessed;
    }

    public void train(Instances data) {
        Attribute classAttr = data.classAttribute();
        Set<Attribute> attributeSet = AttributeUtil.getAttributes(data.firstInstance(), this.idxToBeProcessed);

        Iterator<Instance> instanceIterator = data.iterator();
        while (instanceIterator.hasNext()) {
            Instance currInst = instanceIterator.next();
            this.updateCounterOnly(currInst, attributeSet, classAttr);
        }

        this.updateAssigneeOnly();
    }

    public List<PredictionResult> test(Instance instance) {
        Set<Attribute> attributeSet = AttributeUtil.getAttributes(instance, this.idxToBeProcessed);
        return testByAttributes(instance, attributeSet);
    }

    private List<PredictionResult> testByAttributes(Instance instance, Set<Attribute> attributesToBeProcessed) {
        Set<String> terms = TermUtil.extractTerms(instance, attributesToBeProcessed);

        List<PredictionResult> predictionResults = new ArrayList<PredictionResult>();
        Set<String> assigneeNamePool = this.getMostRecentlyActiveDevelopers();
        for (String assigneeName: assigneeNamePool) {
            Assignee assignee = this.assigneeMap.get(assigneeName);
            if (assignee == null)
                continue;

            double score = calculateAssigneeScore(terms, assignee);
            predictionResults.add(new PredictionResult(assigneeName, score));
        }

        return predictionResults;
    }

    public List<PredictionResult> testAndUpdate(Instance instance) {
        Set<Attribute> attributeSet = AttributeUtil.getAttributes(instance, this.idxToBeProcessed);
        List<PredictionResult> predictionResults = testByAttributes(instance, attributeSet);
        this.update(instance, attributeSet, instance.classAttribute());

        return predictionResults;
    }



    // for training phase
    private void updateCounterOnly(Instance instance, Set<Attribute> attributeSet, Attribute classAttr) {
        String assigneeName = instance.stringValue(classAttr);
        Set<String> terms = TermUtil.extractTerms(instance, attributeSet);

        this.updateCounterOnly(terms, assigneeName);
    }








    // for update phase
    private void update(Instance instance, Set<Attribute> attributeSet, Attribute classAttr) {
        String assigneeName = instance.stringValue(classAttr);
        Set<String> terms = TermUtil.extractTerms(instance, attributeSet);

        Assignee assignee = addAssignee(assigneeName);
        for (String term: terms)
            addTermToCounterAndAssignee(term, assignee);
        updateAssignee(assignee);
    }





    /// print methods for verifying the results
    public void printAssigneeMRUStack() {
        Set<String> mruAssignees = this.assigneeMRUStack.getMostRecentlyUpdated(this.assigneeCachePercent);
        System.out.println("MRU assignees:");
        for (String assignee: mruAssignees)
            System.out.print(assignee + " ");
        System.out.println();
    }

    public void printAssigneeInfo() {
        for (String name: this.assigneeMap.keySet()) {
            int n_d = this.assigneeCounter.getCount(name);

            Assignee assignee = this.assigneeMap.get(name);
            System.out.println(assignee.toString(n_d, this.termCounter, this.assigneeTermCounter));
        }
    }

    public void printCounters() {
        System.out.println("Term counter:");
        System.out.println(this.termCounter.toCSVString());

        System.out.println("Assignee counter:");
        System.out.println(this.assigneeCounter.toCSVString());

        System.out.println("Assignee term counter:");
        System.out.println(this.assigneeTermCounter.toCSVString());
    }
}
