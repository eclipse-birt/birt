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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.LibraryDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.MeasureComboPropertyDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.InnerTextSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class CrosstabGeneralPage extends AttributePage
{

	private TextSection librarySection;
	private SeperatorSection seperatorSection;
	// private Button chkBrandColumn;
	// private Button chkBrandRow;

	IDescriptorProvider grandTotalColumnProvider, grandTotalRowProvider;
	IDescriptorProvider layoutMeasuresProvider;

	public void buildContent( )
	{
		container.setLayout( WidgetUtil.createGridLayout( 4, 15 ) );

		LibraryDescriptorProvider provider = new LibraryDescriptorProvider( );
		librarySection = new TextSection( provider.getDisplayName( ),
				container,
				true );
		librarySection.setProvider( provider );
		librarySection.setGridPlaceholder( 1, true );
		addSection( CrosstabPageSectionId.CROSSTAB_LIBRARY, librarySection );

		seperatorSection = new SeperatorSection( container, SWT.HORIZONTAL );
		addSection( CrosstabPageSectionId.CROSSTAB_SEPERATOR, seperatorSection );

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ReportItemHandle.NAME_PROP,
				ReportDesignConstants.EXTENDED_ITEM );
		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );
		nameSection.setProvider( nameProvider );
		nameSection.setGridPlaceholder( 2, true );
		// nameSection.setWidth( 280 );
		addSection( CrosstabPageSectionId.CROSSTAB_NAME, nameSection );

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider( );
		TextSection elementIdSection = new TextSection( elementIdProvider.getDisplayName( ),
				container,
				true );
		elementIdSection.setProvider( elementIdProvider );
		elementIdSection.setWidth( 200 );
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
		// ChoiceSetFactory.getElementChoiceSet(ICrosstabConstants.CROSSTAB_EXTENSION_NAME
		// ,
		// ICrosstabReportItemConstants.MEASURE_DIRECTION_PROP );

		layoutMeasuresProvider = new MeasureComboPropertyDescriptorProvider( ICrosstabReportItemConstants.MEASURE_DIRECTION_PROP,
				ICrosstabConstants.CROSSTAB_EXTENSION_NAME );
		ComboSection layoutMeasureSection = new ComboSection( Messages.getString( "LayoutMeasuresSection.DisplayName" ),
				container,
				true );
		layoutMeasureSection.setProvider( layoutMeasuresProvider );
		layoutMeasureSection.setWidth( 200 );
		layoutMeasureSection.setGridPlaceholder( 2, true );
		addSection( CrosstabPageSectionId.LAYOUT_MEASURES, layoutMeasureSection );

		TextPropertyDescriptorProvider emptyCellValueProvider = new TextPropertyDescriptorProvider( ICrosstabReportItemConstants.EMPTY_CELL_VALUE_PROP,
				ReportDesignConstants.EXTENDED_ITEM );
		InnerTextSection emptyCellValueSection = new InnerTextSection( Messages.getString( "CrosstabGeneraPage.ForEmptyCell" ),
				container,
				true );
		emptyCellValueSection.setProvider( emptyCellValueProvider );
		emptyCellValueSection.setLayoutNum( 3 );
		emptyCellValueSection.setGridPlaceholder( 2, true );
		addSection( CrosstabPageSectionId.EMPTY_CELL_VALUE,
				emptyCellValueSection );

		createSections( );
		layoutSections( );
	}

	public void buildUI( Composite parent )
	{
		super.buildUI( parent );
		buildContent( );
	}

	// temporary refresh, should update later
	public void refresh( )
	{
		if ( input instanceof List
				&& DEUtil.getMultiSelectionHandle( (List) input )
						.isExtendedElements( ) )
		{
			librarySection.setHidden( false );
			seperatorSection.setHidden( false );
			librarySection.load( );
		}
		else
		{
			librarySection.setHidden( true );
			seperatorSection.setHidden( true );
		}

		container.layout( true );
		container.redraw( );

		super.refresh( );

	}
}
