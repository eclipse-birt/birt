/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.views.attributes.page;

import java.util.List;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartBindingGroupDescriptorProvider;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartShareBindingsFormHandlerProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AggregateOnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.BindingGroupSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SortingFormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.AggregateOnBindingsFormDescriptor;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public class ChartBindingPage extends BindingPage
{

	protected void applyCustomSections( )
	{
		ChartBindingGroupDescriptorProvider bindingProvider = new ChartBindingGroupDescriptorProvider( );
		bindingProvider.setRefrenceSection( ( (BindingGroupSection) getSection( PageSectionId.BINDING_GROUP ) ) );
		( (BindingGroupSection) getSection( PageSectionId.BINDING_GROUP ) ).setProvider( bindingProvider );
		AggregateOnBindingsFormHandleProvider dataSetFormProvider = createDataSetFormProvider();
		( (SortingFormSection) getSection( PageSectionId.BINDING_DATASET_FORM ) ).setCustomForm( new AggregateOnBindingsFormDescriptor( true ) );
		( (SortingFormSection) getSection( PageSectionId.BINDING_DATASET_FORM ) ).setProvider( dataSetFormProvider );
		if ( ( (BindingGroupSection) getSection( PageSectionId.BINDING_GROUP ) ).getProvider( ) != null )
		{
			IDescriptorProvider dataSetProvider = ( (BindingGroupSection) getSection( PageSectionId.BINDING_GROUP ) ).getProvider( );
			if ( dataSetProvider instanceof BindingGroupDescriptorProvider )
				( (BindingGroupDescriptorProvider) dataSetProvider ).setDependedProvider( dataSetFormProvider );
		}
	}

	/**
	 * Create different dataset provider for common and sharing case.
	 * 
	 * @return
	 * @since 2.3
	 */
	protected AggregateOnBindingsFormHandleProvider createDataSetFormProvider()
	{
		if ( input == null ) 
		{
			return  new AggregateOnBindingsFormHandleProvider( );
		}
		
		final ReportItemHandle rih;
		if ( input instanceof List ) {
			rih = (ExtendedItemHandle) ( (List) input ).get( 0 );
		} else {
			rih = (ExtendedItemHandle) input;
		}
		
		if ( ChartReportItemUtil.isChildOfMultiViewsHandle( rih ) )
		{
			return new ChartShareBindingsFormHandlerProvider( );
		}
		
		return new AggregateOnBindingsFormHandleProvider( ) {

			@Override
			public boolean isEditable( )
			{
				return !ChartReportItemUtil.isInheritGroup( rih );
			}

		};
	}
}
