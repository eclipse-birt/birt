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

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider.BindingInfo;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BindingGroupDescriptor extends PropertyDescriptor
{

	protected Composite container;
	private Button datasetRadio;
	private Button reportItemRadio;
	private CCombo datasetCombo;
	private CCombo reportItemCombo;
	private Button bindingButton;

	public BindingGroupDescriptor( boolean formStyle )
	{
		setFormStyle( formStyle );
	}

	public Control createControl( Composite parent )
	{
		container = new Composite( parent, SWT.NONE );

		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		container.setLayout( layout );
		container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		datasetRadio = FormWidgetFactory.getInstance( )
				.createButton( container, SWT.RADIO, isFormStyle( ) );
		datasetRadio.setText( getProvider( ).getText( 0 ) );
		datasetRadio.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				refreshBinding( );
				if ( datasetRadio.getSelection( )
						&& getProvider( ).isBindingReference( ) )
					saveBinding( );
			}

		} );
		if ( isFormStyle( ) )
			datasetCombo = FormWidgetFactory.getInstance( )
					.createCCombo( container, true );
		else
			datasetCombo = new CCombo( container, SWT.READ_ONLY );
		datasetCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				saveBinding( );
			}

		} );
		GridData gd = new GridData( );
		gd.widthHint = 300;
		datasetCombo.setLayoutData( gd );
		bindingButton = FormWidgetFactory.getInstance( )
				.createButton( container, SWT.PUSH, isFormStyle( ) );
		bindingButton.setText( getProvider( ).getText( 1 ) );
		bindingButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				getProvider( ).bindingDialog( );
			}
		} );
		reportItemRadio = FormWidgetFactory.getInstance( )
				.createButton( container, SWT.RADIO, isFormStyle( ) );
		reportItemRadio.setText( getProvider( ).getText( 2 ) );
		reportItemRadio.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				refreshBinding( );
			}

		} );
		if ( isFormStyle( ) )
			reportItemCombo = FormWidgetFactory.getInstance( )
					.createCCombo( container, true );
		else
			reportItemCombo = new CCombo( container, SWT.READ_ONLY );
		reportItemCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				saveBinding( );
			}
		} );
		gd = new GridData( );
		gd.widthHint = 300;
		reportItemCombo.setLayoutData( gd );
		return container;
	}

	public Control getControl( )
	{
		return container;
	}

	private void saveBinding( )
	{
		BindingInfo info = new BindingInfo( );
		if ( datasetRadio.getSelection( ) )
		{
			info.setBindingType( ReportItemHandle.DATABINDING_TYPE_DATA );
			info.setBindingValue( datasetCombo.getText( ) );
		}
		else
		{
			info.setBindingType( ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF );
			info.setBindingValue( reportItemCombo.getText( ) );
		}
		try
		{
			save( info );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	public void load( )
	{
		if ( !provider.isEnable( ) )
		{
			datasetRadio.setEnabled( false );
			datasetRadio.setSelection( false );
			datasetCombo.setEnabled( false );
			bindingButton.setEnabled( false );
			reportItemRadio.setSelection( false );
			reportItemRadio.setEnabled( false );
			reportItemCombo.setEnabled( false );
			datasetCombo.deselectAll( );
			reportItemCombo.deselectAll( );
			return;
		}
		datasetRadio.setEnabled( true );
		reportItemRadio.setEnabled( true );
		BindingInfo info = (BindingInfo) getDescriptorProvider( ).load( );
		if ( info != null )
		{
			refreshBindingInfo( info );
		}
	}

	private void refreshBinding( )
	{
		if ( datasetRadio.getSelection( ) )
		{
			datasetRadio.setSelection( true );
			datasetCombo.setEnabled( true );
			bindingButton.setEnabled( getProvider( ).enableBindingButton( ) );
			reportItemRadio.setSelection( false );
			reportItemCombo.setEnabled( false );
			if ( datasetCombo.getSelectionIndex( ) == -1 )
			{
				datasetCombo.setItems( getProvider( ).getAvailableDatasetItems( ) );
				datasetCombo.select( 0 );
			}
		}
		else
		{
			datasetRadio.setSelection( false );
			datasetCombo.setEnabled( false );
			bindingButton.setEnabled( false );
			reportItemRadio.setSelection( true );
			reportItemCombo.setEnabled( true );
			if ( reportItemCombo.getSelectionIndex( ) == -1 )
			{
				reportItemCombo.setItems( getProvider( ).getReferences( ) );
				reportItemCombo.select( 0 );
			}
		}
	}

	private void refreshBindingInfo( BindingInfo info )
	{
		int type = info.getBindingType( );
		Object value = info.getBindingValue( );
		datasetCombo.setItems( getProvider( ).getAvailableDatasetItems( ) );
		reportItemCombo.setItems( getProvider( ).getReferences( ) );
		if ( type == ReportItemHandle.DATABINDING_TYPE_NONE )
		{
			if ( DEUtil.getBindingHolder( getProvider( ).getReportItemHandle( ),
					true ) != null
					&& DEUtil.getBindingHolder( getProvider( ).getReportItemHandle( ),
							true )
							.getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
				type = ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF;
		}
		switch ( type )
		{
			case ReportItemHandle.DATABINDING_TYPE_NONE :
			case ReportItemHandle.DATABINDING_TYPE_DATA :
				datasetRadio.setSelection( true );
				datasetCombo.setEnabled( true );
				datasetCombo.setText( value.toString( ) );
				bindingButton.setEnabled( getProvider( ).enableBindingButton( ) );
				reportItemRadio.setSelection( false );
				reportItemCombo.setEnabled( false );
				break;
			case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
				datasetRadio.setSelection( false );
				datasetCombo.setEnabled( false );
				bindingButton.setEnabled( false );
				reportItemRadio.setSelection( true );
				reportItemCombo.setEnabled( true );
				reportItemCombo.setText( value.toString( ) );
		}
	}

	public void save( Object obj ) throws SemanticException
	{
		getProvider( ).save( obj );
	}

	private BindingGroupDescriptorProvider provider;

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		this.descriptorProvider = (BindingGroupDescriptorProvider)provider;
		if ( provider instanceof BindingGroupDescriptorProvider )
			this.provider = (BindingGroupDescriptorProvider) provider;
	}

	public BindingGroupDescriptorProvider getProvider( )
	{
		return provider;
	}
}
