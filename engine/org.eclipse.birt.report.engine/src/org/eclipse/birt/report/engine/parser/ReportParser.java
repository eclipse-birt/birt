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

package org.eclipse.birt.report.engine.parser;

import java.io.InputStream;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;

/**
 * Report Parser.
 * 
 * used to parse the design file, and get the IR of design.
 * 
 * 
 * @version $Revision: 1.4 $ $Date: 2005/02/23 07:33:53 $
 */
public class ReportParser
{

	/**
	 * logger used to log syntax errors.
	 */
	static protected Log logger = LogFactory.getLog( ReportParser.class );

	/**
	 * constructor.
	 */
	public ReportParser( )
	{
	}

	/**
	 * parse the XML input stream.
	 * 
	 * @param in
	 *            design file
	 * @return created report IR, null if exit any errors.
	 */
	public Report parse( String name, InputStream in ) throws DesignFileException
	{
		// Create new design session
		SessionHandle sessionHandle = DesignEngine.newSession( Locale
				.getDefault( ) );

		// Obtain design handle
		ReportDesignHandle designHandle = sessionHandle.openDesign( name, in );
	
		return parse( designHandle );
	}

	/**
	 * parse the XML input stream.
	 * 
	 * @param design
	 *            DE's IR
	 * @return FPE's IR, null if there is any error.
	 */
	public Report parse( ReportDesignHandle design )
	{
		assert ( design != null );
		assert ( design.getErrorList().isEmpty());

		EngineIRVisitor visitor = new EngineIRVisitor( design );
		return visitor.translate( );
	}
}
