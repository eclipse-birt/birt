/*
 *************************************************************************
 * Copyright (c) 2009, 2010 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.datasource;

import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl.PropertyAdapter;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider;

/**
 * The provider of the effective database profile properties. It is used for
 * converting local ODA data source properties to those that can be used in a
 * DTP database profile instance, and vice versa.
 */
public class DbProfilePropertyProvider implements IPropertyProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider#
	 * getDataSourceProperties(java.util.Properties, java.lang.Object)
	 */
	@Override
	public Properties getDataSourceProperties(Properties candidateProperties, Object appContext) throws OdaException {
		// exporting data source properties to be saved in an external db profile
		return PropertyAdapter.adaptToDbProfilePropertyNames(candidateProperties);
	}

	static Properties adaptToDbProfileProperties(Properties connProperties) {
		return PropertyAdapter.adaptToDbProfilePropertyNames(connProperties);
	}

	static Properties adaptToDataSourceProperties(Properties profileProperties) {
		return PropertyAdapter.adaptToDataSourcePropertyNames(profileProperties);
	}

}
