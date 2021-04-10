/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.toc;

import java.io.IOException;

public interface ITOCWriter {

	ITreeNode getTree();

	void startTOCEntry(TOCEntry entry) throws IOException;

	void closeTOCEntry(TOCEntry entry) throws IOException;

	void close() throws IOException;
}
