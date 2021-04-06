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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;

/**
 * 
 */

public class CrosstabHeaderHandleAdapter extends TotalCrosstabCellHandleAdapter {

	/**
	 * ID for the crosstab header
	 */
	public static final int CROSSTAB_HEADER = GRAND_TOTAL + 1;

	/**
	 * @param handle
	 * @param type
	 */
	public CrosstabHeaderHandleAdapter(CrosstabCellHandle handle, int type) {
		super(handle, type);
	}
}
