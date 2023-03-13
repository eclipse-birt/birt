/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.toc;

import java.util.Collection;

public interface ITreeNode {

	String getNodeId();

	Collection<ITreeNode> getChildren();

	boolean isGroup();

	String getHiddenFormats();

	Object getTOCValue();

	long getElementId();

	String getBookmark();
}
