package com.kt.flyer.batch.servlet;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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

public class OrgPathUpdateJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Logger logger = Logger.getLogger(OrgPathUpdateJob.class);
		logger.info("OrgPathUpdate Execute time = " + new Date());
		Util uu = new Util();
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = null;
		
		JobDao dao = JobDao.getInstance();
		try {
			List list = dao.getOrgPathUpdateSeq();
			dao.orgPathUpdate(list);
			int result = dao.orgPathDelete();
			logger.info("#### TB_CPZ_ORG_PATH 새로 추가-insert 실행(건수): "+list.size());
			logger.info("#### TB_CPZ_ORG_PATH 중복 삭제-delete 실행(건수): "+result);
			scheduler = schedulerFactory.getScheduler();
		} catch (SQLException e) {
			logger.info("SQLException : "+e);
		} catch (SchedulerException e) {
			logger.info("SchedulerException : "+e);
		}
		uu.writeJobList(scheduler);
	}

}
