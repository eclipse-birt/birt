
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
package org.eclipse.birt.report.engine.emitter.html;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;

/**
 * 
 */

public class TableLayoutTest extends HTMLReportEmitterTestCase
{

	public String getWorkSpace( )
	{
		// TODO Auto-generated method stub
		return "./tableLayoutTest";
	}
	
	/**
	 * 
	 * @throws EngineException
	 * @throws IOException
	 */
	public void testTableColumnWidth( ) throws EngineException, IOException
	{
		//the default cell to place the group icon is the first cell.
		String designFile = "org/eclipse/birt/report/engine/emitter/html/TableColumnWidth.xml";
		HTMLRenderOption options = new HTMLRenderOption( );
		options.setLayoutPreference( "fixed" );
		
		ByteArrayOutputStream output = new ByteArrayOutputStream( );
		List instanceIDs = new ArrayList( );
		options.setInstanceIDs( instanceIDs );
		options.setOutputStream( output );
		options.setEnableMetadata( true );
		IRenderTask task = createRenderTask( designFile );
		task.setRenderOption( options );
		task.render( );
		task.close( );
		String content = new String( output.toByteArray( ) );
		output.close( );
		
		content = content.replaceAll( "\n", "\"\n\"+\\\\n" );
		String regex = "table-layout:fixed";
		Matcher matcher = Pattern.compile( regex ).matcher( content );
		assertEquals( true, matcher.find( ) );

	}

}
