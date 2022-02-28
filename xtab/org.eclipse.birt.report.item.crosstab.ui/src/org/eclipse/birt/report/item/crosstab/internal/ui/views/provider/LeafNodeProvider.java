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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

public class LeafNodeProvider extends DefaultNodeProvider {

	@Override
	public Object getParent(Object model) {
		if (model instanceof DesignElementHandle) {
			DesignElementHandle container = ((DesignElementHandle) model).getContainer();
			if (container instanceof ExtendedItemHandle) {
				try {
					IReportItem item = ((ExtendedItemHandle) container).getReportItem();
					if (item instanceof CrosstabCellHandle) {
						return container;
					}
				} catch (ExtendedElementException e) {
				}
			}
		}
		return super.getParent(model);
	}
}
