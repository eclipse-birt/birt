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
