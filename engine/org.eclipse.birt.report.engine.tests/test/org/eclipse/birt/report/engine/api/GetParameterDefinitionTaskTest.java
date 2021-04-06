/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.impl.ScalarParameterDefn;

public class GetParameterDefinitionTaskTest extends EngineCase {

	protected IReportEngine engine = null;
	protected IReportRunnable runnable = null;
	protected IGetParameterDefinitionTask gpdTask = null;
	protected IScalarParameterDefn scalarParam = null;

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/GetParameterDefinitionTaskTest.rptdesign";
//	static final String REPORT_DESIGN = "GetParameterDefinitionTaskTest.rptdesign";

	public void setUp() throws Exception {
		String reportDesign = REPORT_DESIGN_RESOURCE;
		initialize(reportDesign);
	}

	private void initialize(String reportDesign) throws EngineException {
		useDesignFile(reportDesign);

		engine = createReportEngine();
		runnable = engine.openReportDesign(REPORT_DESIGN);
		gpdTask = engine.createGetParameterDefinitionTask(runnable);
	}

	public void tearDown() {
		// shut down the engine.
		destroy();
	}

	private void destroy() {
		if (engine != null) {
			engine.shutdown();
		}
		removeFile(REPORT_DESIGN);
	}

	/**
	 * API test on IScalarParameterDefn.getDefaultValue( ) method Get the default
	 * values of several types of parameters:
	 * <li>String</li>
	 * <li>Boolean</li>
	 * <li>Static List</li>
	 * <li>Dynamic List</li>
	 * <li>Sorted Combo</li>
	 * <li>Number</li>
	 * <li>DateTime</li>
	 */
	public void testGetDefaultValue() {
		String[] paramDefnArray = new String[] { "paramString", "paramBoolean", "paramList", "paramListDynamic",
				"paramComboSort", "paramDispFormatNum", "paramDispFormatDateTime" };
		String[] goldenString = new String[] { "defaultStringValue", "false", "2", "0", "item2", "123",
				"08/10/2006 10:32:58 AM" };
		assertTrue(paramDefnArray.length == goldenString.length);
		for (int size = paramDefnArray.length, index = 0; index < size; index++) {
			String resultString = null;
			scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn(paramDefnArray[index]);
			resultString = scalarParam.getDefaultValue();
			assertTrue(resultString != null);
			assertTrue(goldenString[index].equals(resultString));
		}
	}

	/**
	 * API test on IScalarParameterDefn.isValueConcealed( ) method
	 */
	public void testIsValueConcealed() {
		String[] paramDefnArray = new String[] { "paramConceal", "paramString" };
		boolean[] goldenResult = new boolean[] { true, false };
		assertTrue(paramDefnArray.length == goldenResult.length);
		for (int size = paramDefnArray.length, index = 0; index < size; index++) {
			scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn(paramDefnArray[index]);
			assertEquals(scalarParam.isValueConcealed(), goldenResult[index]);
		}
	}

	/**
	 * API test on IScalarParameterDefn.allowNull( ) method
	 */
	/*
	 * allowNull() method is deprecated in model API the return value of
	 * allowBlank() is depends on isRequired()
	 */
//	public void testAllowNull( )
//	{
//		String[] paramArray = new String[]
//		{
//			"paramAllowNullAndBlank",
//			"paramString",
//			"paramBoolean",
//			"paramDatetimeFormat",
//			"paramList",
//			"paramComboSort",
//			"paramListDynamic"
//		};
//		boolean[] results = new boolean[]
//		{//			true,
//			true,
//			true,
//			true,
//			true,
//			true,
//			true
//		};
//		assertTrue( paramArray.length == results.length );
//		for ( int size = paramArray.length, index = 0; index < size; index++ )
//		{
//			scalarParam = (IScalarParameterDefn) gpdTask
//					.getParameterDefn( paramArray[index] );
//			assertTrue( results[index] == scalarParam.allowNull( ) );
//		}
//	}

	/**
	 * API test IScalarParameterDefn.allowBlank( ) method
	 */
	/*
	 * allowBlank() method is deprecated in model API the return value of
	 * allowBlank() is depends on isRequired()
	 */
//	public void testAllowBlank( )
//	{
//		String[] paramArray = new String[]{"paramString",
//				"paramDispFormatString", "paramListDynamic"};
//		boolean[] results = new boolean[]{true, true, true};
//		assertTrue( paramArray.length == results.length );
//		for ( int size = paramArray.length, index = 0; index < size; index++ )
//		{
//			scalarParam = (IScalarParameterDefn) gpdTask
//					.getParameterDefn( paramArray[index] );
//			assertTrue( scalarParam.allowBlank( ) );
//		}
//	}

	/**
	 * API test on IScalarParameterDefn.getDisplayFormat( ) method
	 */
	public void testGetDisplayFormat() {
		String[] paramArray = new String[] { "paramDispFormatString", "paramDispFormatDateTime", "paramDispFormatNum" };
		String[] goldenResult = new String[] { "(@@)", "Short Date", "Scientific" };
		assertTrue(paramArray.length == goldenResult.length);

		for (int size = paramArray.length, index = 0; index < size; index++) {
			scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn(paramArray[index]);
			assertEquals(goldenResult[index], scalarParam.getDisplayFormat());
		}
	}

	/**
	 * API test on IScalarParameterDefn.getControlType( ) method
	 */
	public void testGetControlType() {
		String[] paramArray = new String[] { "paramString", "paramBoolean", "paramList", "paramComboSort",
				"paramRadio" };
		int[] results = new int[] { IScalarParameterDefn.TEXT_BOX, IScalarParameterDefn.CHECK_BOX,
				IScalarParameterDefn.LIST_BOX, IScalarParameterDefn.LIST_BOX, IScalarParameterDefn.RADIO_BUTTON };
		assertTrue(paramArray.length == results.length);
		for (int size = paramArray.length, index = 0; index < size; index++) {
			scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn(paramArray[index]);
			assertEquals(results[index], scalarParam.getControlType());
		}
	}

	/**
	 * API test on IScalarParameterDefn.getAlignment( ) method
	 */
	public void testGetAlignment() {
		scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn("paramAlign");
		assertEquals(IScalarParameterDefn.RIGHT, scalarParam.getAlignment());
	}

	/**
	 * API test on IScalarParameterDefn.getSelectionList( ) method
	 */
	public void testGetSelectionList() {
		final int LIST_SIZE = 3;
		String[] goldenResult = new String[] { "1", "2", "3" };
		scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn("paramList");
		assertEquals(LIST_SIZE, ((ScalarParameterDefn) scalarParam).getSelectionList().size());
		assertEquals(scalarParam.getDefaultValue(), "2");
		IParameterSelectionChoice choice = null;
		assertTrue(goldenResult.length == LIST_SIZE);
		for (int index = 0; index < LIST_SIZE; index++) {
			choice = (IParameterSelectionChoice) ((ScalarParameterDefn) scalarParam).getSelectionList().get(index);
			assertEquals(goldenResult[index], choice.getValue().toString());
		}
	}

	/**
	 * API test on IScalarParameterDefn.getSelectionListType( ) method
	 */
	public void testGetSelectionListType() {
		scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn("paramList");
		assertEquals(IScalarParameterDefn.SELECTION_LIST_STATIC, scalarParam.getSelectionListType());
		scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn("paramListDynamic");
		assertEquals(IScalarParameterDefn.SELECTION_LIST_DYNAMIC, scalarParam.getSelectionListType());
	}

	/**
	 * API test on IScalarParameterDefn.displayInFixedOrder( ) method
	 */
	public void testDisplayInFixedOrder() {
		scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn("paramList");
		assertTrue(scalarParam.displayInFixedOrder());

		scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn("paramComboSort");
		assertFalse(scalarParam.displayInFixedOrder());
	}

	/*
	 * API test on IScalarParameterDefn.getParameterType( ) method
	 */
	public void testGetDataType() {
		String[] paramArray = new String[] { "paramBoolean", "paramDatetimeFormat", "paramDispFormatNum", "paramFloat",
				"paramInteger", "paramString" };
		int[] results = new int[] { IScalarParameterDefn.TYPE_BOOLEAN, IScalarParameterDefn.TYPE_DATE_TIME,
				IScalarParameterDefn.TYPE_DECIMAL, IScalarParameterDefn.TYPE_FLOAT, IScalarParameterDefn.TYPE_INTEGER,
				IScalarParameterDefn.TYPE_STRING };

		assertTrue(paramArray.length == results.length);
		for (int size = paramArray.length, index = 0; index < size; index++) {
			scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn(paramArray[index]);
			assertEquals(results[index], scalarParam.getDataType());
		}
	}

	/*
	 * API test on IScalarParameterDefn.allowNewValues( ) method
	 */
	public void testAllowNewValues() {
		scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn("paramList");
		assertFalse(scalarParam.allowNewValues());

		scalarParam = (IScalarParameterDefn) gpdTask.getParameterDefn("paramComboSort");
		assertTrue(scalarParam.allowNewValues());
	}

	/*
	 * API test on IGetParameterDefinitionTask.getParameterDefns( boolean
	 * includeParameterGroups )
	 */
	public void testGetParameterDefns() {
		final int PARAMS_COUNT_DO_INCLUDE_GROUP = 17;
		final int PARAMS_COUNT_DONOT_INCLUDE_GROUP = 16;

		/* DONOT include parameter groups */
		Collection collection = gpdTask.getParameterDefns(false);
		assertTrue(PARAMS_COUNT_DO_INCLUDE_GROUP == collection.size());

		/* DO include parameter groups */
		collection = gpdTask.getParameterDefns(true);
		assertTrue(PARAMS_COUNT_DONOT_INCLUDE_GROUP == collection.size());
	}

	public void testGetParameterGroupDefn() {
		final String GROUP_NAME = "paramGroup";
		final String GROUP_DISPLAY_NAME = "paramGroupDispName";
		// paramGroup
		IParameterDefnBase base = gpdTask.getParameterDefn("paramGroup");
		assertTrue(GROUP_NAME.equals(base.getName()));
		assertTrue(IParameterDefnBase.PARAMETER_GROUP == base.getParameterType());
		assertTrue(GROUP_DISPLAY_NAME.equals(base.getDisplayName()));
	}

	public void testGetTypeName() {
		/*
		 * There are 5 types of parameters in the ParameterDefnBase.java Here, only test
		 * 2 of them <li>IParameterDefnBase.PARAMETER_GROUP
		 * <li>IParameterDefnBase.SCALAR_PARAMETER
		 */
		String[] paramNames = new String[] { "paramString", "paramGroup" };
		String[] goldenTypeNames = new String[] { "scalar", "group" };
		assertTrue(paramNames.length == goldenTypeNames.length);
		for (int index = 0, size = paramNames.length; index < size; index++) {
			IParameterDefnBase base = gpdTask.getParameterDefn(paramNames[index]);
			assertTrue(goldenTypeNames[index].equals(base.getTypeName()));
		}
	}

	public void testGetSelectionTree() throws EngineException {
		destroy();
		initialize("org/eclipse/birt/report/engine/api/GetSelectionTreeTest.rptdesign");
		Map parentToChildren = new HashMap();
		parentToChildren.put("Singapore", new Object[] { new Integer(1621), new Integer(1612), null });
		parentToChildren.put("Hong Kong", new Object[] { new Integer(1621) });

		String[] parent = new String[] { "Singapore", "Hong Kong" };
		checkTree("DistinctFixedOrder", parent, parentToChildren);
		checkTree("MultiDataSetDistinctFixedOrder", parent, parentToChildren);

		parent = new String[] { "Singapore", "Singapore", "Singapore", "Hong Kong" };
		checkTree("NotDistinctFixedOrder", parent, parentToChildren);
		checkTree("MultiDataSetNotDistinctFixedOrder", parent, parentToChildren);

		parent = new String[] { "Hong Kong", "Singapore" };
		checkTree("DistinctNotFixedOrder", parent, parentToChildren);
		checkTree("MultiDataSetDistinctNotFixedOrder", parent, parentToChildren);

		parent = new String[] { "Hong Kong", "Singapore", "Singapore", "Singapore" };
		checkTree("NotDistinctNotFixedOrder", parent, parentToChildren);
		checkTree("MultiDataSetNotDistinctNotFixedOrder", parent, parentToChildren);
	}

	private void checkTree(String parameterName, String[] parent, Map parentToChildren) {
		Collection tree = gpdTask.getSelectionTreeForCascadingGroup(parameterName);
		Iterator iterator = tree.iterator();
		assertEquals(parent.length, tree.size());
		for (int i = 0; i < parent.length; i++) {
			ICascadingParameterSelectionChoice choice = (ICascadingParameterSelectionChoice) iterator.next();
			Object value = choice.getValue();
			assertEquals(parent[i], value);
			checkChildren((Object[]) parentToChildren.get(value), choice.getChildSelectionList());
		}
	}

	private void checkChildren(Object[] expectedChildren, Collection children) {
		assertEquals(expectedChildren.length, children.size());
		Iterator iterator = children.iterator();
		for (int i = 0; i < expectedChildren.length; i++) {
			ICascadingParameterSelectionChoice choice = (ICascadingParameterSelectionChoice) iterator.next();
			if (expectedChildren[i] != null)
				assertEquals(expectedChildren[i], choice.getValue());
			else
				assertNull(choice.getValue());
		}
	}
}
