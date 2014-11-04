package co.uniandes.howtobogota.engine;

public class AnswerResponse {
	
	public enum STATUS { OK, NEW, INVALID};
	
	public STATUS status;
	public Answer step;
	public AnswerResponse(STATUS status, Answer step) {
		super();
		this.status = status;
		this.step = step;
	}
	public STATUS getStatus() {
		return status;
	}
	public void setStatus(STATUS status) {
		this.status = status;
	}
	public Answer getStep() {
		return step;
	}
	public void setStep(Answer step) {
		this.step = step;
	}

	
}