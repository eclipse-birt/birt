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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;

/**
 * Represents the extension element definition for ODA.
 */

public final class ODAExtensionElementDefn extends ExtensionElementDefn
{

	/**
	 * Constructs the add-on extension element definition with element
	 * definition name and base element definition.
	 * 
	 * @param baseElementDefn
	 *            definition of the base element, from which this extension
	 *            element definition extends.
	 */

	public ODAExtensionElementDefn( IElementDefn baseElementDefn )
	{
		assert baseElementDefn != null;

		this.name = baseElementDefn.getName( );
		this.displayNameKey = (String) baseElementDefn.getDisplayNameKey( );
		this.nameOption = MetaDataConstants.REQUIRED_NAME;
		this.allowExtend = false;
		this.extendsFrom = baseElementDefn.getName( );
	}

	/**
	 * Builds element definition.
	 * 
	 * @throws MetaDataException
	 *             if error occurs in building
	 */

	public void buildDefinition( ) throws MetaDataException
	{
		build( );
	}
}