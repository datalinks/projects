package com.datalinks.restwsexample.services;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;

public class HelloWorldServiceTest {

	private static String TST_URL = "http://localhost:9090/RestWSExample/rest/hello/chris";
	
	@Test
	public void testGetMsg() {

		String serviceResponse 	= Client.create().resource(TST_URL).accept(MediaType.TEXT_PLAIN).get(String.class);
		String expectedResponse = "Jersey responds with: hello chris";
		assertTrue("Expected Value: "+expectedResponse+" actual value: "+serviceResponse, serviceResponse.equals(expectedResponse));

	}

}
