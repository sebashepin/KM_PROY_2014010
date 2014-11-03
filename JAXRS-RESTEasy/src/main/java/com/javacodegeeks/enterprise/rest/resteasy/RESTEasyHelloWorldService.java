package com.javacodegeeks.enterprise.rest.resteasy;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/RESTEasyHelloWorld")
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
	public Object answerQuestion() {

		AnswerResponse st = new AnswerResponse("OK", new Answer("23", "I'm the first and most popular step", "1101"));

		return st;

	}
	
	class AnswerResponse {
		String status;
		Answer step;
		public AnswerResponse(String status, Answer step) {
			this.status = status;
			this.step = step;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Answer getStep() {
			return step;
		}
		public void setAnswer(Answer answer) {
			this.step = answer;
		}
		
	}
	
}
