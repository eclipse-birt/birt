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
import org.eclipse.birt.report.engine.api.script.element.ITable;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ITableInstance;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Table;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.engine.script.internal.instance.TableInstance;
import org.eclipse.birt.report.model.api.TableHandle;

public class TableScriptExecutor extends ScriptExecutor {
	public static void handleOnPrepare(TableHandle tableHandle, ExecutionContext context) {
		try {
			ITable table = new Table(tableHandle);
			ITableEventHandler eh = getEventHandler(tableHandle, context);
			if (eh != null)
				eh.onPrepare(table, context.getReportContext());
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public static void handleOnCreate(ITableContent content, ExecutionContext context) {
		ReportItemDesign tableDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnCreate(tableDesign)) {
			return;
		}
		try {
			ITableInstance table = new TableInstance(content, context, RunningState.CREATE);
			if (handleScript(table, tableDesign.getOnCreate(), context).didRun())
				return;
			ITableEventHandler eh = getEventHandler(tableDesign, context);
			if (eh != null)
				eh.onCreate(table, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, tableDesign.getHandle());
		}
	}

	public static void handleOnRender(ITableContent content, ExecutionContext context) {
		ReportItemDesign tableDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnRender(tableDesign)) {
			return;
		}
		try {
			ITableInstance table = new TableInstance(content, context, RunningState.RENDER);
			if (handleScript(table, tableDesign.getOnRender(), context).didRun())
				return;
			ITableEventHandler eh = getEventHandler(tableDesign, context);
			if (eh != null)
				eh.onRender(table, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, tableDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(ITableContent content, ExecutionContext context) {
		ReportItemDesign tableDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnPageBreak(tableDesign, context)) {
			return;
		}
		try {
			ITableInstance table = new TableInstance(content, context, RunningState.PAGEBREAK);
			if (handleScript(table, tableDesign.getOnPageBreak(), context).didRun())
				return;
			ITableEventHandler eh = getEventHandler(tableDesign, context);
			if (eh != null)
				eh.onPageBreak(table, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, tableDesign.getHandle());
		}
	}

	private static ITableEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (ITableEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), ITableEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}

	private static ITableEventHandler getEventHandler(TableHandle handle, ExecutionContext context) {
		try {
			return (ITableEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, ITableEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}
}
