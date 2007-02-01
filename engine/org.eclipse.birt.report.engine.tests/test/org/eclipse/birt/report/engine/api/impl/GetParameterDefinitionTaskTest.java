/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.Collection;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.GetParameterDefinitionTaskUtil.SelectionChoiceUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class GetParameterDefinitionTaskTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/impl/TestGetParameterTask.xml"; //$NON-NLS-1$
	static final String REPORT_DESIGN = "GetParameterTaskTest.rptdesign"; //$NON-NLS-1$

	protected IReportEngine engine;

	public void setUp( )
	{
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		// create the report engine using default config
		engine = createReportEngine( );
	}

	public void tearDown( )
	{
		// shut down the engine.
		engine.shutdown( );
		removeFile( REPORT_DESIGN );
	}

	public void testParameterWithDataSet( ) throws SemanticException,
			EngineException
	{
		IReportRunnable report = engine.openReportDesign( REPORT_DESIGN );
		ReportDesignHandle design = (ReportDesignHandle) report
				.getDesignHandle( ).getModuleHandle( );
		ScalarParameterHandle parameter1 = (ScalarParameterHandle) design
				.findParameter( "NewParameter2" ); //$NON-NLS-1$
		parameter1.setDataSetName( "Data Set1" ); //$NON-NLS-1$
		ScalarParameterHandle parameter2 = (ScalarParameterHandle) design
				.findParameter( "NewParameter3" ); //$NON-NLS-1$
		parameter2.setDataSetName( "Data Set" ); //$NON-NLS-1$
		GetParameterDefinitionTask task = new GetParameterDefinitionTask(
				engine, report );
		Collection list = task.getSelectionListForCascadingGroup(
				"NewCascadingParameterGroup", new Object[0] ); //$NON-NLS-1$
		Object[] content = list.toArray( );
		assertEquals( "1002", ( SelectionChoiceUtil.getValue( content[0] ) ) ); //$NON-NLS-1$
		list = task.getSelectionListForCascadingGroup(
				"NewCascadingParameterGroup", new Object[]{"1002"} ); //$NON-NLS-1$ //$NON-NLS-2$
		content = list.toArray( );
		assertEquals(
				"Atelier graphique", ( SelectionChoiceUtil.getValue( content[0] ) ) ); //$NON-NLS-1$
	}
}
