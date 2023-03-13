/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api;

/**
 * An interface implemented by app developer to provide handler after each page
 * is generated in factoery. Can be used to support checkpointing, and therefore
 * progressive viewing.
 */
public interface IPageHandler {
	/**
	 * @param pageNumber page indexed by pageNumber has finished generation
	 * @param checkpoint whether the page indexed by pageNumber is ready for viewing
	 */
	void onPage(int pageNumber, boolean checkpoint, IReportDocumentInfo doc);
}
