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
	public int getRowID();

	public void setRowID(int rowID);

	public ITableContent getTable();

	public String getGroupId();

	public void setGroupId(String groupId);

	public IGroupContent getGroup();

	public IBandContent getBand();

	public void setRepeatable(boolean repeatable);

	public boolean isRepeatable();

}
