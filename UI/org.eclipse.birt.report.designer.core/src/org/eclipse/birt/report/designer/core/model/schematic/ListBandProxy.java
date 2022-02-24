/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementDetailHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;

/**
 * List band proxy node. For list band doesn't have corrensponding model
 * element, this proxy provides GUI virtual model.
 * 
 */
public class ListBandProxy {

	private ElementDetailHandle handle;

	private String displayName = ""; //$NON-NLS-1$

	private int type;

	protected static final String LIST_HEADER = Messages.getString("ListBandProxy.list.header"); //$NON-NLS-1$

	protected static final String LIST_FOOTER = Messages.getString("ListBandProxy.list.footer"); //$NON-NLS-1$

	protected static final String LIST_DETAIL = Messages.getString("ListBandProxy.list.detail"); //$NON-NLS-1$

	protected static final String LIST_GROUP_HEADER = Messages.getString("ListBandProxy.list.groupHeader"); //$NON-NLS-1$

	protected static final String LIST_GROUP_FOOTER = Messages.getString("ListBandProxy.list.groupFooter"); //$NON-NLS-1$

	public static final int LIST_HEADER_TYPE = 1;

	public static final int LIST_FOOTER_TYPE = 5;

	public static final int LIST_DETAIL_TYPE = 3;

	public static final int LIST_GROUP_HEADER_TYPE = 2;

	public static final int LIST_GROUP_FOOTER_TYPE = 4;

	/**
	 * constructor
	 * 
	 * @param handle
	 */
	public ListBandProxy(SlotHandle handle) {
		super();
		this.handle = handle;
	}

	public ListBandProxy(PropertyHandle handle) {
		super();
		this.handle = handle;
	}

	/**
	 * constructor
	 * 
	 * @param handle Slot handle
	 * @param name   Given name
	 */
	public ListBandProxy(SlotHandle handle, String name) {
		super();
		this.displayName = name;
		if (displayName.startsWith(LIST_HEADER)) {
			type = LIST_HEADER_TYPE;
		} else if (displayName.startsWith(LIST_FOOTER)) {
			type = LIST_FOOTER_TYPE;
		} else if (displayName.startsWith(LIST_DETAIL)) {
			type = LIST_DETAIL_TYPE;
		} else if (displayName.startsWith(LIST_GROUP_HEADER)) {
			type = LIST_GROUP_HEADER_TYPE;
		} else if (displayName.startsWith(LIST_GROUP_FOOTER)) {
			type = LIST_GROUP_FOOTER_TYPE;
		}

		this.handle = handle;
	}

	/**
	 * Get slot handle which is corresponding model of list band
	 * 
	 * @return slot handle which is corresponding model of list band
	 */
	public ElementDetailHandle getSlotHandle() {
		return handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ListBandProxy) {
			return handle == ((ListBandProxy) obj).handle;
		}
		return obj == handle;
	}

	/**
	 * Gets the Children iterator. This children relationship is determined by GUI
	 * requirement. This is not the model children relationship.
	 * 
	 * @return Children iterator
	 */
	public List getChildren() {
		List list = new ArrayList();
		Iterator iterator = null;
		if (handle instanceof SlotHandle) {
			iterator = ((SlotHandle) handle).iterator();
		} else {
			iterator = ((PropertyHandle) handle).iterator();
		}
		for (Iterator it = iterator; it.hasNext();) {
			// list.add( ( (DesignElementHandle) it.next( ) ).getElement( ) );
			list.add((it.next()));
		}
		return list;
	}

	/**
	 * Get display
	 * 
	 * @return Display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return slot handle id
	 */
	public int getSlotId() {
		if (handle instanceof SlotHandle) {
			return ((SlotHandle) handle).getSlotID();
		}
		return -1;
	}

	/**
	 * Get the parent of slot handle
	 * 
	 * @return the parent of slot handle
	 */
	public DesignElementHandle getElemtHandle() {
		return handle.getElementHandle();
	}

	/**
	 * @return type of list band
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets diaplay name
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return handle.hashCode();
	}
}
