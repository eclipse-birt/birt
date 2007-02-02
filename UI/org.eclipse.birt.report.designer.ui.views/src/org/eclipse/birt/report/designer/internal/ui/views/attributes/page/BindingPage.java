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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.util.SortMap;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.DataSetDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboAndButtonSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DataSetColumnBindingsFormDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * The Binding attribute page of DE element. Note: Binding Not support
 * multi-selection.
 */
public class BindingPage extends AttributePage
{

	private static final String BUTTON_BINDING = Messages.getString( "parameterBinding.title" ); //$NON-NLS-1$

	private ComboAndButtonSection dataSetSection;

	private DataSetColumnBindingsFormHandleProvider dataSetFormProvider;

	private DataSetDescriptorProvider dataSetProvider;

	private FormSection dataSetFormSection;

	private Composite composite;

	public void buildUI( Composite parent )
	{
		container = new ScrolledComposite( parent, SWT.H_SCROLL | SWT.V_SCROLL );
		container.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		( (ScrolledComposite) container ).setExpandHorizontal( true );
		( (ScrolledComposite) container ).setExpandVertical( true );
		container.addControlListener( new ControlAdapter( ) {

			public void controlResized( ControlEvent e )
			{
				computeSize( );
			}
		} );

		container.addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				deRegisterEventManager( );
			}
		} );

		composite = new Composite( container, SWT.NONE );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		if ( sections == null )
			sections = new SortMap( );

		composite.setLayout( WidgetUtil.createGridLayout( 6 ) );

		dataSetProvider = new DataSetDescriptorProvider( );
		dataSetSection = new ComboAndButtonSection( dataSetProvider.getDisplayName( ),
				composite,
				true );
		dataSetSection.setProvider( dataSetProvider );
		dataSetSection.addButtonSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				dataSetProvider.bindingDialog( );
			}
		} );
		dataSetSection.setWidth( 300 );
		dataSetSection.setButtonText( BUTTON_BINDING );
		dataSetSection.setGridPlaceholder( 2, true );
		dataSetProvider.setComboAndButtonSection( dataSetSection );
		addSection( PageSectionId.BINDING_DATASET, dataSetSection );

		dataSetFormProvider = new DataSetColumnBindingsFormHandleProvider( );
		dataSetFormSection = new FormSection( dataSetFormProvider.getDisplayName( ),
				composite,
				true );
		dataSetFormSection.setCustomForm( new DataSetColumnBindingsFormDescriptor( ) );
		dataSetFormSection.setProvider( dataSetFormProvider );
		dataSetFormSection.showDisplayLabel( true );
		dataSetFormSection.setButtonWithDialog( true );
		dataSetFormSection.setStyle( FormPropertyDescriptor.FULL_FUNCTION );
		dataSetFormSection.setFillForm( true );
		dataSetFormSection.setGridPlaceholder( 1, true );
		addSection( PageSectionId.BINDING_DATASET_FORM, dataSetFormSection );

		dataSetProvider.setDependedProvider( dataSetFormProvider );

		createSections( );
		layoutSections( );

		( (ScrolledComposite) container ).setContent( composite );
	}

	private void computeSize( )
	{
		Point size = composite.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		( (ScrolledComposite) container ).setMinSize( size.x, size.y + 10 );
		container.layout( );

	}


	public void addElementEvent( DesignElementHandle focus, NotificationEvent ev )
	{
		dataSetFormSection.getFormControl( ).addElementEvent( focus, ev );
	}

	public void clear( )
	{
		dataSetFormSection.getFormControl( ).clear( );
	}

	public void postElementEvent( )
	{

		dataSetFormSection.getFormControl( ).postElementEvent( );

	}

	public void setInput( Object input )
	{
		super.setInput( input );
	}

}