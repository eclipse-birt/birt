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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.commands.DeleteColumnCommand;
import org.eclipse.birt.report.designer.core.commands.DeleteRowCommand;
import org.eclipse.birt.report.designer.core.model.ITableAdapterHelper;
import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SectionBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.TableXYLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.TableFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.AbstractGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.TableGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.layer.TableGridLayer;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.command.ContentException;
import org.eclipse.birt.report.model.command.NameException;
import org.eclipse.birt.report.model.command.PropertyEvent;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editparts.GuideLayer;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

/**
 * <p>
 * Table EditPart,control the UI & model of table
 * </p>
 *  
 */
public class TableEditPart extends ReportElementEditPart
		implements
			LayerConstants,
			ITableAdapterHelper
{

	private static final String RESIZE_COLUMN_TRANS_LABEL = Messages.getString( "TableEditPart.Label.ResizeColumn" ); //$NON-NLS-1$

	private static final String MERGE_TRANS_LABEL = Messages.getString( "TableEditPart.Label.Merge" ); //$NON-NLS-1$

	private static final String GUIDEHANDLE_TEXT = Messages.getString( "TableEditPart.GUIDEHANDLE_TEXT" ); //$NON-NLS-1$

	protected FreeformLayeredPane innerLayers;

	protected LayeredPane printableLayers;

	private Rectangle selectRowAndColumnRect = null;

	/**
	 * Constructor
	 * 
	 * @param obj
	 */
	public TableEditPart( Object obj )
	{
		super( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createGuideHandle()
	 */
	protected AbstractGuideHandle createGuideHandle( )
	{
		TableGuideHandle handle = new TableGuideHandle( this );
		handle.setIndicatorLabel( GUIDEHANDLE_TEXT );
		handle.setIndicatorIcon( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_TABLE ) );
		return handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate( )
	{
		super.activate( );
		addListenerToChildren( );
	}

	/*
	 * s (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate( )
	{
		super.deactivate( );
		removeRowListener( );
		removeColumnListener( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		TableFigure viewport = new TableFigure( );
		viewport.setOpaque( false );

		innerLayers = new FreeformLayeredPane( );
		createLayers( innerLayers );
		viewport.setContents( innerLayers );
		return viewport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE,
				new ReportComponentEditPolicy( ) );
		installEditPolicy( EditPolicy.CONTAINER_ROLE,
				new ReportContainerEditPolicy( ) );
		//should add highlight policy
		//installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new
		// ContainerHighlightEditPolicy());
		installEditPolicy( EditPolicy.LAYOUT_ROLE,
				new TableXYLayoutEditPolicy( (XYLayout) getContentPane( ).getLayoutManager( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{
		return getTableAdapter( ).getChildren( );
	}

	protected void addRowListener( )
	{
		List list = getRows( );
		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			RowHandle hanlde = (RowHandle) list.get( i );
			hanlde.addListener( this );
		}
	}

	protected void addColumnListener( )
	{
		List list = getColumns( );
		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			( (ColumnHandle) list.get( i ) ).addListener( this );
		}
	}

	protected void removeColumnListener( )
	{
		List list = getColumns( );
		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			( (ColumnHandle) list.get( i ) ).removeListener( this );
		}
	}

	protected void removeRowListener( )
	{
		List list = getRows( );
		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			( (RowHandle) list.get( i ) ).removeListener( this );
		}
	}

	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( !isActive( ) )
		{
			return;
		}
		switch ( ev.getEventType( ) )
		{
			case NotificationEvent.CONTENT_EVENT :
			{
				markDirty( true );
				if ( focus instanceof TableHandle
						|| focus instanceof TableGroupHandle )
				{
					addListenerToChildren( );
				}
				refreshChildren( );
				refreshVisuals( );

				break;
			}
			case NotificationEvent.PROPERTY_EVENT :
			{

				markDirty( true );
				reLayout( );

				PropertyEvent event = (PropertyEvent) ev;

				if ( event.getPropertyName( ).startsWith( "border" ) )//$NON-NLS-1$
				{
					refreshVisuals( );
				}
				if ( event.getPropertyName( ).equals( Style.PADDING_TOP_PROP )
						|| event.getPropertyName( )
								.equals( Style.PADDING_BOTTOM_PROP )
						|| event.getPropertyName( )
								.equals( Style.PADDING_LEFT_PROP )
						|| event.getPropertyName( )
								.equals( Style.PADDING_RIGHT_PROP ) )
				{
					invalidParent( );
				}
				if ( event.getPropertyName( ).equals( ReportItem.WIDTH_PROP )
						|| event.getPropertyName( )
								.equals( ReportItem.HEIGHT_PROP ) )
				{
					invalidParent( );
				}

				refresh( );

				break;
			}
			case NotificationEvent.ELEMENT_DELETE_EVENT :
			{
				markDirty( true );
				refresh( );

				break;
			}
			case NotificationEvent.STYLE_EVENT :
			{
				markDirty( true );

				invalidParent( );

				refresh( );
			}
			default :
				break;
		}

	}

	/**
	 * Re-layouts table.
	 */
	public void reLayout( )
	{
		getFigure( ).invalidateTree( );
		getFigure( ).getUpdateManager( ).addInvalidFigure( getFigure( ) );
	}

	protected void invalidParent( )
	{
		getFigure( ).getParent( ).revalidate( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
	 */
	protected void refreshChildren( )
	{
		super.refreshChildren( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest( Request request )
	{
		if ( RequestConstants.REQ_OPEN.equals( request.getType( ) ) )
		{
			Object obj = request.getExtendedData( )
					.get( DesignerConstants.TABLE_ROW_NUMBER );
			if ( obj != null )
			{
				int rowNum = ( (Integer) obj ).intValue( );
				RowHandle row = (RowHandle) getRow( rowNum );
				if ( row.getContainer( ) instanceof TableGroupHandle )
				{
					IAction action = new EditGroupAction( null,
							(TableGroupHandle) row.getContainer( ) );
					if ( action.isEnabled( ) )
					{
						action.run( );
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractReportEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		refreshBorder( getTableAdapter( ).getHandle( ),
				(BaseBorder) getFigure( ).getBorder( ) );

		( (SectionBorder) ( getFigure( ).getBorder( ) ) ).setPaddingInsets( getTableAdapter( ).getPadding( getFigure( ).getInsets( ) ) );

		refreshBackground( (DesignElementHandle) getModel( ) );

		refreshMargin( );

		for ( Iterator itr = getChildren( ).iterator( ); itr.hasNext( ); )
		{
			TableCellEditPart fg = (TableCellEditPart) itr.next( );
			fg.updateBlankString( );
		}
	}

	/**
	 * Gets the top, left, right, bottom of edit part.
	 * 
	 * @param parts
	 * @return cell edit parts.
	 */
	public TableCellEditPart[] getMinAndMaxNumber( TableCellEditPart[] parts )
	{
		if ( parts == null || parts.length == 0 )
		{
			return null;
		}
		int size = parts.length;
		TableCellEditPart leftTopPart = parts[0];
		TableCellEditPart leftBottomPart = parts[0];

		TableCellEditPart rightBottomPart = parts[0];
		TableCellEditPart rightTopPart = parts[0];
		for ( int i = 1; i < size; i++ )
		{
			TableCellEditPart part = parts[i];
			if ( part == null )
			{
				continue;
			}

			if ( part.getRowNumber( ) <= leftTopPart.getRowNumber( )
					&& part.getColumnNumber( ) <= leftTopPart.getColumnNumber( ) )
			{
				leftTopPart = part;
			}

			if ( part.getRowNumber( ) <= rightTopPart.getRowNumber( )
					&& part.getColumnNumber( ) + part.getColSpan( ) - 1 >= leftTopPart.getColumnNumber( ) )
			{
				rightTopPart = part;
			}

			if ( part.getColumnNumber( ) <= leftBottomPart.getColumnNumber( )
					&& part.getRowNumber( ) + part.getRowSpan( ) - 1 >= leftBottomPart.getRowNumber( ) )
			{
				leftBottomPart = part;
			}

			if ( part.getRowNumber( ) + part.getRowSpan( ) - 1 >= rightBottomPart.getRowNumber( )
					&& part.getColumnNumber( ) + part.getColSpan( ) - 1 >= rightBottomPart.getColumnNumber( ) )
			{
				rightBottomPart = part;
			}
		}
		return new TableCellEditPart[]{
				leftTopPart, rightTopPart, leftBottomPart, rightBottomPart
		};
	}

	/**
	 * Returns the layer indicated by the key. Searches all layered panes.
	 * 
	 * @see LayerManager#getLayer(Object)
	 */
	public IFigure getLayer( Object key )
	{
		if ( innerLayers == null )
			return null;
		IFigure layer = innerLayers.getLayer( key );
		if ( layer != null )
			return layer;
		if ( printableLayers == null )
			return null;
		return printableLayers.getLayer( key );
	}

	/**
	 * Creates the top-most set of layers on the given layered pane.
	 * 
	 * @param layeredPane
	 *            the parent for the created layers
	 */
	protected void createLayers( LayeredPane layeredPane )
	{
		layeredPane.add( createGridLayer( ), GRID_LAYER );
		layeredPane.add( getPrintableLayers( ), PRINTABLE_LAYERS );
		layeredPane.add( new FreeformLayer( ), HANDLE_LAYER );
		layeredPane.add( new GuideLayer( ), GUIDE_LAYER );
	}

	/**
	 * Creates a {@link GridLayer grid}. Sub-classes can override this method
	 * to customize the appearance of the grid. The grid layer should be the
	 * first layer (i.e., beneath the primary layer) if it is not to cover up
	 * parts on the primary layer. In that case, the primary layer should be
	 * transparent so that the grid is visible.
	 * 
	 * @return the newly created GridLayer
	 */
	protected GridLayer createGridLayer( )
	{
		GridLayer grid = new TableGridLayer( this );
		grid.setOpaque( false );
		return grid;
	}

	/**
	 * Creates a layered pane and the layers that should be printed.
	 * 
	 * @see org.eclipse.gef.print.PrintGraphicalViewerOperation
	 * @return a new LayeredPane containing the printable layers
	 */
	protected LayeredPane createPrintableLayers( )
	{
		FreeformLayeredPane layeredPane = new FreeformLayeredPane( );
		FreeformLayer layer = new FreeformLayer( );

		layer.setLayoutManager( new TableLayout( this ) );
		layeredPane.add( layer, PRIMARY_LAYER );
		return layeredPane;
	}

	/**
	 * this layer may be a un-useful layer.
	 * 
	 * @return the layered pane containing all printable content
	 */
	protected LayeredPane getPrintableLayers( )
	{
		if ( printableLayers == null )
			printableLayers = createPrintableLayers( );
		return printableLayers;
	}

	/**
	 * Resets size of column.
	 * 
	 * @param start
	 * @param end
	 * @param value
	 */
	public void resizeColumn( int start, int end, int value )
	{
		Object startColumn = getColumn( start );
		ColumnHandleAdapter startAdapt = HandleAdapterFactory.getInstance( )
				.getColumnHandleAdapter( startColumn );

		Object endColumn = getColumn( end );
		ColumnHandleAdapter endAdapt = HandleAdapterFactory.getInstance( )
				.getColumnHandleAdapter( endColumn );
		int startWidth = 0;
		int endWidth = 0;

		startWidth = TableUtil.caleVisualWidth( this, startColumn );
		endWidth = TableUtil.caleVisualWidth( this, endColumn );

		try
		{
			getTableAdapter( ).transStar( RESIZE_COLUMN_TRANS_LABEL ); //$NON-NLS-1$
			startAdapt.setWidth( startWidth + value );
			endAdapt.setWidth( endWidth - value );
			getTableAdapter( ).transEnd( );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Selects the columns
	 * 
	 * @param numbers
	 */
	public void selectColumn( int[] numbers )
	{
		if ( numbers == null || numbers.length == 0 )
		{
			return;
		}
		ArrayList list = new ArrayList( );
		int size = numbers.length;
		int width = 0;

		int minColumnnumber = numbers[0];
		for ( int i = 0; i < size; i++ )
		{
			if ( minColumnnumber > numbers[i] )
			{
				minColumnnumber = numbers[i];
			}
			width = width
					+ TableUtil.caleVisualWidth( this, getColumn( numbers[i] ) );
			list.add( new DummyColumnEditPart( getColumn( numbers[i] ) ) );
		}
		for ( int i = 0; i < size; i++ )
		{
			int rowNumber = getTableAdapter( ).getRowCount( );
			for ( int j = 0; j < rowNumber; j++ )
			{
				TableCellEditPart part = getCell( j + 1, numbers[i] );
				if ( part != null )
				{
					list.add( part );
				}
			}
		}

		int x = TableUtil.caleX( this, minColumnnumber );
		Rectangle rect = new Rectangle( x,
				0,
				width,
				getFigure( ).getBounds( ).height
						- ( getFigure( ).getInsets( ).top + getFigure( ).getInsets( ).bottom ) );

		setSelectRowAndColumnRect( rect );
		getViewer( ).setSelection( new StructuredSelection( list ) );
		setSelectRowAndColumnRect( null );
	}

	/**
	 * Resize the row.
	 * 
	 * @param start
	 * @param end
	 * @param value
	 */
	public void resizeRow( int start, int end, int value )
	{
		Object row = getRow( start );
		RowHandleAdapter adapt = HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( row );
		int rowHeight = 0;
		if ( adapt.isCustomHeight( ) )
		{
			rowHeight = adapt.getHeight( );
		}
		else
		{
			rowHeight = TableUtil.caleVisualHeight( this, row );
		}
		try
		{
			adapt.setHeight( rowHeight + value );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Selects rows
	 * 
	 * @param numbers
	 */
	public void selectRow( int[] numbers )
	{
		if ( numbers == null || numbers.length == 0 )
		{
			return;
		}
		ArrayList list = new ArrayList( );
		int size = numbers.length;
		int height = 0;
		int minRownumber = numbers[0];

		//add row object in the list first
		for ( int i = 0; i < size; i++ )
		{

			if ( minRownumber > numbers[i] )
			{
				minRownumber = numbers[i];
			}
			height = height
					+ TableUtil.caleVisualHeight( this, getRow( numbers[i] ) );
			list.add( new DummyRowEditPart( getRow( numbers[i] ) ) );
		}

		for ( int i = 0; i < size; i++ )
		{
			int columnNumber = getTableAdapter( ).getColumnCount( );
			for ( int j = 0; j < columnNumber; j++ )
			{
				TableCellEditPart part = getCell( numbers[i], j + 1 );

				if ( part != null )
				{
					list.add( part );
				}
			}
		}

		int y = TableUtil.caleY( this, minRownumber );
		Rectangle rect = new Rectangle( 0,
				y,
				getFigure( ).getBounds( ).width
						- ( getFigure( ).getInsets( ).left + getFigure( ).getInsets( ).right ),
				height );

		setSelectRowAndColumnRect( rect );
		getViewer( ).setSelection( new StructuredSelection( list ) );
		setSelectRowAndColumnRect( null );
	}

	/**
	 * Get mini height of row.
	 * 
	 * @param rowNumber
	 * @return the minimum height of row.
	 */
	public int getMinHeight( int rowNumber )
	{
		return Math.max( TableUtil.getMinHeight( this, rowNumber ),
				getTableAdapter( ).getMinHeight( rowNumber ) );
	}

	/**
	 * Get mini width of column.
	 * 
	 * @param columnNumber
	 * @return the minimum height of column.
	 */
	public int getMinWidth( int columnNumber )
	{
		return Math.max( TableUtil.getMinWidth( this, columnNumber ),
				getTableAdapter( ).getMinWidth( columnNumber ) );
	}

	/**
	 * The contents' Figure will be added to the PRIMARY_LAYER.
	 * 
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane( )
	{
		return getLayer( PRIMARY_LAYER );
	}

	/**
	 * @return the table adapter
	 */
	protected TableHandleAdapter getTableAdapter( )
	{
		return (TableHandleAdapter) getModelAdapter( );
	}

	/**
	 * Get all rows list
	 * 
	 * @return all rows list.
	 */
	public List getRows( )
	{
		return getTableAdapter( ).getRows( );
	}

	/**
	 * @param number
	 *            a row position
	 * @return a specific row.
	 */
	public Object getRow( int number )
	{
		return getTableAdapter( ).getRow( number );
	}

	/**
	 * @param number
	 *            a column position
	 * @return a specific column.
	 */
	public Object getColumn( int number )
	{
		return getTableAdapter( ).getColumn( number );
	}

	/**
	 * Gets all columns list
	 * 
	 * @return all columns list.
	 */
	public List getColumns( )
	{
		return getTableAdapter( ).getColumns( );
	}

	/**
	 * Gets the rows count
	 * 
	 * @return row count
	 */
	public int getRowCount( )
	{
		return getTableAdapter( ).getRowCount( );
	}

	/**
	 * Gets the columns count
	 * 
	 * @return column count
	 */
	public int getColumnCount( )
	{
		return getTableAdapter( ).getColumnCount( );
	}

	/**
	 * @return select bounds
	 */
	public Rectangle getSelectBounds( )
	{
		if ( getSelectRowAndColumnRect( ) != null )
		{
			return getSelectRowAndColumnRect( );
		}
		List list = TableUtil.getSelectionCells( this );
		int size = list.size( );
		TableCellEditPart[] parts = new TableCellEditPart[size];
		list.toArray( parts );

		TableCellEditPart[] caleNumber = getMinAndMaxNumber( parts );
		TableCellEditPart minRow = caleNumber[0];
		TableCellEditPart maxColumn = caleNumber[3];

		Rectangle min = minRow.getBounds( ).getCopy( );
		Rectangle max = maxColumn.getBounds( ).getCopy( );

		return min.union( max );
	}

	/**
	 * @return selected row and column area
	 */
	public Rectangle getSelectRowAndColumnRect( )
	{
		return selectRowAndColumnRect;
	}

	/**
	 * Set selected row and column area.
	 * 
	 * @param selectRowAndColumnRect
	 */
	public void setSelectRowAndColumnRect( Rectangle selectRowAndColumnRect )
	{
		this.selectRowAndColumnRect = selectRowAndColumnRect;
	}

	/**
	 * Gets data set, which is biding on table.
	 *  
	 */
	public Object getDataSet( )
	{
		return getTableAdapter( ).getDataSet( );
	}

	/**
	 * Get the cell on give position.
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 */
	public TableCellEditPart getCell( int rowNumber, int columnNumber )
	{
		Object cell = getTableAdapter( ).getCell( rowNumber, columnNumber );
		return (TableCellEditPart) getViewer( ).getEditPartRegistry( )
				.get( cell );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#removeChild(org.eclipse.gef.EditPart)
	 */
	protected void removeChild( EditPart child )
	{
		super.removeChild( child );
	}

	/**
	 * Delete specified row.
	 * 
	 * @param numbers
	 */
	public void deleteRow( int[] numbers )
	{
		try
		{
			getTableAdapter( ).deleteRow( numbers );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Delete specified column
	 * 
	 * @param numbers
	 */
	public void deleteColumn( int[] numbers )
	{
		try
		{
			getTableAdapter( ).deleteColumn( numbers );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * inserts a row after the row number
	 * 
	 * @param rowNumber
	 */
	public void insertRow( int rowNumber )
	{
		insertRow( -1, rowNumber );
	}

	/**
	 * Inserts row at give position.
	 * 
	 * @param rowNumber
	 * @param parentRowNumber
	 */
	public void insertRow( final int rowNumber, final int parentRowNumber )
	{
		final RowHandleAdapter adapter = HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( getRow( parentRowNumber ) );
		try
		{
			getTableAdapter( ).insertRow( rowNumber, parentRowNumber );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}

		Display.getCurrent( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				//reLayout();
				selectRow( new int[]{
					adapter.getRowNumber( )
				} );
			}
		} );
	}

	/**
	 * Inserts a row after the row number
	 * 
	 * @param columnNumber
	 */
	public void insertColumn( int columnNumber )
	{
		insertColumn( -1, columnNumber );
	}

	/**
	 * Inserts column on give position.
	 * 
	 * @param rowNumber
	 * @param parentRowNumber
	 */
	public void insertColumn( final int rowNumber, final int parentRowNumber )
	{
		final ColumnHandleAdapter adapter = HandleAdapterFactory.getInstance( )
				.getColumnHandleAdapter( getColumn( parentRowNumber ) );
		try
		{
			getTableAdapter( ).insertColumn( rowNumber, parentRowNumber );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}

		Display.getCurrent( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				//reLayout();
				selectColumn( new int[]{
					adapter.getColumnNumber( )
				} );
			}
		} );
	}

	/**
	 * merge the selection cell
	 */
	public void merge( )
	{
		List selections = TableUtil.getSelectionCells( this );
		if ( selections.size( ) == 1 )
		{
			return;
		}
		int size = selections.size( );
		TableCellEditPart[] parts = new TableCellEditPart[size];
		selections.toArray( parts );

		TableCellEditPart[] caleNumber = getMinAndMaxNumber( parts );
		TableCellEditPart minRow = caleNumber[0];

		TableCellEditPart maxRow = caleNumber[2];
		TableCellEditPart maxColumn = caleNumber[3];

		TableCellEditPart cellPart = caleNumber[0];
		ArrayList list = new ArrayList( );
		//first is the contain cell(minrow, minColumn)
		for ( int i = 0; i < size; i++ )
		{
			if ( selections.get( i ) != cellPart )
			{
				list.add( selections.get( i ) );
			}
		}

		int rowSpan = maxRow.getRowNumber( )
				- minRow.getRowNumber( ) + maxRow.getRowSpan( );
		int colSpan = maxColumn.getColumnNumber( )
				- maxRow.getColumnNumber( ) + maxColumn.getColSpan( );

		getTableAdapter( ).transStar( MERGE_TRANS_LABEL );
		cellPart.setRowSpan( rowSpan );
		cellPart.setColumnSpan( colSpan );

		removeMergeList( list );
		getTableAdapter( ).transEnd( );
		getViewer( ).setSelection( new StructuredSelection( cellPart ) );
		getTableAdapter( ).reload( );
	}

	/**
	 * not use?
	 * 
	 * @param list
	 */
	private void removeMergeList( ArrayList list )
	{

		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			remove( (TableCellEditPart) list.get( i ) );
		}
	}

	/**
	 * not use?
	 * 
	 * @param cellPart
	 */
	public void remove( TableCellEditPart cellPart )
	{
		try
		{
			getTableAdapter( ).removeChild( cellPart.getModel( ) );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.IModelAdaptHelper#getPreferredSize()
	 */
	public Dimension getPreferredSize( )
	{
		Dimension retValue = getFigure( ).getParent( )
				.getClientArea( )
				.getSize( );
		Rectangle rect = getBounds( );

		if ( rect.width > 0 )
		{
			retValue.width = rect.width;
		}
		if ( rect.height > 0 )
		{
			retValue.height = rect.height;
		}
		return retValue;
	}

	protected void notifyChildrenDirty( boolean bool )
	{
		super.notifyChildrenDirty( bool );

		if ( bool )
		{
			reLayout( );
			( (TableLayout) getContentPane( ).getLayoutManager( ) ).markDirty( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#markDirty(boolean)
	 */
	public void markDirty( boolean bool, boolean notifyParent )
	{
		super.markDirty( bool, notifyParent );
		if ( bool )
		{
			( (TableLayout) getContentPane( ).getLayoutManager( ) ).markDirty( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.facade.ITableAdaptHelper#caleVisualWidth(int)
	 */
	public int caleVisualWidth( int columnNumber )
	{
		assert columnNumber > 0;
		return TableUtil.caleVisualWidth( this, getColumn( columnNumber ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.facade.ITableAdaptHelper#caleVisualHeight(int)
	 */
	public int caleVisualHeight( int rowNumber )
	{
		return TableUtil.caleVisualHeight( this, getRow( rowNumber ) );
	}

	/**
	 * Determines if selected cells can be merged.
	 * 
	 * @return true if merge success, else false.
	 */
	public boolean canMerge( )
	{
		List list = TableUtil.getSelectionCells( this );
		int size = list.size( );
		List temp = new ArrayList( );
		for ( int i = 0; i < size; i++ )
		{
			temp.add( ( (EditPart) list.get( i ) ).getModel( ) );
		}
		return getTableAdapter( ).canMerge( temp );
	}

	/**
	 * Split merged cells
	 * 
	 * @param part
	 */
	public void splitCell( TableCellEditPart part )
	{
		try
		{
			getTableAdapter( ).splitCell( part.getModel( ) );
		}
		catch ( ContentException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( NameException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * @param bool
	 * @param id
	 */
	public void includeSlotHandle( boolean bool, int id )
	{
		try
		{
			if ( bool )
			{
				getTableAdapter( ).insertRowInSlotHandle( id );
			}
			else
			{
				getTableAdapter( ).deleteRowInSlotHandle( id );
			}
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Inserts group in table.
	 */
	public boolean insertGroup( )
	{
		return UIUtil.createGroup( getTableAdapter( ).getHandle( ) );
	}

	/**
	 * Inserts group in table.
	 * 
	 * @param part
	 *            the current row or cell to specify the position of new group.
	 *            Null to call <code>insertGroup( )</code>
	 */
	public boolean insertGroup( Object part )
	{
		RowHandle row = null;
		if ( part != null )
		{
			if ( part instanceof RowHandle )
			{
				row = (RowHandle) part;
			}
			else if ( part instanceof CellHandle )
			{
				row = (RowHandle) ( (CellHandle) part ).getContainer( );
			}
		}
		if ( row != null )
		{
			return UIUtil.createTableGroup( row );
		}
		return insertGroup( );
	}

	/**
	 * Removes group in table
	 * 
	 * @param group
	 */
	public void removeGroup( Object group )
	{
		try
		{
			( (TableHandleAdapter) getModelAdapter( ) ).removeGroup( group );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}

	protected void addListenerToChildren( )
	{
		addGroupListener( );
		addRowListener( );
		addColumnListener( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.ITableAdaptHelper#getClientAreaSize()
	 */
	public Dimension getClientAreaSize( )
	{
		return getFigure( ).getParent( ).getClientArea( ).getSize( );
	}

	/**
	 *  
	 */
	protected void addGroupListener( )
	{
		for ( Iterator it = ( (TableHandle) getModel( ) ).getGroups( )
				.iterator( ); it.hasNext( ); )
		{
			( (DesignElementHandle) it.next( ) ).addListener( this );
		}
	}

	public void showTargetFeedback( Request request )
	{
		if ( this.getSelected( ) == 0
				&& isActive( )
				&& request.getType( ) == RequestConstants.REQ_SELECTION )
		{

			if ( isFigureLeft( request ) )
			{
				this.getViewer( ).setCursor( ReportPlugin.getDefault( )
						.getLeftCellCursor( ) );
			}
			else
			{
				this.getViewer( ).setCursor( ReportPlugin.getDefault( )
						.getRightCellCursor( ) );
			}
		}
		super.showTargetFeedback( request );
	}

	public void eraseTargetFeedback( Request request )
	{
		if ( isActive( ) )
		{
			this.getViewer( ).setCursor( null );
		}
		super.eraseTargetFeedback( request );
	}

	protected void addChildVisual( EditPart part, int index )
	{
		// make sure we don't keep a select cell cursor after new contents
		// are added
		this.getViewer( ).setCursor( null );
		super.addChildVisual( part, index );
	}

	/**
	 * The class use for select row in table.
	 *  
	 */
	public static class DummyColumnEditPart extends DummyEditpart
	{

		/**
		 * @param model
		 */
		public DummyColumnEditPart( Object model )
		{
			super( model );
			createEditPolicies( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart#createEditPolicies()
		 */
		protected void createEditPolicies( )
		{

			ReportComponentEditPolicy policy = new ReportComponentEditPolicy( ) {

				protected org.eclipse.gef.commands.Command createDeleteCommand(
						GroupRequest deleteRequest )
				{
					DeleteColumnCommand command = new DeleteColumnCommand( getModel( ) );
					return command;
				}
			};
			installEditPolicy( EditPolicy.COMPONENT_ROLE, policy );
		}

		public int getColumnNumber( )
		{
			return HandleAdapterFactory.getInstance( )
					.getColumnHandleAdapter( getModel( ) )
					.getColumnNumber( );
		}
	}

	/**
	 * The class use for select row in table.
	 *  
	 */
	public static class DummyRowEditPart extends DummyEditpart
	{

		/**
		 * @param model
		 */
		public DummyRowEditPart( Object model )
		{
			super( model );
			createEditPolicies( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart#createEditPolicies()
		 */
		protected void createEditPolicies( )
		{
			ReportComponentEditPolicy policy = new ReportComponentEditPolicy( ) {

				protected org.eclipse.gef.commands.Command createDeleteCommand(
						GroupRequest deleteRequest )
				{
					DeleteRowCommand command = new DeleteRowCommand( getModel( ) );
					return command;
				}
			};
			installEditPolicy( EditPolicy.COMPONENT_ROLE, policy );
		}

		public int getRowNumber( )
		{
			return HandleAdapterFactory.getInstance( )
					.getRowHandleAdapter( getModel( ) )
					.getRowNumber( );
		}
	}
}