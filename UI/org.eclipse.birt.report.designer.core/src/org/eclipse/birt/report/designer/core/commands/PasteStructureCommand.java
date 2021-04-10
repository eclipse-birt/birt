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

package org.eclipse.birt.report.designer.core.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.gef.commands.Command;

/**
 * Paste structure to container.
 */

public class PasteStructureCommand extends Command {
	protected static final Logger logger = Logger.getLogger(PasteStructureCommand.class.getName());
	private IStructure copyData;
	private Object container;

	public PasteStructureCommand(IStructure copyData, Object container) {
		this.copyData = copyData;
		this.container = container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return DNDUtil.handleValidateTargetCanContain(container, copyData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		if (container instanceof EmbeddedImageNode) {
			container = ((EmbeddedImageNode) container).getReportDesignHandle();
		}
		try {
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("PasteStructureCommand >>  Starts. Source: " //$NON-NLS-1$
						+ copyData.getStructName() + ",Target: " //$NON-NLS-1$
						+ DEUtil.getDisplayLabel(container));
			}
			EmbeddedImage image = (EmbeddedImage) copyData.copy();
			((ModuleHandle) container).rename(image);
			((ModuleHandle) container).addImage(image);
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("PasteStructureCommand >>  Finished"); //$NON-NLS-1$
			}
		} catch (SemanticException e) {
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("PasteStructureCommand >>  Failed"); //$NON-NLS-1$
			}
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}