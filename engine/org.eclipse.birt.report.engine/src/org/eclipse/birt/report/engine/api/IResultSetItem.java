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
package org.eclipse.birt.report.engine.api;

public interface IResultSetItem {

	/**
	 * return the result set name.
	 *
	 * @return the result set name
	 */
	String getResultSetName();

	/**
	 * return the display name from externalization
	 *
	 * @return the display name
	 */
	String getResultSetDisplayName();

	/**
	 * return the result meta data.
	 *
	 * @return the result meta data
	 */
	IResultMetaData getResultMetaData();

}
