/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractLayoutCheck;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * 
 */

public class CrosstabLayoutCheck extends AbstractLayoutCheck {
	@Override
	public boolean layoutCheck(Object model) {
		if (!(model instanceof ExtendedItemHandle)) {
			return true;
		}
		CrosstabReportItemHandle crossItem = null;
		try {
			Object obj = ((ExtendedItemHandle) model).getReportItem();
			if (!(obj instanceof CrosstabReportItemHandle)) {
				return true;
			}
			crossItem = (CrosstabReportItemHandle) obj;
		} catch (ExtendedElementException e) {
			// do nothing now
		}
		if (crossItem == null) {
			return true;
		}
		CrosstabHandleAdapter adapter = new CrosstabHandleAdapter(crossItem);
		adapter.getModelList();
		return adapter.layoutCheck();
	}
}
