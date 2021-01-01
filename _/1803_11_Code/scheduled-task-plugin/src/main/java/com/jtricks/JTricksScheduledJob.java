package com.jtricks;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JTricksScheduledJob implements Job{

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Running the job at "+(new Date()).toString());
	}

}
