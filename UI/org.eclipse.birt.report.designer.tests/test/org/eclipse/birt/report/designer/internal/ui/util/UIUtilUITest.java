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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.tests.ITestConstants;
import org.eclipse.birt.report.designer.testutil.BirtUITestCase;
import org.eclipse.swt.widgets.Display;

/**
 * UI tests for UIUtil
 */

public class UIUtilUITest extends BirtUITestCase
{

	public void testIsReportEditorActivated( ) throws Exception
	{
		openEditor( );
		assertTrue( UIUtil.isReportEditorActivated( ) );
		closeEditor( );
		assertFalse( UIUtil.isReportEditorActivated( ) );
	}

	/*
	 * Class under test for ReportEditor getActiveReportEditor()
	 */
	public void testGetActiveReportEditor( ) throws Exception
	{
		openEditor( );
		assertTrue( UIUtil.getActiveReportEditor( ) != null );
		assertTrue( UIUtil.getActiveReportEditor( true ) != null );
		assertTrue( UIUtil.getActiveReportEditor( false ) != null );
		closeEditor( );
		assertTrue( UIUtil.getActiveReportEditor( ) == null );
		assertTrue( UIUtil.getActiveReportEditor( true ) == null );
		assertTrue( UIUtil.getActiveReportEditor( false ) == null );
	}

	public void testGetLayoutEditPartViewer( ) throws Exception
	{
		openEditor( );
		assertTrue( UIUtil.getLayoutEditPartViewer( ) != null );
		closeEditor( );
		assertTrue( UIUtil.getLayoutEditPartViewer( ) == null );
	}

	public void testGetDefaultShell( )
	{
		assertEquals( Display.getCurrent( ).getActiveShell( ),
				UIUtil.getDefaultShell( ) );
	}

	public void testGeiDefaultProject( )
	{
		assertNotNull( UIUtil.getDefaultProject( ) );
		assertEquals( ITestConstants.TEST_PROJECT_NAME,
				UIUtil.getDefaultProject( ).getName( ) );
	}

}
