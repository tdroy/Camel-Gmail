package com.troy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.troy.ws.client.WsClient;

public class OracleUserProcessor implements Processor {
	
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

	@Override
	
	public void process(Exchange exchange) throws Exception {
	
		List<String> userList = new ArrayList<String>();
		
		try {
			
			
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/troy?","root","root");
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM USER;");
            
            while (resultSet.next()) {
            	
                userList.add(resultSet.getString("email")); 
            }
            System.out.println("[TROY-OracleUserProcessor] " + userList.toString());
            
			Map<String, Object> msgMap = exchange.getIn().getHeaders();
			System.out.println("[TROY-OracleUserProcessor] In Exchange method.");
			System.out.println("[TROY-OracleUserProcessor] Email message : \r" +		exchange.getIn().getBody(String.class));
			
			
			String fromAddress = (String) msgMap.get("From");
			String toAddress = (String) msgMap.get("To");
			String subject = (String) msgMap.get("Subject");
			String body = exchange.getIn().getBody(String.class);

			String fromEmailId = fromAddress.substring(fromAddress.indexOf("<")+1, fromAddress.lastIndexOf(">"));
			System.out.println("[TROY-OracleUserProcessor] From Address : " + fromEmailId);
			
			InputStream input = new ByteArrayInputStream(body.getBytes());
	        Properties prop = new Properties();
	        prop.load(input);
			
			String payload = fromEmailId 
							+ "|" + prop.getProperty("account")
							+ "|" + prop.getProperty("title")
							+ "|" + prop.getProperty("category")
							+ "|" + prop.getProperty("startTime")
							+ "|" + prop.getProperty("endTime")
							+ "|" + prop.getProperty("severity")
							+ "|" + prop.getProperty("detail")
							;
			System.out.println("[TROY-OracleUserProcessor] Payload = " + payload);
			
			if (userList.toString().contains(fromEmailId)) {
				System.out.println("[TROY-OracleUserProcessor] User : " + fromEmailId + " authorized for activity submission.");
				WsClient client = new WsClient();
				int response = client.callActivityService(payload);
				
				if (response > 0)
					{	
						subject = subject + " === Ref nob #" + response;
						body = "Succesfull \r\r"  + body;
						
						exchange.getOut().setHeader("To", fromAddress);
						exchange.getOut().setHeader("From", toAddress);
						exchange.getOut().setHeader("Subject", subject);
						exchange.getOut().setBody(body);	
						System.out.println("[TROY-OracleUserProcessor] Activity submitted");
					}
				else {
					subject = subject + " === Ref nob # error";
					body = "Some error reported, please contact Admin. \r\r"  + body;
					
					exchange.getOut().setHeader("To", fromAddress);
					exchange.getOut().setHeader("From", toAddress);
					exchange.getOut().setHeader("Subject", subject);
					exchange.getOut().setBody(body);	
					System.out.println("[TROY-OracleUserProcessor] Error in submission.");
				}
			}
			else {
				System.out.println("[TROY-OracleUserProcessor] User : " + fromEmailId + " Not authorized.");
				subject = subject + " === Ref nob # Not authorized";
				body = "Your not autheroized. Please contact Oracle ACS. \r\r"  + body;
				
				exchange.getOut().setHeader("To", fromAddress);
				exchange.getOut().setHeader("From", toAddress);
				exchange.getOut().setHeader("Subject", subject);
				exchange.getOut().setBody(body);	
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}