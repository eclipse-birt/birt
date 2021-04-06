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

package org.eclipse.birt.report.model.api.metadata;

import java.util.Iterator;

/**
 * Represents an optional argument list of a method.
 */

public interface IArgumentInfoList {

	/**
	 * Returns the argument definition given the name.
	 * 
	 * @param argumentName name of the argument to get
	 * @return the argument definition with the specified name.
	 */

	public IArgumentInfo getArgument(String argumentName);

	/**
	 * Returns the iterator of argument definition. Each one is a list that contains
	 * <code>IArgumentInfo</code>.
	 * 
	 * @return iterator of argument definition.
	 */

	public Iterator<IArgumentInfo> argumentsIterator();

}
