/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.page;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.FilterListDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.GraphicalEditPartsFactory;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.GraphicalViewerKeyHandler;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.DatasetNodeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.HierarchyNodeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class LinkGroupsPage extends AbstractCubePropertyPage {

	private CubeHandle input;
	private CubeBuilder builder;

	public LinkGroupsPage(CubeBuilder builder, CubeHandle model) {
		input = model;
		this.builder = builder;
	}

	public Control createContents(Composite parent) {
		Composite contents = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		layout.marginWidth = 10;
		layout.marginTop = 10;
		layout.numColumns = 2;
		contents.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH);
		contents.setLayoutData(data);

		createCubeArea(contents);

		filterButton = new Button(contents, SWT.PUSH);
		filterButton.setText(Messages.getString("DatasetPage.Button.Filter")); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.widthHint = Math.max(60, filterButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
		filterButton.setLayoutData(gd);
		filterButton.setEnabled(false);
		filterButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				EditPart editPart = (EditPart) viewer.getSelectedEditParts().get(0);
				CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
				stack.startTrans(""); //$NON-NLS-1$

				FilterHandleProvider provider = (FilterHandleProvider) ElementAdapterManager.getAdapter(builder,
						FilterHandleProvider.class);
				if (provider == null)
					provider = new FilterHandleProvider();

				FilterListDialog dialog = new FilterListDialog(provider);
				if (editPart instanceof DatasetNodeEditPart)
					dialog.setInput((ReportElementHandle) (editPart.getParent().getModel()));
				else if (editPart instanceof HierarchyNodeEditPart)
					dialog.setInput((ReportElementHandle) (editPart.getModel()));
				if (dialog.open() == Window.OK) {
					stack.commit();
				} else
					stack.rollback();
			}

		});
		return contents;
	}

	private Composite createCubeArea(Composite parent) {
		Composite viewerContent = new Composite(parent, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 500;
		gd.heightHint = 300;
		viewerContent.setLayoutData(gd);
		viewerContent.setLayout(new FillLayout());
		viewer = new ScrollingGraphicalViewer();
		EditDomain editDomain = new EditDomain();
		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		viewer.setRootEditPart(root);
		viewer.setEditDomain(editDomain);
		viewer.createControl(viewerContent);
		viewer.getControl().setBackground(ColorConstants.listBackground);
		factory = new GraphicalEditPartsFactory();
		viewer.setEditPartFactory(factory);
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() != null) {
					StructuredSelection selection = (StructuredSelection) event.getSelection();
					if (selection.getFirstElement() instanceof HierarchyNodeEditPart
							|| selection.getFirstElement() instanceof DatasetNodeEditPart) {
						Object obj = selection.getFirstElement();
						if (obj instanceof HierarchyNodeEditPart) {
							TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) ((HierarchyNodeEditPart) obj)
									.getModel();
							if (hierarchy.getPrimaryKeys() != null && hierarchy.getPrimaryKeys().size() > 0) {
								filterButton.setEnabled(false);
							} else
								filterButton.setEnabled(true);
						} else
							filterButton.setEnabled(true);
					} else
						filterButton.setEnabled(false);
				} else
					filterButton.setEnabled(false);
			}
		});
		load();
		return viewerContent;
	}

	private ScrollingGraphicalViewer viewer;
	private GraphicalEditPartsFactory factory;
	private Button filterButton;

	public void pageActivated() {
		UIUtil.bindHelp(builder.getShell(), IHelpContextIds.CUBE_BUILDER_LINK_GROUPS_PAGE);
		getContainer().setMessage(Messages.getString("LinkGroupsPage.Container.Title.Message"), //$NON-NLS-1$
				IMessageProvider.NONE);
		builder.setTitleTitle(Messages.getString("LinkGroupsPage.Title.Title")); //$NON-NLS-1$
		builder.setErrorMessage(null);
		builder.setTitleMessage(Messages.getString("LinkGroupsPage.Title.Message")); //$NON-NLS-1$
		load();
	}

	private void load() {
		if (input != null && ((TabularCubeHandle) input).getDataSet() != null) {
			viewer.setContents(input);
		}
	}

}
