package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteFieldValue;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteValidationException;

public class UpdateCFValue {

	// Login details
	static final String LOGIN_NAME = "username";
	static final String LOGIN_PASSWORD = "password";

	// Constants for issue updation
	static final String ISSUE_KEY = "TST-22840";

	static final String CUSTOM_FIELD_KEY_1 = "customfield_10010";
	static final String CUSTOM_FIELD_VALUE_1 = "New Test";

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

			// Update Issue
			RemoteIssue issue = updateIssue(jiraSoapService, authToken);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static RemoteIssue updateIssue(JiraSoapService jiraSoapService, String authToken) throws RemotePermissionException,
			RemoteValidationException, RemoteAuthenticationException, com.atlassian.jira.rpc.soap.client.RemoteException, RemoteException {
		// Create the remote value to update - Key will be the customfield_id and value will be String array
		RemoteFieldValue[] actionParams = new RemoteFieldValue[] { new RemoteFieldValue(CUSTOM_FIELD_KEY_1,
				new String[] { CUSTOM_FIELD_VALUE_1 }) };

		// Update the issue - 3rd parameter is an array of RemoteField Values
		RemoteIssue updatedIssue = jiraSoapService.updateIssue(authToken, ISSUE_KEY, actionParams);
		System.out.println("\tSuccessfully updated issue " + updatedIssue.getKey());
		return updatedIssue;

	}

}
