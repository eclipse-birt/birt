/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ooxml;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public interface IPart extends IPartContainer {

	String getRelativeUri();

	Package getPackage();

	ContentType getContentType();

	OutputStream getCacheOutputStream() throws IOException;

	OutputStream getOutputStream() throws IOException;

	String getAbsoluteUri();

	String getRelationshipId();

	String getRelationshipUri();

	String getRelationshipType();

	OOXmlWriter getCacheWriter() throws IOException;

	OOXmlWriter getWriter() throws IOException;

	String getHyperlinkId(String url);

	String getExternalImageId(String url);

	boolean isCached();

	boolean isReference();

	String getBookmarkId(String bmkurl);
}
