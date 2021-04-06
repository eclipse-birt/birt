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