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

import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

/**
 * Data set binding page.
 */

public class DataSetColumnBindingsFormPage extends FormPage
{

	// private Button generateAllBindingsButton;
	// Comments this button because of bug 143398.
	// private Button removeUnusedColumnButton;

	private DataSetColumnBindingsFormHandleProvider provider;

	public DataSetColumnBindingsFormPage( Composite parent,
			DataSetColumnBindingsFormHandleProvider provider )
	{
		super( parent, FormPage.FULL_FUNCTION, provider, true );
		this.provider = provider;
		this.provider.setTableViewer( this.getTableViewer( ) );
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
		btnUp.setVisible( false );
		btnDown.setVisible( false );
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

		FormData data = new FormData( );
		data.top = new FormAttachment( btnEdit, 0, SWT.BOTTOM );
		data.left = new FormAttachment( btnEdit, 0, SWT.LEFT );
		data.width = Math.max( 60, btnDel.computeSize( SWT.DEFAULT,
				SWT.DEFAULT,
				true ).x );
		btnDel.setLayoutData( data );

		data = new FormData( );
		data.top = new FormAttachment( btnDel, 0, SWT.BOTTOM );
		data.left = new FormAttachment( btnDel, 0, SWT.LEFT );
		// data.width = 135;
		// generateAllBindingsButton.setLayoutData( data );
		// Comments this button because of bug 143398.
		// removeUnusedColumnButton.setLayoutData( data );
		// btnEdit.dispose( );
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

			// Comments this button because of bug 143398.
			// if ( element instanceof ReportElementHandle )
			// {
			setBindingObject( (ReportElementHandle) element );
			/*
			 * if ( element instanceof GroupHandle ) { DesignElementHandle
			 * parentHandle = ( (GroupHandle) element ).getContainer( ); if (
			 * parentHandle instanceof ReportItemHandle ) {
			 * generateAllBindingsButton.setEnabled( ( (ReportItemHandle)
			 * parentHandle ).getDataSet( ) != null ); } } else if ( element
			 * instanceof ReportItemHandle ) {
			 * generateAllBindingsButton.setEnabled( ( (ReportItemHandle)
			 * element ).getDataSet( ) != null ); }
			 */
			// Comments this button because of bug 143398.
			// if ( element instanceof GroupHandle )
			// {
			// DesignElementHandle parentHandle = ( (GroupHandle) element )
			// .getContainer( );
			// if ( parentHandle instanceof ReportItemHandle )
			// {
			// removeUnusedColumnButton
			// .setEnabled( ( (ReportItemHandle) parentHandle )
			// .getDataSet( ) != null );
			// }
			// }
			// else if ( element instanceof ReportItemHandle )
			// {
			// removeUnusedColumnButton
			// .setEnabled( ( (ReportItemHandle) element )
			// .getDataSet( ) != null );
			// }
			/*
			CellEditor[] cellEditor = provider.getEditors( getTableViewer( ).getTable( ) );
			BindingExpressionProvider provider = new BindingExpressionProvider( (ReportElementHandle) element );
			ComputedColumnExpressionFilter filter = new ComputedColumnExpressionFilter( getTableViewer( ) );
			provider.addFilter( filter );
			( (ExpressionDialogCellEditor) cellEditor[2] ).setExpressionProvider( provider );
			DataSetColumnBindingsFormPage.this.provider.setExpressionProvider( provider );
			*/
			// Comments this button because of bug 143398.
			// }
			// else
			// {
			// removeUnusedColumnButton.setEnabled( false );
			// }
		}

	}

	private void setBindingObject( ReportElementHandle bindingObject )
	{
		provider.setBindingObject( bindingObject );
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
		super.elementChanged( elementHandle, event );
		// Comments this button because of bug 143398.
		// if ( elementHandle instanceof ReportItemHandle )
		// {
		// if ( event.getEventType( ) == NotificationEvent.PROPERTY_EVENT )
		// {
		// PropertyEvent ev = (PropertyEvent) event;
		// String propertyName = ev.getPropertyName( );
		// /*
		// * if ( ReportItemHandle.DATA_SET_PROP.equals( propertyName ) ) {
		// * generateAllBindingsButton.setEnabled( ( (ReportItemHandle)
		// * elementHandle ).getDataSet( ) != null ); }
		// */
		//
		// if ( ReportItemHandle.DATA_SET_PROP.equals( propertyName ) )
		// {
		// removeUnusedColumnButton
		// .setEnabled( ( (ReportItemHandle) elementHandle )
		// .getDataSet( ) != null );
		// }
		//
		// }
		// }
	}

	public void generateAllBindingColumns( )
	{
		provider.generateAllBindingColumns( );
	}
}
