package com.troy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.builder.RouteBuilder;

public class IntegrationRoute extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
					
		try{

			System.out.println("Building route.");
			//from("imaps://imap.gmail.com?username=acs.wls.icici@gmail.com&password=****@1"
				//	+ "&delete=false&unseen=true&consumer.delay=2000&debugMode=false").routeId("TroyPoller").process(new LoggingProcessor()).to("file:c:/delete/inbox/dvd/");
	
			from("imaps://imap.gmail.com?username=acs.wls.icici@gmail.com&password=***"
					+ "&delete=false&unseen=true&consumer.delay=2000&debugMode=false").routeId("TroyEmailPoller")
					.choice()
					.when(header("from").contains("@gmail.com")).to("file:c:/delete/inbox/Gmail/")
					.when(header("from").contains("@oracle.com")).process(new OracleUserProcessor()).to("smtps://smtp.gmail.com:465?username=acs.wls.icici@gmail.com&password=Acs_welcome@1")
					.otherwise().to("file:c:/delete/inbox/else/")
					.end();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void myGmail() {
		System.out.println("[TROY] - Inside myGmail().");
	}

}