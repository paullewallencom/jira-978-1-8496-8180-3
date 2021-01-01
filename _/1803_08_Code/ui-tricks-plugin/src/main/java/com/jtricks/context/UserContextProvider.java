package com.jtricks.context;

import java.util.Map;

import com.atlassian.core.util.map.EasyMap;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.opensymphony.user.User;

public class UserContextProvider extends AbstractJiraContextProvider {

	@Override
	public Map getContextMap(User user, JiraHelper helper) {
		return EasyMap.build("userName", user.getFullName());
	}

}
