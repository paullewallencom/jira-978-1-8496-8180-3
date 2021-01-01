package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemotePermission;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemotePermissionScheme;
import com.atlassian.jira.rpc.soap.client.RemoteProject;
import com.atlassian.jira.rpc.soap.client.RemoteProjectRole;
import com.atlassian.jira.rpc.soap.client.RemoteUser;
import com.atlassian.jira.rpc.soap.client.RemoteValidationException;

public class Administer {

	// Login details
	static final String LOGIN_NAME = "jobinkk";
	static final String LOGIN_PASSWORD = "jira_123";

	public static void main(String[] args) {
		System.out.println("Executing J-tricks' JIRA SOAP Client");

		String baseUrl = "http://localhost:8080/rpc/soap/jirasoapservice-v2";

		try {
			// Create new Session
			SOAPSession soapSession = new SOAPSession(new URL(baseUrl));

			// Now login
			soapSession.connect(LOGIN_NAME, LOGIN_PASSWORD);

			// Get the JIRA SOAP Service and authentication token
			JiraSoapService jiraSoapService = soapSession.getJiraSoapService();
			String authToken = soapSession.getAuthenticationToken();

			RemotePermissionScheme permScheme = createPermissionScheme(jiraSoapService, authToken);

			RemotePermission[] permissions = jiraSoapService.getAllPermissions(authToken);
			RemotePermission adminPermission = null;
			for (RemotePermission remotePermission : permissions) {
				if (remotePermission.getPermission().equals(23L)) {
					adminPermission = remotePermission;
					break;
				}
			}

			RemoteUser user = jiraSoapService.getUser(authToken, "jobinkk");

			permScheme = jiraSoapService.addPermissionTo(authToken, permScheme, adminPermission, user);

			RemoteProject project = jiraSoapService.createProject(authToken, "TESTKEY", "Test Name", "Test Description",
					"http://www.j-tricks.com", "jobinkk", permScheme, null, null);

			System.out.println("Created Project:" + project.getId());

			RemoteProjectRole adminRole = jiraSoapService.getProjectRole(authToken, 10002L);
			jiraSoapService.addActorsToProjectRole(authToken, new String[] { "jobinkk" }, adminRole, project,
					"atlassian-user-role-actor");
			
			System.out.println("Done");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static RemotePermissionScheme createPermissionScheme(JiraSoapService jiraSoapService, String authToken)
			throws RemotePermissionException, RemoteValidationException, RemoteAuthenticationException,
			com.atlassian.jira.rpc.soap.client.RemoteException, RemoteException {
		return jiraSoapService.createPermissionScheme(authToken, "Test P Scheme", "Test P Description");
	}
}
