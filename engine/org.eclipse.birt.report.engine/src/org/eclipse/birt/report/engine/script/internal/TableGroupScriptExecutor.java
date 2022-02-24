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
import org.eclipse.birt.report.engine.api.script.element.ITableGroup;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableGroupEventHandler;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.TableGroup;
import org.eclipse.birt.report.engine.script.internal.instance.ReportElementInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.model.api.TableGroupHandle;

public class TableGroupScriptExecutor extends ScriptExecutor {

	public static void handleOnPrepare(TableGroupHandle groupHandle, ExecutionContext context) {
		try {
			ITableGroup group = new TableGroup(groupHandle);
			ITableGroupEventHandler eh = getEventHandler(groupHandle, context);
			if (eh != null)
				eh.onPrepare(group, context.getReportContext());
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public static void handleOnCreate(ITableGroupContent content, ExecutionContext context) {
		ReportItemDesign tableGroupDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnCreate(tableGroupDesign)) {
			return;
		}
		try {
			ReportElementInstance table = new ReportElementInstance(content, context, RunningState.CREATE);
			if (handleScript(table, tableGroupDesign.getOnCreate(), context).didRun())
				return;
			ITableGroupEventHandler eh = getEventHandler(tableGroupDesign, context);
			if (eh != null)
				eh.onCreate(table, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, tableGroupDesign.getHandle());
		}
	}

	public static void handleOnRender(ITableGroupContent content, ExecutionContext context) {
		ReportItemDesign tableGroupDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnRender(tableGroupDesign)) {
			return;
		}
		try {
			ReportElementInstance table = new ReportElementInstance(content, context, RunningState.RENDER);
			if (handleScript(table, tableGroupDesign.getOnRender(), context).didRun())
				return;
			ITableGroupEventHandler eh = getEventHandler(tableGroupDesign, context);
			if (eh != null)
				eh.onRender(table, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, tableGroupDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(ITableGroupContent content, ExecutionContext context) {
		ReportItemDesign tableGroupDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnPageBreak(tableGroupDesign, context)) {
			return;
		}
		try {
			ReportElementInstance table = new ReportElementInstance(content, context, RunningState.PAGEBREAK);
			if (handleScript(table, tableGroupDesign.getOnPageBreak(), context).didRun())
				return;
			ITableGroupEventHandler eh = getEventHandler(tableGroupDesign, context);
			if (eh != null)
				eh.onPageBreak(table, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, tableGroupDesign.getHandle());
		}
	}

	private static ITableGroupEventHandler getEventHandler(TableGroupHandle handle, ExecutionContext context) {
		try {
			return (ITableGroupEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, ITableGroupEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}

	private static ITableGroupEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (ITableGroupEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), ITableGroupEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}
}
