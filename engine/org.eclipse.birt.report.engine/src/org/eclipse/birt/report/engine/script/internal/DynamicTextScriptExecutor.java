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
import org.eclipse.birt.report.engine.api.script.element.IDynamicText;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDynamicTextEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDynamicTextInstance;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.DynamicText;
import org.eclipse.birt.report.engine.script.internal.instance.DynamicTextInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.model.api.TextDataHandle;

public class DynamicTextScriptExecutor extends ScriptExecutor {

	public static void handleOnPrepare(TextDataHandle textDataHandle, ExecutionContext context) {
		try {
			IDynamicText text = new DynamicText(textDataHandle);
			IDynamicTextEventHandler eh = getEventHandler(textDataHandle, context);
			if (eh != null) {
				eh.onPrepare(text, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public static void handleOnCreate(IContent content, ExecutionContext context) {
		ReportItemDesign textItemDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnCreate(textItemDesign)) {
			return;
		}
		try {
			IDynamicTextInstance text = createDynamicTextInstance(content, context, RunningState.CREATE);
			if (handleScript(text, textItemDesign.getOnCreate(), context).didRun()) {
				return;
			}
			IDynamicTextEventHandler eh = getEventHandler(textItemDesign, context);
			if (eh != null) {
				eh.onCreate(text, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, textItemDesign.getHandle());
		}
	}

	public static void handleOnRender(IContent content, ExecutionContext context) {
		ReportItemDesign textItemDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnRender(textItemDesign)) {
			return;
		}
		try {
			IDynamicTextInstance text = createDynamicTextInstance(content, context, RunningState.RENDER);
			if (handleScript(text, textItemDesign.getOnRender(), context).didRun()) {
				return;
			}
			IDynamicTextEventHandler eh = getEventHandler(textItemDesign, context);
			if (eh != null) {
				eh.onRender(text, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, textItemDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(IContent content, ExecutionContext context) {
		ReportItemDesign textItemDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnPageBreak(textItemDesign, context)) {
			return;
		}
		try {
			IDynamicTextInstance text = createDynamicTextInstance(content, context, RunningState.PAGEBREAK);
			if (handleScript(text, textItemDesign.getOnPageBreak(), context).didRun()) {
				return;
			}
			IDynamicTextEventHandler eh = getEventHandler(textItemDesign, context);
			if (eh != null) {
				eh.onPageBreak(text, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, textItemDesign.getHandle());
		}
	}

	private static IDynamicTextInstance createDynamicTextInstance(IContent content, ExecutionContext context,
			RunningState runningState) {
		return new DynamicTextInstance(content, context, runningState);
	}

	private static IDynamicTextEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (IDynamicTextEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), IDynamicTextEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}

	private static IDynamicTextEventHandler getEventHandler(TextDataHandle handle, ExecutionContext context) {
		try {
			return (IDynamicTextEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, IDynamicTextEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}
}
