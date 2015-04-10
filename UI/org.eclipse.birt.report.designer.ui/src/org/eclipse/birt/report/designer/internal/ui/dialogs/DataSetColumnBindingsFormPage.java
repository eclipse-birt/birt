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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Data set binding page.
 */

public class DataSetColumnBindingsFormPage extends FormPage
{

	private Button btnAddAggr;
	private Button btnRefresh;

	// private Button generateAllBindingsButton;
	// Comments this button because of bug 143398.
	// private Button removeUnusedColumnButton;

	public DataSetColumnBindingsFormPage( Composite parent,
			DataSetColumnBindingsFormHandleProvider provider )
	{
		super( parent, FormPage.FULL_FUNCTION, provider, true );
		provider.setTableViewer( this.getTableViewer( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#createControl()
	 */
	protected void createControl( )
	{
		// createGenerateAllBindingsButton( );
		// Comments this calling because of bug 143398.
		// createRemoveUnusedColumnButton( );
		super.createControl( );

		if ( ( (DataSetColumnBindingsFormHandleProvider) provider ).canAggregation( ) )
		{
			btnAddAggr = new Button( this, SWT.PUSH );
			btnAddAggr.setText( Messages.getString( "FormPage.Button.Add.AggregateOn" ) ); //$NON-NLS-1$
			btnAddAggr.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					handleAddAggregateOnSelectEvent( );
				}
			} );
		}
		btnRefresh = new Button( this, SWT.PUSH );
		btnRefresh.setText( Messages.getString( "FormPage.Button.Binding.Refresh" ) ); //$NON-NLS-1$
		btnRefresh.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleRefreshSelectEvent( );
			}
		} );

		fullLayout( );
	}

	protected void handleAddAggregateOnSelectEvent( )
	{
		int pos = table.getSelectionIndex( );
		try
		{
			( (DataSetColumnBindingsFormHandleProvider) provider ).addAggregateOn( pos );
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( btnAddAggr.getShell( ), e );
			return;
		}

		refresh( );
		table.setSelection( table.getItemCount( ) - 1 );
	}

	protected void handleRefreshSelectEvent( )
	{
		( (DataSetColumnBindingsFormHandleProvider) provider ).generateAllBindingColumns( );
		refresh( );
	}

	// Comments this method because of bug 143398.
	// private void createRemoveUnusedColumnButton( )
	// {
	// removeUnusedColumnButton = new Button( this, SWT.BORDER );
	// removeUnusedColumnButton.setText( Messages.getString(
	// "DataSetColumnBindingsFormPage.Button.RemoveUnused" ) );
	// removeUnusedColumnButton.addSelectionListener( new SelectionAdapter( ) {
	//
	// public void widgetSelected( SelectionEvent e )
	// {
	// provider.removedUnusedColumnBindings( input );
	// }
	//
	// } );
	// }

	/*
	 * private void createGenerateAllBindingsButton( ) {
	 * generateAllBindingsButton = new Button( this, SWT.BORDER );
	 * generateAllBindingsButton.setText( Messages.getString(
	 * "DataSetColumnBindingsFormPage.Button.Generate" ) ); //$NON-NLS-1$
	 * generateAllBindingsButton.addSelectionListener( new SelectionAdapter( ) {
	 * 
	 * public void widgetSelected( SelectionEvent e ) {
	 * provider.generateAllBindingColumns( ); } } ); }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#fullLayout()
	 */
	protected void fullLayout( )
	{
		super.fullLayout( );
		// put btnDel flow over btnEdit

		// btnEdit.setVisible( false );

		if ( btnAddAggr != null )
		{
			FormData data = new FormData( );
			data.top = new FormAttachment( btnAdd, 0, SWT.BOTTOM );
			data.left = new FormAttachment( btnAdd, 0, SWT.LEFT );
			data.width = Math.max( 60, btnAddAggr.computeSize( SWT.DEFAULT,
					SWT.DEFAULT,
					true ).x );
			btnAddAggr.setLayoutData( data );

			data = new FormData( );
			data.top = new FormAttachment( btnAddAggr, 0, SWT.BOTTOM );
			data.left = new FormAttachment( btnAddAggr, 0, SWT.LEFT );
			data.width = Math.max( 60, btnEdit.computeSize( SWT.DEFAULT,
					SWT.DEFAULT,
					true ).x );
			btnEdit.setLayoutData( data );
		}
		FormData data = new FormData( );
		data.top = new FormAttachment( btnEdit, 0, SWT.BOTTOM );
		data.left = new FormAttachment( btnEdit, 0, SWT.LEFT );
		data.width = Math.max( 60, btnDel.computeSize( SWT.DEFAULT,
				SWT.DEFAULT,
				true ).x );
		btnDel.setLayoutData( data );

		if ( btnRefresh != null )
		{
			data = new FormData( );
			data.top = new FormAttachment( btnDel, 0, SWT.BOTTOM );
			data.left = new FormAttachment( btnDel, 0, SWT.LEFT );
			data.width = Math.max( 60, btnRefresh.computeSize( SWT.DEFAULT,
					SWT.DEFAULT,
					true ).x );
			btnRefresh.setLayoutData( data );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#setInput(java.util.List)
	 */
	public void setInput( List elements )
	{
		super.setInput( elements );

		if ( elements.size( ) > 0 )
		{
			Object element = elements.get( 0 );
			setBindingObject( (ReportElementHandle) element );
			checkButtonsEnabled( );
		}

	}

	private void checkButtonsEnabled( )
	{
		if ( ( (DataSetColumnBindingsFormHandleProvider) provider ).canAggregation( ) )
		{
			if ( !btnAddAggr.isDisposed( ) )
				btnAddAggr.setEnabled( provider.isEditable( ) );
		}
		if ( !btnRefresh.isDisposed( ) )
			btnRefresh.setEnabled( provider.isEditable( ) );
	}

	private void setBindingObject( ReportElementHandle bindingObject )
	{
		( (DataSetColumnBindingsFormHandleProvider) provider ).setBindingObject( bindingObject );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage#elementChanged(org.eclipse.birt.report.model.api.DesignElementHandle,
	 *      org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle elementHandle,
			NotificationEvent event )
	{
		checkButtonsEnabled( );
	}

	public void generateAllBindingColumns( )
	{
		( (DataSetColumnBindingsFormHandleProvider) provider ).generateAllBindingColumns( );
	}

	public void generateBindingColumns( Object[] columns )
	{
		( (DataSetColumnBindingsFormHandleProvider) provider ).generateBindingColumns( columns );
	}
}
