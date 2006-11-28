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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontSizePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontStylePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FontSizeSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FontStyleSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;

/**
 * The general attribute page of Label element.
 */
public class LabelPage extends GeneralPage
{
	public void buildContent(   )
	{
		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ReportItemHandle.NAME_PROP,
				ReportDesignConstants.LABEL_ITEM );
		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );
		nameSection.setProvider( nameProvider );
		nameSection.setGridPlaceholder( 4, true );
		nameSection.setWidth( 200 );
		addSection( PageSectionId.LABEL_NAME, nameSection );

		SeperatorSection seperator = new SeperatorSection( container, SWT.HORIZONTAL );
		addSection( PageSectionId.LABEL_SEPERATOR, seperator );

		ComboPropertyDescriptorProvider fontFamilyProvider = new ComboPropertyDescriptorProvider( StyleHandle.FONT_FAMILY_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		ComboSection fontFamilySection = new ComboSection( fontFamilyProvider.getDisplayName( ),
				container,
				true );
		fontFamilySection.setProvider( fontFamilyProvider );
		fontFamilySection.setLayoutNum( 2 );
		fontFamilySection.setWidth( 200 );
		addSection( PageSectionId.LABEL_FONT_FAMILY, fontFamilySection );

		FontSizePropertyDescriptorProvider fontSizeProvider = new FontSizePropertyDescriptorProvider( StyleHandle.FONT_SIZE_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		FontSizeSection fontSizeSection = new FontSizeSection( fontSizeProvider.getDisplayName( ),
				container,
				true );
		fontSizeSection.setProvider( fontSizeProvider );
		fontSizeSection.setLayoutNum( 4 );
		fontSizeSection.setGridPlaceholder( 2, true );
		fontSizeSection.setWidth( 200 );
		addSection( PageSectionId.LABEL_FONT_SIZE, fontSizeSection );

		ColorPropertyDescriptorProvider colorProvider = new ColorPropertyDescriptorProvider( StyleHandle.COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		ColorSection colorSection = new ColorSection( colorProvider.getDisplayName( ),
				container,
				true );
		colorSection.setProvider( colorProvider );
		colorSection.setLayoutNum( 2 );
		colorSection.setWidth( 200 );
		addSection( PageSectionId.LABEL_COLOR, colorSection );

		ColorPropertyDescriptorProvider bgColorProvider = new ColorPropertyDescriptorProvider( StyleHandle.BACKGROUND_COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		ColorSection bgColorSection = new ColorSection( bgColorProvider.getDisplayName( ),
				container,
				true );
		bgColorSection.setProvider( bgColorProvider );
		bgColorSection.setLayoutNum( 4 );
		bgColorSection.setGridPlaceholder( 2, true );
		bgColorSection.setWidth( 200 );
		addSection( PageSectionId.LABEL_BGCOLOR, bgColorSection );

		String[] textStyles = new String[]{
				StyleHandle.FONT_WEIGHT_PROP,
				StyleHandle.FONT_STYLE_PROP,
				StyleHandle.TEXT_UNDERLINE_PROP,
				StyleHandle.TEXT_LINE_THROUGH_PROP,
		};

		IDescriptorProvider[] providers = new IDescriptorProvider[5];
		for ( int i = 0; i < textStyles.length; i++ )
		{
			providers[i] = new FontStylePropertyDescriptorProvider( textStyles[i],
					ReportDesignConstants.STYLE_ELEMENT );
		}
		providers[4] = new PropertyDescriptorProvider( StyleHandle.TEXT_ALIGN_PROP,
				ReportDesignConstants.STYLE_ELEMENT );
		FontStyleSection fontStyleSection = new FontStyleSection( container,
				true,
				true );
		fontStyleSection.setProviders( providers );
		fontStyleSection.setGridPlaceholder( 3, true );
		fontStyleSection.setWidth( 200 );
		addSection( PageSectionId.LABEL_FONT_STYLE, fontStyleSection );
		/*
		 * WidgetUtil.createGridPlaceholder( container, 1, false );
		 * 
		 * Composite container = WidgetUtil.buildFontStyleUI( container,
		 * propertiesMap ); GridData data = new GridData( ); data.horizontalSpan =
		 * 3; container.setLayoutData( data );
		 * 
		 * int height = container.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		 * Label separator = new Label( container, SWT.SEPARATOR | SWT.VERTICAL |
		 * SWT.CENTER ); int width = separator.computeSize( SWT.DEFAULT,
		 * SWT.DEFAULT ).x + WidgetUtil.SPACING 2; separator.setLayoutData( new
		 * RowData( width, height ) );
		 * 
		 * IPropertyDescriptor descriptor =
		 * PropertyDescriptorFactory.getPropertyDescriptor(
		 * ReportDesignConstants.STYLE_ELEMENT, StyleHandle.TEXT_ALIGN_PROP );
		 * propertiesMap.put( StyleHandle.TEXT_ALIGN_PROP, descriptor );
		 * descriptor.createControl( container );
		 */
		SeperatorSection seperator1 = new SeperatorSection( container,
				SWT.HORIZONTAL );
		addSection( PageSectionId.LABEL_SEPERATOR_1, seperator1 );

		SimpleComboPropertyDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider( ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.REPORT_ITEM );
		SimpleComboSection styleSection = new SimpleComboSection( styleProvider.getDisplayName( ),
				container,
				true );
		styleSection.setProvider( styleProvider );
		styleSection.setGridPlaceholder( 4, true );
		styleSection.setWidth( 200 );
		addSection( PageSectionId.LABEL_STYLE, styleSection );

		createSections( );
		layoutSections( );

	}
}