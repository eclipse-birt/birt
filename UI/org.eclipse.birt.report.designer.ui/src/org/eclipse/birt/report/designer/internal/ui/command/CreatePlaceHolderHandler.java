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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.Request;

/**
 * 
 */

public class CreatePlaceHolderHandler extends SelectionHandler {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		if (Policy.TRACING_ACTIONS) {
			System.out.println("CreatePlaceHolder action "); //$NON-NLS-1$
		}

		Object selElementHandle = getFirstSelectVariable();

		if (selElementHandle == null) {
			return Boolean.FALSE;
		}

		INodeProvider provider = ProviderFactory.createProvider(selElementHandle);

		if (provider == null) {
			return Boolean.FALSE;
		}

		boolean retBool = false;

		try {
			retBool = provider.performRequest(selElementHandle,
					new Request(IRequestConstants.REQUEST_CREATE_PLACEHOLDER));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return Boolean.valueOf(retBool);
	}
}
