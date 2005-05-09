/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.DefaultRangeModel;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.ui.rulers.GuideEditPart;
import org.eclipse.gef.internal.ui.rulers.RulerEditPart;
import org.eclipse.gef.internal.ui.rulers.RulerRootEditPart;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * A RulerComposite is used to show rulers to the north and west of the control
 * of a given
 * {@link #setGraphicalViewer(ScrollingGraphicalViewer) graphical viewer}. The
 * rulers will be shown based on whether or not
 * {@link org.eclipse.gef.rulers.RulerProvider#PROPERTY_HORIZONTAL_RULER horizontal ruler}
 * and
 * {@link org.eclipse.gef.rulers.RulerProvider#PROPERTY_VERTICAL_RULER vertical ruler}
 * properties are set on the given viewer, and the value of the
 * {@link org.eclipse.gef.rulers.RulerProvider#PROPERTY_RULER_VISIBILITY visibility}
 * property.
 *  
 */
public class EditorRulerComposite extends Composite
{

	private static final int TOP_LEFT = 0;
	private static final int TOP_RIGHT = 1;
	private static final int LEFT_TOP = 2;
	private static final int LEFT_BOTTOM = 3;

	private EditDomain rulerEditDomain;
	private GraphicalViewer left, top;
	private FigureCanvas editor;
	private GraphicalViewer diagramViewer;
	private Font font;
	private Listener layoutListener;
	private PropertyChangeListener propertyListener;
	private boolean layingOut = false;
	private boolean isRulerVisible = true;
	private boolean needToLayout = false;

	private ZoomListener zoomListener = new ZoomListener( ) {

		public void zoomChanged( double newZoomValue )
		{
			layout( true );
			//processProvider();
		}
	};

	private Runnable runnable = new Runnable( ) {

		public void run( )
		{
			layout( false );
		}
	};

	private org.eclipse.birt.report.model.api.core.Listener designListener = new org.eclipse.birt.report.model.api.core.Listener( ) {

		public void elementChanged( DesignElementHandle focus,
				NotificationEvent ev )
		{
			switch ( ev.getEventType( ) )
			{
				case NotificationEvent.PROPERTY_EVENT :
				{

					PropertyEvent event = (PropertyEvent) ev;
					if ( ReportDesignHandle.UNITS_PROP.equals( event.getPropertyName( ) ) )
					{
						setUnit( getUnitFromDesign( getReportDesignHandle( ).getDefaultUnits( ) ) );
					}
					else if ( MasterPageHandle.LEFT_MARGIN_PROP.equals( event.getPropertyName( ) ) )
					{
						setMargin( TOP_LEFT );
					}
					else if ( MasterPageHandle.RIGHT_MARGIN_PROP.equals( event.getPropertyName( ) ) )
					{
						setMargin( TOP_RIGHT );
					}
					else if ( MasterPageHandle.TOP_MARGIN_PROP.equals( event.getPropertyName( ) ) )
					{
						setMargin( LEFT_TOP );
					}
					else if ( MasterPageHandle.BOTTOM_MARGIN_PROP.equals( event.getPropertyName( ) ) )
					{
						setMargin( LEFT_BOTTOM );
					}
					else if ( MasterPageHandle.TYPE_PROP.equals( event.getPropertyName( ) ) )
					{
						layout( true );
						resetAllGuide( );
					}
					else if ( MasterPageHandle.WIDTH_PROP.equals( event.getPropertyName( ) ) )
					{
						layout( true );
						setMargin( TOP_LEFT );
						setMargin( TOP_RIGHT );
					}
					else if ( MasterPageHandle.HEIGHT_PROP.equals( event.getPropertyName( ) ) )
					{
						layout( true );
						setMargin( LEFT_TOP );
						setMargin( LEFT_BOTTOM );
					}
					break;

				}
			}

		}
	};

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 * @see Composite#Composite(org.eclipse.swt.widgets.Composite, int)
	 */
	public EditorRulerComposite( Composite parent, int style )
	{
		super( parent, style );
		addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				disposeResources( );
			}
		} );
	}

	private GraphicalViewer createRulerContainer( int orientation )
	{
		ScrollingGraphicalViewer viewer = new RulerViewer( );
		final boolean isHorizontal = orientation == PositionConstants.NORTH
				|| orientation == PositionConstants.SOUTH;

		// Finish initialize the viewer
		viewer.setRootEditPart( new RulerRootEditPart( isHorizontal ) );
		viewer.setEditPartFactory( new EditorRulerEditPartFactory( diagramViewer ) );
		viewer.createControl( this );
		( (GraphicalEditPart) viewer.getRootEditPart( ) ).getFigure( )
				.setBorder( new RulerBorder( isHorizontal ) );
		viewer.setProperty( GraphicalViewer.class.toString( ), diagramViewer );

		// Configure the viewer's control
		FigureCanvas canvas = (FigureCanvas) viewer.getControl( );
		canvas.setScrollBarVisibility( FigureCanvas.NEVER );
		if ( font == null )
		{
			FontData[] data = canvas.getFont( ).getFontData( );
			for ( int i = 0; i < data.length; i++ )
			{
				data[i].setHeight( data[i].getHeight( ) - 1 );
			}
			font = new Font( Display.getCurrent( ), data );
		}
		canvas.setFont( font );
		if ( isHorizontal )
		{
			canvas.getViewport( ).setHorizontalRangeModel( editor.getViewport( )
					.getHorizontalRangeModel( ) );
		}
		else
		{
			canvas.getViewport( ).setVerticalRangeModel( editor.getViewport( )
					.getVerticalRangeModel( ) );
		}

		// Add the viewer to the rulerEditDomain
		if ( rulerEditDomain == null )
		{
			rulerEditDomain = new EditDomain( );
			rulerEditDomain.setCommandStack( diagramViewer.getEditDomain( )
					.getCommandStack( ) );
		}
		rulerEditDomain.addViewer( viewer );

		return viewer;
	}

	private void disposeResources( )
	{
		if ( diagramViewer != null )
			diagramViewer.removePropertyChangeListener( propertyListener );
		getZoomManager( ).removeZoomListener( zoomListener );
		if ( font != null )
			font.dispose( );
		if ( getReportDesignHandle( ) != null )
		{
			getReportDesignHandle( ).removeListener( designListener );
		}
		if ( getMasterPageHandle( ) != null )
		{
			getMasterPageHandle( ).removeListener( designListener );
		}
		// layoutListener is not being removed from the scroll bars because they
		// are already
		// disposed at this point.
	}

	private void disposeRulerViewer( GraphicalViewer viewer )
	{
		if ( viewer == null )
			return;
		/*
		 * There's a tie from the editor's range model to the RulerViewport (via
		 * a listener) to the RulerRootEditPart to the RulerViewer. Break this
		 * tie so that the viewer doesn't leak and can be garbage collected.
		 */

		RangeModel rModel = new DefaultRangeModel( );
		Viewport port = ( (FigureCanvas) viewer.getControl( ) ).getViewport( );
		port.setHorizontalRangeModel( rModel );
		port.setVerticalRangeModel( rModel );
		rulerEditDomain.removeViewer( viewer );
		viewer.getControl( ).dispose( );
	}

	private void doLayout( )
	{
		if ( left == null && top == null )
		{
			Rectangle area = getClientArea( );
			if ( !editor.getBounds( ).equals( area ) )
				editor.setBounds( area );
			return;
		}

		int leftWidth, rightWidth, topHeight, bottomHeight;
		leftWidth = left == null ? 0 : left.getControl( )
				.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		rightWidth = 0;
		topHeight = top == null ? 0 : top.getControl( )
				.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		bottomHeight = 0;

		Point size = getSize( );
		Point editorSize = new Point( size.x - ( leftWidth + rightWidth ),
				size.y - ( topHeight + bottomHeight ) );
		if ( !editor.getSize( ).equals( editorSize ) )
			editor.setSize( editorSize );
		Point editorLocation = new Point( leftWidth, topHeight );
		if ( !editor.getLocation( ).equals( editorLocation ) )
			editor.setLocation( editorLocation );

		//		int vBarWidth = 0, hBarHeight = 0;
		//		Rectangle trim = editor.computeTrim( 0, 0, 0, 0 );
		//		/*
		//		 * Fix for Bug# 67554 Motif leaves a few pixels of space around the
		//		 * Canvas which can cause the rulers to mis-aligned.
		//		 */
		//		if ( editor.getVerticalBar( ).getVisible( ) )
		//			vBarWidth = trim.width
		//					+ ( "motif".equals( SWT.getPlatform( ) ) ? trim.x * 2 : 0 );
		// //$NON-NLS-1$
		//		if ( editor.getHorizontalBar( ).getVisible( ) )
		//			hBarHeight = trim.height
		//					+ ( "motif".equals( SWT.getPlatform( ) ) ? trim.y * 2 : 0 );
		// //$NON-NLS-1$

		//Dimension dim = getMasterPageSize( getMasterPageHandle( ) );
		//modify by gao

		PrecisionRectangle dim = new PrecisionRectangle( getLayoutSize( ) );
		dim.performScale( getZoom( ) );
		if ( left != null )
		{
			Rectangle leftBounds = new Rectangle( 0,
					topHeight - 1,
					leftWidth,
					dim.height + dim.y );
			if ( !left.getControl( ).getBounds( ).equals( leftBounds ) )
				left.getControl( ).setBounds( leftBounds );
		}
		if ( top != null )
		{
			Rectangle topBounds = new Rectangle( leftWidth - 1, 0, dim.width
					+ dim.x, topHeight );
			if ( !top.getControl( ).getBounds( ).equals( topBounds ) )
				top.getControl( ).setBounds( topBounds );
		}
	}

	protected org.eclipse.draw2d.geometry.Rectangle getScaleValue(
			org.eclipse.draw2d.geometry.Rectangle value )
	{
		PrecisionRectangle dim = new PrecisionRectangle( value );
		dim.performScale( getZoom( ) );
		return dim;
	}

	/**
	 * Sets the ruler unit.
	 * 
	 * @param unit
	 */
	public void setUnit( int unit )
	{
		if ( unit != EditorRulerProvider.UNIT_NOSUPPOER )
		{
			//			added by gao
			Object obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_HORIZONTAL_RULER ) );
			if ( obj instanceof EditorRulerProvider )
			{
				( (EditorRulerProvider) obj ).setUnit( unit );
			}

			obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_VERTICAL_RULER ) );

			if ( obj instanceof EditorRulerProvider )
			{
				( (EditorRulerProvider) obj ).setUnit( unit );
			}
		}

	}

	/**
	 * Sets the ruler margin.
	 * 
	 * @param direction
	 */
	public void setMargin( int direction )
	{
		switch ( direction )
		{
			case TOP_LEFT :
			{
				Object obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_HORIZONTAL_RULER ) ).getRuler( );
				if ( obj instanceof EditorRuler )
				{
					( (EditorRuler) obj ).setLeftMargin( getLayoutSize( ).x
							+ getLeftMargin( ) );
				}
				break;
			}
			case TOP_RIGHT :
			{
				Object obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_HORIZONTAL_RULER ) ).getRuler( );
				if ( obj instanceof EditorRuler )
				{
					( (EditorRuler) obj ).setRightMargin( getLayoutSize( ).right( )
							- getRightMargin( ) );
				}
				break;
			}
			case LEFT_TOP :
			{
				Object obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_VERTICAL_RULER ) ).getRuler( );
				if ( obj instanceof EditorRuler )
				{
					( (EditorRuler) obj ).setLeftMargin( getLayoutSize( ).y
							+ getTopMargin( ) );
				}
				break;
			}
			case LEFT_BOTTOM :
			{
				Object obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_VERTICAL_RULER ) ).getRuler( );
				if ( obj instanceof EditorRuler )
				{
					( (EditorRuler) obj ).setRightMargin( getLayoutSize( ).bottom( )
							- getBottomMargin( ) );
				}
				break;
			}
			default :
				break;
		}
	}

	private int getLeftMargin( )
	{
		return (int) ( getMasterPageInsets( getMasterPageHandle( ) ).left );
	}

	private int getRightMargin( )
	{
		return (int) ( getMasterPageInsets( getMasterPageHandle( ) ).right );
	}

	private int getBottomMargin( )
	{
		return (int) ( getMasterPageInsets( getMasterPageHandle( ) ).bottom );
	}

	private double getZoom( )
	{
		return getZoomManager( ).getZoom( );
	}

	/**
	 * Returns the zoom manager for current viewer.
	 * 
	 * @return
	 */
	public ZoomManager getZoomManager( )
	{
		return (ZoomManager) diagramViewer.getProperty( ZoomManager.class.toString( ) );
	}

	private int getTopMargin( )
	{
		return (int) ( getMasterPageInsets( getMasterPageHandle( ) ).top );
	}

	private GraphicalViewer getRulerContainer( int orientation )
	{
		GraphicalViewer result = null;
		switch ( orientation )
		{
			case PositionConstants.NORTH :
				result = top;
				break;
			case PositionConstants.WEST :
				result = left;
		}
		return result;
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#layout(boolean)
	 */
	public void layout( boolean change )
	{
		if ( !layingOut && !isDisposed( ) )
		{
			checkWidget( );
			if ( change || needToLayout )
			{
				needToLayout = false;
				layingOut = true;
				doLayout( );
				layingOut = false;
			}
		}
	}

	/**
	 * Creates rulers for the given graphical viewer.
	 * <p>
	 * The primaryViewer or its Control cannot be <code>null</code>. The
	 * primaryViewer's Control should be a FigureCanvas and a child of this
	 * Composite. This method should only be invoked once.
	 * <p>
	 * To create ruler(s), simply add the RulerProvider(s) (with the right key:
	 * RulerProvider.PROPERTY_HORIZONTAL_RULER or
	 * RulerProvider.PROPERTY_VERTICAL_RULER) as a property on the given viewer.
	 * It can be done after this method is invoked.
	 * RulerProvider.PROPERTY_RULER_VISIBILITY can be used to show/hide the
	 * rulers.
	 * 
	 * @param primaryViewer
	 *            The graphical viewer for which the rulers have to be created
	 */
	public void setGraphicalViewer( ScrollingGraphicalViewer primaryViewer )
	{
		// pre-conditions
		Assert.isNotNull( primaryViewer );
		Assert.isNotNull( primaryViewer.getControl( ) );
		Assert.isTrue( diagramViewer == null );

		diagramViewer = primaryViewer;
		editor = (FigureCanvas) diagramViewer.getControl( );

		getZoomManager( ).addZoomListener( zoomListener );
		// layout whenever the scrollBars are shown or hidden, and whenever the
		// RulerComposite
		// is resized
		layoutListener = new Listener( ) {

			public void handleEvent( Event event )
			{
				// @TODO: If you use Display.asyncExec(runnable) here,
				// some flashing
				// occurs. You can see it when the palette is in the editor, and
				// you hit
				// the button to show/hide it.
				layout( true );
			}
		};
		addListener( SWT.Resize, layoutListener );
		editor.getHorizontalBar( ).addListener( SWT.Show, layoutListener );
		editor.getHorizontalBar( ).addListener( SWT.Hide, layoutListener );
		editor.getVerticalBar( ).addListener( SWT.Show, layoutListener );
		editor.getVerticalBar( ).addListener( SWT.Hide, layoutListener );

		propertyListener = new PropertyChangeListener( ) {

			public void propertyChange( PropertyChangeEvent evt )
			{
				String property = evt.getPropertyName( );

				if ( DeferredGraphicalViewer.PROPERTY_MARGIN_VISIBILITY.equals( property ) )
				{
					Object obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_HORIZONTAL_RULER ) );

					if ( obj instanceof EditorRulerProvider )
					{
						Object ruler = ( (RulerProvider) obj ).getRuler( );
						if ( ruler instanceof EditorRuler )
						{
							( (EditorRuler) ruler ).setMarginOff( !( (Boolean) evt.getNewValue( ) ).booleanValue( ) );
							setMargin( TOP_LEFT );
							setMargin( TOP_RIGHT );
						}
					}

					obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_VERTICAL_RULER ) );

					if ( obj instanceof EditorRulerProvider )
					{
						Object ruler = ( (RulerProvider) obj ).getRuler( );
						if ( ruler instanceof EditorRuler )
						{
							( (EditorRuler) ruler ).setMarginOff( !( (Boolean) evt.getNewValue( ) ).booleanValue( ) );
							setMargin( LEFT_TOP );
							setMargin( LEFT_BOTTOM );
						}
					}

				}
				else if ( DeferredGraphicalViewer.LAYOUT_SIZE.equals( property ) )
				{
					processProvider( );
				}
				else if ( RulerProvider.PROPERTY_HORIZONTAL_RULER.equals( property ) )
				{
					setRuler( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_HORIZONTAL_RULER ),
							PositionConstants.NORTH );
				}
				else if ( RulerProvider.PROPERTY_VERTICAL_RULER.equals( property ) )
				{
					setRuler( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_VERTICAL_RULER ),
							PositionConstants.WEST );
				}
				else if ( RulerProvider.PROPERTY_RULER_VISIBILITY.equals( property ) )
					setRulerVisibility( ( (Boolean) diagramViewer.getProperty( RulerProvider.PROPERTY_RULER_VISIBILITY ) ).booleanValue( ) );
			}
		};
		diagramViewer.addPropertyChangeListener( propertyListener );
		Boolean rulerVisibility = (Boolean) diagramViewer.getProperty( RulerProvider.PROPERTY_RULER_VISIBILITY );
		if ( rulerVisibility != null )
			setRulerVisibility( rulerVisibility.booleanValue( ) );
		setRuler( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_HORIZONTAL_RULER ),
				PositionConstants.NORTH );
		setRuler( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_VERTICAL_RULER ),
				PositionConstants.WEST );

		resetAllGuide( );

		setUnit( getUnitFromDesign( getReportDesignHandle( ).getDefaultUnits( ) ) );
		getReportDesignHandle( ).addListener( designListener );
		getMasterPageHandle( ).addListener( designListener );
	}

	private void processProvider( )
	{
		org.eclipse.draw2d.geometry.Rectangle rect = (org.eclipse.draw2d.geometry.Rectangle) diagramViewer.getProperty( DeferredGraphicalViewer.LAYOUT_SIZE );
		if ( rect == null )
		{
			return;
		}
		Object obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_HORIZONTAL_RULER ) );

		if ( obj instanceof EditorRulerProvider )
		{

			( (EditorRulerProvider) obj ).setLayoutSize( rect.getCopy( ) );

			( (EditorRulerProvider) obj ).setLeftSpace( rect.getCopy( ) );
			//layout( true );

			//setMargin( TOP_RIGHT );
			//resetAllGuide();
		}

		obj = ( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_VERTICAL_RULER ) );

		if ( obj instanceof EditorRulerProvider )
		{
			( (EditorRulerProvider) obj ).setLayoutSize( rect.getCopy( ) );
			( (EditorRulerProvider) obj ).setLeftSpace( rect.getCopy( ) );
			//layout( true );
			//setMargin( LEFT_BOTTOM );
			//resetAllGuide();
		}
		layout( true );
		resetAllGuide( );
	}

	private void resetAllGuide( )
	{
		setMargin( TOP_LEFT );
		setMargin( TOP_RIGHT );
		setMargin( LEFT_TOP );
		setMargin( LEFT_BOTTOM );
	}

	private void setRuler( RulerProvider provider, int orientation )
	{
		Object ruler = null;
		if ( isRulerVisible && provider != null )
			// provider.getRuler() might return null (at least the API does not
			// prevent that)
			ruler = provider.getRuler( );

		if ( ruler == null )
		{
			// Ruler is not visible or is not present
			setRulerContainer( null, orientation );
			// Layout right-away to prevent an empty control from showing
			layout( true );
			return;
		}

		GraphicalViewer container = getRulerContainer( orientation );
		if ( container == null )
		{
			container = createRulerContainer( orientation );
			setRulerContainer( container, orientation );
		}
		if ( container.getContents( ) != ruler )
		{
			container.setContents( ruler );
			needToLayout = true;
			Display.getCurrent( ).asyncExec( runnable );
		}
	}

	private void setRulerContainer( GraphicalViewer container, int orientation )
	{
		if ( orientation == PositionConstants.NORTH )
		{
			if ( top == container )
				return;
			disposeRulerViewer( top );
			top = container;
		}
		else if ( orientation == PositionConstants.WEST )
		{
			if ( left == container )
				return;
			disposeRulerViewer( left );
			left = container;
		}
	}

	/**
	 * Returns the actual layout size.
	 * 
	 * @return
	 */
	public org.eclipse.draw2d.geometry.Rectangle getLayoutSize( )
	{
		Object obj = diagramViewer.getProperty( DeferredGraphicalViewer.LAYOUT_SIZE );

		if ( obj instanceof org.eclipse.draw2d.geometry.Rectangle )
		{
			return ( (org.eclipse.draw2d.geometry.Rectangle) obj ).getCopy( );
		}

		Dimension dim = getMasterPageSize( getMasterPageHandle( ) );

		return new org.eclipse.draw2d.geometry.Rectangle( 0,
				0,
				dim.width,
				dim.height );
	}

	/**
	 * Get the current master page size.
	 * 
	 * @param handle
	 *            The handle of master page.
	 * @return The current master page size.
	 */
	public static Dimension getMasterPageSize( MasterPageHandle masterPage )
	{
		Dimension size = null;

		if ( masterPage == null
				|| masterPage.getPageType( )
						.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_US_LETTER ) )
		{
			size = new Dimension( MetricUtility.inchToPixel( 8.5, 11 ).x,
					MetricUtility.inchToPixel( 8.5, 11 ).y );
		}
		else if ( masterPage.getPageType( )
				.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_US_LEGAL ) )
		{
			size = new Dimension( MetricUtility.inchToPixel( 8.5, 14 ).x,
					MetricUtility.inchToPixel( 8.5, 14 ).y );
		}
		else if ( masterPage.getPageType( )
				.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_A4 ) )
		{
			size = new Dimension( MetricUtility.inchToPixel( 8.3, 11.7 ).x,
					MetricUtility.inchToPixel( 8.3, 11.7 ).y );
		}
		else if ( masterPage.getPageType( )
				.equalsIgnoreCase( DesignChoiceConstants.PAGE_SIZE_CUSTOM ) )
		{
			int width = (int) DEUtil.convertoToPixel( masterPage.getWidth( ) );
			int height = (int) DEUtil.convertoToPixel( masterPage.getHeight( ) );
			size = new Dimension( width, height );
		}

		if ( DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE.equalsIgnoreCase( masterPage.getOrientation( ) ) )
		{
			size = new Dimension( size.height, size.width );
		}

		return size;

	}

	private void setRulerVisibility( boolean isVisible )
	{
		if ( isRulerVisible != isVisible )
		{
			isRulerVisible = isVisible;
			if ( diagramViewer != null )
			{
				setRuler( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_HORIZONTAL_RULER ),
						PositionConstants.NORTH );
				setRuler( (RulerProvider) diagramViewer.getProperty( RulerProvider.PROPERTY_VERTICAL_RULER ),
						PositionConstants.WEST );
			}
		}
	}

	private Insets getMasterPageInsets( MasterPageHandle masterPage )
	{
		return new Insets( (int) DEUtil.convertoToPixel( masterPage.getTopMargin( ) ),
				(int) DEUtil.convertoToPixel( masterPage.getLeftMargin( ) ),
				(int) DEUtil.convertoToPixel( masterPage.getBottomMargin( ) ),
				(int) DEUtil.convertoToPixel( masterPage.getRightMargin( ) ) );
	}

	private MasterPageHandle getMasterPageHandle( )
	{
		return SessionHandleAdapter.getInstance( ).getMasterPageHandle( );
	}

	private ReportDesignHandle getReportDesignHandle( )
	{
		return SessionHandleAdapter.getInstance( ).getReportDesignHandle( );
	}

	private int getUnitFromDesign( String name )
	{
		if ( DesignChoiceConstants.UNITS_CM.equals( name ) )
		{
			return EditorRulerProvider.UNIT_CENTIMETERS;
		}
		else if ( DesignChoiceConstants.UNITS_IN.equals( name ) )
		{
			return EditorRulerProvider.UNIT_INCHES;
		}
		else if ( DesignChoiceConstants.UNITS_PX.equals( name ) )
		{
			return EditorRulerProvider.UNIT_PIXELS;
		}
		else if ( DesignChoiceConstants.UNITS_MM.equals( name ) )
		{
			return EditorRulerProvider.UNIT_MM;
		}
		else if ( DesignChoiceConstants.UNITS_PC.equals( name ) )
		{
			return EditorRulerProvider.UNIT_PC;
		}
		else if ( DesignChoiceConstants.UNITS_PT.equals( name ) )
		{
			return EditorRulerProvider.UNIT_PT;
		}
		return EditorRulerProvider.UNIT_NOSUPPOER;
		//return UNIT_NOSUPPOER;
	}

	private static class RulerBorder extends AbstractBorder
	{

		private static final Insets H_INSETS = new Insets( 0, 1, 0, 0 );
		private static final Insets V_INSETS = new Insets( 1, 0, 0, 0 );
		private boolean horizontal;

		/**
		 * Constructor
		 * 
		 * @param isHorizontal
		 *            whether or not the ruler being bordered is horizontal or
		 *            not
		 */
		public RulerBorder( boolean isHorizontal )
		{
			horizontal = isHorizontal;
		}

		/**
		 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
		 */
		public Insets getInsets( IFigure figure )
		{
			return horizontal ? H_INSETS : V_INSETS;
		}

		/**
		 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure,
		 *      org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
		 */
		public void paint( IFigure figure, Graphics graphics, Insets insets )
		{
			graphics.setForegroundColor( ColorConstants.buttonDarker );
			if ( horizontal )
			{
				graphics.drawLine( figure.getBounds( ).getTopLeft( ),
						figure.getBounds( )
								.getBottomLeft( )
								.translate( new org.eclipse.draw2d.geometry.Point( 0,
										-4 ) ) );
			}
			else
			{
				graphics.drawLine( figure.getBounds( ).getTopLeft( ),
						figure.getBounds( )
								.getTopRight( )
								.translate( new org.eclipse.draw2d.geometry.Point( -4,
										0 ) ) );
			}
		}
	}

	/**
	 * Custom graphical viewer intended to be used for rulers.
	 *  
	 */
	private static class RulerViewer extends ScrollingGraphicalViewer
	{

		/**
		 * Constructor
		 */
		public RulerViewer( )
		{
			super( );
			init( );
		}

		/**
		 * @see org.eclipse.gef.EditPartViewer#appendSelection(org.eclipse.gef.EditPart)
		 */
		public void appendSelection( EditPart editpart )
		{
			if ( editpart instanceof RootEditPart )
				editpart = ( (RootEditPart) editpart ).getContents( );
			setFocus( editpart );
			super.appendSelection( editpart );
		}

		//		/**
		//		 * @see
		// org.eclipse.gef.GraphicalViewer#findHandleAt(org.eclipse.draw2d.geometry.Point)
		//		 */
		//		public Handle findHandleAt( org.eclipse.draw2d.geometry.Point p )
		//		{
		//			final GraphicalEditPart gep = (GraphicalEditPart)
		// findObjectAtExcluding(
		//					p, new ArrayList( ) );
		//			if ( gep == null || !( gep instanceof GuideEditPart ) )
		//				return null;
		//			return new Handle( )
		//			{
		//
		//				public DragTracker getDragTracker( )
		//				{
		//					return ( (GuideEditPart) gep ).getDragTracker( null );
		//				}
		//
		//				public org.eclipse.draw2d.geometry.Point getAccessibleLocation( )
		//				{
		//					return null;
		//				}
		//			};
		//		}

		/**
		 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#init()
		 */
		protected void init( )
		{
			setContextMenu( new EditorRulerContextMenuProvider( this ) );
			setKeyHandler( new RulerKeyHandler( this ) );
		}

		/**
		 * Requests to reveal a ruler are ignored since that causes undesired
		 * scrolling to the origin of the ruler
		 * 
		 * @see org.eclipse.gef.EditPartViewer#reveal(org.eclipse.gef.EditPart)
		 */
		public void reveal( EditPart part )
		{
			if ( part != getContents( ) )
				super.reveal( part );
		}

		/**
		 * @see org.eclipse.gef.EditPartViewer#setContents(org.eclipse.gef.EditPart)
		 */
		public void setContents( EditPart editpart )
		{
			super.setContents( editpart );
			setFocus( getContents( ) );
		}

		/**
		 * Custom KeyHandler intended to be used with a RulerViewer
		 * 
		 * @author Pratik Shah
		 * @since 3.0
		 */
		protected static class RulerKeyHandler extends
				GraphicalViewerKeyHandler
		{

			/**
			 * Constructor
			 * 
			 * @param viewer
			 *            The viewer for which this handler processes keyboard
			 *            input
			 */
			public RulerKeyHandler( GraphicalViewer viewer )
			{
				super( viewer );
			}

			/**
			 * @see org.eclipse.gef.KeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
			 */
			public boolean keyPressed( KeyEvent event )
			{
				if ( event.keyCode == SWT.DEL )
				{
					// If a guide has focus, delete it
					if ( getFocusEditPart( ) instanceof GuideEditPart )
					{
						RulerEditPart parent = (RulerEditPart) getFocusEditPart( ).getParent( );
						getViewer( ).getEditDomain( )
								.getCommandStack( )
								.execute( parent.getRulerProvider( )
										.getDeleteGuideCommand( getFocusEditPart( ).getModel( ) ) );
						event.doit = false;
						return true;
					}
					return false;
				}
				else if ( ( ( event.stateMask & SWT.ALT ) != 0 )
						&& ( event.keyCode == SWT.ARROW_UP ) )
				{
					// ALT + UP_ARROW pressed
					// If a guide has focus, give focus to the ruler
					EditPart parent = getFocusEditPart( ).getParent( );
					if ( parent instanceof RulerEditPart )
						navigateTo( getFocusEditPart( ).getParent( ), event );
					return true;
				}
				return super.keyPressed( event );
			}
		}
	}

}