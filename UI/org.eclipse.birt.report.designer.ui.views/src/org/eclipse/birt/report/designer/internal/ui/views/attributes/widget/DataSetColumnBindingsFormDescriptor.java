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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Data set binding page.
 */

public class DataSetColumnBindingsFormDescriptor extends
		SortingFormPropertyDescriptor
{

	private DataSetColumnBindingsFormHandleProvider provider;

	public DataSetColumnBindingsFormDescriptor( boolean formStyle )
	{
		super( formStyle );
		super.setStyle( FormPropertyDescriptor.FULL_FUNCTION );
		super.setButtonWithDialog( false );
	}

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( provider instanceof DataSetColumnBindingsFormHandleProvider )
			this.provider = (DataSetColumnBindingsFormHandleProvider) provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage
	 * #createControl()
	 */

	protected Button btnRefresh;
	protected Button btnClear;

	public Control createControl( Composite parent )
	{
		Control control = super.createControl( parent );
		provider.setTableViewer( getTableViewer( ) );

		if ( isFormStyle( ) )
			btnClear = FormWidgetFactory.getInstance( )
					.createButton( (Composite) control, "", SWT.PUSH ); //$NON-NLS-1$
		else
			btnClear = new Button( (Composite) control, SWT.BORDER );

		btnClear.setText( Messages.getString( "FormPage.Button.Binding.Clear" ) ); //$NON-NLS-1$
		btnClear.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleClearSelectEvent( );
			}
		} );
		btnClear.setEnabled( true );

		if ( isFormStyle( ) )
			btnRefresh = FormWidgetFactory.getInstance( )
					.createButton( (Composite) control, "", SWT.PUSH ); //$NON-NLS-1$
		else
			btnRefresh = new Button( (Composite) control, SWT.BORDER );

		btnRefresh.setText( Messages.getString( "FormPage.Button.Binding.Refresh" ) ); //$NON-NLS-1$
		btnRefresh.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleRefreshSelectEvent( );
			}
		} );
		btnRefresh.setEnabled( true );

		fullLayout( );

		return control;
	}

	protected void handleRefreshSelectEvent( )
	{
		provider.generateAllBindingColumns( );
	}

	protected void handleClearSelectEvent( )
	{
		provider.clearAllBindingColumns( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage
	 * #fullLayout()
	 */
	protected void fullLayout( )
	{
		super.fullLayout( );
		FormData data = new FormData( );
		data.top = new FormAttachment( btnEdit, 0, SWT.BOTTOM );
		data.left = new FormAttachment( btnEdit, 0, SWT.LEFT );
		data.width = Math.max( 60,
				btnDel.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
		btnDel.setLayoutData( data );

		if ( btnClear != null )
		{
			data = new FormData( );
			data.top = new FormAttachment( btnDel, 0, SWT.BOTTOM );
			data.left = new FormAttachment( btnDel, 0, SWT.LEFT );
			data.width = Math.max( 60,
					btnClear.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
			btnClear.setLayoutData( data );
		}

		if ( btnRefresh != null )
		{
			data = new FormData( );
			data.top = new FormAttachment( btnClear, 0, SWT.BOTTOM );
			data.left = new FormAttachment( btnClear, 0, SWT.LEFT );
			data.width = Math.max( 60,
					btnRefresh.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
			btnRefresh.setLayoutData( data );
		}

	}

	protected void updateArraw( )
	{
		super.updateArraw( );
		if ( provider.isEnable( ) && provider.isEditable( ) )
		{
			if ( table.getItemCount( ) > 0 )
				btnClear.setEnabled( provider.isClearEnable( ) );
			else
				btnClear.setEnabled( false );
		}
		else
		{
			btnClear.setEnabled( false );
		}
	}

	public void setInput( Object object )
	{
		super.setInput( object );
		if ( DEUtil.getInputSize( object ) > 0 )
		{
			Object element = DEUtil.getInputFirstElement( object );
			setBindingObject( (ReportElementHandle) element );
		}
		if ( provider.isEnable( ) && provider.isEditable( ) )
			btnRefresh.setEnabled( true );
		else
			btnRefresh.setEnabled( false );

	}

	private void setBindingObject( ReportElementHandle bindingObject )
	{
		provider.setBindingObject( bindingObject );
	}
}