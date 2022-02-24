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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;

/**
 * Represents an optional argument list of a method.
 * 
 */

public class ArgumentInfoList implements IArgumentInfoList {

	/**
	 * The list contains a set of arguments.
	 */

	private List<IArgumentInfo> arguments = null;

	/**
	 * Constructs a default <code>ArgumentInfoList</code>.
	 */

	public ArgumentInfoList() {
	}

	/**
	 * Adds argument to this method definition.
	 * 
	 * @param argument the argument definition to add
	 * @throws MetaDataException if the argument name exists.
	 */

	public void addArgument(IArgumentInfo argument) throws MetaDataException {
		if (arguments == null)
			arguments = new ArrayList<IArgumentInfo>();

		if (getArgument(argument.getName()) != null) {
			throw new MetaDataException(new String[] { null, argument.getName() },
					MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ARGUMENT_NAME);
		}
		arguments.add(argument);
	}

	/**
	 * Returns the argument definition given the name.
	 * 
	 * @param argumentName name of the argument to get
	 * @return the argument definition with the specified name.
	 */

	public IArgumentInfo getArgument(String argumentName) {
		if (arguments == null)
			return null;

		for (Iterator<IArgumentInfo> iter = arguments.iterator(); iter.hasNext();) {
			IArgumentInfo argument = iter.next();

			if (argument.getName().equalsIgnoreCase(argumentName))
				return argument;
		}

		return null;
	}

	/**
	 * Returns the iterator of argument definition. Each one is a list that contains
	 * <code>ArgumentInfo</code>.
	 * 
	 * @return iterator of argument definition.
	 */

	public Iterator<IArgumentInfo> argumentsIterator() {
		if (arguments == null)
			return Collections.EMPTY_LIST.iterator();

		return arguments.iterator();
	}
}
