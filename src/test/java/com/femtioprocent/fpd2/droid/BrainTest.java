package com.femtioprocent.fpd2.droid;

import com.femtioprocent.fpd2.droid.DKey.Name;
import com.femtioprocent.fpd2.util.SundryUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

//AbstractIntegrationTestCase -> commit, no user transaction
//AbstractTransactionalTestCase -> rollback, with user transaction. Rollback for each @Test
public class BrainTest extends TestCase {

    static DKeyService pkvS;

    public static class JUnitDroid extends Droid {

	public JUnitDroid() {
	    super(new InstanceId());
	}

	public JUnitDroid(InstanceId instanceId) {
	    super(instanceId);
	}
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	pkvS = DKeyService.locate(DKeyService.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void before() {
	Droid juDroid = DroidFactory.create(JUnitDroid.class);
	juDroid.relaod(); // roolback after each @Test-method call. Make sure to read from database.
    }

    @After
    public void after() {
    }

    Name name = new DKey.Name("junit", "BrainTest", "instance");

    @Test
    public void test() {
	assertEquals(3, 3);
    }

//    @Test
//    @Ignore
//    public void testSaveAll() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//
//        DKey v = new DKey(name, "value " + System.currentTimeMillis());
//        pkvS.append(v);
//        v = new DKey(name, "value " + System.currentTimeMillis());
//        pkvS.append(v);
//        v = new DKey(name, "value " + System.currentTimeMillis());
//        pkvS.append(v);
//        v = new DKey(name, "value " + System.currentTimeMillis());
//        pkvS.append(v);
//        int x = v.getId();
//        v = new DKey(name, "value " + System.currentTimeMillis());
//        pkvS.append(v);
//        v = new DKey(name, "value " + System.currentTimeMillis());
//        pkvS.append(v);
//
//        List<DKey> byName = pkvS.getByName(name);
//        assertEquals(6, byName.size());
//
//        pkvS.deleteBeforeByName(name, x);
//
//        byName = pkvS.getByName(name);
//        assertEquals(3, byName.size());
//        assertEquals(x, byName.get(0).getId());
//
//        pkvS.deleteBeforeByName(name, Integer.MAX_VALUE);
//
//        byName = pkvS.getByName(name);
//        assertEquals(0, byName.size());
//
//        System.out.println("x was " + x);
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0n() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId("noll"));
//        assertEquals("noll", juDroid.instanceId.id);
//        assertEquals(String.class, juDroid.instanceId.clazz);
//        assertEquals(":noll", juDroid.instanceId.mkKey());
//        assertEquals(":noll", juDroid.instanceId.key);
//        assertEquals("JUnitDroid::noll:sub", juDroid.mkKey("sub"));
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0ne() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(Object.class, ""));
//        assertEquals("", juDroid.instanceId.id);
//        assertEquals(Object.class, juDroid.instanceId.clazz);
//        assertEquals(":", juDroid.instanceId.mkKey());
//        assertEquals(":", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:::sub", juDroid.mkKey("sub"));
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0e() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(""));
//        assertEquals("", juDroid.instanceId.id);
//        assertEquals(String.class, juDroid.instanceId.clazz);
//        assertEquals(":", juDroid.instanceId.mkKey());
//        assertEquals(":", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:::sub", juDroid.mkKey("sub"));
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0ee() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(null));
//        assertEquals("", juDroid.instanceId.id);
//        assertEquals(Object.class, juDroid.instanceId.clazz);
//        assertEquals(":", juDroid.instanceId.mkKey());
//        assertEquals(":", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:::sub", juDroid.mkKey("sub"));
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0eee() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class);
//        assertEquals("", juDroid.instanceId.id);
//        assertEquals(Object.class, juDroid.instanceId.clazz);
//        assertEquals(":", juDroid.instanceId.mkKey());
//        assertEquals(":", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:::sub", juDroid.mkKey("sub"));
//    }
//
//    @Test(expected=IllegalArgumentException.class)
//    @Ignore
//    public void testDroid0eee42() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(Droid.class);
//    }
//
//    @Test(expected=IllegalArgumentException.class)
//    @Ignore
//    public void testDroid0eee42Null() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(null);
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0eee0() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create();
//        assertEquals("", juDroid.instanceId.id);
//        assertEquals(Object.class, juDroid.instanceId.clazz);
//        assertEquals(":", juDroid.instanceId.mkKey());
//        assertEquals(":", juDroid.instanceId.key);
//        assertEquals(":::sub", juDroid.mkKey("sub"));
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0eN() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId("zz"));
//        Droid juDroid2 = DroidFactory.create(juDroid, new Droid.InstanceId("qq"));
//        assertEquals("qq", juDroid2.instanceId.id);
//        assertEquals(String.class, juDroid2.instanceId.clazz);
//        assertEquals(":zz.qq", juDroid2.instanceId.mkKey());
//        assertEquals(":zz.qq", juDroid2.instanceId.key);
//        assertEquals("JUnitDroid::zz.qq:sub", juDroid2.mkKey("sub"));
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0eN3() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId("zz"));
//        Droid juDroid2 = DroidFactory.create(juDroid, new Droid.InstanceId("qq"));
//        Droid juDroid3 = DroidFactory.create(juDroid2, new Droid.InstanceId("xx"));
//        
//        assertEquals("zz", juDroid.instanceId.id);
//        assertEquals(String.class, juDroid2.instanceId.clazz);
//        assertEquals(":zz", juDroid.instanceId.mkKey());
//        assertEquals(":zz", juDroid.instanceId.key);
//        assertEquals("JUnitDroid::zz:sub", juDroid.mkKey("sub"));
//        
//        assertEquals("qq", juDroid2.instanceId.id);
//        assertEquals(String.class, juDroid2.instanceId.clazz);
//        assertEquals(":zz.qq", juDroid2.instanceId.mkKey());
//        assertEquals(":zz.qq", juDroid2.instanceId.key);
//        assertEquals("JUnitDroid::zz.qq:sub", juDroid2.mkKey("sub"));
//        
//        assertEquals("xx", juDroid3.instanceId.id);
//        assertEquals(String.class, juDroid3.instanceId.clazz);
//        assertEquals(":zz.qq.xx", juDroid3.instanceId.mkKey());
//        assertEquals(":zz.qq.xx", juDroid3.instanceId.key);
//        assertEquals("JUnitDroid::zz.qq.xx:sub", juDroid3.mkKey("sub"));
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0eN3t() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId("zz"));
//        Droid juDroid2 = DroidFactory.create(juDroid, new Droid.InstanceId(this.getClass(), "qq"));
//        Droid juDroid3 = DroidFactory.create(juDroid2, new Droid.InstanceId("xx"));
//        
//        assertEquals("zz", juDroid.instanceId.id);
//        assertEquals(String.class, juDroid.instanceId.clazz);
//        assertEquals(":zz", juDroid.instanceId.mkKey());
//        assertEquals(":zz", juDroid.instanceId.key);
//        assertEquals("JUnitDroid::zz:sub", juDroid.mkKey("sub"));
//        
//        assertEquals("qq", juDroid2.instanceId.id);
//        assertEquals(BrainTest.class, juDroid2.instanceId.clazz);
//        assertEquals("String.BrainTest:zz.qq", juDroid2.instanceId.mkKey());
//        assertEquals("String.BrainTest:zz.qq", juDroid2.instanceId.key);
//        assertEquals("JUnitDroid:String.BrainTest:zz.qq:sub", juDroid2.mkKey("sub"));
//        
//        assertEquals("xx", juDroid3.instanceId.id);
//        assertEquals(String.class, juDroid3.instanceId.clazz);
//        assertEquals("String.BrainTest.String:zz.qq.xx", juDroid3.instanceId.mkKey());
//        assertEquals("String.BrainTest.String:zz.qq.xx", juDroid3.instanceId.key);
//        assertEquals("JUnitDroid:String.BrainTest.String:zz.qq.xx:sub", juDroid3.mkKey("sub"));
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0C() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(Integer.class, 123));
//        List<Integer> asList = Arrays.asList(1, 2, 3);
//        List<Integer> aList = new ArrayList<Integer>();
//        aList.addAll(asList); // asList can't  be serialized into XML ! :-(
//        juDroid.remember("iL", aList);
//        
//        List<Integer> iL = (List<Integer>) juDroid.retrieve("iL");
//        assertEquals("123", juDroid.instanceId.id);
//        assertEquals(Integer.class, juDroid.instanceId.clazz);
//        assertEquals("Integer:123", juDroid.instanceId.mkKey());
//        assertEquals("Integer:123", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:Integer:123:sub", juDroid.mkKey("sub"));
//        assertEquals(iL.get(0).intValue(), 1);
//        assertEquals(iL.get(1).intValue(), 2);
//        assertEquals(iL.get(2).intValue(), 3);
//
//        juDroid.relaod();
//        dbDump(SundryUtil.whichMethod());
//
//        iL = (List<Integer>) juDroid.retrieve("iL");
//        assertEquals("123", juDroid.instanceId.id);
//        assertEquals(Integer.class, juDroid.instanceId.clazz);
//        assertEquals("Integer:123", juDroid.instanceId.mkKey());
//        assertEquals("Integer:123", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:Integer:123:sub", juDroid.mkKey("sub"));
//        assertEquals(iL.get(0).intValue(), 1);
//        assertEquals(iL.get(1).intValue(), 2);
//        assertEquals(iL.get(2).intValue(), 3);
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0CC() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(Integer.class, 123));
//        List<Integer> asList = Arrays.asList(1, 2, 3);
//        List<Integer> aList = new ArrayList<Integer>();
//        aList.addAll(asList); // asList can't  be serialized into XML ! :-(
//        juDroid.remember("iL", aList);
//        
//        List<Integer> iL = (List<Integer>) juDroid.retrieve("iL");
//        assertEquals("123", juDroid.instanceId.id);
//        assertEquals(Integer.class, juDroid.instanceId.clazz);
//        assertEquals("Integer:123", juDroid.instanceId.mkKey());
//        assertEquals("Integer:123", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:Integer:123:sub", juDroid.mkKey("sub"));
//        assertEquals(iL.get(0).intValue(), 1);
//        assertEquals(iL.get(1).intValue(), 2);
//        assertEquals(iL.get(2).intValue(), 3);
//        dbDump(SundryUtil.whichMethod());
//        
//        juDroid.relaod();
//        dbDump(SundryUtil.whichMethod());
//
//        iL = (List<Integer>) juDroid.retrieve("iL");
//        assertEquals("123", juDroid.instanceId.id);
//        assertEquals(Integer.class, juDroid.instanceId.clazz);
//        assertEquals("Integer:123", juDroid.instanceId.mkKey());
//        assertEquals("Integer:123", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:Integer:123:sub", juDroid.mkKey("sub"));
//        assertEquals(iL.get(0).intValue(), 1);
//        assertEquals(iL.get(1).intValue(), 2);
//        assertEquals(iL.get(2).intValue(), 3);
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0CCC() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(123));
//        List<Integer> asList = Arrays.asList(1, 2, 3);
//        List<Integer> aList = new ArrayList<Integer>();
//        aList.addAll(asList); // asList can't  be serialized into XML ! :-(
//        juDroid.remember("iL", aList);
//        
//        List<Integer> iL = (List<Integer>) juDroid.retrieve("iL");
//        assertEquals("123", juDroid.instanceId.id);
//        assertEquals(Integer.class, juDroid.instanceId.clazz);
//        assertEquals("Integer:123", juDroid.instanceId.mkKey());
//        assertEquals("Integer:123", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:Integer:123:sub", juDroid.mkKey("sub"));
//        assertEquals(iL.get(0).intValue(), 1);
//        assertEquals(iL.get(1).intValue(), 2);
//        assertEquals(iL.get(2).intValue(), 3);
//        dbDump(SundryUtil.whichMethod());
//        
//        juDroid.relaod();
//        dbDump(SundryUtil.whichMethod());
//        
//        iL = (List<Integer>) juDroid.retrieve("iL");
//        assertEquals("123", juDroid.instanceId.id);
//        assertEquals(Integer.class, juDroid.instanceId.clazz);
//        assertEquals("Integer:123", juDroid.instanceId.mkKey());
//        assertEquals("Integer:123", juDroid.instanceId.key);
//        assertEquals("JUnitDroid:Integer:123:sub", juDroid.mkKey("sub"));
//        assertEquals(iL.get(0).intValue(), 1);
//        assertEquals(iL.get(1).intValue(), 2);
//        assertEquals(iL.get(2).intValue(), 3);
//    }
//
//    @Test
//    @Ignore
//    public void testDroid1() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//
//        String base = "_jU_A";
//        droidPopulate1(base);
//        droidTest1(base);
//        dbDump(SundryUtil.whichMethod());
//    }
//
//    @Test
//    @Ignore
//    public void testDroid2() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//
//        String base = "_jU_B";
//        droidPopulate2(base);
//        droidTest2(base);
//        dbDump(SundryUtil.whichMethod());
//    }
//
//    @Test
//    @Ignore
//    public void testDroid0Rem() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(""));
//        juDroid.remember("ka1", "A1");
//        juDroid.remember("ka2", "A2");
//        juDroid.remember("ka3", "A3");
//        String ka2 = (String) juDroid.retrieve("ka2");
//        assertEquals("A2", ka2);
//        dbDump(SundryUtil.whichMethod());
//        juDroid.forget("ka2");
//
//        ka2 = (String) juDroid.retrieve("ka2");
//        assertEquals(null, ka2);
//        dbDump(SundryUtil.whichMethod());
//
//        juDroid.relaod();
//        String ka1 = (String) juDroid.retrieve("ka1");
//        assertEquals("A1", ka1);
//        ka2 = (String) juDroid.retrieve("ka2");
//        assertEquals(null, ka2);
//        dbDump(SundryUtil.whichMethod());
//    }
//
//    @Test
//    @Ignore
//    public void testDroidDroid() throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId("nr1"));
//        juDroid.remember("ka1", "A1");
//        juDroid.remember("ka2", "A2");
//        juDroid.remember("ka3", "A3");
//        dbDump(SundryUtil.whichMethod());
//        
//        Droid juDroid2 = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId("nr2"));
//        juDroid2.remember("droid", juDroid);
//
//        dbDump(SundryUtil.whichMethod());
//        
//        JUnitDroid jd = (JUnitDroid) juDroid2.retrieve("droid");
//        assertEquals(juDroid, jd);
//    }
//
//    void droidTest1(String base) {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(base));
//        List<String> list = (List<String>) juDroid.retrieve("l");
//        for (String key : list) {
//            int max = key.charAt(1) - '0';
//
//            Droid juDroid2 = DroidFactory.create(juDroid, key);
//            HashMap<String, String> m = (HashMap<String, String>) juDroid2.retrieve("hm");
//            for (int i = 0; i < max; i++) {
//                int i1 = i + 1;
//                String s = m.get("ab" + i1);
//                assertEquals("key=" + key, key + "AB" + i1, s);
//            }
//            String s = m.get("ab" + (max + 1));
//            assertEquals("key=" + key, null, s);
//            System.out.println("" + key + ": " + m);
//        }
//    }
//
//    public void droidPopulate1(String base) throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(base));
//
//        List<String> list = new ArrayList<String>();
//        list.add("41");
//        list.add("42");
//        list.add("43");
//        juDroid.remember("l", list);
//
//        for (String key : list) {
//            Droid juDroid2 = DroidFactory.create(juDroid, key);
//            HashMap m = new HashMap();
//            if ("41".equals(key)) {
//                m.put("ab1", "41AB1");
//            }
//            if ("42".equals(key)) {
//                m.put("ab1", "42AB1");
//                m.put("ab2", "42AB2");
//            }
//            if ("43".equals(key)) {
//                m.put("ab1", "43AB1");
//                m.put("ab2", "43AB2");
//                m.put("ab3", "43AB3");
//            }
//            juDroid2.remember("hm", m);
//        }
//    }
//
//    void droidTest2(String base) {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(this.getClass(), base));
//        List<String> list = (List<String>) juDroid.retrieve("l");
//        for (String key : list) {
//            int max = key.charAt(1) - '0';
//
//            Droid juDroid2 = DroidFactory.create(juDroid, new Droid.InstanceId(Integer.class, key));
//            HashMap<String, String> m = (HashMap<String, String>) juDroid2.retrieve("hm");
//            for (int i = 0; i < max; i++) {
//                int i1 = i + 1;
//                String s = m.get("ab" + i1);
//                assertEquals("key=" + key, key + "AB" + i1, s);
//            }
//            String s = m.get("ab" + (max + 1));
//            assertEquals("key=" + key, null, s);
//            System.out.println("" + key + ": " + m);
//        }
//    }
//
//    public void droidPopulate2(String base) throws Exception {
//        System.out.println("" + SundryUtil.whichMethod());
//        Droid juDroid = DroidFactory.create(JUnitDroid.class, new Droid.InstanceId(this.getClass(), base));
//
//        List<String> list = new ArrayList<String>();
//        list.add("41");
//        list.add("42");
//        list.add("43");
//        juDroid.remember("l", list);
//
//        for (String key : list) {
//            int keyInt = Integer.parseInt(key);
//            Droid juDroid2 = DroidFactory.create(juDroid, new Droid.InstanceId(Integer.class, keyInt));
//            HashMap m = new HashMap();
//            if ("41".equals(key)) {
//                m.put("ab1", "41AB1");
//            }
//            if ("42".equals(key)) {
//                m.put("ab1", "42AB1");
//                m.put("ab2", "42AB2");
//            }
//            if ("43".equals(key)) {
//                m.put("ab1", "43AB1");
//                m.put("ab2", "43AB2");
//                m.put("ab3", "43AB3");
//            }
//            juDroid2.remember("hm", m);
//        }
//    }
//
//    private void dbDump(String prefix) {
//        List<DKey> byName = pkvS.getByName(new DKey.Name("class",
//            "com.femtioprocent.fpd2.droid.Brain$Elephant$ElephantMemory", "cortex"));
//        for (DKey kv : byName) {
//            System.out.println(prefix + " kv: " + kv);
//        }
//    }
}
