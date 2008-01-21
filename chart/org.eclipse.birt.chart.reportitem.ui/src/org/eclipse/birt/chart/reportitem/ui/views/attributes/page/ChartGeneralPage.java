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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.page;

import java.util.List;

import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartUnitPropertyDescriptorProvider;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChoicePropertyDescriptorProvider;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.section.ChoiceSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.LibraryDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.UnitSection;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ChartGeneralPage extends AttributePage
{

	private TextSection librarySection;
	private SeperatorSection seperatorSection;


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

	public void buildUI( Composite parent )
	{
		super.buildUI( parent );
		container.setLayout(WidgetUtil.createGridLayout( 5, 15 ));
		
		LibraryDescriptorProvider provider = new LibraryDescriptorProvider( );
		librarySection = new TextSection( provider.getDisplayName( ),
				container,
				true );
		librarySection.setProvider( provider );
		librarySection.setGridPlaceholder( 2, true );
		addSection( ChartPageSectionId.CHART_LIBRARY, librarySection );

		seperatorSection = new SeperatorSection( container, SWT.HORIZONTAL );
		addSection( ChartPageSectionId.CHART_SEPERATOR, seperatorSection );

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ReportItemHandle.NAME_PROP,
				ReportDesignConstants.EXTENDED_ITEM );
		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );
		nameSection.setProvider( nameProvider );
		nameSection.setLayoutNum( 2 );
		nameSection.setGridPlaceholder( 0, true );
		nameSection.setWidth( 200 );
		addSection( ChartPageSectionId.CHART_NAME, nameSection );
		
		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider( );
		TextSection elementIdSection = new TextSection( elementIdProvider.getDisplayName( ),
				container,
				true );
		elementIdSection.setProvider( elementIdProvider );
		elementIdSection.setWidth( 200 );
		elementIdSection.setLayoutNum( 3 );
		elementIdSection.setGridPlaceholder( 1, true );
		addSection( ChartPageSectionId.CHART_ELEMENT_ID, elementIdSection );

		SeperatorSection seperator1 = new SeperatorSection( container,
				SWT.HORIZONTAL );
		addSection( ChartPageSectionId.CHART_SEPERATOR_1, seperator1 );

		IDescriptorProvider widthProvider = new ChartUnitPropertyDescriptorProvider( ReportItemHandle.WIDTH_PROP,
				ReportDesignConstants.EXTENDED_ITEM );
		UnitSection widthSection = new UnitSection( widthProvider.getDisplayName( ),
				container,
				true );
		widthSection.setWidth( 200 );
		widthSection.setProvider( widthProvider );
		widthSection.setLayoutNum( 2 );
		addSection( ChartPageSectionId.CHART_WIDTH, widthSection );

		IDescriptorProvider heightProvider = new ChartUnitPropertyDescriptorProvider( ReportItemHandle.HEIGHT_PROP,
				ReportDesignConstants.EXTENDED_ITEM );
		UnitSection heightSection = new UnitSection( heightProvider.getDisplayName( ),
				container,
				true );
		heightSection.setProvider( heightProvider );
		heightSection.setWidth( 200 );
		heightSection.setLayoutNum( 3 );
		heightSection.setGridPlaceholder( 1, true );
		addSection( ChartPageSectionId.CHART_HEIGHT, heightSection );

		IDescriptorProvider styleProvider = new ChoicePropertyDescriptorProvider( ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.EXTENDED_ITEM );
		ChoiceSection styleSection = new ChoiceSection( styleProvider.getDisplayName( ),
				container,
				true );
		styleSection.setProvider( styleProvider );
		styleSection.setWidth( 200 );
		styleSection.setGridPlaceholder( 3, true );
		addSection( ChartPageSectionId.CHART_STYLE, styleSection );

		createSections( );
		layoutSections( );
	}

}
