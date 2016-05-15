package com.xfz.mobilesafe.junit.test;

import java.util.List;
import java.util.Random;

import com.xfz.mobilesafe.bean.BlackNumberInfo;
import com.xfz.mobilesafe.db.dao.BlackNumberDao;

import android.content.Context;
import android.test.AndroidTestCase;

/** 
 * 
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-15 
 */
public class TestCase extends AndroidTestCase {
	
	public Context mContext;
	
	@Override
	protected void setUp() throws Exception {
		this.mContext = getContext();
		super.setUp();
	}
	
	public void testAdd() {
		BlackNumberDao dao = new BlackNumberDao(mContext);
		Random random = new Random();
		for (int i = 0; i < 200; i++) {
			long number = 2672510000l + i;
			dao.add(number+"", String.valueOf(random.nextInt(3)+1));
		}
	}
	public void testDelete() {
		BlackNumberDao dao = new BlackNumberDao(mContext);
		boolean delete = dao.delete("2672510000");
		assertEquals(true, delete);
	}
	public void testFind() {
		BlackNumberDao dao = new BlackNumberDao(mContext);
		String number = dao.findNumber("2672510001");
		System.out.println(number);
	}
	public void testFindAll() {
		BlackNumberDao dao = new BlackNumberDao(mContext);
		List<BlackNumberInfo> findAll = dao.findAll();
		for (BlackNumberInfo blackNumberInfo : findAll) {
			System.out.println(blackNumberInfo.getNumber()+":"+blackNumberInfo.getMode());
		}
		
	}
}
