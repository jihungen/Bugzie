package hk.ust.ipam.bugtriage.bugzie.term;

import weka.core.Attribute;
import weka.core.Instance;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jeehoonyoo on 16/3/16.
 */
public class TermUtil {
    public final static String SEPARATOR    = "@@";

    public static Set<String> extractTerms(Instance instance, Set<Attribute> attributeSet) {
        Set<String> terms = new HashSet<String>();
        for (Attribute currAttr: attributeSet) {
            String termName = null;
            if (currAttr.isNumeric()) {
                if (instance.value(currAttr) > 0)
                    termName = currAttr.name();
            } else {
                termName = instance.stringValue(currAttr);
                if (currAttr.isNominal() == false) {
                    if (termName != null && termName.trim().compareTo("") == 0)
                        termName = null;
                }
            }

            if (termName != null)
                terms.add(termName);
        }

        return terms;
    }

    public static String generateAssigneeTerm(String assignee, String term) {
        return assignee + SEPARATOR + term;
    }

    public static String[] separateAssigneeTerm(String assigneeTerm) {
        return assigneeTerm.split(SEPARATOR);
    }
}
