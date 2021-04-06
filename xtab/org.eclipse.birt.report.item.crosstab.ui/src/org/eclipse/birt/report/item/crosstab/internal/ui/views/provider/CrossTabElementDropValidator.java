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

	public boolean canDrop() {
		if (item.getExtensionName().indexOf("Cell") > -1) //$NON-NLS-1$
		{
			boolean canDrop = item.getContents(DEUtil.getDefaultContentName(item)).size() > 0 && item.canDrop();
			return canDrop;
		}
		if (item.getExtensionName().equals("LevelView")) //$NON-NLS-1$
			return false;

		return true;
	}

	public boolean accpetValidator() {
		if (item.getExtensionName().indexOf("Cell") > -1) //$NON-NLS-1$
			return true;

		if (item.getExtensionName().equals("LevelView")) //$NON-NLS-1$
			return true;

		return false;
	}

}
