package com.jtricks;

import java.util.Vector;

public interface XmlRpcService {
	
	String login(String username, String password) throws Exception;
	
	Vector getprojectCategories(String token);

}
