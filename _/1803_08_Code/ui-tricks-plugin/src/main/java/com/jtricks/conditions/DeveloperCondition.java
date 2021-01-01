package com.jtricks.conditions;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractJiraCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.opensymphony.user.User;

public class DeveloperCondition extends AbstractJiraCondition {

	@Override
	public boolean shouldDisplay(User user, JiraHelper jiraHelper) {
		return user != null && user.getGroups().contains("jira-developers");
	}

}
