/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportEventContext;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;

/*
 * currently ReportEventContext just extends ReportContextImpl
 */
public class ReportEventContext extends ReportContextImpl implements IReportEventContext {

	public ReportEventContext(ExecutionContext context) {
		super(context);
	}
}
