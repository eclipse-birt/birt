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

public class EditHandler extends SelectionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		super.execute(event);

		Object obj = getFirstSelectVariable();
		if ((obj == null) || (!(obj instanceof DesignElementHandle))) {
			return Boolean.FALSE;
		}

		DesignElementHandle elmentHandle = (DesignElementHandle) obj;

		if (elmentHandle == null) {
			return Boolean.FALSE;
		}

		INodeProvider provider = ProviderFactory.createProvider(elmentHandle);
		boolean retBoolean;
		try {
			retBoolean = provider.performRequest(elmentHandle, new Request(IRequestConstants.REQUEST_TYPE_EDIT));
		} catch (Exception e) {
			retBoolean = false;
		}

		return Boolean.valueOf(retBoolean);
	}
}
