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

package org.eclipse.birt.report.designer.internal.ui.views.outline;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Applies visitor to the report element and the children element
 * 
 */

public class ListenerElementVisitor extends DesignVisitor {

	/**
	 * The listener
	 */

	private Listener listener;

	private boolean install = true;

	/**
	 * constructor. Sets the listener and design
	 * 
	 * @param listener the listener value to be set
	 */

	public ListenerElementVisitor(Listener listener) {
		super();
		this.listener = listener;
	}

	public void addListener(DesignElementHandle handle) {
		install = true;
		apply(handle);
	}

	public void removeListener(DesignElementHandle handle) {
		install = false;
		apply(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.model.api.DesignVisitor#visitDesignElement(org.eclipse.birt.
	 * model.api.DesignElementHandle)
	 */
	public void visitDesignElement(DesignElementHandle obj) {
		if (install) {
			obj.addListener(listener);
			if (obj instanceof ReportItemHandle) {
				addViewsListener((ReportItemHandle) obj);
			}
		} else {
			obj.removeListener(listener);
			if (obj instanceof ReportItemHandle) {
				removeViewsListener((ReportItemHandle) obj);
			}
		}
		for (int i = 0; i < obj.getDefn().getSlotCount(); i++) {
			visitContents(obj.getSlot(i));
		}
		for (int i = 0; i < obj.getDefn().getContents().size(); i++) {
			visitContents(obj, ((PropertyDefn) obj.getDefn().getContents().get(i)).getName());
		}
	}

	private void addViewsListener(ReportItemHandle handle) {
		List views = handle.getViews();
		for (int i = 0; i < views.size(); i++) {
			((DesignElementHandle) views.get(i)).addListener(listener);
		}
	}

	private void removeViewsListener(ReportItemHandle handle) {
		List views = handle.getViews();
		for (int i = 0; i < views.size(); i++) {
			((DesignElementHandle) views.get(i)).removeListener(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.DesignVisitor#visitReportDesign(org.eclipse
	 * .birt.report.model.api.ReportDesignHandle)
	 */
	protected void visitModule(ModuleHandle obj) {
		if (listener instanceof IValidationListener) {
			IValidationListener vl = (IValidationListener) listener;
			if (install) {
				obj.addValidationListener(vl);
			} else {
				obj.removeValidationListener(vl);
			}
		}
		super.visitModule(obj);
	}

	/**
	 * Sets the listener null.
	 */

	public void dispose() {
		listener = null;
	}
}
