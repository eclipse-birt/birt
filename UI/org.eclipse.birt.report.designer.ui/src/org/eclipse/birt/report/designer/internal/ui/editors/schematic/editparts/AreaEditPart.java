/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.AreaFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

/**
 * Provides support for area edit part.
 */
public class AreaEditPart extends ReportElementEditPart
{

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public AreaEditPart( Object model )
	{
		super( model );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		AreaFigure figure = new AreaFigure( );
		figure.setLayoutManager( new ReportFlowLayout( ) );

		return figure;
	}	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE, null );
		installEditPolicy( EditPolicy.LAYOUT_ROLE,
				new ReportFlowLayoutEditPolicy( ) );
		installEditPolicy( EditPolicy.CONTAINER_ROLE,
				new ReportContainerEditPolicy( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{
		List list = new ArrayList( );
		insertIteratorToList( ( (SlotHandle) getModel( ) )
				.iterator( ), list );
		return list;
	}

	// TODO:move this code into util class?
	protected void insertIteratorToList( Iterator iterator, List list )
	{
		for ( Iterator it = iterator; it.hasNext( ); )
		{
			list.add( ( it.next( ) ) );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		( (MasterPageEditPart) getParent( ) ).setLayoutConstraint( this,
				figure, getConstraint( ) );
	}

	/**
	 * Get the default constraint of area figure.
	 * 
	 * @return
	 */
	private Rectangle getConstraint( )
	{
		IFigure parent = ( (MasterPageEditPart) getParent( ) ).getFigure( );

		Rectangle region = parent.getClientArea( );
		Rectangle rect = new Rectangle( );

		rect.height = -1;
		rect.width = region.width;

		// Define the default height value of header and footer
		SimpleMasterPageHandle mphandle = ( (SimpleMasterPageHandle) ( (MasterPageEditPart) getParent( ) )
				.getModel( ) );

		if ( ( (SlotHandle) getModel( ) ).getSlotID( ) == SimpleMasterPageHandle.PAGE_HEADER_SLOT )
		{
			if ( mphandle.getPropertyHandle(
					SimpleMasterPageHandle.HEADER_HEIGHT_PROP ).isSet( ) )
			{
				DimensionHandle handle = mphandle.getHeaderHeight( );

				rect.height = (int) DEUtil.convertoToPixel( handle );
			}
		}
		else
		{
			if ( mphandle.getPropertyHandle(
					SimpleMasterPageHandle.FOOTER_HEIGHT_PROP ).isSet( ) )
			{
				DimensionHandle handle = mphandle.getFooterHeight( );

				rect.height = (int) DEUtil.convertoToPixel( handle );
			}
		}

		if ( ( (SlotHandle) getModel( ) ).getSlotID( ) == SimpleMasterPageHandle.PAGE_HEADER_SLOT )
		{
			rect.setLocation( 0, 0 );
		}
		else
		{
			rect.setLocation( -1, -1 );
		}

		return rect;
	}

}