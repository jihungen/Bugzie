package hk.ust.ipam.bugtriage.bugzie.term;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jeehoonyoo on 14/3/16.
 */
public class TermWithScore implements Comparable<TermWithScore> {
    private String name;
    private double score;

    public TermWithScore(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(TermWithScore other) {
        if (this.score > other.score)
            return -1;
        else if (this.score < other.score)
            return 1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return this.name + "," + this.score;
    }

    public static Set<String> getNames(List<TermWithScore> termWithScores) {
        Set<String> names = new HashSet<String>();
        for (TermWithScore termWithScore: termWithScores)
            names.add(termWithScore.getName());

        return names;
    }
}
