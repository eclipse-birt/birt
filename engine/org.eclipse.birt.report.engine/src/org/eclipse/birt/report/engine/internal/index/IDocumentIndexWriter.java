/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.index;

import java.io.IOException;

import org.eclipse.birt.report.engine.content.impl.BookmarkContent;

public interface IDocumentIndexWriter extends IDocumentIndexVersion {

	void setBookmark(String bookmark, BookmarkContent content) throws IOException;

	void setOffsetOfBookmark(String bookmark, long offset) throws IOException;

	void setOffsetOfInstance(String instanceId, long offset) throws IOException;

	void close() throws IOException;
}
