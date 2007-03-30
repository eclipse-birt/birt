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

package org.eclipse.birt.report.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.eclipse.birt.report.engine.EngineCase;

public class EngintTaskLoggerTest extends EngineCase
{

	public void testLogger( ) throws Exception
	{

		Logger logger = Logger.getAnonymousLogger( );
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		StreamHandler handler = new StreamHandler( out, new SimpleFormatter( ) );
		handler.setLevel( Level.FINEST );
		logger.addHandler( handler );
		logger.setLevel( Level.FINE );

		engine.setLogger( logger );

		new File( "./utest/" ).mkdirs( );
		copyResource(
				"org/eclipse/birt/report/engine/api/engine-task-logger-test.rptdesign",
				"./utest/reportdesign.rptdesign" );
		IReportRunnable runnable = engine
				.openReportDesign( "./utest/reportdesign.rptdesign" );
		IRunTask task = engine.createRunTask( runnable );
		task.setParameter( "sample", "==golden values==", "displayText" );
		task.run( "./utest/report.rptdocument" );
		task.close( );
		engine.setLogger( logger );

		removeFile( "./utest" );

		handler.close( );
		System.out.println( out.toString( ) );
		assertTrue( out.toString( ).indexOf( "==golden values==" ) != -1 );

	}
}
