package co.uniandes.howtobogota.jenaont;

import co.uniandes.howtobogota.engine.KnowledgeEngine.STEP_NEIGHBOR;

public class Step {
    private String stepId;
    private String stepDescription;
    private String nextStep;
    private String previousStep;
    private String upStep;
    private String downStep;

    public Step(String stepId, String stepDescription) {
      super();
      this.stepId=stepId;
      this.setStepDescription(stepDescription);
      this.nextStep=null;
      this.previousStep=null;
      this.upStep=null;
      this.downStep=null;
    }

    public String getNextStep() {
		return nextStep;
	}

	public void setNextStep(String nextStep) {
		this.nextStep = nextStep;
	}

	public String getPreviousStep() {
		return previousStep;
	}

	public void setPreviousStep(String previousStep) {
		this.previousStep = previousStep;
	}

	public String getUpStep() {
		return upStep;
	}

	public void setUpStep(String upStep) {
		this.upStep = upStep;
	}

	public String getDownStep() {
		return downStep;
	}

	public void setDownStep(String downStep) {
		this.downStep = downStep;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}
	
	public String getNeighborhoodString() {
		char[] neighbors = "0000".toCharArray();
	      if (upStep != null)
	        neighbors[0] = '1';
	      if (nextStep != null)
	        neighbors[1] = '1';
	      if (downStep != null)
	        neighbors[2] = '1';
	      if (previousStep != null)
	        neighbors[3] = '1';

	      return "" + String.valueOf(neighbors);
	    }

	    public String getStepByDirection(STEP_NEIGHBOR stepDirection) {
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
