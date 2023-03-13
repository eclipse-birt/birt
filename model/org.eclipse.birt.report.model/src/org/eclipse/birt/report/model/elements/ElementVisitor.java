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

package org.eclipse.birt.report.model.elements;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * The base class for defining algorithms that traverse the design tree. The
 * derived class overrides the various <code>visitMumble</code> methods to
 * perform tasks specific to that element.
 *
 */

public class ElementVisitor extends ElementVisitorImpl {

	/**
	 * Visits the contents of the given context. Allows a derived class to traverse
	 * downward though the design tree.
	 *
	 * @param module  the module where the contents reside
	 * @param context the container context where the contents reside
	 */

	public void visitContents(Module module, ContainerContext context) {
		List<DesignElement> contents = context.getContents(module);
		Iterator<DesignElement> iter = contents.iterator();
		while (iter.hasNext()) {
			(iter.next()).apply(this);
		}
	}
}
