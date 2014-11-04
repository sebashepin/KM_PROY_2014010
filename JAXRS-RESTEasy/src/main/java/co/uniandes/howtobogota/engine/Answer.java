package co.uniandes.howtobogota.engine;

public class Answer {


	private String step_id;
	
	private String step_description;
	
	private String steps_neighborhood;

	public Answer(String step_id, String step_description,
			String steps_neighborhood) {
		super();
		this.step_id = step_id;
		this.step_description = step_description;
		this.steps_neighborhood = steps_neighborhood;
	}

	public String getStep_id() {
		return step_id;
	}

	public void setStep_id(String step_id) {
		this.step_id = step_id;
	}

	public String getStep_description() {
		return step_description;
	}

	public void setStep_description(String step_description) {
		this.step_description = step_description;
	}

	public String getSteps_neighborhood() {
		return steps_neighborhood;
	}

	public void setSteps_neighborhood(String step_neighborhood) {
		this.steps_neighborhood = step_neighborhood;
	}
}
