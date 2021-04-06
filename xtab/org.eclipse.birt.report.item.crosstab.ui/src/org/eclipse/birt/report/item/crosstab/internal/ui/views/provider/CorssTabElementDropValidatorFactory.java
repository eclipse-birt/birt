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
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * 
 */

public class CorssTabElementDropValidatorFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IDropValidator.class) {
			if (adaptableObject instanceof ExtendedItemHandle) {
				ExtendedItemHandle item = (ExtendedItemHandle) adaptableObject;
				return new CrossTabElementDropValidator(item);
			}
		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { IDropValidator.class };
	}

}
