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
 * This interface is used to create alternavite reportitem view for multi-view
 * host.
 */
public interface IReportItemViewProvider {

	/**
	 * Returns the name of this view
	 */
	String getViewName();

	/**
	 * Creates view upon given multiview host
	 */
	DesignElementHandle createView(DesignElementHandle host) throws BirtException;
}
