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
package org.eclipse.birt.data.engine.executor;

import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * 
 */
public class JointDataSetQuery extends BaseQuery {
	//
	private IResultClass meta;

	/**
	 * Constructor
	 * 
	 * @param resultClass
	 */
	public JointDataSetQuery(IResultClass resultClass) {
		meta = resultClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IQuery#close()
	 */
	public void close() {

	}

	/**
	 * Return the result class of this joint data set.
	 * 
	 * @return
	 */
	public IResultClass getResultClass() {
		return meta;
	}

}
