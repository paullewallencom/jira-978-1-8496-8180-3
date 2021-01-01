package com.jtricks;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;

public class NewEventListenerModified implements InitializingBean, DisposableBean {

	private final EventPublisher eventPublisher;

	public NewEventListenerModified(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@EventListener
	public void onIssueEvent(IssueEvent issueEvent) {
		System.out.println("Capturing event with ID:" + issueEvent.getEventTypeId() + " here");

		Long eventTypeId = issueEvent.getEventTypeId();
		Issue issue = issueEvent.getIssue();

		if (eventTypeId.equals(EventType.ISSUE_CREATED_ID)) {
			System.out.println("Issue " + issue.getKey() + " has been created");
		} else if (eventTypeId.equals(10033L)) {
			System.out.println("Custom Event thrown here for issue:" + issue.getKey());
		}
	}

	public void afterPropertiesSet() throws Exception {
		eventPublisher.register(this);
	}

	public void destroy() throws Exception {
		eventPublisher.unregister(this);
	}

}
