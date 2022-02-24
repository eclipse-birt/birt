/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.extensions;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * ReportItemViewAdapter
 */
public abstract class ReportItemViewAdapter implements IReportItemViewProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemViewProvider#
	 * createView(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public DesignElementHandle createView(DesignElementHandle host) throws BirtException {
		return null;
	}

}
