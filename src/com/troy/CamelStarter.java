package com.troy;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelStarter {

	public static void main(String[] args) throws Exception {
		
		System.out.println("************************************");
		System.out.println("****      Troy EIP Service.     ****");
		System.out.println("****      Auth : Tanmoy Roy     ****");
		System.out.println("************************************");
		  

		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new IntegrationRoute());
		
		System.out.println("Starting camel context.");
		
		context.start(); 
		
		while(true)
		{
			//System.out.println("Status : " + context.getRoutes().get(0).getId());
		}
		
		//context.stop();
	
	}

}