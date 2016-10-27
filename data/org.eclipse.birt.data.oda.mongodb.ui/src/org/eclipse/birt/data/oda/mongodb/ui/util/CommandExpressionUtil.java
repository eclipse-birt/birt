/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.mongodb.ui.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CommandExpressionUtil
{

	/**
	 * Fetch the string text from the file with the given file name.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getCommandExpressionText( String fileName )
			throws IOException
	{
		return getCommandExpressionText( new File( fileName ) );
	}

	/**
	 * Fetch the string text from the given file.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getCommandExpressionText( File file )
			throws IOException
	{
		BufferedReader reader = new BufferedReader( new FileReader( file ) );
		StringBuffer buffer = new StringBuffer( (int) file.length( ) );
		String line;
		while ( ( line = reader.readLine( ) ) != null )
		{
			buffer.append( line ).append( "\n" ); //$NON-NLS-1$
		}
		reader.close( );

		return buffer.toString( );
	}

	/**
	 * Export the string text to the file with the specified file name.
	 * 
	 * @param fileName
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public static boolean exportToFile( String fileName, String text )
			throws IOException
	{
		boolean success = true;
		BufferedWriter writer = new BufferedWriter( new FileWriter( fileName ) );
		try
		{
			writer.write( text );
			writer.flush( );
			writer.close( );
		}
		catch ( IOException ex )
		{
			success = false;
		}
		return success;
	}

}
