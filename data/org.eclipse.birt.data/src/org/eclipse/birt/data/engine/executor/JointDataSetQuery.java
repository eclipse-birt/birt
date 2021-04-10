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
