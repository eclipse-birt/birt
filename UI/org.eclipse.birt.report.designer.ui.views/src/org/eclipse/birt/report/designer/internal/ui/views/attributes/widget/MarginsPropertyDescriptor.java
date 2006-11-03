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

import java.math.BigDecimal;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MarginsPropertyDescriptorProvider;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * SpinnerPropertyDescriptor manages Spinner choice control.
 */
public class MarginsPropertyDescriptor extends PropertyDescriptor
{

	protected Spinner spinner;

	protected CCombo combo;

	protected Composite container;

	/**
	 * @param propertyProcessor
	 *            The property handle
	 */

	public MarginsPropertyDescriptor( boolean formStyle )
	{
		setFormStyle( formStyle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PropertyDescriptor#resetUIData()
	 */
	public void load( )
	{
		String value = provider.load( ).toString( );

		boolean stateFlag = ( ( value == null ) == spinner.getEnabled( ) );
		if ( stateFlag )
		{
			spinner.setEnabled( value != null );
			combo.setEnabled( value != null );
		}
		if ( value == null )
			return;
		String spinnerValue = provider.getMeasureValue( );
		BigDecimal bigValue = new BigDecimal( spinnerValue );

		bigValue = bigValue.movePointRight( spinner.getDigits( ) );
		spinner.setSelection( bigValue.intValue( ) );

		if ( combo.getItems( ) == null || combo.getItemCount( ) == 0 )
			combo.setItems( provider.getUnits( ) );
		String comboValue = provider.getDefaultUnit( );
		if ( provider.getUnitDisplayName( comboValue ) == null )
		{
			combo.deselectAll( );
			return;
		}
		if ( !provider.getUnitDisplayName( comboValue ).equals( combo.getText( ) ) )
		{
			combo.deselectAll( );
			combo.setText( provider.getUnitDisplayName( comboValue ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PropertyDescriptor#getControl()
	 */
	public Control getControl( )
	{
		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl( Composite parent )
	{
		container = new Composite( parent, SWT.NONE );
		FormLayout layout = new FormLayout( );
		if ( isFormStyle( ) )
		{
			layout.marginHeight = 1;
			layout.marginWidth = 1;
			layout.spacing = 3;
		}else{
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.spacing = 0;
		}
		container.setLayout( layout );

		Label label = FormWidgetFactory.getInstance( ).createLabel( container,
				isFormStyle( ) );
		label.setText( provider.getDisplayName( ) );

		if(isFormStyle( ))spinner = FormWidgetFactory.getInstance( ).createSpinner( container );
		else spinner = new Spinner(container,SWT.BORDER);
		spinner.setDigits( 2 );
		spinner.setMaximum( 10000 );
		spinner.setMinimum( -10000 );
		spinner.setIncrement( 25 );
		spinner.setSelection( 0 );
		spinner.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				handleSelectedEvent( );
			}

			public void widgetSelected( SelectionEvent e )
			{
				handleSelectedEvent( );
			}
		} );

		if ( !isFormStyle( ) )
			combo = new CCombo( container, SWT.BORDER | SWT.READ_ONLY );
		else
			combo = FormWidgetFactory.getInstance( ).createCCombo( container,
					true );
		combo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleSelectedEvent( );
			}
		} );
		FormData data = new FormData( );
		data.top = new FormAttachment( spinner, 0, SWT.BOTTOM );
		data.left = new FormAttachment( spinner, 0, SWT.LEFT );
		data.right = new FormAttachment( spinner, 0, SWT.RIGHT );
		combo.setLayoutData( data );

		data = new FormData( );
		data.top = new FormAttachment( label, 0, SWT.BOTTOM );
		data.left = new FormAttachment( label, 0, SWT.LEFT );
		data.right = new FormAttachment( 100, -layout.spacing );
		spinner.setLayoutData( data );
		return container;
	}

	protected void handleSelectedEvent( )
	{
		BigDecimal bigValue = new BigDecimal( spinner.getSelection( ) );

		bigValue = bigValue.movePointLeft( spinner.getDigits( ) );

		String value = bigValue.toString( );

		if ( provider.getUnit( combo.getText( ) ) != null )
			value += provider.getUnit( combo.getText( ) );
		try
		{
			save( value );
		}
		catch ( SemanticException e )
		{
			WidgetUtil.processError( combo.getShell( ), e );
		}
	}

	private MarginsPropertyDescriptorProvider provider;

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( provider instanceof MarginsPropertyDescriptorProvider )
			this.provider = (MarginsPropertyDescriptorProvider) provider;

	}

	public void save( Object obj ) throws SemanticException
	{
		provider.save( obj );
	}

	public void setHidden( boolean isHidden )
	{
		WidgetUtil.setExcludeGridData( container, isHidden );
	}

	public void setVisible( boolean isVisible )
	{
		container.setVisible( isVisible );
	}

	public void setInput( Object input )
	{
		super.setInput( input );
		getDescriptorProvider( ).setInput( input );
	}

}