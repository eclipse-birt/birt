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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.editors.IReportProviderFactory;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * 
 */

public class ReportProviderFactoryAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IReportProviderFactory.class) {
			return new IDEReportProviderFactory();
		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { IReportProvider.class };
	}

}
