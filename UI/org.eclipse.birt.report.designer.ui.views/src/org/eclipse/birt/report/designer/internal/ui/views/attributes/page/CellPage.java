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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.Page;

/**
 * The general attribute page of Cell element.
 */
public class CellPage extends AttributePage
{

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 5 ,15) );

		// Defines providers.

		IDescriptorProvider dropProvider = new ComboPropertyDescriptorProvider( CellHandle.DROP_PROP,
				ReportDesignConstants.CELL_ELEMENT );

		IDescriptorProvider backgroundProvider = new ColorPropertyDescriptorProvider( StyleHandle.BACKGROUND_COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT );

		IDescriptorProvider vAlignProvider = new ComboPropertyDescriptorProvider( StyleHandle.VERTICAL_ALIGN_PROP,
				ReportDesignConstants.STYLE_ELEMENT );

		IDescriptorProvider hAlignProvider = new ComboPropertyDescriptorProvider( StyleHandle.TEXT_ALIGN_PROP,
				ReportDesignConstants.STYLE_ELEMENT );

		IDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider( ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.CELL_ELEMENT );

		// Defines sections.

		ComboSection dropSection = new ComboSection( dropProvider.getDisplayName( ),
				container,
				true );

		ColorSection backgroundSection = new ColorSection( backgroundProvider.getDisplayName( ),
				container,
				true );

		ComboSection vAlignSection = new ComboSection( vAlignProvider.getDisplayName( ),
				container,
				true );

		ComboSection hAlignSection = new ComboSection( hAlignProvider.getDisplayName( ),
				container,
				true );

		Section seperatorSection = new SeperatorSection( container, SWT.HORIZONTAL );

		SimpleComboSection styleSection = new SimpleComboSection( styleProvider.getDisplayName( ),
				container,
				true );

		// Sets providers.

		dropSection.setProvider( dropProvider );
		backgroundSection.setProvider( backgroundProvider );
		vAlignSection.setProvider( vAlignProvider );
		hAlignSection.setProvider( hAlignProvider );
		styleSection.setProvider( styleProvider );

		// Sets widths.

		dropSection.setWidth( 150 );
		backgroundSection.setWidth( 150 );
		vAlignSection.setWidth( 150 );
		hAlignSection.setWidth( 150 );
		styleSection.setWidth( 150 );

		// Sets layout num.

		dropSection.setLayoutNum( 2 );
		backgroundSection.setLayoutNum( 3 );
		vAlignSection.setLayoutNum( 2 );
		hAlignSection.setLayoutNum( 3 );
		styleSection.setLayoutNum( 5 );

		// Sets fill grid num.

		dropSection.setGridPlaceholder( 0, true );
		backgroundSection.setGridPlaceholder( 1, true );
		vAlignSection.setGridPlaceholder( 0, true );
		hAlignSection.setGridPlaceholder( 1, true );
		styleSection.setGridPlaceholder( 3, true );

		// Adds sections into this page.

		addSection( PageSectionId.CELL_DROP, dropSection ); //$NON-NLS-1$
		addSection( PageSectionId.CELL_BACKGROUND, backgroundSection ); //$NON-NLS-1$
		addSection( PageSectionId.CELL_VERTICAL_ALIGN, vAlignSection ); //$NON-NLS-1$
		addSection( PageSectionId.CELL_HORIZONTAL_ALIGN, hAlignSection ); //$NON-NLS-1$
		addSection( PageSectionId.CELL_SEPERATOR, seperatorSection ); //$NON-NLS-1$
		addSection( PageSectionId.CELL_STYLE, styleSection ); //$NON-NLS-1$

		createSections( );
		layoutSections( );
	}
}
