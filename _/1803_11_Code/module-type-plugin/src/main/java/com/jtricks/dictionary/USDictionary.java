package com.jtricks.dictionary;

public class USDictionary implements Dictionary {

	public String getDefinition(String text) {
		if (text.equals("JIRA")){
			return "JIRA in San Fransisco!";
		} else {
			return "What are you asking? We in US don't know anything other than JIRA!!";
		}
	}

}
