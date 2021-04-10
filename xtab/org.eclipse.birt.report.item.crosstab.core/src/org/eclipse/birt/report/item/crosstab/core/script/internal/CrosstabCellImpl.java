/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public long getCellID() {
		return id;
	}

	public String getCellType() {
		return type;
	}

}
