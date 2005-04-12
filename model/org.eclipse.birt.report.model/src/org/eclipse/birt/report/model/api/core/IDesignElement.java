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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.elements.ReportDesign;


/**
 * Interface for all the design elements.
 */

public interface IDesignElement extends Cloneable
{
	/**
	 * Returns the definition object for this element.
	 * <p>
	 * Part of: Meta data system.
	 * 
	 * @return The element definition. Will always be non-null in a valid build.
	 */

	public IElementDefn getDefn( );
	
	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle for this element.
	 */

	public DesignElementHandle getHandle( ReportDesign design );
}
