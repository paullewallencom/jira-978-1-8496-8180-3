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

public class CreateIssueWithCF {

	// Login details
	static final String LOGIN_NAME = "username";
	static final String LOGIN_PASSWORD = "password";

	// Constants for issue creation
	static final String PROJECT_KEY = "TST";
	static final String ISSUE_TYPE_ID = "1";

	static final String CUSTOM_FIELD_KEY_1 = "customfield_10061";
	static final String CUSTOM_FIELD_VALUE_1 = "10098";
	//It is cascading Select!
	static final String CUSTOM_FIELD_KEY_2 = "customfield_10061:1";
	static final String CUSTOM_FIELD_VALUE_2 = "10105";

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
		// Create the issue with only the mandatory fields and out CFs
		RemoteIssue issue = new RemoteIssue();
		issue.setProject(PROJECT_KEY);
		issue.setType(ISSUE_TYPE_ID);

		issue.setSummary("Test Issue via my tutorial");

		// Add custom fields
		RemoteCustomFieldValue customFieldValue = new RemoteCustomFieldValue(CUSTOM_FIELD_KEY_1, "", new String[] { CUSTOM_FIELD_VALUE_1 });
		RemoteCustomFieldValue customFieldValue2 = new RemoteCustomFieldValue(CUSTOM_FIELD_KEY_2, "", new String[] { CUSTOM_FIELD_VALUE_2 });
		RemoteCustomFieldValue[] customFieldValues = new RemoteCustomFieldValue[] { customFieldValue, customFieldValue2 };
		issue.setCustomFieldValues(customFieldValues);

		// Run the create issue code
		RemoteIssue createdIssue = jiraSoapService.createIssue(authToken, issue);

		System.out.println("\tSuccessfully created issue " + createdIssue.getKey());
		return createdIssue;

	}

}
