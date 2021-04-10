/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public Object[] getMax() {
		return keyValue;
	}

	public Object[] getMin() {
		return keyValue;
	}

	public boolean isSelected(Object[] obj) {
		if (CompareUtil.compare(keyValue, obj) == 0)
			return true;
		else
			return false;
	}

}
