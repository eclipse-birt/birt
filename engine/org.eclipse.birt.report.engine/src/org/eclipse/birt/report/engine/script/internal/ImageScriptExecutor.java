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

import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.element.IImage;
import org.eclipse.birt.report.engine.api.script.eventhandler.IImageEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IImageInstance;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Image;
import org.eclipse.birt.report.engine.script.internal.instance.ImageInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.engine.script.internal.instance.UnsupportedImageInstance;
import org.eclipse.birt.report.model.api.ImageHandle;

public class ImageScriptExecutor extends ScriptExecutor {
	public static void handleOnPrepare(ImageHandle imageHandle, ExecutionContext context) {
		try {
			IImage image = new Image(imageHandle);
			IImageEventHandler eh = getEventHandler(imageHandle, context);
			if (eh != null)
				eh.onPrepare(image, context.getReportContext());
		} catch (Exception e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public static void handleOnCreate(IContent content, ExecutionContext context) {

		ReportItemDesign imageDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnCreate(imageDesign)) {
			return;
		}
		try {
			IImageInstance image = createImageInstance(content, context, RunningState.CREATE);
			if (handleScript(image, imageDesign.getOnCreate(), context).didRun())
				return;
			IImageEventHandler eh = getEventHandler(imageDesign, context);
			if (eh != null)
				eh.onCreate(image, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, imageDesign.getHandle());
		}
	}

	public static void handleOnRender(IContent content, ExecutionContext context) {
		ReportItemDesign imageDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnRender(imageDesign)) {
			return;
		}
		try {
			IImageInstance image = createImageInstance(content, context, RunningState.RENDER);
			if (handleScript(image, imageDesign.getOnRender(), context).didRun())
				return;
			IImageEventHandler eh = getEventHandler(imageDesign, context);
			if (eh != null)
				eh.onRender(image, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, imageDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(IContent content, ExecutionContext context) {
		ReportItemDesign imageDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnPageBreak(imageDesign, context)) {
			return;
		}
		try {
			IImageInstance image = createImageInstance(content, context, RunningState.PAGEBREAK);
			if (handleScript(image, imageDesign.getOnPageBreak(), context).didRun())
				return;
			IImageEventHandler eh = getEventHandler(imageDesign, context);
			if (eh != null)
				eh.onPageBreak(image, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, imageDesign.getHandle());
		}
	}

	private static IImageEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (IImageEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), IImageEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}

	private static IImageEventHandler getEventHandler(ImageHandle handle, ExecutionContext context) {
		try {
			return (IImageEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, IImageEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}

	private static IImageInstance createImageInstance(IContent content, ExecutionContext context, RunningState state) {
		if (content instanceof IImageContent) {
			return new ImageInstance(content, context, state);
		} else if (content instanceof ITextContent) {
			return new UnsupportedImageInstance((ITextContent) content);
		}
		return null;
	}
}
