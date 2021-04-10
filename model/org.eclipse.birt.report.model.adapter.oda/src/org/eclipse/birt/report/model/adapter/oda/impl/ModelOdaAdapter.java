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
import org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDesignerStateHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.OdaDesignSession;

/**
 * An adapter class that converts between ROM OdaDataSourceHandle and ODA
 * DataSourceDesign.
 * 
 * @see OdaDataSourceHandle
 * @see DataSourceDesign
 */

public class ModelOdaAdapter implements IModelOdaAdapter {

	/**
	 * Constructs a DesignEngine with the given platform config.
	 * 
	 * @param config the platform config.
	 */

	public ModelOdaAdapter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * createDataSourceDesign
	 * (org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public DataSourceDesign createDataSourceDesign(OdaDataSourceHandle sourceHandle) {
		return new DataSourceAdapter().createDataSourceDesign(sourceHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * createDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public DataSetDesign createDataSetDesign(OdaDataSetHandle setHandle) {
		return new DataSetAdapter().createDataSetDesign(setHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * createDataSetHandle
	 * (org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 * org.eclipse.birt.report.model.api.ModuleHandle)
	 */

	public OdaDataSetHandle createDataSetHandle(DataSetDesign setDesign, ModuleHandle module)
			throws SemanticException, IllegalStateException {
		return new DataSetAdapter().createDataSetHandle(setDesign, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 * org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */

	public void updateDataSetDesign(OdaDataSetHandle setHandle, DataSetDesign setDesign) {
		new DataSetAdapter().updateDataSetDesign(setHandle, setDesign);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 * org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 * java.lang.String)
	 */

	public void updateDataSetDesign(OdaDataSetHandle setHandle, DataSetDesign setDesign, String propertyName) {
		new DataSetAdapter().updateDataSetDesign(setHandle, setDesign, propertyName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSourceDesign
	 * (org.eclipse.birt.report.model.api.OdaDataSourceHandle,
	 * org.eclipse.datatools.connectivity.oda.design.DataSourceDesign)
	 */

	public void updateDataSourceDesign(OdaDataSourceHandle sourceHandle, DataSourceDesign sourceDesign) {
		new DataSourceAdapter().updateDataSourceDesign(sourceHandle, sourceDesign);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * createDataSourceHandle
	 * (org.eclipse.datatools.connectivity.oda.design.DataSourceDesign,
	 * org.eclipse.birt.report.model.api.ModuleHandle)
	 */

	public OdaDataSourceHandle createDataSourceHandle(DataSourceDesign sourceDesign, ModuleHandle module)
			throws SemanticException, IllegalStateException {
		return new DataSourceAdapter().createDataSourceHandle(sourceDesign, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSourceHandle
	 * (org.eclipse.datatools.connectivity.oda.design.DataSourceDesign,
	 * org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public void updateDataSourceHandle(DataSourceDesign sourceDesign, OdaDataSourceHandle sourceHandle)
			throws SemanticException {
		new DataSourceAdapter().updateDataSourceHandle(sourceDesign, sourceHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSetHandle
	 * (org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 * org.eclipse.birt.report.model.api.OdaDataSetHandle, boolean)
	 */

	public void updateDataSetHandle(DataSetDesign setDesign, OdaDataSetHandle setHandle, boolean isSourceChanged)
			throws SemanticException {
		new DataSetAdapter().updateDataSetHandle(setDesign, setHandle, isSourceChanged);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * newOdaDesignerState(org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public DesignerState newOdaDesignerState(OdaDataSetHandle setHandle) {
		OdaDesignerStateHandle designerState = setHandle.getDesignerState();

		return DesignerStateAdapter.createOdaDesignState(designerState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateROMDesignerState
	 * (org.eclipse.datatools.connectivity.oda.design.DesignerState,
	 * org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public void updateROMDesignerState(DesignerState designerState, OdaDataSetHandle setHandle)
			throws SemanticException {
		if (designerState == null || setHandle == null)
			return;

		DesignerStateAdapter.updateROMDesignerState(designerState, setHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * newOdaDesignerState (org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public DesignerState newOdaDesignerState(OdaDataSourceHandle sourceHandle) {
		OdaDesignerStateHandle designerState = sourceHandle.getDesignerState();

		return DesignerStateAdapter.createOdaDesignState(designerState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateROMDesignerState
	 * (org.eclipse.datatools.connectivity.oda.design.DesignerState,
	 * org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public void updateROMDesignerState(DesignerState designerState, OdaDataSourceHandle sourceHandle)
			throws SemanticException {
		if (designerState == null || sourceHandle == null)
			return;

		DesignerStateAdapter.updateROMDesignerState(designerState, sourceHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * isEqualDataSourceDesign
	 * (org.eclipse.datatools.connectivity.oda.design.DataSourceDesign,
	 * org.eclipse.datatools.connectivity.oda.design.DataSourceDesign)
	 */

	public boolean isEqualDataSourceDesign(DataSourceDesign designFromHandle, DataSourceDesign design) {
		return new DataSourceAdapter().isEqualDataSourceDesign(designFromHandle, design);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * createOdaDesignSession (org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public OdaDesignSession createOdaDesignSession(OdaDataSetHandle dataSetHandle) {
		return new DataSetAdapter().createOdaDesignSession(dataSetHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSetHandle(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 * org.eclipse.datatools.connectivity.oda.design.OdaDesignSession)
	 */

	public void updateDataSetHandle(OdaDataSetHandle dataSetHandle, OdaDesignSession completedSession)
			throws SemanticException {
		new DataSetAdapter().updateDataSetHandle(dataSetHandle, completedSession);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#getAmbiguousOption
	 * (org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 * org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */
	public IAmbiguousOption getAmbiguousOption(DataSetDesign setDesign, OdaDataSetHandle setHandle) {
		return new AdvancedDataSetAdapter().getAmbiguousOption(setDesign, setHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSetHandle
	 * (org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 * org.eclipse.birt.report.model.api.OdaDataSetHandle, java.util.List,
	 * java.util.List, boolean)
	 */

	public void updateDataSetHandle(DataSetDesign setDesign, OdaDataSetHandle setHandle,
			List<OdaDataSetParameter> parameterList, List<OdaResultSetColumn> resultSetList, boolean isSourceChanged)
			throws SemanticException {
		new AdvancedDataSetAdapter().updateDataSetHandle(setDesign, setHandle, parameterList, resultSetList,
				isSourceChanged);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSetHandle(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 * org.eclipse.datatools.connectivity.oda.design.OdaDesignSession,
	 * java.util.List, java.util.List)
	 */

	public void updateDataSetHandle(OdaDataSetHandle setHandle, OdaDesignSession completedSession,
			List<OdaDataSetParameter> parameterList, List<OdaResultSetColumn> resultSetList) throws SemanticException {
		new AdvancedDataSetAdapter().updateDataSetHandle(setHandle, completedSession, parameterList, resultSetList);
	}

	/**
	 * 
	 * @param setDesign
	 * @param setHandle
	 * @param isSourceChanged
	 * @param defaultDataSource
	 * @throws SemanticException
	 */
	void updateLinkedParameterDataSetHandle(DataSetDesign setDesign, OdaDataSetHandle setHandle,
			boolean isSourceChanged, DataSourceHandle defaultDataSource) throws SemanticException {
		new DataSetAdapter(defaultDataSource).updateDataSetHandle(setDesign, setHandle, isSourceChanged);
	}

	public OdaDataSetHandle createLinkedParameterDataSetHandle(DataSetDesign setDesign, ModuleHandle module,
			DataSourceHandle defaultDataSource) throws SemanticException, IllegalStateException {
		return new DataSetAdapter(defaultDataSource).createDataSetHandle(setDesign, module);
	}

}
