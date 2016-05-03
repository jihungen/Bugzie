package hk.ust.ipam.bugtriage.bugzie.assignee;

import hk.ust.ipam.bugtriage.bugzie.BugzieARFF;
import hk.ust.ipam.bugtriage.bugzie.counter.Counter;
import hk.ust.ipam.bugtriage.bugzie.term.TermUtil;
import hk.ust.ipam.bugtriage.bugzie.term.TermWithScore;

import java.util.*;

/**
 * Created by jeehoonyoo on 14/3/16.
 */
public class Assignee {
    private String name;
    private Set<String> terms;

    private int cacheSize;

    public Assignee(String name, int cacheSize) {
        this.name = name;
        this.cacheSize = cacheSize;

        this.terms = new HashSet<String>();
    }

    public String getName() {
        return name;
    }

    public void addTerms(int n_d, Counter termCounter, Counter assigneeTermCounter) {
        List<TermWithScore> termWithScores = new ArrayList<TermWithScore>();
        for (String assigneeTerm: assigneeTermCounter.values()) {
            String[] separatedAssigneeTerm = TermUtil.separateAssigneeTerm(assigneeTerm);
            if (separatedAssigneeTerm[0].compareTo(this.name) != 0)
                continue;

            String term = separatedAssigneeTerm[1];
            int n_t = termCounter.getCount(term);
            int n_dt = assigneeTermCounter.getCount(assigneeTerm);

            termWithScores.add(new TermWithScore(term, BugzieARFF.calculateScore(n_d, n_t, n_dt)));
        }

        if (termWithScores.size() <= 0)
            return;

        Collections.sort(termWithScores);
        while (termWithScores.size() > this.cacheSize)
            termWithScores.remove(termWithScores.size() - 1);

        this.terms = TermWithScore.getNames(termWithScores);
    }

    public void addTerm(String name, int n_d, Counter termCounter, Counter assigneeTermCounter) {
        if (this.terms.contains(name) || this.terms.size() < this.cacheSize) {
            this.terms.add(name);
            return;
        }

        this.terms.add(name);
        List<TermWithScore> termWithScores = new ArrayList<TermWithScore>();
        for (String term: this.terms) {
            int n_t = termCounter.getCount(term);
            int n_dt = assigneeTermCounter.getCount(TermUtil.generateAssigneeTerm(this.name, term));

            termWithScores.add(new TermWithScore(term, BugzieARFF.calculateScore(n_d, n_t, n_dt)));
        }

        Collections.sort(termWithScores);
        termWithScores.remove(termWithScores.size() - 1);
        this.terms = TermWithScore.getNames(termWithScores);
    }

    public void clear() {
        this.name = null;
        this.terms.clear();
    }

    public double calculateScore(Collection<String> termsInReport,
                                 int n_d, Counter termCounter, Counter assigneeTermCounter) {
        double score = -1.0f;
        for (String term: termsInReport) {
            if (this.terms.contains(term) == false)
                continue;

            int n_t = termCounter.getCount(term);
            int n_dt = assigneeTermCounter.getCount(TermUtil.generateAssigneeTerm(this.name, term));

            double currScore = BugzieARFF.calculateScore(n_d, n_t, n_dt);
            if (score < 0.0f)
                score = (1.0f - currScore);
            else
                score *= (1.0f - currScore);
        }

        if (score < 0.0f)
            return 0.0f;

        return (1.0f - score);
    }

    public String toString(int n_d, Counter termCounter, Counter assigneeTermCounter) {
        String str = this.name;
        str += "," + this.cacheSize + ":\n";

        for (String term: this.terms) {
            int n_t = termCounter.getCount(term);
            int n_dt = assigneeTermCounter.getCount(TermUtil.generateAssigneeTerm(this.name, term));

            str += term.toString() + "," + BugzieARFF.calculateScore(n_d, n_t, n_dt) + "\n";
        }

        return str;
    }
}
