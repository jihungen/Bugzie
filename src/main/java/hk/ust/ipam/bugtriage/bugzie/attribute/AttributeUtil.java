package hk.ust.ipam.bugtriage.bugzie.attribute;

import weka.core.Attribute;
import weka.core.Instance;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jeehoonyoo on 16/3/16.
 */
public class AttributeUtil {
    public static final String IDX_SEPARATOR    = ",";
    public static final String IDX_RANGE    = "-";

    public static Set<Attribute> getAttributes(Instance data, Set<Integer> idxSet) {
        if (idxSet == null)
            return null;

        int noAttributes = data.numAttributes();
        Set<Attribute> attributeSet = new HashSet<Attribute>();
        for (int idx: idxSet) {
            if (idx >= noAttributes)
                continue;

            Attribute currAttr = data.attribute(idx);
            if (currAttr != null)
                attributeSet.add(currAttr);
        }

        return attributeSet;
    }

    public static Set<Integer> parseIdxString(String idxString) {
        if (idxString == null)
            return null;

        String[] content = idxString.split(IDX_SEPARATOR);
        if (content == null)
            return null;

        Set<Integer> idxList = new HashSet<Integer>();
        for (String curr: content) {
            if (curr.indexOf(IDX_RANGE) >= 0) {
                String[] rangeContent = curr.split(IDX_RANGE);
                int idxBegin = Integer.parseInt(rangeContent[0].trim());
                int idxEnd = Integer.parseInt(rangeContent[1].trim());

                for (int i = idxBegin; i <= idxEnd; i++)
                    idxList.add(i);
            } else {
                idxList.add(Integer.parseInt(curr.trim()));
            }
        }

        return idxList;
    }
}
