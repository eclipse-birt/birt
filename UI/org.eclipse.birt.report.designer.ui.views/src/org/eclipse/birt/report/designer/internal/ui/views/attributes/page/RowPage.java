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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ColorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.UnitSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The general attribute page of Row element.
 */
public class RowPage extends GeneralFontPage
{

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 6 ,15) );

		// Defines providers.

		IDescriptorProvider heightProvider = new UnitPropertyDescriptorProvider( RowHandle.HEIGHT_PROP,
				ReportDesignConstants.ROW_ELEMENT );

		IDescriptorProvider hAlignProvider = new ComboPropertyDescriptorProvider( StyleHandle.TEXT_ALIGN_PROP,
				ReportDesignConstants.STYLE_ELEMENT );

		IDescriptorProvider backgroundProvider = new ColorPropertyDescriptorProvider( StyleHandle.BACKGROUND_COLOR_PROP,
				ReportDesignConstants.STYLE_ELEMENT );

		IDescriptorProvider vAlignProvider = new ComboPropertyDescriptorProvider( StyleHandle.VERTICAL_ALIGN_PROP,
				ReportDesignConstants.STYLE_ELEMENT );

		IDescriptorProvider styleProvider = new SimpleComboPropertyDescriptorProvider( ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.ROW_ELEMENT );

		// Defines sections.

		UnitSection heightSection = new UnitSection( heightProvider.getDisplayName( ),
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

		// Sets providers.

		heightSection.setProvider( heightProvider );
		hAlignSection.setProvider( hAlignProvider );
		backgroundSection.setProvider( backgroundProvider );
		vAlignSection.setProvider( vAlignProvider );
		styleSection.setProvider( styleProvider );

		// Sets widths.

		heightSection.setWidth( 200 );
		hAlignSection.setWidth( 200 );
		backgroundSection.setWidth( 200 );
		vAlignSection.setWidth( 200 );
		styleSection.setWidth( 200 );

		// Sets layout num.

		heightSection.setLayoutNum( 2 );
		hAlignSection.setLayoutNum( 4 );
		backgroundSection.setLayoutNum( 2 );
		vAlignSection.setLayoutNum( 4 );
		styleSection.setLayoutNum( 6 );

		// Sets fill grid num.

		heightSection.setGridPlaceholder( 0, true );
		hAlignSection.setGridPlaceholder( 2, true );
		backgroundSection.setGridPlaceholder( 0, true );
		vAlignSection.setGridPlaceholder( 2, true );
		styleSection.setGridPlaceholder( 4, true );

		// Adds sections into container page.

		addSection( PageSectionId.ROW_HEIGHT, heightSection ); //$NON-NLS-1$
		addSection( PageSectionId.ROW_HORIZONTAL_ALIGN, hAlignSection ); //$NON-NLS-1$
		addSection( PageSectionId.ROW_BACKGROUND_COLOR, backgroundSection ); //$NON-NLS-1$
		addSection( PageSectionId.ROW_VERTICAL_ALIGN, vAlignSection ); //$NON-NLS-1$
		
		Section seperatorSection1 = new SeperatorSection( container, SWT.HORIZONTAL );
		addSection( PageSectionId.ROW_SEPERATOR1, seperatorSection1 ); //$NON-NLS-1$

		addFontsSection( );
		
		addSection( PageSectionId.ROW_SEPERATOR, seperatorSection ); //$NON-NLS-1$
		addSection( PageSectionId.ROW_STYLE, styleSection ); //$NON-NLS-1$

		createSections( );
		layoutSections( );
	}
}
