/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.commands;

import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * A Command to set the Constraints for a TableNodeEditPart
 * 
 */
public class SetConstraintCommand extends org.eclipse.gef.commands.Command {

	private Point newPos;

	private Dimension newSize;

	private Object module;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		if (module == null || id == null)
			return;

		if (module instanceof ModuleHandle) {
			ModuleHandle module = (ModuleHandle) this.module;
			try {
				UIHelper.setIntProperty(module, id, BuilderConstants.POSITION_X, newPos.x);
				UIHelper.setIntProperty(module, id, BuilderConstants.POSITION_Y, newPos.y);
				UIHelper.setIntProperty(module, id, BuilderConstants.SIZE_WIDTH, newSize.width);
				UIHelper.setIntProperty(module, id, BuilderConstants.SIZE_HEIGHT, newSize.height);
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	public void setLocation(Rectangle r) {
		setLocation(r.getLocation());
		setSize(r.getSize());
	}

	/**
	 * @param dimension
	 */
	private void setSize(Dimension dimension) {
		newSize = dimension;
	}

	/**
	 * Sets the Location of the element
	 * 
	 * @param p
	 */
	public void setLocation(Point p) {
		newPos = p;
	}

	/**
	 * Sets the Edit Part for this Event
	 * 
	 * @param part The Editr Part to be Set
	 */
	public void setModuleHandle(Object module) {
		this.module = module;
	}

	private String id;

	public void setId(String id) {
		this.id = id;
	}

}