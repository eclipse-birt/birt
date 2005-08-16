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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * Top-level handler for the XML design file. Recognizes the top-level tags in
 * the file.
 * 
 */

public class DesignParserHandler extends ModuleParserHandler
{

	/**
	 * Constructs the design parser handler with the design session.
	 * 
	 * @param theSession
	 *            the design session that is to own the design
	 */

	public DesignParserHandler( DesignSession theSession )
	{
		super(theSession);
		module = new ReportDesign( session );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLParserHandler#createStartState()
	 */

	public AbstractParseState createStartState( )
	{
		return new StartState( );
	}

	/**
	 * Recognizes the top-level tags: Report.
	 */

	class StartState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.REPORT_TAG ) )
				return new ReportState( DesignParserHandler.this );
			return super.startElement( tagName );
		}
	}


}