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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.action.Action;

/**
 * Uses this action to fill blank menu.
 */

public class NoneAction extends Action {

	public static final String ID = "none"; //$NON-NLS-1$

	public static final String DISPLAY_TEXT = Messages.getString("NoneAction.text"); //$NON-NLS-1$

	private static NoneAction instance;

	/**
	 *
	 */
	public NoneAction() {
		this(DISPLAY_TEXT);
	}

	private NoneAction(String text) {
		super();
		setId(ID);
		setText(text);
	}

	/*
	 * Return the unique NoneAction instance
	 */
	public static NoneAction getInstance() {
		if (instance == null) {
			instance = new NoneAction();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return false;
	}
}
