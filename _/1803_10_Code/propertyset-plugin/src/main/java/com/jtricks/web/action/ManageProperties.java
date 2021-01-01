package com.jtricks.web.action;

import java.util.HashMap;

import net.java.ao.Query;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.config.properties.PropertiesManager;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.jtricks.entity.AddressEntity;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;

public class ManageProperties extends JiraWebActionSupport {

	private ActiveObjects ao;

	public ManageProperties(ActiveObjects ao) {
		this.ao = ao;
	}

	@Override
	protected String doExecute() throws Exception {

		System.out.println("Creating boolean property...");
		PropertySet propertySet = PropertiesManager.getInstance().getPropertySet();
		propertySet.setBoolean("mt.custom.key1", new Boolean(true));
		System.out.println("Set property:" + (propertySet.getBoolean("mt.custom.key1") ? "True" : "False"));

		System.out.println("Creating user address property...");
		HashMap entityDetails = new HashMap();
		entityDetails.put("delegator.name", "default");
		entityDetails.put("entityName", "OSUser");
		entityDetails.put("entityId", new Long(10032));
		PropertySet userProperties = PropertySetManager.getInstance("ofbiz", entityDetails);

		userProperties.setString("state", "Kerala");
		userProperties.setString("country", "India");

		System.out.println("Set Address:" + userProperties.getString("state") + ", "
				+ userProperties.getString("country"));

		System.out.println("Creating Address Active Object...");
		addAddress("jobinkk", "Kerala", "India");

		AddressEntity address = getAddress("jobinkk");

		System.out.println("Editing State...");
		editState(address, "Ipswich");

		System.out.println("Deleting...");
		deleteAddress(address);

		System.out.println("\n... And we are done!");
		return SUCCESS;
	}

	private void deleteAddress(AddressEntity address) {
		ao.delete(address);
	}

	private void editState(AddressEntity address, String newState) {
		address.setState(newState);
		address.save();
	}

	private AddressEntity getAddress(String name) {
		AddressEntity[] addressEntities = ao.find(AddressEntity.class, Query.select().where("name = ?", name));
		for (AddressEntity addressEntity : addressEntities) {
			System.out.println("Name:" + addressEntity.getName() + ", State:" + addressEntity.getState() + ", Country:"
					+ addressEntity.getCountry());
		}
		return addressEntities[0];
	}

	private void addAddress(String name, String state, String country) {
		AddressEntity addressEntity = ao.create(AddressEntity.class);
		addressEntity.setName(name);
		addressEntity.setState(state);
		addressEntity.setCountry(country);
		addressEntity.save();
	}

}
