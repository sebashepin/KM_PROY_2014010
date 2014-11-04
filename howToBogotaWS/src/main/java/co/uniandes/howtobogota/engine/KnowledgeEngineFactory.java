package co.uniandes.howtobogota.engine;

public abstract class KnowledgeEngineFactory {
	
	public static final KnowledgeEngine getDefaultKnowledgeEngine()
	{
		//TODO return actual engine, fine by the moment though
		return new PlaceholderKnowledgeEngine();
	}

	
	
}
