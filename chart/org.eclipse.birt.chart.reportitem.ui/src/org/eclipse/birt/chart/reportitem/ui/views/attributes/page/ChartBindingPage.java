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

import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartBindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AggregateOnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.BindingGroupSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.AggregateOnBindingsFormDescriptor;

public class ChartBindingPage extends BindingPage
{

	protected void applyCustomSections( )
	{
		ChartBindingGroupDescriptorProvider bindingProvider = new ChartBindingGroupDescriptorProvider( );
		bindingProvider.setRefrenceSection( ( (BindingGroupSection) getSection( PageSectionId.BINDING_GROUP ) ) );
		( (BindingGroupSection) getSection( PageSectionId.BINDING_GROUP ) ).setProvider( bindingProvider );
		AggregateOnBindingsFormHandleProvider dataSetFormProvider = new AggregateOnBindingsFormHandleProvider( false );
		( (FormSection) getSection( PageSectionId.BINDING_DATASET_FORM ) ).setCustomForm( new AggregateOnBindingsFormDescriptor( true ) );
		( (FormSection) getSection( PageSectionId.BINDING_DATASET_FORM ) ).setProvider( dataSetFormProvider );
		if ( ( (BindingGroupSection) getSection( PageSectionId.BINDING_GROUP ) ).getProvider( ) != null )
		{
			IDescriptorProvider dataSetProvider = ( (BindingGroupSection) getSection( PageSectionId.BINDING_GROUP ) ).getProvider( );
			if ( dataSetProvider instanceof BindingGroupDescriptorProvider )
				( (BindingGroupDescriptorProvider) dataSetProvider ).setDependedProvider( dataSetFormProvider );
		}
	}
}
