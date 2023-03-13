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

package org.eclipse.birt.report.designer.core.model.schematic;

import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement AutoTextHandleAdapter responds to model
 * AutoTextHandle
 */

public class AutoTextHandleAdapter extends LabelHandleAdapter {

	/**
	 * Constructor
	 *
	 * @param labelHandle
	 * @param mark
	 */
	public AutoTextHandleAdapter(ReportItemHandle labelHandle, IModelAdapterHelper mark) {
		super(labelHandle, mark);
	}
}
