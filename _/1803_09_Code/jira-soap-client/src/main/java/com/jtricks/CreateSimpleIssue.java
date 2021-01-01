package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteComponent;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteValidationException;
import com.atlassian.jira.rpc.soap.client.RemoteVersion;

public class CreateSimpleIssue {

	// Login details
	static final String LOGIN_NAME = "username";
	static final String LOGIN_PASSWORD = "password";

	// Constants for issue creation
	static final String PROJECT_KEY = "TST";
	static final String ISSUE_TYPE_ID = "1";
	static final String PRIORITY_ID = "4";
	static final String COMPONENT_ID = "10240";
	static final String VERSION_ID = "10330";

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

			// Create Issue
			RemoteIssue issue = createIssue(jiraSoapService, authToken);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static RemoteIssue createIssue(JiraSoapService jiraSoapService, String authToken) throws RemotePermissionException,
			RemoteValidationException, RemoteAuthenticationException, com.atlassian.jira.rpc.soap.client.RemoteException, RemoteException {
		// Populate the issue
		RemoteIssue issue = new RemoteIssue();
		issue.setProject(PROJECT_KEY);
		issue.setType(ISSUE_TYPE_ID);

		issue.setSummary("Test Issue via my tutorial");
		issue.setPriority(PRIORITY_ID);
		issue.setDuedate(Calendar.getInstance());
		issue.setAssignee("");

		// Add remote compoments - Should be an array of RemoteComponent
		RemoteComponent component = new RemoteComponent();
		component.setId(COMPONENT_ID);
		issue.setComponents(new RemoteComponent[] { component });

		// Add remote versions - Should be an array of RemoteVersion
		RemoteVersion version = new RemoteVersion();
		version.setId(VERSION_ID);
		RemoteVersion[] remoteVersions = new RemoteVersion[] { version };
		issue.setFixVersions(remoteVersions);

		// Run the create issue code
		RemoteIssue createdIssue = jiraSoapService.createIssue(authToken, issue);

		System.out.println("\tSuccessfully created issue " + createdIssue.getKey());
		return createdIssue;

	}

}
