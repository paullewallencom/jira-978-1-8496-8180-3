package com.jtricks;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.jtricks.provider.MyComponent;

public class RedirectActionNew extends JiraWebActionSupport {

	private final MyComponent myComponent;

	public RedirectActionNew(MyComponent myComponent) {
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
