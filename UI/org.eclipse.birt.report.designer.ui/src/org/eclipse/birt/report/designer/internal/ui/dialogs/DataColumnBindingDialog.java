
package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * DataColumnBindingDialog
 */
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

	private boolean bindSelf = false;

	public DataColumnBindingDialog( boolean isCreateNew )
	{
		super( isCreateNew == true ? NEW_DATAITEM_TITLE : EDIT_DATAITEM_TITLE );
	}

	public DataColumnBindingDialog( boolean isCreateNew, boolean bindSelf )
	{
		super( isCreateNew == true ? NEW_DATAITEM_TITLE : EDIT_DATAITEM_TITLE );
		this.bindSelf = bindSelf;
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
		this.bindingObject = bindingObject;
		// setAggregateOns( DEUtil.getGroups( input ) );
		// setDataTypes( ChoiceSetFactory.getDisplayNamefromChoiceSet(
		// DATA_TYPE_CHOICE_SET ) );
		this.bindingColumn = bindingColumn;
		// try
		// {
		// if ( isCreateNew || bindingColumn == null )
		// {
		// createColumnName( input, DEFAULT_ITEM_NAME );
		// setTypeSelect( dataTypes[0] );
		// }
		// else
		// {
		// // Add data set items.
		//
		// setName( bindingColumn.getName( ) );
		// setDisplayName( bindingColumn.getDisplayName( ) );
		// setTypeSelect( DATA_TYPE_CHOICE_SET.findChoice(
		// bindingColumn.getDataType( ) )
		// .getDisplayName( ) );
		// setExpression( bindingColumn.getExpression( ) );
		// //setAggregateOnSelect( bindingColumn.getAggregateOn( ) );
		// }
		//
		// }
		// catch ( Exception e )
		// {
		// ExceptionHandler.handle( e );
		// }

		dialogHelper = (IBindingDialogHelper) ElementAdapterManager.getAdapter( DEUtil.getBindingHolder( bindingObject ),
				IBindingDialogHelper.class );

		if ( dialogHelper == null )
		{
			// use default helper.
			dialogHelper = new BindingDialogHelper( );
		}

		if ( !bindSelf )
			dialogHelper.setBindingHolder( DEUtil.getBindingHolder( bindingObject ) );
		else
			dialogHelper.setBindingHolder( bindingObject );
		dialogHelper.setBinding( bindingColumn );
		dialogHelper.setContainer( container );
		dialogHelper.setDialog( this );
		if ( isAggregate )
		{
			dialogHelper.setAggregate( isAggregate );
		}
		if ( isAggregate
				|| ( bindingColumn != null
						&& bindingColumn.getAggregateFunction( ) != null && !bindingColumn.getAggregateFunction( )
						.equals( "" ) ) ) //$NON-NLS-1$
		{
			setTitle( AGG_BUILDER_TITLE );
		}
	}

	public void setInput( ReportItemHandle bindingObject,
			ComputedColumnHandle bindingColumn )
	{
		this.setInput( bindingObject, bindingColumn, null );
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
		ScrolledComposite sc = new ScrolledComposite(composite, SWT.V_SCROLL);
		sc.setAlwaysShowScrollBars( false );
		sc.setExpandHorizontal( true );
		sc.setLayoutData( new GridData(GridData.FILL_BOTH) );
		
		Composite content = new Composite(sc, SWT.NONE);
		sc.setContent( content );
		content.setLayout( new GridLayout() );

//		sc.setBackground( Display.getCurrent( ).getSystemColor( SWT.COLOR_BLACK ) );
//		content.setBackground( Display.getCurrent( ).getSystemColor( SWT.COLOR_BLUE ) );
//		composite.setBackground( Display.getCurrent( ).getSystemColor( SWT.COLOR_RED ) );
		
		dialogHelper.setExpressionProvider( expressionProvider );
		dialogHelper.createContent( content );
		UIUtil.bindHelp( content, IHelpContextIds.DATA_COLUMN_BINDING_DIALOG );
		return content;
	}

	protected void okPressed( )
	{
		try
		{
			if ( bindingColumn != null )
			{
				if ( dialogHelper.differs( bindingColumn ) )
				{
					if ( isBindingMultipleReferenced( ) )
					{
						MessageDialog dialog = new MessageDialog( UIUtil.getDefaultShell( ),
								Messages.getString( "DataColumnBindingDialog.NewBindingDialogTitle" ), //$NON-NLS-1$
								null,
								Messages.getString( "DataColumnBindingDialog.NewBindingDialogMessage" ), //$NON-NLS-1$
								MessageDialog.QUESTION,
								new String[]{
										Messages.getString( "DataColumnBindingDialog.NewBindingDialogButtonYes" ), Messages.getString( "DataColumnBindingDialog.NewBindingDialogButtonNo" ), Messages.getString( "DataColumnBindingDialog.NewBindingDialogButtonCancel" ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								},
								0 );
						int dialogClick = dialog.open( );
						if ( dialogClick == 0 )
						{
							InputDialog inputDialog = new InputDialog( UIUtil.getDefaultShell( ),
									Messages.getString( "DataColumnBindingDialog.NewBindingDialogInputNewNameTitle" ), //$NON-NLS-1$
									Messages.getString( "DataColumnBindingDialog.NewBindingDialogInputNewNameMessage" ), //$NON-NLS-1$
									"", //$NON-NLS-1$
									new IInputValidator( ) {

										public String isValid( String newText )
										{

											for ( Iterator iterator = DEUtil.getBindingHolder( bindingObject )
													.getColumnBindings( )
													.iterator( ); iterator.hasNext( ); )
											{
												ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next( );
												if ( computedColumn.getName( )
														.equals( newText ) )
												{
													return Messages.getFormattedString( "BindingDialogHelper.error.nameduplicate", //$NON-NLS-1$
															new Object[]{
																newText
															} );
												}
											}
											return null;
										}
									} );
							if ( inputDialog.open( ) == Window.OK )
							{
								bindingColumn = dialogHelper.newBinding( DEUtil.getBindingHolder( bindingObject ),
										inputDialog.getValue( ) );
								super.okPressed( );
								return;
							}
							else
							{
								return;
							}
						}
						else if ( dialogClick == 2 )
						{
							return;
						}
					}
					if ( !dialogHelper.canProcessWithWarning( ) )
						return;
					bindingColumn = dialogHelper.editBinding( bindingColumn );
				}
			}
			else
			{
				if ( !dialogHelper.canProcessWithWarning( ) )
					return;
				if ( bindSelf )
					bindingColumn = dialogHelper.newBinding( bindingObject,
							null );
				else
					bindingColumn = dialogHelper.newBinding( DEUtil.getBindingHolder( bindingObject ),
							null );
			}
			super.okPressed( );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private boolean isBindingMultipleReferenced( )
	{
		// get referenced bindings
		List bindings = new ArrayList( );
		ReportItemHandle holder = DEUtil.getBindingHolder( bindingObject );
		for ( Iterator iterator = holder.getColumnBindings( ).iterator( ); iterator.hasNext( ); )
		{
			ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next( );
			bindings.add( computedColumn );
		}
		Set referencedBindings = DataUtil.getReferencedBindings( bindingColumn,
				bindings );
		referencedBindings.add( bindingColumn );
		return isBindingUsed( holder, referencedBindings, bindingObject );
	}

	private boolean isBindingUsed( Object obj, Set bindings,
			ReportItemHandle excepted )
	{
		if ( obj instanceof DataItemHandle && !obj.equals( excepted ) )
		{
			for ( Iterator iterator = bindings.iterator( ); iterator.hasNext( ); )
			{
				ComputedColumnHandle binding = (ComputedColumnHandle) iterator.next( );
				if ( binding.getName( )
						.equals( ( (DataItemHandle) obj ).getResultSetColumn( ) ) )
					return true;
			}
		}

		Object[] children = ProviderFactory.createProvider( obj )
				.getChildren( obj );
		for ( int i = 0; i < children.length; i++ )
		{
			if ( children[i] instanceof ReportItemHandle
					&& ( ( (ReportItemHandle) children[i] ).getDataSet( ) != null || ( (ReportItemHandle) children[i] ).getDataBindingReference( ) != null ) )
				continue;
			if ( isBindingUsed( children[i], bindings, excepted ) )
				return true;
		}
		return false;
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
