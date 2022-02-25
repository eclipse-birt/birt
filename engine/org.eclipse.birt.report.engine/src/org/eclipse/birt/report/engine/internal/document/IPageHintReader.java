/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

import java.io.IOException;
import java.util.Collection;

import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.presentation.IPageHint;

/**
 * interfaces used to read the page hints.
 *
 */
public interface IPageHintReader extends IPageHintConstant {

	int getVersion();

	void close();

	long getTotalPage() throws IOException;

	Collection<PageVariable> getPageVariables() throws IOException;

	IPageHint getPageHint(long pageNumber) throws IOException;

	long getPageOffset(long pageNumber, String masterPage) throws IOException;
}
