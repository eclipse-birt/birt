/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.interfaces;

/**
 * The class is used to wrap a control to support field assist function.
 */

public interface IAssistField {

	/**
	 * Set contents to the field.
	 *
	 * @param values
	 */
	void setContent(String[] values);

	/**
	 * Set contents.
	 *
	 * @param contents
	 */
	void setContents(String contents);

	/**
	 * Returns contents.
	 *
	 * @return
	 */
	String getContents();

}
