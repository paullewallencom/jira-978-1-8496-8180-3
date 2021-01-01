package com.jtricks;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;

public class NewEventListener {

	public NewEventListener(EventPublisher eventPublisher) {
		eventPublisher.register(this);
	}
	
	@EventListener
    public void onIssueEvent(IssueEvent issueEvent) {
		System.out.println("Capturing event with ID:"+issueEvent.getEventTypeId()+" here");
		
		Long eventTypeId = issueEvent.getEventTypeId();
        Issue issue = issueEvent.getIssue();
		
		if (eventTypeId.equals(EventType.ISSUE_CREATED_ID)) {
			System.out.println("Issue "+issue.getKey()+" has been created");
        } else if (eventTypeId.equals(10033L)) {
        	System.out.println("Custom Event thrown here for issue:"+issue.getKey());
        }
    }

}
