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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UnitPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.UnitSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * The general attribute page of Cell element.
 */
public class CrosstabCellPage extends CellPage
{	
	protected void applyCustomSections(){
		IDescriptorProvider widthProvider = new UnitPropertyDescriptorProvider( ReportItemHandle.WIDTH_PROP,
				ReportDesignConstants.REPORT_ITEM );
		UnitSection widthSection = new UnitSection( widthProvider.getDisplayName( ),
				container,
				true );
		widthSection.setProvider( widthProvider );
		widthSection.setWidth( 200 );
		widthSection.setLayoutNum( 6 );
		widthSection.setGridPlaceholder( 4, true );
		addSectionAfter( CrosstabPageSectionId.CROSSTAB_CELL_WIDTH, widthSection, PageSectionId.CELL_STYLE); 
	}
}
