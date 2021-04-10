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

package org.eclipse.birt.report.model.parser.treebuild;

/**
 * Represents the content tree of a trunk of xml file.
 */

public class ContentTree extends ContentNode {

	/**
	 * Default constructor.
	 */

	public ContentTree() {
		super(null);
	}

	/**
	 * Determines whether the tree is empty.
	 * 
	 * @return tree if it is empty, otherwise false
	 */

	public boolean isEmpty() {
		return getChildren().isEmpty();
	}
}
