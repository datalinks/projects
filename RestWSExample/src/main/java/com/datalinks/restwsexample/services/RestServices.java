package com.datalinks.restwsexample.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.datalinks.restwsexample.model.Product;
import com.datalinks.restwsexample.model.ProductXML;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;


@Path("/product")
public class RestServices {

	
		/**
		 * Rest service that return a JSON object, please note the @Produces annotation
		 * http://localhost:9090/RestWSExample/product/json/get
		 *
		 * @return      A Product, which will be wrapped by jersey in a JSON string if the client does a 
		 * 				Client.create().resource("http://localhost:9090/RestWSExample/product/json/get").get(String.class
		 * 				However if the client does a 
		 * 				ProductXML result = Client.create().resource("http://localhost:9090/RestWSExample/product/json/get").get(new GenericType<ProductXML>(){});
		 * 				It will return a ProductXML object...which is nice but weird imho
		 */
		@GET
		@Path("/json/get")
		@Produces("application/json")
		public Product getProductInJSON() {
	 
			Product product = new Product();
			product.setName("iPad 4");
			product.setPrice(8723);
			product.setCategory("technology");
			return product; 
	 
		}

		/**
		 * Rest service that returns a list of objects
		 * http://localhost:9090/RestWSExample/product/list/get
		 *
		 * @return      list of objects, the client must call
		 * List<ProductXML> result = Client.create().resource("http://localhost:9090/RestWSExample/product/list/get").get(new GenericType<List<ProductXML>>(){});
		 */
		@GET
		@Path("/list/get")
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		public List<ProductXML> getListOfProduct() {
	 
			//	Create List of xml objects
			List<ProductXML> result = new ArrayList<ProductXML>();
			//	Creating products
			ProductXML product = new ProductXML();
			product.setName("iPad 4");
			product.setPrice(8723);
			product.setCategory("technology");
			ProductXML product2 = new ProductXML();
			product2.setName("iPad 4.2");
			product2.setPrice(87230);
			product2.setCategory("technology 2");
			//	Adding products to List/ Result			
			result.add(product);
			result.add(product2);
			
			return result;
	 
		}
		

		/**
		 * Rest service that returns a XMLProduct
		 * http://localhost:9090/RestWSExample/product/xmlobject/get
		 *
		 * @return      XMLProduct
		 */
		@GET
		@Path("/xmlobject/get")
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		public ProductXML getXMLProduct() {
	 
			//	Creating products
			ProductXML product = new ProductXML();
			product.setName("iPad 4");
			product.setPrice(8723);
			product.setCategory("technology");
			
			return product;
	 
		}

		
		
		/**
		 * Rest service for e.g. for persisting data
		 * http://localhost:9090/RestWSExample/product/post
		 *
		 * @return      Response, returns a JSON wrapped object as a string
		 */
		@POST
		@Path("/post")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response createTrackInJSON(Product product) {
			String result = "Product saved as JSON : " + product;
			return Response.status(201).entity(result).build();
			
		}
		
		/*
		 * NOT USED, was used for experiment
		 */
		@POST
		@Path("/post2")
		@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		public List<ProductXML> createTrackInJSON2(ProductXML product) {
			System.out.println("invoked with prod "+product.getName());
			product.setName("did something to your name");			
			//ResponseBuilder response = Response.ok((Product) product);			
			//return response.build();
			List<ProductXML> result = new ArrayList<ProductXML>();
			result.add(product);
			ProductXML prod2 = new ProductXML();
			prod2.setName("did something to your name...AGAIN");
			result.add(prod2);
			return result;
			//return Response.status(201).entity(product);
	
		}

}
