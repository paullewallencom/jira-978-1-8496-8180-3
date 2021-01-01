package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteComponent;
import com.atlassian.jira.rpc.soap.client.RemoteCustomFieldValue;
import com.atlassian.jira.rpc.soap.client.RemoteFieldValue;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteValidationException;
import com.atlassian.jira.rpc.soap.client.RemoteVersion;

public class ProgressIssue {

	// Login details
	static final String LOGIN_NAME = "username";
	static final String LOGIN_PASSWORD = "password";

	// Constants for issue updation
	static final String ISSUE_KEY = "TST-22840";

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

			// get CF Details
			progressIssue(jiraSoapService, authToken);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void progressIssue(JiraSoapService jiraSoapService, String authToken)
			throws RemotePermissionException, RemoteValidationException, RemoteAuthenticationException,
			com.atlassian.jira.rpc.soap.client.RemoteException, RemoteException {
		//Should be fields available on the transition screen
		RemoteFieldValue field1 = new RemoteFieldValue("resolution", new String[] { "3" });
		RemoteFieldValue field2 = new RemoteFieldValue("assignee", new String[] { "jobinkk" });

		RemoteIssue updatedtissue = jiraSoapService.progressWorkflowAction(authToken, ISSUE_KEY, "5",
				new RemoteFieldValue[] { field1, field2 });
		
		System.out.println("Progressed "+updatedtissue.getKey());
	}
}
