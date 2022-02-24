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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.processor.ElementProcessorFactory;
import org.eclipse.birt.report.designer.internal.ui.processor.IElementProcessor;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.jface.viewers.ISelectionProvider;

/**
 *
 */

public class GlobalInsertAction extends AbstractGlobalSelectionAction {

	private String dataType;

	/**
	 * @param provider
	 * @param id
	 */
	protected GlobalInsertAction(ISelectionProvider provider, String id, String dataType) {
		super(provider, id);
		this.dataType = dataType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {

		SlotHandle container = getContainer();
		if (container != null) {
			return container.getElementHandle().canContain(container.getSlotID(), dataType);
		}

		return false;
	}

	private SlotHandle getContainer() {
		SlotHandle container = null;
		if (getSelectedObjects().size() == 1) {
			Object selected = getSelectedObjects().get(0);

			if (selected instanceof SlotHandle) {
				container = (SlotHandle) selected;
			}
//			else if ( selected instanceof ReportElementModel )
//			{
//				container = ( (ReportElementModel) selected ).getSlotHandle( );
//			}
			else if (selected instanceof DesignElementHandle) {
				int slotId = DEUtil.getDefaultSlotID(selected);
				if (slotId != -1) {
					container = ((DesignElementHandle) selected).getSlot(slotId);
				}
			}
		}
		return container;
	}

	@Override
	public void run() {
		if (SessionHandleAdapter.getInstance().getReportDesignHandle() == null) {
			return;
		}
		CommandStack stack = SessionHandleAdapter.getInstance().getReportDesignHandle().getCommandStack();
		IElementProcessor processor = ElementProcessorFactory.createProcessor(dataType);
		stack.startTrans(processor.getCreateTransactionLabel());
		DesignElementHandle handle = processor.createElement(null);
		if (handle == null) {
			stack.rollback();
		} else {
			try {
				getContainer().add(handle);
			} catch (Exception e) {
				stack.rollback();
				ExceptionHandler.handle(e);
				return;
			}

		}
		if (handle instanceof ExtendedItemHandle) {
			if (ElementProcessorFactory.createProcessor(handle) != null
					&& !ElementProcessorFactory.createProcessor(handle).editElement(handle)) {
				stack.rollback();
				return;
			}
		}
		stack.commit();
		synWithMediator(handle);
		super.run();
	}

	private void synWithMediator(DesignElementHandle handle) {
		List list = new ArrayList();

		list.add(handle);
		ReportRequest r = new ReportRequest();
		r.setType(ReportRequest.CREATE_ELEMENT);

		r.setSelectionObject(list);
		SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);
	}
}
