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

package org.eclipse.birt.report.engine.content.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.report.engine.content.IElement;

public class AbstractElement implements IElement {

	final static List EMPTY_CHILDREN_LIST = new ArrayList();
	transient protected IElement parent;
	transient protected Collection children;

	public AbstractElement() {
	}

	public IElement getParent() {
		return parent;
	}

	public void setParent(IElement parent) {
		this.parent = parent;
	}

	public Collection getChildren() {
		if (children == null) {
			children = new ArrayList();
		}
		return children;
	}

}