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

import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_BETWEEN;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_BOTTOM_N;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_BOTTOM_PERCENT;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_EQ;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_FALSE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_GE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_GT;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_IN;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_LE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_LIKE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_LT;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_MATCH;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_BETWEEN;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_IN;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_LIKE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_MATCH;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_NULL;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NULL;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_TOP_N;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_TOP_PERCENT;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_TRUE;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
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
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * in the report design, we define four listing elements: 219: table with query.
 * 277: list of query 280: a table with sub query 289: a table with nest query.
 */
public class DatasetPreviewTaskTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/impl/TestDataExtractionTask.xml";

	IReportDocument document;
	IDatasetPreviewTask previewTask;

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		previewTask = engine.createDatasetPreviewTask( );
	}

	public void tearDown( )
	{
		previewTask.close( );
		removeFile( REPORT_DESIGN );
	}

	public void testPreviewDataset( ) throws Exception
	{
		IReportRunnable reportDesign = engine.openReportDesign( REPORT_DESIGN );
		// get the instance id
		// open the document in the archive.
		// create an RenderTask using the report document
		previewTask.setRunnable( reportDesign );
		previewTask.setDataSet( "DataSet" );
		previewTask.setMaxRow( 5 );
		IExtractionResults results = previewTask.execute( );
		int rowCount = checkExtractionResults( results );
		assertTrue(rowCount==5);
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
				}
				rowCount++;
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