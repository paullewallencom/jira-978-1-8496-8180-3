package com.jtricks;

import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.config.DefaultSubTaskManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.fields.config.manager.IssueTypeSchemeManager;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.util.CollectionReorderer;
import com.opensymphony.user.User;

public class MySubtaskManager extends DefaultSubTaskManager {

	public MySubtaskManager(ConstantsManager constantsManager, IssueLinkTypeManager issueLinkTypeManager,
			IssueLinkManager issueLinkManager, PermissionManager permissionManager,
			ApplicationProperties applicationProperties, CollectionReorderer collectionReorderer,
			IssueTypeSchemeManager issueTypeSchemeManager, IssueManager issueManager) {
		super(constantsManager, issueLinkTypeManager, issueLinkManager, permissionManager, applicationProperties,
				collectionReorderer, issueTypeSchemeManager, issueManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createSubTaskIssueLink(GenericValue parentIssue, GenericValue subTaskIssue, User remoteUser)
			throws CreateException {
		System.out.println("Creating Subtask link in overriden component using GenericValue!");
		super.createSubTaskIssueLink(parentIssue, subTaskIssue, remoteUser);
	}

}
