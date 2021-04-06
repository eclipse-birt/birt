/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;

/**
 * Provider for the MasterPage node
 * 
 * 
 */
public class MasterPageNodeProvider extends DefaultNodeProvider {

	/**
	 * the text for new action
	 */
	public static final String ACTION_NEW = "MasterPageProcess.action.New"; //$NON-NLS-1$

	/**
	 * the text for edit action
	 */
	public static final String ACTION_EDIT = "MasterPageProcess.action.Edit"; //$NON-NLS-1$

	/**
	 * the text for display action
	 */
	public static final String MSG_DISPLAY = "MasterPageProcess.text.Display"; //$NON-NLS-1$

	public static final String MSG_CANNOTDEL = "MasterPageProcess.text.CannotDelete"; //$NON-NLS-1$

	public static final String DISPLAYNAME = "MasterPageProcess.text.DisplayName"; //$NON-NLS-1$

	/**
	 * Gets the children element of the given model using visitor.
	 * 
	 * @param model the model
	 */
	public Object[] getChildren(Object model) {
		if (model instanceof SimpleMasterPageHandle) {
			SimpleMasterPageHandle handle = (SimpleMasterPageHandle) model;
			ArrayList list = new ArrayList();
			list.add(handle.getPageHeader());
			list.add(handle.getPageFooter());
			return list.toArray();
		}
		return super.getChildren(model);
	}
}