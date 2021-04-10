/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.aggregation.impl;

import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;

/**
 * 
 */

public abstract class AggrFunction implements IAggrFunction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDefaultValue()
	 */
	public Object getDefaultValue() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getNumberOfPasses(
	 * )
	 */
	public int getNumberOfPasses() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#
	 * isDataOrderSensitive()
	 */
	public boolean isDataOrderSensitive() {
		return false;
	}
}
