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

import java.util.Set;

import org.eclipse.birt.integration.wtp.ui.internal.wizards.BirtWizardUtil;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * Implement DataModel provider for birt project facet
 */
public class BirtFacetInstallDataModelProvider extends WebFacetInstallDataModelProvider
		implements BirtFacetInstallDataModelProperties, IBirtFacetConstants {

	/**
	 * @see org.eclipse.jst.j2ee.web.project.facet.WebFacetInstallDataModelProvider#getPropertyNames()
	 */
	public Set getPropertyNames() {
		Set names = super.getPropertyNames();
		names.add(BIRT_CONFIG);
		return names;
	}

	/**
	 * Creates the install data model, and initialize the birt properties in
	 * BIRT_CONFIG.
	 * 
	 * @see org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider#create()
	 */
	public Object create() {
		IDataModel dataModel = (IDataModel) super.create();
		dataModel.setProperty(FACET_ID, BIRT_RUNTIME_FACET_ID);
		dataModel.setProperty(BIRT_CONFIG, BirtWizardUtil.initWebapp(null));
		// TODO: define all the birt properties as nested data models
		return dataModel;
	}

}
