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

package org.eclipse.birt.report.designer.internal.ui.processor;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.TableOptionDialog;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.window.Window;

/**
 * The processor for gird and tables
 */

public class TableGridProcessor extends AbstractElementProcessor
{

	/**
	 * Constructor
	 * 
	 * Creates a new instance of the table/gird processor
	 * 
	 * @param elementType
	 *            The type of the element to process. It must be one of the
	 *            following types:
	 *            <ul>
	 *            <li>ReportDesignConstants.TABLE_ITEM
	 *            <li>ReportDesignConstants.GRID_ITEM
	 *            </ul>
	 * 
	 * 
	 */
	TableGridProcessor( String elementType )
	{
		super( elementType );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.processor.IElementProcessor#createElement(java.lang.Object)
	 */
	public DesignElementHandle createElement( Object extendedData )
	{
		boolean isTable = ReportDesignConstants.TABLE_ITEM.equals( getElementType( ) );
		TableOptionDialog dialog = new TableOptionDialog( UIUtil.getDefaultShell( ),
				isTable );
		if ( dialog.open( ) == Window.OK
				&& dialog.getResult( ) instanceof Object[] )
		{
			Object[] data = (Object[]) dialog.getResult( );
			DesignElementHandle handle = null;
			if ( isTable )
			{
				// handle = getElementFactory( ).newTableItem( getNewName(
				// extendedData ),
				// data[1],
				// 1,
				// data[0],
				// 1 );
				handle = DesignElementFactory.getInstance( )
						.newTableItem( getNewName( extendedData ),
								( (Integer) data[1] ).intValue( ),
								1,
								( (Integer) data[0] ).intValue( ),
								1 );
				if ( data[2] != null )
				{
					try
					{
						DataSetHandle dataSet = SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.findDataSet( data[2].toString( ) );
						( (ReportItemHandle) handle ).setDataSet( dataSet );
						DataSetColumnBindingsFormHandleProvider provider = new DataSetColumnBindingsFormHandleProvider( );
						provider.setBindingObject( (ReportItemHandle) handle );
						provider.generateAllBindingColumns( );
					}
					catch ( Exception e )
					{
						ExceptionHandler.handle( e );
					}
				}
			}
			else
			{
				// handle = getElementFactory( ).newGridItem( getNewName(
				// extendedData ),
				// data[1],
				// data[0] );
				handle = DesignElementFactory.getInstance( )
						.newGridItem( getNewName( extendedData ),
								( (Integer) data[1] ).intValue( ),
								( (Integer) data[0] ).intValue( ) );
			}
			InsertInLayoutUtil.setInitWidth( handle );
			return handle;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.processor.IElementProcessor#editElement(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public boolean editElement( DesignElementHandle handle )
	{
		// Do nothing
		return false;
	}

}
