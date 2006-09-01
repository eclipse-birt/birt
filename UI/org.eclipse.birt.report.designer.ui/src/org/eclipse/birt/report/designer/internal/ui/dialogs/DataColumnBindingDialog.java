
package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

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
		setDataTypes( ChoiceSetFactory.getDisplayNamefromChoiceSet( DATA_TYPE_CHOICE_SET ) );
		if ( bindingHandle != null )
			setTitle( EDIT_DATAITEM_TITLE );
		bindingColumn = bindingHandle;
		try
		{
			if ( bindingColumn == null )
			{
				createColumnName( input, DEFAULT_ITEM_NAME );
				setTypeSelect( dataTypes[0] );
			}
			else
			{
				// Add data set items.

				setName( bindingColumn.getName( ) );
				setTypeSelect( DATA_TYPE_CHOICE_SET.findChoice( bindingColumn.getDataType( ) )
						.getDisplayName( ) );
				setExpression( bindingColumn.getExpression( ) );
				setAggregateOnSelect( bindingColumn.getAggregateOn( ) );
			}

		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}

	protected boolean isForceBinding( )
	{
		return true;
	}

	protected Control createDialogArea( Composite parent )
	{
		Control control = super.createDialogArea( parent );
		UIUtil.bindHelp(control, IHelpContextIds.DATA_COLUMN_BINDING_DIALOG);
		return control;
	}
	protected static final String DEFAULT_ITEM_NAME = "data column";

	protected static final String NEW_DATAITEM_TITLE = Messages.getString( "DataColumBindingDialog.title.CreateNewDataBinding" );

	protected static final String EDIT_DATAITEM_TITLE = Messages.getString( "DataColumBindingDialog.title.EditDataBinding" );

}
