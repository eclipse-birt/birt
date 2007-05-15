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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Table;

/**
 * 
 */

public class DataSetColumnBindingsFormHandleProvider extends
		AbstractFormHandleProvider
{

	private static final String ALL = Messages.getString( "DataSetColumnBindingsFormHandleProvider.ALL" );//$NON-NLS-1$
	private static final String NONE = Messages.getString( "DataSetColumnBindingsFormHandleProvider.NONE" );//$NON-NLS-1$

	private String[] columnNames = new String[]{
			Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Name" ),
			Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.DataType" ),
			Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Expression" ),
			Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Filter" ),			
			Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.AggregateOn" )//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	};

	private CellEditor[] editors;

	private static int[] columnWidth = new int[]{
			150, 150, 150, 150, 150
	};

	// object to add data binding.
	private ReportElementHandle bindingObject;

	private static final IChoice[] DATA_TYPE_CHOICES = DEUtil.getMetaDataDictionary( )
			.getStructure( ComputedColumn.COMPUTED_COLUMN_STRUCT )
			.getMember( ComputedColumn.DATA_TYPE_MEMBER )
			.getAllowedChoices( )
			.getChoices( );

	public DataSetColumnBindingsFormHandleProvider( )
	{
	}

	/**
	 * @return the bindingObject
	 */
	public ReportElementHandle getBindingObject( )
	{
		return bindingObject;
	}

	/**
	 * @param bindingObject
	 *            the bindingObject to set
	 */

	public void setBindingObject( ReportElementHandle bindingObject )
	{
		this.bindingObject = bindingObject;
	}

	public String[] getColumnNames( )
	{
		return columnNames;
	}

	public int[] getColumnWidths( )
	{
		return columnWidth;
	}

	public String getDisplayName( )
	{
		return Messages.getString( "DataSetColumnBindingsFormHandleProvider.TableTitle" ); //$NON-NLS-1$
	}

	public CellEditor[] getEditors( Table table )
	{
		if ( editors == null )
		{
			editors = new CellEditor[columnNames.length];

			for ( int i = 0; i < editors.length; i++ )
			{
				editors[i] = new TextCellEditor( table );
			}
		}
		return editors;
	}

	public boolean doMoveItem( int oldPos, int newPos ) throws Exception
	{
		return false;
	}

	public boolean doDeleteItem( int pos ) throws Exception
	{
		if ( pos > -1 )
		{
			if ( bindingObject instanceof ReportItemHandle )
			{
				( (ReportItemHandle) bindingObject ).getColumnBindings( )
						.getAt( pos )
						.drop( );
				return true;
			}
			// if ( bindingObject instanceof GroupHandle )
			// {
			// ( (GroupHandle) bindingObject ).getColumnBindings( )
			// .getAt( pos )
			// .drop( );
			// return true;
			// }
		}
		return false;
	}

	public boolean doAddItem( int pos ) throws Exception
	{
		DataColumnBindingDialog dialog = new DataColumnBindingDialog( true );
		dialog.setInput( (ReportItemHandle) bindingObject );

		if ( dialog.open( ) == Dialog.OK )
		{
			if ( viewer != null )
			{
				viewer.refresh( true );
				return true;
			}
		}
		return false;
	}

	public boolean doEditItem( int pos )
	{
		ComputedColumnHandle bindingHandle = null;
		if ( pos > -1 )
		{
			if ( bindingObject instanceof ReportItemHandle )
			{
				bindingHandle = (ComputedColumnHandle) ( (ReportItemHandle) bindingObject ).getColumnBindings( )
						.getAt( pos );
			}
			// if ( bindingObject instanceof GroupHandle )
			// {
			// bindingHandle = (ComputedColumnHandle) ( (GroupHandle)
			// bindingObject ).getColumnBindings( )
			// .getAt( pos );
			// }
		}
		if ( bindingHandle == null )
			return false;

		boolean isResultSetColumn = false;
		String resultSetName = null;
		if ( bindingObject instanceof DataItemHandle )
			resultSetName = ( (DataItemHandle) bindingObject ).getResultSetColumn( );
		if ( resultSetName != null
				&& bindingHandle.getName( ).equals( resultSetName ) )
			isResultSetColumn = true;

		DataColumnBindingDialog dialog = new DataColumnBindingDialog( false );
		dialog.setInput( (ReportItemHandle) bindingObject, bindingHandle );

		if ( dialog.open( ) == Dialog.OK )
		{
			if ( isResultSetColumn )
			{
				try
				{
					( (DataItemHandle) bindingObject ).setResultSetColumn( bindingHandle.getName( ) );
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}
			if ( viewer != null )
			{
				viewer.refresh( true );
				return true;
			}
			/*
			 * if ( bindingObject instanceof ReportItemHandle ) { if ( viewer !=
			 * null ) viewer.editElement( viewer.getElementAt( pos ), 0 );
			 * return true; } if ( bindingObject instanceof GroupHandle ) { if (
			 * viewer != null ) viewer.editElement( viewer.getElementAt( pos ),
			 * 0 ); return true; }
			 */
		}
		return false;
	}

	public String getColumnText( Object element, int columnIndex )
	{
		switch ( columnIndex )
		{
			case 0 :
				return ( (ComputedColumnHandle) element ).getName( );
			case 1 :
				return getDataTypeDisplayName( ( (ComputedColumnHandle) element ).getDataType( ) );
			case 2 :
				return ( (ComputedColumnHandle) element ).getExpression( );
			case 3 :
				String ExpValue = ( (ComputedColumnHandle) element ).getFilterExpression( );
				if(ExpValue != null && ExpValue.length( ) > 0)
				{
					return ExpValue;
				}else
				{
					return null;
				}
			case 4 :
				String value = ( (ComputedColumnHandle) element ).getAggregrateOn( );
				String groupType = DEUtil.getGroupControlType( bindingObject );
				String text;
				if ( value == null )
				{
					if ( ExpressionUtil.hasAggregation( ( (ComputedColumnHandle) element ).getExpression( ) )
							&& groupType != DEUtil.TYPE_GROUP_NONE )
					{
						text = ALL;
					}
					else
						text = NONE;
				}
				else
					text = value;

				return text;
			default :
				break;
		}
		return null;
	}

	private String getDataTypeDisplayName( String dataType )
	{
		for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
		{
			IChoice choice = DATA_TYPE_CHOICES[i];
			if ( choice.getName( ).equals( dataType ) )
			{
				return choice.getDisplayName( );
			}
		}
		return dataType;
	}

	public String getImagePath( Object element, int columnIndex )
	{
		return null;
	}

	public Object[] getElements( Object inputElement )
	{
		if ( inputElement instanceof Object[]
				&& ( (Object[]) inputElement ).length > 0 )
		{
			return getElements( ( (Object[]) inputElement )[0] );
		}
		if ( inputElement instanceof List )
		{
			return getElements( ( (List) inputElement ).get( 0 ) );
		}
		if ( inputElement instanceof ReportItemHandle )
		{
			ReportItemHandle reportHandle = (ReportItemHandle) inputElement;
			this.bindingObject = reportHandle;
			List children = new ArrayList( );
			for ( Iterator iter = reportHandle.getColumnBindings( ).iterator( ); iter.hasNext( ); )
			{
				children.add( iter.next( ) );
			}
			return children.toArray( );
		}
		// if ( inputElement instanceof GroupHandle )
		// {
		// GroupHandle groupHandle = (GroupHandle) inputElement;
		// this.bindingObject = groupHandle;
		// List children = new ArrayList( );
		// for ( Iterator iter = groupHandle.getColumnBindings( ).iterator( );
		// iter.hasNext( ); )
		// {
		// children.add( iter.next( ) );
		// }
		// return children.toArray( new ComputedColumnHandle[children.size( )]
		// );
		// }
		return new Object[]{};
	}

	public boolean canModify( Object element, String property )
	{
		return false;
	}

	public Object getValue( Object element, String property )
	{
		int index = Arrays.asList( columnNames ).indexOf( property );

		String columnText = getColumnText( element, index );
		return columnText;
	}

	public boolean modify( Object data, String property, Object value )
			throws Exception
	{
		/*
		 * if ( value == null ) return false; int index = Arrays.asList(
		 * columnNames ).indexOf( property ); switch ( index ) { case 0 : if ( !( (
		 * (ComputedColumnHandle) data ).getName( ) != null && (
		 * (ComputedColumnHandle) data ).getName( ) .equals( value.toString(
		 * ).trim( ) ) ) ) { ( (ComputedColumnHandle) data ).setName(
		 * value.toString( ) ); } break; case 1 : ( (ComputedColumnHandle) data
		 * ).setDataType( getDataType( value.toString( ) ) ); break; case 2 : if ( !( (
		 * (ComputedColumnHandle) data ).getExpression( ) != null && (
		 * (ComputedColumnHandle) data ).getExpression( ) .equals( (String)
		 * value ) ) ) { ( (ComputedColumnHandle) data ).setExpression(
		 * value.toString( ) ); String groupType = DEUtil.getGroupControlType(
		 * bindingObject ); if ( ExpressionUtil.hasAggregation( (
		 * (ComputedColumnHandle) data ).getExpression( ) ) ) { if (
		 * groupType.equals( DEUtil.TYPE_GROUP_GROUP ) ) (
		 * (ComputedColumnHandle) data ).setAggregrateOn( ( (GroupHandle)
		 * DEUtil.getGroups( bindingObject ) .get( 0 ) ).getName( ) ); else if (
		 * groupType.equals( DEUtil.TYPE_GROUP_LISTING ) ) (
		 * (ComputedColumnHandle) data ).setAggregrateOn( null ); } if (
		 * !ExpressionUtil.hasAggregation( ( (ComputedColumnHandle) data
		 * ).getExpression( ) ) || groupType.equals( DEUtil.TYPE_GROUP_NONE ) ) { (
		 * (ComputedColumnHandle) data ).setAggregrateOn( null ); } } break;
		 * case 3 : if ( ALL.equals( value.toString( ) ) ) (
		 * (ComputedColumnHandle) data ).setAggregrateOn( null ); else (
		 * (ComputedColumnHandle) data ).setAggregrateOn( value.toString( ) );
		 * break; default : break; }
		 */
		return false;
	}

	private String getDataType( String value )
	{
		for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
		{
			IChoice choice = DATA_TYPE_CHOICES[i];
			if ( choice.getDisplayName( ).equals( value ) )
			{
				return choice.getName( );
			}
		}
		return value;
	}

	public boolean needRefreshed( NotificationEvent event )
	{
		if ( event.getEventType( ) == NotificationEvent.PROPERTY_EVENT )
		{
			PropertyEvent ev = (PropertyEvent) event;
			String propertyName = ev.getPropertyName( );
			if ( ReportItemHandle.BOUND_DATA_COLUMNS_PROP.equals( propertyName )
					|| ReportItemHandle.DATA_SET_PROP.equals( propertyName ) )
			{
				return true;
			}
		}
		return false;
	}

	public void generateAllBindingColumns( )
	{
		if ( bindingObject != null )
		{
			DataSetHandle datasetHandle = null;
			if ( bindingObject instanceof ReportItemHandle )
			{
				datasetHandle = ( (ReportItemHandle) bindingObject ).getDataSet( );
			}
			else if ( bindingObject instanceof GroupHandle )
			{
				datasetHandle = ( (ReportItemHandle) ( (GroupHandle) bindingObject ).getContainer( ) ).getDataSet( );
			}
			if ( datasetHandle != null )
			{
				try
				{
					CachedMetaDataHandle cmdh = DataSetUIUtil.getCachedMetaDataHandle( datasetHandle );
					for ( Iterator iter = cmdh.getResultSet( ).iterator( ); iter.hasNext( ); )
					{
						ResultSetColumnHandle element = (ResultSetColumnHandle) iter.next( );
						ComputedColumn bindingColumn = StructureFactory.newComputedColumn( bindingObject,
								element.getColumnName( ) );
						bindingColumn.setDataType( element.getDataType( ) );
						String groupType = DEUtil.getGroupControlType( bindingObject );
						List groupList = DEUtil.getGroups( bindingObject );
						if ( !isExpressionExisit( DEUtil.getExpression( element ) ) )
						{
							bindingColumn.setExpression( DEUtil.getExpression( element ) );

							if ( bindingObject instanceof ReportItemHandle )
							{
								( (ReportItemHandle) bindingObject ).addColumnBinding( bindingColumn,
										false );
								continue;
							}
							// if ( bindingObject instanceof GroupHandle )
							// {
							// ( (GroupHandle) bindingObject ).addColumnBinding(
							// bindingColumn,
							// false );
							// }
						}
						if ( ExpressionUtil.hasAggregation( bindingColumn.getExpression( ) ) )
						{
							if ( groupType.equals( DEUtil.TYPE_GROUP_GROUP ) )
								bindingColumn.setAggregrateOn( ( (GroupHandle) groupList.get( 0 ) ).getName( ) );
							else if ( groupType.equals( DEUtil.TYPE_GROUP_LISTING ) )
								bindingColumn.setAggregrateOn( null );
						}

					}
				}
				catch ( SemanticException e )
				{
				}
			}
		}
	}

	public void removedUnusedColumnBindings( List inputElement )
	{
		if ( inputElement.size( ) > 0 )
		{
			Object element = inputElement.get( 0 );
			if ( element instanceof ReportElementHandle )
			{
				try
				{
					if ( element instanceof GroupHandle )
					{
						DesignElementHandle parentHandle = ( (GroupHandle) element ).getContainer( );
						if ( parentHandle instanceof ReportItemHandle )
						{
							( (ReportItemHandle) parentHandle ).removedUnusedColumnBindings( );
						}
					}
					else if ( element instanceof ReportItemHandle )
					{
						( (ReportItemHandle) element ).removedUnusedColumnBindings( );
					}
				}
				catch ( SemanticException e )
				{
					e.printStackTrace( );
				}
			}
		}
	}

	private boolean isExpressionExisit( String expression )
	{
		if ( bindingObject != null )
		{
			if ( bindingObject instanceof ReportItemHandle )
			{
				for ( Iterator iter = ( (ReportItemHandle) bindingObject ).getColumnBindings( )
						.iterator( ); iter.hasNext( ); )
				{
					if ( expression.equals( ( (ComputedColumnHandle) iter.next( ) ).getExpression( ) ) )
						return true;
				}
			}
			// if ( bindingObject instanceof GroupHandle )
			// {
			// for ( Iterator iter = ( (GroupHandle) bindingObject
			// ).getColumnBindings( )
			// .iterator( ); iter.hasNext( ); )
			// {
			// if ( expression.equals( ( (ComputedColumnHandle) iter.next( )
			// ).getExpression( ) ) )
			// return true;
			// }
			// }
		}
		return false;
	}

	private IExpressionProvider expressionProvider;

	public void setExpressionProvider( IExpressionProvider expressionProvider )
	{
		this.expressionProvider = expressionProvider;
	}

	protected TableViewer viewer;

	public void setTableViewer( TableViewer viewer )
	{
		this.viewer = viewer;
	}

}