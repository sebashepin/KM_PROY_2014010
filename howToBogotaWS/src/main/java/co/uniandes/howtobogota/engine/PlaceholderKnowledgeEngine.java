package co.uniandes.howtobogota.engine;

import java.util.HashMap;
import java.util.Hashtable;

import co.uniandes.howtobogota.engine.AnswerResponse.STATUS;

/**
 * @author Sebastian Does NOT implement the ranking system Only matches EXACT questions
 */
public class PlaceholderKnowledgeEngine implements KnowledgeEngine {

  private HashMap<String, Step> stepsByQuestion;
  private Hashtable<String, Step> stepsById;
  private int stepIdCounter = 0;

  public PlaceholderKnowledgeEngine() {
    stepsByQuestion = new HashMap<String, PlaceholderKnowledgeEngine.Step>();
    stepsById = new Hashtable<String, PlaceholderKnowledgeEngine.Step>();
    loadPlaceholderData();
  }

  @Override
  public AnswerResponse getAnswerToQuestion(String question) {
    AnswerResponse result;
    Step step = stepsByQuestion.get(question);
    if (step == null)
      result = new AnswerResponse(STATUS.NEW, null);
    else
      result =
          new AnswerResponse(STATUS.OK, new Answer(step.stepId, step.stepDescription,
              step.getNeighborhoodString()));

    return result;
  }

  @Override
  public AnswerResponse getStepNeighbor(String stepId, STEP_NEIGHBOR stepDirection) {
    AnswerResponse result;
    Step step = stepsById.get(stepId);
    if (step == null)
      result = new AnswerResponse(STATUS.INVALID, null);
    else {
      step = step.getStepByDirection(stepDirection);
      if (step == null) {
        result = new AnswerResponse(STATUS.NEW, null);
      } else {
        result =
            new AnswerResponse(STATUS.OK, new Answer(step.stepId, step.stepDescription,
                step.getNeighborhoodString()));
      }
    }
    return result;
  }

  @Override
  public String addStep(String previousStepId, String nextStepId, String stepDescription) {
    Step step = new Step(stepDescription);
    step.stepId = stepIdCounter + "";
    stepIdCounter++;
    step.previousStep = stepsById.get(previousStepId);
    if (step.previousStep.nextStep != null) {
      Step newUpStep = getBottom(step.previousStep.nextStep);
      newUpStep.downStep = step;
      step.upStep = step;
    } else
      step.previousStep.nextStep = step;
    if (nextStepId != null) {
      step.nextStep = stepsById.get(nextStepId);
    }

    stepsById.put(step.stepId, step);

    return step.stepId;
  }

  @Override
  public boolean createQuestion(String question) {
    if (stepsByQuestion.get(question) == null) {
      stepsByQuestion.put(question, null);
      return true;
    } else
      return false;
  }

  @Override
  public String addFirstStep(String question, String stepDescription) {
    Step step = new Step(stepDescription);
    step.stepId = stepIdCounter + "";
    stepIdCounter++;
    stepsByQuestion.put(question, step);
    stepsById.put(step.stepId, step);
    System.out.println("Step Added");
    return step.stepId;
  }

  private Step getBottom(Step step) {
    if (step.downStep == null)
      return step;
    else
      return getBottom(step.downStep);
  }

  private void loadPlaceholderData() {
    String question = "Placeholder question";
    System.out.println(createQuestion(question));
    String firstStepId = addFirstStep(question, "Initial Step");
    String secondStepId = addStep(firstStepId, null, "Second Step");
    addStep(addStep(firstStepId, null, "Alternative Second Step"), null, "Alternative Third Step");
    String thirdStepId = addStep(secondStepId, null, "Third Step");

    addStep(firstStepId, thirdStepId, "From first step to third");
  }


  class Step {
    String stepId;
    String stepDescription;
    Step nextStep;
    Step previousStep;
    Step upStep;
    Step downStep;
    int rating;

    public Step(String stepDescription) {
      super();
      this.stepDescription = stepDescription;
    }

    public String getNeighborhoodString() {
      int neighbors = 0;
      if (upStep != null)
        neighbors += 1000;
      if (nextStep != null)
        neighbors += 0100;
      if (downStep != null)
        neighbors += 0010;
      if (previousStep != null)
        neighbors += 0001;

      return "" + neighbors;
    }

    public Step getStepByDirection(STEP_NEIGHBOR stepDirection) {
      switch (stepDirection) {
        case UP:
          return upStep;
        case NEXT:
          return nextStep;
        case DOWN:
          return downStep;
        case PREVIOUS:
          return previousStep;
        default:
          return null;
      }
    }
  }


}
