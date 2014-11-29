package co.uniandes.howtobogota.engine;

public class AnswerResponse {
	
	public enum STATUS { OK, NEW, INVALID};
	
	private STATUS status;
	private Answer step;
	private String question;
	public AnswerResponse(STATUS status, Answer step) {
		super();
		this.status = status;
		this.step = step;
		this.question="";
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
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}

	
}