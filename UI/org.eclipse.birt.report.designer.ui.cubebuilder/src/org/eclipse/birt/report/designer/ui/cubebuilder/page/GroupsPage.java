/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.page;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class GroupsPage extends AbstractCubePropertyPage {

	public static final String GROUPPAGE_MESSAGE = Messages.getString("GroupsPage.Title.Message"); //$NON-NLS-1$
	private TabularCubeHandle input;
	private CubeGroupContent cubeGroup;
	private CubeBuilder builder;

	public GroupsPage(CubeBuilder builder, TabularCubeHandle model) {
		input = model;
		this.builder = builder;
	}

	@Override
	public Control createContents(Composite parent) {
		cubeGroup = getCubeGroupContent(parent);
		return cubeGroup;
	}

	protected CubeGroupContent getCubeGroupContent(Composite parent) {
		Object[] contentProviders = ElementAdapterManager.getAdapters(input, ICubeGroupContentProvider.class);
		if (contentProviders != null) {
			for (int i = 0; i < contentProviders.length; i++) {
				ICubeGroupContentProvider contentProvider = (ICubeGroupContentProvider) contentProviders[i];
				if (contentProvider != null) {
					return contentProvider.createGroupContent(parent, SWT.NONE);
				}
			}
		}
		return new CubeGroupContent(parent, SWT.NONE);
	}

	@Override
	public void pageActivated() {
		UIUtil.bindHelp(builder.getShell(), IHelpContextIds.CUBE_BUILDER_GROUPS_PAGE);
		getContainer().setMessage(Messages.getString("GroupsPage.Container.Title.Message"), //$NON-NLS-1$
				IMessageProvider.NONE);
		builder.setTitleTitle(Messages.getString("GroupsPage.Title.Title")); //$NON-NLS-1$
		builder.setErrorMessage(null);
		builder.setTitleMessage(GROUPPAGE_MESSAGE);
		load();
	}

	private void load() {
		if (input != null) {
			cubeGroup.setInput(input, null);
			cubeGroup.load();
		}
	}
}
