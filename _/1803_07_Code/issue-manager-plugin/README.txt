* Deploy the plugin under JIRA_HOME/plugins/installed-plugins
* The manager classes in the example are servlets. It will be accessible within JIRA via each url-pattern you specify, beneath the /plugins/servlet parent path.

For example, if your JIRA was deployed at http://yourserver/jira — then your issueManager servlet would be accessed at http://yourserver/jira/plugins/servlet/issueManager .

The manager classes are written just for demo and will create issues by its own before doing things like create links, manage attachments etc!