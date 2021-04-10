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

package org.eclipse.birt.report.designer.ui.preview.parameter.node;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Parameter node interface.
 * 
 */

public interface IParamNode {

	/**
	 * Gets value list. Only used for parameter.
	 * 
	 * @return value list. each item is <code>String</code>
	 */

	public List getValueList();

	/**
	 * Format input string.
	 * 
	 * @param input
	 * @return formatted value.
	 */

	public String format(String input) throws BirtException;

	/**
	 * Gets children of parameter group node.
	 * 
	 * @return children of node.each item is <code>IParamNode</code>
	 */

	public List getChildren();

}
