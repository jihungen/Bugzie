package hk.ust.ipam.bugtriage.bugzie.counter;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by jeehoonyoo on 14/3/16.
 */
public class MRUStack {
    private Stack<String> stack;

    public MRUStack() {
        this.stack = new Stack<String>();
    }

    public void addValue(String value) {
        this.stack.remove(value);
        this.stack.push(value);
    }

    public void clear() {
        this.stack.clear();
    }

    public Set<String> getMostRecentlyUpdated(double percent) {
        int targetSize = (int)(percent * (double)this.stack.size());
        Set<String> valueSet = new HashSet<String>();

        while (this.stack.isEmpty() == false) {
            String value = this.stack.pop();
            valueSet.add(value);

            if (valueSet.size() >= targetSize)
                break;
        }

        for (String value: valueSet)
            this.stack.push(value);

        return valueSet;
    }
}
