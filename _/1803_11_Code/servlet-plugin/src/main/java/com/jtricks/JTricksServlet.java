package com.jtricks;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.opensymphony.user.User;

public class JTricksServlet extends HttpServlet {

	private JiraAuthenticationContext authenticationContext;

	private String siteName;
	private String siteAddress;
	private String sharedText;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		authenticationContext = ComponentManager.getInstance().getJiraAuthenticationContext();
		siteName = config.getInitParameter("siteName");
		siteAddress = config.getInitParameter("siteAddress");
		sharedText = config.getServletContext().getInitParameter("sharedText");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		User user = authenticationContext.getUser();
		out.println("Welcome " + (user != null ? user.getFullName() : "Anonymous"));
		out.println("<br>Invoking the servlet...");
		out.println("<br>My Website : <a href=\"" + siteAddress + "\">" + siteName + "</a>");

		doSomething();

		out.println("<br>Shared Text:"+sharedText);
		out.println("<br>Done!");
	}

	private void doSomething() {
		System.out.println("Invoked servlet at " + (new Date()));
	}

}
