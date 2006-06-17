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
package org.eclipse.birt.report.engine.layout.area;

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;

public interface IAreaVisitor
{
	void start(IReportContent report);
	
	String getOutputFormat( );

	void initialize(IEmitterServices service);
	
	void visitText(ITextArea textArea);
	
	void visitAutoText(ITemplateArea templateArea);
	
	void setTotalPage(ITextArea totalPage);
	
	void visitImage(IImageArea imageArea);
	
	void startContainer(IContainerArea containerArea);
	
	void endContainer(IContainerArea containerArea);
	
	void end(IReportContent report);
			
}
