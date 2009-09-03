/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.excel.ExcelXmlWriter.XMLWriterXLS;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelContext;

/**
 * @author Administrator
 * 
 */
public class ExcelWriter implements IExcelWriter
{

	private ExcelXmlWriter writer, tempWriter;
	private final ExcelContext context;
	private final OutputStream out;
	private final boolean isRTLSheet;
	private String tempFilePath;
	private int sheetIndex = 1;

	/**
	 * @param out
	 * @param context
	 * @param isRtlSheet
	 * @param pageHeader
	 * @param pageFooter
	 * @param orientation
	 */
	public ExcelWriter( OutputStream out, ExcelContext context,
			boolean isRtlSheet )
	{
		this.out = out;
		this.context = context;
		this.isRTLSheet = isRtlSheet;
	}


	public void end( ) throws IOException
	{
		writer.end( );
	}

	public void endRow( )
	{
		writer.endRow( );
	}

	public void endSheet( String oritentation )
	{
		writer.endSheet( oritentation );
	}

	public void outputData( SheetData data ) throws IOException
	{
		writer.outputData( data );
	}

	public void start( IReportContent report, Map<StyleEntry, Integer> styles,
	// TODO: style ranges.
			// List<ExcelRange> styleRanges,
			HashMap<String, BookmarkDef> bookmarkList ) throws IOException
	{
		writer = new ExcelXmlWriter( out, context, isRTLSheet );
		writer.setSheetIndex( sheetIndex );
		// TODO: style ranges.
		// writer.start( report, styles, styleRanges, bookmarkList );
		writer.start( report, styles, bookmarkList );
		copyOutputData( );
	}

	private void copyOutputData( ) throws IOException
	{
		if ( tempWriter != null )
		{
			BufferedReader reader = null;
			try
			{
				reader = new BufferedReader( new FileReader(
						new File( tempFilePath ) ) );
				String line = reader.readLine( );
				XMLWriterXLS xlsWriter = writer.getWriter( );
				while ( line != null )
				{
					xlsWriter.literal( "\n" );
					xlsWriter.literal( line );
					line = reader.readLine( );
				}
			}
			finally
			{
				if ( reader != null )
				{
					reader.close( );
					reader = null;
				}
			}
		}
	}

	public void startRow( double rowHeight )
	{
		writer.startRow( rowHeight );
	}

	public void startSheet( String name ) throws IOException
	{
		if ( writer == null )
		{
			initializeWriterAsTempWriter( );
		}
		writer.startSheet( name );
		sheetIndex++;
	}

	public void startSheet( int[] coordinates, String pageHeader,
			String pageFooter ) throws IOException
	{
		if ( writer == null )
		{
			initializeWriterAsTempWriter( );
		}
		writer.startSheet( coordinates, pageHeader, pageFooter );
		sheetIndex++;
	}

	/**
	 * @throws FileNotFoundException
	 * 
	 */
	private void initializeWriterAsTempWriter( ) throws FileNotFoundException
	{
		tempFilePath = context.getTempFileDir( )
				+ "_BIRTEMITTER_EXCEL_TEMP_FILE"
				+ Thread.currentThread( ).getId( );
		FileOutputStream out = new FileOutputStream( tempFilePath );
		tempWriter = new ExcelXmlWriter( out, context, isRTLSheet );
		writer = tempWriter;
	}

	public void endSheet( )
	{
		writer.endSheet( );
	}

	public void startRow( )
	{
		writer.startRow( );
	}

	public void outputData( int col, int row, int type, Object value )
	{
		writer.outputData( col, row, type, value );
	}

	public String defineName( String cells )
	{
		return writer.defineName( cells );
	}
}
