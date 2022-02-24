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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.internal.ui.processor.ElementProcessorFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemViewProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;

/**
 * 
 */

public class CreateChartHandler extends SelectionHandler {

	private static final String TEXT = Messages.getString("CreateChartHandler.text"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.command.SelectionHandler#execute
	 * (org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		EditPart part = (EditPart) getSelectedObjects().get(0);
		Object model = part.getModel();
		if (!(model instanceof ReportItemHandle) && model instanceof IAdaptable) {
			model = ((IAdaptable) model).getAdapter(DesignElementHandle.class);
		}

		ReportItemHandle handle = (ReportItemHandle) model;
		ModuleHandle module = handle.getModuleHandle();

		Object[] objs = ElementAdapterManager.getAdapters(handle, IReportItemViewProvider.class);
		if (objs == null || objs.length > 1) {
			return Boolean.FALSE;
		}
		IReportItemViewProvider provider = (IReportItemViewProvider) objs[0];

		module.getCommandStack().startTrans(TEXT);
		try {
			DesignElementHandle chart = provider.createView(handle);
			handle.addView(chart);

			if (ElementProcessorFactory.createProcessor(chart) != null
					&& !ElementProcessorFactory.createProcessor(chart).editElement(chart)) {
				module.getCommandStack().rollbackAll();
				return Boolean.FALSE;
			}
		} catch (BirtException e) {
			module.getCommandStack().rollbackAll();
			return Boolean.FALSE;
		}
		module.getCommandStack().commit();
		return Boolean.TRUE;
	}

}
