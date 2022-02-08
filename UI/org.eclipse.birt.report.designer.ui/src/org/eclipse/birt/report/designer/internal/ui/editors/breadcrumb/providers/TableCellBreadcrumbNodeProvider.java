/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers;

import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * 
 */

public class TableCellBreadcrumbNodeProvider extends DefaultBreadcrumbNodeProvider {

	public Object getParent(Object element) {
		if (element instanceof DesignElementHandle)
			return ((DesignElementHandle) element).getContainer();
		return super.getParent(element);
	}

	public Object getRealModel(Object element) {
		return element;
	}
}
