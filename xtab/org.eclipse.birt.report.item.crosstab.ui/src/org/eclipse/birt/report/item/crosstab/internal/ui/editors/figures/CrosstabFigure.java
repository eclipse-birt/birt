/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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
