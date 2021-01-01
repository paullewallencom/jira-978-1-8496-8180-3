package com.jtricks.dictionary;

public class UKDictionary implements Dictionary {

	public String getDefinition(String text) {
		if (text.equals("JIRA")){
			return "JIRA in London!";
		} else {
			return "What are you asking? We in UK don't know anything other than JIRA!!";
		}
	}

}
