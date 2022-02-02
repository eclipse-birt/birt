/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.builder;

import org.eclipse.birt.chart.ui.swt.wizard.TaskSelectType;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * Subtask to wrap TaskFormatType
 */

public class SubtaskSelectType extends SubtaskSheetImpl {

	final private ITask task;

	public SubtaskSelectType() {
		task = new TaskSelectType() {

			@Override
			public void createControl(Composite parent) {
				// Use zero margin in subtask
				pageMargin = 0;
				super.createControl(parent);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if (e.widget == cbMultipleY) {
					// Update tree when axes added
					getParentTask().updateTree();
				}
			}
		};
	}

	@Override
	public void createControl(Composite parent) {
		task.setContext(getContext());
		task.setUIProvider(getWizard());
		task.createControl(parent);
		cmpContent = (Composite) task.getControl();
	}

	@Override
	public boolean isPreviewable() {
		// Has internal preview canvas
		return true;
	}
}
