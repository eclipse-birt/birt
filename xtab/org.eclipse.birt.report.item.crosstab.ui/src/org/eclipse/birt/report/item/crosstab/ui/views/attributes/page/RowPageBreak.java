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
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.PageBreakProvider;
import org.eclipse.swt.widgets.Composite;


/**
 * 
 */

public class RowPageBreak extends AttributePage
{
	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 1 ) );
		PageBreakProvider pageBreakProvider = new PageBreakProvider( );
		pageBreakProvider.setAxis( ICrosstabConstants.ROW_AXIS_TYPE );
		FormSection pageBreakSection = new FormSection( pageBreakProvider.getDisplayName( ),
				container,
				true );
		pageBreakSection.setProvider( pageBreakProvider );
		pageBreakSection.setButtonWithDialog( true );
		pageBreakSection.setStyle( FormPropertyDescriptor.NO_UP_DOWN );
		pageBreakSection.setFillForm( true );
		pageBreakSection.setHeight( 170 );
		addSection( PageSectionId.PAGE_BREAK, pageBreakSection );
		createSections( );
		layoutSections( );
	}
}
