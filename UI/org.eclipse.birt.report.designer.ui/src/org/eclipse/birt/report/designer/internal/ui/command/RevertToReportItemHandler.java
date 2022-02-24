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

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.Request;

/**
 * 
 */

public class RevertToReportItemHandler extends SelectionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO add to commandstack

		super.execute(event);
		boolean retBoolean = false;

		Object selElementHandle = getFirstSelectVariable();

		if (selElementHandle == null || (!(selElementHandle instanceof DesignElementHandle))) {
			return Boolean.FALSE;
		}

		if (((DesignElementHandle) selElementHandle).isTemplateParameterValue()) {

			INodeProvider provider = ProviderFactory.createProvider(selElementHandle);

			try {
				if (selElementHandle instanceof TemplateReportItemHandle) {
					retBoolean = provider.performRequest(selElementHandle,
							new Request(IRequestConstants.REQUEST_TRANSFER_PLACEHOLDER));
				} else {
					retBoolean = provider.performRequest(selElementHandle,
							new Request(IRequestConstants.REQUST_REVERT_TO_REPORTITEM));

				}
			} catch (Exception e) {
//				stack.rollbackAll( );
				ExceptionHandler.handle(e);
				retBoolean = false;
			}

		}

		return Boolean.valueOf(retBoolean);
	}
}
