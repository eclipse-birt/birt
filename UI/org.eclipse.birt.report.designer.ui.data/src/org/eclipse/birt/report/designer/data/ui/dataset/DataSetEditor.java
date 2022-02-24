/*******************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
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

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.data.ui.datasource.PropertyBindingPage;
import org.eclipse.birt.report.designer.data.ui.property.AbstractPropertyDialog;
import org.eclipse.birt.report.designer.data.ui.property.PropertyNode;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataSetExceptionHandler;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DataUIConstants;
import org.eclipse.birt.report.designer.data.ui.util.IHelpConstants;
import org.eclipse.birt.report.designer.data.ui.util.PageLayoutManager;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionRequest;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionResponse;
import org.eclipse.datatools.connectivity.oda.design.SessionStatus;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DataSetDesignSession;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.manifest.UIManifestExplorer;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetEditorPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Presents data set editor dialog.
 *
 */

@SuppressWarnings("deprecation")
public class DataSetEditor extends AbstractPropertyDialog implements IPreferencePageContainer {

	protected ItemModelManager itemModelManager;
	private DataSetDesignSession m_designSession = null;

	protected boolean includeInputParameterPage = false;
	protected boolean includeOutputParameterPage = false;
	protected boolean needToFocusOnOutput = false;
	protected static boolean isNewlyCreated = false;

	private transient HistoryToolBar historyBar;

	// Common internal pages
	// The pages of ODA extensions are considered as internal pages, including
	// jdbc, xml, flatfile and web service, who's page id are hard-coded for
	// current workaround.
	// TODO:Fix me.
	protected static final String DATASET_SETTINGS_PAGE = "org.eclipse.birt.datasource.editor.dataset.settings"; //$NON-NLS-1$
	protected static final String OUTPUT_PARAMETER_PREVIEW_PAGE = "org.eclipse.birt.datasource.editor.dataset.outputparameterpreviewpage"; //$NON-NLS-1$
	protected static final String DATASOURCE_EDITOR_PROPERTY_PAGE = "org.eclipse.birt.datasource.editor.property"; //$NON-NLS-1$
	protected static final String COMPUTED_COLUMNS_PAGE = "org.eclipse.birt.datasource.editor.dataset.computedcolumnspage"; //$NON-NLS-1$
	protected static final String RESULTSET_PREVIEW_PAGE = "org.eclipse.birt.datasource.editor.dataset.resultsetpreviewpage"; //$NON-NLS-1$
	protected static final String FILTERS_PAGE = "org.eclipse.birt.datasource.editor.dataset.filterspage"; //$NON-NLS-1$
	protected static final String PARAMETERS_PAGE = "org.eclipse.birt.datasource.editor.dataset.parameterspage"; //$NON-NLS-1$
	protected static final String OUTPUTCOLUMN_PAGE = "org.eclipse.birt.datasource.editor.dataset.outputcolumnpage"; //$NON-NLS-1$
	protected static final String JOINT_DATA_SET_PAGE = "org.eclipse.birt.datasource.editor.dataset.jointDataSetPage"; //$NON-NLS-1$

	protected static final String DATA_SOURCE_SELECTION_PAGE = "org.eclipse.birt.datasource.editor.dataset.datasourceselectionpage"; //$NON-NLS-1$

	private static Logger logger = Logger.getLogger(DataSetEditor.class.getName());

	static {
		Iterator<String> pageNames = getInternalPageNames().iterator();
		while (pageNames.hasNext()) {
			PageLayoutManager.registerPage(pageNames.next());
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.DATA_SET_EDITOR_ID);
		return super.createDialogArea(parent);
	}

	@Override
	protected boolean needRememberLastSize() {
		return true;
	}

	/**
	 * The constructor.
	 *
	 * @param parentShell
	 */
	public DataSetEditor(Shell parentShell, DataSetHandle ds, boolean needToFocusOnOutput, boolean isNewlyCreated) {
		super(parentShell, ds);

		this.isNewlyCreated = isNewlyCreated;

		ExternalUIUtil.validateDataSetHandle(ds);
		initModelManager();

		this.needToFocusOnOutput = needToFocusOnOutput;
		// get the data source and dataset type from handle
		String dataSourceType, dataSetType;
		if (ds instanceof OdaDataSetHandle) {
			OdaDataSourceHandle dataSource = (OdaDataSourceHandle) ((OdaDataSetHandle) ds).getDataSource();
			dataSourceType = dataSource.getExtensionID();
			dataSetType = ((OdaDataSetHandle) ds).getExtensionID();
		} else if (ds instanceof ScriptDataSetHandle) {
			dataSourceType = DataUIConstants.DATA_SOURCE_SCRIPT;
			dataSetType = DataUIConstants.DATA_SET_SCRIPT;
		} else if (ds instanceof JointDataSetHandle) {
			dataSourceType = ""; //$NON-NLS-1$
			dataSetType = ""; //$NON-NLS-1$
		} else {
			dataSourceType = ExternalUIUtil.getDataSourceType(ds); // $NON-NLS-1$
			dataSetType = ExternalUIUtil.getDataSetType(ds); // $NON-NLS-1$
		}

		// according to the data source type, get the extension point.If
		// extention is birt, populate birt page. or the ODA Custom page will be
		// populated.
		boolean containsDataSource = ExternalUIUtil.containsDataSource(ds);
		if (containsDataSource) {
			addPageTo("/", DATA_SOURCE_SELECTION_PAGE, Messages.getString("dataset.editor.dataSource"), null, //$NON-NLS-1$ //$NON-NLS-2$
					new DataSetDataSourceSelectionPage());

			if (DesignSessionUtil.hasValidOdaDesignUIExtension(dataSourceType)) {
				addCustomPageODAV3((OdaDataSetHandle) ds, dataSourceType, dataSetType);
			} else {
				addBirtPage(dataSourceType, dataSetType);
			}
		}
		// add common pages, just like computedColumn page, parameter page,
		// output column page etc.
		addCommonPages(ds);
		setPageFocus();

		// start model manager to process the edit transaction
		itemModelManager.start(ds, needToFocusOnOutput);
	}

	protected void initModelManager() {
		itemModelManager = new ItemModelManager();
	}

	/**
	 * add page for org.eclipse.datatools.connectivity.oda.design.ui.dataSource
	 *
	 * @param dataSetHandle
	 */
	private void addCustomPageODAV3(OdaDataSetHandle dataSetHandle, String dataSourceType, String dataSetType) {
		try {
			DesignSessionRequest request = DTPUtil.getInstance().createDesignSessionRequest(dataSetHandle);
			if (request != null && request.getDataSourceDesign() != null) {
				DTPUtil.getInstance().supplementDesignAttributes(request.getDataSourceDesign());
			}
			m_designSession = DataSetDesignSession.startEditDesign(request);
			includeInputParameterPage = UIManifestExplorer.getInstance()
					.getDataSetUIElement(dataSourceType, dataSetType).supportsInParameters();
			includeOutputParameterPage = UIManifestExplorer.getInstance()
					.getDataSetUIElement(dataSourceType, dataSetType).supportsOutParameters();
		} catch (OdaException | URISyntaxException e) {
			ExceptionHandler.handle(e);
		}

		if (m_designSession != null) {
			populateEditorPage(m_designSession);
		}
	}

	/**
	 * populate editor page
	 *
	 * @param m_designSession
	 * @param dataSourceType
	 * @param dataSetType
	 */
	private void populateEditorPage(DataSetDesignSession m_designSession) {
		try {
			DataSetEditorPage[] dataSetEditorPages = m_designSession.getEditorPages();
			for (int i = 0; i < dataSetEditorPages.length; i++) {
				DataSetEditorPage dataSetEditorPage = dataSetEditorPages[i];

				PropertyPageWrapper propertyPageWrapper = new PropertyPageWrapper(dataSetEditorPage, m_designSession);

				addPageTo(dataSetEditorPage.getPagePath(), dataSetEditorPage.getPageId(), dataSetEditorPage.getTitle(),
						null, propertyPageWrapper);

				if (dataSetEditorPage.hasInitialFocus()) {
					setDefaultNode(dataSetEditorPage.getPageId());
				}
			}
		} catch (OdaException ex) {
			ExceptionHandler.handle(ex);
		}
	}

	/**
	 * add several common use page for Birt data set editor
	 *
	 * @param ds
	 */
	protected void addCommonPages(DataSetHandle ds) {

		if (ds instanceof ScriptDataSetHandle) {
			addScriptOutputColumnDefnPage();

			addParametersPage();

			addFiltersPage();
			// Setting page
			addDataSetSettingPage(ds);

			addResultSetPreviewPage();

		} else if (ds instanceof OdaDataSetHandle) {
			addOutputColumnsPage();

			addComputedColumnsPage();
			// flatfile driver need not parameter page to be displayed.
			if (includeInputParameterPage) {
				addParametersPage();
			}
			addDataSetFilterPage();

			addPropertyBindingPage();

			// Setting page
			addDataSetSettingPage(ds);
			// Output parameters page
			if (includeOutputParameterPage) {
				addOutputParameterPreviewPage();
			}

			addResultSetPreviewPage();

		} else if (ds instanceof JointDataSetHandle) {
			addJointDataSetPage();

			addOutputColumnsPage();

			addComputedColumnsPage();

			addParametersPage();

			addFiltersPage();

			addResultSetPreviewPage();
		} else {
			IPropertyPage[] pages = ExternalUIUtil.getCommonPages(ds);

			if (pages != null && pages.length > 0) {
				for (int i = 0; i < pages.length; i++) {
					addPageTo("/", pages[i].getClass().getName(), pages[i].getName(), null, pages[i]);//$NON-NLS-1$
				}
				if (!needToFocusOnOutput && pages.length > 0) {
					setDefaultNode(pages[0].getClass().getName());
				}
				addOutputColumnsPage();

				addComputedColumnsPage();
				addParametersPage();
				addFiltersPage();
				addResultSetPreviewPage();
			}
		}
	}

	protected void addDataSetFilterPage() {
		// Filter page
		IPropertyPage filterpage = new DataSetFiltersPage();
		try {
			filterpage = (IPropertyPage) Class
					.forName("org.eclipse.birt.report.designer.data.ui.dataset.AdvancedDataSetFiltersPage") //$NON-NLS-1$
					.newInstance();
		} catch (Throwable e) {

		}
		addPageTo("/", FILTERS_PAGE, Messages.getString("dataset.editor.filters"), null, filterpage);//$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void addOutputParameterPreviewPage() {
		addPageTo("/", OUTPUT_PARAMETER_PREVIEW_PAGE, Messages.getString("dataset.editor.outputparameters"), null, //$NON-NLS-1$ //$NON-NLS-2$
				new OutputParameterPreviewPage());
	}

	protected void addComputedColumnsPage() {
		// Computed column page
		addPageTo("/", COMPUTED_COLUMNS_PAGE, Messages.getString("dataset.editor.computedColumns"), null, //$NON-NLS-1$ //$NON-NLS-2$
				new AdvancedDataSetComputedColumnsPage());
	}

	protected void addResultSetPreviewPage() {
		// Result set preview page
		addPageTo("/", RESULTSET_PREVIEW_PAGE, Messages.getString("dataset.editor.preview"), null, //$NON-NLS-1$ //$NON-NLS-2$
				new ResultSetPreviewPage());
	}

	protected void addFiltersPage() {
		// Data set filters page
		addPageTo("/", FILTERS_PAGE, Messages.getString("dataset.editor.filters"), null, new DataSetFiltersPage());//$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void addParametersPage() {
		// Parameter page
		addPageTo("/", PARAMETERS_PAGE, Messages.getString("dataset.editor.parameters"), null, //$NON-NLS-1$ //$NON-NLS-2$
				new DataSetParametersPage());
	}

	protected void addScriptOutputColumnDefnPage() {
		// Output column is replaced by column definition page
		addPageTo("/", OUTPUTCOLUMN_PAGE, Messages.getString("dataset.editor.outputColumns"), null, //$NON-NLS-1$ //$NON-NLS-2$
				new AdvancedOutputColumnDefnPage());
	}

	protected void addOutputColumnsPage() {
		// Output column is replaced by column definition page
		addPageTo("/", OUTPUTCOLUMN_PAGE, Messages.getString("dataset.editor.outputColumns"), null, //$NON-NLS-1$ //$NON-NLS-2$
				new AdvancedDataSetOutputColumnsPage());
	}

	protected void addJointDataSetPage() {
		addPageTo("/", //$NON-NLS-1$
				JOINT_DATA_SET_PAGE, Messages.getString("JointDataSetPage.query"), //$NON-NLS-1$
				null, new JointDataSetPage(Messages.getString("dataset.editor.dataSource"))); //$NON-NLS-1$
	}

	protected void addPropertyBindingPage() {
		// Property binding page
		addPageTo("/", DATASOURCE_EDITOR_PROPERTY_PAGE, Messages.getString("datasource.editor.property"), null, //$NON-NLS-1$ //$NON-NLS-2$
				new PropertyBindingPage());
	}

	private void setPageFocus() {
		if (needToFocusOnOutput) {
			setDefaultNode(OUTPUTCOLUMN_PAGE);
		}
	}

	/**
	 * add page for org.eclipse.birt.report.designer.ui.odadatasource
	 *
	 * @param dataSourceType
	 * @param dataSetType
	 */
	private void addBirtPage(String dataSourceType, String dataSetType) {
		try {
			IConfigurationElement element = DataSetProvider.findDataSetElement(dataSetType, dataSourceType);
			if (element != null) {
				String supportParameterPage = element.getAttribute("addsDataSetParametersPage"); //$NON-NLS-1$
				if (supportParameterPage != null) {
					includeInputParameterPage = Boolean.parseBoolean(supportParameterPage);
				}

				// Now get all the editor pages
				IConfigurationElement[] editorPages = element.getChildren("dataSetEditorPage");//$NON-NLS-1$
				if (editorPages != null) {
					boolean hasFocus = false;
					for (int n = 0; n < editorPages.length; n++) {
						IPropertyPage page = (IPropertyPage) editorPages[n].createExecutableExtension("class");//$NON-NLS-1$
						addPageTo(editorPages[n].getAttribute("path"), editorPages[n].getAttribute("name"), //$NON-NLS-1$ //$NON-NLS-2$
								editorPages[n].getAttribute("displayName"), null, page);//$NON-NLS-1$
						if (!hasFocus) {
							String initFocusAttr = editorPages[n].getAttribute("initFocus"); //$NON-NLS-1$
							if (initFocusAttr != null && initFocusAttr.equalsIgnoreCase("true")) //$NON-NLS-1$
							{
								setDefaultNode(editorPages[n].getAttribute("name")); //$NON-NLS-1$
								hasFocus = true;
							} else if (n == editorPages.length - 1) {
								setDefaultNode(editorPages[0].getAttribute("name")); //$NON-NLS-1$
							}
						}
					}
				}
			}
		} catch (CoreException e1) {
			ExceptionHandler.handle(e1);
		}
	}

	/**
	 *
	 * @param ds
	 */
	protected void addDataSetSettingPage(DataSetHandle ds) {
		IPropertyPage settingPage = new AdvancedDataSetSettingsPage();
		addPageTo("/", //$NON-NLS-1$
				DATASET_SETTINGS_PAGE, Messages.getString("dataset.editor.settings"), //$NON-NLS-1$
				null, settingPage);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyDialog
	 * #performOk()
	 */
	@Override
	public boolean performOk() {
		try {
			if (m_designSession != null) {
				m_designSession.finish();
			}
		} catch (OdaException e) {
		}
		itemModelManager.destory(false);
		return true;
	}

	/**
	 * update dataset design
	 */
	public void updateDataSetDesign(IPropertyPage page) {
		try {
			if (this.getCurrentNode() != null && this.getCurrentNode().getPage() != page) {
				return;
			}
			if (m_designSession != null) {
				// restart the oda design session with a new request
				// based on the latest state of the data set handle
				DesignSessionRequest request = DTPUtil.getInstance()
						.createDesignSessionRequest((OdaDataSetHandle) getHandle());
				if (request != null && request.getDataSourceDesign() != null) {
					DTPUtil.getInstance().supplementDesignAttributes(request.getDataSourceDesign());
				}

				// try to preserve the existing editor pages if feasible
				boolean hasResetEditorPages = m_designSession.restartEditDesign(request, false);

				if (hasResetEditorPages) {
					populateDataSetEditor();
				}
			}
		} catch (OdaException | URISyntaxException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyDialog
	 * #performCancel()
	 */
	@Override
	public boolean performCancel() {
		if (m_designSession != null) {
			m_designSession.cancel();
		}

		itemModelManager.destory(true);
		return true;
	}

	/**
	 * Returns the current model handle.
	 *
	 */
	public DataSetHandle getHandle() {
		return (DataSetHandle) getModel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		String title = Messages.getFormattedString("dataset.edit", new String[] { getHandle().getName() });//$NON-NLS-1$
		getShell().setText(title);

		Control control = super.createContents(parent);
		setPageHelpContent(control);

		return control;
	}

	protected void setPageHelpContent(Control control) {
		Utility.setSystemHelp(control, IHelpConstants.CONEXT_ID_DATASET_EDIT);
	}

	/**
	 * Gets all columns items from dataset list
	 *
	 * @return DataSetViewData[]
	 * @throws BirtException
	 */
	public DataSetViewData[] getCurrentItemModel() throws BirtException {
		return itemModelManager.getCurrentItemModel(true);
	}

	/**
	 * Gets all columns items from dataset list
	 *
	 * @param useColumnHint
	 * @param suppressErrorMessage
	 * @return DataSetViewData[]
	 * @throws BirtException
	 */
	public DataSetViewData[] getCurrentItemModel(boolean useColumnHint) throws BirtException {
		return itemModelManager.getCurrentItemModel(useColumnHint);
	}

	/**
	 * Set the modelChanged status to "true"
	 *
	 */
	public void enableLinkedParamChanged() {
		this.itemModelManager.enableLinkedParamChanged();
	}

	/**
	 * @return if the model has changed or the weak-linked report parameter has
	 *         changed, return true;
	 */
	public boolean modelChanged() {
		return this.itemModelManager.modelChanged();
	}

	/**
	 *
	 * @return
	 * @throws OdaException
	 */
	public DataSetDesign getCurrentDataSetDesign() throws OdaException {
		if (m_designSession != null) {
			DesignSessionResponse response = m_designSession.flush().getResponse();
			if (response.getSessionStatus() != SessionStatus.OK_LITERAL) {
				return null;
			}
			DataSetDesign dataSetDesign = response.getDataSetDesign();
			return dataSetDesign;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		super.okPressed();

		try {
			DataSetUIUtil.updateColumnCache(this.getHandle(), false);
		} catch (Exception e) {
			DataSetExceptionHandler.handle(e);
			return;
		}

		// First call ok on all the pages
		if (super.rootNode.hasSubNodes()) {
			PropertyNode[] nodes = rootNode.getSubNodes();
			for (int n = 0; n < nodes.length; n++) {
				// Check whether the current page can be closed
				if (nodes[n].getPage() instanceof DataSetParametersPage) {
					if (viewer == null || viewer.getTree() == null) {
						return;
					}

					DataSetParametersPage page = (DataSetParametersPage) nodes[n].getPage();
					if (!page.canFinish() && !viewer.getTree().isDisposed()) {
						TreeItem firstNode = viewer.getTree().getItems()[n];
						StructuredSelection select = new StructuredSelection(firstNode.getData());
						viewer.setSelection(select);
						String name = ((DataSetParametersPage) nodes[n].getPage()).getNoneValuedParameterName();
						this.setMessage(Messages.getFormattedString("dataset.editor.error.noInputParameterDefaultValue", //$NON-NLS-1$
								new Object[] { name }), IMessageProvider.ERROR);
						return;
					}
				}
			}
		}
	}

	/**
	 * get current PropertyPage
	 *
	 * @return
	 */
	private PropertyPage getCurrentPropertyPage() {
		if (getCurrentNode() != null) {
			IPropertyPage ipropertyPage = getCurrentNode().getPage();
			if (ipropertyPage instanceof PropertyPageWrapper) {
				return ((PropertyPageWrapper) ipropertyPage).getPropertyPage();
			}
		}

		return null;
	}

	/**
	 * populate dataSetEditor page in design session
	 *
	 */
	private void populateDataSetEditor() {
		if (m_designSession == null) {
			return;
		}
		DataSetEditorPage[] dataSetEditorPages;
		try {
			dataSetEditorPages = m_designSession.getEditorPages();
		} catch (OdaException e) {
			return;
		}
		IPropertyPage currentNode = null;
		for (int i = 0; i < dataSetEditorPages.length; i++) {
			DataSetEditorPage dataSetEditorPage = dataSetEditorPages[i];
			PropertyPageWrapper propertyPageWrapper = new PropertyPageWrapper(dataSetEditorPage, m_designSession);

			// First call ok on all the pages
			if (super.rootNode.hasSubNodes()) {
				PropertyNode[] nodes = rootNode.getSubNodes();
				for (int n = 0; n < nodes.length; n++) {
					if (nodes[n] != null && nodes[n].getId().equals(dataSetEditorPage.getPageId())) {
						nodes[n].removePageControl();
						nodes[n].setPage(propertyPageWrapper);
						nodes[n].setContainer(this);
						if (this.getCurrentNode().getId().equals(dataSetEditorPage.getPageId())) {
							currentNode = propertyPageWrapper;
						}
						break;
					}
				}
			}
		}
		if (currentNode != null) {
			rootNode.setPage(currentNode);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#getPreferenceStore
	 * ()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateButtons()
	 */
	@Override
	public void updateButtons() {
		if (getOkButton() != null) {
			PropertyPage propertyPage = this.getCurrentPropertyPage();
			if (propertyPage != null) {
				getOkButton().setEnabled(propertyPage.okToLeave());
			} else if (getCurrentNode().getPage() instanceof WizardPage) {
				getOkButton().setEnabled(((WizardPage) getCurrentNode().getPage()).isPageComplete());
			}
		}
	}

	public void updateOKButtonStatus(boolean isEnabled) {
		if (getOkButton() != null) {
			getOkButton().setEnabled(isEnabled);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateMessage()
	 */
	@Override
	public void updateMessage() {
		PropertyPage propertyPage = getCurrentPropertyPage();

		if (propertyPage != null) {
			setMessage(propertyPage.getMessage(), propertyPage.getMessageType());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateTitle()
	 */
	@Override
	public void updateTitle() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyDialog
	 * #createTitleArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite createTitleArea(Composite parent) {
		GridLayout layout = (GridLayout) parent.getLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		parent.setLayout(layout);

		createMessageComposite(parent);
		createToolbarComposite(parent);

		return null;
	}

	/**
	 *
	 * @param parent
	 */
	private void createMessageComposite(Composite parent) {
		super.createTitleArea(parent);
	}

	/**
	 *
	 * @param parent
	 */
	private void createToolbarComposite(Composite parent) {
		historyBar = new HistoryToolBar(parent, viewer, SWT.FLAT | SWT.HORIZONTAL);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyDialog
	 * # showSelectionPage(org.eclipse.birt.report.designer.ui.dialogs.properties.
	 * PropertyNode)
	 */
	@Override
	public void showSelectionPage(PropertyNode selectedNode) {
		super.showSelectionPage(selectedNode);

		if (showPage) {
			if (historyBar != null) {
				historyBar.addHistoryNode(selectedNode);
			}
			// automatically pack(resize) the shell if some external page
			// specified was selected
			if (!PageLayoutManager.isRegisteredPage(selectedNode.getId())) {
				getShell().pack();
			}
		}
	}

	/**
	 * whether support input parameter
	 *
	 * @return
	 */
	public boolean supportsInParameters() {
		return this.includeInputParameterPage;
	}

	/**
	 * whether support output parameter
	 *
	 * @return
	 */
	public boolean supportsOutputParameters() {
		return this.includeOutputParameterPage;
	}

	/**
	 * Helper class which is a itemModel manager.
	 */
	protected static class ItemModelManager implements Listener {

		protected DataSetHandle ds = null;
		protected boolean itemModelChanged = true;
		protected boolean linkedParameterChanged = true;
		protected DataSetViewData[] savedItemModel = null;
		protected String savedQueryText = null;
		protected ClassLoader oldContextLoader = null;

		/**
		 * Start action
		 *
		 * @param ds model to be listened
		 */
		public void start(DataSetHandle dataSet, boolean needToFocusOnOutput) {
			assert dataSet != null;

			this.ds = dataSet;

			if (ds instanceof OdaDataSetHandle) {
				this.savedQueryText = ((OdaDataSetHandle) ds).getQueryText();
			}

			setContextLoader(dataSet);

			this.savedItemModel = DataSetProvider.getCurrentInstance().getCachedDataSetItemModel(ds,
					needToFocusOnOutput);

			this.ds.addListener(this);
		}

		/**
		 *
		 * @param dataSet
		 */
		protected void setContextLoader(DataSetHandle dataSet) {
			// set context class loader
			oldContextLoader = Thread.currentThread().getContextClassLoader();
			ClassLoader parentLoader = oldContextLoader;
			if (parentLoader == null) {
				parentLoader = this.getClass().getClassLoader();
			}
			ClassLoader newContextLoader = DataSetProvider.getCustomScriptClassLoader(parentLoader,
					dataSet.getModuleHandle());
			Thread.currentThread().setContextClassLoader(newContextLoader);
		}

		/**
		 * Destroy action
		 *
		 * @param rollback true: rollback to savedItemModel false: do nothing
		 */
		public void destory(boolean rollback) {
			if (rollback) {
				if (ds instanceof OdaDataSetHandle) {
					try {
						((OdaDataSetHandle) ds).setQueryText(this.savedQueryText);
					} catch (BirtException e) {
						// should not arrive here
					}
				}

				DataSetProvider.getCurrentInstance().setModelOfDataSetHandle(this.ds, savedItemModel);
			}
			// Restore old context loader
			Thread.currentThread().setContextClassLoader(oldContextLoader);
			DataSetProvider.getCurrentInstance().clear(ds);

			if (ds != null) {
				ds.removeListener(this);
			}
		}

		/*
		 * @see org.eclipse.birt.report.model.api.core.Listener#elementChanged(org
		 * .eclipse.birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */
		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			itemModelChanged = true;
		}

		/**
		 * Set the linked parameter changed status to 'true'
		 *
		 */
		public void enableLinkedParamChanged() {
			linkedParameterChanged = true;
		}

		/**
		 *
		 * @return the model changed or not
		 */
		public boolean modelChanged() {
			return this.itemModelChanged || linkedParameterChanged;
		}

		/**
		 * Gets all columns items from dataset list
		 *
		 * @return DataSetItemModel[]
		 * @throws BirtException
		 */
		public DataSetViewData[] getCurrentItemModel() throws BirtException {
			DataSetViewData[] dataSetItem = DataSetProvider.getCurrentInstance().getColumns(ds, itemModelChanged);
			itemModelChanged = false;
			linkedParameterChanged = false;
			return dataSetItem;
		}

		/**
		 * Gets all columns items from dataset list
		 *
		 * @param useColumnHint
		 * @param suppressErrorMessage
		 * @return DataSetItemModel[]
		 * @throws BirtException
		 */
		public DataSetViewData[] getCurrentItemModel(boolean useColumnHint) throws BirtException {
			DataSetViewData[] dataSetItem = DataSetProvider.getCurrentInstance().getColumns(ds, itemModelChanged,
					useColumnHint);
			itemModelChanged = false;
			linkedParameterChanged = false;
			return dataSetItem;
		}

	}

	protected static Set<String> getInternalPageNames() {
		Set<String> result = new HashSet<>();

		result.add(DATASET_SETTINGS_PAGE);
		result.add(OUTPUT_PARAMETER_PREVIEW_PAGE);
		result.add(DATASOURCE_EDITOR_PROPERTY_PAGE);
		result.add(COMPUTED_COLUMNS_PAGE);
		result.add(RESULTSET_PREVIEW_PAGE);
		result.add(FILTERS_PAGE);
		result.add(PARAMETERS_PAGE);
		result.add(OUTPUTCOLUMN_PAGE);
		result.add(JOINT_DATA_SET_PAGE);
		result.add(DATA_SOURCE_SELECTION_PAGE);

		String extensionName = "org.eclipse.datatools.connectivity.oda.design.ui.dataSource";
		IExtensionRegistry extReg = Platform.getExtensionRegistry();
		IExtensionPoint extPoint = extReg.getExtensionPoint(extensionName);

		if (extPoint == null) {
			return result;
		}

		IExtension[] exts = extPoint.getExtensions();
		if (exts == null) {
			return result;
		}

		for (int e = 0; e < exts.length; e++) {
			IConfigurationElement[] configElems = exts[e].getConfigurationElements();
			if (configElems == null) {
				continue;
			}

			for (int i = 0; i < configElems.length; i++) {
				if (configElems[i].getName().equals("dataSetUI")) {
					IConfigurationElement[] elems = configElems[i].getChildren("dataSetPage");
					if (elems != null && elems.length > 0) {
						for (int j = 0; j < elems.length; j++) {
							String value = elems[j].getAttribute("id");
							if (value != null) {
								result.add(value);
							}
						}
					}
				}
			}
		}
		return result;
	}
}
