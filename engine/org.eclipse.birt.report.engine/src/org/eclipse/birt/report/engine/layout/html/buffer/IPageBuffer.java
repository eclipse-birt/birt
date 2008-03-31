/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html.buffer;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;

public interface IPageBuffer
{

	public void startContainer( IContent content, boolean isFirst,
			IContentEmitter emitter, boolean visible );

	public void endContainer( IContent content, boolean finished,
			IContentEmitter emitter, boolean visible );
	
	
	public void startContent( IContent content, IContentEmitter emitter, boolean visible);

	public void setRepeated( boolean isRepeated );

	public boolean isRepeated( );
	
	public void flush();
	
	public boolean finished();
	
	public void openPage(IContent[] contentList, IContentEmitter emitter);
	
	public void closePage(IContent[] contentList, IContentEmitter emitter);
	
	public IContent[] getContentStack();
	
	public void addTableColumnHint(TableColumnHint hint);
}
