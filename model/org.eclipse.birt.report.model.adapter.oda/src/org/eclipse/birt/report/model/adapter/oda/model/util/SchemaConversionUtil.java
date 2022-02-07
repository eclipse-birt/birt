/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.model.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter;
import org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters;
import org.eclipse.birt.report.model.adapter.oda.model.DynamicList;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class SchemaConversionUtil {

	/**
	 * @param designParams
	 * @return
	 */

	public static DataSetParameters convertToAdapterParameters(
			org.eclipse.datatools.connectivity.oda.design.DataSetParameters designParams) {
		if (designParams == null)
			return null;

		DataSetParameters adapterParams = ModelFactory.eINSTANCE.createDataSetParameters();

		int itemNum = designParams.getParameterDefinitions().size();
		for (int i = 0; i < itemNum; i++) {
			DataSetParameter adapterParam = ModelFactory.eINSTANCE.createDataSetParameter();
			adapterParam.setParameterDefinition(EcoreUtil.copy(designParams.getParameterDefinitions().get(i)));

			adapterParams.getParameters().add(adapterParam);
		}
		return adapterParams;
	}

	/**
	 * @param adapterParams
	 * @return
	 */

	public static org.eclipse.datatools.connectivity.oda.design.DataSetParameters convertToDesignParameters(
			DataSetParameters adapterParams) {
		if (adapterParams == null)
			return null;

		IODADesignFactory designFactory = ODADesignFactory.getFactory();

		org.eclipse.datatools.connectivity.oda.design.DataSetParameters designParams = designFactory
				.createDataSetParameters();

		int itemNum = adapterParams.getParameters().size();
		for (int i = 0; i < itemNum; i++) {
			DataSetParameter adapterParam = adapterParams.getParameters().get(i);

			designParams.getParameterDefinitions().add(EcoreUtil.copy(adapterParam.getParameterDefinition()));
		}
		return designParams;
	}

	/**
	 * @param adapterParams
	 * @return
	 */

	public static List<DynamicList> getCachedDynamicList(DataSetParameters adapterParams) {
		if (adapterParams == null)
			return null;

		int itemNum = adapterParams.getParameters().size();
		List<DynamicList> retList = new ArrayList<DynamicList>(itemNum);

		for (int i = 0; i < itemNum; i++) {
			DataSetParameter adapterParam = adapterParams.getParameters().get(i);

			retList.add(EcoreUtil.copy(adapterParam.getDynamicList()));
		}
		return retList;
	}
}
