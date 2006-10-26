
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
	
	protected IReportEngine engine = null;
	
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
		EngineConfig config = new EngineConfig( );
		this.engine = createReportEngine( config );
	}

	protected void tearDown( ) throws Exception
	{
		this.engine.shutdown( );
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
		super.tearDown( );
	}

	public void testRunTaskMultithreadCancel()
	{
		try
		{
			IReportRunnable runnable = engine.openReportDesign( REPORT_DESIGN );
			IRunTask engineTask = engine.createRunTask( runnable );
			
			CancelTask cancelTask = new CancelTask( "CancleTask", engineTask );
			cancelTask.start( );
			
			engineTask.setPageHandler( new RenderTaskTrigger( engine, cancelTask ) );
			
			engineTask.run( REPORT_DOCUMENT );
			engineTask.close( );
			
			IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
			assertTrue( reportDoc.getPageCount( ) < 6 );
			reportDoc.close( );
		}
		catch ( EngineException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail( "EngineTask.cancel() failed!" );
		}
	}
	
	public void testRunTaskMultithreadCancelSignal()
	{
		try
		{
			IReportRunnable runnable = engine.openReportDesign( REPORT_DESIGN );
			IRunTask engineTask = engine.createRunTask( runnable );
			
			Monitor monitor = new Monitor( "Monitor Thread" );
			monitor.start( );
			CancelTask cancelTask = new CancelTask( "CancleTask", engineTask, monitor );
			cancelTask.start( );
			
			engineTask.setPageHandler( new RenderTaskTrigger( engine, cancelTask ) );
			
			engineTask.run( REPORT_DOCUMENT );
			engineTask.close( );
			
			if(!monitor.notified)
			{
				try
				{
					Thread.sleep( 500 );
				}
				catch ( InterruptedException e )
				{
				}
			}
			assertTrue(monitor.notified );
			
			IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
			assertTrue( reportDoc.getPageCount( ) < 6 );
			reportDoc.close( );
		}
		catch ( EngineException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail( "EngineTask.cancel() failed!" );
		}
	}
	
	public void testRunTaskCancel()
	{
		try
		{
			IReportRunnable runnable = engine.openReportDesign( REPORT_DESIGN );
			IRunTask engineTask = engine.createRunTask( runnable );
			
			engineTask.setPageHandler( new RenderTaskTriggerCancel( engine, engineTask, null ) );
			
			engineTask.run( REPORT_DOCUMENT );
			engineTask.close( );
			
			IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
			assertTrue( reportDoc.getPageCount( ) < 6 );
			reportDoc.close( );
		}
		catch ( EngineException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail( "EngineTask.cancel() failed!" );
		}
	}
	
	public void testRunTaskCancelSignal()
	{
		try
		{
			IReportRunnable runnable = engine.openReportDesign( REPORT_DESIGN );
			IRunTask engineTask = engine.createRunTask( runnable );
			
			Monitor monitor = new Monitor( "Monitor Thread" );
			monitor.start( );
			
			engineTask.setPageHandler( new RenderTaskTriggerCancel( engine, engineTask, monitor ) );
			
			engineTask.run( REPORT_DOCUMENT );
			engineTask.close( );
			
			if(!monitor.notified)
			{
				try
				{
					Thread.sleep( 500 );
				}
				catch ( InterruptedException e )
				{
				}
			}
			assertTrue(monitor.notified );
			
			IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
			assertTrue( reportDoc.getPageCount( ) < 6 );
			reportDoc.close( );
		}
		catch ( EngineException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail( "EngineTask.cancel() failed!" );
		}
	}
	
	private class Monitor extends Thread
	{
		public boolean notified = false;
		public Monitor( String threadName )
		{
			super( threadName );
		}
		
		public void run()
		{
			synchronized( this )
			{
				try
				{
					wait( );
				}
				catch ( InterruptedException e )
				{
				}
				notified = true;
			}
		}
	}
	
	/*
	 * A new thread to cancel existed runTask
	 */
	private class CancelTask extends Thread
	{

		private IEngineTask runTask;
		Object signal = null;

		public CancelTask( String threadName, IEngineTask task )
		{
			super( threadName );
			runTask = task;
		}
		
		public CancelTask( String threadName, IEngineTask task, Object signal)
		{
			super( threadName );
			runTask = task;
			this.signal = signal;
		}
		
		public void run( )
		{
			synchronized (this)
			{
				try
				{
					wait( );
				}
				catch(InterruptedException ex)
				{
				}
			}
			try
			{
				if( null == signal)
				{
					runTask.cancel( );
				}
				else
				{
					runTask.cancel( signal );
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
				fail( "EngineTask.cancel() failed!" );
			}
		}
		
		public void cancel()
		{
			synchronized(this)
			{
				notifyAll( );
			}
		}
	}
	
	private class RenderTaskTrigger implements IPageHandler
	{
		IReportEngine engine;
		CancelTask cancelTask;

		RenderTaskTrigger( IReportEngine engine, CancelTask cancelTask )
		{
			this.engine = engine;
			this.cancelTask = cancelTask;
		}

		public void onPage( int pageNumber, boolean checkpoint,
				IReportDocumentInfo doc )
		{
			try
			{
				if ( pageNumber == 2 )
				{
					cancelTask.cancel( );
				}
				if ( pageNumber == 5 )
				{
					Thread.sleep( 500 );
				}
				if ( pageNumber == 6 )
				{
					fail( "EngineTask.cancel() failed!" );
				}
			}
			catch ( Exception ex )
			{
				ex.printStackTrace( );
				fail( "EngineTask.cancel() failed!" );
			}
		}
	}
	
	private class RenderTaskTriggerCancel implements IPageHandler
	{

		IEngineTask engineTask;
		IReportEngine engine;
		Object signal = null;

		RenderTaskTriggerCancel( IReportEngine engine, IEngineTask task, Object signal )
		{
			this.engine = engine;
			this.engineTask = task;
			this.signal = signal;
		}

		public void onPage( int pageNumber, boolean checkpoint,
				IReportDocumentInfo doc )
		{
			try
			{
				if ( pageNumber == 2 )
				{
					if( null == signal)
					{
						engineTask.cancel( );
					}
					else
					{
						engineTask.cancel( signal );
					}
				}
				if ( pageNumber == 5 )
				{
					Thread.sleep( 500 );
				}
				if ( pageNumber == 6 )
				{
					fail( "EngineTask.cancel() failed!" );
				}
			}
			catch ( Exception ex )
			{
				ex.printStackTrace( );
				fail( "EngineTask.cancel() failed!" );
			}
		}
	}
}
