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

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions.GuiExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemUI;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * add comment here
 *  
 */
public class ExtendedEditPart extends ReportElementEditPart
{

	private IReportItemUI elementUI;

	/**
	 * @param model
	 */
	public ExtendedEditPart( ExtendedItemHandle model )
	{
		super( model );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#elementChanged(org.eclipse.birt.model.api.DesignElementHandle,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle arg0, NotificationEvent arg1 )
	{
		markDirty( true );
		refresh( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE,
				new ReportComponentEditPolicy( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		getExtendedElementUI( ).updateFigure( getExtendedItemHandle( ),
				getFigure( ) );
		( (AbstractGraphicalEditPart) getParent( ) ).setLayoutConstraint( this,
				getFigure( ),
				getConstraint( ) );
	}

	/**
	 * @return The constraint
	 */
	protected Object getConstraint( )
	{
		ExtendedItemHandle handle = getExtendedItemHandle( );
		ReportItemConstraint constraint = new ReportItemConstraint( );

		String type = handle.getPrivateStyle( ).getDisplay( );
		if ( type == null )
		{
			type = DesignChoiceConstants.DISPLAY_BLOCK;
		}
		constraint.setDisplay( type );
		return constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		return getExtendedElementUI( ).getFigure( getExtendedItemHandle( ) );
	}

	public void performDirectEdit( )
	{
		IReportItemBuilderUI builder = ExtensionPointManager.getInstance( )
		.getExtendedElementPoint( ((ExtendedItemHandle)getModel()).getExtensionName() )
		.getReportItemBuilderUI( );
		if ( builder != null && builder.open( getExtendedItemHandle( ) ) > 0 )
		{
			refreshVisuals( );
		}
	}

	/**
	 * perform edit directly when the request is the corresponding type.
	 */
	public void performRequest( Request request )
	{
		if ( request.getType( ) == RequestConstants.REQ_OPEN )
		{
			performDirectEdit( );
		}
	}

	public IReportItemUI getExtendedElementUI( )
	{
		return elementUI;
	}

	public void setExtendedElementUI( IReportItemUI elementUI )
	{
		this.elementUI = elementUI;
	}

	public ExtendedItemHandle getExtendedItemHandle( )
	{
		return (ExtendedItemHandle) getModel( );
	}

	public boolean canResize( )
	{
		String id = GuiExtensionManager.getExtendedElementID( getExtendedItemHandle( ) );
		Boolean bool = (Boolean) ExtensionPointManager.getInstance( )
				.getExtendedElementPoint( id )
				.getAttribute( IExtensionConstants.EDITOR_CAN_RESIZE );

		return bool.booleanValue( );
	}
	
	public void deactivate( )
	{
		elementUI.disposeFigure( getExtendedItemHandle( ), (IFigure)getFigure( ) );
		super.deactivate( );
	}
}