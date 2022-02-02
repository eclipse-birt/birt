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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * MultipleAdapter
 */

public class MultipleAdapter extends ReportItemtHandleAdapter {

	private static final String SET_VIEW = Messages.getString("MultipleAdapter.SetCurrentView"); //$NON-NLS-1$
	private static final String REMOVE_VIEW = Messages.getString("MultipleAdapter.RemoveView");//$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param mark
	 */
	public MultipleAdapter(ReportItemHandle handle, IModelAdapterHelper mark) {
		super(handle, mark);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter#
	 * getChildren()
	 */
	public List getChildren() {
		ReportItemHandle handle = getReportItemHandle();
		DesignElementHandle viewHnadle = handle.getCurrentView();
		List list = new ArrayList();
		if (viewHnadle != null) {
			list.add(viewHnadle);
		} else {
			list.add(handle);
		}
		return list;
	}

	/**
	 * Gets the view from the ReportItemHandle.
	 * 
	 * @return
	 */
	public List getViews() {
		ReportItemHandle handle = getReportItemHandle();
		return handle.getViews();
	}

	/**
	 * Gets the current view from the ReportItemHandle
	 * 
	 * @return
	 */
	public DesignElementHandle getCurrentView() {
		ReportItemHandle handle = getReportItemHandle();
		return handle.getCurrentView();
	}

	/**
	 * Sets the current view, if number is 0 that means set current view as null.
	 * 
	 * @param number
	 */
	public void setCurrentView(int number) {
		try {
			// if the number is 0, set current view
			if (number == 0 && getCurrentView() == null) {
				return;
			}
			int position = number - 1;
			List list = getViews();
			if (position < 0 && position > list.size() - 1) {
				return;
			}
			transStar(SET_VIEW);
			ReportItemHandle handle = getReportItemHandle();
			if (number == 0 && getCurrentView() != null) {
				handle.setCurrentView(null);
				transEnd();
				return;
			}
			handle.setCurrentView((DesignElementHandle) list.get(position));
			transEnd();

		} catch (SemanticException e) {
			rollBack();
			// TODO log
		}
	}

	/**
	 * Remove the view from the ReportItemHandle.The number isn't less than 1.
	 * 
	 * @param number
	 */
	public void removeView(int number) {
		if (number < 1) {
			return;
		}
		int position = number - 1;
		List list = getViews();
		if (position < 0 && position > list.size() - 1) {
			return;
		}

		transStar(REMOVE_VIEW);
		ReportItemHandle handle = getReportItemHandle();
		try {
			handle.dropView((DesignElementHandle) list.get(position));
			transEnd();
		} catch (SemanticException e) {
			rollBack();
		}
	}

	/**
	 * Gets the current view number, if the current view is null, return 0;
	 * 
	 * @return
	 */
	public int getCurrentViewNumber() {
		ReportItemHandle handle = getReportItemHandle();
		DesignElementHandle current = handle.getCurrentView();
		if (current == null || !getViews().contains(handle.getCurrentView())) {
			return 0;
		}
		return getViews().indexOf(handle.getCurrentView()) + 1;
	}
}
