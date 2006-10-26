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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;

public class ReportletTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/reportlet.rptdesign";
	static final String REPORT_DESIGN_RESOURCE2 = "org/eclipse/birt/report/engine/api/reportlet1.rptdesign";

	public void setUp( )
	{
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		// create the report engine using default config
		engine = createReportEngine( );
	}

	public void tearDown( )
	{
		// shut down the engine.
		engine.shutdown( );
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}

	public void testReportlet( ) throws Exception
	{
		ArrayList iidList = new ArrayList( );

		// first execute the report to get the reportlet
		IReportRunnable runnable = engine.openReportDesign( REPORT_DESIGN );
		IRunTask task = engine.createRunTask( runnable );
		task.run( REPORT_DOCUMENT );
		task.close( );

		// render the whole text to html.
		IReportDocument document = engine.openReportDocument( REPORT_DOCUMENT );
		IRenderTask render = engine.createRenderTask( document );
		ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
		HTMLRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" );
		option.setOutputStream( ostream );
		render.setRenderOption( option );
		render.render( );
		render.close( );

		// for all the reportlets
		String content = ostream.toString( "utf-8" );
		Pattern iidPattern = Pattern.compile( "iid=\"(.*)\"" );
		Matcher matcher = iidPattern.matcher( content );
		while ( matcher.find( ) )
		{
			String strIid = matcher.group( 1 );
			InstanceID iid = InstanceID.parse( strIid );
			iidList.add( iid );
			long designId = iid.getComponentID( );
			runnable = render.getReportRunnable( );
			ReportDesignHandle report = (ReportDesignHandle) runnable
					.getDesignHandle( );
			DesignElementHandle element = report.getElementByID( designId );
			if ( element instanceof TableHandle )
			{
				// we get the report let
				render = engine.createRenderTask( document );
				option = new HTMLRenderOption( );
				option.setOutputFormat( "html" );
				ByteArrayOutputStream out = new ByteArrayOutputStream( );
				option.setOutputStream( out );
				render.setRenderOption( option );
				render.setInstanceID( iid.toString( ) );

				render.render( );
				render.close( );
				System.out.println( out.toString( "utf-8" ) );
				assertTrue( out.toString( "utf-8" ).length( ) > 2048 );
			}
		}

		/*
		 * API test on IReportDocument.getPageNumber( InstanceID )
		 * And here only test on the first *offset*
		 */ 
		int[] goldenPageNumbers = new int[]{1};/* is the first page */
		InstanceID iidTemp = (InstanceID) iidList.get( 0 );
		assertTrue( goldenPageNumbers[0] == document.getPageNumber( iidTemp ) );
		assertTrue( document.getInstanceOffset( iidTemp ) > 0 );
		render.close( );
		document.close( );
	}

	public void testRenderReportlet( ) throws Exception
	{
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE2, REPORT_DESIGN );
		// create the report engine using default config
		engine = createReportEngine( );

		createReportDocument( );
		doRenderReportletTest( );
		engine.shutdown( );
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}

	protected void doRenderReportletTest( ) throws Exception
	{
		// open the document in the archive.
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );
		// create an RenderTask using the report document
		IRenderTask task = engine.createRenderTask( reportDoc );
		// get the page number
		List bookmarks = reportDoc.getBookmarks( );
		assertEquals( 2, bookmarks.size( ) );
		String[] contents = new String[]{"test_reportlet_table1",
				"test_reportlet_table2"};
		List instanceIds = getTableInstanceIds( );
		assertEquals( 2, instanceIds.size( ) );
		for ( int i = 0; i <= 1; i++ )
		{
			task.setReportlet( (String) bookmarks.get( i ) );
			testRender( task, contents, i );
			task.setInstanceID( ( InstanceID ) instanceIds.get( i ) );
			testRender( task, contents, i );
		}

		task.close( );

		reportDoc.close( );
	}

	private void testRender( IRenderTask task, String[] contents, int i ) throws EngineException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		render( task, outputStream );
		String content = new String( outputStream.toByteArray( ) );
		assertTrue( contains( content, contents[i] ) );
		assertFalse( contains( content, contents[1 - i] ) );
	}

	private boolean contains( String content, String searchString )
	{
		return content.indexOf( searchString ) >= 0;
	}

	private void render( IRenderTask task, OutputStream outputStream )
			throws EngineException
	{
		IRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" ); //$NON-NLS-1$
		option.setOutputStream( outputStream );
		// set the render options
		task.setRenderOption( option );
		// render report by page
		task.render( );
	}

	private List getTableInstanceIds( ) throws EngineException, UnsupportedEncodingException
	{
		List result = new ArrayList( );
		IReportRunnable runnable;
		IReportDocument document = engine.openReportDocument( REPORT_DOCUMENT );
		IRenderTask render = engine.createRenderTask( document );
		ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
		HTMLRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" );
		option.setOutputStream( ostream );
		render.setRenderOption( option );
		render.render( );
		render.close( );

		// for all the reportlets
		String content = ostream.toString( "utf-8" );
		Pattern iidPattern = Pattern.compile( "iid=\"(.*)\"" );
		Matcher matcher = iidPattern.matcher( content );
		while ( matcher.find( ) )
		{
			String strIid = matcher.group( 1 );
			InstanceID iid = InstanceID.parse( strIid );
			long designId = iid.getComponentID( );
			runnable = render.getReportRunnable( );
			ReportDesignHandle report = (ReportDesignHandle) runnable
					.getDesignHandle( );
			DesignElementHandle element = report.getElementByID( designId );
			if ( element instanceof TableHandle )
			{
				result.add( iid );
			}
		}
		document.close( );
		return result;
	}
}