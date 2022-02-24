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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.dialogs.IBindingDialogHelper;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * 
 */

public class CrosstabBindingDialogFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IBindingDialogHelper.class && adaptableObject instanceof ExtendedItemHandle
				&& ((ExtendedItemHandle) adaptableObject).getDefn().getName().equals("Crosstab")) //$NON-NLS-1$
		{
			return new CrosstabBindingDialogHelper();
		}
		return null;
	}

	public Class[] getAdapterList() {
		// TODO Auto-generated method stub
		return null;
	}

}
