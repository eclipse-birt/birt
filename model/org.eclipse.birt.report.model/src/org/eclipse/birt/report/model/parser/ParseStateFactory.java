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

package org.eclipse.birt.report.model.parser;

/**
 * Factory class to create a parse state.
 */

public class ParseStateFactory extends ParseStateFactoryImpl {

	private static ParseStateFactory instance = new ParseStateFactory();

	private ParseStateFactory() {

	}

	/**
	 * Returns the singleton instance.
	 * 
	 * @return the instance
	 */

	public static ParseStateFactory getInstance() {
		return instance;
	}

}
