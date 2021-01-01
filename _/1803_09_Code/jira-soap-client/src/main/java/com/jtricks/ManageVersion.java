package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteVersion;

public class ManageVersion {

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
			
			//Create Release
			RemoteVersion remoteVersion = new RemoteVersion();
			remoteVersion.setName("Test Release");
			remoteVersion.setReleaseDate(new GregorianCalendar(2011, Calendar.MAY, 10));
			
			RemoteVersion createdVersion = jiraSoapService.addVersion(authToken, "TST", remoteVersion);
			System.out.println("Created version with id:"+createdVersion.getId());
			
			//Release the version
			createdVersion.setReleased(true);
			jiraSoapService.releaseVersion(authToken, "TST", createdVersion);
			
			//Archive version
			jiraSoapService.archiveVersion(authToken, "TST", "Test Release", true);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
