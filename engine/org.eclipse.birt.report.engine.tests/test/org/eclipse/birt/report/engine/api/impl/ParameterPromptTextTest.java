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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * 
 */

public class ParameterPromptTextTest extends EngineCase
{

	static final String REPORT_PROMPT_TEXT_SOURCE = "org/eclipse/birt/report/engine/api/impl/prompt_text.xml";
	static final String REPORT_GROUP_PROMPT_TEXT_SOURCE = "org/eclipse/birt/report/engine/api/impl/parameterGroupDefnPromptText.xml";

	static final String REPORT_PROMPT_TEXT_DESIGN = "prompt_text.rptdesign";
	static final String REPORT_GROUP_PROMPT_TEXT_DESIGN = "parameterGroupDefnPromptText.rptdesign";

	protected ScalarParameterDefn scalarParameter;
	protected ArrayList parameters;

	public void setUp( ) throws Exception
	{
		super.setUp( );
		copyResource( REPORT_PROMPT_TEXT_SOURCE, REPORT_PROMPT_TEXT_DESIGN );
		copyResource( REPORT_GROUP_PROMPT_TEXT_SOURCE,
				REPORT_GROUP_PROMPT_TEXT_DESIGN );
	}

	public void tearDown( ) throws Exception
	{
		removeFile( REPORT_PROMPT_TEXT_DESIGN );
		removeFile( REPORT_GROUP_PROMPT_TEXT_DESIGN );
		super.tearDown();
	}

	public void testPromptText( ) throws EngineException
	{
		IReportRunnable runnable = engine.openReportDesign( REPORT_PROMPT_TEXT_DESIGN );
		ReportDesignHandle handle = (ReportDesignHandle) ( (ReportRunnable) runnable ).getDesignHandle( );
		GetParameterDefinitionTask paramTask = (GetParameterDefinitionTask) engine.createGetParameterDefinitionTask( runnable );
		parameters = paramTask.getParameters( handle, true );
		assertTrue( parameters != null );

		// prompt text
		if ( parameters != null && parameters.size( ) > 0 )
		{
			assertEquals( "testPromptText",
					( (ScalarParameterDefn) parameters.get( 0 ) ).getPromptText( ) );
		}
	}

	public void testCascadingParameterPromptText( ) throws EngineException
	{
		IReportRunnable runnable = engine.openReportDesign( REPORT_GROUP_PROMPT_TEXT_DESIGN );
		ReportDesignHandle handle = (ReportDesignHandle) ( (ReportRunnable) runnable ).getDesignHandle( );
		GetParameterDefinitionTask paramTask = (GetParameterDefinitionTask) engine.createGetParameterDefinitionTask( runnable );
		parameters = paramTask.getParameters( handle, true );
		assertTrue( parameters != null );
		if ( parameters != null && parameters.size( ) > 0 )
		{
			for ( int i = 0; i < parameters.size( ); i++ )
			{
				IParameterDefnBase parameter = (IParameterDefnBase) parameters.get( i );
				if ( parameter instanceof ParameterGroupDefn )
				{
					assertEquals( "testParameterGroupDefnPromptText",
							( (ParameterGroupDefn) parameter ).getPromptText( ) );
				}
			}
		}
	}
}
