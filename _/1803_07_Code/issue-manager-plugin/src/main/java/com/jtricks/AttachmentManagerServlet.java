package com.jtricks;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.core.entity.GenericEntityException;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.IssueService.CreateValidationResult;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.exception.RemoveException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueInputParametersImpl;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.attachment.Attachment;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.action.issue.DeleteAttachment;
import com.atlassian.jira.web.util.AttachmentException;
import com.opensymphony.user.User;

public class AttachmentManagerServlet extends HttpServlet {

	private IssueService issueService;
	private JiraAuthenticationContext authenticationContext;
	private AttachmentManager attachmentManager;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		issueService = ComponentManager.getInstance().getIssueService();
		authenticationContext = ComponentManager.getInstance().getJiraAuthenticationContext();
		attachmentManager = ComponentManager.getInstance().getAttachmentManager();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();

		out.println("Creating Issue...");
		out.flush();

		User user = authenticationContext.getUser();

		MutableIssue issue = createIssue(out, user);
		
		out.println("Attaches files to Issue..");
		
		attachFileOnIssue(issue, user, "G:\\test1.txt", "myFile1");
		attachFileOnIssue(issue, user, "G:\\test2.txt", "myFile2");
		
		printAttachments(issue, out);
		
		out.println("Deleting myFile...");
		
		deleteAttachment(issue, "myFile1");
		
		out.println("\n... And we are done!");
	}

	private void deleteAttachment(MutableIssue issue, String fileName) {
		List<Attachment> attachments = this.attachmentManager.getAttachments(issue);
		Attachment attachmentTBD = null;
		for (Attachment attachment : attachments) {
			if (attachment.getFilename().equals(fileName)){
				attachmentTBD = attachment;
			}
		}
		try {
			this.attachmentManager.deleteAttachment(attachmentTBD);
		} catch (RemoveException e) {
			e.printStackTrace();
		}
	}

	private void printAttachments(MutableIssue issue, PrintWriter out) {
		List<Attachment> attachments = this.attachmentManager.getAttachments(issue);
		for (Attachment attachment : attachments) {
			out.println("Attachment: "+attachment.getFilename()+" attached by "+attachment.getAuthor());
		}
	}

	private void attachFileOnIssue(MutableIssue issue, User user, String fileName, String newFileName) {
		try {
			this.attachmentManager.createAttachment(new File(fileName), newFileName, "text/plain", user, issue.getGenericValue());
		} catch (AttachmentException e) {
			e.printStackTrace();
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
	}

	private MutableIssue createIssue(PrintWriter out, User user) {
		IssueInputParameters issueInputParameters = new IssueInputParametersImpl();
		issueInputParameters.setProjectId(10000L).setIssueTypeId("1").setSummary("Test Summary")
				.setReporterId("jobinkk").setAssigneeId("jobinkk").setDescription("Test Description")
				.setStatusId("1").setPriorityId("2");

		CreateValidationResult createValidationResult = issueService.validateCreate(user, issueInputParameters);
		
		MutableIssue issue = create(out, user, createValidationResult);

		return issue;
	}

	public MutableIssue create(PrintWriter out, User user, CreateValidationResult createValidationResult) {
		MutableIssue issue = null;

		if (createValidationResult.isValid()) {
			IssueResult createResult = issueService.create(user, createValidationResult);
			if (createResult.isValid()) {
				issue = createResult.getIssue();
				out.println("Created " + issue.getKey());
				out.flush();
			} else {
				Collection<String> errorMessages = createResult.getErrorCollection().getErrorMessages();
				for (String errorMessage : errorMessages) {
					out.println(errorMessage);
				}
				out.flush();
			}
		} else {
			Collection<String> errorMessages = createValidationResult.getErrorCollection().getErrorMessages();
			for (String errorMessage : errorMessages) {
				out.println(errorMessage);
			}
			Map<String, String> errors = createValidationResult.getErrorCollection().getErrors();
			Set<String> errorKeys = errors.keySet();
			for (String errorKey : errorKeys) {
				out.println(errors.get(errorKey));
			}
			out.flush();
		}
		return issue;
	}
}
