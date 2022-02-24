/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SectionBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.TableBorder;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;

/**
 * Presents grid figure for grid edit part
 *
 * @author Dazhen Gao
 * @version $Revision: 1.1 $ $Date: 2005/02/05 06:30:14 $
 */
public class GridFigure extends TableFigure {

	private static final String BORDER_TEXT = Messages.getString("GridFigure.BORDER_TEXT"); //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public GridFigure() {
		SectionBorder border = new TableBorder();
		border.setIndicatorLabel(BORDER_TEXT);

		border.setIndicatorIcon(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_GRID));
		setBorder(border);
	}
}
