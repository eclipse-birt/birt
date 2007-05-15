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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AggregateOnBindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AggregateOnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.AggregateOnBindingsFormDescriptor;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabSimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.CrosstabSimpleComboSection;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * 
 */

public class CrosstabBindingPage extends AggregateOnBindingPage
{

	protected void applyCustomSections( )
	{
		IDescriptorProvider cubeProvider = new CrosstabSimpleComboPropertyDescriptorProvider( ICrosstabReportItemConstants.CUBE_PROP,/* ICrosstabReportItemConstants.CUBE_PROP */
				ReportDesignConstants.EXTENDED_ITEM );
		CrosstabSimpleComboSection cubeSection = new CrosstabSimpleComboSection( cubeProvider.getDisplayName( ),
				getSectionContainer( ),
				true );
		cubeSection.setProvider( cubeProvider );
		cubeSection.setWidth( 280 );
		cubeSection.setGridPlaceholder( 2, true );
		addSection( PageSectionId.BINDING_DATASET, cubeSection );

		AggregateOnBindingsFormHandleProvider dataSetFormProvider = new AggregateOnBindingsFormHandleProvider( );
		( (FormSection) getSection( PageSectionId.BINDING_DATASET_FORM ) ).setCustomForm( new AggregateOnBindingsFormDescriptor( true ) );
		( (FormSection) getSection( PageSectionId.BINDING_DATASET_FORM ) ).setProvider( dataSetFormProvider );
	}

}
