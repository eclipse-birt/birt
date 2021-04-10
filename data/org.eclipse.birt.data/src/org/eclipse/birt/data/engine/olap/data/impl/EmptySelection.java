
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

/**
 * 
 */

public class EmptySelection implements ISelection {

	public Object[] getMax() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] getMin() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSelected(Object[] key) {
		return false;
	}

}
