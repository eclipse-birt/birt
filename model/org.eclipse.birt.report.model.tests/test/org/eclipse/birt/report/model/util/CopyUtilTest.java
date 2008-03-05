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

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.util.CopyUtil;
import org.eclipse.birt.report.model.api.util.IElementCopy;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;

/**
 * Test cases for CopyUtil class.
 * 
 */

public class CopyUtilTest extends BaseTestCase
{

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>Copy/Paste label without extends.
	 * <li>Copy/Paste label and table with extends.
	 * </ul>
	 * 
	 * @throws Exception
	 * 
	 */

	public void testCopyPasteInSameDesign( ) throws Exception
	{
		openDesign( "CopyUtilTest_1.xml" ); //$NON-NLS-1$

		LabelHandle label = (LabelHandle) designHandle
				.findElement( "Body Label" ); //$NON-NLS-1$

		IElementCopy copy = CopyUtil.copy( label );

		// paste once
		CopyUtil.paste( copy, designHandle, IReportDesignModel.BODY_SLOT );

		// paste twice
		CopyUtil.paste( copy, designHandle, IReportDesignModel.BODY_SLOT );

		save( );

		assertTrue( compareFile( "CopyUtilTest_1_golden.xml" ) ); //$NON-NLS-1$

		openDesign( "CopyUtilTest.xml" ); //$NON-NLS-1$
		label = (LabelHandle) designHandle.findElement( "Body Label" ); //$NON-NLS-1$

		copy = CopyUtil.copy( label );

		// paste twice
		CopyUtil.paste( copy, designHandle, IReportDesignModel.BODY_SLOT );
		CopyUtil.paste( copy, designHandle, IReportDesignModel.BODY_SLOT );

		TableHandle table = (TableHandle) designHandle.findElement( "table" ); //$NON-NLS-1$

		copy = CopyUtil.copy( table );

		// paste twice
		CopyUtil.paste( copy, designHandle, IReportDesignModel.BODY_SLOT );
		CopyUtil.paste( copy, designHandle, IReportDesignModel.BODY_SLOT );

		save( );
		assertTrue( compareFile( "CopyUtilTest_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>Copy/Paste label without extends cross design.
	 * <li>Copy/Paste label with extends cross design.
	 * <li>Copy/Paste table without extends cross design.
	 * <li>Copy/Paste table without extends cross design.
	 * </ul>
	 * 
	 * @throws Exception
	 * 
	 */

	public void testCopyPasteCrossDesign( ) throws Exception
	{
		openDesign( "CopyUtilTest_1.xml" ); //$NON-NLS-1$

		ReportDesignHandle design1 = designHandle;

		openDesign( "CopyUtilTest.xml" ); //$NON-NLS-1$

		ReportDesignHandle design = designHandle;

		// copy one label with extends to another design

		LabelHandle label = (LabelHandle) design.findElement( "Body Label" ); //$NON-NLS-1$

		IElementCopy copy = CopyUtil.copy( label );
		CopyUtil.paste( copy, design1, IReportDesignModel.BODY_SLOT );
		CopyUtil.paste( copy, design1, IReportDesignModel.BODY_SLOT );

		TableHandle table = (TableHandle) design.findElement( "table" ); //$NON-NLS-1$
		copy = CopyUtil.copy( table );
		CopyUtil.paste( copy, design1, IReportDesignModel.BODY_SLOT );
		CopyUtil.paste( copy, design1, IReportDesignModel.BODY_SLOT );

		designHandle = design1;
		save( );
		assertTrue( compareFile( "CopyUtilTest_cross_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>the copy item is invalid
	 * </ul>
	 * 
	 * @throws Exception
	 * 
	 */

	public void testInvalidPaste( ) throws Exception
	{
		openDesign( "CopyUtilTest.xml" ); //$NON-NLS-1$

		try
		{
			CopyUtil.paste( null, designHandle, IReportDesignModel.BODY_SLOT );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED,
					e.getErrorCode( ) );
		}

	}
}
