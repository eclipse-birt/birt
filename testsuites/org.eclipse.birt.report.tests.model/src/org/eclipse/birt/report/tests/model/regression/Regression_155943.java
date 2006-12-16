/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SimpleGroupElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

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
    private String filename = "Regression_155943.xml"; //$NON-NLS-1$
    private String libname = "Regression_155943_lib.xml";
    
    public void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		//copyResource_INPUT( filename , filename );
		copyInputToFile ( INPUT_FOLDER + "/" + filename );
		copyInputToFile ( INPUT_FOLDER + "/" + libname );
	}
	
	public void tearDown( )
	{
		removeResource( );
	}

	/**
	 * @throws DesignFileException 
	 * @throws SemanticException 
	 * 
	 */
	public void test_regression_155943( ) throws DesignFileException, SemanticException
	{
        openDesign(filename);
        libraryHandle = designHandle.getLibrary( "lib" ); //$NON-NLS-1$
        DesignElementHandle chart = libraryHandle.findElement( "NewChart" ); //$NON-NLS-1$
		DesignElementHandle rchart = designHandle.getElementFactory( ).newElementFrom( chart, "rchart" ); //$NON-NLS-1$
        assertNotNull(rchart);
		
        List list = new ArrayList();
        list.add( rchart );
        SimpleGroupElementHandle group = new SimpleGroupElementHandle(designHandle, list);
        
        //No local properties for the simple group
        assertFalse(group.hasLocalPropertiesForExtendedElements( ));
		
        //Set local properity
		rchart.setProperty( ReportItemHandle.WIDTH_PROP, "20pt" ); //$NON-NLS-1$
		assertTrue(group.hasLocalPropertiesForExtendedElements( ));
		
		//Clear local property
		group.clearLocalProperties( );
		assertFalse(group.hasLocalPropertiesForExtendedElements( ));
		
		
	}
}
