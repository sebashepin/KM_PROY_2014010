package co.uniandes.howtobogota.engine;

/**
 * @author Sebastian
 * KES = Knowledge Engine Singleton
 */
public class KES {

	private static KES instance;
	
	private KnowledgeEngine knowledgeEngine;
	
	
	
	private KES() {
		super();
		knowledgeEngine = KnowledgeEngineFactory.getDefaultKnowledgeEngine();
	}

	public KnowledgeEngine getKnowledgeEngine(){
		return knowledgeEngine;
	}


	public static KES getInstance()	{
		if(instance == null)
			instance = new KES();
		return instance;
	}
}
