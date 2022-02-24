/*******************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
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
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.data.ui.dataset.PromptParameterDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.DateSetPreferencePage;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousOption;
import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.core.resources.IProject;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.helper.DriverExtensionHelper;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionRequest;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionResponse;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.OdaDesignSession;
import org.eclipse.datatools.connectivity.oda.design.SessionStatus;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;

/**
 * A Utility Class to handle procedures needed to be done <br>
 * before <code>edit</code> or after <code>finish</code>
 */

public class DTPUtil {

	private static DTPUtil instance = null;
	private SessionStatus sessionStatus = null;
	private DesignerState designerState = null;
	private ModelOdaAdapter modelOdaAdapter = new ModelOdaAdapter();
	private static final String SAMPELDB_DATA_SOURCE_ID = "org.eclipse.birt.report.data.oda.sampledb"; //$NON-NLS-1$
	private static final String JDBC_DATA_SOURCE_ID = "org.eclipse.birt.report.data.oda.jdbc"; //$NON-NLS-1$

	private static Logger logger = Logger.getLogger(DTPUtil.class.getName());

	private DTPUtil() {
	}

	public static synchronized DTPUtil getInstance() {
		if (instance == null) {
			instance = new DTPUtil();
		}

		return instance;
	}

	/**
	 * update DataSourceHandle
	 *
	 * @param response
	 * @param dataSourceHandle
	 */
	public void updateDataSourceHandle(DesignSessionResponse response, DataSourceDesign requestDesign,
			OdaDataSourceHandle dataSourceHandle) {
		initDesignSessionFields(response);
		if (isSessionOk()) {
			if (new EcoreUtil.EqualityHelper().equals(requestDesign, response.getDataSourceDesign())) {
				return;
			}
			try {
				updateROMDesignerState(dataSourceHandle);
				modelOdaAdapter.updateDataSourceHandle(response.getDataSourceDesign(), dataSourceHandle);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		}
	}

	/**
	 * update DataSetHandle
	 *
	 * @param response
	 * @param dataSetHandle
	 * @param isSourceChanged
	 */
	public void updateDataSetHandle(OdaDesignSession designSession, OdaDataSetHandle dataSetHandle) {
		DataSetDesign requestDesign = designSession.getRequestDataSetDesign();
		DesignSessionResponse response = designSession.getResponse();

		initDesignSessionFields(response);
		if (isSessionOk()) {
			if (new EcoreUtil.EqualityHelper().equals(requestDesign, response.getDataSetDesign())) {
				return;
			}

			try {
				modelOdaAdapter.updateDataSetHandle(dataSetHandle, designSession);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		}
	}

	/**
	 * update DataSetHandle
	 *
	 * @param response
	 * @param dataSetHandle
	 * @param isSourceChanged
	 */
	public void updateDataSetHandle(DesignSessionResponse response, DataSetDesign requestDesign,
			OdaDataSetHandle dataSetHandle, boolean isSourceChanged) {
		initDesignSessionFields(response);
		if (isSessionOk()) {
			EcoreUtil.EqualityHelper equalityHelper = new EcoreUtil.EqualityHelper();
			if (equalityHelper.equals(response.getDataSetDesign(), requestDesign)
					&& equalityHelper.equals(response.getDesignerState(), this.designerState)) {
				return;
			}
			try {
				DataSetDesign design = response.getDataSetDesign();

				// Check hint in column definition
				if (design.getPrimaryResultSet() != null
						&& design.getPrimaryResultSet().getResultSetColumns() != null) {
					List<ColumnDefinition> resultColumnDefinitions = design.getPrimaryResultSet().getResultSetColumns()
							.getResultColumnDefinitions();
					if (resultColumnDefinitions != null && !resultColumnDefinitions.isEmpty()) {
						Object hint = resultColumnDefinitions.get(0).getUsageHints();
						if (hint == null) {
							design.setPrimaryResultSet(null);
							design.setResultSets(null);
						}
					}
				}

				if (ReportPlugin.getDefault().getPluginPreferences()
						.getBoolean(DateSetPreferencePage.PROMPT_ENABLE)) {
					IAmbiguousOption ambiguousOption = modelOdaAdapter.getAmbiguousOption(design, dataSetHandle);
					if (ambiguousOption != null && !ambiguousOption.getAmbiguousParameters().isEmpty()) {
						PromptParameterDialog dialog = new PromptParameterDialog(
								Messages.getString("PromptParameterDialog.title"));
						dialog.setInput(ambiguousOption);
						if (dialog.open() == Dialog.OK) {
							Object result = dialog.getResult();
							if (result instanceof List) {
								List<OdaDataSetParameter> selectedParameters = (List) result;
								updateROMDesignerState(dataSetHandle);
								modelOdaAdapter.updateDataSetHandle(design, dataSetHandle, selectedParameters, null,
										isSourceChanged);
								refreshCachedMetaData(dataSetHandle);
								return;
							}
						} else {
							updateROMDesignerState(dataSetHandle);
							modelOdaAdapter.updateDataSetHandle(design, dataSetHandle, isSourceChanged);
							refreshCachedMetaData(dataSetHandle);
							return;
						}
					}
				}
				updateROMDesignerState(dataSetHandle);
				modelOdaAdapter.updateDataSetHandle(design, dataSetHandle, isSourceChanged);
				// update cached meta data
				refreshCachedMetaData(dataSetHandle);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		}
	}

	private void refreshCachedMetaData(OdaDataSetHandle dataSetHandle) throws SemanticException {
		if (dataSetHandle.getCachedMetaDataHandle() != null
				&& dataSetHandle.getCachedMetaDataHandle().getResultSet() != null) {
			dataSetHandle.getCachedMetaDataHandle().getResultSet().clearValue();
		}

		DataSetUIUtil.updateColumnCache(dataSetHandle);
	}

	/**
	 * create OdaDataSourceHandle
	 *
	 * @param response
	 * @param parentHandle
	 * @return
	 * @throws SemanticException
	 */
	public OdaDataSourceHandle createOdaDataSourceHandle(DesignSessionResponse response, ModuleHandle parentHandle)
			throws SemanticException {
		initDesignSessionFields(response);
		OdaDataSourceHandle dataSourceHandle = null;

		if (isSessionOk()) {
			DataSourceDesign dataSourceDesign = response.getDataSourceDesign();
			if (dataSourceDesign.getOdaExtensionId().equals(SAMPELDB_DATA_SOURCE_ID)) {
				dataSourceDesign.setOdaExtensionId(JDBC_DATA_SOURCE_ID);
			}

			try {
				dataSourceHandle = modelOdaAdapter.createDataSourceHandle(dataSourceDesign, parentHandle);
				updateROMDesignerState(dataSourceHandle);
			} catch (SemanticException e) {
				throw e;
			}
		}
		return dataSourceHandle;
	}

	/**
	 * create OdaDataSetHandle
	 *
	 * @param response
	 * @param parentHandle
	 * @return
	 * @throws OdaException
	 * @throws SemanticException
	 */
	public OdaDataSetHandle createOdaDataSetHandle(DesignSessionResponse response, ModuleHandle parentHandle)
			throws SemanticException {
		initDesignSessionFields(response);
		OdaDataSetHandle dataSetHandle = null;

		if (isSessionOk()) {
			try {
				dataSetHandle = modelOdaAdapter.createDataSetHandle(response.getDataSetDesign(), parentHandle);
				updateROMDesignerState(dataSetHandle);
			} catch (SemanticException e) {
				throw e;
			}
		}
		return dataSetHandle;
	}

	/**
	 * create DesignSessionRequest
	 *
	 * @param dataSourceHandle
	 * @return
	 * @throws URISyntaxException
	 */
	public DesignSessionRequest createDesignSessionRequest(OdaDataSourceHandle dataSourceHandle)
			throws URISyntaxException {
		DataSourceDesign dataSourceDesign = modelOdaAdapter.createDataSourceDesign(dataSourceHandle);
		supplementDesignAttributes(dataSourceDesign);

		DesignSessionRequest designSessionRequest = DesignFactory.eINSTANCE
				.createDesignSessionRequest(dataSourceDesign);

		designerState = modelOdaAdapter.newOdaDesignerState(dataSourceHandle);
		if (designerState != null) {
			designSessionRequest.setDesignerState(designerState);
		}

		return designSessionRequest;
	}

	/**
	 * Adds supplemental attributes to complete the specified data source design.
	 *
	 * @param dataSourceDesign
	 * @throws URISyntaxException
	 */
	public void supplementDesignAttributes(DataSourceDesign dataSourceDesign) throws URISyntaxException {
		applyResourceIdentifiers(dataSourceDesign);
		applyEffectiveDataSourceId(dataSourceDesign);
	}

	/**
	 * Applies the ResourceIdentifiers instance to the specified DataSourceDesign
	 *
	 * @param dataSourceDesign
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public void applyResourceIdentifiers(DataSourceDesign dataSourceDesign) throws URISyntaxException {
		if (Utility.getReportModuleHandle() == null) {
			return;
		}
		DesignSessionUtil.setDataSourceResourceIdentifiers(dataSourceDesign, getBIRTResourcePath(),
				getReportDesignPath());
	}

	private void applyEffectiveDataSourceId(DataSourceDesign dataSourceDesign) {
		String odaExtensionId = dataSourceDesign != null ? dataSourceDesign.getOdaExtensionId() : null;
		if (odaExtensionId == null) {
			return; // insufficent info to get seffective data source id
		}

		String effectiveExtensionId;
		try {
			effectiveExtensionId = DriverExtensionHelper.getEffectiveDataSourceId(odaExtensionId);
		} catch (OdaException ex) {
			// log and ignore
			logger.log(Level.INFO, "Unable to determine the effective runtime data source id of " + odaExtensionId + //$NON-NLS-1$
					".  Exception: " + ex.getMessage()); //$NON-NLS-1$
			return;
		}

		if (!odaExtensionId.equalsIgnoreCase(effectiveExtensionId)) {
			dataSourceDesign.setEffectiveOdaExtensionId(effectiveExtensionId);
		}
	}

	/**
	 * Gets the BIRT resource path
	 *
	 * @return
	 * @throws URISyntaxException
	 */
	public URI getReportDesignPath() {
		if (Utility.getReportModuleHandle() == null || Utility.getReportModuleHandle().getSystemId() == null) {
			return null;
		}
		try {
			return Utility.getReportModuleHandle().getSystemId().toURI();
		} catch (URISyntaxException e) {
			return null;
		}
	}

	/**
	 * Gets the report design file path
	 *
	 * @return
	 */
	public URI getBIRTResourcePath() {
		if (getCurrentProject() == null) {
			try {
				ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();
				if (handle != null) {
					String rscFolder = handle.getResourceFolder();
					File file = new File(rscFolder);
					if (file.isAbsolute()) {
						return file.toURI();
					} else {
						try {
							return new URI(rscFolder);
						} catch (URISyntaxException ex) {
							return new URI(null, null, convertURI(rscFolder), null);
						}
					}
				}
			} catch (URISyntaxException e) {
				return null;
			}
		}
		try {
			String rscFolder = ReportPlugin.getDefault().getResourceFolder(getCurrentProject(), (ModuleHandle) null);
			File file = new File(rscFolder);
			if (file.isAbsolute()) {
				return file.toURI();
			}
			try {
				return new URI(rscFolder);
			} catch (URISyntaxException ex) {
				return new URI(null, null, convertURI(rscFolder), null);
			}
		} catch (URISyntaxException e) {
			return null;
		}
	}

	private static String convertURI(String fileURL) {
		return fileURL.replace('\\', '/');
	}

	/**
	 * Guarantee getting current project within the scope of UI thread.
	 *
	 * @return Reference to IProject
	 */
	private IProject getCurrentProject() {
		final IProject[] project = new IProject[1];
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}

		if (display.getThread().equals(Thread.currentThread())) {
			project[0] = UIUtil.getCurrentProject();
		} else {
			display.syncExec(new Runnable() {

				@Override
				public void run() {
					project[0] = UIUtil.getCurrentProject();
				}
			});
		}
		return project[0];
	}

	/**
	 * Create a DesignSessionRequest with the specified dataSetHandle
	 *
	 * @param dataSetHandle
	 * @return
	 */
	public DesignSessionRequest createDesignSessionRequest(OdaDataSetHandle dataSetHandle) {
		return modelOdaAdapter.createOdaDesignSession(dataSetHandle).getRequest();
	}

	/**
	 * Create a DesignSessionRequest with the specified dataSetDesign and
	 * designerState.
	 *
	 * @param dataSetDesign
	 * @param designerState
	 * @return
	 */
	public DesignSessionRequest createDesignSessionRequest(DataSetDesign requestDataSetDesign,
			DesignerState requestDesignerState) {
		DesignSessionRequest newRequest = DesignFactory.eINSTANCE.createDesignSessionRequest(requestDataSetDesign);

		designerState = requestDesignerState;
		newRequest.setDesignerState(requestDesignerState);

		return newRequest;
	}

	/**
	 *
	 * @param dataSetDesign
	 * @param handle
	 */
	public void updateDataSetDesign(DesignSessionResponse response, DataSetHandle handle, String propName) {
		initDesignSessionFields(response);
		if (isSessionOk()) {
			modelOdaAdapter.updateDataSetDesign((OdaDataSetHandle) handle, response.getDataSetDesign(), propName);
		}
	}

	/**
	 * assign values to the fields of current session
	 *
	 * @param response
	 * @throws OdaException
	 */
	private void initDesignSessionFields(DesignSessionResponse response) {
		sessionStatus = response.getSessionStatus();
		designerState = response.getDesignerState();
	}

	/**
	 * check the status of current session
	 *
	 * @throws OdaException
	 */
	private boolean isSessionOk() {
		assert sessionStatus != null;

		if (sessionStatus.getValue() != SessionStatus.OK) {
			logger.log(Level.WARNING, Messages.getFormattedString("dataset.warning.invalidReponseStatus",
					new Object[] { sessionStatus.toString() }));
			return false;
		}
		return true;
	}

	/**
	 * update ROMDesignerState
	 *
	 * @param obj
	 * @throws SemanticException
	 */
	private void updateROMDesignerState(Object obj) throws SemanticException {
		if (designerState == null || obj == null) {
			return;
		}

		if (obj instanceof OdaDataSourceHandle) {
			modelOdaAdapter.updateROMDesignerState(designerState, (OdaDataSourceHandle) obj);
		} else if (obj instanceof OdaDataSetHandle) {
			modelOdaAdapter.updateROMDesignerState(designerState, (OdaDataSetHandle) obj);
		}
	}

	public IAmbiguousOption getAmbiguousOption(DataSetDesign design, OdaDataSetHandle handle) {
		return modelOdaAdapter.getAmbiguousOption(design, handle);
	}

	public ResourceIdentifiers createResourceIdentifiers() {
		ResourceIdentifiers ri = new ResourceIdentifiers();
		ri.setDesignResourceBaseURI(getReportDesignPath());
		ri.setApplResourceBaseURI(getBIRTResourcePath());
		return ri;
	}
}
