package com.jtricks;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueFactory;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutStorageException;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.web.action.issue.CloneIssueDetails;
import com.atlassian.jira.web.action.issue.IssueCreationHelperBean;

public class JTricksCloneIssueDetails extends CloneIssueDetails {

	private final CustomFieldManager customFieldManager;

	public JTricksCloneIssueDetails(ApplicationProperties applicationProperties, PermissionManager permissionManager,
			IssueLinkManager issueLinkManager, IssueLinkTypeManager issueLinkTypeManager,
			SubTaskManager subTaskManager, AttachmentManager attachmentManager, FieldManager fieldManager,
			IssueCreationHelperBean issueCreationHelperBean, IssueFactory issueFactory, IssueService issueService,
			CustomFieldManager customFieldManager) {
		super(applicationProperties, permissionManager, issueLinkManager, issueLinkTypeManager, subTaskManager,
				attachmentManager, fieldManager, issueCreationHelperBean, issueFactory, issueService);
		this.customFieldManager = customFieldManager;
	}

	@Override
	protected void setFields() throws FieldLayoutStorageException {
		super.setFields();

		getIssueObject().setFixVersions(null);
		
		CustomField customField = customFieldManager.getCustomFieldObjectByName("Test Number");
		getIssueObject().setCustomFieldValue(customField, null);
	}

}
