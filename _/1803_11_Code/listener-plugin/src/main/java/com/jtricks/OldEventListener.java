package com.jtricks;

import java.util.Map;

import com.atlassian.jira.event.issue.AbstractIssueEventListener;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.issue.Issue;

public class OldEventListener extends AbstractIssueEventListener {
	
	private String prop1;

	@Override
	public void issueCreated(IssueEvent event) {
		Issue issue = event.getIssue();
		System.out.println("Issue " + issue.getKey() + " has been created and property is:"+prop1);
	}

	@Override
	public void customEvent(IssueEvent event) {
		Long eventTypeId = event.getEventTypeId();
		Issue issue = event.getIssue();

		if (eventTypeId.equals(10033L)) {
			System.out.println("Custom Event thrown here for issue:" + issue.getKey()+" and property is:"+prop1);
		}
	}

	@Override
	public String[] getAcceptedParams() {
		return new String[] { "prop 1" };
	}
	
	@Override
	public void init(Map params) {
		prop1 = (String) params.get("prop 1");
		System.out.println(prop1);
	}

}
