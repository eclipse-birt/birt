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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.core.runtime.IAdapterFactory;

/**
 * 
 */

public class BindingDialogFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IBindingDialogHelper.class) {
			return new BindingDialogHelper();
		}
		return null;
	}

	public Class[] getAdapterList() {
		// TODO Auto-generated method stub
		return null;
	}

}
