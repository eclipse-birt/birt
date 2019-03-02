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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.CellDragTracker;
import org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutCell;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;

/**
 * Abstract class for the cell editpart
 */
public abstract class AbstractCellEditPart extends ReportElementEditPart implements
		ITableLayoutCell
{

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public AbstractCellEditPart( Object model )
	{
		super( model );
	}

	/*
	 * Gets the paint layer (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getLayer(java.lang.Object)
	 */
	public IFigure getLayer( Object key )
	{
		//Because the table layer is special,so gets the layer from the table parent
		if ( getParent( ) instanceof AbstractTableEditPart )
		{
			return ( (AbstractTableEditPart) getParent( ) ).getLayer( key );
		}
		return super.getLayer( key );
	}

	/**
	 * Gets the edit part bounds
	 * 
	 * @return the edit part bounds
	 */
	public Rectangle getBounds( )
	{
		//Maybe delete in the future
		return getFigure( ).getBounds( );
	}
		
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request req )
	{
		return new CellDragTracker( this );
	}
	
	@Override
	public Object getAdapter( Class key )
	{
		if (key == IDelaySelectionDragTracker.class)
		{
			return new CellDragTracker( this );
		}
			
		return super.getAdapter( key );
	}
	
	/**
	 * Update the edit part that nor recreate.
	 */
	protected void updateExistPart()
	{
		//do nothing now
	}
	
	protected void setTextAliment(StyleHandle style )
	{
		String hAlign = style.getTextAlign( );
		String vAlign = style.getVerticalAlign( );

		ReportFlowLayout rflayout = (ReportFlowLayout) getFigure( )
				.getLayoutManager( );

		if ( DesignChoiceConstants.TEXT_ALIGN_CENTER.equals( hAlign ) )
		{
			rflayout.setMajorAlignment( ReportFlowLayout.ALIGN_CENTER );
		}
		else if ( DesignChoiceConstants.TEXT_ALIGN_RIGHT.equals( hAlign ) 
				&& !this.getFigure( ).isMirrored( ) ) // bidi_hcg
		{
			rflayout.setMajorAlignment( ReportFlowLayout.ALIGN_RIGHTBOTTOM );
		}
		else if ( DesignChoiceConstants.TEXT_ALIGN_LEFT.equals( hAlign ) 
				&& this.getFigure( ).isMirrored( ) ) // bidi_hcg
		{
			rflayout.setMajorAlignment( ReportFlowLayout.ALIGN_RIGHTBOTTOM );
		}
		else
		{
			rflayout.setMajorAlignment( ReportFlowLayout.ALIGN_LEFTTOP );
		}

		if ( DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE.equals( vAlign ) )
		{
			rflayout.setMinorAlignment( ReportFlowLayout.ALIGN_CENTER );
		}
		else if ( DesignChoiceConstants.VERTICAL_ALIGN_BOTTOM.equals( vAlign ) )
		{
			rflayout.setMinorAlignment( ReportFlowLayout.ALIGN_RIGHTBOTTOM );
		}
		else
		{
			rflayout.setMinorAlignment( ReportFlowLayout.ALIGN_LEFTTOP );
		}
		
		rflayout.layout( getFigure( ) );
	}
}
