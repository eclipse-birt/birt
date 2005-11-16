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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ImageHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.LineBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ImageFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.ImageBuilder;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

/**
 * <p>
 * Image edit part
 * </p>
 * 
 */
public class ImageEditPart extends ReportElementEditPart
{

	private static final String IMG_TRANS_MSG = Messages.getString( "ImageEditPart.trans.editImage" ); //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public ImageEditPart( Object model )
	{
		super( model );
	}

	/**
	 * @return Returns the handle.
	 */
	public ImageHandleAdapter getImageAdapter( )
	{
		return (ImageHandleAdapter) getModelAdapter( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		return new ImageFigure( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE,
				new ReportComponentEditPolicy( ) {

					public boolean understandsRequest( Request request )
					{
						if ( RequestConstants.REQ_DIRECT_EDIT.equals( request.getType( ) )
								|| RequestConstants.REQ_OPEN.equals( request.getType( ) ) )
							return true;
						return super.understandsRequest( request );
					}
				} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#notify(org.eclipse.birt.model.core.DesignElement,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle arg0, NotificationEvent arg1 )
	{
		markDirty( true );
		this.refreshVisuals( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		refreshBorder( (DesignElementHandle) getModel( ), new LineBorder( ) );

		Insets pist = getImageAdapter( ).getPadding( getFigure( ).getInsets( ) );

		( (LineBorder) ( getFigure( ).getBorder( ) ) ).setPaddingInsets( pist );

		Image image = null;
		try
		{
			image = getImageAdapter( ).getImage( );
		}
		catch ( SWTException e )
		{
			// Do nothing
		}

		( (ImageFigure) this.getFigure( ) ).setStretched( image != null );
		if ( image == null )
		{
			image = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_MISSING_IMG );
		}

		( (ImageFigure) this.getFigure( ) ).setImage( image );

		if ( getImageAdapter( ).getSize( ) != null )
		{
			this.getFigure( ).setSize( getImageAdapter( ).getSize( ) );
		}
		else if ( image != null )
		{
			Dimension rawSize = getImageAdapter( ).getRawSize( );

			if ( rawSize.height == 0 && rawSize.width == 0 )
			{
				this.getFigure( )
						.setSize( new Dimension( image.getBounds( ).width,
								image.getBounds( ).height ) );
			}
			else if ( rawSize.height == 0 )
			{
				this.getFigure( ).setSize( new Dimension( rawSize.width,
						image.getBounds( ).height ) );
			}
			else
			{
				this.getFigure( )
						.setSize( new Dimension( image.getBounds( ).width,
								rawSize.height ) );
			}

		}

		refreshBackgroundColor( (DesignElementHandle) getModel( ) );

		refreshMargin( );

		( (AbstractGraphicalEditPart) getParent( ) ).setLayoutConstraint( this,
				getFigure( ),
				getConstraint( ) );
	}

	/**
	 * @return The constraint
	 */
	protected Object getConstraint( )
	{
		ReportItemHandle handle = (ReportItemHandle) getModel( );
		ReportItemConstraint constraint = new ReportItemConstraint( );

		constraint.setDisplay( handle.getPrivateStyle( ).getDisplay( ) );
		return constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest( Request request )
	{
		if ( request.getType( ) == RequestConstants.REQ_OPEN )
		{
			performDirectEdit( );
		}

	}

	/**
	 * 
	 */
	private void performDirectEdit( )
	{
		ImageBuilder dialog = new ImageBuilder( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ), ImageBuilder.DLG_TITLE_EDIT );
		dialog.setInput( getModel( ) );
		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getCommandStack( );
		stack.startTrans( IMG_TRANS_MSG );
		if ( dialog.open( ) == Window.OK )
		{
			stack.commit( );
		}
		else
		{
			stack.rollback( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#markDirty(boolean,
	 *      boolean)
	 */
	public void markDirty( boolean bool, boolean notifyParent )
	{
		super.markDirty( bool, notifyParent );
		if ( bool )
		{
			refreshVisuals( );
		}
	}
}