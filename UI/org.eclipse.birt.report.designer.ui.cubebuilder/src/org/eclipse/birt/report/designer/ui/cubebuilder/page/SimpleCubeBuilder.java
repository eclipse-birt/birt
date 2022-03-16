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
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SimpleCubeBuilder extends TitleAreaDialog {

	public SimpleCubeBuilder(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
	}

	private TabularCubeHandle cube;
	private DataSetHandle dataset;
	private Text nameText;
	private CubeGroupContent group;

	public void setInput(TabularCubeHandle cube, DataSetHandle dataset) {
		this.cube = cube;
		this.dataset = dataset;
		try {
			cube.setDataSet(dataset);
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new GridLayout());

		this.setTitle(Messages.getString("SimpleCubeBuilder.Title.Title")); //$NON-NLS-1$
		this.setMessage(Messages.getString("SimpleCubeBuilder.Title.Message")); //$NON-NLS-1$

		createNameArea(container);
		group = getCubeGroupContent(container);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));

		initDialog();

		UIUtil.bindHelp(parent, IHelpContextIds.SIMPLE_CUBE_BUILDER_ID);
		return area;
	}

	protected CubeGroupContent getCubeGroupContent(Composite parent) {
		Object[] contentProviders = ElementAdapterManager.getAdapters(cube, ICubeGroupContentProvider.class);
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

	private void initDialog() {
		if (cube != null) {
			nameText.setText(cube.getName() == null ? "" : cube.getName()); //$NON-NLS-1$
			group.setInput(cube, dataset);
			group.load();
		}

	}

	protected void createNameArea(Composite parent) {

		Composite nameArea = new Composite(parent, SWT.NONE);
		nameArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 10;
		nameArea.setLayout(layout);

		Label nameLabel = new Label(nameArea, SWT.NONE);
		nameLabel.setText(Messages.getString("SimpleCubeBuilder.Label.Name")); //$NON-NLS-1$

		nameText = new Text(nameArea, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		nameText.setLayoutData(gd);
		nameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Button finishButton = getButton(IDialogConstants.OK_ID);
				if (!nameText.getText().trim().equals("")) //$NON-NLS-1$
				{
					String name = nameText.getText().trim();
					try {
						cube.setName(name);
						if (finishButton != null) {
							finishButton.setEnabled(true);
						}
						SimpleCubeBuilder.this.setErrorMessage(null);
						SimpleCubeBuilder.this.setMessage(Messages.getString("SimpleCubeBuilder.Title.Message")); //$NON-NLS-1$
					} catch (NameException e1) {
						SimpleCubeBuilder.this.setErrorMessage(e1.getMessage());
						if (finishButton != null) {
							finishButton.setEnabled(false);
						}
					}
					group.refresh();

				} else if (finishButton != null) {
					finishButton.setEnabled(false);
				}
			}

		});

		Label space = new Label(nameArea, SWT.NONE);
		gd = new GridData();
		gd.widthHint = 70;
		space.setLayoutData(gd);

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setText(IDialogConstants.FINISH_LABEL);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.getString("SimpleCubeBuilder.Title")); //$NON-NLS-1$
	}

}
