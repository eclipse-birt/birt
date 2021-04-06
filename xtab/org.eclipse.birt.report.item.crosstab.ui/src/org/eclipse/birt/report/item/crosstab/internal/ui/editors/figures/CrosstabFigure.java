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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SectionBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.TableBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;

/**
 * Crosstab figure
 */

public class CrosstabFigure extends ReportElementFigure {

	/**
	 * Constructor
	 */
	public CrosstabFigure() {
		super();
		SectionBorder border = new TableBorder();
		// border.setIndicatorLabel( "crostab" );// name come from adapt set

		// table name throught Adapt may be set icon
		border.setIndicatorIcon(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_TABLE));
		setBorder(border);
	}
}
