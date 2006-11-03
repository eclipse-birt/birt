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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ColorPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * The general attribute page of ListItem element.
 */
public class ListPage extends GeneralPage
{

	
	void buildContent( )
	{

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ReportItemHandle.NAME_PROP,
				ReportDesignConstants.LIST_ITEM );
		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );
		nameSection.setProvider( nameProvider );
		nameSection.setGridPlaceholder( 4, true );
		nameSection.setWidth( 200 );
		addSection( PageSectionId.LIST_NAME, nameSection );

		SimpleComboPropertyDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider( ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.REPORT_ITEM );
		SimpleComboSection styleSection = new SimpleComboSection( styleProvider.getDisplayName( ),
				container,
				true );
		styleSection.setProvider( styleProvider );
		styleSection.setLayoutNum( 2 );
		styleSection.setWidth( 200 );
		addSection( PageSectionId.LIST_STYLE, styleSection );

		ColorPropertyDescriptorProvider colorProvider = new ColorPropertyDescriptorProvider( StyleHandle.BACKGROUND_COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		ColorSection colorSection = new ColorSection( colorProvider.getDisplayName( ),
				container,
				true );
		colorSection.setProvider( colorProvider );
		colorSection.setLayoutNum( 4 );
		colorSection.setGridPlaceholder( 2, true );
		colorSection.setWidth( 200 );
		addSection( PageSectionId.LIST_COLOR, colorSection );
		/*
		 * Label styleLabel = FormWidgetFactory.getInstance( ).createLabel(
		 * this, true ); TestSimpleComboPropertyDescriptor styleCombo =
		 * DescriptorToolkit.createSimpleComboPropertyDescriptor( true );
		 * SimpleComboPropertyDescriptorProvider comboProvider = new
		 * SimpleComboPropertyDescriptorProvider( ReportItemHandle.STYLE_PROP,
		 * ReportDesignConstants.REPORT_ITEM );
		 * styleCombo.setDescriptorProvider( comboProvider );
		 * styleLabel.setText( comboProvider.getDisplayName( ) );
		 * WidgetUtil.setGridData( styleCombo.createControl( this ), 1, 200 );
		 * descriptorContainer.add( styleCombo );
		 * 
		 * Label colorLabel = FormWidgetFactory.getInstance( ).createLabel(
		 * this, true ); TestColorPropertyDescriptor colorBuilder =
		 * DescriptorToolkit.createColorPropertyDescriptor( true );
		 * ColorPropertyDescriptorProvider colorProvider = new
		 * ColorPropertyDescriptorProvider( StyleHandle.BACKGROUND_COLOR_PROP,
		 * ReportDesignConstants.STYLE_ELEMENT );
		 * colorBuilder.setDescriptorProvider( colorProvider );
		 * colorLabel.setText( colorProvider.getDisplayName( ) );
		 * WidgetUtil.setGridData( colorBuilder.createControl( this ), 1, 200 );
		 * descriptorContainer.add( colorBuilder );
		 * 
		 * WidgetUtil.createGridPlaceholder( this, 2, true );
		 */
		createSections( );
		layoutSections( );
	}

}