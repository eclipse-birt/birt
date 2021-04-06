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

import java.util.ArrayList;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;

/**
 * 
 */

public class GetParameterGroupDefnTest extends EngineCase {

	protected IReportEngine engine = null;
	protected IReportRunnable runnable = null;
	protected IGetParameterDefinitionTask paramGroupTask = null;
	protected IScalarParameterDefn scalarParam = null;

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/GetParameterGroupDefnTest.rptdesign";
	static final String REPORT_DESIGN = "GetParameterGroupDefnTest.rptdesign";

	public void setUp() throws Exception {
		removeFile(REPORT_DESIGN);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);

		engine = createReportEngine();
		runnable = engine.openReportDesign(REPORT_DESIGN);
		paramGroupTask = engine.createGetParameterDefinitionTask(runnable);
	}

	public void tearDown() {
		// shut down the engine.
		if (engine != null) {
			engine.shutdown();
		}
		removeFile(REPORT_DESIGN);
	}

	/*
	 * get the parameter defined out of the parameter group
	 */
	public void testGetParameterOutofGroup() {
		IScalarParameterDefn paramDefn = (IScalarParameterDefn) paramGroupTask
				.getParameterDefn("paramStringOutofGroup");
		assert ("outof".equals(paramDefn.getDefaultValue()));
	}

	/*
	 * Get the parameter group by name and test every parameters defined in this
	 * group
	 */
	public void testGetGroupByName() {
		final String PARAM_GROUP_NAME = "paramGroup";
		final String PARAM_GROUP_DISP_NAME = "paramGroupDispName";
		final int PARAM_COUNT_IN_GROUP = 11;
		IParameterGroupDefn paramGroupDefn = (IParameterGroupDefn) paramGroupTask.getParameterDefn("paramGroup");
		assertTrue(paramGroupDefn != null);
		assertTrue(PARAM_GROUP_NAME.equals(paramGroupDefn.getName()));
		assertTrue(PARAM_GROUP_DISP_NAME.equals(paramGroupDefn.getDisplayName()));
		ArrayList parameters = paramGroupDefn.getContents();
		assertTrue(PARAM_COUNT_IN_GROUP == parameters.size());

		IParameterDefnBase param = null;
		final String[] goldenParamNames = new String[] { "paramString", "paramInteger", "paramFloat", "paramDecimal",
				"paramDateTime", "paramBoolean", "paramStringListBoxStatic", "paramComboBoxStatic",
				"paramRadioButtonStatic", "paramStringListBoxDynamic", "paramStringComboBoxDynamic" };
		assertTrue(PARAM_COUNT_IN_GROUP == goldenParamNames.length);
		for (int size = parameters.size(), index = 0; index < size; index++) {
			param = (IParameterDefnBase) parameters.get(index);
			assertTrue(goldenParamNames[index].equals(param.getName()));
		}
	}

	/*
	 * Get the parameter by name and this parameter is in some parameter group
	 */
	public void testGetParameterDirectlyByName() {
		final String PARAMETER_NAME = "paramString";
		IParameterDefn paramDefn = (IParameterDefn) paramGroupTask.getParameterDefn(PARAMETER_NAME);
		assertTrue(paramDefn != null);
	}
}
