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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;

/**
 * 
 * the row content object which contains cell content objects
 * 
 */
public class RowContentWrapper extends AbstractContentWrapper implements IRowContent {

	IRowContent rowContent;

	/**
	 * constructor
	 * 
	 * @param row the row deign
	 */
	public RowContentWrapper(IRowContent content) {
		super(content);
		rowContent = content;
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitRow(this, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IRowContent#getRowID()
	 */
	public int getRowID() {
		return rowContent.getRowID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IRowContent#setRowID(int)
	 */
	public void setRowID(int rowID) {
		rowContent.setRowID(rowID);
	}

	public ITableContent getTable() {
		return null;
	}

	public IBandContent getBand() {
		return rowContent.getBand();
	}

	public IGroupContent getGroup() {
		return rowContent.getGroup();
	}

	public String getGroupId() {
		return rowContent.getGroupId();
	}

	public void setGroupId(String groupId) {
		rowContent.setGroupId(groupId);
	}

	public void setRepeatable(boolean repeatable) {
		rowContent.setRepeatable(repeatable);

	}

	public boolean isRepeatable() {
		return rowContent.isRepeatable();
	}
}