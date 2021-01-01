package com.jtricks;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteWorklog;

public class Worklog {

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

			RemoteWorklog worklog = new RemoteWorklog();
			worklog.setComment("Some comment!");
			worklog.setGroupLevel("jira-users");
			worklog.setStartDate(new GregorianCalendar(2011, Calendar.MAY, 10));
			worklog.setTimeSpent("1d 3h");

			//Time logged = 1d 3h, original = 2d 5h, Remaining 1d 2h
			RemoteWorklog work = jiraSoapService
					.addWorklogAndAutoAdjustRemainingEstimate(authToken, ISSUE_KEY, worklog);
			System.out.println("Added work:" + work.getId());

			//Time logged = 2d 6h, original = 2d 5h, Remaining 1d 2h
			RemoteWorklog work1 = jiraSoapService.addWorklogAndRetainRemainingEstimate(authToken, ISSUE_KEY, worklog);
			work1.setTimeSpent("1d 7h");
			//Time logged = 3d 2h, original = 2d 5h, Remaining 1d
			jiraSoapService.updateWorklogWithNewRemainingEstimate(authToken, work1, "1d");
			//Time logged = 1d 3h, original = 2d 5h, Remaining 1d
			jiraSoapService.deleteWorklogAndRetainRemainingEstimate(authToken, work1.getId());

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
