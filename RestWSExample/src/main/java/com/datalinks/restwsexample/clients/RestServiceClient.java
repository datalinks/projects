package com.datalinks.restwsexample.clients;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.datalinks.restwsexample.model.ProductXML;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public class RestServiceClient  {

	
	public String doRestPostCall(){

		Client client = Client.create();
		WebResource webResource = client.resource("http://localhost:9090/RestWSExample/product/post");
		String input = "{\"category\":\"cat\",\"name\":\"Fade To Black\",\"price\":\"1000\"}";	
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}
		String result = response.getEntity(String.class);
		System.out.println("OUTPUT FROM REST POST RETURNS JSON: "+result);
		return result;
	}

	
	public String doRestGetCallReturnXML(){
		String result = Client.create().resource("http://localhost:9090/RestWSExample/product/list/get").accept(MediaType.APPLICATION_XML).get(String.class);
		System.out.println("OUTPUT FROM REST GET RETURNS XML: "+result);		
		return result;
	}
	

	public String doRestGetCallReturnJsonObject(){
	    String result = Client.create().resource("http://localhost:9090/RestWSExample/product/json/get").get(String.class);
		System.out.println("OUTPUT FROM REST GET RETURNS JSON OBJECT: "+result.toString());		
		return result;
	}

	
	public ProductXML doRestGetCallReturnXMLObject(){
		ProductXML result = Client.create().resource("http://localhost:9090/RestWSExample/product/json/get").get(new GenericType<ProductXML>(){});
		System.out.println("OUTPUT FROM REST GET RETURNS XML OBJECT: "+result.toString());		
		return result;
	}

	
	public List<ProductXML> doRestGetCallReturnListOfObjects(){
	    List<ProductXML> result = Client.create().resource("http://localhost:9090/RestWSExample/product/list/get").get(new GenericType<List<ProductXML>>(){});
	    System.out.println("OUTPUT FROM REST GET RETURNS LIST OF OBJECTS: ");		
	    for(ProductXML prd : result){
	    	System.out.println("prod: "+prd.toString());
	    }
		return result;
	}
	

	public String doGetXMLProduct(){
		String result = Client.create().resource("http://localhost:9090/RestWSExample/product/xmlobject/get").accept(MediaType.APPLICATION_XML).get(String.class);
		System.out.println(result);
		return result;
	}

	
	
	public static void main(String[] args){
		
		RestServiceClient rsc = new RestServiceClient();
		rsc.doRestPostCall();
		rsc.doRestGetCallReturnXML();
		rsc.doRestGetCallReturnJsonObject();
		rsc.doRestGetCallReturnXMLObject();
		rsc.doRestGetCallReturnListOfObjects();
		rsc.doGetXMLProduct();
	}
	
	
}
