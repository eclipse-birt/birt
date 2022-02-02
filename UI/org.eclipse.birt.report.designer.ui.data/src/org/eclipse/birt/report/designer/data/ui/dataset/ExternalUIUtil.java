/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.util.DataUIConstants;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.DerivedDataSet;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;

public class ExternalUIUtil {

	public static void validateDataSetHandle(DataSetHandle ds) {
		if (!(ds instanceof JointDataSetHandle)) {
			if (ds.getDataSource() == null) {
				throw new RuntimeException(Messages.getFormattedString("dataset.editor.error.noDataSource", //$NON-NLS-1$
						new String[] { ds.getQualifiedName() }));
			}
			if ((ds instanceof OdaDataSetHandle && !(ds.getDataSource() instanceof OdaDataSourceHandle))) {
				throw new RuntimeException(Messages.getFormattedString("dataset.editor.error.nonmatchedDataSource", //$NON-NLS-1$
						new String[] { ds.getQualifiedName(), ((OdaDataSetHandle) ds).getExtensionID() }));
			} else if (ds instanceof ScriptDataSetHandle && !(ds.getDataSource() instanceof ScriptDataSourceHandle)) {
				throw new RuntimeException(Messages.getFormattedString("dataset.editor.error.nonmatchedDataSource", //$NON-NLS-1$
						new String[] { ds.getQualifiedName(), DataUIConstants.DATA_SET_SCRIPT }));
			}
		}
	}

	public static void updateColumnCache(DataSetHandle dataSetHandle, boolean holdEvent) throws BirtException {
		if (dataSetHandle.getModuleHandle() instanceof ReportDesignHandle) {
			EngineConfig ec = new EngineConfig();
			ReportEngine engine = (ReportEngine) new ReportEngineFactory().createReportEngine(ec);

			ReportDesignHandle copy = (ReportDesignHandle) (dataSetHandle.getModuleHandle().copy().getHandle(null));

			DummyEngineTask engineTask = new DummyEngineTask(engine,
					new ReportEngineHelper(engine).openReportDesign(copy), copy);

			DataRequestSession session = engineTask.getDataSession();

			Map appContext = new HashMap();
			appContext.put(DataEngine.MEMORY_DATA_SET_CACHE, Integer.valueOf(dataSetHandle.getRowFetchLimit()));

			appContext.put(ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS, createResourceIdentifiers());

			engineTask.setAppContext(appContext);
			try {
				engineTask.run();

				DataService.getInstance().registerSession(dataSetHandle, session);
				session.refreshMetaData(dataSetHandle, holdEvent);
			} finally {
				DataService.getInstance().unRegisterSession(session);
				session.shutdown();
				engineTask.close();
				engine.destroy();
			}
		} else {
			DataSessionContext context = new DataSessionContext(DataEngineContext.DIRECT_PRESENTATION,
					dataSetHandle.getRoot());
			Map appContext = new HashMap();

			appContext.put(DataEngine.MEMORY_DATA_SET_CACHE, Integer.valueOf(dataSetHandle.getRowFetchLimit()));
			appContext.put(ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS, createResourceIdentifiers());

			context.setAppContext(appContext);

			DataRequestSession drSession = DataRequestSession.newSession(context);
			try {
				drSession.refreshMetaData(dataSetHandle, holdEvent);
			} finally {
				drSession.shutdown();
			}
		}
	}

	public static ResourceIdentifiers createResourceIdentifiers() {
		ResourceIdentifiers ri = new ResourceIdentifiers();
		ri.setDesignResourceBaseURI(getReportDesignPath());
		ri.setApplResourceBaseURI(getBIRTResourcePath());
		return ri;
	}

	/**
	 * Gets the BIRT resource path
	 * 
	 * @return
	 * @throws URISyntaxException
	 */
	public static URI getReportDesignPath() {
		if (Utility.getReportModuleHandle() == null || Utility.getReportModuleHandle().getSystemId() == null) {
			return null;
		}
		try {
			return new URI(Utility.getReportModuleHandle().getSystemId().getPath());
		} catch (URISyntaxException e) {
			return null;
		}
	}

	/**
	 * Gets the report design file path
	 * 
	 * @return
	 */
	public static URI getBIRTResourcePath() {
		try {
			return new URI(encode(ReportPlugin.getDefault().getResourceFolder()));
		} catch (URISyntaxException e) {
			return null;
		}
	}

	private static String encode(String location) {
		try {
			// expects the absolute path. Remove place to avoid relative path.
			return new File(location).toURI().toASCIIString();
			// .replace( new File( "" ).toURI( ).toASCIIString( ), "" );
			// //$NON-NLS-1$//$NON-NLS-2$
		} catch (Exception e) {
			return location;
		}
	}

	public static boolean containsDataSource(DataSetHandle ds) {
		if (ds instanceof JointDataSetHandle) {
			return false;
		}
		return true;
	}

	public static String getDataSourceType(DataSetHandle ds) {
		return null;
	}

	public static String getDataSetType(DataSetHandle ds) {
		return null;
	}

	public static IPropertyPage[] getCommonPages(DataSetHandle ds) {
		return new IPropertyPage[0];
	}

	public static DataSetHandle newDataSetHandle(DataSetHandle dataSetHandle, DesignElement element) {
		DataSetHandle targetHandle = dataSetHandle;
		if (dataSetHandle != null) {
			if (dataSetHandle instanceof OdaDataSetHandle) {
				targetHandle = new OdaDataSetHandle(dataSetHandle.getModule(), (DesignElement) element);
			} else if (dataSetHandle instanceof ScriptDataSetHandle) {
				targetHandle = new ScriptDataSetHandle(dataSetHandle.getModule(), (DesignElement) element);
			} else if (dataSetHandle instanceof JointDataSetHandle) {
				targetHandle = new JointDataSetHandle(dataSetHandle.getModule(), (JointDataSet) element);
			} else if (dataSetHandle instanceof DerivedDataSetHandle) {
				targetHandle = new DerivedDataSetHandle(dataSetHandle.getModule(), (DerivedDataSet) element);
			}
		}
		return targetHandle;
	}

	public static boolean disableCollation(DataSetHandle handle) {
		return false;
	}
}
