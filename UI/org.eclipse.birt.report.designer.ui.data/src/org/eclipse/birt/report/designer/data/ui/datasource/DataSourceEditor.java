/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.datasource;

import java.net.URISyntaxException;

import org.eclipse.birt.report.designer.data.ui.dataset.PropertyPageWrapper;
import org.eclipse.birt.report.designer.data.ui.property.AbstractPropertyDialog;
import org.eclipse.birt.report.designer.data.ui.property.PropertyNode;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.IHelpConstants;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionRequest;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DataSourceDesignSession;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Dialog to edit datasource properties.
 */
@SuppressWarnings("deprecation")
public class DataSourceEditor extends AbstractPropertyDialog implements IPreferencePageContainer {

	protected DataSourceDesignSession m_designSession = null;
	protected OdaDataSourceHandle dataSourceHandle = null;

	protected boolean needRememberLastSize() {
		return true;
	}

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 */
	public DataSourceEditor(Shell parentShell, DataSourceHandle ds) {
		super(parentShell, ds);

		if (ds instanceof OdaDataSourceHandle) {
			dataSourceHandle = (OdaDataSourceHandle) ds;
			assert dataSourceHandle != null;

			String dataSourceType = dataSourceHandle.getExtensionID();
			addPagesToOdaDataSource(dataSourceType);
		} else {
			IPropertyPage[] pages = DataSourceEditorHelper.getExternalPages(ds);
			for (int i = 0; i < pages.length; i++) {
				addPageTo("/", //$NON-NLS-1$
						pages[i].getName(), pages[i].getName(), null, pages[i]);
			}
		}
	}

	protected void addPagesToOdaDataSource(String dataSourceType) {
		if (DesignSessionUtil.hasValidOdaDesignUIExtension(dataSourceType)) {
			addCustomPageODAV3(dataSourceHandle);
			if (supportsPropertyBindingPage())
				addPageTo("/", "org.eclipse.birt.datasource.editor.property", //$NON-NLS-1$ //$NON-NLS-2$
						Messages.getString("datasource.editor.property"), null, new PropertyBindingPage());//$NON-NLS-1$
		} else {
			IConfigurationElement element = DataSetProvider.findDataSourceElement(dataSourceType);

			if (element != null)
				addCustomPageODAV2(element);
		}
	}

	protected boolean supportsPropertyBindingPage() {
		return true;
	}

	/**
	 * 
	 * @param dataSourceDesign
	 */
	private void addCustomPageODAV3(OdaDataSourceHandle dataSourceHandle) {
		try {
			DesignSessionRequest designSessionRequest = DTPUtil.getInstance()
					.createDesignSessionRequest(dataSourceHandle);
			m_designSession = DataSourceDesignSession.startEditDesign(designSessionRequest);
			PropertyPageWrapper customPage = new PropertyPageWrapper(m_designSession.getEditorPage(), m_designSession);

			DataSourceDesign dataSourceDesign = designSessionRequest.getDataSourceDesign();
			addPageTo("/", //$NON-NLS-1$
					dataSourceDesign.getName(), customPage.getPropertyPage().getTitle(), null, customPage);

			// add the ODA profile selection editor page
			PropertyPageWrapper profileSelectionPage = new PropertyPageWrapper(
					m_designSession.getProfileSelectionPropertyPage(), m_designSession);
			String profilePageNodeId = profileSelectionPage.getPropertyPage().getClass().getName();
			addPageTo("/", //$NON-NLS-1$
					profilePageNodeId, profileSelectionPage.getPropertyPage().getTitle(), null, profileSelectionPage);

			boolean isProfileValid = m_designSession.setAndVerifyUseProfileSelectionPage();
			if (!isProfileValid) {
				setDefaultNode(profilePageNodeId);
			}
		} catch (OdaException e) {
			ExceptionHandler.handle(e);
		} catch (URISyntaxException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * 
	 * @param element
	 */
	private void addCustomPageODAV2(IConfigurationElement element) {
		try {
			IConfigurationElement[] editorPages = element.getChildren("dataSourceEditorPage");//$NON-NLS-1$
			if (editorPages != null && editorPages.length > 0) {
				if (editorPages != null) {
					for (int n = 0; n < editorPages.length; n++) {
						IPropertyPage page = (IPropertyPage) editorPages[n].createExecutableExtension("class");//$NON-NLS-1$
						addPageTo(editorPages[n].getAttribute("path"), editorPages[n].getAttribute("name"), //$NON-NLS-1$ //$NON-NLS-2$
								editorPages[n].getAttribute("displayName"), null, page);//$NON-NLS-1$
					}

				}

			}

		} catch (CoreException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * @throws OdaException
	 * @throws URISyntaxException
	 * 
	 */
	public void updateDesignSession() throws OdaException {
		// To start a new design session, firstly clean up the older design
		// session to release the resource.
		if (this.m_designSession != null)
			m_designSession.finish();

		DesignSessionRequest designSessionRequest;
		try {
			designSessionRequest = DTPUtil.getInstance().createDesignSessionRequest((OdaDataSourceHandle) getModel());
		} catch (URISyntaxException e) {
			throw new OdaException(e);
		}
		this.m_designSession = DataSourceDesignSession.startEditDesign(designSessionRequest);
		// Always allow to link external profile
		boolean externalWithProfile = true;
		// if ( designSessionRequest.getDataSourceDesign( ).hasLinkToProfile( )
		// )
		// externalWithProfile = true;
		this.populateDataSourceEditorPage(externalWithProfile);
	}

	/**
	 * 
	 * @param linkedToProfile
	 * @throws OdaException
	 */
	private void populateDataSourceEditorPage(boolean linkedToProfile) throws OdaException {
		if (m_designSession == null) {
			return;
		}
		DataSourceEditorPage dataSourceEditorPages;
		try {
			dataSourceEditorPages = (DataSourceEditorPage) m_designSession.getEditorPage();
		} catch (OdaException e) {
			return;
		}

		PropertyPageWrapper propertyPageWrapper = new PropertyPageWrapper(dataSourceEditorPages, m_designSession);

		// First call ok on all the pages
		if (super.rootNode.hasSubNodes()) {
			PropertyNode[] nodes = rootNode.getSubNodes();

			nodes[0].setPage(propertyPageWrapper);
			nodes[0].setContainer(this);
			nodes[0].createPageControl(getPropertyPane());
		}

		// add the ODA profile selection editor page
		PropertyPageWrapper profileSelectionPage = new PropertyPageWrapper(
				m_designSession.getProfileSelectionPropertyPage(), m_designSession);
		m_designSession.setUseProfileSelectionPage(linkedToProfile);

		PropertyNode[] nodes = rootNode.getSubNodes();
		if (nodes != null && nodes.length >= 2) {
			nodes[1].setPage(profileSelectionPage);
			nodes[1].setContainer(this);
			nodes[1].createPageControl(getPropertyPane());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyDialog
	 * #performOk()
	 */
	public boolean performOk() {
		if (m_designSession != null) {
			try {
				DataSourceDesign requestDesign = m_designSession.getRequest().getDataSourceDesign();
				DTPUtil.getInstance().updateDataSourceHandle(m_designSession.finish().getResponse(), requestDesign,
						dataSourceHandle);
			} catch (OdaException e) {
				ExceptionHandler.handle(e);
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyDialog
	 * #performCancel()
	 */
	public boolean performCancel() {
		if (m_designSession != null) {
			m_designSession.cancel();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createContents(Composite parent) {
		String title = Messages.getFormattedString("datasource.edit", //$NON-NLS-1$
				new String[] { ((DataSourceHandle) getModel()).getName() });
		getShell().setText(title);

		Control control = super.createContents(parent);
		Utility.setSystemHelp(control, IHelpConstants.CONEXT_ID_DATASOURCE_EDIT);

		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#getPreferenceStore
	 * ()
	 */
	public IPreferenceStore getPreferenceStore() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateButtons()
	 */
	public void updateButtons() {
		if (getOkButton() != null) {
			PropertyPage propertyPage = getCurrentPropertyPage();
			if (propertyPage != null)
				getOkButton().setEnabled(propertyPage.isValid());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateMessage()
	 */
	public void updateMessage() {
		PropertyPage propertyPage = getCurrentPropertyPage();
		if (propertyPage != null) {
			String message = propertyPage.getMessage();
			int messageType = propertyPage.getMessageType();

			// if error message exists, it takes precedence over page's non-error message
			if (messageType < IMessageProvider.ERROR) {
				String errMessage = propertyPage.getErrorMessage();
				if (errMessage != null) {
					message = errMessage;
					messageType = IMessageProvider.ERROR;
				}
			}

			setMessage(message, messageType);
		}
	}

	/**
	 * 
	 * @return
	 */
	private PropertyPage getCurrentPropertyPage() {
		if (getCurrentNode() == null)
			return null;
		IPropertyPage currentPage = getCurrentNode().getPage();
		if (!(currentPage instanceof PropertyPageWrapper))
			return null;

		return ((PropertyPageWrapper) currentPage).getPropertyPage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateTitle()
	 */
	public void updateTitle() {
		// TODO Auto-generated method stub

	}

	protected Control createButtonBar(Composite parent) {
		Control container = super.createButtonBar(parent);
		updateButtons();
		return container;
	}

	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.DATA_SOURCE_EDITOR_ID);
		return super.createDialogArea(parent);
	}
}
