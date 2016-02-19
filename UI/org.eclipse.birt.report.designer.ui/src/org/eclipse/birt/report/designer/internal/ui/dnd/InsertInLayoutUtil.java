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

package org.eclipse.birt.report.designer.internal.ui.dnd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.CellHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataSetBindingSelector;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;

/**
 * Utility for creation from data view to layout
 */

public class InsertInLayoutUtil
{

	/**
	 * Rule interface for defining insertion rule
	 */
	abstract static interface InsertInLayoutRule
	{

		public boolean canInsert( );

		public Object getInsertPosition( );

		public void insert( Object object ) throws SemanticException;
	}

	/**
	 * 
	 * Rule for inserting label after inserting data set column
	 */
	static class LabelAddRule implements InsertInLayoutRule
	{

		private Object container;

		private CellHandle newTarget;

		public LabelAddRule( Object container )
		{
			this.container = container;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.report.designer.internal.ui.views.actions.
		 * InsertInLayoutAction.InsertInLayoutRule#canInsert()
		 */
		public boolean canInsert( )
		{
			if ( container instanceof SlotHandle )
			{
				container = ( (SlotHandle) container ).getElementHandle( );
			}
			if ( !( container instanceof CellHandle ) )
				return false;

			CellHandle cell = (CellHandle) container;

			// Validates source position of data item
			boolean canInsert = false;
			if ( cell.getContainer( ).getContainer( ) instanceof TableGroupHandle )
			{
				canInsert = true;
			}
			else
			{
				if ( cell.getContainer( ).getContainerSlotHandle( ).getSlotID( ) == TableHandle.DETAIL_SLOT )
				{
					canInsert = true;
				}
			}

			// Validates column count and gets the target
			if ( canInsert )
			{
				TableHandle table = null;
				if ( cell.getContainer( ).getContainer( ) instanceof TableHandle )
				{
					table = (TableHandle) cell.getContainer( ).getContainer( );
				}
				else
				{
					table = (TableHandle) cell.getContainer( )
							.getContainer( )
							.getContainer( );
				}
				SlotHandle header = table.getHeader( );
				if ( header != null && header.getCount( ) > 0 )
				{
					int columnNum = HandleAdapterFactory.getInstance( )
							.getCellHandleAdapter( cell )
							.getColumnNumber( );
					newTarget = (CellHandle) HandleAdapterFactory.getInstance( )
							.getTableHandleAdapter( table )
							.getCell( 1, columnNum, false );
					return newTarget != null
							&& newTarget.getContent( ).getCount( ) == 0;
				}
			}
			return false;
		}

		/**
		 * Returns new Label insert position in form of <code>CellHandle</code>
		 */
		public Object getInsertPosition( )
		{
			return newTarget;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil
		 * .InsertInLayoutRule#insert()
		 */
		public void insert( Object object ) throws SemanticException
		{
			Assert.isTrue( object instanceof DesignElementHandle );
			newTarget.addElement( (DesignElementHandle) object,
					CellHandle.CONTENT_SLOT );
		}
	}

	/**
	 * 
	 * Rule for inserting multiple data into table, and populating adjacent
	 * cells
	 */
	static class MultiItemsExpandRule implements InsertInLayoutRule
	{

		private Object[] items;
		private Object target;
		private int focusIndex = 0;

		public MultiItemsExpandRule( Object[] items, Object target )
		{
			this.items = items;
			this.target = target;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.report.designer.internal.ui.views.actions.
		 * InsertInLayoutAction.InsertInLayoutRule#canInsert()
		 */
		public boolean canInsert( )
		{
			return items != null
					&& items.length > 1
					&& target != null
					&& ( target instanceof DesignElementHandle || target instanceof ListBandProxy );
		}

		/**
		 * 
		 * Returns multiple insert positions in form of array
		 */
		public Object getInsertPosition( )
		{
			Object[] positions = new Object[items.length];

			if ( target instanceof CellHandle )
			{
				CellHandle firstCell = (CellHandle) target;
				TableHandleAdapter tableAdapter = HandleAdapterFactory.getInstance( )
						.getTableHandleAdapter( getTableHandle( firstCell ) );
				int currentColumn = HandleAdapterFactory.getInstance( )
						.getCellHandleAdapter( firstCell )
						.getColumnNumber( );
				int currentRow = HandleAdapterFactory.getInstance( )
						.getCellHandleAdapter( firstCell )
						.getRowNumber( );
				int columnDiff = currentColumn
						+ items.length
						- tableAdapter.getColumnCount( )
						- 1;

				// Insert columns if table can not contain all items
				if ( columnDiff > 0 )
				{
					int insertColumn = tableAdapter.getColumnCount( );
					try
					{
						tableAdapter.insertColumns( columnDiff, insertColumn );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
						return null;
					}
				}

				for ( int i = 0; i < positions.length; i++ )
				{
					positions[i] = tableAdapter.getCell( currentRow,
							currentColumn++ );
				}
				focusIndex = 0;
			}
			else
			{
				for ( int i = 0; i < positions.length; i++ )
				{
					positions[i] = target;
				}
				focusIndex = items.length - 1;
			}
			return positions;
		}

		protected ReportItemHandle getTableHandle( CellHandle firstCell )
		{
			DesignElementHandle tableContainer = firstCell.getContainer( )
					.getContainer( );
			if ( tableContainer instanceof ReportItemHandle )
			{
				return (ReportItemHandle) tableContainer;
			}
			return (ReportItemHandle) tableContainer.getContainer( );
		}

		/**
		 * Returns the index of the focus element in the items
		 */
		public int getFocusIndex( )
		{
			return focusIndex;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil
		 * .InsertInLayoutRule#insert()
		 */
		public void insert( Object object ) throws SemanticException
		{
			// TODO Auto-generated method stub

		}
	}

	/**
	 * 
	 * Rule for setting key when inserting data set column to group handle
	 */
	static class GroupKeySetRule implements InsertInLayoutRule
	{

		private Object container;
		private Object dataSetColumn;

		public GroupKeySetRule( Object container,
				ResultSetColumnHandle dataSetColumn )
		{
			this.container = container;
			this.dataSetColumn = dataSetColumn;
		}

		public GroupKeySetRule( Object container,
				MeasureHandle dataSetColumn )
		{
			this.container = container;
			this.dataSetColumn = dataSetColumn;
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil
		 * .InsertInLayoutRule#canInsert()
		 */
		public boolean canInsert( )
		{
			return getGroupContainer( container ) != null
					&& getGroupHandle( container ).getKeyExpr( ) == null
					&& ( getGroupContainer( container ).getDataSet( ) == getDataSet( dataSetColumn ) || getGroupContainer( container ).getDataSet( ) == null );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil
		 * .InsertInLayoutRule#getInsertPosition()
		 */
		public Object getInsertPosition( )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil
		 * .InsertInLayoutRule#insert(java.lang.Object)
		 */
		public void insert( Object object ) throws SemanticException
		{
			Assert.isTrue( object instanceof ResultSetColumnHandle );
			Assert.isTrue( object == dataSetColumn || object == null );

			ReportItemHandle groupContainer = getGroupContainer( container );

			DataSetHandle dataSetHandle = null;

			dataSetHandle = groupContainer.getDataSet( );

			if ( dataSetHandle == null )
			{
				for ( DesignElementHandle elementHandle = groupContainer; elementHandle != null; elementHandle = elementHandle.getContainer( ) )
				{
					if ( elementHandle instanceof ListingHandle
							&& ( dataSetHandle = ( (ListingHandle) elementHandle ).getDataSet( ) ) != null
							&& ( dataSetHandle == getDataSet( dataSetColumn ) ) )
					{
						break;
					}
				}
			}

			if ( dataSetHandle == null
					|| dataSetHandle != getDataSet( dataSetColumn ) )
			{
				setDataSet( getGroupContainer( container ),
						getDataSet( dataSetColumn ) );
			}

			String name;
			if ( dataSetColumn instanceof ResultSetColumnHandle )
			{
				name = ( (ResultSetColumnHandle) dataSetColumn )
						.getColumnName( );
			}
			else
			{
				name = ( (MeasureHandle) dataSetColumn ).getName( );
			}
			String expr = DEUtil.getColumnExpression( name );
			getGroupHandle( container ).setKeyExpr( expr );

		}

	}

	static protected GroupHandle getGroupHandle( Object target )
	{
		DesignElementHandle handle = null;
		if ( target instanceof CellHandle )
		{
			handle = ( (CellHandle) target ).getContainer( ).getContainer( );
		}
		else if ( target instanceof ListBandProxy )
		{
			handle = ( (ListBandProxy) target ).getElemtHandle( );
		}

		if ( handle instanceof GroupHandle )
		{
			return (GroupHandle) handle;
		}
		return null;
	}

	static protected ReportItemHandle getGroupContainer( Object target )
	{
		GroupHandle group = getGroupHandle( target );
		if ( group != null
				&& group.getContainer( ) instanceof ReportItemHandle )
			return (ReportItemHandle) group.getContainer( );
		return null;
	}
	
	/**
	 * Creates a object to insert.
	 * 
	 * @param insertObj
	 *            object insert to layout
	 * @param target
	 *            insert target, like cell or ListBandProxy
	 * @param targetParent
	 *            insert target's non-dummy container, like table or list
	 * @return new object in layout
	 * @throws SemanticException
	 */
	public static DesignElementHandle performInsert( Object insertObj,
			Object target, Object targetParent ) throws SemanticException
	{
		Assert.isNotNull( insertObj );
		Assert.isNotNull( target );
		if ( insertObj instanceof DataSetHandle )
		{
			return performInsertDataSet( (DataSetHandle) insertObj );
		}
		else if ( insertObj instanceof ResultSetColumnHandle )
		{
			return performInsertDataSetColumn( (ResultSetColumnHandle) insertObj,
					target,
					targetParent );
		}
		else if ( insertObj instanceof MeasureHandle )
		{
			return performInsertLinkedDataModelMeasure( (MeasureHandle) insertObj,
					target,
					(ReportItemHandle)targetParent );
		}
		else if ( insertObj instanceof ScalarParameterHandle )
		{
			return performInsertParameter( (ScalarParameterHandle) insertObj );
		}
		else if ( insertObj instanceof VariableElementHandle )
		{
			return performInsertVariable( (VariableElementHandle) insertObj );
		}
		else if ( insertObj instanceof String )
		{
			// Such as invalid group key
			return performInsertString( (String) insertObj, target );
		}
		else if ( insertObj instanceof Object[] )
		{
			return performMultiInsert( (Object[]) insertObj,
					target,
					targetParent );
		}
		else if ( insertObj instanceof IStructuredSelection )
		{
			return performMultiInsert( ( (IStructuredSelection) insertObj ).toArray( ),
					target,
					targetParent );
		}
		return null;
	}

	/**
	 * Creates a object, "Add" operation to layout needs to handle later.
	 * <p>
	 * Must make sure operation legal before execution.
	 * </p>
	 * 
	 * @param insertObj
	 *            object insert to layout
	 * @param editPart
	 *            target EditPart
	 * @return new object in layout
	 * @throws SemanticException
	 */
	public static DesignElementHandle performInsert( Object insertObj,
			EditPart editPart ) throws SemanticException
	{
		Assert.isNotNull( insertObj );
		Assert.isNotNull( editPart );
		return performInsert( insertObj,
				editPart.getModel( ),
				editPart.getParent( ).getModel( ) );
	}

	/**
	 * Creates multiple objects
	 * 
	 * @param array
	 *            multiple creation source
	 * @param target
	 * @param targetParent
	 * @return first creation in layout
	 * @throws SemanticException
	 */
	protected static DesignElementHandle performMultiInsert( Object[] array,
			Object target, Object targetParent ) throws SemanticException
	{
		DesignElementHandle result = null;

		MultiItemsExpandRule rule = new MultiItemsExpandRule( array, target );
		if ( rule.canInsert( ) )
		{
			Object[] positions = (Object[]) rule.getInsertPosition( );
			if ( positions != null )
			{
				for ( int i = 0; i < array.length; i++ )
				{
					DesignElementHandle newObj = performInsert( array[i],
							positions[i],
							targetParent );
					if ( i == rule.getFocusIndex( ) )
					{
						result = newObj;
					}
					else
					{
						DNDUtil.addElementHandle( positions[i], newObj );
						if ( newObj instanceof DesignElementHandle )
						{
							DEUtil.setDefaultTheme( (DesignElementHandle) newObj );
						}
					}
				}
			}
		}
		else if ( array.length != 0 )
		{
			result = performInsert( array[0], target, targetParent );
		}
		return result;
	}

	public static DataItemHandle performInsertParameter(
			ScalarParameterHandle model ) throws SemanticException
	{
		// DataItemHandle dataHandle = SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( )
		// .getElementFactory( )
		// .newDataItem( null );
		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( null );

		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( dataHandle,
				model.getName( ) );
		ExpressionUtility.setBindingColumnExpression( model, bindingColumn );

		// hardcode
		// parameter's type datatime is not equals data's.
		String paramType = model.getDataType( );
		if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( paramType ) )
			paramType = DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;

		if ( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.endsWith( model.getParamType( ) ) )
		{
			bindingColumn.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_JAVA_OBJECT );
		}
		else
		{
			bindingColumn.setDataType( paramType );
		}

		dataHandle.addColumnBinding( bindingColumn, false );
		dataHandle.setResultSetColumn( bindingColumn.getName( ) );
		return dataHandle;
	}

	public static DataItemHandle performInsertVariable(
			VariableElementHandle model ) throws SemanticException
	{
		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( null );

		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( dataHandle,
				model.getName( ) );
		bindingColumn.setExpression( DEUtil.getExpression( model ) );

		// FIXME, currently varialbe does not support type
		String paramType = DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;

		bindingColumn.setDataType( paramType );

		dataHandle.addColumnBinding( bindingColumn, false );
		dataHandle.setResultSetColumn( bindingColumn.getName( ) );
		return dataHandle;
	}

	private static GroupHandle addGroupHandle( TableHandle tableHandle,
			ResultSetColumnHandle model, DataItemHandle dataHandle, int index )
			throws SemanticException
	{
		String columnName = model.getColumnName( );
		DesignElementFactory factory = DesignElementFactory.getInstance( tableHandle.getModuleHandle( ) );
		GroupHandle groupHandle = factory.newTableGroup( );
		int columnCount = tableHandle.getColumnCount( );
		groupHandle.getHeader( ).add( factory.newTableRow( columnCount ) );
		groupHandle.getFooter( ).add( factory.newTableRow( columnCount ) );
		groupHandle.setName( columnName );

		groupHandle.setExpressionProperty( IGroupElementModel.KEY_EXPR_PROP,
				new Expression( ExpressionUtility.getColumnExpression( columnName,
						ExpressionUtility.getExpressionConverter( UIUtil.getDefaultScriptType( ) ) ),
						UIUtil.getDefaultScriptType( ) ) );

		TOC toc = StructureFactory.createTOC( );
		toc.setExpressionProperty( TOC.TOC_EXPRESSION,
				new Expression( ExpressionUtility.getColumnExpression( columnName,
						ExpressionUtility.getExpressionConverter( UIUtil.getDefaultScriptType( ) ) ),
						UIUtil.getDefaultScriptType( ) ) );

		groupHandle.addTOC( toc );

		// slotHandle.add( groupHandle, slotHandle.getCount( ) );

		RowHandle rowHandle = ( (RowHandle) groupHandle.getHeader( ).get( 0 ) );
		CellHandle cellHandle = null;
		if ( index >= 0 && index < rowHandle.getCells( ).getCount( ) )
		{
			cellHandle = (CellHandle) rowHandle.getCells( ).get( index );
		}
		if ( cellHandle == null )
		{
			cellHandle = (CellHandle) rowHandle.getCells( ).get( 0 );
		}

		cellHandle.getContent( ).add( dataHandle );
		SlotHandle header = tableHandle.getHeader( );
		if ( header != null && header.getCount( ) > 0 )
		{
			CellHandle newTarget = null;
			if ( index >= 0 )
			{
				newTarget = (CellHandle) HandleAdapterFactory.getInstance( )
						.getTableHandleAdapter( tableHandle )
						.getCell( 1, index + 1, false );
			}
			if ( newTarget == null )
			{
				newTarget = (CellHandle) HandleAdapterFactory.getInstance( )
						.getTableHandleAdapter( tableHandle )
						.getCell( 1, 1, false );
			}
			if ( newTarget != null && newTarget.getContent( ).getCount( ) == 0 )
			{
				LabelHandle label = DesignElementFactory.getInstance( )
						.newLabel( null );
				label.setText( UIUtil.getColumnDisplayName( model ) );
				newTarget.addElement( label, CellHandle.CONTENT_SLOT );
			}
		}

		return groupHandle;
	}

	/**
	 * Inserts dataset column into the target. Add label or group key if
	 * possible
	 * 
	 * @param model
	 *            column item
	 * @param target
	 *            insert target like cell or ListBandProxy
	 * @param targetParent
	 *            target container like table or list
	 * @return to be inserted data item
	 * @throws SemanticException
	 */
	protected static DesignElementHandle performInsertDataSetColumn(
			ResultSetColumnHandle model, Object target, Object targetParent )
			throws SemanticException
	{

		/*
		 * search the target container, if container has the same dataset, add
		 * the column binding if it does not exist in the container. If the
		 * container's dataset is not the dragged dataset column's dataset,
		 * column binding will be added to the new dataitem, and set dataitem's
		 * dataset with the dragged dataset column's dataset.
		 */
		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( null );
		DataSetHandle dataSet = getDataSet( model );

		if ( targetParent instanceof TableHandle )
		{
			TableHandle tableHandle = (TableHandle) targetParent;
			if ( tableHandle.isSummaryTable( ) )
			{
				setDataSet( tableHandle, dataSet );
				setDataItemAction( model, dataHandle );
				if ( DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION.equals( UIUtil.getColumnAnalysis( model ) ) )
				{

					ComputedColumn bindingColumn = StructureFactory.newComputedColumn( tableHandle,
							model.getColumnName( ) );
					bindingColumn.setDataType( model.getDataType( ) );
					ExpressionUtility.setBindingColumnExpression( model,
							bindingColumn );
					bindingColumn.setDisplayName( UIUtil.getColumnDisplayName( model ) );
					String displayKey = UIUtil.getColumnDisplayNameKey( model );
					if ( displayKey != null )
						bindingColumn.setDisplayNameID( displayKey );
					tableHandle.addColumnBinding( bindingColumn, false );
					dataHandle.setResultSetColumn( model.getColumnName( ) );

					SlotHandle slotHandle = tableHandle.getGroups( );
					for ( Object o : slotHandle.getContents( ) )
					{
						GroupHandle group = (GroupHandle) o;
						if ( group.getName( ).equals( model.getColumnName( ) ) )
						{
							if ( target instanceof CellHandle )
							{
								CellHandle cellTarget = (CellHandle) target;
								if ( cellTarget.getContent( ).getCount( ) == 0 )
								{
									return dataHandle;
								}
							}
							return null;
						}
					}
					int index = -1;
					if ( target instanceof CellHandle )
					{
						CellHandle cellTarget = (CellHandle) target;
						CellHandleAdapter cellAdapter = HandleAdapterFactory.getInstance( )
								.getCellHandleAdapter( cellTarget );
						index = cellAdapter.getColumnNumber( );
					}

					return addGroupHandle( tableHandle,
							model,
							dataHandle,
							index - 1 );
				}
				else if ( DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE.equals( UIUtil.getColumnAnalysis( model ) ) )
				{
					DataSetHandle dataset = getDataSet( model );
					String str = UIUtil.getAnalysisColumn( model );
					String type = ""; //$NON-NLS-1$

					ResultSetColumnHandle newResultColumn = null;
					if ( str != null )
					{
						List<ColumnHintHandle> columnHints = DataUtil.getColumnHints( dataset );
						ColumnHintHandle temp = null;
						for ( int i = 0; i < columnHints.size( ); i++ )
						{
							ColumnHintHandle columnHintHandle = (ColumnHintHandle) columnHints.get( i );
							if ( str.equals( columnHintHandle.getColumnName( ) ) || str.equals( columnHintHandle.getAlias( )))
							{
								temp = columnHintHandle;
								break;
							}
						}
						if (temp == null)
						{
							return dataHandle;
						}
						List columnList = DataUtil.getColumnList( dataset );

						for ( int i = 0; i < columnList.size( ); i++ )
						{
							ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) columnList.get( i );
							if ( temp != null && (temp.getAlias().equals( resultSetColumn.getColumnName( ) ) 
									|| temp.getColumnName().equals((resultSetColumn.getColumnName()))))
							{
								newResultColumn = resultSetColumn;
								break;
							}
						}

						
						for( ColumnHintHandle columnHint : columnHints )
						{
							if ( str.equals( columnHint.getColumnName( ) )
									|| str.equals( columnHint.getAlias( ) ) )
							{
								type = columnHint.getAnalysis( );
								break;
							}
						}
						if ( DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION.equals( type ) )
						{
							boolean hasGroup = false;
							SlotHandle slotHandle = tableHandle.getGroups( );
							for ( Object o : slotHandle.getContents( ) )
							{
								GroupHandle group = (GroupHandle) o;
								//if ( group.getName( ).equals( str ) )
								if ( group.getName( ).equals( temp.getColumnName() ) ||  group.getName( ).equals( temp.getAlias()))
									hasGroup = true;
							}
							if ( !hasGroup )
							{
								ComputedColumn bindingColumn = StructureFactory.newComputedColumn( tableHandle,
										model.getColumnName( ) );
								bindingColumn.setDataType( model.getDataType( ) );
								ExpressionUtility.setBindingColumnExpression( model,
										bindingColumn );
								bindingColumn.setDisplayName( UIUtil.getColumnDisplayName( model ) );
								String displayKey = UIUtil.getColumnDisplayNameKey( model );
								if ( displayKey != null )
									bindingColumn.setDisplayNameID( displayKey );
								tableHandle.addColumnBinding( bindingColumn,
										false );
								dataHandle.setResultSetColumn( model.getColumnName( ) );

								bindingColumn = StructureFactory.newComputedColumn( tableHandle,
										newResultColumn.getColumnName( ) );
								bindingColumn.setDataType( newResultColumn.getDataType( ) );
								ExpressionUtility.setBindingColumnExpression( newResultColumn,
										bindingColumn );
								bindingColumn.setDisplayName( UIUtil.getColumnDisplayName( newResultColumn ) );
								displayKey = UIUtil.getColumnDisplayNameKey( newResultColumn );
								if ( displayKey != null )
									bindingColumn.setDisplayNameID( displayKey );
								tableHandle.addColumnBinding( bindingColumn,
										false );
								int index = -1;
								if ( target instanceof CellHandle )
								{
									CellHandle cellTarget = (CellHandle) target;
									CellHandleAdapter cellAdapter = HandleAdapterFactory.getInstance( )
											.getCellHandleAdapter( cellTarget );
									index = cellAdapter.getColumnNumber( );
								}
								return addGroupHandle( tableHandle,
										newResultColumn,
										dataHandle,
										index - 1 );
							}
						}
					}
					if ( target instanceof CellHandle )
					{
						ComputedColumn column = StructureFactory.newComputedColumn( tableHandle,
								model.getColumnName( ) );

						column.setDataType( model.getDataType( ) );
						// binding.setAggregateFunction(
						// DesignChoiceConstants.MEASURE_FUNCTION_MAX );

						// binding.setExpression(
						// ExpressionUtil.createJSRowExpression(
						// model.getColumnName( ) ) );
						ExpressionUtility.setBindingColumnExpression( model,
								column );

						ComputedColumnHandle binding = DEUtil.addColumn( tableHandle,
								column,
								false );
						dataHandle.setResultSetColumn( binding.getName( ) );
						InsertInLayoutRule rule = new LabelAddRule( target );
						if ( rule.canInsert( ) )
						{
							// LabelHandle label =
							// SessionHandleAdapter.getInstance( )
							// .getReportDesignHandle( )
							// .getElementFactory( )
							// .newLabel( null );
							LabelHandle label = DesignElementFactory.getInstance( )
									.newLabel( null );
							label.setText( UIUtil.getColumnDisplayName( model ) );
							rule.insert( label );
						}
						rule = new GroupKeySetRule( target, model );
						if ( rule.canInsert( ) )
						{
							rule.insert( model );
						}
						return dataHandle;
					}
				}
				else if ( DesignChoiceConstants.ANALYSIS_TYPE_MEASURE.equals( UIUtil.getColumnAnalysis( model ) ) )
				{
					CellHandle cellHandle = (CellHandle) target;

					ComputedColumn column = StructureFactory.newComputedColumn( tableHandle,
							model.getColumnName( ) );
					ExpressionUtility.setBindingColumnExpression( model, column );
					column.setDataType( model.getDataType( ) );
					ComputedColumnHandle binding = DEUtil.addColumn( tableHandle,
							column,
							false );
					DesignElementHandle group = cellHandle.getContainer( )
							.getContainer( );
					if ( group instanceof GroupHandle )
					{
						binding.setAggregateOn( ( (GroupHandle) group ).getName( ) );
					}
					else
					{
						binding.setAggregateOn( null );
					}

					if ( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals( model.getDataType( ) )
							|| DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals( model.getDataType( ) )
							|| DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals( model.getDataType( ) ) )
					{
						binding.setAggregateFunction( DesignChoiceConstants.MEASURE_FUNCTION_SUM );
					}
					// else if (
					// DesignChoiceConstants.COLUMN_DATA_TYPE_STRING.equals(
					// model.getDataType( )))
					// {
					// binding.setAggregateFunction(
					// DesignChoiceConstants.MEASURE_FUNCTION_MAX );
					// }
					// else
					// {
					// binding.setAggregateFunction(
					// DesignChoiceConstants.MEASURE_FUNCTION_SUM );
					// }
					else
					{
						binding.setAggregateFunction( DesignChoiceConstants.MEASURE_FUNCTION_MAX );
					}

					// binding.setExpression(
					// ExpressionUtil.createJSRowExpression(
					// model.getColumnName( ) ) );

					dataHandle.setResultSetColumn( binding.getName( ) );
					formatDataHandle( dataHandle, model );
					InsertInLayoutRule rule = new LabelAddRule( target );
					if ( rule.canInsert( ) )
					{
						// LabelHandle label =
						// SessionHandleAdapter.getInstance( )
						// .getReportDesignHandle( )
						// .getElementFactory( )
						// .newLabel( null );
						LabelHandle label = DesignElementFactory.getInstance( )
								.newLabel( null );
						label.setText( UIUtil.getColumnDisplayName( model ) );
						rule.insert( label );
					}
					rule = new GroupKeySetRule( target, model );
					if ( rule.canInsert( ) )
					{
						rule.insert( model );
					}
					return dataHandle;
				}
				// else if ( DesignChoiceConstants.ANALYSIS_TYPE_MEASURE.equals(
				// UIUtil.getColumnAnalysis( model ) )
				// || DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE.equals(
				// UIUtil.getColumnAnalysis( model ) ) )
				// {
				// // check target is a group cell
				// if ( target instanceof CellHandle
				// && ( (CellHandle) target ).getContainer( )
				// .getContainer( ) instanceof GroupHandle )
				// {
				// CellHandle cellHandle = (CellHandle) target;
				// GroupHandle group = (GroupHandle) cellHandle.getContainer( )
				// .getContainer( );
				//
				// ComputedColumn column = StructureFactory.newComputedColumn(
				// tableHandle,
				// model.getColumnName( ) );
				// ComputedColumnHandle binding = DEUtil.addColumn( tableHandle,
				// column,
				// true );
				// binding.setAggregateOn( group.getName( ) );
				//
				// if ( DesignChoiceConstants.ANALYSIS_TYPE_MEASURE.equals(
				// UIUtil.getColumnAnalysis( model ) ) )
				// binding.setAggregateFunction(
				// DesignChoiceConstants.MEASURE_FUNCTION_SUM );
				// else
				// binding.setAggregateFunction(
				// DesignChoiceConstants.MEASURE_FUNCTION_MAX );
				//
				// binding.setExpression( ExpressionUtil.createJSRowExpression(
				// model.getColumnName( ) ) );
				// dataHandle.setResultSetColumn( binding.getName( ) );
				//
				// InsertInLayoutRule rule = new LabelAddRule( target );
				// if ( rule.canInsert( ) )
				// {
				// // LabelHandle label =
				// // SessionHandleAdapter.getInstance( )
				// // .getReportDesignHandle( )
				// // .getElementFactory( )
				// // .newLabel( null );
				// LabelHandle label = DesignElementFactory.getInstance( )
				// .newLabel( null );
				// label.setText( UIUtil.getColumnDisplayName( model ) );
				// rule.insert( label );
				// }
				//
				// rule = new GroupKeySetRule( target, model );
				// if ( rule.canInsert( ) )
				// {
				// rule.insert( model );
				// }
				//
				// return dataHandle;
				// }
				// }
			}
		}

		dataHandle.setResultSetColumn( model.getColumnName( ) );
		formatDataHandle( dataHandle, model );
		if ( targetParent instanceof ReportItemHandle )
		{
			ReportItemHandle container = (ReportItemHandle) targetParent;
			// ComputedColumn bindingColumn =
			// StructureFactory.newComputedColumn( container,
			// model.getColumnName( ) );
			// bindingColumn.setDataType( model.getDataType( ) );
			// ExpressionUtility.setBindingColumnExpression( model,
			// bindingColumn );
			// bindingColumn.setDisplayName( UIUtil.getColumnDisplayName( model
			// ) );
			// String displayKey = UIUtil.getColumnDisplayNameKey( model );
			// if ( displayKey != null )
			// bindingColumn.setDisplayNameID( displayKey );
			// if ( target instanceof DesignElementHandle )
			// {
			// if ( ExpressionUtil.hasAggregation( bindingColumn.getExpression(
			// ) ) )
			// {
			// String groupType = DEUtil.getGroupControlType(
			// (DesignElementHandle) target );
			// if ( groupType.equals( DEUtil.TYPE_GROUP_GROUP ) )
			// bindingColumn.setAggregateOn( ( (GroupHandle) DEUtil.getGroups(
			// (DesignElementHandle) target )
			// .get( 0 ) ).getName( ) );
			// else if ( groupType.equals( DEUtil.TYPE_GROUP_LISTING ) )
			// bindingColumn.setAggregateOn( null );
			// }
			// }

			ReportItemHandle root = DEUtil.getBindingRoot( container );
			if ( root == null )
			{
				container = DEUtil.getListingContainer( container );
				// if listing handle is null, then binding to self, else bind to
				// list handle.
				if ( container == null )
				{
					ComputedColumn bindingColumn = createBindingColumn( target,
							dataHandle,
							model );
					setDataSet( dataHandle, dataSet );
					dataHandle.addColumnBinding( bindingColumn, false );
				}
				else
				{
					ComputedColumn bindingColumn = createBindingColumn( target,
							container,
							model );
					setDataSet( container, dataSet );
					container.addColumnBinding( bindingColumn, false );
				}
			}
			else if ( root.getDataSet( ) == dataSet
					|| ( getAdapter( ) != null && root.getDataSet( )!=null && getAdapter( ).resolveExtendedData( root.getDataSet( ) )
							.equals( getAdapter( ).resolveExtendedData( dataSet ) ) ) )
			{
				container = DEUtil.getBindingHolder( container );
				ComputedColumn bindingColumn = createBindingColumn( target,
						container,
						model );
				container.addColumnBinding( bindingColumn, false );
			}
			else
			{
				ReportItemHandle listingHandle = DEUtil.getListingContainer( container );
				if ( listingHandle != null
						&& DEUtil.getBindingRoot( listingHandle ) == root
						&& DEUtil.getBindingHolder( listingHandle ) != listingHandle )
				{
					ComputedColumn bindingColumn = createBindingColumn( target,
							listingHandle,
							model );
					setDataSet( listingHandle, dataSet );
					listingHandle.addColumnBinding( bindingColumn, false );
				}
				// do nothing, forbid dragging into the place.

			}
			//
			// DataSetHandle containerDataSet = DEUtil.getBindingRoot( container
			// )
			// .getDataSet( );
			// DataSetHandle itsDataSet = null;
			// if ( container != null )
			// {
			// itsDataSet = container.getDataSet( );
			// }
			// container = DEUtil.getListingContainer( container );
			// if ( ( itsDataSet == null && ( !dataSet.equals( containerDataSet
			// ) ) )
			// && container != null )
			// {
			// container.setDataSet( dataSet );
			// containerDataSet = dataSet;
			// }
			// if ( dataSet.equals( containerDataSet ) && container != null )
			// {
			// if ( container.getDataBindingReference( ) != null )
			// container.getDataBindingReference( )
			// .addColumnBinding( bindingColumn, false );
			// else
			// container.addColumnBinding( bindingColumn, false );
			// }
			// else
			// {
			// // should not happen
			// dataHandle.setDataSet( dataSet );
			// dataHandle.addColumnBinding( bindingColumn, false );
			// }
			// GroupHandle groupHandle = getGroupHandle( target );
			// if ( groupHandle != null )
			// {
			// ComputedColumn bindingColumn =
			// StructureFactory.newComputedColumn( groupHandle,
			// model.getColumnName( ) );
			// // bindingColumn.setColumnName( model.getColumnName( ) );
			// bindingColumn.setDataType( model.getDataType( ) );
			// bindingColumn.setExpression( DEUtil.getExpression( model ) );
			//
			// groupHandle.addColumnBinding( bindingColumn, false );
			// }
			// else
			// {
			// ComputedColumn bindingColumn =
			// StructureFactory.newComputedColumn( container,
			// model.getColumnName( ) );
			// bindingColumn.setDataType( model.getDataType( ) );
			// bindingColumn.setExpression( DEUtil.getExpression( model ) );
			// container.addColumnBinding( bindingColumn, false );
			// }
			// ComputedColumn bindingColumn =
			// StructureFactory.createComputedColumn( );
			// bindingColumn.setName( model.getColumnName( ) );
			// bindingColumn.setDataType( model.getDataType( ) );
			// bindingColumn.setExpression( DEUtil.getExpression( model ) );

			// GroupHandle groupHandle = getGroupHandle( target );
			//
			// if ( groupHandle != null )
			// {
			// for ( Iterator iter = groupHandle.getColumnBindings( )
			// .iterator( ); iter.hasNext( ); )
			// {
			// ComputedColumnHandle element = (ComputedColumnHandle) iter.next(
			// );
			// if ( element.getStructure( ).equals( bindingColumn ) )
			// {
			// bindingExist = true;
			// break;
			// }
			// }
			// }
			// else
			// {
			// for ( Iterator iter = container.getColumnBindings( ).iterator( );
			// iter.hasNext( ); )
			// {
			// ComputedColumnHandle element = (ComputedColumnHandle) iter.next(
			// );
			// if ( element.getStructure( ).equals( bindingColumn ) )
			// {
			// bindingExist = true;
			// break;
			// }
			// }
			//
			// }
		}
		else
		{
			ComputedColumn bindingColumn = StructureFactory.newComputedColumn( dataHandle,
					model.getColumnName( ) );
			bindingColumn.setDataType( model.getDataType( ) );
			ExpressionUtility.setBindingColumnExpression( model, bindingColumn );
			bindingColumn.setDisplayName( UIUtil.getColumnDisplayName( model ) );
			String displayKey = UIUtil.getColumnDisplayNameKey( model );
			if ( displayKey != null )
				bindingColumn.setDisplayNameID( displayKey );
			if ( target instanceof DesignElementHandle )
			{
				if ( ExpressionUtil.hasAggregation( bindingColumn.getExpression( ) ) )
				{
					String groupType = DEUtil.getGroupControlType( (DesignElementHandle) target );
					if ( groupType.equals( DEUtil.TYPE_GROUP_GROUP ) )
						bindingColumn.setAggregateOn( ( (GroupHandle) DEUtil.getGroups( (DesignElementHandle) target )
								.get( 0 ) ).getName( ) );
					else if ( groupType.equals( DEUtil.TYPE_GROUP_LISTING ) )
						bindingColumn.setAggregateOn( null );
				}
			}
			dataHandle.addColumnBinding( bindingColumn, false );
			setDataSet( dataHandle, dataSet );
		}

		setDataItemAction( model, dataHandle );

		// if ( !bindingExist )
		// {
		// ComputedColumn bindingColumn = StructureFactory.newComputedColumn(
		// dataHandle,
		// model.getColumnName( ) );
		// bindingColumn.setDataType( model.getDataType( ) );
		// bindingColumn.setExpression( DEUtil.getExpression( model ) );
		// dataHandle.addColumnBinding( bindingColumn, false );
		// dataHandle.setDataSet( dataSet );
		// }

		InsertInLayoutRule rule = new LabelAddRule( target );
		if ( rule.canInsert( ) )
		{
			// LabelHandle label = SessionHandleAdapter.getInstance( )
			// .getReportDesignHandle( )
			// .getElementFactory( )
			// .newLabel( null );
			LabelHandle label = DesignElementFactory.getInstance( )
					.newLabel( null );
			label.setText( UIUtil.getHeadColumnDisplayName( model ) );
			String displayKey = UIUtil.getColumnHeaderDisplayNameKey( model );
			if ( displayKey == null )
			{
				displayKey = UIUtil.getColumnDisplayNameKey( model );
			}
			if ( displayKey != null )
			{
				label.setTextKey( displayKey );
			}
			rule.insert( label );
		}

		rule = new GroupKeySetRule( target, model );
		if ( rule.canInsert( ) )
		{
			rule.insert( model );
		}

		return dataHandle;
	}

	private static void createBindingColumn( ResultSetColumnHandle model,
			TableHandle tableHandle ) throws SemanticException
	{
		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( tableHandle,
				model.getColumnName( ) );
		bindingColumn.setDataType( model.getDataType( ) );
		ExpressionUtility.setBindingColumnExpression( model,
				bindingColumn );
		bindingColumn.setDisplayName( UIUtil.getColumnDisplayName( model ) );
		String displayKey = UIUtil.getColumnDisplayNameKey( model );
		if ( displayKey != null )
			bindingColumn.setDisplayNameID( displayKey );
		tableHandle.addColumnBinding( bindingColumn, false );
	}

	/**
	 * Inserts measure into the target. Add label or group key if
	 * possible
	 * 
	 * @param model
	 *            column item
	 * @param target
	 *            insert target like cell or ListBandProxy
	 * @param targetParent
	 *            target container like table or list
	 * @return to be inserted data item
	 * @throws SemanticException
	 */
	protected static DesignElementHandle performInsertLinkedDataModelMeasure(
			MeasureHandle model, Object target, ReportItemHandle tableHandle )
			throws SemanticException
	{
		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( null );
		setDataItemAction( model, dataHandle );
		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( tableHandle,
				model.getName( ) );
		bindingColumn.setDataType( model.getDataType( ) );
		String defaultScriptType = UIUtil.getDefaultScriptType( );
		IExpressionConverter converter = ExpressionUtility.getExpressionConverter( defaultScriptType );
		String expression = null;
		boolean isOnlySupportJS = false;
		if ( converter != null && !isOnlySupportJS )
		{
			expression = converter.getMeasureExpression( model.getName( ) );
		}
		else
		{
			defaultScriptType = ExpressionType.JAVASCRIPT;
//			expression = DEUtil.getExpression( model );
			expression = ExpressionUtil.createJSMeasureExpression( model.getName( ) );
		}

		Expression bindingExpression = new Expression( expression,
				defaultScriptType );
		bindingColumn.setExpressionProperty( ComputedColumn.EXPRESSION_MEMBER,
				bindingExpression );
		
		bindingColumn.setDisplayName( model.getDisplayName( ) );
		String displayKey = model.getDisplayNameKey( );
		if ( displayKey != null )
			bindingColumn.setDisplayNameID( displayKey );
		GroupHandle group = getGroupHandle( target );
		
		if ( group != null )
		{
			bindingColumn.setAggregateOn( ( (GroupHandle) group ).getName( ) );
		}
		else
		{
			bindingColumn.setAggregateOn( "All" );
		}
		tableHandle.addColumnBinding( bindingColumn, false );
		dataHandle.setResultSetColumn( model.getName( ) );

		InsertInLayoutRule rule = new LabelAddRule( target );
		if ( rule.canInsert( ) )
		{
			// LabelHandle label =
			// SessionHandleAdapter.getInstance( )
			// .getReportDesignHandle( )
			// .getElementFactory( )
			// .newLabel( null );
			LabelHandle label = DesignElementFactory.getInstance( )
					.newLabel( null );
			label.setText( model.getDisplayName( ) );
			rule.insert( label );
		}
		rule = new GroupKeySetRule( target, model );
		if ( rule.canInsert( ) )
		{
			rule.insert( model );
		}
		
		return dataHandle;
	}

	private static void setDataItemAction( MeasureHandle model,
			DataItemHandle dataHandle )
	{
		ActionHandle actionHandle = model.getActionHandle( );
		if ( actionHandle != null )
		{
			List source = new ArrayList( );
			source.add( actionHandle.getStructure( ) );
			List newAction = ModelUtil.cloneStructList( source );
			try
			{
				dataHandle.setAction( (Action) newAction.get( 0 ) );
			}
			catch ( SemanticException e )
			{
				// Do nothing now
			}
		}
	}

	private static void setDataItemAction( ResultSetColumnHandle model,
			DataItemHandle dataHandle )
	{
		ActionHandle actionHandle = UIUtil.getColumnAction( model );
		if ( actionHandle != null )
		{
			List source = new ArrayList( );
			source.add( actionHandle.getStructure( ) );
			List newAction = ModelUtil.cloneStructList( source );
			try
			{
				dataHandle.setAction( (Action) newAction.get( 0 ) );
			}
			catch ( SemanticException e )
			{
				// Do nothing now
			}
		}
	}

	/**
	 * create a ComputedColumn object
	 * 
	 * @param target
	 *            where data item will be inserted.
	 * @param bindingHolder
	 *            where the ComputedColumn will be inserted.
	 * @param model
	 *            column item
	 * @return
	 */
	private static ComputedColumn createBindingColumn( Object target,
			ReportItemHandle bindingHolder, ResultSetColumnHandle model )
	{

		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( bindingHolder,
				model.getColumnName( ) );
		bindingColumn.setDataType( model.getDataType( ) );
		ExpressionUtility.setBindingColumnExpression( model, bindingColumn );
		bindingColumn.setDisplayName( UIUtil.getColumnDisplayName( model ) );
		String displayKey = UIUtil.getColumnDisplayNameKey( model );
		if ( displayKey != null )
			bindingColumn.setDisplayNameID( displayKey );
		if ( target instanceof DesignElementHandle )
		{
			if ( ExpressionUtil.hasAggregation( bindingColumn.getExpression( ) ) )
			{
				String groupType = DEUtil.getGroupControlType( (DesignElementHandle) target );
				if ( groupType.equals( DEUtil.TYPE_GROUP_GROUP ) )
					bindingColumn.setAggregateOn( ( (GroupHandle) DEUtil.getGroups( (DesignElementHandle) target )
							.get( 0 ) ).getName( ) );
				else if ( groupType.equals( DEUtil.TYPE_GROUP_LISTING ) )
					bindingColumn.setAggregateOn( null );
			}
		}
		return bindingColumn;
	}

	// private static GroupHandle getGroupHandle( Object target )
	// {
	// DesignElementHandle handle = null;
	// if ( target instanceof CellHandle )
	// {
	// handle = ( (CellHandle) target ).getContainer( ).getContainer( );
	// }
	// else if ( target instanceof ListBandProxy )
	// {
	// handle = ( (ListBandProxy) target ).getElemtHandle( );
	// }
	//
	// if ( handle instanceof GroupHandle )
	// {
	// return (GroupHandle) handle;
	// }
	// return null;
	// }

	/**
	 * Inserts invalid column string into the target. Add label if possible
	 * 
	 * @param expression
	 *            invalid column or other expression
	 * @param target
	 *            insert target like cell or ListBandProxy
	 * @return to be inserted data item
	 * @throws SemanticException
	 */
	protected static DesignElementHandle performInsertString(
			String expression, Object target ) throws SemanticException
	{
		// DataItemHandle dataHandle = SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( )
		// .getElementFactory( )
		// .newDataItem( null );
		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( null );
		dataHandle.setResultSetColumn( expression );

		InsertInLayoutRule rule = new LabelAddRule( target );
		if ( rule.canInsert( ) )
		{
			// LabelHandle label = SessionHandleAdapter.getInstance( )
			// .getReportDesignHandle( )
			// .getElementFactory( )
			// .newLabel( null );
			LabelHandle label = DesignElementFactory.getInstance( )
					.newLabel( null );
			label.setText( expression );
			rule.insert( label );
		}

		return dataHandle;
	}

	protected static TableHandle performInsertDataSet( DataSetHandle model )
			throws SemanticException
	{
		DataSetBindingSelector selector=new DataSetBindingSelector( UIUtil.getDefaultShell( ),
				Messages.getString( "DataSetBindingSelectorPage.Title" ) );
		selector.setDatasetHandle(model);
		selector.setValidateEmptyResults(true);
		if ( selector.open( ) == Window.OK )
		{		
			Object[] datasetInfo = (Object[])selector.getResult();
			Object[] selectedColumns=(Object[])datasetInfo[1];
					
			ResultSetColumnHandle[] columns=new ResultSetColumnHandle[selectedColumns.length];		
			for(int i=0;i<selectedColumns.length;i++){
				columns[i]=(ResultSetColumnHandle) selectedColumns[i];
			}
			
			TableHandle tableHandle = DesignElementFactory.getInstance( )
					.newTableItem( null, columns.length );
			
			setInitWidth( tableHandle );
			insertToCell( model,
					tableHandle,
					tableHandle.getHeader( ),
					columns,
					true );
			insertToCell( model,
					tableHandle,
					tableHandle.getDetail( ),
					columns,
					false );

			setDataSet( tableHandle, model );
			return tableHandle;
			
		}
		
		return null;

	}

	/**
	 * Validates object can be inserted to layout. Support the multiple.
	 * 
	 * @param insertObj
	 *            single inserted object or multi-objects
	 * @param targetPart
	 * @return if can be inserted to layout
	 */
	public static boolean handleValidateInsertToLayout( Object insertObj,
			EditPart targetPart )
	{
		if ( targetPart == null )
		{
			return false;
		}
		if ( insertObj instanceof Object[] )
		{
			Object[] array = (Object[]) insertObj;
			if ( !checkSameDataSetInMultiColumns( array ) )
			{
				return false;
			}
			if ( !checkContainContainMulitItem( array, targetPart.getModel( ) ) )
			{
				return false;
			}
			for ( int i = 0; i < array.length; i++ )
			{
				if ( !handleValidateInsertToLayout( array[i], targetPart ) )
				{
					return false;
				}
			}
			return true;
		}
		else if ( insertObj instanceof IStructuredSelection )
		{
			return handleValidateInsertToLayout( ( (IStructuredSelection) insertObj ).toArray( ),
					targetPart );
		}
		else if ( insertObj instanceof DataSetHandle )
		{
			return isHandleValid( (DataSetHandle) insertObj )
					&& handleValidateDataSet( targetPart );
		}
		else if ( insertObj instanceof ResultSetColumnHandle )
		{
			return handleValidateDataSetColumn( (ResultSetColumnHandle) insertObj,
					targetPart );
		}
		// else if ( insertObj instanceof DimensionHandle )
		// {
		// return handleValidateDimension( (DimensionHandle) insertObj,
		// targetPart );
		// }
		// else if ( insertObj instanceof MeasureHandle )
		// {
		// return handleValidateMeasure( (MeasureHandle) insertObj,
		// targetPart );
		// }
		else if ( insertObj instanceof LabelHandle )
		{
			return handleValidateLabel( (LabelHandle) insertObj, targetPart );
		}
		else if ( insertObj instanceof ResultSetColumnHandle )
		{
			return handleValidateDataSetColumn( (ResultSetColumnHandle) insertObj,
					targetPart );
		}
		else if ( insertObj instanceof ScalarParameterHandle )
		{
			return isHandleValid( (ScalarParameterHandle) insertObj )
					&& handleValidateParameter( targetPart );
		}
		else if ( insertObj instanceof MeasureHandle )
		{
			Object target = targetPart.getModel( );
			GroupHandle group = getGroupHandle( target );
			if ( group != null )
			{
				return true;
			}
			if ( target instanceof CellHandle )
			{
				Object container = ( (CellHandle) target ).getContainer( );
				if ( container instanceof RowHandle )
				{
					RowHandle row = (RowHandle)container;
					SlotHandle slotHandle = row.getContainerSlotHandle( );
					int slotId = slotHandle.getSlotID( );
					return slotId != TableHandle.DETAIL_SLOT;
				}
			}
			else if ( target instanceof ListBandProxy )
			{
				int type = ( (ListBandProxy) target ).getType( );
				return type != ListBandProxy.LIST_DETAIL_TYPE;
			}
		}
		return false;
	}

	private static boolean handleValidateLabel( LabelHandle handle,
			EditPart targetPart )
	{
		if ( targetPart.getModel( ) instanceof IAdaptable )
		{
			Object obj = ( (IAdaptable) targetPart.getModel( ) ).getAdapter( DesignElementHandle.class );
			if ( obj instanceof ExtendedItemHandle )
			{
				return ( (ExtendedItemHandle) obj ).canContain( DEUtil.getDefaultContentName( obj ),
						handle );
			}
		}
		return false;
	}

	private static boolean checkContainContainMulitItem( Object[] objects,
			Object slotHandle )
	{
		SlotHandle handle = null;
		// if ( slotHandle instanceof ReportElementModel )
		// {
		// handle = ( (ReportElementModel) slotHandle ).getSlotHandle( );
		// }
		// else
		if ( slotHandle instanceof SlotHandle )
		{
			handle = (SlotHandle) slotHandle;
		}
		if ( handle != null && objects != null && objects.length > 1 )
		{
			if ( !handle.getDefn( ).isMultipleCardinality( ) )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if all the DataSetColumn has the same DataSet.
	 * 
	 * @param array
	 *            all elements
	 * @return false if not same; true if every column has the same DataSet or
	 *         the element is not an instance of DataSetColumn
	 */
	protected static boolean checkSameDataSetInMultiColumns( Object[] array )
	{
		if ( array == null )
			return false;
		Object dataSet = null;
		for ( int i = 0; i < array.length; i++ )
		{
			if ( array[i] instanceof ResultSetColumnHandle )
			{
				Object currDataSet = getDataSet( ( (ResultSetColumnHandle) array[i] ) );
				if ( currDataSet == null )
				{
					return false;
				}

				if ( dataSet == null )
				{
					dataSet = currDataSet;
				}
				else
				{
					if ( dataSet != currDataSet )
					{
						return false;
					}
				}
			}
		}
		return true;
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
		if ( dropPart.getParent( ) == null )
		{
			return false;
		}
		Object container = dropPart.getParent( ).getModel( );
		return ( container instanceof GridHandle
				|| container instanceof TableHandle
				|| container instanceof FreeFormHandle
				|| container instanceof ListHandle || dropPart.getModel( ) instanceof ModuleHandle );
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
		if ( dropPart.getParent( ) == null )
		{
			return false;
		}
		Object container = dropPart.getParent( ).getModel( );
		return ( container instanceof GridHandle
				|| container instanceof TableHandle
				|| container instanceof FreeFormHandle
				|| container instanceof ListHandle
				|| container instanceof MasterPageHandle || dropPart.getModel( ) instanceof ModuleHandle );
	}

	protected static boolean handleValidateMeasureDropContainer(
			MeasureHandle measure, EditPart dropPart )
	{
		if ( dropPart.getModel( ) instanceof IVirtualValidator )
		{
			return ( (IVirtualValidator) dropPart.getModel( ) ).handleValidate( measure );
		}
		return false;
	}

	protected static boolean handleValidateDimensionDropContainer(
			DimensionHandle dimension, EditPart dropPart )
	{
		if ( dropPart.getModel( ) instanceof IVirtualValidator )
		{
			return ( (IVirtualValidator) dropPart.getModel( ) ).handleValidate( dimension );
		}
		return false;
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
		if ( dropPart.getParent( ) == null )
		{
			return false;
		}
		Object container = dropPart.getParent( ).getModel( );
		return ( container instanceof GridHandle
				|| container instanceof TableHandle
				|| container instanceof FreeFormHandle
				|| container instanceof ListHandle || dropPart.getModel( ) instanceof ModuleHandle );
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

	protected static boolean handleValidateDimension(
			DimensionHandle insertObj, EditPart target )
	{
		return handleValidateDimensionDropContainer( insertObj, target );
	}

	protected static boolean handleValidateMeasure( MeasureHandle insertObj,
			EditPart target )
	{
		return handleValidateMeasureDropContainer( insertObj, target );
	}

	/**
	 * Validates drop target from data set column in data view.
	 * 
	 * @return validate result
	 */
	protected static boolean handleValidateDataSetColumn(
			ResultSetColumnHandle insertObj, EditPart target )
	{
		if ( handleValidateDataSetColumnDropContainer( target )
				&& DNDUtil.handleValidateTargetCanContainType( target.getModel( ),
						ReportDesignConstants.DATA_ITEM ) )
		{
			// Validates target is report root
			if ( target.getModel( ) instanceof ModuleHandle
					|| isMasterPageHeaderOrFooter( target.getModel( ) ) )
			{
				return true;
			}

			// Validates target's dataset is null or the same with the inserted
			DesignElementHandle handle = (DesignElementHandle) target.getParent( )
					.getModel( );
			if ( handle instanceof TableHandle
					&& target.getModel( ) instanceof CellHandle
					&& ( (TableHandle) handle ).isSummaryTable( ) )
			{
				TableHandle tableHandle = (TableHandle) handle;
				CellHandle cellHandle = (CellHandle) target.getModel( );
				if ( DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION.equals( UIUtil.getColumnAnalysis( insertObj ) ) )
				{
					// SlotHandle slotHandle = tableHandle.getGroups( );
					// for ( Object o : slotHandle.getContents( ) )
					// {
					// GroupHandle group = (GroupHandle) o;
					// if ( group.getName( ).equals( insertObj.getColumnName( )
					// ) )
					// return false;
					// }
					if ( cellHandle.getContent( ).getCount( ) == 0 )
					{
						return true;
					}
					return !hasGroup( tableHandle, insertObj.getColumnName( ) );
				}
				else if ( DesignChoiceConstants.ANALYSIS_TYPE_ATTRIBUTE.equals( UIUtil.getColumnAnalysis( insertObj ) ) )
				{
					String str = UIUtil.getAnalysisColumn( insertObj );
					DataSetHandle dataset = getDataSet( insertObj );
					String type = "";
					if ( str != null )
					{
						List<ColumnHintHandle> columnHints = DataUtil.getColumnHints( dataset );
						for( ColumnHintHandle columnHint : columnHints )
						{
							if ( str.equals( columnHint.getColumnName( ) )
									|| str.equals( columnHint.getAlias( ) ) )
							{
								type = columnHint.getAnalysis( );
								break;
							}
						}
						
						if ( DesignChoiceConstants.ANALYSIS_TYPE_DIMENSION.equals( type ) )
						{
							GroupHandle findGroup = null;
							SlotHandle slotHandle = tableHandle.getGroups( );
							for ( Object o : slotHandle.getContents( ) )
							{
								GroupHandle group = (GroupHandle) o;
								if ( group.getName( ).equals( str ) )
								{
									findGroup = group;
								}
							}
							if ( findGroup == null )
							{
								// DesignElementHandle container =
								// cellHandle.getContainer( ).getContainer( );
								// if (container instanceof TableHandle)
								// {
								// return true;
								// }
								// if (container instanceof GroupHandle &&
								// str.equals( ((GroupHandle)container).getName(
								// )))
								// {
								// return true;
								// }
								// return false;
								return true;
							}
							else
							{
								if ( cellHandle.getContainer( ).getContainer( ) == findGroup )
								{
									return true;
								}
								else
								{
									return false;
								}
							}
						}
						else if ( type != null && !type.equals( "" ) ) //$NON-NLS-1$
						{
							SlotHandle slotHandle = cellHandle.getContainer( )
									.getContainerSlotHandle( );
							if ( slotHandle.equals( tableHandle.getHeader( ) )
									|| slotHandle.equals( tableHandle.getFooter( ) ) )
							{
								return true;
							}
							else
							{
								return false;
							}
						}
					}
					else
					{
						SlotHandle slotHandle = cellHandle.getContainer( )
								.getContainerSlotHandle( );
						if ( slotHandle == tableHandle.getHeader( )
								|| slotHandle == tableHandle.getFooter( ) )
						{
							return true;
						}
						return false;
					}

				}
			}
			if ( handle instanceof ReportItemHandle )
			{
				ReportItemHandle bindingHolder = DEUtil.getListingContainer( handle );
				DataSetHandle itsDataSet = ( (ReportItemHandle) handle ).getDataSet( );
				DataSetHandle dataSet = null;
				ReportItemHandle bindingRoot = DEUtil.getBindingRoot( handle );
				if ( bindingRoot != null )
				{
					dataSet = bindingRoot.getDataSet( );
				}
				if ( itsDataSet == null
						&& ( bindingHolder == null || !bindingHolder.getColumnBindings( )
								.iterator( )
								.hasNext( ) )
						|| getDataSet( insertObj ).equals( dataSet ) )
				{
					return true;
				}
				else
				{
					if ( ExtendedDataModelUIAdapterHelper.isBoundToExtendedData( bindingRoot ) )
					{
						return getAdapter( ) != null
								&& getAdapter( ).getBoundExtendedData( bindingRoot )
										.equals( getAdapter( ).resolveExtendedData( getDataSet( insertObj ) ) );
					}
				}
			}
		}
		return false;
	}

	private static boolean hasGroup( TableHandle tableHandle, String groupName )
	{
		SlotHandle slotHandle = tableHandle.getGroups( );
		for ( Object o : slotHandle.getContents( ) )
		{
			GroupHandle group = (GroupHandle) o;
			if ( group.getName( ).equals( groupName ) )
				return true;
		}

		return true;
	}

	private static boolean isMasterPageHeaderOrFooter( Object obj )
	{
		if ( !( obj instanceof SlotHandle ) )
		{
			return false;
		}
		if ( ( (SlotHandle) obj ).getElementHandle( ) instanceof MasterPageHandle )
		{
			return true;
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
	 * Validates drag source from data view to layout. Support the multiple.
	 * 
	 * @return validate result
	 */
	public static boolean handleValidateInsert( Object insertObj )
	{
		if ( insertObj instanceof Object[] )
		{
			Object[] array = (Object[]) insertObj;
			if ( array.length == 0 )
			{
				return false;
			}
			for ( int i = 0; i < array.length; i++ )
			{
				if ( !handleValidateInsert( array[i] ) )
					return false;
			}
			return true;
		}
		else if ( insertObj instanceof IStructuredSelection )
		{
			return handleValidateInsert( ( (IStructuredSelection) insertObj ).toArray( ) );
		}
		// else if ( insertObj instanceof ParameterHandle )
		// {
		// if ( ( (ParameterHandle) insertObj ).getRoot( ) instanceof
		// LibraryHandle )
		// return false;
		// }
		if ( insertObj instanceof DataSetHandle )
		{
			return DataSetUIUtil.hasMetaData( (DataSetHandle) insertObj );
		}
		return insertObj instanceof ResultSetColumnHandle
				|| insertObj instanceof ScalarParameterHandle
				|| insertObj instanceof DimensionHandle
				|| insertObj instanceof MeasureHandle;
	}

	public static void insertToCell( DataSetHandle model,
			TableHandle tableHandle, SlotHandle slot,
			ResultSetColumnHandle[] columns, boolean isLabel )
	{
		List<ColumnHintHandle> list = null;
		for ( int i = 0; i < slot.getCount( ); i++ )
		{
			SlotHandle cells = ( (RowHandle) slot.get( i ) ).getCells( );
			for ( int j = 0; j < cells.getCount( ) && j < columns.length; j++ )
			{
				if (list == null)
				{
					list = DataUtil.getColumnHints(model);
				}
				CellHandle cell = (CellHandle) cells.get( j );

				try
				{
					if ( isLabel )
					{
						LabelHandle labelItemHandle = SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.getElementFactory( )
								.newLabel( null );
						// LabelHandle labelItemHandle =
						// DesignElementFactory.getInstance( )
						// .newLabel( null );
						String labelText = UIUtil.getHeadColumnDisplayName( list, columns[j] );
						if ( labelText != null )
						{
							labelItemHandle.setText( labelText );
						}
						String displayKey = UIUtil.getColumnHeaderDisplayNameKey( list,columns[j] );
						if ( displayKey == null )
						{
							displayKey = UIUtil.getColumnDisplayNameKey( list, columns[j] );
						}
						if ( displayKey != null )
						{
							labelItemHandle.setTextKey( displayKey );
						}
						cell.addElement( labelItemHandle, cells.getSlotID( ) );
					}
					else
					{
						DataItemHandle dataHandle = SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.getElementFactory( )
								.newDataItem( null );
						// DataItemHandle dataHandle =
						// DesignElementFactory.getInstance( )
						// .newDataItem( null );
						dataHandle.setResultSetColumn( columns[j].getColumnName( ) );

						formatDataHandle( dataHandle, columns[j], list);
						cell.addElement( dataHandle, cells.getSlotID( ) );

						// add data binding to table.
						ComputedColumn bindingColumn = StructureFactory.newComputedColumn( tableHandle,
								columns[j].getColumnName( ) );
						bindingColumn.setDataType( columns[j].getDataType( ) );
						ExpressionUtility.setBindingColumnExpression( columns[j],
								bindingColumn );
						bindingColumn.setDisplayName( UIUtil.getColumnDisplayName(list,  columns[j] ) );
						String displayKey = UIUtil.getColumnDisplayNameKey(list, columns[j] );
						if ( displayKey != null )
							bindingColumn.setDisplayNameID( displayKey );
						tableHandle.addColumnBinding( bindingColumn, false );

						ActionHandle actionHandle = UIUtil.getColumnAction(list, columns[j] );
						if ( actionHandle != null )
						{
							List source = new ArrayList( );
							source.add( actionHandle.getStructure( ) );
							List newAction = ModelUtil.cloneStructList( source );
							dataHandle.setAction( (Action) newAction.get( 0 ) );
						}
					}
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}
		}
	}
	
	private static void formatDataHandle( DataItemHandle dataHandle,
			ResultSetColumnHandle column, ColumnHintHandle hintHandle )
	{
		if (hintHandle == null)
		{
			return;
		}
		try
		{
			StyleHandle styleHandle = dataHandle.getPrivateStyle( );
			
			if ( hintHandle != null
					&& hintHandle.isLocal( ColumnHint.WORD_WRAP_MEMBER ) )
			{
				boolean wordWrap = UIUtil.isWordWrap( column );
				if ( wordWrap )
				{
					styleHandle.setWhiteSpace( DesignChoiceConstants.WHITE_SPACE_NORMAL );
				}
				else
				{
					styleHandle.setWhiteSpace( DesignChoiceConstants.WHITE_SPACE_NOWRAP );
				}
			}

			String aliment = hintHandle.getHorizontalAlign( );
			if ( aliment != null )
			{
				styleHandle.setTextAlign( aliment );
			}

			String helpText = hintHandle.getHelpText( );
			if ( helpText != null )
			{
				dataHandle.setHelpText( helpText );
			}

			if ( hintHandle != null )
			{
				formatDataHandleDataType( column.getDataType( ),
						hintHandle.getValueFormat( ),
						styleHandle );
			}

		}
		catch ( SemanticException e )
		{
			// do nothing now
		}

	}
	
	private static void formatDataHandle( DataItemHandle dataHandle,
			ResultSetColumnHandle column, List<ColumnHintHandle> list )
	{
		
		ColumnHintHandle hintHandle = findColumnHintHandle( list, column );
		formatDataHandle(dataHandle, column, hintHandle);	

	}
	private static void formatDataHandle( DataItemHandle dataHandle,
			ResultSetColumnHandle column )
	{
		ColumnHintHandle hintHandle = findColumnHintHandle( column );
		formatDataHandle(dataHandle, column, hintHandle);	
	}

	private static ColumnHintHandle findColumnHintHandle(List<ColumnHintHandle> columnHints,
			ResultSetColumnHandle column )
	{
		for( ColumnHintHandle columnHint : columnHints )
		{
			if ( column.getColumnName( ).equals( columnHint.getColumnName( ) )
					|| column.getColumnName( ).equals( columnHint.getAlias( ) ) )
			{
				return columnHint;
			}
		}
		return null;
	}
	
	private static ColumnHintHandle findColumnHintHandle(
			ResultSetColumnHandle column )
	{
		DataSetHandle dataset = getDataSet( column );
		List<ColumnHintHandle> columnHints = DataUtil.getColumnHints( dataset );
		for( ColumnHintHandle columnHint : columnHints )
		{
			if ( column.getColumnName( ).equals( columnHint.getColumnName( ) )
					|| column.getColumnName( ).equals( columnHint.getAlias( ) ) )
			{
				return columnHint;
			}
		}
		return null;
	}

	public static void formatDataHandleDataType( String type,
			FormatValue formartValue, StyleHandle styleHandle )
	{
		if ( formartValue == null )
		{
			return;
		}
		try
		{
			if ( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals( type )
					|| DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals( type )
					|| DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals( type ) )
			{
				if ( formartValue.getPattern( ) != null )
				{
					styleHandle.setNumberFormat( formartValue.getPattern( ) );
				}
				if ( formartValue.getCategory( ) != null )
				{
					styleHandle.setNumberFormatCategory( formartValue.getCategory( ) );
				}
			}
			else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals( type )
					|| DesignChoiceConstants.COLUMN_DATA_TYPE_DATE.equals( type ) )
			{
				if ( formartValue.getPattern( ) != null )
				{
					styleHandle.setDateTimeFormat( formartValue.getPattern( ) );
				}
				if ( formartValue.getCategory( ) != null )
				{
					styleHandle.setDateTimeFormatCategory( formartValue.getCategory( ) );
				}
			}
			else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING.equals( type ) )
			{
				if ( formartValue.getPattern( ) != null )
				{
					styleHandle.setStringFormat( formartValue.getPattern( ) );
				}
				if ( formartValue.getCategory( ) != null )
				{
					styleHandle.setStringFormatCategory( formartValue.getCategory( ) );
				}
			}
		}
		catch ( SemanticException e )
		{
			// do nothing now
		}
	}

	/**
	 * Sets initial width to new object
	 * 
	 * @param object
	 *            new object
	 */
	public static void setInitWidth( Object object )
	{
		// int percentAll = 100;
		// try
		// {
		// if ( object instanceof TableHandle )
		// {
		// TableHandle table = (TableHandle) object;
		// table.setWidth( percentAll
		// + DesignChoiceConstants.UNITS_PERCENTAGE );
		// }
		// else if ( object instanceof GridHandle )
		// {
		// GridHandle grid = (GridHandle) object;
		// grid.setWidth( percentAll
		// + DesignChoiceConstants.UNITS_PERCENTAGE );
		// }
		// else
		// return;
		// }
		// catch ( SemanticException e )
		// {
		// ExceptionHandler.handle( e );
		// }
	}

	protected static boolean isHandleValid( DesignElementHandle handle )
	{
		if ( handle instanceof DataSetHandle )
		{
			boolean needsDataSource = getAdapter( ) == null ? true
					: getAdapter( ).needsDataSource( (DataSetHandle) handle );

			if ( ( !( handle instanceof JointDataSetHandle
					|| handle instanceof DerivedDataSetHandle || !needsDataSource ) && ( (DataSetHandle) handle ).getDataSource( ) == null )
					|| !DataSetUIUtil.hasMetaData( (DataSetHandle) handle ) )
			{
				return false;
			}
		}
		return handle.isValid( ) && handle.getSemanticErrors( ).isEmpty( );
	}

	/**
	 * Converts edit part selection into model selection.
	 * 
	 * @param selection
	 *            edit part
	 * @return model, return Collections.EMPTY_LIST if selection is null or
	 *         empty.
	 */
	public static IStructuredSelection editPart2Model( ISelection selection )
	{
		if ( selection == null || !( selection instanceof IStructuredSelection ) )
			return new StructuredSelection( Collections.EMPTY_LIST );
		List list = ( (IStructuredSelection) selection ).toList( );
		List resultList = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof ReportElementEditPart )
			{
				Object model = ( (ReportElementEditPart) obj ).getModel( );
				if ( model instanceof ListBandProxy )
				{
					model = ( (ListBandProxy) model ).getSlotHandle( );
				}
				resultList.add( model );
			}
		}
		return new StructuredSelection( resultList );
	}

	/**
	 * Converts edit part selection into model selection.
	 * 
	 * @param selection
	 *            edit part
	 * @return model, return Collections.EMPTY_LIST if selection is null or
	 *         empty.
	 */
	public static IStructuredSelection editPart2Model( List selection )
	{
		if ( selection == null || ( selection.size( ) == 0 ) )
			return new StructuredSelection( Collections.EMPTY_LIST );
		List list = selection;
		List resultList = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof ReportElementEditPart )
			{
				Object model = ( (ReportElementEditPart) obj ).getModel( );
				if ( model instanceof ListBandProxy )
				{
					model = ( (ListBandProxy) model ).getSlotHandle( );
				}
				resultList.add( model );
			}
		}
		return new StructuredSelection( resultList );
	}

	private static DataSetHandle getDataSet( Object column )
	{
		if ( column instanceof ResultSetColumnHandle )
		{
			return getDataSet( (ResultSetColumnHandle) column );
		}
		return null;
	}

	private static DataSetHandle getDataSet( ResultSetColumnHandle column )
	{
		DataSetHandle dataSet;

		if ( getAdapter( ) != null
				&& getAdapter( ).getDataSet( column ) != null )
		{
			dataSet = getAdapter( ).getDataSet( column );
		}
		else
		{
			dataSet = (DataSetHandle) column.getElementHandle( );
		}

		return dataSet;
	}

	private static DataSetHandle getDataSet( MeasureHandle column )
	{
		DataSetHandle dataSet;

		if ( getAdapter( ) != null
				&& getAdapter( ).getDataSet( column ) != null )
		{
			dataSet = getAdapter( ).getDataSet( column );
		}
		else
		{
			dataSet = (DataSetHandle) column.getContainer( ).getContainer( );
		}

		return dataSet;
	}

	private static void setDataSet( ReportItemHandle target,
			DataSetHandle dataSet ) throws SemanticException
	{
		if ( getAdapter( ) != null
				&& getAdapter( ).resolveExtendedData( dataSet ) != null )
		{
			getAdapter( ).setExtendedData( target,
					getAdapter( ).resolveExtendedData( dataSet ) );
		}
		else
		{
			target.setDataSet( dataSet );
		}
	}

	private static IExtendedDataModelUIAdapter getAdapter( )
	{
		return ExtendedDataModelUIAdapterHelper.getInstance( ).getAdapter( );
	}
}
