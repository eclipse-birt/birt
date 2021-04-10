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