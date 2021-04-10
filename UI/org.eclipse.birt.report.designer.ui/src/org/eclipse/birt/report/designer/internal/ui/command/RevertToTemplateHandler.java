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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.Request;

/**
 * 
 */

public class RevertToTemplateHandler extends SelectionHandler {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		boolean retBoolean = false;
		Object selElementHandle = getFirstSelectVariable();

		if (selElementHandle == null || (!(selElementHandle instanceof DesignElementHandle))) {
			return Boolean.FALSE;
		}

		else if (((DesignElementHandle) selElementHandle).isTemplateParameterValue()) {
			INodeProvider provider = ProviderFactory.createProvider(selElementHandle);
			try {
				retBoolean = provider.performRequest(selElementHandle,
						new Request(IRequestConstants.REQUST_REVERT_TO_TEMPLATEITEM));
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		return Boolean.valueOf(retBoolean);
	}
}
