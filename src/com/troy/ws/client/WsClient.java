package com.troy.ws.client;

import java.net.URL;

import javax.xml.ws.Service;
import javax.xml.ws.WebServiceRef;
import javax.xml.namespace.QName; 
import com.troy.ws.client.RemoteSubmitActivity;

public class WsClient {
    //@WebServiceRef(wsdlLocation="http://localhost:7001/AcsActivity/RemoteSubmitActivityService?WSDL")
    //static RemoteSubmitActivityService service;
    
    private static final String SERVICE_NAME = "RemoteSubmitActivityService";  
    private static final String WSDL_LOCATION = "http://localhost:7001/AcsActivity/RemoteSubmitActivityService?WSDL";  
    private Service service;  

    public static void main(String[] args) {
        try {
        	WsClient client = new WsClient();
            client.callActivityService("Payload|Test");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int callActivityService(String payload) {
        try {
        	service = Service.create(new URL(WSDL_LOCATION), new QName("http://ws.troy.com/",SERVICE_NAME));  
            System.out.println("[TROY-WsClient] : Retrieving the port from the following service: " + service);
            RemoteSubmitActivity port = service.getPort(RemoteSubmitActivity.class);
            System.out.println("[TROY-WsClient] : Invoking the activity submition operation on the port.");

            int response = port.submitActivity(payload);
            System.out.println("[TROY-WsClient] : Response " + response);
            return response;
        } catch(Exception e) {
        	e.printStackTrace();
        	return 0;
        }
    }
}
