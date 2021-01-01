package com.jtricks;

import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.rpc.soap.beans.AbstractNamedRemoteEntity;

public class RemoteCategory extends AbstractNamedRemoteEntity {

	private String description;

	public RemoteCategory(GenericValue value) {
		super(value);
		this.description = value.getString("description");
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
