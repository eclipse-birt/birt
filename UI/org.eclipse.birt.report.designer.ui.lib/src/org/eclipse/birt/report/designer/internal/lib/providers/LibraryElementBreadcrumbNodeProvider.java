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

package org.eclipse.birt.report.designer.internal.lib.providers;

import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.DefaultBreadcrumbNodeProvider;
import org.eclipse.birt.report.model.api.LibraryHandle;

/**
 *
 */

public class LibraryElementBreadcrumbNodeProvider extends DefaultBreadcrumbNodeProvider {

	@Override
	public Object[] getChildren(Object element) {
		if (getRealModel(element) instanceof LibraryHandle) {
			return ((LibraryHandle) getRealModel(element)).getComponents().getContents().toArray();
		}
		return super.getChildren(element);
	}
}
