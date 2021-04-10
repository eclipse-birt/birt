/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;

/**
 *
 */

class ExpressionTypeImpl implements IExpressionType {

	private static IExpressionType instance = new ExpressionTypeImpl();

	protected static IExpressionType getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IExpressionType#iterator()
	 */

	public Iterator<String> iterator() {
		return ExpressionType.iterator();
	}

}
