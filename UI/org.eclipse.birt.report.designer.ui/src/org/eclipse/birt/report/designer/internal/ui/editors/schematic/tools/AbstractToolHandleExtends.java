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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Abstrct creation tool extension.
 * 
 */
public abstract class AbstractToolHandleExtends {

	private CreateRequest request = null;

	private EditPart part;

	private Object model;

	public abstract boolean preHandleMouseDown();

	/**
	 * Process after creation
	 * 
	 * @deprecated tentative solution for bugzilla#145284, will be refactored later.
	 */
	public boolean postHandleCreation() {
		// doing nothing by default
		return true;
	}

	/**
	 * Process before mouse up.
	 */
	public boolean preHandleMouseUp() {
		if (model != null) {
			getRequest().getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, model);
			return true;
		}

		return false;

	}

	/**
	 * @return Returns the request.
	 */
	public CreateRequest getRequest() {
		return request;
	}

	/**
	 * @param request The request to set.
	 */
	public void setRequest(CreateRequest request) {
		this.request = request;
	}

	/**
	 * Set target edit part.
	 * 
	 * @param part
	 */
	public void setTargetEditPart(EditPart part) {
		this.part = part;
	}

	/**
	 * @return target edit part
	 */
	public EditPart getTargetEditPart() {
		return part;
	}

	/**
	 * Set model.
	 * 
	 * @param obj
	 */
	protected void setModel(Object obj) {
		model = obj;
	}

	protected Object getModel() {
		return this.model;
	}
}