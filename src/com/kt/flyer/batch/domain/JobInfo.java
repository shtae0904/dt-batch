package com.kt.flyer.batch.domain;

import java.util.Date;

public class JobInfo {

	private String jobName;
	private Date nextFireTime;
	
	public JobInfo() {
		super();
	}

	public JobInfo(String jobName, Date nextFireTime) {
		super();
		this.jobName = jobName;
		this.nextFireTime = nextFireTime;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	
	
	
}
