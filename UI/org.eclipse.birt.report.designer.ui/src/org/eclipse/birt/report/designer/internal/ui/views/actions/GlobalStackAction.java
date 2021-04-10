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

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.Action;

/**
 * Abstract class for all global stack actions
 */

public abstract class GlobalStackAction extends Action implements UpdateAction {

	protected CommandStack stack;

	protected GlobalStackAction(String id, CommandStack stack) {
		setId(id);
		this.stack = stack;
		stack.addListener(new ActivityStackListener() {

			public void stackChanged(ActivityStackEvent event) {
				if (event.getStack() == GlobalStackAction.this.stack) {
					update();
				}
			}

		});
	}

	abstract protected boolean calculateEnabled();

	abstract protected String getDisplayLabel();

	public void update() {
		setEnabled(calculateEnabled());
		setText(getDisplayLabel());
	}

	public CommandStack getStack() {
		return stack;
	}
}