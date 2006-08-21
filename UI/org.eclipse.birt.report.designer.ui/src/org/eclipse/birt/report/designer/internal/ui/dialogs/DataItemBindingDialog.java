
package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.CCombo;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BindingExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.internal.win32.MSG;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class DataItemBindingDialog extends BaseDialog
{

	protected static final String NAME = Messages.getString( "DataItemBindingDialog.text.Name" );

	protected static final String DATA_TYPE = Messages.getString( "DataItemBindingDialog.text.DataType" );

	protected static final String EXPRESSION = Messages.getString( "DataItemBindingDialog.text.Expression" );

	protected static final String AGGREGATE_ON = Messages.getString( "DataItemBindingDialog.text.AggregateOn" );

	protected static final String FORCE_BINDING_TEXT = Messages.getString( "DataItemBindingDialog.text.ForceBinding" );

	protected static final String DEFAULT_ITEM_NAME = "data item";

	protected static final String ALL = Messages.getString( "DataItemBindingDialog.text.All" );

	protected static final String NONE = Messages.getString( "DataItemBindingDialog.text.None" );

	protected static final IChoiceSet dataTypeChoiceSet = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE );

	protected static final IChoice[] dataTypeChoices = dataTypeChoiceSet.getChoices( null );

	protected static final String NEW_DATAITEM_TITLE = Messages.getString( "DataItemBindingDialog.title.CreateNewItem" );

	protected static final String EDIT_DATAITEM_TITLE = Messages.getString( "DataItemBindingDialog.title.EditDataItem" );

	protected ReportItemHandle input;

	protected DesignElementHandle bindingObject;

	protected String[] dataTypes;

	protected String[] itemNames;

	protected String[] aggregateOns;

	protected String expression;

	private Combo itemType;

	private CCombo itemName;

	private Combo itemAggregateOn;

	private Text itemExpression;

	private String typeSelect;

	private String nameSelect;

	private String aggregateOnSelect;

	private Label aggregateOnLabel;

	private Label hiddenLabel;

	protected ComputedColumnHandle bindingColumn;

	public DataItemBindingDialog( )
	{
		super( NEW_DATAITEM_TITLE );
	}
	
	public DataItemBindingDialog( String title )
	{
		super( title );
	}

	protected String[] convertListToStrings( List list )
	{
		if ( list == null )
			return null;
		String[] strings = new String[list.size( )];
		for ( int i = 0; i < list.size( ); i++ )
		{
			strings[i] = list.get( i ).toString( );
		}
		return strings;
	}

	protected Control createDialogArea( Composite parent )
	{
		final Composite composite = (Composite) super.createDialogArea( parent );
		( (GridLayout) composite.getLayout( ) ).numColumns = 3;

		new Label( composite, SWT.NONE ).setText( NAME );
		itemName = new CCombo( composite, SWT.BORDER );
		GridData data = new GridData( );
		int width = itemName.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		data.widthHint = width < 250 ? 250 : width;
		itemName.setLayoutData( data );
		WidgetUtil.createGridPlaceholder( composite, 1, false );

		new Label( composite, SWT.NONE ).setText( DATA_TYPE );
		itemType = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		itemType.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );
		WidgetUtil.createGridPlaceholder( composite, 1, false );

		new Label( composite, SWT.NONE ).setText( EXPRESSION );
		itemExpression = new Text( composite, SWT.BORDER );
		itemExpression.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );

		Button expressionButton = new Button( composite, SWT.PUSH );
		expressionButton.setText( "..." );
		expressionButton.setLayoutData( new GridData( ) );
		expressionButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleExpressionButtonSelectEvent( );
			}
		} );

		itemExpression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				if ( ExpressionUtil.hasAggregation( itemExpression.getText( ) ) )
				{
					String groupType = DEUtil.getGroupControlType( input );
					if ( !( bindingColumn != null
							&& bindingColumn.getExpression( ) != null && bindingColumn.getExpression( )
							.equals( itemExpression.getText( ) ) ) )
					{
						if ( groupType.equals( DEUtil.TYPE_GROUP_GROUP ) )
						{
							setAggregateOnSelect( ( (GroupHandle) DEUtil.getGroups( input )
									.get( 0 ) ).getName( ) );
						}
						else if ( groupType.equals( DEUtil.TYPE_GROUP_LISTING ) )
						{
							setAggregateOnSelect( ALL );
						}
						else
							setAggregateOnSelect( NONE );
					}
					else if ( groupType.equals( DEUtil.TYPE_GROUP_NONE ) )
					{
						setAggregateOnSelect( NONE );
					}
				}
				if ( ExpressionUtil.hasAggregation( itemExpression.getText( ) )
						&& !itemAggregateOn.getVisible( ) )
				{
					aggregateOnLabel.setVisible( true );
					itemAggregateOn.setVisible( true );
					hiddenLabel.setVisible( true );
				}
				else if ( !ExpressionUtil.hasAggregation( itemExpression.getText( ) )
						&& itemAggregateOn.getVisible( ) )
				{
					aggregateOnLabel.setVisible( false );
					itemAggregateOn.setVisible( false );
					hiddenLabel.setVisible( false );
				}
			}
		} );

		aggregateOnLabel = new Label( composite, SWT.NONE );
		aggregateOnLabel.setText( AGGREGATE_ON );
		itemAggregateOn = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		itemAggregateOn.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		hiddenLabel = WidgetUtil.createGridPlaceholder( composite, 1, false );
		aggregateOnLabel.setVisible( false );
		itemAggregateOn.setVisible( false );
		hiddenLabel.setVisible( false );

		if ( bindingColumn== null )
		{
			btnForce = new Button( composite, SWT.CHECK );
			btnForce.setText( FORCE_BINDING_TEXT );
			data = new GridData( GridData.FILL_HORIZONTAL );
			data.horizontalSpan = 3;
			btnForce.setLayoutData( data );
		}

		init( );
		return composite;
	}

	private ComputedColumnHandle getInputBinding( ReportItemHandle input,
			String bindingName )
	{
		List elementsList = DEUtil.getVisiableColumnBindingsList( input );
		if ( elementsList != null && elementsList.size( ) > 0 )
		{
			for ( int i = 0; i < elementsList.size( ); i++ )
			{
				if ( ( (ComputedColumnHandle) elementsList.get( i ) ).getName( )
						.equals( bindingName ) )
					return (ComputedColumnHandle) elementsList.get( i );
			}
		}
		return null;
	}

	private int getItemIndex( String[] items, String item )
	{
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].equals( item ) )
				return i;
		}
		return -1;
	}

	private void init( )
	{
		initDataTypes( );
		initItemNames( );
		initAggregateOns( );
		initExpression( );
	}

	private void initAggregateOns( )
	{
		if ( aggregateOns != null && itemAggregateOn != null )
		{
			itemAggregateOn.setItems( aggregateOns );
			if ( aggregateOnSelect != null )
				itemAggregateOn.select( getItemIndex( itemAggregateOn.getItems( ),
						aggregateOnSelect ) );
			else
				itemAggregateOn.select( 0 );
		}
	}

	private void initDataTypes( )
	{
		if ( dataTypes != null && itemType != null )
		{
			itemType.setItems( dataTypes );
			if ( typeSelect != null )
				itemType.select( getItemIndex( itemType.getItems( ), typeSelect ) );
			else
				itemType.select( 0 );
		}
	}

	private void initExpression( )
	{
		if ( expression != null && itemExpression != null )
			itemExpression.setText( expression );
	}

	private void initItemNames( )
	{
		if ( itemNames != null && itemName != null )
		{
			itemName.setItems( itemNames );
			if ( nameSelect != null )
				itemName.select( getItemIndex( itemName.getItems( ), nameSelect ) );
		}
	}

	protected void setValue( ) throws SemanticException
	{
		if ( itemName.getText( ) != null
				&& itemName.getText( ).trim( ).length( ) > 0 )
		{

			if ( bindingColumn == null )
			{
				ComputedColumn newBinding = StructureFactory.newComputedColumn( input,
						itemName.getText( ) );
				for ( int i = 0; i < dataTypeChoices.length; i++ )
				{
					if ( dataTypeChoices[i].getDisplayName( )
							.endsWith( itemType.getText( ) ) )
					{
						newBinding.setDataType( dataTypeChoices[i].getName( ) );
						break;
					}
				}
				newBinding.setExpression( itemExpression.getText( ) );

				if ( itemAggregateOn.isVisible( )
						&& !( itemAggregateOn.getText( ).equals( ALL ) || itemAggregateOn.getText( )
								.equals( NONE ) ) )
				{
					newBinding.setAggregateOn( itemAggregateOn.getText( ) );
				}
				else
					newBinding.setAggregateOn( null );
				bindingColumn = DEUtil.addColumn( getBindingObject( ),
						newBinding,
						btnForce.getSelection( ) );
			}
			else
			{
				if ( !( bindingColumn.getName( ) != null && bindingColumn.getName( )
						.equals( itemName.getText( ).trim( ) ) ) )
					bindingColumn.setName( itemName.getText( ) );
				for ( int i = 0; i < dataTypeChoices.length; i++ )
				{
					if ( dataTypeChoices[i].getDisplayName( )
							.endsWith( itemType.getText( ) ) )
					{
						bindingColumn.setDataType( dataTypeChoices[i].getName( ) );
						break;
					}
				}

				bindingColumn.setExpression( itemExpression.getText( ) );

				if ( itemAggregateOn.isVisible( )
						&& !( itemAggregateOn.getText( ).equals( ALL ) || itemAggregateOn.getText( )
								.equals( NONE ) ) )
				{
					bindingColumn.setAggregateOn( itemAggregateOn.getText( ) );
				}
				else
					bindingColumn.setAggregateOn( null );

			}
		}
	}

	protected void setResultSetColumn( ) throws SemanticException
	{
		if ( input instanceof DataItemHandle )
		{
			( (DataItemHandle) input ).setResultSetColumn( bindingColumn.getName( ) );
		}
	}

	protected void okPressed( )
	{
		try
		{
			setValue( );
			setResultSetColumn( );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}

		super.okPressed( );
	}

	public void setAggregateOns( List aggregateOnList )
	{
		if ( aggregateOnList == null )
			setAggregateOns( new String[0] );
		String[] strings = new String[aggregateOnList.size( )];
		for ( int i = 0; i < aggregateOnList.size( ); i++ )
		{
			strings[i] = ( (GroupHandle) aggregateOnList.get( i ) ).getName( );
		}
		setAggregateOns( strings );
	}

	public void setAggregateOns( String[] aggregateOns )
	{
		if ( aggregateOns == null || aggregateOns.length == 0 )
		{
			if ( input != null
					&& DEUtil.getGroupControlType( input ) != DEUtil.TYPE_GROUP_NONE )
			{
				aggregateOns = new String[]{
					ALL
				};
			}
			else
				aggregateOns = new String[]{
					NONE
				};
			this.aggregateOns = aggregateOns;
		}
		else
		{
			this.aggregateOns = new String[aggregateOns.length + 1];
			this.aggregateOns[0] = ALL;
			System.arraycopy( aggregateOns,
					0,
					this.aggregateOns,
					1,
					aggregateOns.length );
		}
		initAggregateOns( );
	}

	public void setAggregateOnSelect( String aggregateOnSelect )
	{
		this.aggregateOnSelect = aggregateOnSelect;
		initAggregateOns( );
	}

	protected DesignElementHandle getBindingObject( )
	{
		return DEUtil.getBindingHolder( input );
	}

	public void setDataItemNames( String[] itemNames )
	{
		this.itemNames = itemNames;
		initItemNames( );
	}

	public void setDataTypes( String[] dataTypes )
	{
		this.dataTypes = dataTypes;
		initDataTypes( );
	}

	public void setExpression( String expression )
	{
		this.expression = expression;
		initExpression( );
	}

	protected String createColumnName( ReportItemHandle input, String name )
	{
		return StructureFactory.newComputedColumn( input, name ).getName( );
	}

	
	public void setInput( ReportItemHandle input )
	{
		this.input = input;
		setAggregateOns( DEUtil.getGroups( input ) );
		setDataTypes( ChoiceSetFactory.getDisplayNamefromChoiceSet( dataTypeChoiceSet ) );
		try
		{
			String bindingName = ( (DataItemHandle) input ).getResultSetColumn( );
			setTitle( bindingName == null ? NEW_DATAITEM_TITLE
					: EDIT_DATAITEM_TITLE );
			if ( bindingName != null )
			{
				bindingColumn = getInputBinding( input, bindingName );
			}
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
				
				List bindingList = DEUtil.getAllColumnBindingList(  input ,true );
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

	public void setNameSelect( String nameSelect )
	{
		this.nameSelect = nameSelect;
		initItemNames( );
	}

	public void setTypeSelect( String typeSelect )
	{
		this.typeSelect = typeSelect;
		initDataTypes( );
	}

	public String getExpression( )
	{
		return itemExpression.getText( );
	}

	public String getAggregateOnSelect( )
	{
		return itemAggregateOn.getText( );
	}

	public String getNameSelect( )
	{
		return itemName.getText( );
	}

	public String getTypeSelect( )
	{
		return itemType.getText( );
	}

	protected void handleExpressionButtonSelectEvent( )
	{
		ExpressionBuilder expression = new ExpressionBuilder( getExpression( ) );
		if ( expressionProvider != null )
			expression.setExpressionProvier( expressionProvider );
		else
			expression.setExpressionProvier( new BindingExpressionProvider( input ) );
		if ( expression.open( ) == OK )
		{
			setExpression( expression.getResult( ) );
		}
	}

	protected IExpressionProvider expressionProvider;

	protected Button btnForce;

	public void setExpressionProvider( IExpressionProvider provider )
	{
		expressionProvider = provider;
	}

	
	public Combo getItemAggregateOn( )
	{
		return itemAggregateOn;
	}

	
	public Text getItemExpression( )
	{
		return itemExpression;
	}

	
	public CCombo getItemName( )
	{
		return itemName;
	}

	
	public Combo getItemType( )
	{
		return itemType;
	}
}
