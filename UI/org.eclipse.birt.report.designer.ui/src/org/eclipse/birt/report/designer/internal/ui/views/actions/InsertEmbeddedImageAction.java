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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.gef.Request;

/**
 * Insert Embedded Image Action
 */

public class InsertEmbeddedImageAction extends AbstractElementAction {

	public final static String ID = "org.eclipse.birt.report.designer.ui.views.action.InsertEmbeddedImageAction";//$NON-NLS-1$

	/**
	 * Create a new insert action with given selection and text at specified
	 * position
	 *
	 * @param selectedObject the selected object,which cannot be null
	 *
	 */
	public InsertEmbeddedImageAction(Object selectedObject) {
		this(selectedObject, Messages.getString("EmbeddedImageNodeProvider.action.New")); //$NON-NLS-1$
	}

	/**
	 * Create a new insert action with given selection and text
	 *
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public InsertEmbeddedImageAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	@Override
	protected boolean doAction() throws Exception {
		Request request = new Request(IRequestConstants.REQUEST_TYPE_INSERT);

		return ProviderFactory.createProvider(getSelection()).performRequest(getSelection(), request);
	}
}
