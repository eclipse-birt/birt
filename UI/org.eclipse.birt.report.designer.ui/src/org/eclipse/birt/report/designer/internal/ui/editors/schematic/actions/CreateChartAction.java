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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemViewProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 */

public class CreateChartAction extends ContextSelectionAction {

	private static final String TEXT = Messages.getString("CreateChartAction.text"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.CreateChartAction"; //$NON-NLS-1$

	/**
	 * @param part
	 */
	public CreateChartAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(TEXT);
	}

	protected boolean calculateEnabled() {
		List selected = getSelectedObjects();

		if (selected.size() != 1 || !(selected.get(0) instanceof EditPart)) {
			return false;
		}

		EditPart part = (EditPart) selected.get(0);
		Object model = part.getModel();
		if (!(model instanceof ReportItemHandle) && model instanceof IAdaptable) {
			model = ((IAdaptable) model).getAdapter(DesignElementHandle.class);
		}
		if (!(model instanceof ReportItemHandle) || DEUtil.isReferenceElement((ReportItemHandle) model)) {
			return false;
		}
		Object[] objs = ElementAdapterManager.getAdapters(model, IReportItemViewProvider.class);
		if (objs == null || objs.length > 1) {
			return false;
		}
		if (((ReportItemHandle) model).getViews().size() != 0 || !((ReportItemHandle) model).canAddView("Chart"))//$NON-NLS-1$
		{
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.CreateChartViewCommand", //$NON-NLS-1$
					null);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
