/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.script.internal.handler;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabCellInstance;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabInstance;
import org.eclipse.birt.report.item.crosstab.core.script.internal.CrosstabCellInstanceImpl;
import org.eclipse.birt.report.item.crosstab.core.script.internal.CrosstabInstanceImpl;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * CrosstabCreationHandler
 */
public class CrosstabCreationHandler extends BaseCrosstabEventHandler {

	private CrosstabScriptHandler handler;

	public CrosstabCreationHandler(ExtendedItemHandle modelHandle, ClassLoader contextLoader) throws BirtException {
		String javaClass = modelHandle.getEventHandlerClass();
		String script = modelHandle.getOnCreate();

		if ((javaClass == null || javaClass.trim().length() == 0) && (script == null || script.trim().length() == 0)) {
			return;
		}

		handler = createScriptHandler(modelHandle, ICrosstabReportItemConstants.ON_CREATE_METHOD, script,
				contextLoader);
	}

	public void handleCrosstab(CrosstabReportItemHandle crosstab, ITableContent content, IReportContext context,
			RunningState runningState) throws BirtException {
		if (handler == null || crosstab == null) {
			return;
		}

		ICrosstabInstance crosstabInst = new CrosstabInstanceImpl(content, crosstab.getModelHandle(), runningState);

		handler.callFunction(CrosstabScriptHandler.ON_CREATE_CROSSTAB, crosstabInst, context);
	}

	public void handleCell(CrosstabCellHandle cell, ICellContent content, IReportContext context) throws BirtException {
		if (handler == null || cell == null) {
			return;
		}

		ICrosstabCellInstance cellInst = new CrosstabCellInstanceImpl(content,
				(ExtendedItemHandle) cell.getModelHandle(), context);

		handler.callFunction(CrosstabScriptHandler.ON_CREATE_CELL, cellInst, context);
	}
}
