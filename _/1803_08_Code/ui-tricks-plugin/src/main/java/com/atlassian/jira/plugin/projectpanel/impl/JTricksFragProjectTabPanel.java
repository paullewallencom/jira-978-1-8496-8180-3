package com.atlassian.jira.plugin.projectpanel.impl;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.plugin.projectpanel.fragment.ProjectTabPanelFragment;
import com.atlassian.jira.project.browse.BrowseContext;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.velocity.VelocityManager;
import com.jtricks.fragments.FragmentOne;
import com.jtricks.fragments.FragmentTwo;

public class JTricksFragProjectTabPanel extends AbstractFragmentBasedProjectTabPanel {

	private final FragmentOne fragmentOne;
	private final FragmentTwo fragmentTwo;

	public JTricksFragProjectTabPanel(VelocityManager velocityManager, ApplicationProperties applicationProperites,
			JiraAuthenticationContext jiraAuthenticationContext) {
		this.fragmentOne = new FragmentOne(velocityManager, applicationProperites, jiraAuthenticationContext);
		this.fragmentTwo = new FragmentTwo(velocityManager, applicationProperites, jiraAuthenticationContext);
	}

	@Override
	protected List<ProjectTabPanelFragment> getLeftColumnFragments(BrowseContext ctx) {
		final List<ProjectTabPanelFragment> frags = new ArrayList<ProjectTabPanelFragment>();
		frags.add(fragmentOne);
		return frags;
	}

	@Override
	protected List<ProjectTabPanelFragment> getRightColumnFragments(BrowseContext ctx) {
		final List<ProjectTabPanelFragment> frags = new ArrayList<ProjectTabPanelFragment>();
		frags.add(fragmentTwo);
		return frags;
	}

	public boolean showPanel(BrowseContext ctx) {
		return true;
	}

}
