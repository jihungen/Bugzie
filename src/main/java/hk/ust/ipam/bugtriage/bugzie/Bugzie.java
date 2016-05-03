package hk.ust.ipam.bugtriage.bugzie;

import hk.ust.ipam.bugtriage.bugzie.assignee.Assignee;
import hk.ust.ipam.bugtriage.bugzie.counter.Counter;
import hk.ust.ipam.bugtriage.bugzie.counter.MRUStack;
import hk.ust.ipam.bugtriage.bugzie.term.TermUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jeehoonyoo on 29/3/16.
 */
public class Bugzie {
    protected Counter termCounter = new Counter();
    protected Counter assigneeCounter = new Counter();
    protected Counter assigneeTermCounter = new Counter();

    protected Map<String, Assignee> assigneeMap = new HashMap<String, Assignee>();

    protected MRUStack assigneeMRUStack = new MRUStack();

    protected int termCacheSize;
    protected double assigneeCachePercent;

    protected void init(int termCacheSize, double assigneeCachePercent) {
        this.termCacheSize = termCacheSize;
        this.assigneeCachePercent = assigneeCachePercent;
    }


    // for training phase
    protected void updateCounterOnly(Set<String> terms, String assigneeName) {
        this.assigneeCounter.addValue(assigneeName);
        this.assigneeMRUStack.addValue(assigneeName);

        for (String term: terms)
            this.addTermToCounter(term, assigneeName);
    }

    // for training phase
    protected void updateAssigneeOnly() {
        for (String assigneeName: this.assigneeCounter.values()) {
            Assignee assignee = new Assignee(assigneeName, this.termCacheSize);
            int n_d = this.assigneeCounter.getCount(assigneeName);

            assignee.addTerms(n_d, this.termCounter, this.assigneeTermCounter);
            this.assigneeMap.put(assigneeName, assignee);
        }
    }




    // for test phase
    protected Set<String> getMostRecentlyActiveDevelopers() {
        return this.assigneeMRUStack.getMostRecentlyUpdated(this.assigneeCachePercent);
    }

    // for test phase
    protected double calculateAssigneeScore(Set<String> terms, Assignee assignee) {
        if (assignee == null)
            return 0.0f;

        int n_d = this.assigneeCounter.getCount(assignee.getName());
        return assignee.calculateScore(terms, n_d, this.termCounter, this.assigneeTermCounter);
    }




    // for update phase
    protected Assignee addAssignee(String assigneeName) {
        this.assigneeCounter.addValue(assigneeName);
        this.assigneeMRUStack.addValue(assigneeName);

        Assignee assignee = this.assigneeMap.get(assigneeName);
        if (assignee == null)
            assignee = new Assignee(assigneeName, this.termCacheSize);

        return assignee;
    }

    // for update phase
    protected void updateAssignee(Assignee assignee) {
        this.assigneeMap.put(assignee.getName(), assignee);
    }


    // terms
    protected void addTermToCounterAndAssignee(String termName, Assignee assignee) {
        String assigneeName = assignee.getName();
        this.addTermToCounter(termName, assigneeName);

        int n_d = assigneeCounter.getCount(assigneeName);
        assignee.addTerm(termName, n_d, termCounter, assigneeTermCounter);
    }

    protected void addTermToCounter(String termName, String assigneeName) {
        this.termCounter.addValue(termName);
        this.assigneeTermCounter.addValue(TermUtil.generateAssigneeTerm(assigneeName, termName));
    }



    // score
    public static double calculateScore(int n_d, int n_t, int n_dt) {
        if (n_dt == 0)
            return 0.0f;

        return ((double)n_dt / (double)(n_d + n_t - n_dt));
    }
}
