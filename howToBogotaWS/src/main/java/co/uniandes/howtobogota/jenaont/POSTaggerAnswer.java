package co.uniandes.howtobogota.jenaont;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aliasi.hmm.HiddenMarkovModel;
import com.aliasi.hmm.HmmDecoder;
import com.aliasi.tag.Tagging;
import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;
import com.aliasi.tokenizer.RegExTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.Streams;

public class POSTaggerAnswer {
	
	private ArrayList<String> verbs;
	private ArrayList<String> entities;
	private ArrayList<String> adjectives;
	
	private String originalText;

	public POSTaggerAnswer(String originalText){
		this.originalText=originalText;
		this.verbs= new ArrayList<String>();
		this.entities= new ArrayList<String>();
		this.adjectives= new ArrayList<String>();
		
		try {
			tagData();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> getVerbs() {
		return verbs;
	}

	public void setVerbs(ArrayList<String> verbs) {
		this.verbs = verbs;
	}

	public ArrayList<String> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<String> entities) {
		this.entities = entities;
	}

	public ArrayList<String> getAdjectives() {
		return adjectives;
	}

	public void setAdjectives(ArrayList<String> adjectives) {
		this.adjectives = adjectives;
	}

	public String getOriginalText() {
		return originalText;
	}

	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}

	static TokenizerFactory TOKENIZER_FACTORY 
    = new RegExTokenizerFactory("(-|'|\\d|\\p{L})+|\\S");


static final Set<String> ENTITY_TAGS = new HashSet<String>();
static final Set<String> ADVERB_TAGS = new HashSet<String>();
static final Set<String> VERB_TAGS = new HashSet<String>();

static final Set<String> RELEVANT_TAGS= new HashSet<String>();


static {
	ADVERB_TAGS.add("abl");
	ADVERB_TAGS.add("abn");
	ADVERB_TAGS.add("abx");
	ADVERB_TAGS.add("ap");
	ADVERB_TAGS.add("ap$");
	ADVERB_TAGS.add("jj");
	ADVERB_TAGS.add("jj$");
    ADVERB_TAGS.add("jjr");
    ADVERB_TAGS.add("jjs");
    ADVERB_TAGS.add("jjt");
    ADVERB_TAGS.add("ql");
    ADVERB_TAGS.add("rb");
    ADVERB_TAGS.add("rb$");
    ADVERB_TAGS.add("rbr");
    ADVERB_TAGS.add("rbt");
    ADVERB_TAGS.add("rn");
    ADVERB_TAGS.add("ql");
    
    ENTITY_TAGS.add("nn");
    ENTITY_TAGS.add("nn$");
    ENTITY_TAGS.add("nns");
    ENTITY_TAGS.add("nns$");
    ENTITY_TAGS.add("np");
    ENTITY_TAGS.add("np$");
    ENTITY_TAGS.add("nps");
    ENTITY_TAGS.add("nps$");
    ENTITY_TAGS.add("nr");
    ENTITY_TAGS.add("nr$");
    ENTITY_TAGS.add("nrs");

    VERB_TAGS.add("vb");
    VERB_TAGS.add("vbd");
    VERB_TAGS.add("vbg");
    VERB_TAGS.add("vbn");
    VERB_TAGS.add("vbp");
    VERB_TAGS.add("vbz");



}


private void tagData()
    throws ClassNotFoundException, IOException {
    
	String text="C:/models/pos-en-general-brown.HiddenMarkovModel";
    System.out.println("Reading model from file="+text);
    FileInputStream fileIn = new FileInputStream(text);
    ObjectInputStream objIn = new ObjectInputStream(fileIn);
    HiddenMarkovModel hmm = (HiddenMarkovModel) objIn.readObject();
    Streams.closeQuietly(objIn);
    HmmDecoder decoder = new HmmDecoder(hmm);
    

    originalText=originalText.toLowerCase();
    long n = System.currentTimeMillis();
    char[] cs = originalText.toCharArray();

    Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(cs,0,cs.length);
    String[] tokens = tokenizer.tokenize();
    List<String> tokenList = Arrays.asList(tokens);

    Tagging<String> tagging = decoder.tag(tokenList);
    System.out.println("\nResults:");
    for (int i = 0; i < tagging.size(); ++i){
    	System.out.print(tagging.token(i)+"->"+PorterStemmerTokenizerFactory.stem(tagging.token(i) )+ "_" + tagging.tag(i) + " ");

    	if(tagging.token(i).length()>1){
    	if(ADVERB_TAGS.contains(tagging.tag(i))){
    		adjectives.add(PorterStemmerTokenizerFactory.stem(tagging.token(i)));
    	}else if(VERB_TAGS.contains(tagging.tag(i))){
    		verbs.add(PorterStemmerTokenizerFactory.stem(tagging.token(i)));
    	}else if(ENTITY_TAGS.contains(tagging.tag(i))){
    		entities.add(PorterStemmerTokenizerFactory.stem(tagging.token(i)));
    	}
    	}
    	
    }
    System.out.println(System.currentTimeMillis()-n);
}

}
