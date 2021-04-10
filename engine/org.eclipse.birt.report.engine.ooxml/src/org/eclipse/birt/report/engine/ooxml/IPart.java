/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
