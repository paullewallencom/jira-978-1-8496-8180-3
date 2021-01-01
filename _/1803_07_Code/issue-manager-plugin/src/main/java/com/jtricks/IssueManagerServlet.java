package com.jtricks;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
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
import com.atlassian.jira.bc.issue.IssueService.DeleteValidationResult;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.bc.issue.IssueService.UpdateValidationResult;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueInputParametersImpl;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.jira.web.util.AttachmentException;
import com.opensymphony.user.User;

public class IssueManagerServlet extends HttpServlet {

	private IssueService issueService;
	private SubTaskManager subTaskManager;
	private JiraAuthenticationContext authenticationContext;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		issueService = ComponentManager.getInstance().getIssueService();
		authenticationContext = ComponentManager.getInstance().getJiraAuthenticationContext();
		subTaskManager = ComponentManager.getInstance().getSubTaskManager();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();

		out.println("Creating Issue...");
		out.flush();

		User user = authenticationContext.getUser();

		MutableIssue issue = createIssue(out, user);

		if (issue != null) {
			out.println("Updating "+issue.getKey()+" ...");
			issue = editIssue(issue, out, user);
			
			out.println("Creating Subtask 1...");
			MutableIssue issue1 = createSubtaskIssue(out, user, issue, 1);
			out.println("Creating Subtask 2...");
			MutableIssue issue2 = createSubtaskIssue(out, user, issue, 2);
			
			out.println("Deleting "+issue1.getKey()+" ...");
			deleteIssue(issue1, out, user);
		}
		
		out.println("\n... And we are done!");
	}

	private void deleteIssue(MutableIssue issue, PrintWriter out, User user) {
		DeleteValidationResult deleteValidationResult = issueService.validateDelete(user, issue.getId());
		 
		if (deleteValidationResult.isValid())
		{
		    ErrorCollection deleteErrors = issueService.delete(user, deleteValidationResult);
		    if (deleteErrors.hasAnyErrors())
		    {
		    	Collection<String> errorMessages = deleteErrors.getErrorMessages();
				for (String errorMessage : errorMessages) {
					out.println(errorMessage);
				}
				out.flush();
		    } else {
		    	out.println("Deleted!");
		    }
		} else {
			Collection<String> errorMessages = deleteValidationResult.getErrorCollection().getErrorMessages();
			for (String errorMessage : errorMessages) {
				out.println(errorMessage);
			}
			Map<String, String> errors = deleteValidationResult.getErrorCollection().getErrors();
			Set<String> errorKeys = errors.keySet();
			for (String errorKey : errorKeys) {
				out.println(errors.get(errorKey));
			}
			out.flush();
		}
	}

	private MutableIssue editIssue(MutableIssue issue, PrintWriter out, User user) {
		IssueInputParameters issueInputParameters = new IssueInputParametersImpl();
		issueInputParameters.setSummary("Modified Summary");

		UpdateValidationResult updateValidationResult = issueService.validateUpdate(user, issue.getId(),
				issueInputParameters);

		if (updateValidationResult.isValid()) {
			IssueResult updateResult = issueService.update(user, updateValidationResult);
			if (updateResult.isValid()) {
				MutableIssue updatedIssue = updateResult.getIssue();
				out.println("Updated " + updatedIssue.getKey() + " with new Summary!");
				out.flush();
				return issue;
			} else {
				Collection<String> errorMessages = updateResult.getErrorCollection().getErrorMessages();
				for (String errorMessage : errorMessages) {
					out.println(errorMessage);
				}
				out.flush();
			}
		} else {
			Collection<String> errorMessages = updateValidationResult.getErrorCollection().getErrorMessages();
			for (String errorMessage : errorMessages) {
				out.println(errorMessage);
			}
			Map<String, String> errors = updateValidationResult.getErrorCollection().getErrors();
			Set<String> errorKeys = errors.keySet();
			for (String errorKey : errorKeys) {
				out.println(errors.get(errorKey));
			}
			out.flush();
		}

		return null;
	}

	private MutableIssue createIssue(PrintWriter out, User user) {
		IssueInputParameters issueInputParameters = new IssueInputParametersImpl();
		issueInputParameters.setProjectId(10000L).setIssueTypeId("1").setSummary("Test Summary")
				.setReporterId("jobinkk").setAssigneeId("jobinkk").setDescription("Test Description")
				.setStatusId("1").setPriorityId("2").setFixVersionIds(10000L);

		CreateValidationResult createValidationResult = issueService.validateCreate(user, issueInputParameters);
		
		MutableIssue issue = create(out, user, createValidationResult);

		return issue;
	}

	private MutableIssue createSubtaskIssue(PrintWriter out, User user, MutableIssue parent, int cnt) {
		IssueInputParameters issueInputParameters = new IssueInputParametersImpl();
		issueInputParameters.setProjectId(10000L).setIssueTypeId("5").setSummary("Test Subtask " + cnt)
				.setDescription("Test Description " + cnt).setStatusId("1");

		CreateValidationResult createValidationResult = issueService.validateSubTaskCreate(user, parent.getId(), issueInputParameters);
		
		MutableIssue issue = create(out, user, createValidationResult);
		
		try {
			createSubtaskLink(issue, parent, user);
		} catch (CreateException e) {
			e.printStackTrace();
			out.println("Not able to create Subtask link " + e.getMessage());
		}

		return issue;
	}

	private void createSubtaskLink(MutableIssue issue, MutableIssue parent, User user) throws CreateException {
		this.subTaskManager.createSubTaskIssueLink(parent, issue, user);
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
