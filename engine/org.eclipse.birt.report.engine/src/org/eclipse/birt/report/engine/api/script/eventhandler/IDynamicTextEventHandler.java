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
import org.eclipse.birt.report.engine.api.script.element.ITextData;
import org.eclipse.birt.report.engine.api.script.instance.IDynamicTextInstance;

public interface IDynamicTextEventHandler
{
	/**
	 * Handle the onPrepare event
	 */
	void onPrepare( ITextData textData, IReportContext reportContext );

	/**
	 * Handle the onCreate event
	 */
	void onCreate( IDynamicTextInstance text, IReportContext reportContext );

	/**
	 * Handle the onRender event
	 */
	void onRender( IDynamicTextInstance text, IReportContext reportContext );
}
