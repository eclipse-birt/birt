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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.UnitSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;

/**
 * The general attribute page of Image element.
 */
public class ImagePage extends GeneralPage
{


	void buildContent( )
	{
		// Defines providers.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ReportItemHandle.NAME_PROP,
				ReportDesignConstants.IMAGE_ITEM );

		IDescriptorProvider widthProvider = new UnitPropertyDescriptorProvider( ReportItemHandle.WIDTH_PROP,
				ReportDesignConstants.REPORT_ITEM );

		IDescriptorProvider heightProvider = new UnitPropertyDescriptorProvider( ReportItemHandle.HEIGHT_PROP,
				ReportDesignConstants.REPORT_ITEM );

		IDescriptorProvider reportProvider = new SimpleComboPropertyDescriptorProvider( ReportItemHandle.STYLE_PROP,
				ReportDesignConstants.REPORT_ITEM );

		// Defines sections.

		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );

		Section seperator1Section = new SeperatorSection( container, SWT.HORIZONTAL );

		UnitSection widthSection = new UnitSection( widthProvider.getDisplayName( ),
				container,
				true );

		UnitSection heightSection = new UnitSection( heightProvider.getDisplayName( ),
				container,
				true );

		Section seperator2Section = new SeperatorSection( container, SWT.HORIZONTAL );

		SimpleComboSection reportSection = new SimpleComboSection( reportProvider.getDisplayName( ),
				container,
				true );

		// Sets providers.

		nameSection.setProvider( nameProvider );
		widthSection.setProvider( widthProvider );
		heightSection.setProvider( heightProvider );
		reportSection.setProvider( reportProvider );

		// Sets widths.

		nameSection.setWidth( 200 );
		widthSection.setWidth( 200 );
		heightSection.setWidth( 200 );
		reportSection.setWidth( 200 );

		// Sets layout num.

		nameSection.setLayoutNum( 2 );
		widthSection.setLayoutNum( 2 );
		heightSection.setLayoutNum( 4 );
		reportSection.setLayoutNum( 6 );

		// Sets fill grid num.

		nameSection.setGridPlaceholder( 0, true );
		widthSection.setGridPlaceholder( 0, true );
		heightSection.setGridPlaceholder( 2, true );
		reportSection.setGridPlaceholder( 4, true );

		// Adds sections into container page.

		addSection( PageSectionId.IMAGE_NAME, nameSection ); //$NON-NLS-1$
		
		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider( );
		TextSection elementIdSection = new TextSection( elementIdProvider.getDisplayName( ),
				container,
				true );
		elementIdSection.setProvider( elementIdProvider );
		elementIdSection.setWidth( 200 );
		elementIdSection.setLayoutNum( 4 );
		elementIdSection.setGridPlaceholder( 2, true );
		addSection( PageSectionId.IMAGE_ELEMENT_ID, elementIdSection );
		
		
		addSection( PageSectionId.IMAGE_SEPERATOR, seperator1Section ); //$NON-NLS-1$
		addSection( PageSectionId.IMAGE_WIDTH, widthSection ); //$NON-NLS-1$
		addSection( PageSectionId.IMAGE_HEIGHT, heightSection ); //$NON-NLS-1$
		addSection( PageSectionId.IMAGE_SEPERATOR_1, seperator2Section ); //$NON-NLS-1$
		addSection( PageSectionId.IMAGE_REPORT, reportSection ); //$NON-NLS-1$

		createSections( );
		layoutSections( );
	}
}
