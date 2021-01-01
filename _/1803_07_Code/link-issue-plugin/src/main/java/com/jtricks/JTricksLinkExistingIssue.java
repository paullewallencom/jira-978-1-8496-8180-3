package com.jtricks;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.bc.issue.comment.CommentService;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.screen.FieldScreenRendererFactory;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.web.action.issue.LinkExistingIssue;

public class JTricksLinkExistingIssue extends LinkExistingIssue {

	private final IssueManager issueManager;

	public JTricksLinkExistingIssue(IssueLinkManager issueLinkManager, IssueLinkTypeManager issueLinkTypeManager,
			SubTaskManager subTaskManager, FieldManager fieldManager,
			FieldScreenRendererFactory fieldScreenRendererFactory, ProjectRoleManager projectRoleManager,
			CommentService commentService, IssueManager issueManager) {
		super(issueLinkManager, issueLinkTypeManager, subTaskManager, fieldManager, fieldScreenRendererFactory,
				projectRoleManager, commentService);
		this.issueManager = issueManager;
	}

	@Override
	protected void doValidation() {
		super.doValidation();

		List<String> invalidIssues = new ArrayList<String>();

		for (String key : getLinkKey()) {
			MutableIssue issue = this.issueManager.getIssueObject(key);
			if (issue.getIssueTypeObject().getName().equals("New Feature")) {
				invalidIssues.add(key);
			}
		}

		if (!invalidIssues.isEmpty()) {
			addErrorMessage("Linking not allowed to New Features:" + getString(invalidIssues));
		}
	}

	private String getString(List<String> invalidIssues) {
		StringBuffer invalidIssue = new StringBuffer("{ ");
		for (String key : invalidIssues) {
			invalidIssue.append(key + " ");
		}
		invalidIssue.append("}");
		return invalidIssue.toString();
	}

}
