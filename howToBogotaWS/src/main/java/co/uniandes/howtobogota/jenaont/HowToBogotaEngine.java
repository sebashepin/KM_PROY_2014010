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
			}else{
				result = new AnswerResponse(STATUS.INVALID, null);
			}
		}
		else{
			Step step= instance.darRespuesta(idQuestion);
			if(step!=null){
			 result = new AnswerResponse(STATUS.OK, new Answer(step.getStepId(), step.getStepDescription(),
			              step.getNeighborhoodString()));
			}else{
				result = new AnswerResponse(STATUS.NEW, null);
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
//		POSTaggerAnswer tg=new POSTaggerAnswer(question);
//		OntologyManager instance = OntologyManager.darInstancia();
//		//TODO Buscar pregunta de acuerdo a similaridades. 
//		ArrayList<String> res=instance.buscarPregunta(tg.getVerbs(), tg.getEntities());
//		if(res.size()>0){
//			return false;
//		}else{
//			instance.agregarPregunta("preg"+Math.random()*100, question, tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
//			return true;
//		}
		//TODO invalid question 
		return false;
	}

	@Override
	public String addFirstStep(String question, String stepDescription) {
		POSTaggerAnswer tg=new POSTaggerAnswer(stepDescription);
		OntologyManager instance = OntologyManager.darInstancia();
		return instance.agregarPrimerPaso("preg"+Math.random()*100, question, stepDescription,tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
	}

	@Override
	public boolean createAndAnswerQuestion(String question, Object[] steps) {
//		POSTaggerAnswer tg=new POSTaggerAnswer(question);
//		OntologyManager instance = OntologyManager.darInstancia();
//		ArrayList<String> res=instance.buscarPregunta(tg.getVerbs(), tg.getEntities());
//		if(res.size()>0){
//			return false;
//		}else{
//			String idPregunta="preg"+Math.random()*100;
//			instance.agregarPregunta(idPregunta, question, tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
//			instance.agregarRespuesta("resp"+Math.random()*100, idPregunta, (String[])steps, tg.getVerbs(), tg.getEntities(), tg.getAdjectives());
//			return true;
//		}
		return false;
	}

}
