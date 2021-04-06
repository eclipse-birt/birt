/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.parameters;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * ReportHyperlinkParameterProviderFactory
 */
public class ReportHyperlinkParameterProviderFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IHyperlinkParameterProvider.class.equals(adapterType)) {
			if (adaptableObject instanceof ReportDesignHandle) {
				return new ReportHyperlinkParameterProvider((ReportDesignHandle) adaptableObject);
			}
		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { IHyperlinkParameterProvider.class };
	}

}
