
package org.eclipse.birt.report.tests.engine.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.ICascadingParameterGroup;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.tests.engine.EngineCase;

import com.ibm.icu.util.ULocale;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>IGetParameterDefinitionTask API</b>
 * <p>
 * This test tests all methods in IGetParameterDefinitionTask API
 */
public class IGetParameterDefinitionTaskTest extends EngineCase {

	private String name = "IGetParameterDefinitionTaskTest.rptdesign";
	private String input = this.genInputFile(name);
	private IGetParameterDefinitionTask task = null;

	public IGetParameterDefinitionTaskTest(String name) {
		super(name);
	}

	/**
	 * Test suite()
	 *
	 * @return
	 */
	public static Test suite() {
		return new TestSuite(IGetParameterDefinitionTaskTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(name, name);
		IReportRunnable reportRunnable = engine.openReportDesign(input);
		task = engine.createGetParameterDefinitionTask(reportRunnable);
		task.setLocale(ULocale.ENGLISH);
		// task.setTimeZone(TimeZone.getTimeZone("GMT"));
		assertTrue(task.getErrors().size() == 0);
	}

	protected void tearDown() throws Exception {
		task.close();
		super.tearDown();
		removeResource();
	}

	/**
	 * Test getParameterDefns() method
	 *
	 * @throws Exception
	 */
	public void testGetParameterDefns() throws Exception {
		boolean includeParameterGroups = true;
		ArrayList params = (ArrayList) task.getParameterDefns(includeParameterGroups);
		assertEquals(8, params.size());
		assertTrue(params.get(0) instanceof IScalarParameterDefn);
		assertTrue(params.get(1) instanceof IScalarParameterDefn);
		assertTrue(params.get(2) instanceof IScalarParameterDefn);
		assertTrue(params.get(3) instanceof IParameterGroupDefn);
		assertTrue(params.get(4) instanceof ICascadingParameterGroup);
		assertTrue(params.get(5) instanceof ICascadingParameterGroup);

		includeParameterGroups = false;
		params = (ArrayList) task.getParameterDefns(includeParameterGroups);
		assertEquals(14, params.size());

		for (int i = 0; i < params.size(); i++) {
			assertTrue(params.get(i) instanceof IScalarParameterDefn);
		}

		assertEquals("p1_string", ((IScalarParameterDefn) params.get(0)).getName());
		assertEquals(0, ((IScalarParameterDefn) params.get(0)).getControlType());
		assertEquals("abc", ((IScalarParameterDefn) params.get(0)).getDefaultValue());
		assertEquals(2, ((IScalarParameterDefn) params.get(1)).getSelectionListType());
		assertEquals(1, ((IScalarParameterDefn) params.get(1)).getControlType());
		assertEquals(1, ((IScalarParameterDefn) params.get(2)).getSelectionListType());
		assertEquals("p42_float", ((IScalarParameterDefn) params.get(4)).getName());
		assertEquals(2, ((IScalarParameterDefn) params.get(4)).getDataType());
		assertEquals("p51", ((IScalarParameterDefn) params.get(5)).getName());
		assertEquals("p61_country", ((IScalarParameterDefn) params.get(7)).getName());
		// 282501
		assertEquals(2, ((IScalarParameterDefn) params.get(10)).getSelectionListType());
	}

	/**
	 * Test getParameterDefn(String) method
	 *
	 * @throws Exception
	 */
	public void testGetParameterDefn() throws Exception {
		IParameterDefnBase paramDefn = task.getParameterDefn("p1_string");
		assertNotNull(paramDefn);
		assertEquals(0, paramDefn.getParameterType());
		assertEquals("scalar", paramDefn.getTypeName());
		assertEquals(6, paramDefn.getHandle().getID());

		paramDefn.getHandle().setDisplayName("STRINGParameter");
		assertEquals("STRINGParameter", paramDefn.getHandle().getDisplayName());
		// getDisplayName() from paramDefn
		// assertEquals("STRINGParameter",paramDefn.getDisplayName());

		paramDefn = task.getParameterDefn("p2_static_dt");
		assertNotNull(paramDefn);
		assertEquals(0, paramDefn.getParameterType());
		assertEquals("p2_static_dt", paramDefn.getName());

		paramDefn = task.getParameterDefn("p3_dynamic_int");
		assertNotNull(paramDefn);
		// what's the difference between the following parameters?
		// SCALAR_PARAMETER = 0;
		// FILTER_PARAMETER = 1;
		// LIST_PARAMETER = 2;
		// TABLE_PARAMETER = 3;
		assertEquals(0, paramDefn.getParameterType());

		paramDefn = task.getParameterDefn("p4_group");
		assertNotNull(paramDefn);
		assertEquals(4, paramDefn.getParameterType());
		assertEquals(9, paramDefn.getHandle().getID());

		paramDefn = task.getParameterDefn("p41_decimal");
		assertNotNull(paramDefn);

		paramDefn = task.getParameterDefn("p5_CascadingGroup_single");
		assertNotNull(paramDefn);
		assertEquals(5, paramDefn.getParameterType());

		paramDefn = task.getParameterDefn("p51");
		assertNotNull(paramDefn);

		paramDefn = task.getParameterDefn("p6_CascadingGroup_multiple");
		assertNotNull(paramDefn);

		paramDefn = task.getParameterDefn("p62_customernumber");
		assertNotNull(paramDefn);

		paramDefn = task.getParameterDefn("invalid");
		assertNull(paramDefn);

		paramDefn = task.getParameterDefn(null);
		assertNull(paramDefn);

	}

	/**
	 * Test setValue method This method is not implemented at present.
	 *
	 * @throws Exception
	 */
	public void testSetValue() throws Exception {
		task.setValue("p1_string", "aaa");
		Date d = new Date();
		task.setValue("p2_static_dt", d);

	}

	/**
	 * Test getDefaultValues() method
	 *
	 * @throws Exception
	 */
	public void testGetDefaultValues() throws Exception {
		HashMap values = task.getDefaultValues();

		assertNotNull(values);
		assertEquals(14, values.size());
		assertEquals("abc", values.get("p1_string"));

		assertEquals("10251", values.get("p3_dynamic_int").toString());
		assertEquals("2.35", values.get("p41_decimal").toString());
		assertEquals("87.16", values.get("p42_float").toString());
		setLocale(Locale.CHINA);

		assertEquals(null, values.get("p51"));
		assertEquals(null, values.get("p52"));

		assertNull(values.get("p61_country"));
		assertNull(values.get("p61_customernumber"));
		assertNull(values.get("p61_orderno"));
	}

	/**
	 * Test getDefaultValue(IParameterDefnBase param) method Test
	 * getDefaultValue(String name) method
	 *
	 * @throws Exception
	 */
	public void testGetDefaultValue() throws Exception {
		assertEquals("abc", task.getDefaultValue("p1_string"));
		assertEquals("10251", task.getDefaultValue("p3_dynamic_int").toString());
		assertEquals("2.35", task.getDefaultValue("p41_decimal").toString());
		assertEquals("87.16", task.getDefaultValue("p42_float").toString());

	}

	public void testGetSelectionList() throws Exception {
		ArrayList selist = (ArrayList) task.getSelectionList("p2_static_dt");

		IParameterSelectionChoice se = (IParameterSelectionChoice) selist.get(0);
		assertTrue(se.getValue().toString(), se.getValue().toString().startsWith("Tue May 11 00:00:00"));
		se = (IParameterSelectionChoice) selist.get(1);
		assertTrue(se.getValue().toString(), se.getValue().toString().startsWith("Tue May 18 00:00:00"));

		/*
		 * 05/11/2004 12:00:00 AM 05/18/2004 12:00:00 AM
		 */

		selist = (ArrayList) task.getSelectionList("p3_dynamic_int");
		se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("10250", se.getValue().toString());
		int listnumb = selist.size();
		assertEquals(21, listnumb);

		// Cascading parameters with single dataset
		selist = (ArrayList) task.getSelectionList("p51");
		se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("Shipped", se.getValue().toString());
		task.setValue("p51", "Cancelled");

		selist = (ArrayList) task.getSelectionList("p52");
		se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("10253", se.getValue().toString());

		task.setValue("p51", "Shipped");
		selist = (ArrayList) task.getSelectionList("p52");
		se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("10250", se.getValue().toString());

		// Cascading parameters with multiple datasets
		selist = (ArrayList) task.getSelectionList("p61_country");
		se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("USA", se.getValue().toString());
		task.setValue("p61_country", "UK");

		selist = (ArrayList) task.getSelectionList("p62_customernumber");
		se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("187", se.getValue().toString());

		Integer icustno = new Integer("240");
		task.setValue("p62_customernumber", icustno);

		selist = (ArrayList) task.getSelectionList("p63_orderno");
		se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("10232", se.getValue().toString());
		// set the value that not in the list
		Integer iorderno = new Integer("10333");
		task.setValue("p63_orderno", iorderno);

	}

	public void testGetSelectionListForCascadingGroup() throws Exception {
		task.evaluateQuery("p5_CascadingGroup_single");

		ArrayList selist = (ArrayList) task.getSelectionListForCascadingGroup("p5_CascadingGroup_single",
				new Object[] { "Cancelled" });
		IParameterSelectionChoice se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("10253", se.getValue().toString());

		selist = (ArrayList) task.getSelectionListForCascadingGroup("p6_CascadingGroup_multiple",
				new Object[] { "UK" });
		se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("187", se.getValue().toString());
		Integer icustnum = new Integer("187");
		selist = (ArrayList) task.getSelectionListForCascadingGroup("p6_CascadingGroup_multiple",
				new Object[] { "UK", icustnum });
		se = (IParameterSelectionChoice) selist.get(0);
		assertEquals("10110", se.getValue().toString());

	}

	public void testGetSelectionListForCascadingMultiple() throws EngineException {

		/**
		 * #16721 Australia Victoria Melbourne Denmark NULL Kobenhavn France NULL Nantes
		 * Lyon Germany NULL Frankfurt Norway NULL Stavern Poland NULL Warszawa
		 * Singapore NULL Singapore Spain NULL Madrid Sweden NULL Lulea USA CA San
		 * Francisco San Rafael NV Las Vegas NY NYC
		 *
		 */
		String cpg1 = "NewCascadingParameterGroup";

		String[][] values1 = new String[][] { { "USA" } };
		Collection col;
		col = task.getSelectionListForCascadingGroup(cpg1, values1);
		assertEquals(3, col.size());
		values1 = new String[][] { { "France", "USA" } };
		col = task.getSelectionListForCascadingGroup(cpg1, values1);
		assertEquals(4, col.size());
		values1 = new String[][] { { "France", "Germany", "Singapore", "USA" } };
		col = task.getSelectionListForCascadingGroup(cpg1, values1);
		assertEquals(6, col.size());

		String[][] values2 = { { "USA" }, { "CA", "NV" } };
		col = task.getSelectionListForCascadingGroup(cpg1, values2);
		assertEquals(3, col.size());
		values2 = new String[][] { { "France", "USA" }, { "CA", "NV", null } };
		col = task.getSelectionListForCascadingGroup(cpg1, values2);
		assertEquals(5, col.size());
		values2 = new String[][] { { "France", "Germany", "USA" }, { "CA", "NV", null } };
		col = task.getSelectionListForCascadingGroup(cpg1, values2);
		assertEquals(6, col.size());

		String[][] values3 = { { "France", "USA" }, { "CA", "NV" } };
		col = task.getSelectionListForCascadingGroup(cpg1, values3);
		assertEquals(3, col.size());
		values3 = new String[][] { { "France", "USA" }, { null } };
		col = task.getSelectionListForCascadingGroup(cpg1, values3);
		assertEquals(2, col.size());
		values3 = new String[][] { { "France", "Germany", "USA" }, { null } };
		col = task.getSelectionListForCascadingGroup(cpg1, values3);
		assertEquals(3, col.size());
		values3 = new String[][] { { "USA" }, { null } };
		col = task.getSelectionListForCascadingGroup(cpg1, values3);
		assertEquals(0, col.size());
	}

	public void testEvaluateQuery() throws Exception {
		// TODO;

	}

}
