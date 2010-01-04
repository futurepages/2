package org.futurepages.core.quartz;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.futurepages.core.tags.build.ModulesAutomation;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

public class QuartzJobsRegister extends ModulesAutomation{

	public QuartzJobsRegister(File[] modules) {
		super(modules, "jobs");
	}

	public void register() throws ClassNotFoundException, SchedulerException, ParseException{
		Map<String, List<Class<Job>>> classes = getModulesDirectoryClasses(Job.class, null);
		for (String moduleName : classes.keySet()) {
			for (Class<Job> jobKlass : classes.get(moduleName)) {
				scheduleJob(moduleName, jobKlass);
			}
		}
	}

	private void scheduleJob(String moduleName, Class<?> jobKlass)
			throws SchedulerException, ParseException {
		JobDetail jobDetail = new JobDetail(moduleName+"-"+jobKlass.getSimpleName()+"_Job", 
				Scheduler.DEFAULT_GROUP, jobKlass);
		
		Trigger trigger = buildTrigger(moduleName, jobKlass);
		QuartzManager.getSchedulerFactory().getScheduler().scheduleJob(jobDetail, trigger);
	}

	private Trigger buildTrigger(String moduleName, Class<?> jobKlass) throws ParseException {
		Trigger trigger = null;
		final Class<org.futurepages.core.quartz.CronTrigger> cronTagClass = org.futurepages.core.quartz.CronTrigger.class;
		if(jobKlass.isAnnotationPresent(cronTagClass)){
			org.futurepages.core.quartz.CronTrigger cronAnot = jobKlass.getAnnotation(cronTagClass);
			String name = moduleName+"_"+jobKlass.getSimpleName();
			trigger = new org.quartz.CronTrigger(name, Scheduler.DEFAULT_GROUP, cronAnot.expression());
		}			
		return trigger;
	}
}
