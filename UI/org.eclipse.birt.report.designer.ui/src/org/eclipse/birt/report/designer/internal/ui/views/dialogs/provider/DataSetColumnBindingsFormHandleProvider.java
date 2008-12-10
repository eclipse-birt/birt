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

package org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.CommandStack;
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
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

/**
 * 
 */

public class DataSetColumnBindingsFormHandleProvider implements
		ISortingFormHandleProvider
{

	protected static Logger logger = Logger.getLogger( DataSetColumnBindingsFormHandleProvider.class.getName( ) );

	private static final String ALL = Messages.getString( "DataSetColumnBindingsFormHandleProvider.ALL" );//$NON-NLS-1$
	private static final String NONE = Messages.getString( "DataSetColumnBindingsFormHandleProvider.NONE" );//$NON-NLS-1$

	private String[] columnNames;

	// object to add data binding.
	private ReportElementHandle bindingObject;

	private static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
			.getStructure( ComputedColumn.COMPUTED_COLUMN_STRUCT )
			.getMember( ComputedColumn.DATA_TYPE_MEMBER )
			.getAllowedChoices( );

	public DataSetColumnBindingsFormHandleProvider( )
	{
	}

	private boolean canAggregation = true;

	public DataSetColumnBindingsFormHandleProvider( boolean canAggregation )
	{
		this.canAggregation = canAggregation;
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
		if ( canAggregation )
			columnNames = new String[]{
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Name" ),//$NON-NLS-1$
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.DisplayName" ),//$NON-NLS-1$
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.DataType" ), //$NON-NLS-1$
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Expression" ),
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Function" ),//$NON-NLS-1$
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Filter" ),//$NON-NLS-1$
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.AggregateOn" )//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			};
		else
			columnNames = new String[]{
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Name" ),//$NON-NLS-1$
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.DisplayName" ),//$NON-NLS-1$
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.DataType" ), //$NON-NLS-1$
					Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Expression" ),//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			};
		return columnNames;
	}

	public int[] getColumnWidths( )
	{
		if ( canAggregation )
			return new int[]{
					130, 130, 70, 130, 100, 130, 130
			};
		else
			return new int[]{
					150, 150, 150, 150
			};
	}

	public String getTitle( )
	{
		if ( isEditable( ) )
			return Messages.getString( "DataSetColumnBindingsFormHandleProvider.DatasetTitle" ); //$NON-NLS-1$
		else
			return Messages.getString( "DataSetColumnBindingsFormHandleProvider.ReportItemTitle" ); //$NON-NLS-1$
	}

	public boolean isEditable( )
	{
		if ( bindingObject == null )
			return false;
		else if ( ( (ReportItemHandle) DEUtil.getInputFirstElement( bindingObject ) ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			return false;
		else if ( ( (ReportItemHandle) DEUtil.getInputFirstElement( bindingObject ) ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_NONE )
		{
			if ( DEUtil.getBindingHolder( (ReportItemHandle) DEUtil.getInputFirstElement( bindingObject ),
					true ) != null
					&& DEUtil.getBindingHolder( (ReportItemHandle) DEUtil.getInputFirstElement( bindingObject ),
							true )
							.getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
				return false;
			else
				return true;
		}
		else
			return true;
	}

	public boolean doDeleteItem( int pos ) throws Exception
	{
		int modelPos = getOriginalIndex( pos );
		if ( modelPos > -1 )
		{
			if ( bindingObject instanceof ReportItemHandle )
			{
				( (ReportItemHandle) bindingObject ).getColumnBindings( )
						.getAt( modelPos )
						.drop( );
				if ( viewer != null )
				{
					viewer.refresh( true );
					if ( pos - 1 > -1
							|| viewer.getTable( ).getItemCount( ) == 0 )
						viewer.getTable( ).setSelection( pos - 1 );
					else
					{
						viewer.getTable( ).setSelection( 0 );
					}
					return true;
				}
				return true;
			}
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
		pos = getOriginalIndex( pos );
		if ( pos > -1 )
		{
			if ( bindingObject instanceof ReportItemHandle )
			{
				bindingHandle = (ComputedColumnHandle) ( (ReportItemHandle) bindingObject ).getColumnBindings( )
						.getAt( pos );
			}
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
		}
		return false;
	}

	public int getOriginalIndex( int pos )
	{
		List children = new ArrayList( );
		for ( Iterator iter = ( (ReportItemHandle) bindingObject ).getColumnBindings( )
				.iterator( ); iter.hasNext( ); )
		{
			children.add( iter.next( ) );
		}

		Object[] arrays = children.toArray( );
		Arrays.sort( arrays, new BindingComparator( ) );
		return children.indexOf( Arrays.asList( arrays ).get( pos ) );
	}

	public int getShowIndex( int pos )
	{
		List children = new ArrayList( );
		for ( Iterator iter = ( (ReportItemHandle) bindingObject ).getColumnBindings( )
				.iterator( ); iter.hasNext( ); )
		{
			children.add( iter.next( ) );
		}

		Object[] arrays = children.toArray( );
		Arrays.sort( arrays, new BindingComparator( ) );
		return Arrays.asList( arrays ).indexOf( children.get( pos ) );
	}

	public String getColumnText( Object element, int columnIndex )
	{
		ComputedColumnHandle handle = ( (ComputedColumnHandle) element );
		String text = null;

		switch ( columnIndex )
		{
			case 0 :
				text = handle.getName( );
				break;
			case 1 :
				text = handle.getDisplayName( );
				break;
			case 2 :
				text = ChoiceSetFactory.getDisplayNameFromChoiceSet( handle.getDataType( ),
						DATA_TYPE_CHOICE_SET );
				break;
			case 3 :
				text = org.eclipse.birt.report.designer.data.ui.util.DataUtil.getAggregationExpression( handle );
				break;
			case 4 :
				try
				{
					String function = handle.getAggregateFunction( );
					if ( function != null )
						text = DataUtil.getAggregationManager( )
								.getAggregation( function )
								.getDisplayName( );
				}
				catch ( BirtException e )
				{
					ExceptionHandler.handle( e );
					text = null;
				}
				break;
			case 5 :
				text = handle.getFilterExpression( );
				break;
			case 6 :
				String value = DEUtil.getAggregateOn( handle );
				if ( value == null )
				{
					if ( handle.getAggregateFunction( ) != null )
					{
						text = ALL;
					}
					else
						text = NONE;
				}
				else
				{
					text = value;
				}

				break;
		}

		if ( text == null )
		{
			text = ""; //$NON-NLS-1$
		}
		return text;
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
			ReportItemHandle reportHandle = DEUtil.getBindingRoot( (ReportItemHandle) inputElement );
			this.bindingObject = reportHandle;
			List children = new ArrayList( );
			if ( reportHandle != null )
			{
				for ( Iterator iter = reportHandle.getColumnBindings( )
						.iterator( ); iter.hasNext( ); )
				{
					children.add( iter.next( ) );
				}
			}
			Object[] arrays = children.toArray( );
			Arrays.sort( arrays, new BindingComparator( ) );
			return arrays;
		}
		return new Object[]{};
	}

	private class BindingComparator implements Comparator
	{

		public int compare( Object o1, Object o2 )
		{
			ComputedColumnHandle binding1 = (ComputedColumnHandle) o1;
			ComputedColumnHandle binding2 = (ComputedColumnHandle) o2;
			String columnText1 = getColumnText( binding1, sortingColumnIndex );
			String columnText2 = getColumnText( binding2, sortingColumnIndex );
			int result = ( columnText1 == null ? "" : columnText1 ).compareTo( ( columnText2 == null ? ""
					: columnText2 ) );
			if ( sortDirection == SWT.UP )
				return result;
			else
				return 0 - result;
		}
	}

	public Object getValue( Object element, String property )
	{
		int index = Arrays.asList( columnNames ).indexOf( property );

		String columnText = getColumnText( element, index );
		return columnText;
	}

	public boolean needRefreshed( NotificationEvent event )
	{
		if ( event.getEventType( ) == NotificationEvent.PROPERTY_EVENT )
		{
			PropertyEvent ev = (PropertyEvent) event;
			String propertyName = ev.getPropertyName( );
			if ( ReportItemHandle.BOUND_DATA_COLUMNS_PROP.equals( propertyName )
					|| ReportItemHandle.DATA_SET_PROP.equals( propertyName )
					|| ReportItemHandle.DATA_BINDING_REF_PROP.equals( propertyName ) )
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
				ReportItemHandle root = DEUtil.getBindingRoot( (ReportItemHandle) bindingObject );
				if ( root != null )
				{
					datasetHandle = root.getDataSet( );
				}
			}
			else if ( bindingObject instanceof GroupHandle )
			{
				ReportItemHandle root = DEUtil.getBindingRoot( (ReportItemHandle) ( (GroupHandle) bindingObject ).getContainer( ) );
				if ( root != null )
				{
					datasetHandle = root.getDataSet( );
				}
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

						if ( ExpressionUtil.hasAggregation( bindingColumn.getExpression( ) ) )
						{
							if ( groupType.equals( DEUtil.TYPE_GROUP_GROUP ) )
								bindingColumn.setAggregateOn( ( (GroupHandle) groupList.get( 0 ) ).getName( ) );
							else if ( groupType.equals( DEUtil.TYPE_GROUP_LISTING ) )
								bindingColumn.setAggregateOn( null );
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
					logger.log( Level.SEVERE, e.getMessage( ), e );
				}
			}
		}
	}

	private TableViewer viewer;

	public void setTableViewer( TableViewer viewer )
	{
		this.viewer = viewer;
	}

	public boolean canAggregation( )
	{
		return canAggregation;
	}

	public void addAggregateOn( int pos ) throws Exception
	{
		boolean sucess = false;
		CommandStack stack = getActionStack( );
		stack.startTrans( Messages.getString( "FormPage.Menu.ModifyProperty" ) ); //$NON-NLS-1$
		try
		{
			sucess = doAddAggregateOnItem( pos );
		}
		catch ( Exception e )
		{
			stack.rollback( );
			throw new Exception( e );
		}
		if ( sucess )
		{
			stack.commit( );
		}
		else
		{
			stack.rollback( );
		}
	}

	public boolean doAddAggregateOnItem( int pos )
	{
		DataColumnBindingDialog dialog = new DataColumnBindingDialog( true );
		dialog.setAggreate( true );
		dialog.setInput( (ReportItemHandle) getBindingObject( ) );
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

	protected CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	private int sortingColumnIndex;

	public void setSortingColumnIndex( int index )
	{
		this.sortingColumnIndex = index;
	}

	private int sortDirection = SWT.UP;

	public void setSortDirection( int dir )
	{
		sortDirection = dir;
	}

}