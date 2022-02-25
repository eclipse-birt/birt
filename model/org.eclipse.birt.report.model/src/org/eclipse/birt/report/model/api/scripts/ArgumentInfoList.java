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

package org.eclipse.birt.report.model.api.scripts;

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

	private List arguments = null;

	/**
	 * Constructor.
	 *
	 * @param params the parameters for the method
	 */

	protected ArgumentInfoList(Class[] params) {
		initialize(params);

	}

	/**
	 * @param params
	 */

	private void initialize(Class[] params) {
		for (int i = 0; i < params.length; i++) {
			ArgumentInfo argument = new ArgumentInfo(params[i]);
			if (arguments == null) {
				arguments = new ArrayList();
			}
			arguments.add(argument);
		}
	}

	/**
	 * Returns the argument definition given the name.
	 *
	 * @param argumentName name of the argument to get
	 * @return the argument definition with the specified name.
	 */

	@Override
	public IArgumentInfo getArgument(String argumentName) {
		if (arguments == null) {
			return null;
		}

		for (Iterator iter = ((ArrayList) arguments).iterator(); iter.hasNext();) {
			IArgumentInfo argument = (ArgumentInfo) iter.next();

			if (argument.getName().equalsIgnoreCase(argumentName)) {
				return argument;
			}
		}

		return null;
	}

	/**
	 * Returns the iterator of argument definition. Each one is a list that contains
	 * <code>IArgumentInfo</code>.
	 *
	 * @return iterator of argument definition.
	 */

	@Override
	public Iterator argumentsIterator() {
		if (arguments == null) {
			return Collections.EMPTY_LIST.iterator();
		}

		return arguments.iterator();
	}
}
