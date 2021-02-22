package com.kt.flyer.batch.servlet;

import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.kt.flyer.batch.common.util.Util;
import com.kt.flyer.batch.dao.JobDao;

public class CntpntUpdateJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Logger logger = Logger.getLogger(CntpntUpdateJob.class);
		logger.info("CNTPNT_UPDATE Execute time = " + new Date());
		Util uu = new Util();
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = null;
		
		JobDao dao = JobDao.getInstance();
		try {
			dao.cntpntUpdate();
			scheduler = schedulerFactory.getScheduler();
		} catch (SQLException e) {
			logger.info("SQLException : "+e);
		} catch (SchedulerException e) {
			logger.info("SchedulerException : "+e);
		}
		uu.writeJobList(scheduler);
	}

}
