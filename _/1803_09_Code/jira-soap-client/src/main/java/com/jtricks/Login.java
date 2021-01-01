package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;

public class Login {

	// Login details
	static final String LOGIN_NAME = "username";
	static final String LOGIN_PASSWORD = "password";

	public static void main(String[] args) {
		System.out.println("Executing J-tricks' JIRA SOAP Client");

		String baseUrl = "http://jira.atlassian.com/rpc/soap/jirasoapservice-v2";

		try {
			// Create new Session
			SOAPSession soapSession = new SOAPSession(new URL(baseUrl));

			// Now login
			soapSession.connect(LOGIN_NAME, LOGIN_PASSWORD);

			// Get the JIRA SOAP Service and authentication token
			JiraSoapService jiraSoapService = soapSession.getJiraSoapService();
			String authToken = soapSession.getAuthenticationToken();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
