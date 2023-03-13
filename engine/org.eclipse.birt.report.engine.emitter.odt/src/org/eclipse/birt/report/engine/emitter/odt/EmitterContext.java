/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.odt;

import java.util.LinkedList;

import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.odf.AbstractOdfEmitterContext;
import org.eclipse.birt.report.engine.odf.OdfUtil;
import org.eclipse.birt.report.engine.odf.style.StyleBuilder;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.odf.style.StyleManager;

public class EmitterContext extends AbstractOdfEmitterContext {

	private LinkedList<Double> widthList = new LinkedList<>();

	private boolean isFirst = true;
	private boolean inMasterPage = false;

	public EmitterContext() {
		super();
		inMasterPage = false;
	}

	public void startInline() {
		isFirst = false;
	}

	public boolean isFirstInline() {
		return isFirst;
	}

	public void endInline() {
		isFirst = true;
	}

	public void addWidth(double witdh) {
		widthList.addLast(witdh);
	}

	public void resetWidth() {
		widthList.clear();
	}

	public double getCurrentWidth() {
		return widthList.getLast();
	}

	public void removeWidth() {
		widthList.removeLast();
	}

	public double getCellWidth(int columnId, int columnSpan) {
		double[] cols = getCurrentTableColmns();

		double width = 0;

		int colNum = Math.min(columnId + columnSpan, OdtEmitter.MAX_COLUMN);

		for (int i = columnId; i < colNum; i++) {
			width += cols[i];
		}

		return width;
	}

	public void startMasterPage() {
		inMasterPage = true;
	}

	public void endMasterPage() {
		inMasterPage = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.odf.AbstractEmitterContext#getStyleManager()
	 */
	@Override
	public StyleManager getStyleManager() {
		// when requestng style creation in the master page,
		// the styles must be added to the global style manager
		if (inMasterPage) {
			return super.getGlobalStyleManager();
		} else {
			return super.getStyleManager();
		}
	}

	/**
	 * Returns a style instance for the given row height
	 *
	 * @param rowHeight
	 * @return style instance
	 */
	public StyleEntry getRowHeightStyle(DimensionType rowHeight) {
		StyleEntry style = tables.getLast().getRowHeightStyle(rowHeight);
		if (style == null && rowHeight != null) {
			style = StyleBuilder.createEmptyStyleEntry(StyleEntry.TYPE_TABLE_ROW);
			style.setProperty(StyleConstant.MIN_HEIGHT, OdfUtil.convertTo(rowHeight, dpi));
			tables.getLast().addRowHeightStyle(rowHeight, style);
		}
		return style;
	}
}
