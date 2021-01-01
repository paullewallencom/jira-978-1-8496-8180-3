package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteGroup;
import com.atlassian.jira.rpc.soap.client.RemoteUser;

public class UsersAndGroups {

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
			
			//Create group jtricks-test-group
			RemoteGroup group = jiraSoapService.createGroup(authToken, "jtricks-test-group", null);
			//Create user jtricks-test-user
			RemoteUser user = jiraSoapService.createUser(authToken, "jtricks-test-user", "password", "Test User", "support@j-tricks.com");
			//Add user to the test group
			jiraSoapService.addUserToGroup(authToken, group, user);
			
			//Browse User
			user = jiraSoapService.getUser(authToken, "jtricks-test-user");
			System.out.println("User Name:"+user.getName());
			
			//Browse Group
			group = jiraSoapService.getGroup(authToken, "jtricks-test-group");
			System.out.println("Group Name:"+group.getName());
			System.out.println("Users in group:");
			RemoteUser[] users = group.getUsers();
			for (RemoteUser remoteUser : users) {
				System.out.println("Full Name:"+remoteUser.getFullname());
			}
			
			//Create new user for deletion
			RemoteUser user1 = jiraSoapService.createUser(authToken, "jtricks-test-user1", "password", "Test User1", "support@j-tricks.com");
			//Create new group for deletion with first user
			RemoteGroup group1 = jiraSoapService.createGroup(authToken, "jtricks-test-group1", user1);
			
			
			//Delete User
			jiraSoapService.deleteUser(authToken, user1.getName());
			//Delete Group. swapGroup identifies the group to change comment and worklog visibility to.
			jiraSoapService.deleteGroup(authToken, group1.getName(), group.getName());
			

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
