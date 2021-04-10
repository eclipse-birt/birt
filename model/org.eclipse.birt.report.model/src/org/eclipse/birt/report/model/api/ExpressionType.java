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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Enumerates the expression types.
 */

public class ExpressionType {

	/**
	 * 
	 */

	public static final String CONSTANT = "constant"; //$NON-NLS-1$

	/**
	 * 
	 */

	public static final String JAVASCRIPT = "javascript"; //$NON-NLS-1$

	private static List<String> types = null;

	static {
		types = new ArrayList<String>();
		types.add(CONSTANT);
		types.add(JAVASCRIPT);
	}

	/**
	 * Gets possible types for the expression.
	 * 
	 * @return the
	 */

	public static Iterator<String> iterator() {
		return types.iterator();
	}
}
