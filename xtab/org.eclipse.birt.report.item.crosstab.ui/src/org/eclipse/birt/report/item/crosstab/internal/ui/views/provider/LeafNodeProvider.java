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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

public class LeafNodeProvider extends DefaultNodeProvider {

	public Object getParent(Object model) {
		if (model instanceof DesignElementHandle) {
			DesignElementHandle container = ((DesignElementHandle) model).getContainer();
			if (container instanceof ExtendedItemHandle) {
				try {
					IReportItem item = ((ExtendedItemHandle) container).getReportItem();
					if (item instanceof CrosstabCellHandle)
						return container;
				} catch (ExtendedElementException e) {
				}
			}
		}
		return super.getParent(model);
	}
}
