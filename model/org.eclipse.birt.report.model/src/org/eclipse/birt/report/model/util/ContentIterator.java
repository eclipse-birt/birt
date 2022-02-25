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

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Iterator that is used to visit an container element. We go through the given
 * element use <strong>Depth-first</strong> searching algorithm. The iterator
 * guarantees the consistency between several rounds of visiting. That is, a
 * given element will get the same iterating results between two times of
 * iterating, as long as the element is not modified.
 *
 */

public class ContentIterator extends LevelContentIterator {

	/**
	 * The maximal level.
	 */

	protected static final int MAX_LEVEL = Integer.MAX_VALUE;

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given <code>element</code>
	 *
	 * @param module
	 *
	 * @param element the element to visit.
	 */

	public ContentIterator(Module module, DesignElement element) {
		super(module, element, LevelContentIterator.MAX_LEVEL);
	}

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given slot id of the given <code>element</code>
	 *
	 * @param module        module where contents reside.
	 * @param containerInfo container infor to traverse
	 */

	public ContentIterator(Module module, ContainerContext containerInfo) {
		super(module, containerInfo, LevelContentIterator.MAX_LEVEL);
	}
}
