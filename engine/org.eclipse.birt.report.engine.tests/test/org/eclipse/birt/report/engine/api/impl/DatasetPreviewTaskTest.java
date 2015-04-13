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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IDatasetPreviewTask;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SessionHandle;

import com.ibm.icu.util.ULocale;

/**
 * in the report design, we define four listing elements: 219: table with query.
 * 277: list of query 280: a table with sub query 289: a table with nest query.
 */
public class DatasetPreviewTaskTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/impl/TestDataExtractionTask.xml";
	static final String REPORT_LIBRARY_RESOURCE = "org/eclipse/birt/report/engine/api/impl/library.xml";

	IReportDocument document;
	IDatasetPreviewTask previewTask;

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeFile( REPORT_DESIGN );
		
	}

	public void tearDown( ) throws Exception
	{
		super.tearDown();
	}
	
	protected ModuleHandle getHandle(String fileName) throws Exception
	{
		ModuleHandle designHandle;
		// Create new design session
		SessionHandle sessionHandle = new DesignEngine( new DesignConfig( ) )
				.newSessionHandle( ULocale.getDefault( ) );
		designHandle = sessionHandle.openModule( fileName );
		return designHandle;
	}
	
	public void testPreviewDatasetInLib( ) throws Exception
	{		
		copyResource( REPORT_LIBRARY_RESOURCE, REPORT_DESIGN );
		previewTask = engine.createDatasetPreviewTask( );
		ModuleHandle muduleHandle = getHandle(REPORT_DESIGN );
		List ds = muduleHandle.getAllDataSets( );
		for ( Object obj : ds )
		{
			DataSetHandle dataset = (DataSetHandle) obj;
			if ( dataset.getName( ).equals( "Data Set" ) )
			{
				previewTask.setDataSet( dataset );
			}
		}

		previewTask.setMaxRow( 20 );
		IExtractionResults results = previewTask.execute( );
		int rowCount = checkExtractionResults( results );
		assertTrue( rowCount == 20 );
		previewTask.close( );
		removeFile( REPORT_DESIGN );
	}

	public void testPreviewDatasetInReport( ) throws Exception
	{		
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		previewTask = engine.createDatasetPreviewTask( );
		IReportRunnable reportDesign = engine.openReportDesign( REPORT_DESIGN );
		List ds = reportDesign.getDesignHandle( ).getModuleHandle( )
				.getAllDataSets( );
		for ( Object obj : ds )
		{
			DataSetHandle dataset = (DataSetHandle) obj;
			if ( dataset.getName( ).equals( "DataSet" ) )
			{
				previewTask.setDataSet( dataset );
			}
		}

		previewTask.setMaxRow( 5 );
		IExtractionResults results = previewTask.execute( );
		int rowCount = checkExtractionResults( results );
		assertTrue( rowCount == 5 );
		previewTask.close( );
		removeFile( REPORT_DESIGN );
	}

	/**
	 * access all the data in the results, no exception should be throw out.
	 * 
	 * @param results
	 * @return row count in the result.
	 * @throws BirtException
	 */
	protected int checkExtractionResults( IExtractionResults results )
			throws Exception
	{
		int rowCount = 0;
		IDataIterator dataIter = results.nextResultIterator( );
		if ( dataIter != null )
		{
			while ( dataIter.next( ) )
			{
				IResultMetaData resultMeta = dataIter.getResultMetaData( );
				for ( int i = 0; i < resultMeta.getColumnCount( ); i++ )
				{
					Object obj = dataIter.getValue( resultMeta
							.getColumnName( i ) );
					String type = resultMeta.getColumnTypeName( i );
					assertTrue( type != null );
					System.out.print(obj + " ");
				}
				rowCount++;
				System.out.println();
			}
			dataIter.close( );
		}
		results.close( );
		return rowCount;
	}

	private Set<InstanceID> getAllInstanceIds( IReportDocument document )
			throws EngineException, UnsupportedEncodingException
	{
		Set<InstanceID> instanceIds = new HashSet<InstanceID>( );
		IRenderTask task = engine.createRenderTask( document );

		ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
		// create the render options
		HTMLRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" );
		option.setOutputStream( ostream );
		option.setEnableMetadata( true );
		// set the render options
		task.setRenderOption( option );
		assertTrue( task.getRenderOption( ).equals( option ) );
		task.render( );
		task.close( );

		String content = ostream.toString( "utf-8" );
		// get all the instance ids
		Pattern iidPattern = Pattern.compile( "iid=\"([^\"]*)\"" );
		Matcher matcher = iidPattern.matcher( content );
		while ( matcher.find( ) )
		{
			String strIid = matcher.group( 1 );
			InstanceID iid = InstanceID.parse( strIid );
			instanceIds.add( iid );
		}
		return instanceIds;
	}
}