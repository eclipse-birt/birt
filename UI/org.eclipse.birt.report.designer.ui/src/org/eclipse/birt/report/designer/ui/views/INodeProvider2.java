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

package org.eclipse.birt.report.designer.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.RetargetAction;

/**
 * 
 */
public interface INodeProvider2 extends INodeProvider {

	public RetargetAction[] getRetargetActions(Object object);

	public Action[] getActions(Object object);

}
