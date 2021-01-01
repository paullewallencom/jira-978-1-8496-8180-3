package com.jtricks;

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

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.IssueService.CreateValidationResult;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.bc.issue.comment.CommentService;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueInputParametersImpl;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.MutableComment;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.SimpleErrorCollection;
import com.atlassian.jira.web.action.issue.DeleteComment;
import com.opensymphony.user.User;

public class CommentManagerServlet extends HttpServlet {

	private IssueService issueService;
	private CommentService commentService;
	private JiraAuthenticationContext authenticationContext;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		issueService = ComponentManager.getInstance().getIssueService();
		authenticationContext = ComponentManager.getInstance().getJiraAuthenticationContext();
		commentService = ComponentManager.getInstance().getComponentInstanceOfType(CommentService.class);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();

		out.println("Creating Issue...");
		out.flush();

		User user = authenticationContext.getUser();

		MutableIssue issue = createIssue(out, user);
		
		out.println("Add Comment");
		Comment comment1 = addComment("My Comment1", issue, user);
		
		out.println("Add Comment Restricted to role!");
		Comment comment2 = addCommentRole("My Comment2", issue, user, 10001L);
		
		out.println("Add Comment Restricted to group!");
		Comment comment3 = addCommentGroup("My Comment3", issue, user, "jira-administrators");
		
		out.println("Editing first comment!");
		editComment("My New Comment", issue, user, comment1);
		
		out.println("Deleting second comment!");
		deleteComment(comment2, user);
		
		out.println("\n... And we are done!");
	}

	private void deleteComment(Comment comment, User user) {
		this.commentService.delete(new JiraServiceContextImpl(user), comment, false);
	}

	private void editComment(String commentString, MutableIssue issue, User user, Comment comment) {
		MutableComment comm = this.commentService.getMutableComment(user, comment.getId(), new SimpleErrorCollection());
		this.commentService.update(user, comm, false, new SimpleErrorCollection());
	}

	private Comment addCommentRole(String commentString, MutableIssue issue, User user, Long roleId) {
		return this.commentService.create(user, issue, commentString, null, roleId, false, new SimpleErrorCollection());
	}
	
	private Comment addCommentGroup(String commentString, MutableIssue issue, User user, String group) {
		return this.commentService.create(user, issue, commentString, group, null, false, new SimpleErrorCollection());
	}

	private Comment addComment(String commentString, MutableIssue issue, User user) {
		return this.commentService.create(user, issue, commentString, false, new SimpleErrorCollection());		
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
