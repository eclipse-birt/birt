/*******************************************************************************
 * Copyright (c) 2004, 2026 Actuate Corporation and others
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

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.script.IExternalContext;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.lc.type.TypeInfo;

/**
 * An external context implementation for BIRT environment.
 */
public class BIRTExternalContext implements IExternalContext {

	private static final long serialVersionUID = 1L;

	private transient IReportContext context;
	private Scriptable scriptableContext;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public BIRTExternalContext() {
		super();
	}

	/**
	 * The constructor.
	 *
	 * @param context
	 */
	public BIRTExternalContext(IReportContext context) {
		this.context = context;

		final Context cx = Context.enter();

		try {
			Scriptable scope = new ImporterTopLevel(cx);

			scriptableContext = cx.getWrapFactory().wrapAsJavaObject(cx, scope, context, TypeInfo.NONE);
		} catch (Exception e) {
			logger.log(e);
		} finally {
			Context.exit();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.script.IExternalContext#getScriptable()
	 */
	@Override
	public Scriptable getScriptable() {
		return scriptableContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.script.IExternalContext#getObject()
	 */
	@Override
	public Object getObject() {
		return context;
	}

}
