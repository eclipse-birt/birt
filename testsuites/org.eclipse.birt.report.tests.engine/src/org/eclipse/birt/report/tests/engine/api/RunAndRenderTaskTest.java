/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>IRunAndRenderTask test</b>
 * <p>
 * This case tests methods in IRunAndRenderTask API.
 * 
 */
public class RunAndRenderTaskTest extends EngineCase
{

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( "report_engine.rptdesign", "report_engine.rptdesign" );
		copyResource_INPUT( "pages9.rptdesign", "pages9.rptdesign" );
	}

	public void tearDown( )
	{
		removeResource( );
	}
	
	private Boolean cancelSignal = new Boolean( false );

	/**
	 * Test methods in RunAndRenderTask class
	 */
	public void testRunAndRenderTask( ) throws EngineException
	{

		String input = this.genInputFile( "report_engine.rptdesign" );

		try
		{
			IReportRunnable runnable = engine
					.openReportDesign( new FileInputStream( new File( input ) ) );
			IRunAndRenderTask task = engine.createRunAndRenderTask( runnable );
			// validateParameters
			assertTrue( task.validateParameters( ) );
			// set/getRenderOption
			RenderOptionBase option = new RenderOptionBase( ), optionGet;
			task.setRenderOption( option );
			optionGet = (RenderOptionBase) task.getRenderOption( );
			assertEquals( "set/getRenderOption fail", option, optionGet );

			// parameters
			HashMap hm = new HashMap( ), hmGet;
			task.setParameterValues( hm );
			hmGet = task.getParameterValues( );
			assertEquals( "set/getParameterValues(hashmap) fail", hm, hmGet );

			task.setParameterValue( "p1", "p1value" );
			assertEquals( "Set/getParameterValues fail", task
					.getParameterValues( ).get( "p1" ), "p1value" );
		}
		catch ( FileNotFoundException e )
		{
			e.printStackTrace( );
		}
	}

	/*
	 * Cancel RunAndRenderTask in another thread with cancel method.
	 */
	public void testCancelRunAndRenderTask( )
	{
		String input = this.genInputFile( "pages9.rptdesign" );
		long bTime, eTime, timeSpan1, timeSpan2, timeSpan3;
		try
		{
			IReportRunnable runnable = engine
					.openReportDesign( new FileInputStream( new File( input ) ) );
			IRunAndRenderTask task = engine.createRunAndRenderTask( runnable );
			HTMLRenderOption option = new HTMLRenderOption( );
			option.setOutputFormat( "html" );
			task.setRenderOption( option );
			task.setAppContext( new HashMap( ) );

			CancelTask cancelThread = new CancelTask( "cancelThread", task );
			cancelThread.start( );
			bTime = System.currentTimeMillis( );
			task.run( );
			eTime = System.currentTimeMillis( );
			task.close( );
			timeSpan1 = eTime - bTime;

			CancelWithFlagTask cancelWithFlagTask = new CancelWithFlagTask(
					"cancelWithFlagTask", task );
			cancelWithFlagTask.start( );
			bTime = System.currentTimeMillis( );
			task.run( );
			eTime = System.currentTimeMillis( );
			task.close( );
			timeSpan2 = eTime - bTime;

			task = engine.createRunAndRenderTask( runnable );
			task.setRenderOption( option );
			task.setAppContext( new HashMap( ) );
			bTime = System.currentTimeMillis( );
			task.run( );
			eTime = System.currentTimeMillis( );
			task.close( );
			timeSpan3 = eTime - bTime;

			assertTrue( "RunAndRenderTask.cancel() failed!",
					timeSpan3 > timeSpan1 );
			assertTrue( "RunAndRenderTask.cancel(signal) failed!",
					timeSpan3 > timeSpan2 );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			fail( "RunAndRenderTask.cancel() failed!" );
		}

	}

	/*
	 * A new thread to cancel existed runTask
	 */
	private class CancelTask extends Thread
	{

		private IRunAndRenderTask runTask;

		public CancelTask( String threadName, IRunAndRenderTask task )
		{
			super( threadName );
			runTask = task;
		}

		public void run( )
		{
			try
			{
				Thread.currentThread( ).sleep( 100 );
				runTask.cancel( );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
				fail( "RunAndRenderTask.cancel() failed!" );
			}
		}

	}

	/*
	 * A new thread to cancel existed runTask which return a flag
	 */
	private class CancelWithFlagTask extends Thread
	{

		private IRunAndRenderTask runTask;

		public CancelWithFlagTask( String threadName, IRunAndRenderTask task )
		{
			super( threadName );
			runTask = task;
		}

		public void run( )
		{
			try
			{
				Thread.currentThread( ).sleep( 100 );
				runTask.cancel( cancelSignal );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
				fail( "RunAndRenderTask.cancel() failed!" );
			}
		}

	}

}
