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
	@Override
	public String getGuideLabel() {
		return GUIDEHANDLE_TEXT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner#
	 * getDefinedHeight()
	 */
	@Override
	public String getDefinedHeight() {
		GridHandleAdapter tadp = HandleAdapterFactory.getInstance().getGridHandleAdapter(getModel());
		return tadp.getDefinedHeight();
	}

}
