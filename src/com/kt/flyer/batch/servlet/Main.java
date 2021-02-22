package com.kt.flyer.batch.servlet;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.kt.flyer.batch.common.util.Util;
import com.kt.flyer.batch.domain.JobInfo;



@WebListener
public class Main extends HttpServlet implements ServletContextListener{

	private static final long serialVersionUID = 1L;
	
	private JobDetail job;
	private Trigger trigger;
	private Logger logger;
	
	public Main() {
		logger = Logger.getLogger(this.getClass());
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent e) {
		//Logger logger = Logger.getLogger(Main.class);
		logger.info("##########       batch 준비");
		
		SchedulerFactory schedulerFactory = new StdSchedulerFactory(); 
		Scheduler scheduler = null; 
		Util uu = new Util(); 
		
		try {
			
			//cron 시간 (초 분 시간 일 월 요일 연도) 초 단위 주의 (* 쓰지 말것)
			scheduler = schedulerFactory.getScheduler();
			if(scheduler.isStarted()) {
				return;
			}
			
			// 조직정보 업데이트 새벽 2시 30초 
			job = newJob(OrgUpdateJob.class)
					.withIdentity("OrgUpdateJob",Scheduler.DEFAULT_GROUP)
					.build();
				
			trigger = newTrigger() 
					.withIdentity("OrgUpdateTrigger",Scheduler.DEFAULT_GROUP) 
					.withSchedule(cronSchedule("30 0 2 * * ?"))
					.build();
			scheduler.scheduleJob(job,trigger); 
			
			// 접점정보 업데이트 새벽 3시 30초 
			job = newJob(CntpntUpdateJob.class)
					.withIdentity("CntpntUpdateJob",Scheduler.DEFAULT_GROUP)
					.build();
			
			trigger = newTrigger() 
					.withIdentity("CntpntUpdateTrigger",Scheduler.DEFAULT_GROUP) 
					.withSchedule(cronSchedule("30 0 3 * * ?"))
					.build(); 
			scheduler.scheduleJob(job,trigger);
			
			// 조직정보 FULL PATH 업데이트 새벽 4시 30초 
			job = newJob(OrgPathUpdateJob.class)
					.withIdentity("OrgPathUpdateJob",Scheduler.DEFAULT_GROUP)
					.build();
			
			trigger = newTrigger() 
					.withIdentity("OrgPathUpdateTrigger", Scheduler.DEFAULT_GROUP)
					.withSchedule(cronSchedule("30 0 4 * * ?"))
					.build();
			scheduler.scheduleJob(job,trigger);
			
			uu.writeJobList(scheduler);
			scheduler.start();
			
			List<JobInfo> jobs = uu.getJobs(scheduler);
			logger.info("##########        job 목록");
			for(JobInfo info : jobs) {
				logger.info("##########        "+info.getJobName());
			}
		logger.info("##########        batch 시작");
		} catch (SchedulerException ex) { 
			logger.info("SchedulerError : " + ex); 
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent e) {
		logger.info("##########        batch 종료");
	}
}
