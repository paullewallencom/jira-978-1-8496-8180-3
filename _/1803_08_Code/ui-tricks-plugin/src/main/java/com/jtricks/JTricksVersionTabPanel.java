package com.jtricks;

import java.util.Map;

import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.plugin.versionpanel.BrowseVersionContext;
import com.atlassian.jira.plugin.versionpanel.impl.GenericTabPanel;
import com.atlassian.jira.security.JiraAuthenticationContext;

public class JTricksVersionTabPanel extends GenericTabPanel {

	public JTricksVersionTabPanel(JiraAuthenticationContext authenticationContext, SearchProvider searchProvider) {
		super(authenticationContext, searchProvider);
	}

	@Override
	protected Map<String, Object> createVelocityParams(BrowseVersionContext context) {
		Map<String, Object> createVelocityParams = super.createVelocityParams(context);
		createVelocityParams.put("user", context.getUser().getFullName());
		return createVelocityParams;
	}

	@Override
	public boolean showPanel(BrowseVersionContext context) {
		return true;
	}
}
