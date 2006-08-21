package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

public class DataColumnBindingDialog extends DataItemBindingDialog
{

	public DataColumnBindingDialog( )
	{
		super( NEW_DATAITEM_TITLE );
	}
	
	public void setInput( ReportItemHandle input )
	{
		setInput( input, null );
	}
	
	protected DesignElementHandle getBindingObject( )
	{
		return input;
	}

	public void setInput( ReportItemHandle input,
			ComputedColumnHandle bindingHandle )
	{
		this.input = input;
		setAggregateOns( DEUtil.getGroups( input ) );
		setDataTypes( ChoiceSetFactory.getDisplayNamefromChoiceSet( dataTypeChoiceSet ) );
		if ( bindingHandle != null )
			setTitle( EDIT_DATAITEM_TITLE );
		bindingColumn = bindingHandle;
		try
		{
			List columnList = DataUtil.generateComputedColumns( input );
			if ( ( columnList == null || columnList.size( ) == 0 )
					&& bindingColumn == null )
			{
				setDataItemNames( new String[]{
					createColumnName( input, DEFAULT_ITEM_NAME )
				} );
				setNameSelect( itemNames[0] );
			}
			else
			{
				// Add data set items.
				boolean isBindingDataSet = false;
				List list = new LinkedList( );
				
				
				List bindingList = DEUtil.getAllColumnBindingList(   input ,true  );
				List bindingNameList = new LinkedList();
				for(int i=0;i<bindingList.size( );i++)bindingNameList.add( ((ComputedColumnHandle)bindingList.get( i )).getName( ) );
				
			
				for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
				{
					ComputedColumn resultSetColumn = (ComputedColumn) iter.next( );
					if ( bindingColumn != null){
						if(bindingColumn.getName( )
									.equals( resultSetColumn.getName( ) ) )
						isBindingDataSet = true;
						else if(bindingNameList.contains( resultSetColumn.getName( ) )) continue;
					}
					list.add( resultSetColumn.getName( ) );
				}

				setDataItemNames( convertListToStrings( list ) );

				if ( bindingColumn != null )
				{
					if ( !isBindingDataSet )
					{
						String names[] = new String[itemNames.length + 1];
						System.arraycopy( itemNames,
								0,
								names,
								1,
								itemNames.length );
						names[0] = bindingColumn.getName( );
						setDataItemNames( names );
					}
					setNameSelect( bindingColumn.getName( ) );
					setTypeSelect( dataTypeChoiceSet.findChoice( bindingColumn.getDataType( ) )
							.getDisplayName( ) );
					setExpression( bindingColumn.getExpression( ) );
					setAggregateOnSelect( bindingColumn.getAggregateOn( ) );
				}
				else
				{
					String names[] = new String[itemNames.length + 1];
					System.arraycopy( itemNames, 0, names, 1, itemNames.length );
					names[0] = createColumnName( input, DEFAULT_ITEM_NAME );
					setDataItemNames( names );
					setNameSelect( itemNames[0] );
					setTypeSelect( dataTypes[0] );
				}
			}
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}
	
	protected static final String DEFAULT_ITEM_NAME="data column";
	
	protected static final String NEW_DATAITEM_TITLE = Messages.getString( "DataColumBindingDialog.title.CreateNewDataBinding" );

	protected static final String EDIT_DATAITEM_TITLE = Messages.getString( "DataColumBindingDialog.title.EditDataBinding" );

}
