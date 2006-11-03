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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class DataSourcePage extends AttributePage
{

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 3 ) );

		// Defines provider.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ReportItemHandle.NAME_PROP,
				ReportDesignConstants.DATA_SOURCE_ELEMENT );

		// Defines section.

		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );

		nameSection.setProvider( nameProvider );
		nameSection.setWidth( 500 );
		nameSection.setLayoutNum( 3 );
		nameSection.setGridPlaceholder( 1, true );

		// Adds section into this page.

		addSection( PageSectionId.DATASOURCE_NAME, nameSection ); //$NON-NLS-1$

		createSections( );
		layoutSections( );
	}
}
