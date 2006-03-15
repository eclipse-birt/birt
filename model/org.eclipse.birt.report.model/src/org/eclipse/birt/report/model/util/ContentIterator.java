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

package org.eclipse.birt.report.model.util;

import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Iterator that is used to visit an container element. We go through the given
 * element use <strong>Depth-first</strong> searching algorithm. The iterator
 * guarantees the consistency between several rounds of visiting. That is, a
 * given element will get the same iterating results between two times of
 * iterating, as long as the element is not modified.
 * 
 */

public class ContentIterator extends LevelContentIterator
{

	/**
	 * The maximal level.
	 */

	protected static final int MAX_LEVEL = Integer.MAX_VALUE;

	/**
	 * List of content elements.
	 */

	List elementContents = null;

	/**
	 * Current iteration position.
	 */

	protected int posn = 0;

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given <code>element</code>
	 * 
	 * @param element
	 *            the element to visit.
	 */

	public ContentIterator( DesignElement element )
	{
		super( element, LevelContentIterator.MAX_LEVEL );
	}

	/**
	 * Constructs a iterator that will visit all the content element within the
	 * given slot id of the given <code>element</code>
	 * 
	 * @param element
	 *            the element to visit.
	 * @param slotId
	 *            the slot id
	 */

	public ContentIterator( DesignElement element, int slotId )
	{
		super( element, slotId, LevelContentIterator.MAX_LEVEL );
	}
}
