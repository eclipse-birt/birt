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

	@Override
	public IElement getParent() {
		return parent;
	}

	@Override
	public void setParent(IElement parent) {
		this.parent = parent;
	}

	@Override
	public Collection getChildren() {
		if (children == null) {
			children = new ArrayList();
		}
		return children;
	}

}
