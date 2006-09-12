
package org.eclipse.birt.report.tests.engine.api;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>IRunTask test</b>
 * <p>
 * This case tests methods in IRunTask API.
 * 
 */
public class RunTaskTest extends EngineCase
{

	private Boolean cancelSignal = new Boolean( false );
	private String separator = System.getProperty( "file.separator" );
	private String INPUT = getClassFolder( ) + separator + INPUT_FOLDER
			+ separator;
	private String OUTPUT = getClassFolder( ) + separator + OUTPUT_FOLDER
			+ separator;
	private String report_design, report_document, name;
	private IReportRunnable runnable;

	public RunTaskTest( String name )
	{
		super( name );
	}

	public static Test Suite( )
	{
		return new TestSuite( RunTaskTest.class );
	}

	/**
	 * Test two Run method with different argument.
	 * 
	 */
	public void testRunTask1( )
	{
		runReport( "case1" );
	}

	public void testRunTask2( )
	{
		runReport( "long_text" );
	}

	public void testRunTask3( )
	{
		runReport( "master_page" );
	}

	public void testRunTask4( )
	{
		runReport( "multiple_datasets" );
	}

	public void testRunTask5( )
	{
		runReport( "pages9" );
	}

	public void testRunTask6( )
	{
		runReport( "table_nest_pages" );
	}

	public void testRunTask7( )
	{
		runReport( "chart" );
	}

	public void testRunTask8( )
	{
		runReport( "complex_report" );
	}

	public void testRunTask9( )
	{
		runReport( "area3dChart" );
	}

	public void testRunTask10( )
	{
		runReport( "image_in_DB" );
	}

	public void testRunTask11( )
	{
		runReport( "MeterChart" );
	}

	public void testCancel( )
	{
		report_design = INPUT + "pages9.rptdesign";
		String fileDocument = OUTPUT + "cancel_pages9.rptdocument";
		long bTime, eTime, timeSpan1, timeSpan2, timeSpan3;
		try
		{
			runnable = engine.openReportDesign( report_design );
			IRunTask task = engine.createRunTask( runnable );
			CancelTask cancelThread = new CancelTask( "cancelThread", task );
			cancelThread.start( );
			bTime = System.currentTimeMillis( );
			task.run( fileDocument );
			eTime = System.currentTimeMillis( );
			task.close( );
			timeSpan1 = eTime - bTime;

			task = engine.createRunTask( runnable );
			CancelWithFlagTask cancelWithFlagTask = new CancelWithFlagTask(
					"cancelWithFlagTask", task );
			cancelWithFlagTask.start( );
			bTime = System.currentTimeMillis( );
			task.run( fileDocument );
			eTime = System.currentTimeMillis( );
			task.close( );
			timeSpan2 = eTime - bTime;

			task = engine.createRunTask( runnable );
			bTime = System.currentTimeMillis( );
			task.run( fileDocument );
			eTime = System.currentTimeMillis( );
			task.close( );
			timeSpan3 = eTime - bTime;

			removeFile( fileDocument );

			assertTrue( "RunTask.cancel() failed!", ( timeSpan3 > timeSpan1 ) );
			assertTrue( "RunTask.cancel(signal) failed!",
					( timeSpan3 > timeSpan2 ) );

		}
		catch ( EngineException ee )
		{
			ee.printStackTrace( );
			fail( "RunTask.cancel() failed!" );
		}
	}

	public void testGetErrors( )
	{
		report_design = INPUT + "jdbc_exception.rptdesign";
		String fileDocument = OUTPUT + "jdbc_exception.rptdocument";

		try
		{
			runnable = engine.openReportDesign( report_design );
			IRunTask task = engine.createRunTask( runnable );
			task.run( fileDocument );

			if ( task != null )
			{
				assertTrue( "IRunTask.getErrors() fails!",
						task.getErrors( ) != null );
				assertTrue(
						"IRunTask.getErrors() returns wrong exception",
						task
								.getErrors( )
								.get( 0 )
								.getClass( )
								.toString( )
								.equalsIgnoreCase(
										"class org.eclipse.birt.data.engine.core.DataException" ) );
			}
			task.close( );
		}
		catch ( Exception e )
		{
		}

	}

	private void runReport( String report )
	{
		report_design = INPUT + report + ".rptdesign";
		String fileDocument = OUTPUT + report + ".rptdocument";
		String folderDocument = OUTPUT + "runtask_folderdocument_" + report
				+ separator;
		try
		{
			runnable = engine.openReportDesign( report_design );
			IRunTask task = engine.createRunTask( runnable );
			task.run( fileDocument );
			task.run( folderDocument );
			task.close( );

			assertTrue( "Fail to generate file archive for " + report,
					new File( fileDocument ).exists( ) );
			assertTrue( "Fail to generate folder archive for " + report,
					new File( folderDocument ).exists( ) );
		}
		catch ( EngineException ee )
		{
			ee.printStackTrace( );
			assertTrue( "Failed to generate document for " + report
					+ ee.getLocalizedMessage( ), false );
		}
	}

	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/*
	 * A new thread to cancel existed runTask
	 */
	private class CancelTask extends Thread
	{

		private IRunTask runTask;

		public CancelTask( String threadName, IRunTask task )
		{
			super( threadName );
			runTask = task;
		}

		public void run( )
		{
			try
			{
				System.out.print( "cancel started waiting" );
				Thread.currentThread( ).sleep( 100 );
				System.out.print( "cancel stop waiting" );
				runTask.cancel( );
				System.out.print( "cancel done" );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
				fail( "RunTask.cancel() failed" );
			}
		}

	}

	/*
	 * A new thread to cancel existed runTask which return a flag
	 */
	private class CancelWithFlagTask extends Thread
	{

		private IRunTask runTask;

		public CancelWithFlagTask( String threadName, IRunTask task )
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
				fail( "RunTask.cancel(signal) failed" );
			}
		}

	}
}
