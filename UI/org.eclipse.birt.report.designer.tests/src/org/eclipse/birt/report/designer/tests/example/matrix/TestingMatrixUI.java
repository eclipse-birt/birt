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

package org.eclipse.birt.report.designer.tests.example.matrix;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilder;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemPropertyEdit;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemUI;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.extension.ExtendedElementException;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.window.Window;

/**
 *  
 */

public class TestingMatrixUI implements IReportItemUI
{

	public static final String TEST_ELEMENT = "TestingMatrix"; //$NON-NLS-1$
	public static final String TEST_NAME = "Just for test"; //$NON-NLS-1$
	public static final String[] TEST_PROPERTY = {
			"test1", "test2",}; //$NON-NLS-1$ //$NON-NLS-2$ 
	public static final Object[] TEST_ELEMENT_CONTENT = {
			"default test", new Integer( 1024 )}; //$NON-NLS-1$ 
	public static final Object[] TEST_ELEMENT_CONTENT_EDITED = {
			"edit test", new Integer( 1025 )}; //$NON-NLS-1$ 

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IExtendedElementUI#getQuickEditPage()
	 */
	public IReportItemPropertyEdit getPropertyEditPage( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IExtendedElementUI#getBuilder()
	 */
	public IReportItemBuilder getBuilder( )
	{
		return new IReportItemBuilder( ) {

			public int open( ExtendedItemHandle handle )
			{
				if ( handle == null )
				{
					return Window.CANCEL;
				}
				try
				{
					handle.loadExtendedElement( );
				}
				catch ( ExtendedElementException e )
				{
					return Window.CANCEL;
				}
				if ( handle.getProperty( TEST_PROPERTY[1] ) == null )
				{
					System.out.println( "Created OK" ); //$NON-NLS-1$
					try
					{
						handle.setProperty( TEST_PROPERTY[1],
								TEST_ELEMENT_CONTENT[1] );
					}
					catch ( SemanticException e1 )
					{
						return Window.CANCEL;
					}

					return Window.OK;
				}
				try
				{
					int value = ( (Integer) handle.getProperty( TEST_PROPERTY[1] ) ).intValue( ) + 1;
					handle.setProperty( TEST_PROPERTY[0],
							TEST_ELEMENT_CONTENT_EDITED[0] );
					handle.setProperty( TEST_PROPERTY[1], new Integer( value ) );
				}
				catch ( SemanticException e1 )
				{
					return Window.CANCEL;
				}

				System.out.println( "Edit OK" ); //$NON-NLS-1$
				return Window.OK;

			}

		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemUI#getFigure(org.eclipse.birt.report.model.api.ReportItemHandle)
	 */
	public IFigure getFigure( ExtendedItemHandle handle )
	{
		IFigure figure = new LabelFigure( );
		if ( handle != null )
		{
			( (LabelFigure) figure ).setText( handle.getProperty( TEST_PROPERTY[0] )
					+ ":"
					+ handle.getProperty( TEST_PROPERTY[1] ).toString( ) );
		}
		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemUI#updateFigure(org.eclipse.birt.report.model.api.ReportItemHandle,
	 *      org.eclipse.draw2d.IFigure)
	 */
	public void updateFigure( ExtendedItemHandle handle, IFigure figure )
	{
		Assert.isNotNull( handle );
		( (LabelFigure) figure ).setText( handle.getProperty( TEST_PROPERTY[0] )
				+ ":"
				+ handle.getProperty( TEST_PROPERTY[1] ).toString( ) );
	}

}