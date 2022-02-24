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

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertTableGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;

/**
 * Create Group action to be used in Cheat Sheets. The first parameter is the
 * table name inside the template file
 *
 */
public class TemplateCreateGroupAction extends TemplateBaseDesignerAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.cheatsheets.actions.
	 * TemplateBaseDesignerAction#getEditPartType()
	 */
	protected boolean checkType(Class type) {
		return (type == TableEditPart.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.cheatsheets.actions.
	 * TemplateBaseDesignerAction#getActionID()
	 */
	protected String getActionID() {
		return InsertTableGroupAction.ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.cheatsheets.actions.
	 * TemplateBaseDesignerAction#showErrorWrongElementSelection()
	 */
	protected void showErrorWrongElementSelection() {
		// TODO Auto-generated method stub
	}

}
