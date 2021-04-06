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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.gef.commands.Command;

/**
 * This command set a generic property on a model object
 * 
 * 
 */

public class SetPropertyCommand extends Command {
	private static Logger logger = Logger.getLogger(SetPropertyCommand.class.getName());

	private Object model;

	private Map extendsData;

	public SetPropertyCommand(Object model, Map extendsData) {
		this.model = model;
		this.extendsData = extendsData;
	}

	/**
	 * Executes the Command. This method should not be called if the Command is not
	 * executable.
	 */

	public void execute() {
		if (DesignerConstants.TRACING_COMMANDS) {
			System.out.println("SetPropertyCommand >> Starts ..."); //$NON-NLS-1$
		}
		try {
			DesignElementHandle handle = (DesignElementHandle) model;
			List elementProperties = handle.getDefn().getProperties();

			for (Iterator it = elementProperties.iterator(); it.hasNext();) {
				String key = ((IElementPropertyDefn) it.next()).getName();
				Object value = null;
				if ((value = extendsData.get(DEUtil.getGUIPropertyKey(key))) != null) {
					if (DesignerConstants.TRACING_COMMANDS) {
						System.out.println("SetPropertyCommand >>  Target: " //$NON-NLS-1$
								+ DEUtil.getDisplayLabel(handle) + ",Property: " //$NON-NLS-1$
								+ key + ",Value: " //$NON-NLS-1$
								+ value);
					}
					handle.setProperty(key, value);
				}
			}
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("SetPropertyCommand >> Finished"); //$NON-NLS-1$
			}
		} catch (SemanticException e) {
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("SetPropertyCommand >> Failed"); //$NON-NLS-1$
			}
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}