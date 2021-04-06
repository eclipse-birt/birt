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
 * Global copy action for views
 */

public class GlobalCopyAction extends AbstractGlobalSelectionAction {

	protected GlobalCopyAction(ISelectionProvider provider) {
		super(provider, GlobalActionFactory.COPY);
	}

	public void run() {
		new CopyAction(getSelection()).run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return new CopyAction(getSelection()).isEnabled();
	}

}