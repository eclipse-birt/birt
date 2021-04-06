/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;

/**
 * CrosstabArgumentInfoList
 */
public class CrosstabArgumentInfoList implements IArgumentInfoList {

	private List<IArgumentInfo> arguments;

	CrosstabArgumentInfoList(Class<?>[] paramTypes, String[] paramNames) {
		for (int i = 0; i < paramTypes.length; i++) {
			CrosstabArgumentInfo argument = new CrosstabArgumentInfo(paramTypes[i], paramNames[i]);
			if (arguments == null) {
				arguments = new ArrayList<IArgumentInfo>();
			}
			arguments.add(argument);
		}
	}

	public Iterator<IArgumentInfo> argumentsIterator() {
		if (arguments == null) {
			return Collections.EMPTY_LIST.iterator();
		}

		return arguments.iterator();
	}

	public IArgumentInfo getArgument(String argumentName) {
		if (arguments == null) {
			return null;
		}

		for (Iterator<IArgumentInfo> itr = arguments.iterator(); itr.hasNext();) {
			IArgumentInfo argument = itr.next();

			if (argument.getName().equalsIgnoreCase(argumentName)) {
				return argument;
			}
		}

		return null;
	}
}
