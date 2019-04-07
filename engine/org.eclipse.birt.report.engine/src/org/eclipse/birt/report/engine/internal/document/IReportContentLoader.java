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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

/**
 * used to load the contents from the report document.
 *
 */
public interface IReportContentLoader
{

	/**
	 * HTML multi pages. This flag is set when output format is "html" and
	 * HTMLPagintion is set to false.
	 */
	public static final int SINGLE_PAGE = 2;
	/**
	 * HTML multi pages. This flag is set when output format is "html" and
	 * HTMLPagintion is set to true.
	 */
	public static final int MULTI_PAGES = 1;
	/**
	 * Pagination type when output format is "pdf".
	 */
	public static final int NO_PAGE = 0;

	/**
	 * load the page from the content stream and output it to the emitter
	 * 
	 * @param pageNumber
	 * @param paginationType
	 * @param emitter
	 */
	public void loadPage( long pageNumber, int paginationType,
			IContentEmitter emitter ) throws BirtException;

	/**
	 * load the page from the content stream and output it to the emitter
	 * 
	 * @param pageNumber
	 * @param paginationType
	 * @param emitter
	 * @throws BirtException 
	 */
	public void loadPageRange( List pageList, int paginationType,
			IContentEmitter emitter ) throws BirtException;
	
	/**
	 * the the content at position offset.
	 * @param offset
	 * @param emitter
	 */
	public void loadReportlet( long offset, IContentEmitter emitter )
			throws BirtException;

}
