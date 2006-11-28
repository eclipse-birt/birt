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
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class ScalarParameterPage extends AttributePage
{
	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 3 ,15) );

		// Defines providers.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider( ScalarParameterHandle.NAME_PROP,
				ReportDesignConstants.SCALAR_PARAMETER_ELEMENT );

		IDescriptorProvider dataTypeProvider = new TextPropertyDescriptorProvider( ScalarParameterHandle.DATA_TYPE_PROP,
				ReportDesignConstants.SCALAR_PARAMETER_ELEMENT );

		IDescriptorProvider ctrlTypeProvider = new TextPropertyDescriptorProvider( ScalarParameterHandle.CONTROL_TYPE_PROP,
				ReportDesignConstants.SCALAR_PARAMETER_ELEMENT );

		// Defines sections.

		TextSection nameSection = new TextSection( nameProvider.getDisplayName( ),
				container,
				true );

		TextSection dataTypeSection = new TextSection( dataTypeProvider.getDisplayName( ),
				container,
				true );

		TextSection ctrlTypeSection = new TextSection( ctrlTypeProvider.getDisplayName( ),
				container,
				true );

		// Sets providers.

		nameSection.setProvider( nameProvider );
		dataTypeSection.setProvider( dataTypeProvider );
		ctrlTypeSection.setProvider( ctrlTypeProvider );

		// Sets widths.

		nameSection.setWidth( 500 );
		dataTypeSection.setWidth( 500 );
		ctrlTypeSection.setWidth( 500 );

		// Sets layout num.

		nameSection.setLayoutNum( 3 );
		dataTypeSection.setLayoutNum( 3 );
		ctrlTypeSection.setLayoutNum( 3 );

		// Sets fill grid num.

		nameSection.setGridPlaceholder( 1, true );
		dataTypeSection.setGridPlaceholder( 1, true );
		ctrlTypeSection.setGridPlaceholder( 1, true );

		// Adds section into container page.

		addSection( PageSectionId.SCALAR_PARAMETER_NAME, nameSection ); //$NON-NLS-1$
		addSection( PageSectionId.SCALAR_PARAMETER_DATA_TYPE, dataTypeSection ); //$NON-NLS-1$
		addSection( PageSectionId.SCALAR_PARAMETER_CTRL_TYPE, ctrlTypeSection ); //$NON-NLS-1$

		createSections( );
		layoutSections( );
	}
}
