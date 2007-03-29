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

package org.eclipse.birt.report.model.simpleapi;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.extension.SimpleRowItem;
import org.eclipse.birt.report.model.api.extension.MultiRowItem;
import org.eclipse.birt.report.model.api.simpleapi.ILabel;
import org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.IReportItem;
import org.eclipse.birt.report.model.api.simpleapi.ITable;
import org.eclipse.birt.report.model.util.BaseTestCase;

public class SimpleApiTest extends BaseTestCase
{

	private final static String FILENAME = "DesignFileTest.xml"; //$NON-NLS-1$

	/**
	 * @throws Exception
	 */

	public void testSimpleElement( ) throws Exception
	{
		DesignEngine engine = new DesignEngine( null );

		InputStream ins = getResource( INPUT_FOLDER + FILENAME ).openStream( );
		IReportDesign simpleDesign = engine.openDesign(
				INPUT_FOLDER + FILENAME, ins, null );

		ITable table = (ITable) simpleDesign.getReportElement( "my table" ); //$NON-NLS-1$
		assertNotNull( table );

		ILabel label = (ILabel) simpleDesign.getReportElement( "my label" ); //$NON-NLS-1$
		assertNotNull( label );

		IReportItem matrix = (IReportItem) simpleDesign
				.getReportElement( "testMatrix" ); //$NON-NLS-1$
		assertNotNull( matrix );
		assertTrue( matrix instanceof IMultiRowItem );
		assertTrue( matrix instanceof MultiRowItem );
		assertEquals( "org.eclipse.birt.report.model.tests.matrix.Matrix", //$NON-NLS-1$
				matrix.getClass( ).getName( ) );

		IReportItem box = (IReportItem) simpleDesign
				.getReportElement( "testBox" ); //$NON-NLS-1$
		assertNotNull( box );
		assertFalse( box instanceof IMultiRowItem );
		assertTrue( box instanceof SimpleRowItem );
		assertEquals(
				"org.eclipse.birt.report.model.tests.box.ReportItemImpl$Box", //$NON-NLS-1$
				box.getClass( ).getName( ) );
	}

	/**
	 * gets the url of the resource.
	 * 
	 * @param name
	 *            name of the resource
	 * @return the url of the resource
	 */

	protected URL getResource( String name )
	{
		return this.getClass( ).getResource( name );
	}
}
