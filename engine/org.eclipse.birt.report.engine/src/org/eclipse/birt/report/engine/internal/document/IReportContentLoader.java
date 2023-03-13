/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

/**
 * used to load the contents from the report document.
 *
 */
public interface IReportContentLoader {

	/**
	 * HTML multi pages. This flag is set when output format is "html" and
	 * HTMLPagintion is set to false.
	 */
	int SINGLE_PAGE = 2;
	/**
	 * HTML multi pages. This flag is set when output format is "html" and
	 * HTMLPagintion is set to true.
	 */
	int MULTI_PAGES = 1;
	/**
	 * Pagination type when output format is "pdf".
	 */
	int NO_PAGE = 0;

	/**
	 * load the page from the content stream and output it to the emitter
	 *
	 * @param pageNumber
	 * @param paginationType
	 * @param emitter
	 */
	void loadPage(long pageNumber, int paginationType, IContentEmitter emitter) throws BirtException;

	/**
	 * load the page from the content stream and output it to the emitter
	 *
	 * @param pageNumber
	 * @param paginationType
	 * @param emitter
	 * @throws BirtException
	 */
	void loadPageRange(List pageList, int paginationType, IContentEmitter emitter) throws BirtException;

	/**
	 * the the content at position offset.
	 *
	 * @param offset
	 * @param emitter
	 */
	void loadReportlet(long offset, IContentEmitter emitter) throws BirtException;

}
