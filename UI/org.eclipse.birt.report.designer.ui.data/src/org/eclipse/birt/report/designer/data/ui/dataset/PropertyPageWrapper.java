/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.report.designer.data.ui.datasource.DataSourceEditor;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionResponse;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DataSetDesignSession;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DataSourceDesignSession;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetEditorPage;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * To wrap PropertyPage to IPropertyPage
 */

public class PropertyPageWrapper extends AbstractPropertyPage {

	private PropertyPage propertyPage = null;
	private DataSetDesignSession dataSetSession = null;
	private DataSourceDesignSession dataSourceSession = null;

	public PropertyPageWrapper(PropertyPage propertyPage, DataSetDesignSession m_designSession) {
		this.propertyPage = propertyPage;
		this.dataSetSession = m_designSession;
	}

	public PropertyPageWrapper(PropertyPage propertyPage, DataSourceDesignSession m_designSession) {
		this.propertyPage = propertyPage;
		this.dataSourceSession = m_designSession;
	}

	@Override
	public Control createPageControl(Composite parent) {
		propertyPage.setContainer((IPreferencePageContainer) getContainer());
		propertyPage.createControl(parent);

		return propertyPage.getControl();
	}

	@Override
	public void pageActivated() {
		if (propertyPage instanceof DataSetEditorPage) {
			((DataSetEditorPage) propertyPage).refresh();
		} else if (propertyPage instanceof DataSourceEditorPage) {
			((DataSourceEditorPage) propertyPage).refresh();
		}

		getContainer().setMessage(propertyPage.getMessage(), propertyPage.getMessageType());
	}

	public PropertyPage getPropertyPage() {
		return propertyPage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * getToolTip()
	 */
	@Override
	public String getToolTip() {
		return propertyPage.getTitle();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#performOk()
	 */
	@Override
	public boolean performOk() {
		return canLeave();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage
	 * #canLeave()
	 */
	@Override
	public boolean canLeave() {
		if (propertyPage instanceof DataSetEditorPage) {
			boolean okToLeave = ((DataSetEditorPage) propertyPage).okToLeave();
			if (okToLeave) {
				try {
					DataSetDesign requestDesign = null;
					DesignSessionResponse response = null;
					if (dataSetSession != null) {
						requestDesign = dataSetSession.getRequest().getDataSetDesign();
						response = dataSetSession.flush().getResponse();
					} else {
						response = ((DataSetEditorPage) propertyPage).collectPageResponse();
					}

					DTPUtil.getInstance().updateDataSetHandle(response, requestDesign,
							(OdaDataSetHandle) ((DataSetEditor) getContainer()).getModel(), false);
				} catch (OdaException e) {
					ExceptionHandler.handle(e);
				}
			}
			return okToLeave;
		}

		if (propertyPage instanceof DataSourceEditorPage) {
			if (propertyPage.okToLeave()) {
				try {
					DataSourceDesign requestDesign = null;
					if (this.dataSourceSession != null) {
						requestDesign = this.dataSourceSession.getRequest().getDataSourceDesign();
					}

					DTPUtil.getInstance().updateDataSourceHandle(
							((DataSourceEditorPage) propertyPage).getEditSessionResponse().getResponse(), requestDesign,
							(OdaDataSourceHandle) (getContainer()).getModel());
					((DataSourceEditor) this.getContainer()).updateDesignSession();
				} catch (OdaException e) {
					ExceptionHandler.handle(e);
				}
			}
			return propertyPage.okToLeave();
		}

		return super.canLeave();
	}

}
