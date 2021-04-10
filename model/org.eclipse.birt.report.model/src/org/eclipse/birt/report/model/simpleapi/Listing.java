/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.simpleapi.IListing;

/**
 * Implements of Listing
 * 
 */

public class Listing extends MultiRowItem implements IListing {

	/**
	 * Constructor
	 * 
	 * @param listing
	 */
	public Listing(ListingHandle listing) {
		super(listing);
	}

}
