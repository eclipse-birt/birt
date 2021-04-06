/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
