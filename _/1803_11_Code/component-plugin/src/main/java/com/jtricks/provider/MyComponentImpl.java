package com.jtricks.provider;

import com.atlassian.jira.security.JiraAuthenticationContext;

public class MyComponentImpl implements MyComponent{
	
	private final JiraAuthenticationContext authenticationContext;
	
	public MyComponentImpl(JiraAuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
	}

	public void doSomething() {
		System.out.println("Hey "+authenticationContext.getUser().getFullName()+", Sample method to check Components");
	}

}
