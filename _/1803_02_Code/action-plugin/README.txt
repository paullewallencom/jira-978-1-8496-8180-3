This is a deo plugin to show how new webwrok actions are created in JIRA.

Following is the structure of the code:

-src
	-main
		-java
			-com
				-jtricks
					-DemoAction.java
		-resources
			-templates
				-input.vm
				-joy.vm
				-tears.vm
			-atlassian-plugin.xml
	-test
		-java
		-resources
-pom.xml


To compile and package the plugin, run  'mvn clean package' or 'atlas-rn' if you using atlassian plugin sdk.