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

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.core.model.schematic.CellHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.IBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.TableCellBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.ISelectionFilter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.CellBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.CellFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.TableCellDragHandle;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.bidi.BidiUIUtils;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

/**
 * <p>
 * Table cell element editPart
 * </p>
 *  
 */
public class TableCellEditPart extends AbstractCellEditPart
{
	/**
	 * The all drag column and row handle
	 */
	private List handles = null;
	/**
	 * Constructor
	 * 
	 * @param obj
	 */
	public TableCellEditPart( Object obj )
	{
		super( obj );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{
		return getCellAdapter( ).getChildren( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		CellFigure figure = new CellFigure( );
		ReportFlowLayout rflayout = new ReportFlowLayout( );
		figure.setLayoutManager( rflayout );
		figure.setOpaque( false );

		return figure;
	}

	protected Dimension getCellDimension( )
	{
		int h = 0;
		int w = 0;

		TableEditPart tablePart = (TableEditPart) getParent( );

		int rNumber = getRowNumber( );
		int cNumber = getColumnNumber( );

		if ( rNumber > 0 && cNumber > 0 )
		{
			for ( int i = rNumber; i < rNumber + getRowSpan( ); i++ )
			{
				h += tablePart.caleVisualHeight( i );
			}

			for ( int j = cNumber; j < cNumber + getColSpan( ); j++ )
			{
				w += tablePart.caleVisualWidth( j );
			}
		}
		return new Dimension( w, h );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.editparts.AbstractReportEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		CellBorder cborder = new CellBorder( );

		if ( getFigure( ).getBorder( ) instanceof CellBorder )
		{
			cborder.setBorderInsets( ( (CellBorder) getFigure( ).getBorder( ) )
					.getBorderInsets( ) );
		}
		refreshBorder( getCellAdapter( ).getHandle( ), cborder );

		Insets ist = getCellAdapter( ).getPadding( getFigure( ).getInsets( ) );

		( (CellBorder) ( getFigure( ).getBorder( ) ) ).setPaddingInsets( ist );

		setTextAliment( ( (CellHandle) getModel( ) ).getPrivateStyle( ) );

		( (CellFigure) getFigure( ) ).setDirectionRTL( BidiUIUtils
				.INSTANCE.isDirectionRTL( getModel( ) ) ); // bidi_hcg

		updateBlankString( );

		refreshBackground( (DesignElementHandle) getModel( ) );
	}
	
	/**
	 * Draws the string when the cell is empty
	 */
	public void updateBlankString( )
	{
		if ( 0 == getModelChildren( ).size( ) && 1 == getColumnNumber( ) )
		{
			TableHandleAdapter tha = ( (TableEditPart) getParent( ) )
					.getTableAdapter( );
	
			RowHandleAdapter rha = HandleAdapterFactory.getInstance( )
					.getRowHandleAdapter( tha.getRow( getRowNumber( ) ) );

			String type = rha.getType( );
			Object obj = rha.getHandle( ).getContainer( );
			
			if ( (TableHandleAdapter.TABLE_GROUP_HEADER.equals( type )
					|| TableHandleAdapter.TABLE_GROUP_FOOTER.equals( type ))
					&&  rha.getHandle( ).getContainer( ) instanceof TableGroupHandle)
			{
				String name = null;
				try
				{
					name = ExpressionUtil.getColumnBindingName( ( (TableGroupHandle) obj ).getKeyExpr( ) );
				}
				catch ( BirtException e )
				{
				}
				if ( name != null )
				{
					( (CellFigure) getFigure( ) ).setBlankString( rha.getTypeString( )
							+ " (" //$NON-NLS-1$
							+ name
							+ ")" ); //$NON-NLS-1$
					return;
				}
			}
			( (CellFigure) getFigure( ) ).setBlankString( rha.getTypeString( ) );	
		}
		else
		{
			( (CellFigure) getFigure( ) ).setBlankString( null );
		}
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
		installEditPolicy( EditPolicy.LAYOUT_ROLE,
				new ReportFlowLayoutEditPolicy( ) );
		installEditPolicy( EditPolicy.CONTAINER_ROLE,
				new ReportContainerEditPolicy( ) );
	}


	/**
	 * Gets the row number
	 * 
	 * @return the row number
	 */
	public int getRowNumber( )
	{
		return getCellAdapter( ).getRowNumber( );
	}

	/**
	 * Gets the column number
	 * 
	 * @return the column number
	 */
	public int getColumnNumber( )
	{
		return getCellAdapter( ).getColumnNumber( );
	}

	/**
	 * Gets the column span
	 * 
	 * @return the column span
	 */
	public int getColSpan( )
	{
		return getCellAdapter( ).getColumnSpan( );
	}

	/**
	 * Sets the column span
	 * 
	 * @param colSpan
	 */
	public void setColumnSpan( int colSpan )
	{
		try
		{
			getCellAdapter( ).setColumnSpan( colSpan );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Gets the row span
	 * 
	 * @return the row span
	 */
	public int getRowSpan( )
	{
		return getCellAdapter( ).getRowSpan( );
	}

	/**
	 * Sets the row span
	 * 
	 * @param rowSpan
	 */
	public void setRowSpan( int rowSpan )
	{
		try
		{
			getCellAdapter( ).setRowSpan( rowSpan );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart( Request request )
	{
		return super.getTargetEditPart( request );
	}


	protected CellHandleAdapter getCellAdapter( )
	{
		return (CellHandleAdapter) getModelAdapter( );
	}

	public void showTargetFeedback( Request request )
	{
		if ( this.getSelected( ) == 0 && isActive( )
				&& request.getType( ) == RequestConstants.REQ_SELECTION )
		{

			if ( isFigureLeft( request ) )
			{
				this.getViewer( ).setCursor(
						ReportPlugin.getDefault( ).getLeftCellCursor( ) );
			}
			else
			{
				this.getViewer( ).setCursor(
						ReportPlugin.getDefault( ).getRightCellCursor( ) );
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

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#updateBaseBorder(org.eclipse.birt.report.model.api.DesignElementHandle, org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder)
	 */
	//now suport the row border, so interest the bottom and top.
	protected void updateBaseBorder( DesignElementHandle handle,
			BaseBorder border )
	{
		super.updateBaseBorder( handle, border );
		DesignElementHandle parent = ((DesignElementHandle) getModel( )).getContainer( );
		if (DesignChoiceConstants.LINE_STYLE_NONE.equals( border.bottomStyle))
		{
			updateBottomBorder( parent, border );
			if ( border instanceof CellBorder )
			{
				((CellBorder)border).setBottomFrom( CellBorder.FROM_ROW );
			}
		}
		else
		{
			if ( border instanceof CellBorder )
			{
				((CellBorder)border).setBottomFrom( CellBorder.FROM_CELL );
			}
		}
		if (DesignChoiceConstants.LINE_STYLE_NONE.equals( border.topStyle))
		{
			updateTopBorder( parent, border );
			if ( border instanceof CellBorder )
			{
				((CellBorder)border).setTopFrom( CellBorder.FROM_ROW );
			}
		}
		else
		{
			if ( border instanceof CellBorder )
			{
				((CellBorder)border).setTopFrom( CellBorder.FROM_CELL );
			}
		}
		if (DesignChoiceConstants.LINE_STYLE_NONE.equals( border.leftStyle))
		{
			if (getColumnNumber( ) == 1)
			{
				updateLeftBorder( parent, border );
			}
		}
		
		if (DesignChoiceConstants.LINE_STYLE_NONE.equals( border.rightStyle))
		{
			if (getColumnNumber( )+getColSpan( ) ==  ((TableEditPart)getParent( )).getColumnCount( ) + 1)
			{
				updateRightBorder( parent, border );
			}
		}
	}
	
	public Object getAdapter( Class key )
	{
		if (key == ISelectionFilter.class)
		{
			return new ISelectionFilter()
			{
				public List filterEditpart( List editparts )
				{
					int size = editparts.size( );
					List copy = new ArrayList( editparts );
					for ( int i = 0; i < size; i++ )
					{
						EditPart part = (EditPart) editparts.get( i );
						if ( part instanceof AreaEditPart)
						{
							copy.remove( part );
						} 
					}
					boolean hasCell = false;
					boolean hasOther = false;
					for ( int i = 0; i < size; i++ )
					{
						Object obj = ( (EditPart) editparts.get( i ) ).getModel( );
						if ( obj instanceof CellHandle
								|| obj instanceof RowHandle
								|| obj instanceof ColumnHandle )
						{
							hasCell = true;
						}
						else
						{
							hasOther = true;
						}
					}
					if ( hasCell && hasOther )
					{
						
						for ( int i = 0; i < size; i++ )
						{
							EditPart part = (EditPart) editparts.get( i );
							Object obj = part.getModel( );

							if ( obj instanceof CellHandle
									|| obj instanceof RowHandle
									|| obj instanceof ColumnHandle )
							{
								copy.remove( part );
							} 
						}
					}
					//editparts = copy;
					//move the above logic to the TableCellEditPart?
					
					editparts = copy;
					return editparts;
				}
				
			};
		}
		if(key == IBreadcrumbNodeProvider.class){
			return new TableCellBreadcrumbNodeProvider( );
		}
		return super.getAdapter( key );
	}
	
	/**
	 * Gets the column and rwo drag handle
	 * 
	 * @return
	 */
	protected List getHandleList( )
	{
		List retValue = new ArrayList( );
		TableEditPart parent = (TableEditPart) getParent( );

		int columnNumner = parent.getColumnCount( );
		int rowNumer = parent.getRowCount( );
		if ( getColumnNumber( ) + getColSpan( ) - 1 < columnNumner )
		{
			TableCellDragHandle column = new TableCellDragHandle( this,
					PositionConstants.EAST,
					getColumnNumber( ) + getColSpan( ) - 1,
					getColumnNumber( ) + getColSpan( ) );
			retValue.add( column );
		}
		else
		{
			TableCellDragHandle column = new TableCellDragHandle( this,
					PositionConstants.EAST,
					getColumnNumber( ) + getColSpan( ) - 1,
					getColumnNumber( ) + getColSpan( ) - 1);
			retValue.add( column );
		}
		if ( getRowNumber( ) + getRowSpan( ) - 1 < rowNumer )
		{
			TableCellDragHandle row = new TableCellDragHandle( this,
					PositionConstants.SOUTH,
					getRowNumber( ) + getRowSpan( ) - 1,
					getRowNumber( ) + getRowSpan( ) );
			retValue.add( row );
		}
		else
		{
			TableCellDragHandle row = new TableCellDragHandle( this,
					PositionConstants.SOUTH,
					getRowNumber( ) + getRowSpan( ) - 1,
					getRowNumber( ) + getRowSpan( ) - 1);
			retValue.add( row );
		}
		return retValue;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#activate()
	 */
	public void activate( )
	{
		if ( handles == null )
		{
			handles = getHandleList( );
		}
		// IFigure layer = getLayer( CrosstabTableEditPart.CELL_HANDLE_LAYER );
		IFigure layer = getLayer( LayerConstants.HANDLE_LAYER );
		int size = handles.size( );
		for ( int i = 0; i < size; i++ )
		{
			Figure handle = (Figure) handles.get( i );
			layer.add( handle );
		}
		super.activate( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#deactivate()
	 */
	public void deactivate( )
	{
		// IFigure layer = getLayer( CrosstabTableEditPart.CELL_HANDLE_LAYER );
		IFigure layer = getLayer( LayerConstants.HANDLE_LAYER );
		int size = handles.size( );
		for ( int i = 0; i < size; i++ )
		{
			Figure handle = (Figure) handles.get( i );
			layer.remove( handle );
		}
		super.deactivate( );
	}
	
	@Override
	protected void updateExistPart( )
	{
		IFigure layer = getLayer( LayerConstants.HANDLE_LAYER );
		int size = handles.size( );
		for ( int i = 0; i < size; i++ )
		{
			Figure handle = (Figure) handles.get( i );
			layer.remove( handle );
		}
		
		handles = getHandleList();
		
		size = handles.size( );
		for ( int i = 0; i < size; i++ )
		{
			Figure handle = (Figure) handles.get( i );
			layer.add( handle );
		}
	}
}