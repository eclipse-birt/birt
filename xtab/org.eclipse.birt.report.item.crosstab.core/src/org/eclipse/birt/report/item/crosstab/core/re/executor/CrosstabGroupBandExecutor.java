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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

/**
 * CrosstabGroupBandExecutor
 */
public class CrosstabGroupBandExecutor extends BaseCrosstabExecutor {

	private int bandType;
	private int dimensionIndex, levelIndex;

	private int currentRow;
	private int totalRow;

	public CrosstabGroupBandExecutor(BaseCrosstabExecutor parent, int dimensionIndex, int levelIndex, int bandType) {
		super(parent);

		this.bandType = bandType;
		this.dimensionIndex = dimensionIndex;
		this.levelIndex = levelIndex;
	}

	public IContent execute() {
		ITableBandContent content = context.getReportContent().createTableBandContent();
		content.setBandType(bandType);

		initializeContent(content, null);

		prepareChildren();

		return content;
	}

	private void prepareChildren() {
		currentRow = 0;

		int count = crosstabItem.getMeasureCount();
		totalRow = (count > 1 && MEASURE_DIRECTION_VERTICAL.equals(crosstabItem.getMeasureDirection())) ? count : 1;
	}

	public IReportItemExecutor getNextChild() {
		if (bandType == IBandContent.BAND_DETAIL) {
			return new CrosstabRowExecutor(this, currentRow++, dimensionIndex, levelIndex);
		} else {
			return new CrosstabSubTotalRowExecutor(this, currentRow++, dimensionIndex, levelIndex);
		}
	}

	public boolean hasNextChild() {
		if (currentRow < totalRow) {
			if (bandType == IBandContent.BAND_DETAIL) {
				return true;
			} else if (GroupUtil.hasTotalContent(crosstabItem, ROW_AXIS_TYPE, dimensionIndex, levelIndex,
					MEASURE_DIRECTION_VERTICAL.equals(crosstabItem.getMeasureDirection()) ? currentRow : -1)) {
				return true;
			} else {
				currentRow++;
				return hasNextChild();
			}
		}

		return false;
	}

}
