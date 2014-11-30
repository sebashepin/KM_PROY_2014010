package co.uniandes.howtobogota.ws;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import co.uniandes.howtobogota.engine.AnswerResponse;
import co.uniandes.howtobogota.engine.KES;
import co.uniandes.howtobogota.engine.KnowledgeEngine;
import co.uniandes.howtobogota.engine.KnowledgeEngine.STEP_NEIGHBOR;

@Path("/")
public class WSEndpoint {

  @GET
  @Path("/test/{pathParameter}")
  @Produces("application/json")
  public Response responseMsg(@PathParam("pathParameter") String pathParameter,
      @DefaultValue("Nothing to say") @QueryParam("queryParameter") String queryParameter) {

    String response = "Hello from: " + pathParameter + " : " + queryParameter;

    return Response.status(200).entity(response).build();
  }

  @GET
  @Path("/answer")
  @Produces("application/json")
  public AnswerResponse placeholderAnswer() {
    KnowledgeEngine knowledgeEngine = KES.getInstance().getKnowledgeEngine();
    return knowledgeEngine.getAnswerToQuestion("Placeholder question");
  }

  @POST
  @Path("/GetAnswer")
  @Produces("application/json")
  public AnswerResponse getAnswer(@FormParam("question") String question) {
    KnowledgeEngine knowledgeEngine = KES.getInstance().getKnowledgeEngine();
    System.out.println("Question received:\t"+question);
    return knowledgeEngine.getAnswerToQuestion(question);
  }

  @GET
  @Path("/GetStep")
  @Produces("application/json")
  public AnswerResponse getStep(@QueryParam("step_id") String stepId,
      @QueryParam("step_direction") String stepDirectionString) {		
    STEP_NEIGHBOR relativeDirection = null;
    int direction = Integer.parseInt(stepDirectionString);

    switch (direction) {
      case 1000:
        relativeDirection = STEP_NEIGHBOR.UP;
        break;
      case 0100:
        relativeDirection = STEP_NEIGHBOR.NEXT;
        break;
      case 10:
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

  /**
   * @return Created Step stepID - 0 if unsuccesful
   */
  @POST
  @Path("/AddFirstStep")
  @Produces("application/json")
  public String addFirstStep(@FormParam("question") String question,
      @FormParam("step_description") String stepDescription) {
    KnowledgeEngine knowledgeEngine = KES.getInstance().getKnowledgeEngine();
    return knowledgeEngine.addFirstStep(question, stepDescription);
  }

  /**
   * @return Created Step stepID - 0 if unsuccesful
   */
  @POST
  @Path("/AddStep")
  @Produces("application/json")
  public String addStep(@FormParam("previous_step_id") String previousStepId,
      @FormParam("step_description") String stepDescription) {      
    KnowledgeEngine knowledgeEngine = KES.getInstance().getKnowledgeEngine();
    return knowledgeEngine.addStep(previousStepId, null, stepDescription);
  }
  
  /**
   * @return Created Step stepID - 0 if unsuccesful
   */
  @POST
  @Path("/CreateAndAnswerQuestion")
  @Produces("application/json")
  public boolean createAndAnswerQuestion(@FormParam("questionCreated") String question,
      @FormParam("steps") List<String> steps, @Context UriInfo info) {
    String[] stepsArray = new String[steps.size()];
    steps.toArray(stepsArray);
    
    
    KnowledgeEngine knowledgeEngine = KES.getInstance().getKnowledgeEngine();
    return knowledgeEngine.createAndAnswerQuestion(question, steps.toArray());
  }
}
