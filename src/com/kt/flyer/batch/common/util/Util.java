package com.kt.flyer.batch.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

import com.kt.flyer.batch.domain.JobInfo;

public class Util {
	
	protected Logger logger;
	
	public Util() {
		super();
		this.logger = Logger.getLogger(this.getClass());
	}
	
	public List<JobInfo> getJobs(Scheduler sc) {
		List<JobInfo> jobs = new ArrayList<JobInfo>();
		JobInfo jobInfo = null;
		try {
			for(String groupName : sc.getJobGroupNames()) {
				for(JobKey jobKey : sc.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					
					jobInfo = new JobInfo();
					
					String jobName = jobKey.getName();
					@SuppressWarnings("unused")
					String jobGroup = jobKey.getGroup();
					
					List<? extends Trigger> triggers = sc.getTriggersOfJob(jobKey);
					Trigger tr = triggers.get(0);
					Date nextFireTime = tr.getNextFireTime();
					
					jobInfo.setJobName(jobName);
					jobInfo.setNextFireTime(nextFireTime);
					
					jobs.add(jobInfo);
					
					jobInfo = null;
				}
			}
		} catch (SchedulerException e) {
			logger.info("SchedulerException : "+e);
		}
		
		return jobs;
	}
	
	
	public void writeJobList(Scheduler scheduler) {
		List<JobInfo> jobList = this.getJobs(scheduler);
		StringBuffer dataList= new StringBuffer();
		String line = System.getProperty("line.separator");
		
		File file = new File("jobList");
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(file);
			
			for(JobInfo jobInfo : jobList) {
				dataList.append(jobInfo.getJobName());
				dataList.append("   다음 실행시간 : ");
				dataList.append(jobInfo.getNextFireTime());
				dataList.append(line);
			}
			fw.write(dataList.toString());
			
		} catch (FileNotFoundException e) {
			logger.info("FileNotFoundException : "+e);
		} catch (IOException e) {
			logger.info("IOException : "+e);
		} finally {
			try {
				if(fw != null) {
					fw.flush();
					fw.close();
				}
			}catch (IOException e) {
				logger.info("IOException : "+e);
			}
		}
	}
}
