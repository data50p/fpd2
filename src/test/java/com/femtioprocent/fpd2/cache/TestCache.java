/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femtioprocent.fpd2.cache;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lars
 */
public class TestCache extends TestCase {

    static class MyData {

        String foo;
        String bar;

        MyData(String foo, String bar) {
            this.foo = foo;
            this.bar = bar;
        }
    }

    private static CachedDataManager<MyData> cacheProgramUnitDataList = CachedDataManager.create("myTagId");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void test() {
        String id = "myId";

        MyData cacheddata = cacheProgramUnitDataList.get(id, new CachedDataFiller<MyData>() {
            // this function is called when there is no data in cache or it is expired (5 minutes)
            public MyData fill() {
                MyData data = new MyData(id, id);
                return data;
            }
        }, TimeUnit.MINUTES.toMillis(5));

        assertEquals("myId", cacheddata.foo);

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
        
        MyData cacheddata2 = cacheProgramUnitDataList.get(id, new CachedDataFiller<MyData>() {
            // this function is called when there is no data in cache or it is expired (5 minutes)
            public MyData fill() {
                MyData data = new MyData(id, id);
                return data;
            }
        }, TimeUnit.MINUTES.toMillis(0));

        assertEquals("myId", cacheddata2.foo);

        assertTrue(cacheddata != cacheddata2);

        MyData cacheddata3 = cacheProgramUnitDataList.get(id, new CachedDataFiller<MyData>() {
            // this function is called when there is no data in cache or it is expired (5 minutes)
            public MyData fill() {
                MyData data = new MyData(id, id);
                return data;
            }
        }, TimeUnit.MINUTES.toMillis(1));

        assertEquals("myId", cacheddata3.foo);

        assertTrue(cacheddata != cacheddata3);
        assertTrue(cacheddata2 == cacheddata3);
    }
}
