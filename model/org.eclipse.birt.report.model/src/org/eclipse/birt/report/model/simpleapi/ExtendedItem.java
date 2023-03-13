/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.simpleapi.IExtendedItem;

/**
 *
 */

public class ExtendedItem extends ReportItem implements IExtendedItem {

	/**
	 * Constructor.
	 *
	 * @param handle
	 */

	public ExtendedItem(ReportItemHandle handle) {
		super(handle);
	}

}
