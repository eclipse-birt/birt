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

	public int validate(Object transfer, Object target) {
		if (transfer instanceof Object[]) {
			if (((Object[]) transfer).length > 0) {
				transfer = ((Object[]) transfer)[0];
			} else {
				return RESULT_UNKNOW;
			}
		} else if (transfer instanceof StructuredSelection) {
			if ((transfer = ((StructuredSelection) transfer).getFirstElement()) == null) {
				return RESULT_UNKNOW;
			}
		}

		if (isSubClass(transfer.getClass(), transferClass) && isSubClass(target.getClass(), targetClass)) {
			return canDrop ? RESULT_YES : RESULT_NO;
		}

		return RESULT_UNKNOW;
	}

	private boolean isSubClass(Class subClazz, Class clazz) {
		if (subClazz == clazz)
			return true;
		if (subClazz == Object.class) {
			return false;
		} else {
			return isSubClass(subClazz.getSuperclass(), clazz);
		}
	}
}
