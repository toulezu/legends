package com.tongbanjie.legends.server.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tongbanjie.legends.server.component.SchedulerWrapper;

@Controller
@RequestMapping("/monitor")
public class MonitorController {

	@Autowired
	private SchedulerWrapper schedulerWrapper;

	@RequestMapping("/index.htm")
	public String monitor(HttpServletRequest request, Model model) throws SchedulerException, IOException {

		Map<JobKey, List<String>> jobMap = new HashMap<JobKey, List<String>>();

		Scheduler scheduler = schedulerWrapper.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

			List<String> nextFireTime = new ArrayList<String>();

			for (Trigger trigger : triggers) {
				nextFireTime.add(DateFormatUtils.format(trigger.getNextFireTime(), "yyyy-MM-dd HH:mm:ss"));
			}

			jobMap.put(jobKey, nextFireTime);

		}
		model.addAttribute("jobMap", jobMap);


		return "monitor/index";
	}
}
