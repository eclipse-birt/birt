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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.GridFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.TableFigure;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.IFigure;

/**
 * Grid EditPart,control the UI & model of grid
 */
public class GridEditPart extends TableEditPart
{

	/**
	 * Constructor
	 * @param obj
	 */
	public GridEditPart( Object obj )
	{
		super( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		TableFigure viewport = new GridFigure( );
		viewport.setOpaque( false );
		innerLayers = new FreeformLayeredPane( );
		createLayers( innerLayers );
		viewport.setContents( innerLayers );
		return viewport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.model.core.DesignElement,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		super.elementChanged( focus, ev );
		switch ( ev.getEventType( ) )
		{
			case NotificationEvent.CONTENT_EVENT :
			{
				markDirty( true );
				if ( focus instanceof GridHandle )
				{
					addListenerToChildren( );
					refreshChildren( );
				}
				break;
			}
			default :
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart#insertRow(int)
	 */
	public void insertRow( int rowNumber )
	{
		super.insertRow( rowNumber );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart#canMerge()
	 */
	public boolean canMerge( )
	{
		return TableUtil.getSelectionCells( this ).size( ) > 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart#addListenerToChildren()
	 */
	protected void addListenerToChildren( )
	{
		addRowListener( );
		addColumnListener( );
	}
}