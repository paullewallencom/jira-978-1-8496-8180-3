package com.jtricks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

public class Category {

	public static final String JIRA_URI = "http://localhost:8080/jira";
	public static final String RPC_PATH = "/rpc/xmlrpc";
	public static final String USER_NAME = "username";
	public static final String PASSWORD = "password";

	public static void main(String[] args) {
		// Initialise RPC Client
		try {
			XmlRpcClient rpcClient = new XmlRpcClient(JIRA_URI + RPC_PATH);

			// Login and retrieve logon token
			Vector loginParams = new Vector(2);
			loginParams.add(USER_NAME);
			loginParams.add(PASSWORD);
			String loginToken = (String) rpcClient.execute("jtricks.login", loginParams);

			System.out.println("Logged in: " + loginToken);

			// Retrieve projects
			Vector loginTokenVector = new Vector(1);
			loginTokenVector.add(loginToken);
			List categories = (List) rpcClient.execute("jtricks.getprojectCategories", loginTokenVector);
			for (Iterator iterator = categories.iterator(); iterator.hasNext();) {
				Map category = (Map) iterator.next();
				System.out.println(category.get("name"));
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
