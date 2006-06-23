/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;


public interface IReportLayoutEngine
{
	void setPageHandler( ILayoutPageHandler handle );
	
	void layout(IReportExecutor executor, IContentEmitter output, boolean pagination);
	
	void layout(IContent content, IContentEmitter output);
	
	void layout(IReportItemExecutor executor, IContentEmitter output);
	
	void cancel();
}
