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

package org.eclipse.birt.report.model.api.simpleapi;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.ExpressionType;

/**
 *
 */

public interface IExpressionType {

	/**
	 * 
	 */

	public static final String CONSTANT = ExpressionType.CONSTANT;

	/**
	 * 
	 */

	public static final String JAVASCRIPT = ExpressionType.JAVASCRIPT;

	/**
	 * Gets possible types for the expression.
	 * 
	 * @return the iterator
	 */

	public Iterator<String> iterator();
}
