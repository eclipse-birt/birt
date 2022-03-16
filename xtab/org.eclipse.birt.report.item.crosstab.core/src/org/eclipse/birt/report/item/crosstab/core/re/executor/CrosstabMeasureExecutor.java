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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

/**
 * CrosstabMeasureExecutor
 */
public class CrosstabMeasureExecutor extends BaseCrosstabExecutor {

	private int currentElement;
	private List elements;

	public CrosstabMeasureExecutor(BaseCrosstabExecutor parent) {
		super(parent);
	}

	@Override
	public void close() {
		super.close();

		elements = null;
	}

	@Override
	public IContent execute() {
		ITableBandContent content = context.getReportContent().createTableBandContent();
		content.setBandType(IBandContent.BAND_DETAIL);

		initializeContent(content, null);

		prepareChildren();

		return content;
	}

	private void prepareChildren() {
		elements = new ArrayList();
		currentElement = 0;

		int count = crosstabItem.getMeasureCount();
		int totalRow = (count > 1 && MEASURE_DIRECTION_VERTICAL.equals(crosstabItem.getMeasureDirection())) ? count
				: Math.min(count, 1);

		for (int i = 0; i < totalRow; i++) {
			elements.add(new CrosstabMeasureRowExecutor(this, i));
		}
	}

	@Override
	public IReportItemExecutor getNextChild() {
		return (IReportItemExecutor) elements.get(currentElement++);
	}

	@Override
	public boolean hasNextChild() {
		if (currentElement < elements.size()) {
			return true;
		}

		return false;
	}

}
