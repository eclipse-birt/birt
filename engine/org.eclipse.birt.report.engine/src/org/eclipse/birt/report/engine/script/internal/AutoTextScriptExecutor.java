/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import org.eclipse.birt.report.engine.api.script.element.IAutoText;
import org.eclipse.birt.report.engine.api.script.eventhandler.IAutoTextEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IAutoTextInstance;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.AutoText;
import org.eclipse.birt.report.engine.script.internal.instance.AutoTextInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.model.api.AutoTextHandle;

public class AutoTextScriptExecutor extends ScriptExecutor {
	public static void handleOnPrepare(AutoTextHandle autoTextHandle, ExecutionContext context) {
		try {
			IAutoText cell = new AutoText(autoTextHandle);
			IAutoTextEventHandler eh = getEventHandler(autoTextHandle, context);
			if (eh != null) {
				eh.onPrepare(cell, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public static void handleOnCreate(IAutoTextContent content, ExecutionContext context) {
		Object generateBy = content.getGenerateBy();
		if (generateBy == null) {
			return;
		}
		ReportItemDesign autoTextItemDesign = (ReportItemDesign) generateBy;
		try {
			if (!needOnCreate(autoTextItemDesign)) {
				return;
			}
			IAutoTextInstance autoText = new AutoTextInstance(content, context, RunningState.CREATE);
			if (handleScript(autoText, autoTextItemDesign.getOnCreate(), context).didRun()) {
				return;
			}
			IAutoTextEventHandler eh = getEventHandler(autoTextItemDesign, context);
			if (eh != null) {
				eh.onCreate(autoText, context.getReportContext());
			}

		} catch (Exception e) {
			addException(context, e, autoTextItemDesign.getHandle());
		}
	}

	public static void handleOnRender(IAutoTextContent content, ExecutionContext context) {
		Object generateBy = content.getGenerateBy();
		if (generateBy == null) {
			return;
		}
		ReportItemDesign autoTextDesign = (ReportItemDesign) generateBy;
		if (!needOnRender(autoTextDesign)) {
			return;
		}
		try {
			// fromGrid doesn't matter here since row data is null
			IAutoTextInstance autoText = new AutoTextInstance(content, context, RunningState.RENDER);
			if (handleScript(autoText, autoTextDesign.getOnRender(), context).didRun()) {
				return;
			}
			IAutoTextEventHandler eh = getEventHandler(autoTextDesign, context);
			if (eh != null) {
				eh.onRender(autoText, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, autoTextDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(IAutoTextContent content, ExecutionContext context) {
		Object generateBy = content.getGenerateBy();
		if (generateBy == null) {
			return;
		}
		ReportItemDesign autoTextDesign = (ReportItemDesign) generateBy;
		try {
			if (!needOnPageBreak(autoTextDesign, context)) {
				return;
			}
			// fromGrid doesn't matter here since row data is null
			IAutoTextInstance autoText = new AutoTextInstance(content, context, RunningState.PAGEBREAK);
			if (handleScript(autoText, autoTextDesign.getOnPageBreak(), context).didRun()) {
				return;
			}
			IAutoTextEventHandler eh = getEventHandler(autoTextDesign, context);
			if (eh != null) {
				eh.onPageBreak(autoText, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, autoTextDesign.getHandle());
		}
	}

	private static IAutoTextEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (IAutoTextEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), IAutoTextEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}

	private static IAutoTextEventHandler getEventHandler(AutoTextHandle handle, ExecutionContext context) {
		try {
			return (IAutoTextEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, IAutoTextEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}
}
