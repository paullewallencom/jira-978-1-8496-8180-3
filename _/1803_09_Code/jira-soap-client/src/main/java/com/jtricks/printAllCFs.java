package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteCustomFieldValue;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteValidationException;

public class printAllCFs {

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
			getCFsFromIssue(jiraSoapService, authToken);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void getCFsFromIssue(JiraSoapService jiraSoapService, String authToken) throws RemotePermissionException,
			RemoteValidationException, RemoteAuthenticationException, com.atlassian.jira.rpc.soap.client.RemoteException, RemoteException {
		RemoteIssue issue = jiraSoapService.getIssue(authToken, ISSUE_KEY);

		RemoteCustomFieldValue[] cfValues = issue.getCustomFieldValues();
		for (RemoteCustomFieldValue remoteCustomFieldValue : cfValues) {
			String[] values = remoteCustomFieldValue.getValues();
			for (String value : values) {
				System.out.println("Value for CF with Id:" + remoteCustomFieldValue.getCustomfieldId() + " -" + value);
			}
		}
		System.out.println("All CF Values printed!");
	}
}
