
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

/**
 *
 */

public class EmptySelection implements ISelection {

	@Override
	public Object[] getMax() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getMin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSelected(Object[] key) {
		return false;
	}

}
