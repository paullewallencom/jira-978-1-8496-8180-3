package com.jtricks.fragments;

import java.util.Map;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.plugin.projectpanel.fragment.impl.AbstractFragment;
import com.atlassian.jira.project.browse.BrowseContext;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.velocity.VelocityManager;

public class FragmentOne extends AbstractFragment{
	
	protected static final String TEMPLATE_DIRECTORY_PATH = "templates/project/fragments/";

	public FragmentOne(VelocityManager velocityManager, ApplicationProperties applicationProperites,
			JiraAuthenticationContext jiraAuthenticationContext) {
		super(velocityManager, applicationProperites, jiraAuthenticationContext);
	}

	public String getId() {
		return "fragmentone";
	}

	public boolean showFragment(BrowseContext ctx) {
		return true;
	}

	@Override
	protected String getTemplateDirectoryPath() {
		return TEMPLATE_DIRECTORY_PATH;
	}

	@Override
	protected Map<String, Object> createVelocityParams(BrowseContext ctx) {
		Map<String, Object> createVelocityParams = super.createVelocityParams(ctx);
		createVelocityParams.put("user", ctx.getUser().getFullName());
		return createVelocityParams;
	}

}
