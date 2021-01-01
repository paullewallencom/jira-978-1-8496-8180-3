package com.jtricks;

import com.atlassian.jira.web.action.JiraWebActionSupport;

public class DemoAction extends JiraWebActionSupport{
	
	private String userName;
	private String modifiedName;

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String doDefault() throws Exception {
		System.out.println("Preparing to recieve inputs");
		return INPUT;
	}
	
	@Override
	protected String doExecute() throws Exception {
		System.out.println("The user Name I got from input view:"+userName);
		this.modifiedName = "Hi,"+userName;
		return SUCCESS;
	}

	public String getModifiedName() {
		return modifiedName;
	}

}
