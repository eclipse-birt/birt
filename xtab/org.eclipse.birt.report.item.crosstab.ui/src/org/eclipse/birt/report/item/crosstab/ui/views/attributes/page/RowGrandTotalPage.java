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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.GrandTotalProvider;
import org.eclipse.swt.widgets.Composite;


/**
 * 
 */

public class RowGrandTotalPage extends AttributePage
{

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 1 ) );
		GrandTotalProvider grandTotalProvider = new GrandTotalProvider( );
		grandTotalProvider.setAxis( ICrosstabConstants.ROW_AXIS_TYPE  );
		FormSection grandTotalSection = new FormSection( grandTotalProvider.getDisplayName( ),
				container,
				true );
		grandTotalSection.setProvider( grandTotalProvider );
		grandTotalSection.setButtonWithDialog( true );
		grandTotalSection.setStyle( FormPropertyDescriptor.NO_UP_DOWN );
		grandTotalSection.setFillForm( true );
		grandTotalSection.setHeight( 170 );
		addSection( PageSectionId.SUB_TOTALS, grandTotalSection );
		createSections( );
		layoutSections( );
	}

}
