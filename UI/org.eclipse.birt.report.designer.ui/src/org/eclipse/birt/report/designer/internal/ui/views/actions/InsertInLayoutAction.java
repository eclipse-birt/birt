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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.ReportDesigner;
import org.eclipse.birt.report.designer.internal.ui.util.DataSetManager;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.ReportEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

/**
 * Action to insert object to layout.
 * <p>
 * Can use run() method of instance or use static method
 * <code>insertSingleInsert()</code> to new a object
 * <p>
 */

public class InsertInLayoutAction extends AbstractViewAction
{

	/**
	 * Rule interface for defining insertion rule
	 */
	abstract static interface InsertInLayoutRule
	{

		public boolean canInsert( );

		public DesignElementHandle getInsertPosition( );
	}

	/**
	 * 
	 * Rule for inserting column
	 */
	static class InsertColumnInLayoutRule implements InsertInLayoutRule
	{

		private CellHandle cell;

		private DesignElementHandle target;

		private static final int[] SUPPORTED_DATA_SLOT = new int[]{
			TableItem.DETAIL_SLOT
		};

		public InsertColumnInLayoutRule( CellHandle cell )
		{
			this.cell = cell;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.InsertInLayoutAction.InsertInLayoutRule#canInsert()
		 */
		public boolean canInsert( )
		{
			//Validates slot id of date item
			int tableSlotId = cell.getContainer( )
					.getContainerSlotHandle( )
					.getSlotID( );
			boolean canInsert = false;
			for ( int i = 0; i < SUPPORTED_DATA_SLOT.length; i++ )
			{
				if ( tableSlotId == SUPPORTED_DATA_SLOT[i] )
				{
					canInsert = true;
					break;
				}
			}

			//Validates column count and gets the target
			if ( canInsert )
			{
				TableHandle table = (TableHandle) cell.getContainer( )
						.getContainer( );
				SlotHandle header = table.getHeader( );
				if ( header != null && header.getCount( ) > 0 )
				{
					int columnNum = HandleAdapterFactory.getInstance( )
							.getCellHandleAdapter( cell )
							.getColumnNumber( );
					target = (CellHandle) HandleAdapterFactory.getInstance( )
							.getTableHandleAdapter( table )
							.getCell( 1, columnNum, false );
					return target != null
							&& ( (CellHandle) target ).getContent( ).getCount( ) == 0;
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.InsertInLayoutAction.InsertInLayoutRule#getInsertPosition()
		 */
		public DesignElementHandle getInsertPosition( )
		{
			return target;
		}

	}

	public static final String DISPLAY_TEXT = Messages.getString( "InsertInLayoutAction.action.text" ); //$NON-NLS-1$

	protected List selections, targets;

	/**
	 * Constructor. Uses DISPLAY_TEXT as default text.
	 * 
	 * @param selectedObject
	 */
	public InsertInLayoutAction( Object selectedObject )
	{
		this( selectedObject, DISPLAY_TEXT );
	}

	/**
	 *  
	 */
	public InsertInLayoutAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isEnabled()
	 */
	public boolean isEnabled( )
	{
		selections = getSelectionObject( );
		ReportEditor reportEditor = (ReportEditor) PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( )
				.getActivePage( )
				.getActiveEditor( );

		if ( !( reportEditor.getActiveEditor( ) instanceof ReportDesigner ) )
			return false;

		targets = ( (ReportDesigner) reportEditor.getActiveEditor( ) ).getGraphicalViewer( )
				.getSelectedEditParts( );

		if ( selections.isEmpty( ) || targets.isEmpty( ) )
			return false;
		for ( Iterator i = selections.iterator( ); i.hasNext( ); )
		{
			Object insertObj = i.next( );
			if ( handleValidateInsert( insertObj ) )
			{
				for ( Iterator j = targets.iterator( ); j.hasNext( ); )
				{
					if ( !handleValidateInsertToLayout( insertObj,
							(EditPart) j.next( ) ) )
						return false;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		for ( Iterator i = selections.iterator( ); i.hasNext( ); )
		{
			Object insertObj = i.next( );
			for ( Iterator j = targets.iterator( ); j.hasNext( ); )
			{
				EditPart targetPart = (EditPart) j.next( );
				Object newElement = runSingleInsert( insertObj, targetPart );
				if ( newElement != null )
					runCreate( newElement, targetPart.getModel( ) );
			}
		}
	}

	/**
	 * Creates a object, "Add" operation to layout needs to handle later.
	 * <p>
	 * Must make sure operation legal before execution.
	 * </p>
	 * 
	 * @param singleInsertObj
	 *            object insert to layout
	 * @param targetPart
	 *            edit part in layout
	 * @return new object in layout
	 */
	public static Object runSingleInsert( Object singleInsertObj,
			EditPart targetPart )
	{
		if ( singleInsertObj instanceof DataSetHandle )
		{
			return runInsertDataSet( (DataSetHandle) singleInsertObj,
					targetPart );
		}
		else if ( singleInsertObj instanceof DataSetItemModel )
		{
			return runInsertDataSetColumn( (DataSetItemModel) singleInsertObj,
					targetPart );
		}
		else if ( singleInsertObj instanceof ScalarParameterHandle )
		{
			return runInsertParameter( (ScalarParameterHandle) singleInsertObj,
					targetPart );
		}
		return null;
	}

	/**
	 * @param targetPart
	 */
	private static Object runInsertParameter( ScalarParameterHandle model,
			EditPart targetPart )
	{
		DataItemHandle dataHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getElementFactory( )
				.newDataItem( null );
		try
		{
			dataHandle.setValueExpr( DEUtil.getExpression( model ) );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		return dataHandle;
	}

	/**
	 * @param model
	 * @param targetPart
	 */
	private static Object runInsertDataSetColumn( DataSetItemModel model,
			EditPart targetPart )
	{
		DataItemHandle dataHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getElementFactory( )
				.newDataItem( null );
		try
		{
			dataHandle.setValueExpr( DEUtil.getExpression( model ) );
			ReportItemHandle container = (ReportItemHandle) targetPart.getParent( )
					.getModel( );
			DataSetHandle dataSet = (DataSetHandle) model.getParent( );
			if ( !DEUtil.getDataSetList( container ).contains( dataSet ) )
			{
				if ( container.getDataSet( ) == null )
				{
					container.setDataSet( dataSet );
				}
			}

			InsertColumnInLayoutRule rule = new InsertColumnInLayoutRule( (CellHandle) targetPart.getModel( ) );
			if ( rule.canInsert( ) )
			{
				LabelHandle label = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getElementFactory( )
						.newLabel( null );
				label.setText( model.getDisplayName( ) );
				rule.getInsertPosition( ).addElement( label, Cell.CONTENT_SLOT );
			}
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		return dataHandle;
	}

	/**
	 * @param targetPart
	 */
	private static Object runInsertDataSet( DataSetHandle model,
			EditPart targetPart )
	{
		DataSetItemModel[] columns = DataSetManager.getCurrentInstance( )
				.getColumns( model, false );
		TableHandle tableHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getElementFactory( )
				.newTableItem( null, columns.length );
		insertToCell( tableHandle.getHeader( ), columns, true );
		insertToCell( tableHandle.getDetail( ), columns, false );

		try
		{
			tableHandle.setDataSet( model );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		return tableHandle;
	}

	/**
	 * Validates object can be inserted to layout.
	 * <p>
	 * This method disables multi-selection since menu and drop listener do not
	 * support now.
	 * </p>
	 * 
	 * @param insertObj
	 *            single inserted object or multi-object form as array
	 * @param targetPart
	 * @return if can be inserted to layout
	 */
	public static boolean handleValidateInsertToLayout( Object insertObj,
			EditPart targetPart )
	{
		if ( insertObj instanceof Object[] )
		{
			Object[] array = (Object[]) insertObj;
			if ( array.length > 1 )
				return false;
			for ( int i = 0; i < array.length; i++ )
				return handleValidateInsertToLayout( array[i], targetPart );
		}

		else if ( insertObj instanceof DataSetHandle )
		{
			return handleValidateDataSet( targetPart );
		}
		else if ( insertObj instanceof DataSetItemModel )
		{
			return handleValidateDataSetColumn( (DataSetItemModel) insertObj,
					targetPart );
		}
		else if ( insertObj instanceof ScalarParameterHandle )
		{
			return handleValidateParameter( targetPart );
		}
		return false;
	}

	/**
	 * Validates container of drop target from data set in data view
	 * 
	 * @param dropPart
	 * @return validate result
	 */
	protected static boolean handleValidateDataSetDropContainer(
			EditPart dropPart )
	{
		Object container = dropPart.getParent( ).getModel( );
		return ( container instanceof GridHandle
				|| container instanceof TableHandle
				|| container instanceof FreeFormHandle
				|| container instanceof ListHandle || dropPart.getModel( ) instanceof ReportDesignHandle );
	}

	/**
	 * Validates container of drop target from data set column in data view
	 * 
	 * @param dropPart
	 * @return validate result
	 */
	protected static boolean handleValidateDataSetColumnDropContainer(
			EditPart dropPart )
	{
		Object container = dropPart.getParent( ).getModel( );
		return ( container instanceof GridHandle
				|| container instanceof TableHandle || container instanceof FreeFormHandle );
	}

	/**
	 * Validates container of drop target from scalar parameter in data view
	 * 
	 * @param dropPart
	 * @return validate result
	 */
	protected static boolean handleValidateParameterDropContainer(
			EditPart dropPart )
	{
		Object container = dropPart.getParent( ).getModel( );
		return ( container instanceof GridHandle
				|| container instanceof TableHandle
				|| container instanceof FreeFormHandle
				|| container instanceof ListHandle || dropPart.getModel( ) instanceof ReportDesignHandle );
	}

	/**
	 * Validates drop target from data set in data view.
	 * 
	 * @return validate result
	 */
	protected static boolean handleValidateDataSet( EditPart target )
	{
		return handleValidateDataSetDropContainer( target )
				&& DNDUtil.handleValidateTargetCanContainType( target.getModel( ),
						ReportDesignConstants.TABLE_ITEM );
	}

	/**
	 * Validates drop target from data set column in data view.
	 * 
	 * @return validate result
	 */
	protected static boolean handleValidateDataSetColumn(
			DataSetItemModel insertObj, EditPart target )
	{
		if ( handleValidateDataSetColumnDropContainer( target )
				&& DNDUtil.handleValidateTargetCanContainType( target.getModel( ),
						ReportDesignConstants.DATA_ITEM ) )
		{
			DesignElementHandle handle = (DesignElementHandle) target.getParent( )
					.getModel( );
			if ( handle instanceof ReportItemHandle
					&& ( (ReportItemHandle) handle ).getDataSet( ) == null )
				return true;
			return DEUtil.getDataSetList( handle )
					.contains( insertObj.getParent( ) );
		}
		return false;
	}

	/**
	 * Validates drop target from scalar parameter in data view.
	 * 
	 * @return validate result
	 */
	protected static boolean handleValidateParameter( EditPart target )
	{
		return handleValidateParameterDropContainer( target )
				&& DNDUtil.handleValidateTargetCanContainType( target.getModel( ),
						ReportDesignConstants.DATA_ITEM );
	}

	/**
	 * Validates drag source from data view to layout
	 * 
	 * @return validate result
	 */
	public static boolean handleValidateInsert( Object insertObj )
	{
		if ( insertObj instanceof Object[] )
		{
			Object[] array = (Object[]) insertObj;
			for ( int i = 0; i < array.length; i++ )
			{
				if ( !handleValidateInsert( array[i] ) )
					return false;
			}
			return true;
		}
		return insertObj instanceof DataSetHandle
				|| insertObj instanceof DataSetItemModel
				|| insertObj instanceof ScalarParameterHandle;
	}

	protected List getSelectionObject( )
	{
		if ( getSelection( ) instanceof StructuredSelection )
		{
			StructuredSelection selection = (StructuredSelection) getSelection( );
			return selection.toList( );
		}
		List list = new ArrayList( );
		list.add( getSelection( ) );
		return list;
	}

	private void runCreate( Object insertedObj, Object container )
	{
		if ( container instanceof ListBandProxy )
		{
			container = ( (ListBandProxy) container ).getSlotHandle( );
		}
		HashMap map = new HashMap( );
		map.put( "newObject", insertedObj ); //$NON-NLS-1$
		CreateCommand command = new CreateCommand( map );
		command.setParent( container );
		command.execute( );
	}

	private static void insertToCell( SlotHandle slot,
			DataSetItemModel[] columns, boolean isLabel )
	{
		for ( int i = 0; i < slot.getCount( ); i++ )
		{
			SlotHandle cells = ( (RowHandle) slot.get( i ) ).getCells( );
			for ( int j = 0; j < cells.getCount( ); j++ )
			{
				CellHandle cell = (CellHandle) cells.get( j );

				try
				{
					if ( isLabel )
					{
						LabelHandle labelItemHandle = SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.getElementFactory( )
								.newLabel( null );
						labelItemHandle.setText( columns[j].getDisplayName( ) );
						cell.addElement( labelItemHandle, cells.getSlotID( ) );
					}
					else
					{
						DataItemHandle dataHandle = SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.getElementFactory( )
								.newDataItem( null );
						dataHandle.setValueExpr( DEUtil.getExpression( columns[j] ) );
						cell.addElement( dataHandle, cells.getSlotID( ) );
					}
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}
		}
	}
}