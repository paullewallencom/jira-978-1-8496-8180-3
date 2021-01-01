package com.jtricks;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.security.JiraAuthenticationContext;

public class JTricksServletFilter implements Filter {

	private JiraAuthenticationContext authenticationContext;

	public void destroy() {
		System.out.println("Filter destroyed!");
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		// Get the IP address of client machine.
		String ipAddress = request.getRemoteAddr();

		// Log the IP address and current timestamp.
		System.out.println("Intercepted in filter, request by user:" + authenticationContext.getUser().getFullName()
				+ " from IP " + ipAddress + " at " + new Date().toString() + ". Accessed URL:"+request.getRequestURI());

		chain.doFilter(req, res);
	}

	public void init(FilterConfig config) throws ServletException {
		System.out.println("Initiating the filter:" + config.getInitParameter("filterName"));
		authenticationContext = ComponentManager.getInstance().getJiraAuthenticationContext();
	}

}
