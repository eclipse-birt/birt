/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabCell;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.SimpleRowItem;

/**
 * CrosstabCellImpl
 */
public class CrosstabCellImpl extends SimpleRowItem implements ICrosstabCell {

	private long id = -1;
	private String type = TYPE_HEADER;

	public CrosstabCellImpl(CrosstabCellHandle cch) {
		super((ExtendedItemHandle) cch.getModelHandle());

		if (cch.getModelHandle() != null) {
			id = cch.getModelHandle().getID();
		}

		if (cch instanceof AggregationCellHandle) {
			type = TYPE_AGGREGATION;
		}
	}

	@Override
	public long getCellID() {
		return id;
	}

	@Override
	public String getCellType() {
		return type;
	}

}
