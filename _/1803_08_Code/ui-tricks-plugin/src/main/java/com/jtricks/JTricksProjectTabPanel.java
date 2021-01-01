package com.jtricks;

import com.atlassian.jira.plugin.projectpanel.impl.AbstractProjectTabPanel;
import com.atlassian.jira.project.browse.BrowseContext;

public class JTricksProjectTabPanel extends AbstractProjectTabPanel{

	public boolean showPanel(BrowseContext ctx) {
		return true;
	}

}
