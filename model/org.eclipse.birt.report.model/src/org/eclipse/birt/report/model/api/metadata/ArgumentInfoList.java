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

import org.eclipse.birt.report.model.metadata.MetaDataException;

/**
 * Represents an optional argument list of a method.
 * 
 */

public class ArgumentInfoList implements IArgumentInfoList {

	private final IArgumentInfoList arguInfoList;

	/**
	 * Constructs a default <code>ArgumentInfoList</code>.
	 */

	public ArgumentInfoList() {
		arguInfoList = new org.eclipse.birt.report.model.metadata.ArgumentInfoList();
	}

	/**
	 * Adds argument to this method definition.
	 * 
	 * @param argument the argument definition to add
	 * @throws MetaDataException if the argument name exists.
	 */

	protected void addArgument(IArgumentInfo argument) {
		try {
			((org.eclipse.birt.report.model.metadata.ArgumentInfoList) arguInfoList).addArgument(argument);
		} catch (MetaDataException e) {
			return;
		}
	}

	/**
	 * Returns the argument definition given the name.
	 * 
	 * @param argumentName name of the argument to get
	 * @return the argument definition with the specified name.
	 */

	public IArgumentInfo getArgument(String argumentName) {
		return arguInfoList.getArgument(argumentName);
	}

	/**
	 * Returns the iterator of argument definition. Each one is a list that contains
	 * <code>ArgumentInfo</code>.
	 * 
	 * @return iterator of argument definition.
	 */

	public Iterator<IArgumentInfo> argumentsIterator() {
		return arguInfoList.argumentsIterator();
	}
}