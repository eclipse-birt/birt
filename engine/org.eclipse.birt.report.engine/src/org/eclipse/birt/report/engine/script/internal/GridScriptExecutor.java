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
import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.engine.api.script.eventhandler.IGridEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IGridInstance;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Grid;
import org.eclipse.birt.report.engine.script.internal.instance.GridInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.model.api.GridHandle;

public class GridScriptExecutor extends ScriptExecutor {
	public static void handleOnPrepare(GridHandle gridHandle, ExecutionContext context) {
		try {
			IGrid grid = new Grid(gridHandle);
			IGridEventHandler eh = getEventHandler(gridHandle, context);
			if (eh != null)
				eh.onPrepare(grid, context.getReportContext());
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public static void handleOnCreate(ITableContent content, ExecutionContext context) {
		ReportItemDesign gridDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnCreate(gridDesign)) {
			return;
		}
		try {
			IGridInstance grid = new GridInstance(content, context, RunningState.CREATE);
			if (handleScript(grid, gridDesign.getOnCreate(), context).didRun())
				return;
			IGridEventHandler eh = getEventHandler(gridDesign, context);
			if (eh != null)
				eh.onCreate(grid, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, gridDesign.getHandle());
		}
	}

	public static void handleOnRender(ITableContent content, ExecutionContext context) {
		ReportItemDesign gridDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnRender(gridDesign)) {
			return;
		}
		try {
			IGridInstance grid = new GridInstance(content, context, RunningState.RENDER);
			if (handleScript(grid, gridDesign.getOnRender(), context).didRun())
				return;
			IGridEventHandler eh = getEventHandler(gridDesign, context);
			if (eh != null)
				eh.onRender(grid, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, gridDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(ITableContent content, ExecutionContext context) {
		ReportItemDesign gridDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnPageBreak(gridDesign, context)) {
			return;
		}
		try {
			IGridInstance grid = new GridInstance(content, context, RunningState.PAGEBREAK);
			if (handleScript(grid, gridDesign.getOnPageBreak(), context).didRun())
				return;
			IGridEventHandler eh = getEventHandler(gridDesign, context);
			if (eh != null)
				eh.onPageBreak(grid, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, gridDesign.getHandle());
		}
	}

	private static IGridEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (IGridEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), IGridEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}

	private static IGridEventHandler getEventHandler(GridHandle handle, ExecutionContext context) {
		try {
			return (IGridEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, IGridEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}
}
