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
 *
 */
public interface IRowContent extends IContainerContent {
	int getRowID();

	void setRowID(int rowID);

	ITableContent getTable();

	String getGroupId();

	void setGroupId(String groupId);

	IGroupContent getGroup();

	IBandContent getBand();

	void setRepeatable(boolean repeatable);

	boolean isRepeatable();

}
