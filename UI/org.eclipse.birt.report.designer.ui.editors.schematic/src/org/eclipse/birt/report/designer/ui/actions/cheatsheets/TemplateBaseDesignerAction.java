/*************************************************************************************
* Copyright (c) 2004 Actuate Corporation and others.
* All rights reserved. This program and the accompanying materials 
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
* 
* Contributors:
* Actuate Corporation - Initial implementation.
************************************************************************************/

package org.eclipse.birt.report.designer.ui.actions.cheatsheets;

import org.eclipse.birt.report.designer.internal.ui.editors.layout.ReportLayoutEditor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;

/**
 * Base class for all CheatSheet Actions that are registered in the designer and
 * apply to a specific type of EditPart
 * 
 * Cheat sheet actions are called by cheat sheets that are usually mapped to a
 * predefined template
 */
public abstract class TemplateBaseDesignerAction extends TemplateBaseAction {

	/**
	 * @return the ID of the underlying action to run
	 */
	protected abstract String getActionID();

	protected IAction getAction(ReportLayoutEditor reportDesigner) {
		// we get the action from the designer registry
		ActionRegistry actionRegistry = (ActionRegistry) reportDesigner.getAdapter(ActionRegistry.class);
		return actionRegistry.getAction(getActionID());
	}

}
