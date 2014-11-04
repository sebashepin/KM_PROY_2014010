/**
 * 
 */
package co.uniandes.howtobogota.engine;


/**
 * @author Sebastian
 *
 */
public interface KnowledgeEngine {

  public enum STEP_NEIGHBOR {
    PREVIOUS, NEXT, UP, DOWN
  };

  /**
   * Give most popular step given a question
   * 
   * @param question
   * @return Response object or null if no question found
   */
  public AnswerResponse getAnswerToQuestion(String question);

  /**
   * Give step relative to given step and direction
   * 
   * @param stepId
   * @param stepDirection - one of {@link STEP_NEIGHBOR}
   * @return The sought for step or null if no step with given id or no step on given direction
   */
  public AnswerResponse getStepNeighbor(String stepId, STEP_NEIGHBOR stepDirection);

  /**
   * @param previousStepId - This one is mandatory
   * @param nextStepId - This one may be null
   * @param stepDescription
   * @return
   */
  public String addStep(String previousStepId, String nextStepId, String stepDescription);

  public boolean createQuestion(String question);

  public String addFirstStep(String question, String stepDescription);

  public boolean createAndAnswerQuestion(String question, Object[] steps);
}
