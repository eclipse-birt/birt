/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.element.ICell;
import org.eclipse.birt.report.engine.api.script.eventhandler.ICellEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ICellInstance;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Cell;
import org.eclipse.birt.report.engine.script.internal.instance.CellInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.model.api.CellHandle;

public class CellScriptExecutor extends ScriptExecutor {

	public static void handleOnPrepare(CellHandle cellHandle, ExecutionContext context) {
		try {
			ICell cell = new Cell(cellHandle);
			ICellEventHandler eh = getEventHandler(cellHandle, context);
			if (eh != null) {
				eh.onPrepare(cell, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public static void handleOnCreate(ICellContent content, ExecutionContext context, boolean fromGrid) {
		Object generateBy = content.getGenerateBy();
		if (generateBy == null) {
			return;
		}
		ReportItemDesign cellDesign = (ReportItemDesign) generateBy;
		try {
			if (!needOnCreate(cellDesign)) {
				return;
			}
			ICellInstance cell = new CellInstance(content, context, RunningState.CREATE, fromGrid);
			if (handleScript(cell, cellDesign.getOnCreate(), context).didRun()) {
				return;
			}
			ICellEventHandler eh = getEventHandler(cellDesign, context);
			if (eh != null) {
				eh.onCreate(cell, context.getReportContext());
			}

		} catch (Exception e) {
			addException(context, e, cellDesign.getHandle());
		}
	}

	public static void handleOnRender(ICellContent content, ExecutionContext context) {
		Object generateBy = content.getGenerateBy();
		if (generateBy == null) {
			return;
		}
		ReportItemDesign cellDesign = (ReportItemDesign) generateBy;
		if (!needOnRender(cellDesign)) {
			return;
		}

		try {
			// fromGrid doesn't matter here since row data is null
			ICellInstance cell = new CellInstance(content, context, RunningState.RENDER, false);
			if (handleScript(cell, cellDesign.getOnRender(), context).didRun()) {
				return;
			}
			ICellEventHandler eh = getEventHandler(cellDesign, context);
			if (eh != null) {
				eh.onRender(cell, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, cellDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(ICellContent content, ExecutionContext context) {
		Object generateBy = content.getGenerateBy();
		if (generateBy == null) {
			return;
		}
		ReportItemDesign cellDesign = (ReportItemDesign) generateBy;
		if (!needOnPageBreak(cellDesign, context)) {
			return;
		}

		try {
			// fromGrid doesn't matter here since row data is null
			ICellInstance cell = new CellInstance(content, context, RunningState.RENDER, false);
			if (handleScript(cell, cellDesign.getOnPageBreak(), context).didRun()) {
				return;
			}
			ICellEventHandler eh = getEventHandler(cellDesign, context);
			if (eh != null) {
				eh.onPageBreak(cell, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, cellDesign.getHandle());
		}
	}

	private static ICellEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (ICellEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), ICellEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}

	private static ICellEventHandler getEventHandler(CellHandle handle, ExecutionContext context) {
		try {
			return (ICellEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, ICellEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}
}
