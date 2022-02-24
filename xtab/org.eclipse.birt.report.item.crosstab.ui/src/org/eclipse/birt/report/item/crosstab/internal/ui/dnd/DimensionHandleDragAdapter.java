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

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDragAdapter;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;

/**
 * 
 */

public class DimensionHandleDragAdapter implements IDragAdapter {

	public int canDrag(Object object) {
		if (object instanceof DimensionHandle)
			return DNDService.LOGIC_TRUE;
		return DNDService.LOGIC_UNKNOW;
	}

	public Object getDragTransfer(Object transfer) {
		if (transfer instanceof DimensionHandle)
			return transfer;
		return null;
	}

}
