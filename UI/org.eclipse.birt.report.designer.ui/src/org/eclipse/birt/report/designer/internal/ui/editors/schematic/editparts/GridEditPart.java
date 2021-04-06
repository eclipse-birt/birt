/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.core.model.schematic.GridHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.nls.Messages;

/**
 * Grid EditPart,control the UI & model of grid
 */
public class GridEditPart extends TableEditPart {

	private static final String GUIDEHANDLE_TEXT = Messages.getString("GridEditPart.GUIDEHANDLE_TEXT"); //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param obj
	 */
	public GridEditPart(Object obj) {
		super(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .TableEditPart#getGuideLabel()
	 */
	public String getGuideLabel() {
		return GUIDEHANDLE_TEXT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner#
	 * getDefinedHeight()
	 */
	public String getDefinedHeight() {
		GridHandleAdapter tadp = HandleAdapterFactory.getInstance().getGridHandleAdapter(getModel());
		return tadp.getDefinedHeight();
	}

}