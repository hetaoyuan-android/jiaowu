package com.youpeng.wefriend.database;

import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.internal.util.AndroidRunnerParams;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import static org.junit.Assert.*;

/**
 * Author Ideal
 * Date   2016/3/10
 * Desc
 */
@RunWith(AndroidJUnit4.class)
public class CourseDaoTest extends AndroidTestCase{

    private CourseDao courseDao;


    @Before
    public void before(){
        courseDao = new CourseDao();
    }
    @Test
    public void testDeleteAll() throws Exception {
        int rowNum = courseDao.deleteAll();
        System.out.println("行数 " + rowNum);
        assertEquals(42, rowNum);
    }

    @Test
    public void testDeteleTabale() throws Exception {
        courseDao.deteleTabale();
    }

}