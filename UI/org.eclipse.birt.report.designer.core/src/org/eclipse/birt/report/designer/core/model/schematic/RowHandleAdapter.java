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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement RowHandleAdapter responds to model RowHandle
 */
public class RowHandleAdapter extends DesignElementHandleAdapter {

	private static final String TABLE_GROUPFOOTER = Messages.getString("RowHandleAdapter.table.groupFooter"); //$NON-NLS-1$

	private static final String TABLE_GROUPHEADER = Messages.getString("RowHandleAdapter.table.groupHeader"); //$NON-NLS-1$

	private static final String TABLE_DETAIL = Messages.getString("RowHandleAdapter.table.detail"); //$NON-NLS-1$

	private static final String TABLE_FOOTER = Messages.getString("RowHandleAdapter.table.footer"); //$NON-NLS-1$

	private static final String TABLE_HEADER = Messages.getString("RowHandleAdapter.table.header"); //$NON-NLS-1$

	static final int DEFAULT_HEIGHT = 23;

	public static final int DEFAULT_MINHEIGHT = 23;

	/**
	 * Constructor
	 * 
	 * @param row <code>RowHandle</code>
	 */
	public RowHandleAdapter(RowHandle row) {
		this(row, null);
	}

	/**
	 * Constructor
	 * 
	 * @param row  <code>RowHandle</code>
	 * @param mark Helper mark
	 * 
	 */
	public RowHandleAdapter(RowHandle row, IModelAdapterHelper mark) {
		super(row, mark);
	}

	/**
	 * Gets the height
	 * 
	 * @return
	 */
	public int getHeight() {
		DimensionHandle handle = getRowHandle().getHeight();

		int px = (int) DEUtil.convertoToPixel(handle);
		if (px <= 0) {
			px = DEFAULT_HEIGHT;
		}
		return px;
	}

	/**
	 * If the user define the row height
	 * 
	 * @return
	 */
	public boolean isCustomHeight() {
		DimensionHandle handle = getRowHandle().getHeight();

		return handle.getMeasure() > 0;
	}

	/**
	 * Gets the row number
	 * 
	 * @return
	 */
	public int getRowNumber() {
		TableHandleAdapter adapter = HandleAdapterFactory.getInstance().getTableHandleAdapter(getTableParent());
		return adapter.getRows().indexOf(getRowHandle()) + 1;
	}

	/**
	 * Gets the minimum height
	 * 
	 * @return
	 */
	public int getMinHeight() {
		return DEFAULT_MINHEIGHT;
	}

	/**
	 * Get RowHandle
	 * 
	 * @return <code>RowHanle</code>
	 */
	private RowHandle getRowHandle() {
		return (RowHandle) getHandle();
	}

	/**
	 * Gets the Children list.
	 * 
	 * @return Children iterator
	 */
	public List getChildren() {
		return getRowHandle().getCells().getContents();
	}

	/**
	 * @return display name
	 */
	public String getDisplayName() {
		TableHandleAdapter adapt = HandleAdapterFactory.getInstance().getTableHandleAdapter(getTableParent());
		TableHandleAdapter.RowUIInfomation info = adapt.getRowInfo(getHandle());
		return info.getRowDisplayName();
	}

	/**
	 * @return type
	 */
	public String getType() {
		TableHandleAdapter adapt = HandleAdapterFactory.getInstance().getTableHandleAdapter(getTableParent());
		TableHandleAdapter.RowUIInfomation info = adapt.getRowInfo(getHandle());
		return info.getType();
	}

	/**
	 * @return type string
	 */
	public String getTypeString() {
		TableHandleAdapter adapt = HandleAdapterFactory.getInstance().getTableHandleAdapter(getTableParent());
		TableHandleAdapter.RowUIInfomation info = adapt.getRowInfo(getHandle());
		String tp = info.getType();

		if (TableHandleAdapter.TABLE_HEADER.equals(tp)) {
			return TABLE_HEADER;
		}

		if (TableHandleAdapter.TABLE_FOOTER.equals(tp)) {
			return TABLE_FOOTER;
		}

		if (TableHandleAdapter.TABLE_DETAIL.equals(tp)) {
			return TABLE_DETAIL;
		}

		if (TableHandleAdapter.TABLE_GROUP_HEADER.equals(tp)) {
			return TABLE_GROUPHEADER;
		}

		if (TableHandleAdapter.TABLE_GROUP_FOOTER.equals(tp)) {
			return TABLE_GROUPFOOTER;
		}

		return null;
	}

	/**
	 * @return parent slot id.
	 */
	public int getParentSlotId() {
		return -1;
	}

	/**
	 * copy a row
	 * 
	 * @returnSemanticException
	 */
	public Object copy() throws SemanticException {
		SlotHandle slotHandle = getRowHandle().getContainerSlotHandle();

		RowHandle retValue = slotHandle.getElementHandle().getElementFactory().newTableRow();

		Iterator iter = getRowHandle().getPropertyIterator();
		while (iter.hasNext()) {
			PropertyHandle handle = (PropertyHandle) iter.next();
			String key = handle.getDefn().getName();
			if (handle.isLocal()) {
				// retValue.setProperty( key, getRowHandle( ).getProperty( key )
				// );
				getRowHandle().copyPropertyTo(key, retValue);
			}
		}

		return retValue;
	}

	/**
	 * @return parent of row
	 */
	public Object getTableParent() {
		DesignElementHandle element = getRowHandle().getContainer();
		if (element instanceof TableGroupHandle) {
			element = element.getContainer();
		}
		return element;
	}

	/**
	 * Set row height
	 * 
	 * @param rowHeight
	 * @throws SemanticException
	 */
	public void setHeight(int rowHeight, String units) throws SemanticException {
		MetricUtility.updateDimension(getRowHandle().getHeight(), rowHeight, units);
	}

	public void setHeight(int rowHeight) throws SemanticException {
		MetricUtility.updateDimension(getRowHandle().getHeight(), rowHeight);
	}
}