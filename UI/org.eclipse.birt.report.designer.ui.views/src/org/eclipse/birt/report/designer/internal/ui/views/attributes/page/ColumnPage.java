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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.CheckSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.UnitSection;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The general attribute page of Column element.
 */
public class ColumnPage extends AttributePage
{

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 5 ,15) );

		// Defines providers.

		IDescriptorProvider widthProvider = new UnitPropertyDescriptorProvider( ColumnHandle.WIDTH_PROP,
				ReportDesignConstants.COLUMN_ELEMENT );

		IDescriptorProvider hAlignProvider = new ComboPropertyDescriptorProvider( StyleHandle.TEXT_ALIGN_PROP,
				ReportDesignConstants.STYLE_ELEMENT );

		IDescriptorProvider backgroundProvider = new ColorPropertyDescriptorProvider( StyleHandle.BACKGROUND_COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT );

		IDescriptorProvider vAlignProvider = new ComboPropertyDescriptorProvider( StyleHandle.VERTICAL_ALIGN_PROP,
				ReportDesignConstants.STYLE_ELEMENT );

		IDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider( ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.COLUMN_ELEMENT );

		IDescriptorProvider suppressDuplicatesProvider = new PropertyDescriptorProvider( ColumnHandle.SUPPRESS_DUPLICATES_PROP,
				ReportDesignConstants.COLUMN_ELEMENT );

		// Defines sections.

		UnitSection widthSection = new UnitSection( widthProvider.getDisplayName( ),
				container,
				true );

		ComboSection hAlignSection = new ComboSection( hAlignProvider.getDisplayName( ),
				container,
				true );

		ColorSection backgroundSection = new ColorSection( backgroundProvider.getDisplayName( ),
				container,
				true );

		ComboSection vAlignSection = new ComboSection( vAlignProvider.getDisplayName( ),
				container,
				true );

		Section seperatorSection = new SeperatorSection( container, SWT.HORIZONTAL );

		SimpleComboSection styleSection = new SimpleComboSection( styleProvider.getDisplayName( ),
				container,
				true );

		CheckSection suppressDuplicatesSection = new CheckSection( container, true );

		// Sets providers.

		widthSection.setProvider( widthProvider );
		hAlignSection.setProvider( hAlignProvider );
		backgroundSection.setProvider( backgroundProvider );
		vAlignSection.setProvider( vAlignProvider );
		styleSection.setProvider( styleProvider );
		suppressDuplicatesSection.setProvider( suppressDuplicatesProvider );

		// Sets widths.

		widthSection.setWidth( 180 );
		hAlignSection.setWidth( 180 );
		backgroundSection.setWidth( 180 );
		vAlignSection.setWidth( 180 );
		styleSection.setWidth( 180 );

		// Sets layout num.

		widthSection.setLayoutNum( 2 );
		hAlignSection.setLayoutNum( 3 );
		backgroundSection.setLayoutNum( 2 );
		vAlignSection.setLayoutNum( 3 );
		styleSection.setLayoutNum( 2 );
		suppressDuplicatesSection.setLayoutNum( 3 );

		// Sets fill grid num.

		widthSection.setGridPlaceholder( 0, true );
		hAlignSection.setGridPlaceholder( 1, true );
		backgroundSection.setGridPlaceholder( 0, true );
		vAlignSection.setGridPlaceholder( 1, true );
		styleSection.setGridPlaceholder( 0, true );
		suppressDuplicatesSection.setGridPlaceholder( 2, true );

		// Adds sections into this page.

		addSection( PageSectionId.COLUMN_WIDTH, widthSection ); //$NON-NLS-1$
		addSection( PageSectionId.COLUMN_HORIZONTAL_ALIGN, hAlignSection ); //$NON-NLS-1$
		addSection( PageSectionId.COLUMN_BACKGROUND_COLOR, backgroundSection ); //$NON-NLS-1$
		addSection( PageSectionId.COLUMN_VERTICAL_ALIGN, vAlignSection ); //$NON-NLS-1$
		addSection( PageSectionId.COLUMN_SEPERATOR, seperatorSection ); //$NON-NLS-1$
		addSection( PageSectionId.COLUMN_STYLE, styleSection ); //$NON-NLS-1$
		addSection( PageSectionId.COLUMN_SUPPRESS_DUPLICATES, suppressDuplicatesSection ); //$NON-NLS-1$

		createSections( );
		layoutSections( );
	}
}
