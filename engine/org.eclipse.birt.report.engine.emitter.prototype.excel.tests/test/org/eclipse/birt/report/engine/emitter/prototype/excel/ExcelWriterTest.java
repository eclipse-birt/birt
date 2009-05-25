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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.emitter.excel.BookmarkDef;
import org.eclipse.birt.report.engine.emitter.excel.ExcelWriter;
import org.eclipse.birt.report.engine.emitter.excel.SheetData;
import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelContext;


public class ExcelWriterTest extends TestCase
{

	public void testStartSheet( ) throws IOException
	{
		ExcelContext context = new ExcelContext( );
		context.setTempFileDir( "d:/" );
		FileOutputStream out = new FileOutputStream( "d:/testXls.xls");
		ExcelWriter writer = new ExcelWriter( out, context, false );
		writer.start( null, new HashMap<StyleEntry, Integer>( ),
				new HashMap<String, BookmarkDef>( ) );
		writer.startSheet( "dataview" );
		writer.startRow( );
		writer.outputData( 1, 1, SheetData.NUMBER, 1 );
		writer.endRow( );
		writer.endSheet( );
		writer.end( );
	}
}
