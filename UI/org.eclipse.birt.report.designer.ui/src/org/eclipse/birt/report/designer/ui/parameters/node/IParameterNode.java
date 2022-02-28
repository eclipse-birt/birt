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

package org.eclipse.birt.report.designer.ui.parameters.node;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Parameter node interface.
 *
 */

public interface IParameterNode {

	/**
	 * Gets value list. Only used for parameter.
	 *
	 * @return value list. each item is <code>String</code>
	 */

	List getValueList();

	/**
	 * Format input string.
	 *
	 * @param input
	 * @return formatted value.
	 */

	String format(String input) throws BirtException;

	/**
	 * Gets children of parameter group node.
	 *
	 * @return children of node.each item is <code>IParameterNode</code>
	 */

	List getChildren();

}
