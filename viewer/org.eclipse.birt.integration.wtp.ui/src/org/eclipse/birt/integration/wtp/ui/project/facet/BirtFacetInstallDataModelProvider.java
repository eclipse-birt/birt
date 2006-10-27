/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.project.facet;

import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * Implement DataModel provider for birt project facet
 * 
 */
public class BirtFacetInstallDataModelProvider
		extends
			WebFacetInstallDataModelProvider implements IBirtFacetConstants
{

	/**
	 * Create IDataModel
	 * 
	 * @see org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider#create()
	 */
	public Object create( )
	{
		IDataModel dataModel = (IDataModel) super.create( );

		// Add facet id of birt runtime
		dataModel.setProperty( "IFacetDataModelProperties.FACET_ID", //$NON-NLS-1$
				BIRT_RUNTIME_FACET_ID );
		
		return dataModel;
	}
}
