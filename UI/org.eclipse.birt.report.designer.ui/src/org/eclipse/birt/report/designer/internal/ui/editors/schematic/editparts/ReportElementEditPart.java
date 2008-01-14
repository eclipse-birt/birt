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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ReportDesignHandleAdapter;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportElementResizePolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.IReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.AbstractGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
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
public abstract class ReportElementEditPart extends AbstractGraphicalEditPart implements
		IModelAdapterHelper
{

	private static final int DELAY_TIME = 1600;
	private DesignElementHandleAdapter peer;
	private AbstractGuideHandle guideHandle = null;
	private boolean isEdited = false;
	protected Logger logger = Logger.getLogger( ReportElementEditPart.class.getName( ) );

	// private static boolean canDeleteGuide = true;

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public ReportElementEditPart( Object model )
	{
		super( );
		if ( Policy.TRACING_EDITPART_CREATE )
		{
			String[] result = this.getClass( ).getName( ).split( "\\." ); //$NON-NLS-1$
			System.out.println( result[result.length - 1] + " >> Created for " //$NON-NLS-1$
					+ model );
		}
		setModel( model );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#setModel(java.lang.Object)
	 */
	public void setModel( Object model )
	{
		super.setModel( model );
		peer = creatDesignElementHandleAdapter( );
	}

	/**
	 * @return
	 */
	public DesignElementHandleAdapter creatDesignElementHandleAdapter( )
	{
		HandleAdapterFactory.getInstance( ).remove( getModel( ) );
		return HandleAdapterFactory.getInstance( )
				.getDesignElementHandleAdapter( getModel( ), this );
	}

	/**
	 * perform edit directly when the request is the corresponding type.
	 */
	public void performRequest( Request request )
	{
		if ( RequestConstants.REQ_OPEN.equals( request.getType( ) )
				|| ReportRequest.CREATE_ELEMENT.equals( request.getType( ) ) )
		{
			if ( isEdited( ) )
			{
				return;
			}
			setEdited( true );
			try
			{
				performDirectEdit( );
			}
			catch ( RuntimeException e )
			{
				// TODO Auto-generated catch block
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}
			setEdited( false );
		}
	}

	public void performDirectEdit( )
	{
		// do nothing
	}

	protected boolean isEdited( )
	{
		return isEdited;
	}

	protected void setEdited( boolean isEdited )
	{
		this.isEdited = isEdited;
	}

	/**
	 * Creates the guide handle, default get from parent.
	 * 
	 * @return
	 */
	protected AbstractGuideHandle createGuideHandle( )
	{
		EditPart part = getParent( );
		if ( part instanceof ReportElementEditPart )
		{
			return ( (ReportElementEditPart) part ).getGuideHandle( );
		}
		return null;
	}

	protected AbstractGuideHandle getGuideHandle( )
	{
		if ( guideHandle == null )
		{
			guideHandle = interCreateGuideHandle( );
		}
		return guideHandle;
	}

	private AbstractGuideHandle interCreateGuideHandle()
	{
		if (getParent( ) instanceof MultipleEditPart)
		{
			return ((MultipleEditPart)getParent( )).createGuideHandle( );
		}
		return createGuideHandle();
	}
	
	/**
	 * Adds the guide handle to the handle layer.
	 * 
	 */
	public void addGuideFeedBack( )
	{
		if ( guideHandle == null )
		{
			guideHandle = interCreateGuideHandle( );
		}

		if ( guideHandle != null && guideHandle != findHandle( ) )
		{
			clearGuideHandle( );
			getHandleLayer( ).add( guideHandle );
			guideHandle.invalidate( );
			guideHandle.setCanDeleteGuide( true );
		}
		else if ( guideHandle != null && guideHandle == findHandle( ) )
		{
			guideHandle.setCanDeleteGuide( false );
		}
		else if ( guideHandle != null )
		{
			guideHandle.setCanDeleteGuide( true );
		}
	}

	private AbstractGuideHandle findHandle( )
	{
		IFigure layer = getHandleLayer( );
		List list = layer.getChildren( );
		int size = list.size( );

		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof AbstractGuideHandle )
			{
				return (AbstractGuideHandle) obj;
			}
		}

		return null;
	}

	protected void clearGuideHandle( )
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
	 * Removes the guide handle.
	 */
	protected void removeGuideFeedBack( )
	{
		if ( guideHandle != null
				&& guideHandle.getParent( ) == getHandleLayer( ) )
		{
			getHandleLayer( ).remove( guideHandle );
		}
		guideHandle = null;
	}

	/**
	 * Removes the guide handle after the specified number of milliseconds.
	 */
	public void delayRemoveGuideFeedBack( )
	{
		if ( guideHandle != null )
		{
			guideHandle.setCanDeleteGuide( true );
		}
		Display.getCurrent( ).timerExec( DELAY_TIME, new Runnable( ) {

			public void run( )
			{
				if ( guideHandle != null && guideHandle.isCanDeleteGuide( ) )
				{
					removeGuideFeedBack( );
				}
			}

		} );
	}

	private IFigure getHandleLayer( )
	{
		super.getLayer( LayerConstants.HANDLE_LAYER );
		LayerManager manager = (LayerManager) getViewer( ).getEditPartRegistry( )
				.get( LayerManager.ID );
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

		super.activate( );

		refreshPageClip( );

		getFigure( ).addMouseMotionListener( new MouseMotionListener.Stub( ) {

			public void mouseEntered( MouseEvent me )
			{
				addGuideFeedBack( );

			}

			public void mouseExited( MouseEvent me )
			{
				delayRemoveGuideFeedBack( );
			}

			public void mouseHover( MouseEvent me )
			{
				addGuideFeedBack( );
			}

			public void mouseMoved( MouseEvent me )
			{
				addGuideFeedBack( );
			}

		} );

		getFigure( ).setFocusTraversable( true );
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

		super.deactivate( );

		HandleAdapterFactory.getInstance( ).remove( getModel( ), this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected abstract void createEditPolicies( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request req )
	{
		DragEditPartsTracker track = new DragEditPartsTracker( this ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.tools.SelectEditPartTracker#handleButtonDown(int)
			 */
			protected boolean handleButtonDown( int button )
			{
				if ( getCurrentViewer( ) instanceof DeferredGraphicalViewer )
				{
					( (DeferredGraphicalViewer) getCurrentViewer( ) ).initStepDat( );
				}
				return super.handleButtonDown( button );
			}
		};
		return track;
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
	protected Font getFont( ReportItemHandle handle )
	{
		StyleHandle styleHandle = handle.getPrivateStyle( );

		String family = (String) ( styleHandle.getFontFamilyHandle( ).getValue( ) );
		// some font not defined in model is encolsed with quote.
		family = DEUtil.RemoveQuote( family );
		String FontFamily = (String) DesignerConstants.familyMap.get( family );

		if ( FontFamily == null )
		{
			FontFamily = family;
		}

		// fix bugzilla 210899, set minimum font size as 1.
		int fontSize = Math.max( DEUtil.getFontSizeIntValue( handle ), 1 );

		int fontStyle = 0;
		String fontWeight = styleHandle.getFontWeight( );
		String style = styleHandle.getFontStyle( );

		// Eclipse does not distinct ITALIC and OBLIQUE, so we treat OBLIQUE as
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

	protected Font getFont( )
	{
		return getFont( (ReportItemHandle) getModel( ) );
	}

	/**
	 * @return display label
	 */
	public String getDisplayLabel( )
	{
		return null;
	}

	private boolean isDirty = true;

	public final void refreshVisuals( )
	{
		super.refreshVisuals( );
		refreshFigure( );
		refreshReportChildren( this );
		// added for must repaint
		getFigure( ).repaint( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
	 */
	public void refreshChildren( )
	{
		super.refreshChildren( );
	}

	public void refreshReportChildren( ReportElementEditPart parent )
	{
		List list = parent.getChildren( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object part = list.get( i );
			if ( part instanceof ReportElementEditPart )
			{
				if ( ( (ReportElementEditPart) part ).isDelete( ) )
				{
					continue;
				}
				( (ReportElementEditPart) part ).refreshFigure( );
				refreshReportChildren( (ReportElementEditPart) part );
			}
		}
	}

	public abstract void refreshFigure( );

	/**
	 * Refresh Margin property for this element.
	 */
	protected void refreshMargin( )
	{
		if ( getFigure( ) instanceof IReportElementFigure )
		{
			( (IReportElementFigure) getFigure( ) ).setMargin( getModelAdapter( ).getMargin( null ) );
		}
	}

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 * 
	 */
	protected void refreshBackground( DesignElementHandle handle )
	{
		refreshBackgroundColor( handle );
		refreshBackgroundImage( handle );
	}

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 * 
	 */
	protected void refreshBackgroundColor( DesignElementHandle handle )
	{
		Object obj = handle.getProperty( StyleHandle.BACKGROUND_COLOR_PROP );

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
	protected void refreshBackgroundImage( DesignElementHandle handle )
	{
		IReportElementFigure figure = (IReportElementFigure) getFigure( );

		String backGroundImage = getBackgroundImage( handle );
		Object[] backGroundPosition = getBackgroundPosition( handle );
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
				image = ImageManager.getInstance( ).getImage( backGroundImage );
			}
			catch ( SWTException e )
			{
				// Should not be ExceptionHandler.handle(e), see SCR#73730
				image = null;
			}

			if ( image == null )
			{
				figure.setImage( null );
				return;
			}

			figure.setImage( image );

			figure.setRepeat( backGroundRepeat );

			Object xPosition = backGroundPosition[0];
			Object yPosition = backGroundPosition[1];
			Rectangle area = getFigure( ).getClientArea( );
			org.eclipse.swt.graphics.Rectangle imageArea = image.getBounds( );
			Point position = new Point( -1, -1 );
			int alignment = 0;

			if ( xPosition instanceof Integer )
			{
				position.x = ( (Integer) xPosition ).intValue( );
			}
			else if ( xPosition instanceof DimensionValue )
			{
				int percentX = (int) ( (DimensionValue) xPosition ).getMeasure( );

				position.x = ( area.width - imageArea.width ) * percentX / 100;
			}
			else if ( xPosition instanceof String )
			{
				alignment |= DesignElementHandleAdapter.getPosition( (String) xPosition );
			}

			if ( yPosition instanceof Integer )
			{
				position.y = ( (Integer) yPosition ).intValue( );
			}
			else if ( yPosition instanceof DimensionValue )
			{
				int percentY = (int) ( (DimensionValue) yPosition ).getMeasure( );

				position.y = ( area.width - imageArea.width ) * percentY / 100;
			}
			else if ( yPosition instanceof String )
			{
				alignment |= DesignElementHandleAdapter.getPosition( (String) yPosition );
			}

			figure.setAlignment( alignment );
			figure.setPosition( position );
		}
	}

	/**
	 * Marks edit part dirty
	 * 
	 * @param bool
	 * @param notifyParent
	 */
	public void markDirty( boolean bool )
	{
		this.isDirty = bool;
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

	protected DesignElementHandleAdapter getModelAdapter( )
	{
		return peer;
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

	protected void refreshPageClip( )
	{
		if ( getFigure( ) instanceof ReportElementFigure )
		{
			Object obj = getViewer( ).getProperty( DeferredGraphicalViewer.LAYOUT_SIZE );

			if ( obj instanceof Rectangle )
			{
				( (ReportElementFigure) getFigure( ) ).setPageClip( (Rectangle) obj );
			}
		}
	}

	protected void updateBaseBorder( DesignElementHandle handle,
			BaseBorder border )
	{
		updateBottomBorder( handle, border );
		updateTopBorder( handle, border );
		updateLeftBorder( handle, border );
		updateRightBorder( handle, border );
	}

	protected void updateBottomBorder( DesignElementHandle handle,
			BaseBorder border )
	{
		border.bottomColor = handle.getPropertyHandle( StyleHandle.BORDER_BOTTOM_COLOR_PROP )
				.getIntValue( );
		border.bottomStyle = handle.getPropertyHandle( StyleHandle.BORDER_BOTTOM_STYLE_PROP )
				.getStringValue( );
		border.bottomWidth = handle.getPropertyHandle( StyleHandle.BORDER_BOTTOM_WIDTH_PROP )
				.getStringValue( );
	}

	protected void updateTopBorder( DesignElementHandle handle,
			BaseBorder border )
	{
		border.topColor = handle.getPropertyHandle( StyleHandle.BORDER_TOP_COLOR_PROP )
				.getIntValue( );
		border.topStyle = handle.getPropertyHandle( StyleHandle.BORDER_TOP_STYLE_PROP )
				.getStringValue( );
		border.topWidth = handle.getPropertyHandle( StyleHandle.BORDER_TOP_WIDTH_PROP )
				.getStringValue( );
	}

	protected void updateLeftBorder( DesignElementHandle handle,
			BaseBorder border )
	{
		border.leftColor = handle.getPropertyHandle( StyleHandle.BORDER_LEFT_COLOR_PROP )
				.getIntValue( );
		border.leftStyle = handle.getPropertyHandle( StyleHandle.BORDER_LEFT_STYLE_PROP )
				.getStringValue( );
		border.leftWidth = handle.getPropertyHandle( StyleHandle.BORDER_LEFT_WIDTH_PROP )
				.getStringValue( );

	}

	protected void updateRightBorder( DesignElementHandle handle,
			BaseBorder border )
	{
		border.rightColor = handle.getPropertyHandle( StyleHandle.BORDER_RIGHT_COLOR_PROP )
				.getIntValue( );
		border.rightStyle = handle.getPropertyHandle( StyleHandle.BORDER_RIGHT_STYLE_PROP )
				.getStringValue( );
		border.rightWidth = handle.getPropertyHandle( StyleHandle.BORDER_RIGHT_WIDTH_PROP )
				.getStringValue( );
	}

	protected void refreshBorder( DesignElementHandle handle, BaseBorder border )
	{
		updateBaseBorder( handle, border );

		getFigure( ).setBorder( border );

		refreshPageClip( );
	}

	protected Insets getMasterPageInsets( DesignElementHandle handle )
	{
		return ( (ReportDesignHandleAdapter) getModelAdapter( ) ).getMasterPageInsets( handle );
	}

	protected Dimension getMasterPageSize( DesignElementHandle handle )
	{
		return ( (ReportDesignHandleAdapter) getModelAdapter( ) ).getMasterPageSize( handle );
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

	protected Object[] getBackgroundPosition( DesignElementHandle handle )
	{
		return getModelAdapter( ).getBackgroundPosition( handle );
	}

	protected int getBackgroundRepeat( DesignElementHandle handle )
	{
		return getModelAdapter( ).getBackgroundRepeat( handle );
	}

	protected boolean isFigureLeft( Request request )
	{
		if ( !( request instanceof SelectionRequest ) )
		{
			return true;
		}
		SelectionRequest selctionRequest = (SelectionRequest) request;
		Point p = selctionRequest.getLocation( );
		// getFigure().translateToAbsolute(p);
		getFigure( ).translateToRelative( p );
		Point center = getFigure( ).getBounds( ).getCenter( );
		return center.x >= p.x;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#isActive()
	 */
	public boolean isDelete( )
	{
		boolean bool = false;
		if ( getModel( ) instanceof DesignElementHandle )
		{
			if ( !( getModel( ) instanceof ModuleHandle ) )
			{
				bool = ( (DesignElementHandle) getModel( ) ).getContainer( ) == null
						|| ( (DesignElementHandle) getModel( ) ).getRoot( ) == null;
			}
		}
		return bool;
	}

	public void notifyModelChange( )
	{
		if ( getParent( ) != null
				&& getParent( ) instanceof ReportElementEditPart )
		{
			( (ReportElementEditPart) getParent( ) ).notifyModelChange( );
		}
	}

	/**
	 * 
	 */
	protected void contentChange( Map info )
	{
		markDirty( true );
		refresh( );
	}

	/**
	 * @param focus
	 */
	protected void propertyChange( Map info )
	{
		refreshVisuals( );
	}

	/**
	 * @param model
	 * @return
	 */
	public boolean isinterest( Object model )
	{
		return getModel( ).equals( model );
	}

	/**
	 * @param object
	 * @return
	 */
	public boolean isinterestSelection( Object object )
	{
		return false;
	}

	/**
	 * @param parentPolice
	 * @return
	 */
	public EditPolicy getResizePolice( EditPolicy parentPolice )
	{
		ReportElementResizePolicy policy = new ReportElementResizePolicy( );
		policy.setResizeDirections( PositionConstants.SOUTH
				| PositionConstants.EAST
				| PositionConstants.SOUTH_EAST );
		return policy;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#removeChild(org.eclipse.gef.EditPart)
	 */
	public void removeChild( EditPart child )
	{
		// TODO Auto-generated method stub
		super.removeChild( child );
	}
}