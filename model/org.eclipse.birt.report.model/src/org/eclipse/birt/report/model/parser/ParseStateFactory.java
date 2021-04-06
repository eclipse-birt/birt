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
