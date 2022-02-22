/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
		types = new ArrayList<>();
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
