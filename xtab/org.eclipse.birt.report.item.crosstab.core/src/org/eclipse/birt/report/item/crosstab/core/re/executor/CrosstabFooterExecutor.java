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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

/**
 * CrosstabFooterExecutor
 */
public class CrosstabFooterExecutor extends BaseCrosstabExecutor {

	private int currentRow;
	private int totalRow;

	public CrosstabFooterExecutor(BaseCrosstabExecutor parent) {
		super(parent);
	}

	public IContent execute() {
		ITableBandContent content = context.getReportContent().createTableBandContent();
		content.setBandType(ITableBandContent.BAND_FOOTER);

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
		return new CrosstabGrandTotalRowExecutor(this, currentRow++);
	}

	public boolean hasNextChild() {
		if (currentRow < totalRow) {
			if (GroupUtil.hasTotalContent(crosstabItem, ROW_AXIS_TYPE, -1, -1,
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
