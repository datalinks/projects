package com.datalinks.restwsexample.clients;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;

import com.datalinks.restwsexample.model.ProductXML;

public class RestServiceClientTest {

	
	RestServiceClient rsc = null;

	@Before
	public void init(){
		rsc = new RestServiceClient();
	}
	
	@Test
	public void testDoPostCall() {

		String actualResult 	= rsc.doRestPostCall();
		String expectedResult	= "Product saved as JSON : Product [name=Fade To Black, category=cat]";
		assertTrue("Expected: "+expectedResult+" actual result: "+actualResult,actualResult.equals(expectedResult));	
	}
	
	@Test
	public void testDoRestGetCallReturnXML() throws JAXBException {

		String actualResult 	= rsc.doGetXMLProduct();
		JAXBContext jaxbContext = JAXBContext.newInstance(ProductXML.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		ProductXML product = (ProductXML) unmarshaller.unmarshal(new StringReader(actualResult));
		String expectedResult	= "iPad 4";
		assertTrue("Expected: "+expectedResult+" actual result: "+product.getName(),product.getName().equals(expectedResult));
	
	}


	@Test
	public void testDoRestGetCallReturnJsonObject() throws JAXBException {

		String actualResult 	= rsc.doRestGetCallReturnJsonObject();
		String expectedResult 	= "{\"name\":\"iPad 4\",\"category\":\"technology\",\"price\":8723}";
		assertTrue("Expected: "+expectedResult+" actual result: "+actualResult,actualResult.equals(expectedResult));
	}

	
	//rsc.doRestGetCallReturnXMLObject();
	//rsc.doRestGetCallReturnListOfObjects();

	
}
