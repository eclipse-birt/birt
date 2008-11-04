/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.index;

import java.io.IOException;

public interface IDocumentIndexWriter extends IDocumentIndexVersion
{

	void setPageOfBookmark( String bookmark, long pageNumber )
			throws IOException;

	void setOffsetOfBookmark( String bookmark, long offset ) throws IOException;

	void setOffsetOfInstance( String instanceId, long offset )
			throws IOException;

	void close( ) throws IOException;
}
