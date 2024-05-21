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

package org.eclipse.birt.report.engine.content;

/**
 * Provides the interfaces for the Row AbstractContent
 *
 */
public interface IRowContent extends IContainerContent {

	/**
	 * Get the row id
	 *
	 * @return the row id
	 */
	int getRowID();

	/**
	 * Set the row id
	 *
	 * @param rowID row id
	 */
	void setRowID(int rowID);

	/**
	 * Get the table
	 *
	 * @return the table
	 */
	ITableContent getTable();

	/**
	 * Get the group id
	 *
	 * @return the group id
	 */
	String getGroupId();

	/**
	 * Set the group id
	 *
	 * @param groupId group id
	 */
	void setGroupId(String groupId);

	/**
	 * Get the group
	 *
	 * @return the group
	 */
	IGroupContent getGroup();

	/**
	 * Get the band
	 *
	 * @return the band
	 */
	IBandContent getBand();

	/**
	 * Set repeatable
	 *
	 * @param repeatable is repeatable
	 */
	void setRepeatable(boolean repeatable);

	/**
	 * Is repeatable
	 *
	 * @return is repeatable
	 */
	boolean isRepeatable();

}
