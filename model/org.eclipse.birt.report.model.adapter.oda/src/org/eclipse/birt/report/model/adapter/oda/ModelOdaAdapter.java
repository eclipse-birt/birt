/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
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

	private IModelOdaAdapter adapter;

	/**
	 * The logger for errors.
	 */

	protected static final Logger errorLogger = Logger.getLogger(ModelOdaAdapter.class.getName());

	/**
	 * Constructs a DesignEngine with the given platform config.
	 * 
	 * @param config the platform config.
	 */

	public ModelOdaAdapter() {
		Object factory = Platform.createFactoryObject(IAdapterFactory.EXTENSION_MODEL_ADAPTER_ODA_FACTORY);
		if (factory instanceof IAdapterFactory) {
			adapter = ((IAdapterFactory) factory).createModelOdaAdapter();
		}
		if (adapter == null) {
			errorLogger.log(Level.INFO, "Can not start the model adapter oda factory."); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * createDataSourceDesign
	 * (org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public DataSourceDesign createDataSourceDesign(OdaDataSourceHandle sourceHandle) {
		return adapter.createDataSourceDesign(sourceHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * createDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public DataSetDesign createDataSetDesign(OdaDataSetHandle setHandle) {
		return adapter.createDataSetDesign(setHandle);
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
		return adapter.createDataSetHandle(setDesign, module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSetDesign(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 * org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */

	public void updateDataSetDesign(OdaDataSetHandle setHandle, DataSetDesign setDesign) {
		adapter.updateDataSetDesign(setHandle, setDesign);

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
		adapter.updateDataSetDesign(setHandle, setDesign, propertyName);
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
		adapter.updateDataSourceDesign(sourceHandle, sourceDesign);
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
		return adapter.createDataSourceHandle(sourceDesign, module);
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
		adapter.updateDataSourceHandle(sourceDesign, sourceHandle);
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
		adapter.updateDataSetHandle(setDesign, setHandle, isSourceChanged);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * newOdaDesignerState(org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */

	public DesignerState newOdaDesignerState(OdaDataSetHandle setHandle) {
		return adapter.newOdaDesignerState(setHandle);
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
		adapter.updateROMDesignerState(designerState, setHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * newOdaDesignerState (org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */

	public DesignerState newOdaDesignerState(OdaDataSourceHandle sourceHandle) {
		return adapter.newOdaDesignerState(sourceHandle);
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
		adapter.updateROMDesignerState(designerState, sourceHandle);
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
		return adapter.isEqualDataSourceDesign(designFromHandle, design);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * createOdaDesignSession (org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */
	public OdaDesignSession createOdaDesignSession(OdaDataSetHandle dataSetHandle) {
		return adapter.createOdaDesignSession(dataSetHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#
	 * updateDataSetHandle(org.eclipse.birt.report.model.api.OdaDataSetHandle,
	 * org.eclipse.datatools.connectivity.oda.design.OdaDesignSession)
	 */

	public void updateDataSetHandle(OdaDataSetHandle handle, OdaDesignSession completedSession)
			throws SemanticException {
		adapter.updateDataSetHandle(handle, completedSession);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IModelOdaAdapter#getAmbiguousOption
	 * (org.eclipse.datatools.connectivity.oda.design.DataSetDesign,
	 * org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */
	public IAmbiguousOption getAmbiguousOption(DataSetDesign design, OdaDataSetHandle handle) {
		return adapter.getAmbiguousOption(design, handle);
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
		adapter.updateDataSetHandle(setDesign, setHandle, parameterList, resultSetList, isSourceChanged);
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
		adapter.updateDataSetHandle(setHandle, completedSession, parameterList, resultSetList);

	}
}
