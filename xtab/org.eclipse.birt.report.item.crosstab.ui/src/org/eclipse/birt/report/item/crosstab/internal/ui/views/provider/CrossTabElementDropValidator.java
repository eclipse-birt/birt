/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import org.eclipse.birt.report.designer.core.model.IDropValidator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 *
 */

public class CrossTabElementDropValidator implements IDropValidator {

	private ExtendedItemHandle item;

	public CrossTabElementDropValidator(ExtendedItemHandle item) {
		this.item = item;
	}

	@Override
	public boolean canDrop() {
		if (item.getExtensionName().indexOf("Cell") > -1) //$NON-NLS-1$
		{
			boolean canDrop = item.getContents(DEUtil.getDefaultContentName(item)).size() > 0 && item.canDrop();
			return canDrop;
		}
		if (item.getExtensionName().equals("LevelView")) { //$NON-NLS-1$
			return false;
		}

		return true;
	}

	@Override
	public boolean accpetValidator() {
		if ((item.getExtensionName().indexOf("Cell") > -1) || item.getExtensionName().equals("LevelView")) { //$NON-NLS-1$
			return true;
		}

		return false;
	}

}
