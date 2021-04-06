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

import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.gef.EditPart;

/**
 * 
 */

public class ListElementBreadcrumbNodeProvider extends DefaultBreadcrumbNodeProvider {

	public Object getRealModel(Object element) {
		EditPart editpart = null;
		if (!(element instanceof EditPart)) {
			editpart = getEditPart(element);
		} else
			editpart = (EditPart) element;

		if (editpart != null && editpart.getModel() instanceof ListBandProxy) {
			ListBandProxy proxy = (ListBandProxy) editpart.getModel();
			return proxy.getSlotHandle();
		}
		return element;
	}
}
