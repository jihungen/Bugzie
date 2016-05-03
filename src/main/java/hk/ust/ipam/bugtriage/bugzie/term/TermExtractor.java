package hk.ust.ipam.bugtriage.bugzie.term;

import hk.ust.ipam.bugtriage.bugzie.nlp.StopwordsHandler;
import weka.core.stemmers.SnowballStemmer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jeehoonyoo on 29/3/16.
 */
public class TermExtractor {
    public static final String DELIMITERS   = "\\s|\\.|,|;|:|\"|\\(|\\)|\\?|!";

    private SnowballStemmer snowballStemmer;
    private StopwordsHandler stopwordsHandler;

    public TermExtractor() {
        this.snowballStemmer = new SnowballStemmer();
        this.snowballStemmer.setStemmer("english");

        this.stopwordsHandler = new StopwordsHandler();
    }

    public Set<String> extractTerms(String str) {
        if (str == null)
            return null;

        Set<String> terms = new HashSet<String>();

        String[] content = str.split(DELIMITERS);
        for (String curr: content) {
            if (this.stopwordsHandler.isStopword(curr) == true)
                continue;

            String stemmedWord = this.snowballStemmer.stem(curr);
            if (stemmedWord.compareTo("") == 0)
                continue;

            terms.add(stemmedWord);
        }

        return terms;
    }
}
