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

import org.eclipse.birt.report.engine.EngineCase;

/**
 * 
 */

public class EngineTaskCancelTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/six_pages_design.xml";
	static final String REPORT_DESIGN = "design.rptdesign";
	static final String REPORT_DOCUMENT = "./reportdocument.folder/";

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}

	protected void tearDown( ) throws Exception
	{
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
		super.tearDown( );
	}

	public void testCancelInPageHandler( ) throws Exception
	{
		IReportRunnable runnable = engine.openReportDesign( REPORT_DESIGN );
		IRunTask runTask = engine.createRunTask( runnable );

		runTask.setPageHandler( new CancelPageHandler( runTask ) );
		runTask.run( REPORT_DOCUMENT );
		runTask.close( );

		assertEquals( IEngineTask.STATUS_CANCELLED, runTask.getStatus( ) );
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
		assertTrue( reportDoc.getPageCount( ) < 6 );
		reportDoc.close( );
	}

	private class CancelPageHandler implements IPageHandler
	{

		IEngineTask task;

		CancelPageHandler( IEngineTask task )
		{
			this.task = task;
		}

		public void onPage( int pageNumber, boolean checkpoint,
				IReportDocumentInfo doc )
		{
			if ( pageNumber == 2 )
			{
				task.cancel( );
			}
		}
	}
}
