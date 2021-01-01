package com.jtricks;

import com.atlassian.jira.web.action.JiraWebActionSupport;

public class RedirectAction extends JiraWebActionSupport{
	
	@Override
	protected String doExecute() throws Exception {
		System.out.println("Action invoked. Doing something important before redirecting to Dashboard!");
		return getRedirect("/secure/Dashboard.jspa");
	}

}
