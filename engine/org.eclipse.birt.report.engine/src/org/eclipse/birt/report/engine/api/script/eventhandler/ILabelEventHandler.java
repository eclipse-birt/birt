/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script.eventhandler;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.element.ILabel;
import org.eclipse.birt.report.engine.api.script.instance.ILabelInstance;

/**
 * Script event handler interface for a label
 */
public interface ILabelEventHandler
{

	/**
	 * Handle the onPrepare event
	 */
	void onPrepare( ILabel labelHandle, IReportContext reportContext );

	/**
	 * Handle the onCreate event
	 */
	void onCreate( ILabelInstance label, IReportContext reportContext );

	/**
	 * Handle the onRender event
	 */
	void onRender( ILabelInstance label, IReportContext reportContext );
}
