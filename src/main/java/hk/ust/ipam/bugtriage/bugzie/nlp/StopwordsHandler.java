package hk.ust.ipam.bugtriage.bugzie.nlp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jeehoonyoo on 29/1/16.
 */
public class StopwordsHandler {
    private final String[] stopwords = {
            "a's","able","about","above","according",
            "accordingly","across","actually","after","afterwards",
            "again","against","ain't","all","allow",
            "allows","almost","alone","along","already",
            "also","although","always","am","among",
            "amongst","an","and","another","any",
            "anybody","anyhow","anyone","anything","anyway",
            "anyways","anywhere","apart","appear","appreciate",
            "appropriate","are","aren't","around","as",
            "aside","ask","asking","associated","at",
            "available","away","awfully","be","became",
            "because","become","becomes","becoming","been",
            "before","beforehand","behind","being","believe",
            "below","beside","besides","best","better",
            "between","beyond","both","brief","but",
            "by","c'mon","c's","came","can",
            "can't","cannot","cant","cause","causes",
            "certain","certainly","changes","clearly","co",
            "com","come","comes","concerning","consequently",
            "consider","considering","contain","containing","contains",
            "corresponding","could","couldn't","course","currently",
            "definitely","described","despite","did","didn't",
            "different","do","does","doesn't","doing",
            "don't","done","down","downwards","during",
            "each","edu","eg","eight","either",
            "else","elsewhere","enough","entirely","especially",
            "et","etc","even","ever","every",
            "everybody","everyone","everything","everywhere","ex",
            "exactly","example","except","far","few",
            "fifth","first","five","followed","following",
            "follows","for","former","formerly","forth",
            "four","from","further","furthermore","get",
            "gets","getting","given","gives","go",
            "goes","going","gone","got","gotten",
            "greetings","had","hadn't","happens","hardly",
            "has","hasn't","have","haven't","having",
            "he","he's","hello","help","hence",
            "her","here","here's","hereafter","hereby",
            "herein","hereupon","hers","herself","hi",
            "him","himself","his","hither","hopefully",
            "how","howbeit","however","i'd","i'll",
            "i'm","i've","ie","if","ignored",
            "immediate","in","inasmuch","inc","indeed",
            "indicate","indicated","indicates","inner","insofar",
            "instead","into","inward","is","isn't",
            "it","it'd","it'll","it's","its",
            "itself","just","keep","keeps","kept",
            "know","known","knows","last","lately",
            "later","latter","latterly","least","less",
            "lest","let","let's","like","liked",
            "likely","little","look","looking","looks",
            "ltd","mainly","many","may","maybe",
            "me","mean","meanwhile","merely","might",
            "more","moreover","most","mostly","much",
            "must","my","myself","name","namely",
            "nd","near","nearly","necessary","need",
            "needs","neither","never","nevertheless","new",
            "next","nine","no","nobody","non",
            "none","noone","nor","normally","not",
            "nothing","novel","now","nowhere","obviously",
            "of","off","often","oh","ok",
            "okay","old","on","once","one",
            "ones","only","onto","or","other",
            "others","otherwise","ought","our","ours",
            "ourselves","out","outside","over","overall",
            "own","particular","particularly","per","perhaps",
            "placed","please","plus","possible","presumably",
            "probably","provides","que","quite","qv",
            "rather","rd","re","really","reasonably",
            "regarding","regardless","regards","relatively","respectively",
            "right","said","same","saw","say",
            "saying","says","second","secondly","see",
            "seeing","seem","seemed","seeming","seems",
            "seen","self","selves","sensible","sent",
            "serious","seriously","seven","several","shall",
            "she","should","shouldn't","since","six",
            "so","some","somebody","somehow","someone",
            "something","sometime","sometimes","somewhat","somewhere",
            "soon","sorry","specified","specify","specifying",
            "still","sub","such","sup","sure",
            "t's","take","taken","tell","tends",
            "th","than","thank","thanks","thanx",
            "that","that's","thats","the","their",
            "theirs","them","themselves","then","thence",
            "there","there's","thereafter","thereby","therefore",
            "therein","theres","thereupon","these","they",
            "they'd","they'll","they're","they've","think",
            "third","this","thorough","thoroughly","those",
            "though","three","through","throughout","thru",
            "thus","to","together","too","took",
            "toward","towards","tried","tries","truly",
            "try","trying","twice","two","un",
            "under","unfortunately","unless","unlikely","until",
            "unto","up","upon","us","use",
            "used","useful","uses","using","usually",
            "value","various","very","via","viz",
            "vs","want","wants","was","wasn't",
            "way","we","we'd","we'll","we're",
            "we've","welcome","well","went","were",
            "weren't","what","what's","whatever","when",
            "whence","whenever","where","where's","whereafter",
            "whereas","whereby","wherein","whereupon","wherever",
            "whether","which","while","whither","who",
            "who's","whoever","whole","whom","whose",
            "why","will","willing","wish","with",
            "within","without","won't","wonder","would",
            "wouldn't","yes","yet","you","you'd",
            "you'll","you're","you've","your","yours",
            "yourself","yourselves","zero"
            /*
            "a", "able", "about", "across", "after", "all", "almost", "also", "am", "among", "an", "and",
            "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot", "could", "dear",
            "did", "do", "does", "either", "else", "ever", "every", "for", "from", "get", "got", "had", "has",
            "have", "he", "her", "hers", "him", "his", "how", "however", "i", "if", "in", "into", "is", "it",
            "its", "just", "least", "let", "like", "likely", "may", "me", "might", "most", "must", "my",
            "neither", "no", "nor", "not", "of", "off", "often", "on", "only", "or", "other", "our", "own",
            "rather", "said", "say", "says", "she", "should", "since", "so", "some", "than", "that", "the",
            "their", "them", "then", "there", "these", "they", "this", "tis", "to", "too", "twas", "us", "wants",
            "was", "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with",
            "would", "yet", "you", "your", "ain't", "aren't", "can't", "could've", "couldn't", "didn't",
            "doesn't", "don't", "hasn't", "he'd", "he'll", "he's", "how'd", "how'll", "how's", "i'd", "i'll",
            "i'm", "i've", "isn't", "it's", "might've", "mightn't", "must've", "mustn't", "shan't", "she'd",
            "she'll", "she's", "should've", "shouldn't", "that'll", "that's", "there's", "they'd", "they'll",
            "they're", "they've", "wasn't", "we'd", "we'll", "we're", "weren't", "what'd", "what's", "when'd",
            "when'll", "when's", "where'd", "where'll", "where's", "who'd", "who'll", "who's", "why'd", "why'll",
            "why's", "won't", "would've", "wouldn't", "you'd", "you'll", "you're", "you've"
            */

    };
    private final String[] programmingJAVAKeywords = {
            "abstract","assert","boolean","break","byte","case","catch","char","class","const","continue",
            "default","do","double","else","enum","extends","final","finally","float","for","goto","if",
            "implements","import","instanceof","int","interface","long","native","new","package","private",
            "protected","public","return","short","static","strictfp","super","switch","synchronized","this",
            "throw","throws","transient","try","void","volatile","while"
    };
    private Set<String> stopwordSet;
    private Pattern symbolPattern;
    // private Pattern numberPattern;

    public StopwordsHandler() {
        this.stopwordSet  = new HashSet<String>(Arrays.asList(this.stopwords));
        this.stopwordSet.addAll(Arrays.asList(this.programmingJAVAKeywords));

        this.symbolPattern = Pattern.compile("^[!\"#$%&\'\\(\\)*+,-./:;<=>?@\\[\\]^_`\\{|\\}~]+$");
        // this.numberPattern = Pattern.compile("^[0-9]+$");
    }

    public boolean isStopword(String word) {
        if (this.stopwordSet.contains(word))
            return true;

        Matcher symbolMatcher = this.symbolPattern.matcher(word);
        return symbolMatcher.matches();

        /*
        Matcher numberMatcher = this.numberPattern.matcher(word);
        return numberMatcher.matches();
        */
    }
}
