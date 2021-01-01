package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteComment;

public class CommentOperations {

	// Login details
	static final String LOGIN_NAME = "username";
	static final String LOGIN_PASSWORD = "password";

	// Issue constants
	static final String ISSUE_KEY = "TST-22840";

	// Comment constants
	static final String COMMENT_BODY = "J-Tricks Comment";
	//static final String ROLE_LEVEL = "10001";

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

			// Add comment to our issue
			addCommentToIssue(jiraSoapService, authToken);

			// Get All comments. You can also get Comment by the comment ID.
			// Hover over 'edit' in the UI to find the comment ID in the URL
			RemoteComment[] comments = printComments(jiraSoapService, authToken);

			// Edit the comment with more details
			editComment(jiraSoapService, authToken, comments[0]);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static RemoteComment[] printComments(JiraSoapService jiraSoapService, String authToken) throws RemoteException {
		RemoteComment[] comments = jiraSoapService.getComments(authToken, ISSUE_KEY);
		for (RemoteComment remoteComment : comments) {
			System.out.println("Comment:" + remoteComment.getBody() + " written by " + remoteComment.getAuthor());
		}
		return comments;
	}

	private static void editComment(JiraSoapService jiraSoapService, String authToken, RemoteComment comment) throws RemoteException {
		// Check permissions first
		if (jiraSoapService.hasPermissionToEditComment(authToken, comment)) {
			comment.setBody(COMMENT_BODY + " Updated");
			comment.setGroupLevel("jira-users"); 

			jiraSoapService.editComment(authToken, comment);
			System.out.println("Updated!");
		}
	}

	private static void addCommentToIssue(JiraSoapService jiraSoapService, String authToken) throws RemoteException {
		// Adding a comment
		final RemoteComment comment = new RemoteComment();
		comment.setBody(COMMENT_BODY);
		//comment.setRoleLevel(ROLE_LEVEL); // Id of your project role
		comment.setGroupLevel(null); // Make it visible to all
		jiraSoapService.addComment(authToken, ISSUE_KEY, comment);
	}
}
