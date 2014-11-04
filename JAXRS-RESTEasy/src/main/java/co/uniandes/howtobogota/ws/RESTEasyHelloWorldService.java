package co.uniandes.howtobogota.ws;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import co.uniandes.howtobogota.engine.Answer;
import co.uniandes.howtobogota.engine.AnswerResponse;
import co.uniandes.howtobogota.engine.KES;
import co.uniandes.howtobogota.engine.KnowledgeEngine;
import co.uniandes.howtobogota.engine.KnowledgeEngine.STEP_NEIGHBOR;

@Path("/HowToBogota")
public class RESTEasyHelloWorldService {

	@GET
	@Path("/test/{pathParameter}")
	@Produces("application/json")
	public Response responseMsg( @PathParam("pathParameter") String pathParameter,
			@DefaultValue("Nothing to say") @QueryParam("queryParameter") String queryParameter) {

		String response = "Hello from: " + pathParameter + " : " + queryParameter;

		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Path("/answer")
	@Produces("application/json")
	public AnswerResponse placeholderAnswer() {
		AnswerResponse st = new AnswerResponse(AnswerResponse.STATUS.OK, new Answer("23", "I'm the first and most popular step", "1101"));

		return st;
	}
	
	@POST
	@Path("/GetAnswer")
	@Produces("application/json")
	public Object answerQuestion(@FormParam("question") String question) {
		
		return null;
	}
	

	@POST
	@Path("/GetStep")
	@Produces("application/json")
	public AnswerResponse getStep(@FormParam("stepId") String stepId, @FormParam("step_direction") String stepDirectionString ) {
		STEP_NEIGHBOR relativeDirection = null;
		int direction = Integer.parseInt(stepDirectionString);
		
		switch(direction){
			case 1000:
				relativeDirection = STEP_NEIGHBOR.UP;
				break;
			case 0100:
				relativeDirection = STEP_NEIGHBOR.NEXT;
				break;
			case 0010:
				relativeDirection = STEP_NEIGHBOR.DOWN;
				break;
			case 0001:
				relativeDirection = STEP_NEIGHBOR.PREVIOUS;
				break;
			default:
				relativeDirection = STEP_NEIGHBOR.NEXT;
		}
		
		KnowledgeEngine knowledgeEngine = KES.getInstance().getKnowledgeEngine();
		return knowledgeEngine.getStepNeighbor(stepId, relativeDirection);
	}
	
	@POST
	@Path("/CreateQuestion")
	@Produces("application/json")
	public boolean createQuestion(@FormParam("question") String question) {
		KnowledgeEngine knowledgeEngine = KES.getInstance().getKnowledgeEngine();
		return knowledgeEngine.createQuestion(question);
	}
	
	@POST
	@Path("/AddFirstStep")
	@Produces("application/json")
	public String addFirstStep(@FormParam("question") String question, @FormParam("step_description") String stepDescription ) {
		KnowledgeEngine knowledgeEngine = KES.getInstance().getKnowledgeEngine();
		return knowledgeEngine.addFirstStep(question, stepDescription);
	}
	
	@POST
	@Path("/AddFirstStep")
	@Produces("application/json")
	public String addStep(@FormParam("previous_step_id") String previousStepId, @FormParam("step_description") String stepDescription ) {
		KnowledgeEngine knowledgeEngine = KES.getInstance().getKnowledgeEngine();
		return knowledgeEngine.addStep(previousStepId, null, stepDescription);
	}	
}
