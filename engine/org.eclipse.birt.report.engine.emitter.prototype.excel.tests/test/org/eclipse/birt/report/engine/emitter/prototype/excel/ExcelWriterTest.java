/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.prototype.excel;

import java.io.File;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.excel.BookmarkDef;
import org.eclipse.birt.report.engine.emitter.excel.ExcelWriter;
import org.eclipse.birt.report.engine.emitter.excel.SheetData;
import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelContext;


public class ExcelWriterTest extends TestCase
{
	final static String WORKSPACE_DIR = "./excelReport"; //$NON-NLS-1$

	@SuppressWarnings("nls")
	public void testStartSheet( )
	{
		EXCELRenderOption renderOption = new EXCELRenderOption( );
		try
		{
			new File( WORKSPACE_DIR ).mkdir( );
			renderOption.setOutputFileName( WORKSPACE_DIR + "/report.xls" ); //$NON-NLS-1$
			EngineEmitterServices services = new EngineEmitterServices( null,
					renderOption, null );

			ExcelContext context = new ExcelContext( );
			context.initialize( services );
			ExcelWriter writer = new ExcelWriter( context );
			writer.start( null, new HashMap<StyleEntry, Integer>( ),
					new HashMap<String, BookmarkDef>( ) );
			writer.startSheet( "dataview" );
			writer.startRow( );
			writer.outputData( 1, 1, SheetData.NUMBER, 1 );
			writer.endRow( );
			writer.endSheet( );
			writer.end( );

		}
		catch ( Exception ex )
		{
			fail( "EXCEPTION not expected" + ex.toString( ) );
		}
	}
}
