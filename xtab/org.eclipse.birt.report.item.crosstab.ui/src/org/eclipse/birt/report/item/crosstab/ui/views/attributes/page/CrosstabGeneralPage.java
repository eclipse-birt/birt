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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.GeneralPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.CheckSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.HideMeasureHeaderProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.MeasureComboPropertyDescriptorProvider;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

public class CrosstabGeneralPage extends GeneralPage
{

	IDescriptorProvider grandTotalColumnProvider, grandTotalRowProvider;
	IDescriptorProvider layoutMeasuresProvider;

	protected void buildContent( )
	{

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ReportItemHandle.NAME_PROP,
				ReportDesignConstants.EXTENDED_ITEM );
		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );
		nameSection.setProvider( nameProvider );
		nameSection.setLayoutNum( 2 );
		nameSection.setWidth( 200 );
		addSection( CrosstabPageSectionId.CROSSTAB_NAME, nameSection );

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider( );
		TextSection elementIdSection = new TextSection( elementIdProvider.getDisplayName( ),
				container,
				true );
		elementIdSection.setProvider( elementIdProvider );
		elementIdSection.setWidth( 200 );
		elementIdSection.setLayoutNum( 4 );
		elementIdSection.setGridPlaceholder( 2, true );
		addSection( CrosstabPageSectionId.CROSSTAB_ELEMENT_ID, elementIdSection );

		// IDescriptorProvider cubeProvider = new
		// CrosstabSimpleComboPropertyDescriptorProvider(
		// ICrosstabReportItemConstants.CUBE_PROP,/*
		// ICrosstabReportItemConstants.CUBE_PROP */
		// ReportDesignConstants.EXTENDED_ITEM );
		// CrosstabSimpleComboSection cubeSection = new
		// CrosstabSimpleComboSection(
		// cubeProvider.getDisplayName( ),
		// container,
		// true );
		// cubeSection.setProvider( cubeProvider );
		// cubeSection.setWidth( 280 );
		// cubeSection.setGridPlaceholder( 2, true );
		// addSection( CrosstabPageSectionId.CUBE, cubeSection );

		// ContainerSection formatOptionSection = new ContainerSection(
		// Messages.getString( "CrosstabGeneraPage.FormatOption" ),
		// container,
		// true );
		// // formatOptionSection.setProvider( formatOptionProvider );
		//
		// formatOptionSection.setGridPlaceholder( 2, true );
		// addSection( CrosstabPageSectionId.FORMAT_OPTION, formatOptionSection
		// );

		// layoutMeasuresProvider = new LayoutMeasuresProvider( );
		// InnerCheckSection layoutMeasuresSection = new InnerCheckSection(
		// container,
		// true );
		// layoutMeasuresSection.setProvider( layoutMeasuresProvider );
		// layoutMeasuresSection.setLayoutNum( 4 );
		// layoutMeasuresSection.setGridPlaceholder(2, true);
		// addSection( CrosstabPageSectionId.LAYOUT_MEASURES,
		// layoutMeasuresSection );

		// IChoiceSet choiceSet =
		// ChoiceSetFactory.getElementChoiceSet(ICrosstabConstants.
		// CROSSTAB_EXTENSION_NAME
		// ,
		// ICrosstabReportItemConstants.MEASURE_DIRECTION_PROP );

		layoutMeasuresProvider = new MeasureComboPropertyDescriptorProvider( ICrosstabReportItemConstants.MEASURE_DIRECTION_PROP,
				ICrosstabConstants.CROSSTAB_EXTENSION_NAME );
		ComboSection layoutMeasureSection = new ComboSection( Messages.getString( "LayoutMeasuresSection.DisplayName" ), //$NON-NLS-1$
				container,
				true );
		layoutMeasureSection.setProvider( layoutMeasuresProvider );
		layoutMeasureSection.setWidth( 200 );
		layoutMeasureSection.setLayoutNum( 2 );
		addSection( CrosstabPageSectionId.LAYOUT_MEASURES, layoutMeasureSection );

		HideMeasureHeaderProvider hideMeasureProvider = new HideMeasureHeaderProvider( ICrosstabReportItemConstants.HIDE_MEASURE_HEADER_PROP,
				ReportDesignConstants.EXTENDED_ITEM );
		CheckSection hideMeasureSection = new CheckSection( container, true );
		hideMeasureSection.setProvider( hideMeasureProvider );
		hideMeasureSection.setLayoutNum( 4 );
		hideMeasureSection.setGridPlaceholder( 2, true );
		addSection( CrosstabPageSectionId.HIDE_MEASURE_HEADER,
				hideMeasureSection );
	}
}
