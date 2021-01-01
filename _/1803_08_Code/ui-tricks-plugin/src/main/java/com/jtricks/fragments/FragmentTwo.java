package com.jtricks.fragments;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.plugin.projectpanel.fragment.impl.AbstractFragment;
import com.atlassian.jira.project.browse.BrowseContext;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.velocity.VelocityManager;

public class FragmentTwo extends AbstractFragment {

	protected static final String TEMPLATE_DIRECTORY_PATH = "templates/project/fragments/";

	public FragmentTwo(VelocityManager velocityManager, ApplicationProperties applicationProperites,
			JiraAuthenticationContext jiraAuthenticationContext) {
		super(velocityManager, applicationProperites, jiraAuthenticationContext);
	}

	public String getId() {
		return "fragmenttwo";
	}

	public boolean showFragment(BrowseContext ctx) {
		return true;
	}

	@Override
	protected String getTemplateDirectoryPath() {
		return TEMPLATE_DIRECTORY_PATH;
	}

}
