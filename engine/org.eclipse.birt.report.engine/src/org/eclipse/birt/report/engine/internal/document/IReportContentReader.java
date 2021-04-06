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

import java.io.IOException;

import org.eclipse.birt.report.engine.content.IContent;

/**
 * reader the content from the content stream.
 *
 */
public interface IReportContentReader {
	public void open(String name) throws IOException;

	public void close();

	public void setOffset(long offset);

	public long getOffset();

	public IContent readContent() throws IOException;
}
