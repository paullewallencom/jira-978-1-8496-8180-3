package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteComponent;
import com.atlassian.jira.rpc.soap.client.RemoteCustomFieldValue;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteValidationException;
import com.atlassian.jira.rpc.soap.client.RemoteVersion;

public class BrowseIssue {

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
			browseIssue(jiraSoapService, authToken);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void browseIssue(JiraSoapService jiraSoapService, String authToken) throws RemotePermissionException,
			RemoteValidationException, RemoteAuthenticationException, com.atlassian.jira.rpc.soap.client.RemoteException, RemoteException {
		RemoteIssue issue = jiraSoapService.getIssue(authToken, ISSUE_KEY); // Use getIssueById if using id

		System.out.println("Summary:"+issue.getSummary());
		System.out.println("Description:"+issue.getDescription());
		System.out.println("Priority:"+issue.getPriority());
		System.out.println("Reporter:"+issue.getReporter());
		System.out.println("Assignee:"+issue.getAssignee());
		
		RemoteVersion[] versions = issue.getFixVersions();
		for (RemoteVersion remoteVersion : versions) {
			System.out.println("Fix version:"+remoteVersion.getName());
		}
		
		RemoteComponent[] components = issue.getComponents();
		for (RemoteComponent remoteComponent : components) {
			System.out.println("Component:"+remoteComponent.getName());
		}
	}
}
