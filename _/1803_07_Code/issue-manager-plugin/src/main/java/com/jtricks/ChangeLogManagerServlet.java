package com.jtricks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.IssueService.CreateValidationResult;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.issue.IssueFieldConstants;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueInputParametersImpl;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.issue.history.ChangeItemBean;
import com.atlassian.jira.issue.history.ChangeLogUtils;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.issue.util.IssueChangeHolder;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.opensymphony.user.User;

public class ChangeLogManagerServlet extends HttpServlet {

	private IssueService issueService;
	private JiraAuthenticationContext authenticationContext;
	private ChangeHistoryManager changeHistoryManager;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		issueService = ComponentManager.getInstance().getIssueService();
		authenticationContext = ComponentManager.getInstance().getJiraAuthenticationContext();
		changeHistoryManager = ComponentManager.getInstance().getChangeHistoryManager();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();

		out.println("Creating Issue...");
		out.flush();

		User user = authenticationContext.getUser();

		MutableIssue issue = createIssue(out, user);

		out.println("Creating dummy summary change history");
		addChangeHistoryForSummary(issue, user);
		
		out.println("Creating dummy status change history for yesterday!");
		addChangeHistoryForStatus(issue, user);
		
		out.println("Creating Custom change histroy");
		addCustomChangeHistory(issue, user);

		out.println("\n... And we are done!");
	}

	private void addCustomChangeHistory(MutableIssue issue, User user) {
		ChangeItemBean changeBean = new ChangeItemBean(ChangeItemBean.CUSTOM_FIELD, "Some Heading",
				"Some Old Value", "Some New Value");

		createChangeGroup(issue, user, changeBean);
	}

	private void addChangeHistoryForStatus(MutableIssue issue, User user) {
		ChangeItemBean changeBean = new ChangeItemBean(ChangeItemBean.STATIC_FIELD, IssueFieldConstants.STATUS,
				"1", "Open", "3", "In Progress");

		createChangeGroup(issue, user, changeBean);
	}

	private void addChangeHistoryForSummary(MutableIssue issue, User user) {

		ChangeItemBean changeBean = new ChangeItemBean(ChangeItemBean.STATIC_FIELD, IssueFieldConstants.SUMMARY,
				"Old Summary", "New Summary");

		createChangeGroup(issue, user, changeBean);
	}

	private void createChangeGroup(MutableIssue issue, User user, ChangeItemBean changeBean) {
		IssueChangeHolder changeHolder = new DefaultIssueChangeHolder();
		changeHolder.addChangeItem(changeBean);

		// create and store the changelog for this whole process
		GenericValue updateLog = ChangeLogUtils.createChangeGroup(user, issue, issue, changeHolder.getChangeItems(),
				false);
	}

	private MutableIssue createIssue(PrintWriter out, User user) {
		IssueInputParameters issueInputParameters = new IssueInputParametersImpl();
		issueInputParameters.setProjectId(10000L).setIssueTypeId("1").setSummary("Test Summary").setReporterId(
				"jobinkk").setAssigneeId("jobinkk").setDescription("Test Description").setStatusId("1").setPriorityId(
				"2").setFixVersionIds(10000L);

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
