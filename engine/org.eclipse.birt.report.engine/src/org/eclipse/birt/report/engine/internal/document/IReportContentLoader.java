/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document;

import java.util.List;

import org.eclipse.birt.report.engine.emitter.IContentEmitter;

/**
 * used to load the contents from the report document.
 *
 * @version $Revision:$ $Date:$
 */
public interface IReportContentLoader
{

	/**
	 * load the page from the content stream and output it to the emitter
	 * 
	 * @param pageNumber
	 * @param emitter
	 */
	public void loadPage( long pageNumber, boolean bodyOnly,
			IContentEmitter emitter );

	/**
	 * load the page from the content stream and output it to the emitter
	 * 
	 * @param pageNumber
	 * @param emitter
	 */
	public void loadPageRange( List pageList, boolean bodyOnly,
			IContentEmitter emitter );
	
	/**
	 * the the content at position offset.
	 * @param offset
	 * @param emitter
	 */
	public void loadReportlet(long offset, IContentEmitter emitter);

}
