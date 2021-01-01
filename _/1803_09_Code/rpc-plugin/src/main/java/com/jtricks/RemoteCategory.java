package com.jtricks;

import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.rpc.soap.beans.AbstractNamedRemoteEntity;

public class RemoteCategory extends AbstractNamedRemoteEntity {

	private String name;

	public RemoteCategory(GenericValue value) {
		super(value);
		this.name = value.getString("name");
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
