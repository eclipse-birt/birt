/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.HandleKit;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;

/**
 * This is the resize policy to provide support for Table resize
 *  
 */
public class TableResizeEditPolice extends ReportElementResizePolicy
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart( Request request )
	{
		return null;
	}

	protected void addSelectionHandles( )
	{
		super.addSelectionHandles( );
		if ( getHost( ).getSelected( ) != EditPart.SELECTED_PRIMARY )
		{
			return;
		}
		IFigure layer = getLayer( LayerConstants.HANDLE_LAYER );
		ArrayList list = new ArrayList( );
		HandleKit.addHandles( (TableEditPart) getHost( ), list );
		for ( int i = 0; i < list.size( ); i++ )
			layer.add( (IFigure) list.get( i ) );
		handles.addAll( list );
	}

	protected void removeSelectionHandles( )
	{
		if ( handles == null )
			return;
		IFigure layer = getLayer( LayerConstants.HANDLE_LAYER );
		for ( int i = 0; i < handles.size( ); i++ )
		{
			Object figure = handles.get( i );
			if ( figure instanceof IFigure )
			{
				layer.remove( (IFigure) figure );
			}

		}
		handles = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showFocus()
	 */
	protected void showFocus( )
	{
		//do nothing
	}
}