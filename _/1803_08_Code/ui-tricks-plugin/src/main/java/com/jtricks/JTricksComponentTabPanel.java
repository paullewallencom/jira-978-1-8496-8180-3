package com.jtricks;

import java.util.Map;

import com.atlassian.jira.plugin.componentpanel.BrowseComponentContext;
import com.atlassian.jira.plugin.componentpanel.impl.GenericTabPanel;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;

public class JTricksComponentTabPanel extends GenericTabPanel{

	public JTricksComponentTabPanel(ProjectManager projectManager, JiraAuthenticationContext authenticationContext) {
		super(projectManager, authenticationContext);
	}

	@Override
	protected Map<String, Object> createVelocityParams(BrowseComponentContext context) {
		Map<String, Object> createVelocityParams = super.createVelocityParams(context);
		createVelocityParams.put("user", context.getUser().getFullName());
		return createVelocityParams;
	}

	@Override
	public boolean showPanel(BrowseComponentContext context) {
		return true;
	}

}
