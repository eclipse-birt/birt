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

import org.eclipse.birt.report.designer.core.mediator.IMediatorColleague;
import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.mediator.MediatorManager;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.FilterListDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DatasetSelectionPage extends AbstractCubePropertyPage {

	private static final String NEW_DATA_SET = Messages.getString("DatasetSelectionPage.Combo.NewDataSet0"); //$NON-NLS-1$
	private CubeHandle input;
	private Combo dataSetCombo;
	private Text nameText;
	private CubeBuilder builder;
	private Button filterButton;
	private Button primaryKeyButton;
	private Label primaryKeyLabel, primaryKeyHint;

	public DatasetSelectionPage(CubeBuilder builder, CubeHandle model) {
		input = model;
		this.builder = builder;
	}

	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		layout.marginRight = 20;
		container.setLayout(layout);

		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(Messages.getString("DatasetPage.Label.Name")); //$NON-NLS-1$
		nameText = new Text(container, SWT.BORDER);
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				try {
					input.setName(nameText.getText());
					builder.setErrorMessage(null);
					builder.setTitleMessage(Messages.getString("DatasetPage.Title.Message")); //$NON-NLS-1$
				} catch (NameException e1) {
					if (nameText.getText().trim().length() == 0)
						builder.setErrorMessage(Messages.getString("DatasePage.EmptyName.ErrorMessage")); //$NON-NLS-1$
					else
						builder.setErrorMessage(e1.getLocalizedMessage());
				}
			}

		});

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		nameText.setLayoutData(data);

		Label dateSetLabel = new Label(container, SWT.NONE);
		dateSetLabel.setText(Messages.getString("DatasetPage.Label.PrimaryDataset")); //$NON-NLS-1$
		dataSetCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		dataSetCombo.setLayoutData(data);
		dataSetCombo.setVisibleItemCount(30);
		dataSetCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleDatasetComboSelectedEvent();
			}

		});

		filterButton = new Button(container, SWT.PUSH);
		filterButton.setText(Messages.getString("DatasetPage.Button.Filter")); //$NON-NLS-1$
		data = new GridData();
		data.widthHint = Math.max(60, filterButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		filterButton.setLayoutData(data);
		filterButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
				stack.startTrans(""); //$NON-NLS-1$

				FilterHandleProvider provider = (FilterHandleProvider) ElementAdapterManager.getAdapter(builder,
						FilterHandleProvider.class);
				if (provider == null)
					provider = new FilterHandleProvider();

				FilterListDialog dialog = new FilterListDialog(provider);
				dialog.setInput(input);
				if (dialog.open() == Window.OK) {
					stack.commit();
				} else
					stack.rollback();
			}

		});

		filterButton.setEnabled(false);

		new Label(container, SWT.NONE);

		primaryKeyButton = new Button(container, SWT.CHECK);
		primaryKeyButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				try {
					((TabularCubeHandle) input).setAutoPrimaryKey(primaryKeyButton.getSelection());
				} catch (SemanticException e1) {
					ExceptionHandler.handle(e1);
				}
			}

		});

		primaryKeyLabel = new Label(container, SWT.WRAP);
		data = new GridData(SWT.FILL, SWT.NONE, false, false);
		data.horizontalSpan = 2;
		data.widthHint = 400;
		primaryKeyLabel.setLayoutData(data);
		primaryKeyLabel.setText(Messages.getString("DatasetSelectionPage.Label.Auto.Primary.Key")); //$NON-NLS-1$

		primaryKeyLabel.addTraverseListener(new TraverseListener() {

			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_MNEMONIC && e.doit) {
					e.detail = SWT.TRAVERSE_NONE;
					primaryKeyButton.setSelection(!primaryKeyButton.getSelection());
				}
			}
		});

		new Label(container, SWT.NONE);

		primaryKeyHint = new Label(container, SWT.WRAP);
		data = new GridData(SWT.FILL, SWT.NONE, false, false);
		data.horizontalSpan = 3;
		data.widthHint = 400;
		primaryKeyHint.setLayoutData(data);
		primaryKeyHint.setText(Messages.getString("DatasetSelectionPage.Text.Auto.Primary.Key")); //$NON-NLS-1$
		primaryKeyHint.setForeground(ColorManager.getColor(128, 128, 128));

		FontData fontData = primaryKeyHint.getFont().getFontData()[0];
		Font font = new Font(parent.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.ITALIC));
		primaryKeyHint.setFont(font);

		return container;
	}

	public void pageActivated() {
		UIUtil.bindHelp(builder.getShell(), IHelpContextIds.CUBE_BUILDER_DATASET_SELECTION_PAGE);
		getContainer().setMessage(Messages.getString("DatasetPage.Container.Title.Message"), //$NON-NLS-1$
				IMessageProvider.NONE);
		builder.setTitleTitle(Messages.getString("DatasetPage.Title.Title")); //$NON-NLS-1$
		builder.setErrorMessage(null);
		builder.setTitleMessage(Messages.getString("DatasetPage.Title.Message")); //$NON-NLS-1$
		load();
	}

	private void refresh() {
		// dataSetCombo.setItems( input.getAvailableDatasetsName( ) );
		// dataSetCombo.select( input.getIndexOfPrimaryDataset( ) );
		if (dataSetCombo != null && !dataSetCombo.isDisposed()) {
			dataSetCombo.setItems(OlapUtil.getAvailableDatasetNames());
			dataSetCombo.add(NEW_DATA_SET);
			if (((TabularCubeHandle) input).getDataSet() != null) {
				String datasetName = ((TabularCubeHandle) input).getDataSet().getQualifiedName();
				if (dataSetCombo.indexOf(datasetName) == -1) {
					dataSetCombo.add(datasetName, 0);
				}
				dataSetCombo.setText(datasetName);
			}
			if (dataSetCombo.getSelectionIndex() == -1) {
				if (dataSetCombo.getItemCount() == 2) {
					dataSetCombo.select(0);
					if (((TabularCubeHandle) input).getDataSet() == null) {
						handleDatasetComboSelectedEvent();
					}
				}
			}
			if (dataSetCombo.getSelectionIndex() == -1) {
				builder.setOKEnable(false);
				filterButton.setEnabled(false);
			} else {
				builder.setOKEnable(true);
				filterButton.setEnabled(true);
			}
		}
		primaryKeyButton.setSelection(((TabularCubeHandle) input).autoPrimaryKey());
	}

	private void load() {
		if (input != null) {
			if (input.getName() != null)
				nameText.setText(input.getName());
			refresh();
		}
	}

	private void setDataset(String datasetName) {
		if (dataSetCombo.getSelectionIndex() == -1) {
			builder.setOKEnable(false);
			filterButton.setEnabled(false);
		} else {
			try {
				((TabularCubeHandle) input).setDataSet(OlapUtil.getDataset(datasetName));
			} catch (SemanticException e1) {
				ExceptionUtil.handle(e1);
			}
			builder.setOKEnable(true);
			filterButton.setEnabled(true);
		}
	}

	private void handleRequest(ReportRequest request) {
		if (ReportRequest.CREATE_ELEMENT.equals(request.getType())) {
			Object obj = DEUtil.getInputFirstElement(request.getSelectionObject());
			if (obj instanceof DataSetHandle) {
				dataSetCombo.removeAll();
				refresh();
				dataSetCombo.setText(((DataSetHandle) obj).getQualifiedName());
				setDataset(dataSetCombo.getItem(dataSetCombo.getSelectionIndex()));
			}
		}
	}

	private void handleDatasetComboSelectedEvent() {
		if (dataSetCombo.getItemCount() == 0)
			return;
		String datasetName = dataSetCombo.getItem(dataSetCombo.getSelectionIndex());
		if (NEW_DATA_SET.equals(datasetName)) {

			IMediatorColleague colleague = new IMediatorColleague() {

				public boolean isInterested(IMediatorRequest request) {
					return request instanceof ReportRequest;
				}

				public void performRequest(IMediatorRequest request) {
					handleRequest((ReportRequest) request);
				}

			};

			MediatorManager.addGlobalColleague(colleague);

			dataSetCombo.removeAll();
			refresh();

			DataService.getInstance().createDataSet();

			MediatorManager.removeGlobalColleague(colleague);

			return;
		}
		setDataset(datasetName);
	}

}
