package com.jtricks.provider;

import com.atlassian.jira.web.action.JiraWebActionSupport;

public class RedirectAction extends JiraWebActionSupport {

	private final MyComponent myComponent;

	public RedirectAction(MyComponent myComponent) {
		this.myComponent = myComponent;
	}

	@Override
	protected String doExecute() throws Exception {
		System.out.println("Execute the method in component!");
		this.myComponent.doSomething();
		System.out.println("Succesfully executed. Go to dashboard");
		return getRedirect("/secure/Dashboard.jspa");
	}

}
