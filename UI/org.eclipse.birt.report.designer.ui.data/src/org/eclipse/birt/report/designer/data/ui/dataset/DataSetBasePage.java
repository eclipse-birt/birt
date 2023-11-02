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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.IHelpConstants;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.odadatasource.wizards.AbstractDataSetWizard;
import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DataSetDesignSession;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataSetType;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

/**
 * Base information page for dataset wizards
 *
 *
 */

public class DataSetBasePage extends WizardPage {

	private transient Text nameEditor = null;
	private transient ComboViewer dataSetTypeChooser = null;

	private transient boolean useTransaction = true;

	final private static String EMPTY_NAME = Messages.getString("error.DataSet.emptyName");//$NON-NLS-1$
	final private static String DUPLICATE_NAME = Messages.getString("error.duplicateName");//$NON-NLS-1$
	final private static String CREATE_PROMPT = Messages.getString("dataset.message.create");//$NON-NLS-1$

	private transient DataSourceHandle newDataSource = null;

	private Hashtable htDataSetWizards = new Hashtable(10);

	// store latest selection data source
	private ISelection dateSetTypeSelection = null;
	private transient DataSetDesignSession m_designSession = null;
	private boolean useODAV3 = false;

	private WizardFilter wizardFilter = null;

	private FilteredTree dataSourceFilteredTree;
	private DataSetBasePageHelper helper;

	private IWizardPage nextPage;

	private final static String SCRIPT_DATASET_NAME = Messages.getString("DataSetBasePage.ScriptedDataSet.name");//$NON-NLS-1$
	private final static String SCRIPT_DATASOURCE_NAME = Messages.getString("DataSetBasePage.ScriptedDataSource.name"); //$NON-NLS-1$

	/**
	 * Creates a new data set wizard page
	 *
	 * @param useTransaction the style of page
	 */
	public DataSetBasePage(boolean useTransaction) {
		super("DataSet Base Page");//$NON-NLS-1$
		setTitle(Messages.getString("dataset.new"));//$NON-NLS-1$
		this.setMessage(Messages.getString("AbstractDataSetWizard.ModelTrans.Create")); //$NON-NLS-1$
		this.useTransaction = useTransaction;
		setImage();
	}

	/**
	 * Sets the Image for the page
	 */
	private void setImage() {
		URL url = null;
		try {
			url = new URL(ReportPlugin.getDefault().getBundle().getEntry("/"), //$NON-NLS-1$
					"icons/wizban/dataset_wizard.gif");//$NON-NLS-1$
		} catch (MalformedURLException e) {
			ExceptionHandler.handle(e);
		}
		ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		this.setImageDescriptor(desc);

	}

	/**
	 * Creates the top level control for this dialog page under the given parent
	 * composite.
	 * <p>
	 * Implementors are responsible for ensuring that the created control can be
	 * accessed via <code>getControl</code>
	 * </p>
	 *
	 * @param parent the parent composite
	 */
	@Override
	public void createControl(Composite parent) {
		helper = new DataSetBasePageHelper();

		// initialize the dialog layout
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		final Group group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout());
		group.setText(Messages.getString("DataSetBasePage.Group.DataSourceSelection")); //$NON-NLS-1$
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 8));// GridData.FILL_BOTH));

		wizardFilter = new WizardFilter();
		dataSourceFilteredTree = new FilteredTree(group, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL,
				this.wizardFilter, true);
		GridData treeData = new GridData(GridData.FILL_BOTH);
		treeData.grabExcessHorizontalSpace = true;
		treeData.grabExcessVerticalSpace = true;
		treeData.heightHint = 300;
		treeData.widthHint = 600;
		dataSourceFilteredTree.setLayoutData(treeData);
		SelectionListener listener = new SelectionListener() {

			TreeItem parent = null;

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				if (event.item.getData() instanceof DataSourceHandle) {
					dataSetTypeChooser.getCombo().setEnabled(true);
					if (parent == null || parent != ((TreeItem) event.item).getParentItem()) {
						parent = ((TreeItem) event.item).getParentItem();
						doDataSourceSelectionChanged(parent.getData());
					}
					setPageComplete(!hasWizard() && (getMessageType() != ERROR));
				} else {
					dataSetTypeChooser.getCombo().clearSelection();
					dataSetTypeChooser.getCombo().setEnabled(false);
					setPageComplete(false);
				}
				dataSourceFilteredTree.getViewer().getTree().setFocus();
			}
		};
		dataSourceFilteredTree.getViewer().getTree().addSelectionListener(listener);
		createDataSetTypeViewer(composite);

		setDataSourceTreeViewer();
		setPageStatus();

		// initialize name editor
		new Label(composite, SWT.RIGHT).setText(Messages.getString("dataset.wizard.label.datasetName"));//$NON-NLS-1$
		nameEditor = new Text(composite, SWT.BORDER);
		String name = ReportPlugin.getDefault().getCustomName(ReportDesignConstants.DATA_SET_ELEMENT);
		if (name != null) {
			nameEditor.setText(Utility.getUniqueDataSetName(name));
		} else
		// can't get defaut name
		{
			nameEditor.setText(Utility.getUniqueDataSetName(Messages.getString("dataset.new.defaultName")));//$NON-NLS-1$
		}

		nameEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameEditor.setToolTipText(Messages.getString("DataSetBasePage.tooltip")); //$NON-NLS-1$
		nameEditor.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (StringUtil.isBlank(nameEditor.getText().trim())) {
					setMessage(EMPTY_NAME, ERROR);
				} else if (isDuplicateName()) {// name is duplicated
					setMessage(DUPLICATE_NAME, ERROR);
				} else if (containInvalidCharactor(nameEditor.getText())) {// name contains invalid ".", "/", "\", "!",
																			// ";", ","
																			// character
					String msg = Messages.getFormattedString("error.invalidName", //$NON-NLS-1$
							new Object[] { nameEditor.getText() });
					setMessage(msg, ERROR);
				} else {// everything is OK
					setMessage(CREATE_PROMPT);
				}

				setPageComplete(!hasWizard() && (getMessageType() != ERROR) && getSelectedDataSource() != null);

				nameEditor.setFocus();
			}
		});
		setControl(composite);

		Utility.setSystemHelp(getControl(), IHelpConstants.CONEXT_ID_DATASET_NEW);
	}

	private void setDataSourceTreeViewer() {

		dataSourceFilteredTree.getViewer().setContentProvider(new ITreeContentProvider() {

			DataSourceType[] types = null;

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			@Override
			public Object[] getElements(Object inputElement) {
				types = new DataSourceType[((Collection) inputElement).size()];
				Iterator iter = ((Collection) inputElement).iterator();
				int i = 0;
				while (iter.hasNext()) {
					types[i] = (DataSourceType) iter.next();
					i++;
				}
				return types;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof DataSourceType) {
					return ((DataSourceType) parentElement).getDataSourceList().toArray();
				} else {
					return new Object[0];
				}
			}

			@Override
			public Object getParent(Object element) {
				if (types != null) {
					for (int i = 0; i < types.length; i++) {
						if (types[i].getDataSourceList().contains(element)) {
							return types[i];
						}
					}
				}
				return null;
			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof DataSourceType) {
					return true;
				} else if (element instanceof DataSourceHandle) {
					return false;
				}
				return false;
			}
		});

		dataSourceFilteredTree.getViewer().setLabelProvider(new ILabelProvider() {

			@Override
			public Image getImage(Object element) {
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof DataSourceType) {
					return ((DataSourceType) element).getDataSourceDisplayName();
				} else if (element instanceof DataSourceHandle) {
					return ((DataSourceHandle) element).getName();
				}
				return element.toString();
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
		});

		dataSourceFilteredTree.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setPageStatus();
			}
		});

		dataSourceFilteredTree.getViewer().setComparator(new ViewerComparator(new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				return o1.toString().compareTo(o2.toString());
			}
		}));

		Map sourceMap = getDataSourceMap();
		dataSourceFilteredTree.getViewer().setInput(sourceMap.values());
		dataSourceFilteredTree.getViewer().expandAll();
		if (dataSourceFilteredTree.getViewer().getTree().getItems().length > 0) {
			dataSourceFilteredTree.getViewer().getTree()
					.select(dataSourceFilteredTree.getViewer().getTree().getItem(0).getItem(0));
			doDataSourceSelectionChanged(dataSourceFilteredTree.getViewer().getTree().getItem(0).getData());
			dataSetTypeChooser.getCombo().setEnabled(true);
		}
		setPageComplete(!hasWizard() && (getMessageType() != ERROR));
	}

	private void setPageStatus() {
		if (dataSourceFilteredTree == null || dataSourceFilteredTree.getViewer().getTree().getSelectionCount() <= 0) {
			setPageComplete(false);
		}
	}

	private Map getDataSourceMap() {
		List dataSources = Utility.getDataSources();

		Map sourceTypeMap = new HashMap();
		for (int i = 0; i < dataSources.size(); i++) {
			DataSourceHandle handle = (DataSourceHandle) dataSources.get(i);
			if (handle instanceof OdaDataSourceHandle) {
				String type = ((OdaDataSourceHandle) handle).getExtensionID();
				if (!sourceTypeMap.containsKey(type)) {
					try {
						// Find the data source
						ExtensionManifest extMF = ManifestExplorer.getInstance().getExtensionManifest(type);
						if (extMF != null) {
							// Find the data sets for this data source.
							DataSetType[] dataSetTypes = extMF.getDataSetTypes();
							OdaDataSetTypeElement[] element = new OdaDataSetTypeElement[dataSetTypes.length];
							for (int n = 0; n < dataSetTypes.length; n++) {
								if (!dataSetTypes[n].isDeprecated()) {
									element[n] = new OdaDataSetTypeElement(dataSetTypes[n],
											DataSetProvider.findDataSetElement(dataSetTypes[n].getID(), type));
								}
							}
							DataSourceType dataSourceType = new DataSourceType(type, extMF.getDataSourceDisplayName(),
									element);
							sourceTypeMap.put(type, dataSourceType);
							dataSourceType.addDataSource(handle);
						}
					} catch (Exception ex) {
						ExceptionHandler.handle(ex);
					}
				} else {
					DataSourceType sourceType = (DataSourceType) sourceTypeMap.get(type);
					sourceType.addDataSource(handle);
				}
			} else if (handle instanceof ScriptDataSourceHandle) {
				useODAV3 = false;
				getScriptDataSourceMap(handle, sourceTypeMap, getScriptDataSetName(handle),
						getScriptDataSourceName(handle));
			} else {
				useODAV3 = false;
				helper.addExternalDataSource(sourceTypeMap, handle);
			}
		}
		return sourceTypeMap;
	}

	private void getScriptDataSourceMap(DataSourceHandle handle, Map sourceTypeMap, String DataSetName,
			String DataSourceName) {
		if (!sourceTypeMap.containsKey(DataSourceName)) {
			DataSetTypeElement[] element = new DataSetTypeElement[1];
			element[0] = new DataSetTypeElement(DataSetName);
			DataSourceType dataSourceType = new DataSourceType(DataSourceName, DataSourceName, element);
			sourceTypeMap.put(DataSourceName, dataSourceType);
			dataSourceType.addDataSource(handle);
		} else {
			DataSourceType sourceType = (DataSourceType) sourceTypeMap.get(DataSourceName);
			sourceType.addDataSource(handle);
		}
	}

	/**
	 * Create the data set type viewer
	 *
	 * @param composite
	 */
	private void createDataSetTypeViewer(Composite composite) {
		// create the data set type chooser combo
		new Label(composite, SWT.RIGHT).setText(Messages.getString("dataset.wizard.label.datasetType"));//$NON-NLS-1$
		dataSetTypeChooser = new ComboViewer(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		dataSetTypeChooser.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		DataSetTypesProvider dataSetTypes = new DataSetTypesProvider();
		dataSetTypeChooser.setContentProvider(dataSetTypes);
		dataSetTypeChooser.setLabelProvider(dataSetTypes);
		dataSetTypeChooser.getCombo().setEnabled(false);
	}

	/**
	 * checks if the name is duplicate
	 *
	 * @return Returns true if the name is duplicate,and false if it is duplicate
	 */
	private boolean isDuplicateName() {
		String name = nameEditor.getText().trim();
		return Utility.checkDataSetName(name);
	}

	private String getDataSetName() {
		if (dataSetName != null) {
			return dataSetName;
		}
		return (nameEditor.getText());
	}

	/**
	 * whether name contains ".", "/", "\", "!", ";", "," charactors
	 *
	 * @param name
	 * @return
	 */
	private boolean containInvalidCharactor(String name) {
		if (name == null) {
			return false;
		} else if (name.indexOf(".") > -1 || //$NON-NLS-1$
				name.indexOf("\\") > -1 || name.indexOf("/") > -1 || //$NON-NLS-1$ //$NON-NLS-2$
				name.indexOf("!") > -1 || name.indexOf(";") > -1 || //$NON-NLS-1$ //$NON-NLS-2$
				name.indexOf(",") > -1) {
			return true;
		} else {
			return false;
		}
	}

	protected final DataSourceHandle getSelectedDataSource() {
		if (dataSource != null) {
			return dataSource;
		}

		if (((IStructuredSelection) dataSourceFilteredTree.getViewer().getSelection())
				.getFirstElement() instanceof DataSourceHandle) {
			return (DataSourceHandle) ((IStructuredSelection) dataSourceFilteredTree.getViewer().getSelection())
					.getFirstElement();
		} else {
			return null;
		}
	}

	/**
	 * @return Returns the newDataSource.
	 */
	final DataSourceHandle getNewDataSource() {
		return newDataSource;
	}

	/**
	 * @param newDataSourceHandle The newDataSource to set.
	 */
	final void setNewDataSource(DataSourceHandle newDataSourceHandle) {
		this.newDataSource = newDataSourceHandle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizardPage#getNextPage()
	 */
	@Override
	public IWizardPage getNextPage() {
		setPageComplete(true);
		if (((IStructuredSelection) dataSetTypeChooser.getSelection())
				.getFirstElement() instanceof DataSetTypeElement) {
			return getDataSetPage();
		}
		// switch to script data set page
		return super.getNextPage();
	}

	public IWizardPage getDataSetPage() {
		if (useODAV3) {
			nextPage = getNextPageODAV3();
			return nextPage;
		} else {
			nextPage = getNextPageODAV2();
			return nextPage;
		}
	}

	private String dataSetID;
	private DataSourceHandle dataSource;
	private String dataSetName;

	public IWizardPage getExtensionDataSetNextPage(String dataSourceID, String dataSetID, DataSourceHandle dataSource,
			String dataSetName) {
		this.dataSetID = dataSetID;
		this.dataSource = dataSource;
		this.dataSetName = dataSetName;

		setPageComplete(true);
		isUseODAV3(dataSourceID);
		return getDataSetPage();
	}

	/**
	 *
	 * @param dataSourceDesign
	 * @return
	 */
	private IWizardPage getNextPageODAV3() {
		DataSourceDesign dataSourceDesign = new ModelOdaAdapter()
				.createDataSourceDesign((OdaDataSourceHandle) getSelectedDataSource());
		String dataSetID = getDataSetID();
		try {
			DTPUtil.getInstance().supplementDesignAttributes(dataSourceDesign);

			if (m_designSession == null) {
				m_designSession = DataSetDesignSession.startNewDesign(getDataSetName().trim(), dataSetID,
						dataSourceDesign);
			} else {
				m_designSession.restartNewDesign(getDataSetName().trim(), dataSetID, dataSourceDesign);
			}

			return m_designSession.getWizardStartingPage();
		} catch (OdaException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (URISyntaxException e) {
			ExceptionHandler.handle(e);
			return null;
		}
	}

	public String getDataSetID() {
		if (dataSetID != null) {
			return dataSetID;
		}
		OdaDataSetTypeElement dataSetElement = (OdaDataSetTypeElement) getSelectedDataSet();
		return dataSetElement.getDataSetType().getID();
	}

	public void setPageFocus() {
		if (nameEditor != null && !nameEditor.isDisposed()) {
			nameEditor.setFocus();
		}
	}

	public boolean canFinish() {
		if (!validStatus()) {
			return false;
		}

		try {
			if (m_designSession != null) {
				return m_designSession.getNewWizard().canFinish();
			} else {
				if (this.nextPage != null) {
					return nextPage.isPageComplete();
				}
				return isPageComplete();
			}
		} catch (OdaException e) {
			return false;
		}
	}

	/**
	 *
	 * @return
	 */
	private IWizardPage getNextPageODAV2() {
		// Get the currently selected Data Set type and invoke its wizard
		// class
		DataSetTypeElement dataSetElement = (DataSetTypeElement) ((IStructuredSelection) dataSetTypeChooser
				.getSelection()).getFirstElement();

		if (m_designSession != null) {
			m_designSession = null;
		}

		if (dataSetElement instanceof OdaDataSetTypeElement) {
			OdaDataSetTypeElement dElement = (OdaDataSetTypeElement) dataSetElement;
			IConfigurationElement element = dElement.getIConfigurationElement();
			AbstractDataSetWizard newWizard = null;
			if (element != null) {
				newWizard = (AbstractDataSetWizard) htDataSetWizards.get(element.getAttribute("id"));//$NON-NLS-1$
			}
			if (newWizard == null && element != null) {
				// Get the new wizard from this element
				IConfigurationElement[] elements = element.getChildren("newDataSetWizard");//$NON-NLS-1$
				if (elements.length > 0) {
					// Use only the first one.
					// There can only be one data set wizard for a data set
					try {
						Object wizard = elements[0].createExecutableExtension("class");//$NON-NLS-1$
						if (wizard instanceof AbstractDataSetWizard) {
							newWizard = (AbstractDataSetWizard) wizard;
							newWizard.setConfigurationElement(element);
							// Allow the wizard to create its pages
							newWizard.addPages();
							newWizard.setUseTransaction(useTransaction);
							htDataSetWizards.put(element.getAttribute("id"), //$NON-NLS-1$
									newWizard);
						}
					} catch (CoreException e) {
						ExceptionHandler.handle(e);
					}
				}
			}

			if (newWizard != null) {
				newWizard.setDataSource(getSelectedDataSource());
				newWizard.setDataSetName(nameEditor.getText().trim());

				// if the data set has been created
				// update it in the data set as well
				if (newWizard.getDataSet() != null) {
					try {
						newWizard.getDataSet().setDataSource(newWizard.getDataSource().getName());
						newWizard.getDataSet().setName(newWizard.getDataSetName());
					} catch (SemanticException e) {
						ExceptionHandler.handle(e);
					}
				}
				return newWizard.getStartingPage();
			}
		} else {
			IWizardPage page = helper.getNextPage(getSelectedDataSource(), dataSetElement);
			if (page == null) {
				return super.getNextPage();
			} else {
				return page;
			}
		}
		return super.getNextPage();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		return validStatus();
	}

	public boolean validStatus() {
		return this.getMessageType() != ERROR && !StringUtil.isBlank(getDataSetName())
				&& (getSelectedDataSource() != null || dataSource != null)
				&& (getSelectedDataSet() != null || dataSetID != null);
	}

	/**
	 * The action after data source is selected
	 *
	 */
	private void doDataSourceSelectionChanged(Object data) {
		if (data instanceof DataSourceType) {
			DataSetTypeElement[] types = ((DataSourceType) data).getDataSetTypes();
			dataSetTypeChooser.setInput(types);
			dateSetTypeSelection = new StructuredSelection(types[0]);
			dataSetTypeChooser.setSelection(dateSetTypeSelection);
			String dataSourceID = ((DataSourceType) data).getDataSourceID();
			isUseODAV3(dataSourceID);
		}
	}

	private void isUseODAV3(String dataSourceID) {
		if (isScriptDataSet(dataSourceID)) {
			useODAV3 = false;
		} else {
			useODAV3 = DesignSessionUtil.hasValidOdaDesignUIExtension(dataSourceID);
		}
	}

	private Object getSelectedDataSet() {
		return ((IStructuredSelection) dataSetTypeChooser.getSelection()).getFirstElement();
	}

	private boolean hasWizard() {
		DataSetTypeElement dTypeElement = (DataSetTypeElement) getSelectedDataSet();
		if (dTypeElement == null) {
			return false;
		}
		if (dTypeElement instanceof OdaDataSetTypeElement) {
			// Get the currently selected Data Set type and invoke its wizard
			// class
			IConfigurationElement element = ((OdaDataSetTypeElement) dTypeElement).getIConfigurationElement();
			if (element != null) {
				AbstractDataSetWizard newWizard = (AbstractDataSetWizard) htDataSetWizards
						.get(element.getAttribute("id"));//$NON-NLS-1$
				if (newWizard != null) {
					return true;
				}
				// Get the new wizard from this element
				IConfigurationElement[] v3elements = element.getChildren("dataSetWizard");//$NON-NLS-1$
				IConfigurationElement[] v2elements = element.getChildren("newDataSetWizard");//$NON-NLS-1$
				if (v3elements.length > 0 || v2elements.length > 0) {
					return true;
				}
			}
		} else if (isScriptDataSet(dTypeElement.getDataSetTypeName())) {
			return true;
		} else {
			return helper.hasWizard(getSelectedDataSource());
		}
		return false;
	}

	public DataSetHandle createSelectedDataSet() {
		DataSetHandle dataSetHandle = null;

		try {
			if (useODAV3) {
				dataSetHandle = createDataSetODAV3();
			} else {
				dataSetHandle = createDataSetODAV2();
			}

			if (nameEditor != null && !nameEditor.isDisposed()) {
				dataSetHandle.setName(nameEditor.getText());
			}

			return dataSetHandle;
		} catch (SemanticException | IllegalStateException | OdaException e) {
			ExceptionHandler.handle(e);
			return null;
		}
	}

	/**
	 *
	 * @return
	 * @throws OdaException
	 * @throws SemanticException
	 * @throws IllegalStateException
	 */
	private DataSetHandle createDataSetODAV3() throws OdaException, SemanticException, IllegalStateException {
		DesignElementHandle parentHandle = Utility.getReportModuleHandle();

		DataSetHandle dataSetHandle = DTPUtil.getInstance()
				.createOdaDataSetHandle(m_designSession.finish().getResponse(), (ModuleHandle) parentHandle);
		m_designSession = null; // reset

		return dataSetHandle;
	}

	/**
	 *
	 * @return
	 * @throws SemanticException
	 */
	private DataSetHandle createDataSetODAV2() throws SemanticException {
		String dataSetTypeName = null;
		if (getSelectedDataSet() instanceof Object[]) {
			DataSetType dataSetElement = (DataSetType) ((Object[]) getSelectedDataSet())[0];
			dataSetTypeName = dataSetElement.getID();
		} else if (getSelectedDataSet() instanceof OdaDataSetTypeElement) {
			dataSetTypeName = ((OdaDataSetTypeElement) getSelectedDataSet()).getDataSetType().getID();
		} else if (getSelectedDataSet() instanceof DataSetTypeElement) {
			dataSetTypeName = ((DataSetTypeElement) getSelectedDataSet()).getDataSetTypeName();
		}

		return createDataSet(dataSetTypeName);
	}

	private DataSetHandle createDataSet(String dataSetType) throws SemanticException {
		DataSourceHandle source = getSelectedDataSource();
		if (source instanceof OdaDataSourceHandle) {
			OdaDataSetHandle dsHandle = Utility.newOdaDataSet(getDataSetName().trim(), dataSetType);
			dsHandle.setDataSource(source.getQualifiedName());
			dsHandle.setQueryText(""); //$NON-NLS-1$ //Need a empty query in the dataset.
			return dsHandle;
		} else if (source instanceof ScriptDataSourceHandle) {
			ScriptDataSetHandle dsHandle = Utility.newScriptDataSet(getDataSetName());
			dsHandle.setDataSource(source.getName());
			return dsHandle;
		} else {
			return helper.createDataSet(getDataSetName().trim(), dataSetType);
		}
	}

	public String getScriptDataSetName(DataSourceHandle dataSourceHandle) {
		if (dataSourceHandle instanceof ScriptDataSourceHandle) {
			return SCRIPT_DATASET_NAME;
		}
		return null;
	}

	public String getScriptDataSourceName(DataSourceHandle dataSourceHandle) {
		if (dataSourceHandle instanceof ScriptDataSourceHandle) {
			return SCRIPT_DATASOURCE_NAME;
		}
		return null;
	}

	public boolean isScriptDataSet(String dataSourceID) {
		if (SCRIPT_DATASOURCE_NAME.equals(dataSourceID)) {
			return true;
		}
		return false;
	}
}

class DataSetTypesProvider implements IStructuredContentProvider, ILabelProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement != null && (inputElement instanceof DataSetTypeElement[])) {
			return (Object[]) inputElement;
		}
		return new Object[] {};
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof DataSetTypeElement) {
			DataSetTypeElement type = (DataSetTypeElement) element;
			return type.getDataSetTypeName();
		}
		return element.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.
	 * jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang
	 * .Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

}

class WizardFilter extends PatternFilter {

	public WizardFilter() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.dialogs.PatternFilter#isElementVisible(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object)
	 */
	@Override
	public boolean isElementVisible(Viewer viewer, Object element) {
		if (isLeafMatch(viewer, element)) {
			return true;
		}

		if (element instanceof DataSourceType) {
			ITreeContentProvider contentProvider = (ITreeContentProvider) ((TreeViewer) viewer).getContentProvider();
			DataSourceType node = (DataSourceType) element;
			Object[] children = contentProvider.getChildren(node);
			// Will return true if any subnode of the element matches the search
			if (filter(viewer, element, children).length > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean isLeafMatch(Viewer viewer, Object element) {

		String text;
		if (element instanceof DataSourceType) {
			return false;
		} else if (element instanceof DataSourceHandle) {
			DataSourceHandle node = (DataSourceHandle) element;
			text = node.getName();

			if (wordMatches(text)) {
				return true;
			}
		}
		return false;
	}
}

class DataSourceType {

	private String dataSourceID, dataSourceDisplayName;
	private DataSetTypeElement[] dataSetType;
	private List dataSourceList = new ArrayList();

	public DataSourceType(String dataSourceID, String dataSourceDisplayName, DataSetTypeElement[] dataSetType) {
		this.dataSourceID = dataSourceID;
		this.dataSourceDisplayName = dataSourceDisplayName;
		this.dataSetType = dataSetType;
	}

	public String getDataSourceID() {
		return this.dataSourceID;
	}

	public DataSetTypeElement[] getDataSetTypes() {
		return this.dataSetType;
	}

	public String getDataSourceDisplayName() {
		return this.dataSourceDisplayName;
	}

	public void addDataSource(DataSourceHandle handle) {
		dataSourceList.add(handle);
	}

	public List getDataSourceList() {
		return this.dataSourceList;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DataSourceType)) {
			return false;
		}
		if (this.dataSourceID != null && this.dataSourceID.equals((((DataSourceType) obj)).getDataSourceID())) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.dataSourceID == null ? 0 : this.dataSourceID.hashCode();
	}
}

class DataSetTypeElement {

	private String name;

	public DataSetTypeElement(String name) {
		this.name = name;
	}

	public String getDataSetTypeName() {
		return this.name;
	}
}

class OdaDataSetTypeElement extends DataSetTypeElement {

	private DataSetType dataSetType;
	private IConfigurationElement configureElement;

	public OdaDataSetTypeElement(DataSetType dataSetType, IConfigurationElement configureElement) {
		super(dataSetType.getDisplayName());
		this.dataSetType = dataSetType;
		this.configureElement = configureElement;
	}

	public DataSetType getDataSetType() {
		return this.dataSetType;
	}

	public IConfigurationElement getIConfigurationElement() {
		return this.configureElement;
	}
}
