/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.Action;

/**
 * 
 */

public class NewParameterAction extends Action implements UpdateAction {

	public static final String INSERT_SCALAR_PARAMETER = "org.eclipse.birt.report.designer.ui.actions.newScalarParameter"; //$NON-NLS-1$

	public static final String INSERT_PARAMETER_GROUP = "org.eclipse.birt.report.designer.ui.actions.newParameterGroup"; //$NON-NLS-1$

	public static final String INSERT_CASCADING_PARAMETER_GROUP = "org.eclipse.birt.report.designer.ui.actions.newCascadingParameterGroup"; //$NON-NLS-1$

	private Action action = null;

	private String type;

	public NewParameterAction(String ID, String type) {
		super();

		setId(ID);

		this.type = type;
	}

	public NewParameterAction(String ID, String type, String text) {
		this(ID, type);

		setText(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.UpdateAction#update()
	 */
	public void update() {
		if (action == null) {
			ModuleHandle module = SessionHandleAdapter.getInstance().getReportDesignHandle();

			if (module == null) {
				setEnabled(false);
				return;
			}
			action = new InsertAction(module.getParameters(),
					Messages.getString("ParametersNodeProvider.menu.text.cascadingParameter"), //$NON-NLS-1$
					type);
		}
		setEnabled(action.isEnabled());
	}

	@Override
	public boolean isEnabled() {
		if (action == null) {
			update();
		}
		return super.isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		action.run();
	}

}
