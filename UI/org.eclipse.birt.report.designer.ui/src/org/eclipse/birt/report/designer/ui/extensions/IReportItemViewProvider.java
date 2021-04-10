/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
