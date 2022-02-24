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
import org.eclipse.birt.report.model.api.DataItemHandle;

/**
 * Adapter class to adapt model handle. This adapter provides convenience.
 * methods to GUI requirement DataItemHandleAdapter responds to model
 * DataItemHandle
 * 
 */

public class DataItemHandleAdapter extends LabelHandleAdapter {

	/**
	 * Constructor
	 * 
	 * @param dataItemHandle The data item handle.
	 * @param mark
	 */
	public DataItemHandleAdapter(DataItemHandle dataItemHandle, IModelAdapterHelper mark) {
		super(dataItemHandle, mark);
	}

}
