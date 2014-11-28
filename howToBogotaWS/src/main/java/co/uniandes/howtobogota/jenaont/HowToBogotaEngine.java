package co.uniandes.howtobogota.jenaont;

import co.uniandes.howtobogota.engine.Answer;
import co.uniandes.howtobogota.engine.AnswerResponse;
import co.uniandes.howtobogota.engine.KnowledgeEngine;
import co.uniandes.howtobogota.engine.AnswerResponse.STATUS;

public class HowToBogotaEngine implements KnowledgeEngine{

	@Override
    public AnswerResponse getAnswerToQuestion(String question) {
		AnswerResponse result;
		OntologyManager instance = OntologyManager.darInstancia();
		POSTaggerAnswer tg=new POSTaggerAnswer(question);
		String idQuestion=instance.buscarPreguntaSimilar(tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
		if(idQuestion==null){
			boolean isValid=true;
			for(int i=0; i< tg.getVerbs().size() && isValid; i++){
				for(int j=0;j<tg.getAdjectives().size() && isValid;j++){
					isValid= (isValid && instance.esValido(tg.getVerbs().get(i), tg.getAdjectives().get(j))); 
				}
			}
			if(isValid){
				result = new AnswerResponse(STATUS.NEW, null);
				System.out.println("new");
			}else{
				result = new AnswerResponse(STATUS.INVALID, null);
				System.out.println("invalid");
			}
		}
		else{
			Step step= instance.darRespuesta(idQuestion);
			if(step!=null){
			 result = new AnswerResponse(STATUS.OK, new Answer(step.getStepId(), step.getStepDescription(),
			              step.getNeighborhoodString()));
			 System.out.println("ok");
			}else{
				result = new AnswerResponse(STATUS.NEW, null);
				System.out.println("new");
			}
		}
		
		return result;
	}

	@Override
	public AnswerResponse getStepNeighbor(String stepId,
			STEP_NEIGHBOR stepDirection) {
		AnswerResponse result;
		OntologyManager instance = OntologyManager.darInstancia();
		Step step=null;
		 switch (stepDirection) {
		 case UP: 
			 step=instance.getUpDownStepNeighbor(stepId, true);
			 break;
		 case DOWN:
			 step=instance.getUpDownStepNeighbor(stepId, false);
			 break;
		 case NEXT:
			 step=instance.getNextStepNeighbor(stepId);
			 break;
		 case PREVIOUS:
			 step=instance.getPrevStepNeighbor(stepId);
			 break;
		 }
	      if (step == null) {
	        result = new AnswerResponse(STATUS.NEW, null);
	      } else {
	        result =
	            new AnswerResponse(STATUS.OK, new Answer(step.getStepId(), step.getStepDescription(),
	                step.getNeighborhoodString()));
	      }
		return result;
	}

	@Override
	public String addStep(String previousStepId, String nextStepId,
			String stepDescription) {
		OntologyManager instance = OntologyManager.darInstancia();
		return instance.addStep(previousStepId, nextStepId, stepDescription);
	}

	@Override
	public boolean createQuestion(String question) {
		POSTaggerAnswer tg=new POSTaggerAnswer(question);
		OntologyManager instance = OntologyManager.darInstancia();

		String idQuestion=instance.buscarPreguntaSimilar(tg.getVerbs(), tg.getEntities(), tg.getAdjectives());

		if(idQuestion!=null){
			return false;
		}else{
			instance.agregarPregunta(question, tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
			return true;
		}
	}
 
	@Override
	public String addFirstStep(String question, String stepDescription) {
		POSTaggerAnswer tg=new POSTaggerAnswer(question);
		OntologyManager instance = OntologyManager.darInstancia();
		String idQuestion=instance.buscarPreguntaSimilar(tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
		if(idQuestion!=null){
			return instance.agregarPrimerPaso(idQuestion, stepDescription);
		}else{
			return "";
		}
		
	}

	@Override
	public boolean createAndAnswerQuestion(String question, Object[] steps) {
		
		POSTaggerAnswer tg=new POSTaggerAnswer(question);
		OntologyManager instance = OntologyManager.darInstancia();

		String idQuestion=instance.buscarPreguntaSimilar(tg.getVerbs(), tg.getEntities(), tg.getAdjectives());

		if(idQuestion!=null){
			return false;
		}else{
			String questionId=instance.agregarPregunta(question, tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
			instance.agregarRespuesta(questionId, (String [])steps);
			return true;
		}
	}

}
