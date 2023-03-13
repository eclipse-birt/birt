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

	@Override
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitRow(this, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IRowContent#getRowID()
	 */
	@Override
	public int getRowID() {
		return rowContent.getRowID();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IRowContent#setRowID(int)
	 */
	@Override
	public void setRowID(int rowID) {
		rowContent.setRowID(rowID);
	}

	@Override
	public ITableContent getTable() {
		return null;
	}

	@Override
	public IBandContent getBand() {
		return rowContent.getBand();
	}

	@Override
	public IGroupContent getGroup() {
		return rowContent.getGroup();
	}

	@Override
	public String getGroupId() {
		return rowContent.getGroupId();
	}

	@Override
	public void setGroupId(String groupId) {
		rowContent.setGroupId(groupId);
	}

	@Override
	public void setRepeatable(boolean repeatable) {
		rowContent.setRepeatable(repeatable);

	}

	@Override
	public boolean isRepeatable() {
		return rowContent.isRepeatable();
	}
}
