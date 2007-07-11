
package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class DataColumnBindingDialog extends BaseDialog
{

	protected static final String DEFAULT_ITEM_NAME = "data column"; //$NON-NLS-1$

	protected static final String NEW_DATAITEM_TITLE = Messages.getString( "DataColumBindingDialog.title.CreateNewDataBinding" ); //$NON-NLS-1$

	protected static final String EDIT_DATAITEM_TITLE = Messages.getString( "DataColumBindingDialog.title.EditDataBinding" ); //$NON-NLS-1$
	
	protected static final String AGG_BUILDER_TITLE = Messages.getString( "DataColumBindingDialog.title.AggBuilder" ); //$NON-NLS-1$

	IBindingDialogHelper dialogHelper;

	private ReportItemHandle bindingObject;

	private ComputedColumnHandle bindingColumn;

	private ExpressionProvider expressionProvider;

	private boolean isAggregate;

	public DataColumnBindingDialog( boolean isCreateNew )
	{
		super( isCreateNew == true ? NEW_DATAITEM_TITLE : EDIT_DATAITEM_TITLE );
	}

	public void setInput( ReportItemHandle input )
	{
		setInput( input, null );
	}

	public DesignElementHandle getBindingObject( )
	{
		return bindingObject;
	}

	public ComputedColumnHandle getBindingColumn( )
	{
		return this.bindingColumn;
	}
	
	public void setInput( ReportItemHandle bindingObject,
			ComputedColumnHandle bindingColumn, Object container )
	{
		this.setInput( bindingObject, bindingColumn );
		if(dialogHelper!=null)
			dialogHelper.setDataItemContainer( container );
	}

	public void setInput( ReportItemHandle bindingObject,
			ComputedColumnHandle bindingColumn )
	{
		this.bindingObject = bindingObject;
		//setAggregateOns( DEUtil.getGroups( input ) );
		//		setDataTypes( ChoiceSetFactory.getDisplayNamefromChoiceSet( DATA_TYPE_CHOICE_SET ) );
		this.bindingColumn = bindingColumn;
		//		try
		//		{
		//			if ( isCreateNew || bindingColumn == null )
		//			{
		//				createColumnName( input, DEFAULT_ITEM_NAME );
		//				setTypeSelect( dataTypes[0] );
		//			}
		//			else
		//			{
		//				// Add data set items.
		//
		//				setName( bindingColumn.getName( ) );
		//				setDisplayName( bindingColumn.getDisplayName( ) );
		//				setTypeSelect( DATA_TYPE_CHOICE_SET.findChoice( bindingColumn.getDataType( ) )
		//						.getDisplayName( ) );
		//				setExpression( bindingColumn.getExpression( ) );
		//				//setAggregateOnSelect( bindingColumn.getAggregateOn( ) );
		//			}
		//
		//		}
		//		catch ( Exception e )
		//		{
		//			ExceptionHandler.handle( e );
		//		}

		dialogHelper = (IBindingDialogHelper) ElementAdapterManager.getAdapter( DEUtil.getBindingHolder( bindingObject ),
				IBindingDialogHelper.class );
		dialogHelper.setBindingHolder( DEUtil.getBindingHolder( bindingObject ) );
		dialogHelper.setBinding( bindingColumn );
		dialogHelper.setDialog( this );
		if ( isAggregate )
		{
			dialogHelper.setAggregate( isAggregate );
		}
		if ( isAggregate
				|| ( bindingColumn != null
						&& bindingColumn.getAggregateFunction( ) != null && !bindingColumn.getAggregateFunction( )
						.equals( "" ) ) )
		{
			setTitle( AGG_BUILDER_TITLE );
		}
	}

	public void setAggreate( boolean isAggregate )
	{
		this.isAggregate = isAggregate;
		if ( isAggregate )
		{
			setTitle( AGG_BUILDER_TITLE );
		}
		if ( this.dialogHelper != null )
		{
			this.dialogHelper.setAggregate( isAggregate );
		}
	}

	public void setExpressionProvider( ExpressionProvider expressionProvider )
	{
		this.expressionProvider = expressionProvider;
	}

	protected boolean isForceBinding( )
	{
		return true;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );

		dialogHelper.setExpressionProvider( expressionProvider );
		dialogHelper.createContent( composite );
		UIUtil.bindHelp( composite, IHelpContextIds.DATA_COLUMN_BINDING_DIALOG );
		return composite;
	}

	protected void okPressed( )
	{
		try
		{
			dialogHelper.save( );
			this.bindingColumn = dialogHelper.getBindingColumn( );
			super.okPressed( );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		dialogHelper.validate( );
	}

	protected boolean initDialog( )
	{
		dialogHelper.initDialog( );
		return super.initDialog( );
	}

	public void setCanFinish( boolean canFinish )
	{
		if ( getOkButton( ) != null )
			getOkButton( ).setEnabled( canFinish );
	}
}
