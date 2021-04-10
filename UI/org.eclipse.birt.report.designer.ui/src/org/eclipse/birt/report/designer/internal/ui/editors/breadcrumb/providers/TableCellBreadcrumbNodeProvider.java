/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
