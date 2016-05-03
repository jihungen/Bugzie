package hk.ust.ipam.bugtriage.bugzie.counter;

import java.util.*;

/**
 * Created by jeehoonyoo on 3/2/16.
 */
public class Counter {
    private Map<String, Integer> counter;

    public Counter() {
        this.counter = new HashMap<String, Integer>();
    }

    public void clear() {
        this.counter.clear();
    }

    public void addValue(String value) {
        Integer cnt = this.counter.get(value);
        if (cnt == null)
            this.counter.put(value, 1);
        else
            this.counter.put(value, cnt + 1);
    }

    public Set<String> values() {
        return this.counter.keySet();
    }

    public String toCSVString() {
        List<String> orderedValues = new ArrayList<String>(this.counter.keySet());
        Collections.sort(orderedValues);

        String str = null;
        for (String currValue: orderedValues) {
            int currCnt = this.counter.get(currValue);
            if (str == null)
                str = currValue + "," + currCnt;
            else
                str += "\n" + currValue + "," + currCnt;
        }

        return str;
    }

    public int getCount(String value) {
        Integer cnt = this.counter.get(value);
        if (cnt == null)
            return 0;

        return cnt;
    }
}
