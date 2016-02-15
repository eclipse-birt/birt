
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.jface.dialogs.Dialog;

public class AggregateOnBindingsFormHandleProvider extends
		DataSetColumnBindingsFormHandleProvider
{

	public AggregateOnBindingsFormHandleProvider( )
	{
		super( );
		setShowAggregation( true );
	}

	public AggregateOnBindingsFormHandleProvider( boolean bShowAggregation )
	{
		super( );
		setShowAggregation( bShowAggregation );
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

	public boolean doAddMeasureOnItem( int pos )
	{
		DataColumnBindingDialog dialog = new DataColumnBindingDialog( true );
		dialog.setAggreate( true );
		dialog.setMeasure( true );
		ComputedColumnHandle bindingColumn = null;
		dialog.setInput( (ReportItemHandle) getBindingObject( ), bindingColumn );
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
		if ( pos == -1 )
			return false;
		ComputedColumnHandle bindingHandle = null;
		pos = getOriginalIndex( pos );
		if ( pos > -1 )
		{
			if ( getBindingObject( ) instanceof ReportItemHandle )
			{
				bindingHandle = (ComputedColumnHandle) ( (ReportItemHandle) getBindingObject( ) ).getColumnBindings( )
						.getAt( pos );
			}
		}
		if ( bindingHandle == null )
			return false;

		boolean isResultSetColumn = false;
		String resultSetName = null;
		if ( getBindingObject( ) instanceof DataItemHandle )
			resultSetName = ( (DataItemHandle) getBindingObject( ) ).getResultSetColumn( );
		if ( resultSetName != null
				&& bindingHandle.getName( ).equals( resultSetName ) )
			isResultSetColumn = true;

		DataColumnBindingDialog dialog = new DataColumnBindingDialog( false );
		dialog.setInput( (ReportItemHandle) getBindingObject( ), bindingHandle );

		if ( dialog.open( ) == Dialog.OK )
		{
			if ( isResultSetColumn )
			{
				try
				{
					( (DataItemHandle) getBindingObject( ) ).setResultSetColumn( bindingHandle.getName( ) );
				}
				catch ( Exception e )
				{
					ExceptionUtil.handle( e );
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

	public void addMeasureOn( int pos ) throws Exception
	{
		boolean sucess = false;
		CommandStack stack = getActionStack( );
		stack.startTrans( Messages.getString( "FormPage.Menu.ModifyProperty" ) ); //$NON-NLS-1$
		try
		{
			sucess = doAddMeasureOnItem( pos );
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
}
