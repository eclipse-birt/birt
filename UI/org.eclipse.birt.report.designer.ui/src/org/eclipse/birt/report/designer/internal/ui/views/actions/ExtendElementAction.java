/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.gef.Request;

public class ExtendElementAction extends InsertAction {

	private final INodeProvider provider;

	/**
	 * @param selectedObject
	 * @param text
	 * @param type
	 */
	public ExtendElementAction(INodeProvider provider, String id, Object selectedObject, String text, String type) {
		super(selectedObject, text, type);
		setId(id);
		this.provider = provider;
	}

	public ExtendElementAction(INodeProvider provider, String id, Object selectedObject, PropertyHandle handle,
			String text, String type) {
		super(selectedObject, text, handle, type);
		setId(id);
		this.provider = provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	protected boolean doAction() throws Exception {
		Request request = new Request(IRequestConstants.REQUEST_TYPE_INSERT);
		Map extendsData = new HashMap();

		if (getPropertyHandle() != null) {
			extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_PROPERTY, getPropertyHandle());
		} else if (getSlotHandle() != null) {
			extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_SLOT, getSlotHandle());
		}

		if (getType() != null) {
			extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_TYPE, getType());
		}
		extendsData.put(IRequestConstants.REQUEST_KEY_INSERT_POSITION, getPosition());
		request.setExtendedData(extendsData);
		boolean bool = provider.performRequest(getSelection(), request);
		if (bool) {
			List list = new ArrayList();

			list.add(request.getExtendedData().get(IRequestConstants.REQUEST_KEY_RESULT));
			ReportRequest r = new ReportRequest();
			r.setType(ReportRequest.CREATE_ELEMENT);

			r.setSelectionObject(list);
			SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);

		}
		return bool;
	}

}
