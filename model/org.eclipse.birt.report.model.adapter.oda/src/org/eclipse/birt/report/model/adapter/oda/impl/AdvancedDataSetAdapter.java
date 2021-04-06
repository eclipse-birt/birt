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

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousOption;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.OdaDesignSession;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.Property;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Advanced data set adapter to provide some new update method and comparison
 * method.
 */
class AdvancedDataSetAdapter extends DataSetAdapter {

	AdvancedDataSetAdapter() {
		super();
	}

	/**
	 * 
	 * @param setDesign
	 * @param setHandle
	 * @param parameterList
	 * @param resultSetList
	 * @param isSourceChanged
	 * @throws SemanticException
	 */
	void updateDataSetHandle(DataSetDesign setDesign, OdaDataSetHandle setHandle,
			List<OdaDataSetParameter> parameterList, List<OdaResultSetColumn> resultSetList, boolean isSourceChanged)
			throws SemanticException {
		if (setDesign == null || setHandle == null)
			return;

		updateDataSetHandle(setDesign, setHandle, isSourceChanged, parameterList, resultSetList);
	}

	/**
	 * 
	 * @param setHandle
	 * @param completedSession
	 * @param parameterList
	 * @param resultSetList
	 * @throws SemanticException
	 */
	void updateDataSetHandle(OdaDataSetHandle setHandle, OdaDesignSession completedSession,
			List<OdaDataSetParameter> parameterList, List<OdaResultSetColumn> resultSetList) throws SemanticException {
		if (completedSession == null || setHandle == null)
			return;

		DataSetDesign responseDesign = completedSession.getResponseDataSetDesign();

		updateDataSetHandle(responseDesign, setHandle, false, parameterList, resultSetList);

		/*
		 * DesignerStateAdapter.updateROMDesignerState( completedSession .getResponse(
		 * ).getDesignerState( ), setHandle );;
		 */
	}

	/**
	 * 
	 * @param setDesign
	 * @param setHandle
	 * @return
	 */
	IAmbiguousOption getAmbiguousOption(DataSetDesign setDesign, OdaDataSetHandle setHandle) {
		AmbiguousOption option = new AmbiguousOption();
		if (setDesign == null || setHandle == null)
			return option;

		// check parameters
		DataSetParametersChecker paramsChecker = new DataSetParametersChecker(setDesign, setHandle);
		option.setAmbiguousParameters(paramsChecker.process());

		// check result sets
		ResultSetsChecker resultsetsChecker = new ResultSetsChecker(setDesign, setHandle);
		option.setAmbiguousResultSets(resultsetsChecker.process());
		return option;
	}

	/**
	 * Updates the data set handle with specified values.
	 * 
	 * @param setDesign         the data set design
	 * @param setHandle         the data set handle
	 * @param isSourceChanged
	 * @param requestParameters
	 * @param requestResultSets
	 * @throws SemanticException
	 */

	private void updateDataSetHandle(DataSetDesign setDesign, OdaDataSetHandle setHandle, boolean isSourceChanged,
			List<OdaDataSetParameter> parameterList, List<OdaResultSetColumn> resultSetList) throws SemanticException {
		if (setDesign == null || setHandle == null)
			return;

		// validate the set design first
		DesignUtil.validateObject(setDesign);

		CommandStack stack = setHandle.getModuleHandle().getCommandStack();

		stack.startTrans(null);
		try {
			// extension id is set without undo/redo support.

			setHandle.getElement().setProperty(OdaDataSourceHandle.EXTENSION_ID_PROP,
					setDesign.getOdaExtensionDataSetId());

			setHandle.setName(setDesign.getName());
			setHandle.setDisplayName(setDesign.getDisplayName());

			// set public properties.

			Properties props = setDesign.getPublicProperties();
			if (props != null) {
				EList propList = props.getProperties();
				for (int i = 0; i < propList.size(); i++) {
					Property prop = (Property) propList.get(i);
					setHandle.setProperty(prop.getName(), prop.getValue());
				}
			}

			// set private properties.

			props = setDesign.getPrivateProperties();
			if (props != null) {
				EList propList = props.getProperties();
				for (int i = 0; i < propList.size(); i++) {
					Property prop = (Property) propList.get(i);
					setHandle.setPrivateDriverProperty(prop.getName(), prop.getValue());
				}
			}

			// update result set column and column hints
			ResultSetsUpdater resultUpdater = new ResultSetsUpdater(setDesign, setHandle, resultSetList);
			resultUpdater.processResultSets(setDesign.getResultSets());

			setHandle.setResultSetName(setDesign.getPrimaryResultSetName());

			setHandle.setQueryText(setDesign.getQueryText());

			// designer values must be saved after convert data set parameters
			// and result set columns.

			// Set Parameter

			// update parameters in the given list
			DataSetParametersUpdater updater = new DataSetParametersUpdater(setDesign, setHandle, parameterList);
			updater.processDataSetParameters(setDesign.getParameters());

			DataSourceDesign sourceDesign = setDesign.getDataSourceDesign();
			if (sourceDesign != null) {
				OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) setHandle.getDataSource();

				DataSourceAdapter dataSourceAdapter = new DataSourceAdapter();

				// only the local data source can be used.

				if (isSourceChanged && sourceHandle != null && !sourceHandle.getModuleHandle().isReadOnly()) {
					setHandle.setDataSource(sourceDesign.getName());
					dataSourceAdapter.updateDataSourceHandle(sourceDesign, sourceHandle);
				}

				// if the source is not changed, and it is not in the included
				// library, then we can update it.

				if (!isSourceChanged && sourceHandle != null && !sourceHandle.getModuleHandle().isReadOnly()
						&& !(dataSourceAdapter.isEqualDataSourceDesign(
								dataSourceAdapter.createDataSourceDesign(sourceHandle), sourceDesign))) {
					dataSourceAdapter.updateDataSourceHandle(sourceDesign, sourceHandle);
				}
			} else
				setHandle.setDataSource(null);

			// updateDesignerValue( setDesign, setHandle, requestParameters,
			// dataParamAdapter.getUserDefinedParams( ), requestResultSets );
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

}
