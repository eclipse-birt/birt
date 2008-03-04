/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.OutputStream;

public interface IDataExtractionOption extends ITaskOption
{

	public static final String OUTPUT_FORMAT = "Format"; //$NON-NLS-1$

	public static final String OUTPUT_FILE_NAME = "outputFile"; //$NON-NLS-1$

	public static final String OUTPUT_STREAM = "outputStream"; //$NON-NLS-1$

	public static final String EXTENSION = "extension"; //$NON-NLS-1$
	
	/**
	 * Set output format.
	 * 
	 * @param format
	 *            output format.
	 */
	void setOutputFormat( String format );

	/**
	 * Get output format.
	 */
	String getOutputFormat( );
	
	/**
	 * Set extension id.
	 * 
	 * @param extension
	 *            extension id.
	 */
	void setExtension( String extension );

	/**
	 * Get extension.
	 */
	String getExtension( );
	
	/**
	 * Set output stream.
	 * 
	 * @param out
	 *            output stream.
	 */
	void setOutputStream( OutputStream out );

	/**
	 * Get output stream.
	 */
	OutputStream getOutputStream( );
	
	/**
	 * Set output file.
	 * 
	 * @param filename
	 *            name of the output file.
	 */
	void setOutputFile( String filename );
	
	/**
	 * Get output file name.
	 */
	String getOutputFile( );
}
