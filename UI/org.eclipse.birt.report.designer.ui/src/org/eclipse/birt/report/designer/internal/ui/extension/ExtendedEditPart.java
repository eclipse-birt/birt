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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.LineBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions.GuiExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.window.Window;

/**
 * add comment here
 * 
 */
public class ExtendedEditPart extends ReportElementEditPart
{

	private IReportItemFigureProvider elementUI;

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
				new ReportComponentEditPolicy( ) {

					public boolean understandsRequest( Request request )
					{
						if ( RequestConstants.REQ_DIRECT_EDIT.equals( request.getType( ) )
								|| RequestConstants.REQ_OPEN.equals( request.getType( ) )
								|| ReportRequest.CREATE_ELEMENT.equals( request.getType( ) ) )
							return true;
						return super.understandsRequest( request );
					}
				} );
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

		refreshBorder( (DesignElementHandle) getModel( ), new LineBorder( ) );

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
		constraint.setMargin( getModelAdapter( ).getMargin( null ) );
		return constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		return getExtendedElementUI( ).createFigure( getExtendedItemHandle( ) );
	}

	public void performDirectEdit( )
	{
		IReportItemBuilderUI builder = ExtensionPointManager.getInstance( )
				.getExtendedElementPoint( ( (ExtendedItemHandle) getModel( ) ).getExtensionName( ) )
				.getReportItemBuilderUI( );

		if ( builder != null )
		{
			// Start a transaction before opening builder

			CommandStack stack = SessionHandleAdapter.getInstance( )
					.getCommandStack( );
			final String transName = Messages.getFormattedString( "ExtendedEditPart.edit", new Object[]{getExtendedItemHandle( ).getExtensionName( )} ); //$NON-NLS-1$

			stack.startTrans( transName );
			int result = Window.CANCEL;
			try
			{
				result = builder.open( getExtendedItemHandle( ) );
			}
			catch ( RuntimeException e )
			{
				ExceptionHandler.handle( e );
				stack.rollback( );
				return;
			}

			if ( result == Window.OK )
			{
				stack.commit( );
				refreshVisuals( );
			}
			else
			{
				stack.rollback( );
			}

		}
	}

	public IReportItemFigureProvider getExtendedElementUI( )
	{
		return elementUI;
	}

	public void setExtendedElementUI( IReportItemFigureProvider elementUI )
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
				.getAttribute( IExtensionConstants.ATTRIBUTE_EDITOR_CAN_RESIZE );

		return bool.booleanValue( );
	}

	public void deactivate( )
	{
		elementUI.disposeFigure( getExtendedItemHandle( ), getFigure( ) );
		super.deactivate( );
	}
}