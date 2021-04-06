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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.jface.viewers.ISelectionProvider;

/**
 * Global cut action for views
 */

public class GlobalCutAction extends AbstractGlobalSelectionAction {

	protected GlobalCutAction(ISelectionProvider provider) {
		super(provider, GlobalActionFactory.CUT);
	}

	public void run() {
		new CutAction(getSelection()).run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return new CutAction(getSelection()).isEnabled();
	}

}