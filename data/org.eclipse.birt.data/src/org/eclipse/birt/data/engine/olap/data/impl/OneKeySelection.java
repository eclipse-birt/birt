/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl;

import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;

/**
 *
 */
public class OneKeySelection implements ISelection {

	Object[] keyValue = null;

	/**
	 * @return the keyValue
	 */
	public Object[] getKeyValue() {
		return keyValue;
	}

	public OneKeySelection(Object[] keyValue) {
		this.keyValue = keyValue;
	}

	@Override
	public Object[] getMax() {
		return keyValue;
	}

	@Override
	public Object[] getMin() {
		return keyValue;
	}

	@Override
	public boolean isSelected(Object[] obj) {
		if (CompareUtil.compare(keyValue, obj) == 0) {
			return true;
		} else {
			return false;
		}
	}

}
