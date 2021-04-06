/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Corner Tracker
 */
public class CornerTracker extends TableSelectionGuideTracker {

	/**
	 * Constructor
	 * 
	 * @param sourceEditPart
	 */
	public CornerTracker(TableEditPart sourceEditPart) {
		super(sourceEditPart, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.
	 * TableSelectionGuideTracker#select()
	 */
	public void select() {
		TableEditPart part = (TableEditPart) getSourceEditPart();
		List list = part.getChildren();
		part.getViewer().setSelection(new StructuredSelection(list));

	}
}