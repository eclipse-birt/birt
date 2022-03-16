/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.internal.wizards;

import java.net.URL;
import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.BirtWTPUIPlugin;
import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.birt.integration.wtp.ui.internal.util.DataUtil;
import org.eclipse.birt.integration.wtp.ui.internal.util.UIUtil;
import org.eclipse.birt.integration.wtp.ui.internal.util.WebArtifactUtil;
import org.eclipse.birt.integration.wtp.ui.project.facet.BirtFacetInstallDataModelProperties;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardContainer2;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage;
import org.osgi.framework.Bundle;

/**
 * This wizard page is to configure Birt deployment settings.
 * <p>
 * These settings will be rewrited in web.xml.
 * <p>
 * <ol>
 * <li>BIRT_RESOURCE_PATH</li>
 * <li>BIRT_VIEWER_WORKING_FOLDER</li>
 * <li>BIRT_VIEWER_DOCUMENT_FOLDER</li>
 * <li>BIRT_VIEWER_IMAGE_DIR</li>
 * <li>BIRT_VIEWER_SCRIPTLIB_DIR</li>
 * <li>BIRT_VIEWER_LOG_DIR</li>
 * <li>WORKING_FOLDER_ACCESS_ONLY</li>
 * <li>BIRT_VIEWER_MAX_ROWS</li>
 * <li>BIRT_VIEWER_MAX_CUBE_ROWLEVELS</li>
 * <li>BIRT_VIEWER_MAX_CUBE_COLUMNLEVELS</li>
 * <li>BIRT_VIEWER_CUBE_MEMORY_SIZE</li>
 * <li>BIRT_VIEWER_LOG_LEVEL</li>
 * <li>BIRT_VIEWER_PRINT_SERVERSIDE</li>
 * </ol>
 *
 */
public class BirtWebProjectWizardConfigurationPage extends AbstractFacetWizardPage implements IBirtWizardConstants {

	/**
	 * Page Properties Map
	 */
	protected Map properties;

	/**
	 * Value for "BIRT_RESOURCE_PATH" setting
	 */
	protected Text txtResourceFolder;

	/**
	 * Value for "BIRT_VIEWER_WORKING_FOLDER" setting
	 */
	protected Text txtWorkingFolder;

	/**
	 * Value for "BIRT_VIEWER_DOCUMENT_FOLDER" setting
	 */
	protected Text txtDocumentFolder;

	/**
	 * Value for "WORKING_FOLDER_ACCESS_ONLY" setting
	 */
	protected Button btAccessOnly;

	/**
	 * Value for "BIRT_VIEWER_IMAGE_DIR" setting
	 */
	protected Text txtImageFolder;

	/**
	 * Value for "BIRT_VIEWER_SCRIPTLIB_DIR" setting
	 */
	protected Text txtScriptlibFolder;

	/**
	 * Value for "BIRT_VIEWER_LOG_DIR" setting
	 */
	protected Text txtLogFolder;

	/**
	 * Value for "BIRT_VIEWER_LOG_LEVEL" setting
	 */
	protected Button btLogLevel;

	/**
	 * Value for "BIRT_VIEWER_MAX_ROWS" setting
	 */
	protected Text txtMaxRows;

	/**
	 * Value for "BIRT_VIEWER_MAX_CUBE_ROWLEVELS" setting
	 */
	protected Text txtMaxRowLevels;

	/**
	 * Value for "BIRT_VIEWER_MAX_CUBE_COLUMNLEVELS" setting
	 */
	protected Text txtMaxColumnLevels;

	/**
	 * Value for "BIRT_VIEWER_CUBE_MEMORY_SIZE" setting
	 */
	protected Text txtCubeMemorySize;

	/**
	 * Value for "BIRT_VIEWER_LOG_LEVEL" setting
	 */
	protected Combo cbLogLevel;

	/**
	 * Value for "BIRT_VIEWER_PRINT_SERVERSIDE" setting
	 */
	protected Combo cbPrintServer;

	/**
	 * Constructor
	 *
	 * @param props
	 */
	public BirtWebProjectWizardConfigurationPage() {
		super(BIRT_CONFIGURATION_PAGE_NAME);
		setTitle(BirtWTPMessages.BIRTProjectConfigurationPage_title);
		setDescription(BirtWTPMessages.BIRTProjectConfigurationPage_desc);
		ImageDescriptor imageDesc = getDefaultPageImageDescriptor();
		if (imageDesc != null) {
			setImageDescriptor(imageDesc);
		}
	}

	/**
	 * Create Configuration Page
	 *
	 * @see org.eclipse.ui.dialogs.WizardNewProjectCreationPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());

		initializeDialogUnits(parent);

		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		// create folder configuration group
		Group paths = new Group(composite, SWT.NULL);
		paths.setLayout(new GridLayout());
		paths.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		paths.setText(BirtWTPMessages.BIRTConfiguration_group_paths);
		paths.setEnabled(true);

		// Initialize UI Utility
		UIUtil uit = new UIUtil(properties);

		// create resource folder setting group
		this.txtResourceFolder = uit.createResourceFolderGroup(paths);

		// create working folder setting group
		this.txtWorkingFolder = uit.createWorkingFolderGroup(paths);

		// create document folder setting group
		this.txtDocumentFolder = uit.createDocumentFolderGroup(paths);

		// create image folder setting group
		this.txtImageFolder = uit.createImageFolderGroup(paths);

		// create scriptlib folder setting group
		this.txtScriptlibFolder = uit.createScriptLibFolderGroup(paths);

		// create log folder setting group
		this.txtLogFolder = uit.createLogFolderGroup(paths);

		// create other configuration group
		Group others = new Group(composite, SWT.NULL);
		others.setLayout(new GridLayout());
		others.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		others.setText(BirtWTPMessages.BIRTConfiguration_group_others);
		others.setEnabled(true);

		// create ifaccess only setting group
		this.btAccessOnly = uit.createAccessOnlyGroup(others);

		// create log level setting group
		this.cbLogLevel = uit.createLogLevelGroup(others);

		// create print server setting group
		this.cbPrintServer = uit.createPrintServerGroup(others);

		// create max rows setting group
		this.txtMaxRows = uit.createMaxRowsGroup(others);

		// create max cube fetching row levels setting group
		this.txtMaxRowLevels = uit.createMaxRowLevelsGroup(others);

		// create max cube fetching column levels setting group
		this.txtMaxColumnLevels = uit.createMaxColumnLevelsGroup(others);

		// create max cube memory size setting group
		this.txtCubeMemorySize = uit.createCubeMemorySizeGroup(others);

		// initialize page properties map
		initializeProperties();

		setControl(composite);

		IWizardContainer container = getContainer();
		if (container instanceof IPageChangeProvider) {
			IPageChangeProvider pageChangeProvider = (IPageChangeProvider) container;
			pageChangeProvider.addPageChangedListener(new WizardPageChangedListener(this));
		}
	}

	/**
	 * Returns the default page banner image
	 *
	 * @return
	 */
	protected ImageDescriptor getDefaultPageImageDescriptor() {
		final Bundle bundle = Platform.getBundle(BirtWTPUIPlugin.PLUGIN_ID);
		if (bundle != null) {
			final URL url = bundle.getEntry(BIRT_PROJECT_WIZBANNER);
			return ImageDescriptor.createFromURL(url);
		}

		return null;
	}

	/**
	 * Do initialize page properties map
	 *
	 */
	protected void initializeProperties() {
		WebArtifactUtil.setContextParamValue(properties, BIRT_RESOURCE_FOLDER_SETTING, txtResourceFolder.getText());
		WebArtifactUtil.setContextParamValue(properties, BIRT_WORKING_FOLDER_SETTING, txtWorkingFolder.getText());
		WebArtifactUtil.setContextParamValue(properties, BIRT_DOCUMENT_FOLDER_SETTING, txtDocumentFolder.getText());
		WebArtifactUtil.setContextParamValue(properties, BIRT_REPORT_ACCESSONLY_SETTING,
				BLANK_STRING + btAccessOnly.getSelection());
		WebArtifactUtil.setContextParamValue(properties, BIRT_IMAGE_FOLDER_SETTING, txtImageFolder.getText());
		WebArtifactUtil.setContextParamValue(properties, BIRT_SCRIPTLIB_FOLDER_SETTING, txtScriptlibFolder.getText());
		WebArtifactUtil.setContextParamValue(properties, BIRT_LOG_FOLDER_SETTING, txtLogFolder.getText());
		WebArtifactUtil.setContextParamValue(properties, BIRT_MAX_ROWS_SETTING,
				DataUtil.getNumberSetting(txtMaxRows.getText()));
		WebArtifactUtil.setContextParamValue(properties, BIRT_MAX_ROWLEVELS_SETTING,
				DataUtil.getNumberSetting(txtMaxRowLevels.getText()));
		WebArtifactUtil.setContextParamValue(properties, BIRT_MAX_COLUMNLEVELS_SETTING,
				DataUtil.getNumberSetting(txtMaxColumnLevels.getText()));
		WebArtifactUtil.setContextParamValue(properties, BIRT_CUBE_MEMORYSIZE_SETTING,
				DataUtil.getNumberSetting(txtCubeMemorySize.getText()));
		WebArtifactUtil.setContextParamValue(properties, BIRT_LOG_LEVEL_SETTING, cbLogLevel.getText());
		WebArtifactUtil.setContextParamValue(properties, BIRT_PRINT_SERVER_SETTING, cbPrintServer.getText());

	}

	/**
	 * Sets the birt facet configuration
	 *
	 * @param config IDataModel
	 * @see org.eclipse.wst.common.project.facet.ui.IFacetWizardPage#setConfig(java.lang.Object)
	 */
	@Override
	public void setConfig(Object config) {
		IDataModel dataModel = (IDataModel) config;
		Map birtProperties = (Map) dataModel.getProperty(BirtFacetInstallDataModelProperties.BIRT_CONFIG);
		this.properties = (Map) birtProperties.get(EXT_CONTEXT_PARAM);
	}

	private class WizardPageChangedListener implements IPageChangedListener {

		private IWizardPage wizardPage;

		/**
		 * Constructs a listener which listens whenever the given page is selected and
		 * updates its size.
		 *
		 * @param wizardPage wizard page
		 */
		public WizardPageChangedListener(IWizardPage wizardPage) {
			this.wizardPage = wizardPage;
		}

		/**
		 * Called whenever the wizard page has changed and forces its container to
		 * resize its content.
		 *
		 * @see org.eclipse.jface.dialogs.IPageChangedListener#pageChanged(org.eclipse.jface.dialogs.PageChangedEvent)
		 */
		@Override
		public void pageChanged(PageChangedEvent event) {
			if (this.wizardPage == event.getSelectedPage()) {
				// force size update
				IWizardContainer container = getContainer();
				if (container instanceof IWizardContainer2) {
					((IWizardContainer2) container).updateSize();
				}
			}
		}
	}
}
