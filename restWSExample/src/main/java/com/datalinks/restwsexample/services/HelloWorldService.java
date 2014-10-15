package com.datalinks.restwsexample.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloWorldService {

	
	/**
	 * This is one of the most simple REST examples you will ever find
	 * http://localhost:9090/RestWSExample/rest/hello/x
	 *
	 * @param  name A String that will be used in the response message
	 * @return      String, see method below
	 * @see         Image
	 */
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String name) {
 
		String output = "Jersey responds with: hello " + name;
		return Response.status(200).entity(output).build();
 
	}
	 
	
}
