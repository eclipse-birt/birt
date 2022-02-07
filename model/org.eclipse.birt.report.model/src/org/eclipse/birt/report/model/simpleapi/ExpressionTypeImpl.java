/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
