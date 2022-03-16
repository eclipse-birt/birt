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

package org.eclipse.birt.report.designer.internal.ui.views.outline.dnd;

import org.eclipse.jface.viewers.StructuredSelection;

/**
 *
 */

public class DropTypeConstraint implements IDropConstraint {

	private Class transferClass;
	private Class targetClass;
	private boolean canDrop;

	public DropTypeConstraint(Class transfer, Class target, boolean canDrop) {
		this.transferClass = transfer;
		this.targetClass = target;
		this.canDrop = canDrop;
	}

	@Override
	public int validate(Object transfer, Object target) {
		if (transfer instanceof Object[]) {
			if (((Object[]) transfer).length > 0) {
				transfer = ((Object[]) transfer)[0];
			} else {
				return RESULT_UNKNOW;
			}
		} else if (transfer instanceof StructuredSelection) {
			transfer = ((StructuredSelection) transfer).getFirstElement();
			if (transfer == null) {
				return RESULT_UNKNOW;
			}
		}

		if (isSubClass(transfer.getClass(), transferClass) && isSubClass(target.getClass(), targetClass)) {
			return canDrop ? RESULT_YES : RESULT_NO;
		}

		return RESULT_UNKNOW;
	}

	private boolean isSubClass(Class subClazz, Class clazz) {
		if (subClazz == clazz) {
			return true;
		}
		if (subClazz == Object.class) {
			return false;
		} else {
			return isSubClass(subClazz.getSuperclass(), clazz);
		}
	}
}
