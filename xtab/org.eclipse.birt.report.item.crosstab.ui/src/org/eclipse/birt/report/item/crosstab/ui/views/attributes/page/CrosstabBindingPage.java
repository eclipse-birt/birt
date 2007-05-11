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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.util.SortMap;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DataSetColumnBindingsFormDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabSimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.CrosstabSimpleComboSection;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;


/**
 * 
 */

public class CrosstabBindingPage extends AttributePage
{
	
	private DataSetColumnBindingsFormHandleProvider dataSetFormProvider;

	private FormSection dataSetFormSection;

	protected Composite composite;

	
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

		IDescriptorProvider cubeProvider = new CrosstabSimpleComboPropertyDescriptorProvider( ICrosstabReportItemConstants.CUBE_PROP,/* ICrosstabReportItemConstants.CUBE_PROP */
				ReportDesignConstants.EXTENDED_ITEM );
		CrosstabSimpleComboSection cubeSection = new CrosstabSimpleComboSection( cubeProvider.getDisplayName( ),
				composite,
				true );
		cubeSection.setProvider( cubeProvider );
		cubeSection.setWidth( 280 );
		cubeSection.setGridPlaceholder( 2, true );
		addSection( CrosstabPageSectionId.CUBE, cubeSection );
		
		
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
		
		createSections( );
		layoutSections( );

		( (ScrolledComposite) container ).setContent( composite );
	}
	
	protected void computeSize( )
	{
		Point size = composite.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		( (ScrolledComposite) container ).setMinSize( size.x, size.y + 10 );
		container.layout( );

	}

	public void addElementEvent( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( checkControl( dataSetFormSection ) )
			dataSetFormSection.getFormControl( ).addElementEvent( focus, ev );
	}

	public void clear( )
	{
		if ( checkControl( dataSetFormSection ) )
			dataSetFormSection.getFormControl( ).clear( );
	}

	public void postElementEvent( )
	{

		if ( checkControl( dataSetFormSection ) )
			dataSetFormSection.getFormControl( ).postElementEvent( );

	}

	public void setInput( Object input )
	{
		super.setInput( input );
	}

	private boolean checkControl( FormSection form )
	{
		return form != null
				&& form.getFormControl( ) != null
				&& !form.getFormControl( ).getControl( ).isDisposed( );
	}

}
