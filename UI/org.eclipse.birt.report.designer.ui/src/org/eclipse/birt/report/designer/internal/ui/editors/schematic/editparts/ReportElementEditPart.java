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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ReportDesignHandleAdapter;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.notification.DeferredRefreshManager;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.IReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.AbstractGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.core.Listener;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.DimensionValue;
import org.eclipse.birt.report.model.util.ColorUtil;
import org.eclipse.birt.report.model.util.DimensionUtil;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * <p>
 * Abstract super class for all report element editPart
 * </p>
 */
public abstract class ReportElementEditPart extends AbstractGraphicalEditPart
		implements
			Listener,
			IModelAdapterHelper
{

	private static final int DELAY_TIME = 1600;
	private DesignElementHandleAdapter peer;
	private AbstractHandle guideHandle = null;
	private boolean canDeleteGuide = true;

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public ReportElementEditPart( Object model )
	{
		super( );
		setModel( model );
		getFigure( ).addMouseMotionListener( new MouseMotionListener.Stub( )
		{

			public void mouseEntered( MouseEvent me )
			{
				//System.out.println("enter");
				addGuideFeedBack( );

			}

			public void mouseExited( MouseEvent me )
			{
				//System.out.println("exit");
				delayRemoveGuideFeedBack( );
			}

			public void mouseHover( MouseEvent me )
			{
				//System.out.println("hover");
				addGuideFeedBack( );

			}

			public void mouseMoved( MouseEvent me )
			{
				//System.out.println("move");
				//addGuideFeedBack();
			}

		} );
	}

	/**Creates the guide handle, default get from parent.
	 * @return
	 */
	protected AbstractGuideHandle createGuideHandle( )
	{
		EditPart part = getParent( );
		if ( part instanceof ReportElementEditPart )
		{
			return ( (ReportElementEditPart) part ).createGuideHandle( );
		}
		return null;
	}

	/**Adds the guide handle to the handle layer.
	 * 
	 */
	public void addGuideFeedBack( )
	{
		canDeleteGuide = false;
		if ( guideHandle == null )
		{
			guideHandle = createGuideHandle( );
		}
		if ( guideHandle != null && guideHandle.getParent( ) == null )
		{
			clearGuideHandle( );	
			getHandleLayer( ).add( guideHandle );
		}
	}

	private void clearGuideHandle( )
	{
		IFigure layer = getHandleLayer( );
		List list = layer.getChildren( );
		List temp = new ArrayList( );
		int size = list.size( );

		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof AbstractGuideHandle )
			{
				temp.add( obj );
			}
		}

		size = temp.size( );
		for ( int i = 0; i < size; i++ )
		{
			IFigure figure = (IFigure) temp.get( i );
			layer.remove( figure );
		}
	}

	/**
	 *Removes the guide handle. 
	 */
	public void removeGuideFeedBack( )
	{
		if ( guideHandle != null
				&& guideHandle.getParent( ) == getHandleLayer( ) )
		{
			getHandleLayer( ).remove( guideHandle );
		}
		guideHandle = null;
	}

	
	/**
	 * Removes the guide handle after the specified
	 * number of milliseconds.
	 */
	public void delayRemoveGuideFeedBack( )
	{
		canDeleteGuide = true;
		Display.getCurrent( ).timerExec( DELAY_TIME, new Runnable( )
		{

			public void run( )
			{
				if (canDeleteGuide)
				{
					removeGuideFeedBack( );
				}
			}

		} );
	}

	private IFigure getHandleLayer( )
	{
		LayerManager manager = (LayerManager) getViewer( )
				.getEditPartRegistry( ).get( LayerManager.ID );
		return manager.getLayer( LayerConstants.HANDLE_LAYER );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate( )
	{
		if ( isActive( ) )
			return;
		addModelListener( );
		super.activate( );
	}

	/**
	 * adds model listener
	 */
	private void addModelListener( )
	{
		if ( getModel( ) instanceof DesignElementHandle )
		{
			( (DesignElementHandle) getModel( ) ).addListener( this );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate( )
	{
		if ( !isActive( ) )
			return;

		removeGuideFeedBack( );

		removeModelLister( );
		super.deactivate( );
		HandleAdapterFactory.getInstance( ).remove( getModel( ) );
	}

	/**
	 * Removes the model listener.
	 */
	private void removeModelLister( )
	{
		if ( getModel( ) instanceof DesignElementHandle )
		{
			( (DesignElementHandle) getModel( ) ).removeListener( this );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.core.Listener#notify(org.eclipse.birt.model.core.DesignElement,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public abstract void elementChanged( DesignElementHandle arg0,
			NotificationEvent arg1 );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected abstract void createEditPolicies( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{
		return super.getModelChildren( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request req )
	{
		return super.getDragTracker( req );
	}

	/**
	 * @return bounds
	 */
	public Rectangle getBounds( )
	{
		return getReportElementHandleAdapt( ).getbounds( );
	}

	/**
	 * Sets bounds
	 * 
	 * @param r
	 */
	public void setBounds( Rectangle r )
	{
		try
		{
			getReportElementHandleAdapt( ).setBounds( r );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Gets location
	 * 
	 * @return
	 */
	public Point getLocation( )
	{
		return getReportElementHandleAdapt( ).getLocation( );
	}

	/**
	 * Sets location
	 * 
	 * @param p
	 */
	public void setLocation( Point p )
	{
		try
		{
			getReportElementHandleAdapt( ).setLocation( p );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * @return size
	 */
	public Dimension getSize( )
	{
		return getReportElementHandleAdapt( ).getSize( );
	}

	/**
	 * Sets size
	 * 
	 * @param d
	 */
	public void setSize( Dimension d )
	{
		try
		{
			getReportElementHandleAdapt( ).setSize( d );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Get the current font family.
	 * 
	 * @return The current font family
	 */
	protected Font getFont( )
	{
		StyleHandle styleHandle = ( (ReportItemHandle) getModel( ) )
				.getPrivateStyle( );

		String family = (String) ( styleHandle.getFontFamilyHandle( )
				.getValue( ) );
		String FontFamily = (String) DesignerConstants.familyMap.get( family );

		if ( FontFamily == null )
		{
			FontFamily = family;
		}

		ReportItemHandle handle = (ReportItemHandle) getModel( );

		int fontSize = 0;
		Object size = handle.getPrivateStyle( ).getFontSize( ).getValue( );
		if ( size instanceof DimensionValue )
		{
			fontSize = (int) DimensionUtil.convertTo( (DimensionValue) size,
					DesignChoiceConstants.UNITS_PT,
					DesignChoiceConstants.UNITS_PT ).getMeasure( );
		}
		else
		{
			fontSize = Integer.valueOf(
					(String) DesignerConstants.fontMap.get( DEUtil
							.getFontSize( handle ) ) ).intValue( );
		}

		int fontStyle = 0;
		String fontWeight = styleHandle.getFontWeight( );
		String style = styleHandle.getFontStyle( );

		//Eclipse does not distinct ITALIC and OBLIQUE, so we treat OBLIQUE as
		// ITATIC. And if font weight >= 700, deal with BOLD.
		if ( fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_BOLD )
				|| fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_BOLDER )
				|| fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_700 )
				|| fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_800 )
				|| fontWeight.equals( DesignChoiceConstants.FONT_WEIGHT_900 ) )
		{
			fontStyle = fontStyle | SWT.BOLD;
		}

		if ( style.equals( DesignChoiceConstants.FONT_STYLE_ITALIC )
				|| style.equals( DesignChoiceConstants.FONT_STYLE_OBLIQUE ) )
		{
			fontStyle = fontStyle | SWT.ITALIC;
		}

		Font font = FontManager.getFont( FontFamily, fontSize, fontStyle );

		return font;
	}

	/**
	 * @return display label
	 */
	public String getDisplayLabel( )
	{
		return null;
	}

	private boolean isDirty = true;

	protected final void refreshVisuals( )
	{
		getRefreshManager( ).markEditPartForRefreshVisuals( this );
	}

	protected void refreshChildren( )
	{
		getRefreshManager( ).markEditPartForRefreshChildren( this );
	}

	public final void refreshChildrenFigures( )
	{
		super.refreshChildren( );
	}

	public abstract void refreshFigure( );

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 *  
	 */
	public void refreshBackground( DesignElementHandle handle )
	{
		refreshBackgroundColor( handle );
		refreshBackgroundImage( handle );
	}

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 *  
	 */
	public void refreshBackgroundColor( DesignElementHandle handle )
	{
		Object obj = handle.getProperty( Style.BACKGROUND_COLOR_PROP );

		if ( handle instanceof MasterPageHandle )
		{
			getFigure( ).setOpaque( true );
		}
		else
		{
			getFigure( ).setOpaque( false );
		}

		if ( obj != null )
		{
			int color = 0xFFFFFF;
			if ( obj instanceof String )
			{
				color = ColorUtil.parseColor( (String) obj );
			}
			else
			{
				color = ( (Integer) obj ).intValue( );
			}
			getFigure( ).setBackgroundColor( ColorManager.getColor( color ) );
			getFigure( ).setOpaque( true );
		}
	}

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 *  
	 */
	public void refreshBackgroundImage( DesignElementHandle handle )
	{
		IReportElementFigure figure = (IReportElementFigure) getFigure( );

		String backGroundImage = getBackgroundImage( handle );
		Object backGroundPosition = getBackgroundPosition( handle );
		int backGroundRepeat = getBackgroundRepeat( handle );

		if ( backGroundImage == null )
		{
			figure.setImage( null );
		}
		else
		{
			Image image = null;
			try
			{
				image = ImageManager.getImage( backGroundImage );
			}
			catch ( SWTException e )
			{
				//Should not be ExceptionHandler.handle(e), see SCR#73730
				image = null;
			}

			if ( image == null )
			{
				figure.setImage( null );
				return;
			}

			figure.setImage( image );

			figure.setRepeat( backGroundRepeat );

			if ( backGroundPosition instanceof int[] )
			{
				//left, center, right, top, bottom
				figure.setAlignment( ( (int[]) backGroundPosition )[0]
						| ( (int[]) backGroundPosition )[1] );
				figure.setPosition( new Point( -1, -1 ) );
			}
			else if ( backGroundPosition instanceof Point )
			{
				//{1cm, 1cm}
				figure.setPosition( (Point) backGroundPosition );
			}
			else if ( backGroundPosition instanceof DimensionValue[] )
			{
				//{0%, 0%}
				int percentX = (int) ( (DimensionValue[]) backGroundPosition )[0]
						.getMeasure( );
				int percentY = (int) ( (DimensionValue[]) backGroundPosition )[1]
						.getMeasure( );
				Rectangle area = getFigure( ).getClientArea( );
				org.eclipse.swt.graphics.Rectangle imageArea = ImageManager
						.getImage( backGroundImage ).getBounds( );
				int x = ( area.width - imageArea.width ) * percentX / 100;
				int y = ( area.height - imageArea.height ) * percentY / 100;

				figure.setPosition( new Point( x, y ) );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.IModelAdaptHelper#markDirty(boolean)
	 */
	public void markDirty( boolean bool )
	{
		markDirty( bool, true );
	}

	/**
	 * Marks edit part dirty
	 * 
	 * @param bool
	 * @param notifyParent
	 */
	public void markDirty( boolean bool, boolean notifyParent )
	{
		this.isDirty = bool;
		if ( notifyParent )
		{
			notifyChildrenDirty( bool );
		}
		if ( bool )
		{
			List list = getChildren( );
			int size = list.size( );
			for ( int i = 0; i < size; i++ )
			{
				( (ReportElementEditPart) list.get( i ) ).markDirty( bool,
						notifyParent );
			}
		}
	}

	protected void notifyChildrenDirty( boolean bool )
	{
		Object parent = getParent( );
		if ( parent != null && parent instanceof ReportElementEditPart )
		{
			( (ReportElementEditPart) parent ).notifyChildrenDirty( bool );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.IModelAdaptHelper#isDirty()
	 */
	public boolean isDirty( )
	{
		return isDirty;
	}

	DesignElementHandleAdapter getModelAdapter( )
	{
		if ( peer == null )
		{
			peer = HandleAdapterFactory.getInstance( )
					.getDesignElementHandleAdapter( getModel( ), this );
		}
		return peer;
	}

	protected DeferredRefreshManager getRefreshManager( )
	{
		EditPart part = getParent( );

		while ( !( part instanceof ReportRootEditPart ) )
		{
			part = part.getParent( );
		}
		if ( part instanceof ReportRootEditPart )
		{
			return ( (ReportRootEditPart) part ).getRefreshManager( );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.IModelAdaptHelper#getPreferredSize()
	 */
	public Dimension getPreferredSize( )
	{
		Dimension size = getFigure( ).getSize( ).getCopy( );
		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.IModelAdaptHelper#getInsets()
	 */
	public Insets getInsets( )
	{
		return new Insets( getFigure( ).getInsets( ) );
	}

	protected ReportItemtHandleAdapter getReportElementHandleAdapt( )
	{
		return (ReportItemtHandleAdapter) getModelAdapter( );
	}

	protected void refreshBorder( DesignElementHandle handle, BaseBorder border )
	{
		border.bottom_color = handle.getPropertyHandle(
				Style.BORDER_BOTTOM_COLOR_PROP ).getStringValue( );
		border.bottom_style = handle.getPropertyHandle(
				Style.BORDER_BOTTOM_STYLE_PROP ).getStringValue( );
		border.bottom_width = handle.getPropertyHandle(
				Style.BORDER_BOTTOM_WIDTH_PROP ).getStringValue( );

		border.top_color = handle.getPropertyHandle(
				Style.BORDER_TOP_COLOR_PROP ).getStringValue( );
		border.top_style = handle.getPropertyHandle(
				Style.BORDER_TOP_STYLE_PROP ).getStringValue( );
		border.top_width = handle.getPropertyHandle(
				Style.BORDER_TOP_WIDTH_PROP ).getStringValue( );

		border.left_color = handle.getPropertyHandle(
				Style.BORDER_LEFT_COLOR_PROP ).getStringValue( );
		border.left_style = handle.getPropertyHandle(
				Style.BORDER_LEFT_STYLE_PROP ).getStringValue( );
		border.left_width = handle.getPropertyHandle(
				Style.BORDER_LEFT_WIDTH_PROP ).getStringValue( );

		border.right_color = handle.getPropertyHandle(
				Style.BORDER_RIGHT_COLOR_PROP ).getStringValue( );
		border.right_style = handle.getPropertyHandle(
				Style.BORDER_RIGHT_STYLE_PROP ).getStringValue( );
		border.right_width = handle.getPropertyHandle(
				Style.BORDER_RIGHT_WIDTH_PROP ).getStringValue( );

		getFigure( ).setBorder( border );
	}

	protected Insets getMasterPageInsets( DesignElementHandle handle )
	{
		return ( (ReportDesignHandleAdapter) getModelAdapter( ) )
				.getMasterPageInsets( handle );
	}

	protected Dimension getMasterPageSize( DesignElementHandle handle )
	{
		return ( (ReportDesignHandleAdapter) getModelAdapter( ) )
				.getMasterPageSize( handle );
	}

	protected int getForegroundColor( DesignElementHandle handle )
	{
		return getModelAdapter( ).getForegroundColor( handle );
	}

	protected int getBackgroundColor( DesignElementHandle handle )
	{
		return getModelAdapter( ).getBackgroundColor( handle );
	}

	protected String getBackgroundImage( DesignElementHandle handle )
	{
		return getModelAdapter( ).getBackgroundImage( handle );
	}

	protected Object getBackgroundPosition( DesignElementHandle handle )
	{
		return getModelAdapter( ).getBackgroundPosition( handle );
	}

	protected int getBackgroundRepeat( DesignElementHandle handle )
	{
		return getModelAdapter( ).getBackgroundRepeat( handle );
	}
}