package com.jtricks.web.action;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.IssueFactory;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutStorageException;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.web.action.issue.CloneIssueDetails;
import com.atlassian.jira.web.action.issue.IssueCreationHelperBean;

public class ExtendedCloneIssueDetails extends CloneIssueDetails {

	private boolean cloneVotes = false;

	public boolean isCloneVotes() {
		return cloneVotes;
	}

	public void setCloneVotes(boolean cloneVotes) {
		this.cloneVotes = cloneVotes;
	}

	public ExtendedCloneIssueDetails(ApplicationProperties applicationProperties, PermissionManager permissionManager,
			IssueLinkManager issueLinkManager, IssueLinkTypeManager issueLinkTypeManager,
			SubTaskManager subTaskManager, AttachmentManager attachmentManager, FieldManager fieldManager,
			IssueCreationHelperBean issueCreationHelperBean, IssueFactory issueFactory, IssueService issueService) {
		super(applicationProperties, permissionManager, issueLinkManager, issueLinkTypeManager, subTaskManager,
				attachmentManager, fieldManager, issueCreationHelperBean, issueFactory, issueService);
	}

	@Override
	protected void setFields() throws FieldLayoutStorageException {
		super.setFields();
		if (isCloneVotes()) {
			getIssueObject().setVotes(getOriginalIssue().getVotes());
		}
	}
}
