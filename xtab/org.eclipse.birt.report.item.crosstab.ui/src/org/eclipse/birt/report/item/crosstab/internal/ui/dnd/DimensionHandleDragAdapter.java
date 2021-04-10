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
