package com.tongbanjie.legends.server.dao.test;

import com.tongbanjie.legends.server.dao.JobSnapshotDAO;
import com.tongbanjie.legends.server.dao.dataobject.JobSnapshot;
import com.tongbanjie.legends.server.dao.dataobject.enums.JobSnapshotStatusEnum;
import com.tongbanjie.legends.server.dao.query.JobSnapshotQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author sunyi
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/legends-dao-test.xml"})
public class JobSnapshotDAOTest {

	@Autowired
	public JobSnapshotDAO dao;

	@Test
	public void mainTest() {
		Assert.notNull(dao);

		JobSnapshotQuery param = new JobSnapshotQuery();
		param.setGroup("TEST");
		param.setStatus(JobSnapshotStatusEnum.COMPLETED);
		param.setRows(10);
		List<JobSnapshot> byParam = dao.findByParam(param);

		System.out.println(byParam.size());

	}
}