package com.jtricks;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.jtricks.issueaction.JTricksAction;
import com.opensymphony.user.User;

public class JTricksIssueTabPanel extends AbstractIssueTabPanel {
	
	private final JiraAuthenticationContext authenticationContext;

	public JTricksIssueTabPanel(JiraAuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
	}

	public List getActions(Issue issue, User remoteUser) {
		List<JTricksAction> panelActions = new ArrayList<JTricksAction>();
		panelActions.add(new JTricksAction(descriptor, authenticationContext));		
		return panelActions;
	}

	public boolean showPanel(Issue issue, User remoteUser) {
		return true;
	}

}
