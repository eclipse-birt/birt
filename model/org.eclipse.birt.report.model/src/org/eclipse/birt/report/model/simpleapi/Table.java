/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IColumn;
import org.eclipse.birt.report.model.api.simpleapi.ITable;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableItemModel;

public class Table extends Listing implements ITable {

	public Table(TableHandle table) {
		super(table);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITable#getColumnCount()
	 */

	public int getColumnCount() {
		return ((TableHandle) handle).getColumnCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#repeatHeader()
	 */

	public boolean repeatHeader() {
		return ((TableHandle) handle).repeatHeader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#setRepeatHeader
	 * (boolean)
	 */

	public void setRepeatHeader(boolean value) throws SemanticException {

		setProperty(IListingElementModel.REPEAT_HEADER_PROP, Boolean.valueOf(value));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getCaption()
	 */

	public String getCaption() {
		return ((TableHandle) handle).getCaption();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#setCaption(java
	 * .lang.String)
	 */

	public void setCaption(String caption) throws SemanticException {
		setProperty(ITableItemModel.CAPTION_PROP, caption);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getCaptionKey()
	 */

	public String getCaptionKey() {
		return ((TableHandle) handle).getCaptionKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#setCaptionKey
	 * (java.lang.String)
	 */

	public void setCaptionKey(String captionKey) throws SemanticException {
		setProperty(ITableItemModel.CAPTION_KEY_PROP, captionKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getColumn(int)
	 */

	public IColumn getColumn(int index) {
		SlotHandle slotHandle = handle.getSlot(ITableItemModel.COLUMN_SLOT);
		ColumnHandle columnHandle = (ColumnHandle) slotHandle.get(index);
		if (columnHandle == null)
			return null;
		IColumn column = new Column(columnHandle);
		return column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.ITable#getSummary()
	 */
	public String getSummary() {
		return ((TableHandle) handle).getSummary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.ITable#setSummary(java.lang
	 * .String)
	 */
	public void setSummary(String summary) throws SemanticException {
		setProperty(ITableItemModel.SUMMARY_PROP, summary);

	}

}
