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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.gef.Request;

/**
 * 
 */

public class InsertHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		// should not use getDefaultVariable( context ), but the selection
		// variable passed.
		// Object selection = getDefaultVariable( context );
		SlotHandle slotHandle = null;
		Object obj = UIUtil.getVariableFromContext(context, ICommandParameterNameContants.INSERT_SLOT_HANDLE);
		if (obj != null && obj instanceof SlotHandle) {
			slotHandle = (SlotHandle) obj;
		}
		Request request = new Request(IRequestConstants.REQUEST_TYPE_INSERT);
		Map extendsData = new HashMap();

		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_SLOT, slotHandle);

		if (UIUtil.getVariableFromContext(context, ICommandParameterNameContants.INSERT_ACTION_TYPE) != null) {
			extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_TYPE,
					UIUtil.getVariableFromContext(context, ICommandParameterNameContants.INSERT_ACTION_TYPE));
		}
		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_POSITION,
				UIUtil.getVariableFromContext(context, ICommandParameterNameContants.INSERT_ACTION_POSITION));
		request.setExtendedData(extendsData);
		Object selection = UIUtil.getVariableFromContext(context,
				ICommandParameterNameContants.INSERT_ACTION_SELECTION);
		boolean bool = false;
		try {
			INodeProvider provider = ProviderFactory.createProvider(selection);
			bool = provider.performRequest(selection, request);
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		if (bool) {
			List list = new ArrayList();

			list.add(request.getExtendedData().get(IRequestConstants.REQUEST_KEY_RESULT));
			ReportRequest r = new ReportRequest();
			r.setType(ReportRequest.CREATE_ELEMENT);

			r.setSelectionObject(list);
			SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);

		}
		return Boolean.valueOf(bool);
	}

}
