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

package org.eclipse.birt.report.designer.testutil;

import junit.framework.TestCase;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Base class of unit tests
 * 
 *  
 */

public abstract class BaseTestCase extends TestCase
{

	private ReportDesignHandle report;

	/**
	 * Default constructor
	 */
	public BaseTestCase( )
	{//Do nothing
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public BaseTestCase( String name )
	{
		super( name );
	}

	protected void setUp( ) throws Exception
	{
		SessionHandleAdapter.getInstance( ).init( "test.iard",
				BaseTestCase.class.getResourceAsStream( "test.iard" ) );
		report = SessionHandleAdapter.getInstance( ).getReportDesignHandle( );
	}

	protected void tearDown( ) throws Exception
	{
		report = null;
	}

	/**
	 * Gets the report design for tests
	 * 
	 * @return the report design for tests
	 */
	protected ReportDesign getReportDesign( )
	{
		return report.getDesign( );
	}

	protected ReportDesignHandle getReportDesignHandle( )
	{
		return report;
	}

}