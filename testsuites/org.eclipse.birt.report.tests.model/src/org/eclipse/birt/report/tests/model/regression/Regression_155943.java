/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SimpleGroupElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Bug Description:</b>
 * <p>
 * "Restore Properties" is always enabled for extended chart
 * <p>
 * <b>Steps to reproduce:</b>
 * <ol>
 * <li>New a report, includes a library, extends lib.chart
 * <li>Choose the chart
 * </ol>
 * <b>Expected result:</b>
 * <p>
 * "Restore Properties" is disabled untill set/change any local properties for
 * the chart
 * <p>
 * <b>Actual result:</b>
 * <p>
 * "Restore Properties" is always enabled
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Follow the steps in bug description, chart can't restore properties till
 * set/change any lccal properties
 */
public class Regression_155943 extends BaseTestCase
{

	private String INPUT = "Regression_155943.xml"; //$NON-NLS-1$
	private String LIBRARY = "Regression_155943_lib.xml";

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );

		// copy the files to input folder
		copyInputToFile( INPUT_FOLDER + "/" + INPUT );
		copyInputToFile( INPUT_FOLDER + "/" + LIBRARY );
	}

	public void tearDown( )
	{
		removeResource( );
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException 
	 */
	public void test_regression_155943( ) throws DesignFileException, SemanticException, IOException
	{
		String report = getTempFolder( ) + "/" + INPUT_FOLDER + "/" + INPUT;
		String libA = getTempFolder( ) + "/" + INPUT_FOLDER + "/" + LIBRARY;

		sessionHandle = new DesignEngine( new DesignConfig( ) ).newSessionHandle( ULocale.ENGLISH );
		designHandle = sessionHandle.openDesign( report );

		//DesignElementHandle chart = designHandle.getComponents( ).get( 0 );
		ExtendedItemHandle chart = (ExtendedItemHandle) designHandle.findElement( "NewChart" );
		assertNotNull( chart );
		assertEquals( "NewChart", chart.getName( ) );
		// assertNotNull(chart);

		// ElementFactory factory = designHandle.getElementFactory( );
		// DesignElementHandle rchart = (DesignElementHandle)
		// factory.newElementFrom( chart, "CHART2" ); //$NON-NLS-1$
		// assertNotNull( rchart );

		List list = new ArrayList( );
		list.add( chart );
		SimpleGroupElementHandle group = new SimpleGroupElementHandle( designHandle, list );

		// No local properties for the simple group
		assertFalse( group.hasLocalPropertiesForExtendedElements( ) );

		// Set local properity
		chart.setProperty( ReportItemHandle.WIDTH_PROP, "212pt" ); //$NON-NLS-1$
		assertEquals("212pt", chart.getProperty( ReportItemHandle.WIDTH_PROP ).toString( ));
		assertTrue( group.hasLocalPropertiesForExtendedElements( ) );

		// Clear local property
		group.clearLocalProperties( );
		assertFalse( group.hasLocalPropertiesForExtendedElements( ) );

	}
}
