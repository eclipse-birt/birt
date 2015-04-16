/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Report style doesn't refresh correspondingly in the layout view<p>
 * Steps to reproduce:
 * <ol>
 * <li>Add a data source and data set
 * <li>Add a table with some data itmes
 * <li>Add a report style, change the font setting
 * <li>preview
 * <li>Change the font setting again in the outline view
 * <li>Preview
 * </ol>
 * <p>
 * Expected result:
 * Style change both applied to the layout view and preview 
 * <p>
 * Actual result:
 * After step 5, style change just applied to preview not the layout view. If do any action in the layout view, OK.
 * </p>
 * Test description:
 * <p>
 * Verify that when setting properties on selector styles, event can be received
 * by the selected Elements(table), and the new property value can be retrieved
 * from the table.
 * </p>
 */
public class Regression_79091 extends BaseTestCase
{

	private static final String INPUT = "regression_79091.rptdesign"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	
	public void setUp( ) throws Exception
	{
		removeResource();
		copyResource_INPUT( INPUT, INPUT );
		
	}

	public void tearDown( )
	{
		removeResource( );
	}
	
	public void test_regression_79091( ) throws DesignFileException, SemanticException
	{
		openDesign( INPUT );
		TableHandle table = (TableHandle) designHandle.findElement( "table1" ); //$NON-NLS-1$
		assertEquals(
				"normal", table.getStringProperty( StyleHandle.FONT_STYLE_PROP ) ); //$NON-NLS-1$

		StyleHandle tableSelector = new ElementFactory( design )
				.newStyle( "table" ); //$NON-NLS-1$
		tableSelector.setFontStyle( "italic" ); //$NON-NLS-1$

		designHandle.getStyles( ).add( tableSelector );

		assertEquals(
				"italic", table.getStringProperty( StyleHandle.FONT_STYLE_PROP ) ); //$NON-NLS-1$

		
		// add a test listener on the table

		TestListener listener = new TestListener( );
		table.addListener( listener );

		// change the table selector.

		tableSelector.setFontStyle( "normal" ); //$NON-NLS-1$
		assertEquals(
				"normal", table.getStringProperty( StyleHandle.FONT_STYLE_PROP ) ); //$NON-NLS-1$
		assertTrue( listener.ev instanceof StyleEvent );
		
		// The listener will be called twice.
		assertEquals( 2, TestListener.count );
	}

	private static class TestListener implements Listener
	{

		DesignElementHandle focus = null;
		NotificationEvent ev = null;
		static int count = 0;

		public void elementChanged( DesignElementHandle focus,
				NotificationEvent ev )
		{
			this.focus = focus;
			this.ev = ev;
			count ++;
		}

	}
}
