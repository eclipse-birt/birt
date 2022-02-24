/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
package org.eclipse.birt.report.engine.api.script.eventadapter;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IImage;
import org.eclipse.birt.report.engine.api.script.eventhandler.IImageEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IImageInstance;

/**
 * Default (empty) implementation of the IImageEventHandler interface
 */
public class ImageEventAdapter implements IImageEventHandler {

	public void onPrepare(IImage imageHandle, IReportContext reportContext) throws ScriptException {

	}

	public void onCreate(IImageInstance image, IReportContext reportContext) throws ScriptException {

	}

	public void onRender(IImageInstance image, IReportContext reportContext) throws ScriptException {

	}

	public void onPageBreak(IImageInstance image, IReportContext reportContext) throws ScriptException {

	}

}
