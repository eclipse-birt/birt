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

package org.eclipse.birt.report.model.api.metadata;

import java.util.Iterator;

/**
 * Represents the method information for both class and element. The class
 * includes the argument list, return type, and whether this method is static or
 * constructor,
 */

public interface IMethodInfo extends ILocalizableInfo {

	/**
	 * Returns the iterator of argument definition. Each one is a list that contains
	 * <code>IArgumentInfoList</code>.
	 *
	 * @return iterator of argument definition.
	 */

	Iterator<IArgumentInfoList> argumentListIterator();

	/**
	 * Returns the script type for return.
	 *
	 * @return the script type for return
	 */

	String getReturnType();

	/**
	 * Returns the script type for return.
	 *
	 * @return the script type for return
	 */

	IClassInfo getClassReturnType();

	/**
	 * Returns the resource key for tool tip.
	 *
	 * @return the resource key for tool tip
	 */

	@Override
	String getToolTipKey();

	/**
	 * Returns the display string for the tool tip of this method.
	 *
	 * @return the user-visible, localized display name for the tool tip of this
	 *         method.
	 */

	@Override
	String getToolTip();

	/**
	 * Returns whether this method is constructor.
	 *
	 * @return true, if this method is constructor
	 */

	boolean isConstructor();

	/**
	 * Returns whether this method is static.
	 *
	 * @return true if this method is static
	 */

	boolean isStatic();

	/**
	 * Returns the method javadoc.
	 *
	 * @return the javadoc
	 */

	String getJavaDoc();
}
