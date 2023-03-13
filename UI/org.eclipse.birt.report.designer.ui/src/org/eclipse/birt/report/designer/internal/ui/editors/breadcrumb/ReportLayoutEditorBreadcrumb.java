/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.DefaultBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.DesignerBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionGroup;

public class ReportLayoutEditorBreadcrumb extends EditorBreadcrumb implements IValidationListener {

	private ProblemBreadcrumbViewer fViewer;

	private class ProblemBreadcrumbViewer extends BreadcrumbViewer {

		public ProblemBreadcrumbViewer(Composite parent, int style) {
			super(parent, style);
		}

		/*
		 * @see org.eclipse.jdt.internal.ui.javaeditor.breadcrumb.BreadcrumbViewer
		 * #configureDropDownViewer(org.eclipse.jface.viewers.TreeViewer,
		 * java.lang.Object)
		 */
		@Override
		public void configureDropDownViewer(TreeViewer viewer, Object input) {
			BreadcrumbViewTreeProvider provier = new BreadcrumbViewTreeProvider(getEditor());
			viewer.setContentProvider(provier);
			viewer.setLabelProvider(provier);
		}

		@Override
		protected int buildItemChain(Object element) {
			if (element != null && !getBreadcrumbNodeProvider(getEditor().getGraphicalViewer()).validate(element)) {
				if (getEditor().getGraphicalViewer().getRootEditPart().getChildren().size() == 1) {
					return super.buildItemChain(
							getEditor().getGraphicalViewer().getRootEditPart().getChildren().get(0));
				} else {
					return 0;
				}
			}
			return super.buildItemChain(element);
		}
	}

	public ReportLayoutEditorBreadcrumb(GraphicalEditorWithFlyoutPalette editor) {
		super(editor);
	}

	private DefaultBreadcrumbNodeProvider provider;

	public DefaultBreadcrumbNodeProvider getBreadcrumbNodeProvider(Object element) {
		if (provider == null) {
			provider = new DesignerBreadcrumbNodeProvider();
		}
		provider.setContext(element);
		return provider;
	}

	public void setBreadcrumbNodeProvider(DefaultBreadcrumbNodeProvider provider) {
		this.provider = provider;
	}

	@Override
	protected Object getCurrentInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BreadcrumbViewer createViewer(Composite parent) {
		BreadcrumbViewTreeProvider provier = new BreadcrumbViewTreeProvider(getEditor());
		fViewer = new ProblemBreadcrumbViewer(parent, SWT.HORIZONTAL);
		fViewer.setContentProvider(provier);
		fViewer.setLabelProvider(provier);

		if (getEditor() != null) {
			Object model = getEditor().getGraphicalViewer().getRootEditPart().getContents().getModel();
			if (model instanceof ModuleHandle) {
				((ModuleHandle) model).addValidationListener(this);
			}
		}

		return fViewer;
	}

	@Override
	public void dispose() {
		if (getEditor() != null) {
			Object model = getEditor().getGraphicalViewer().getRootEditPart().getContents().getModel();
			if (model instanceof ModuleHandle) {
				((ModuleHandle) model).removeValidationListener(this);
			}
		}
		super.dispose();
	}

	@Override
	protected boolean reveal(Object element) {
		boolean flag = false;
		List list = new ArrayList();
		list.add(getBreadcrumbNodeProvider(getEditor().getGraphicalViewer()).getRealModel(element));

		if (fViewer != null && (fViewer.getDropDownShell() == null
				|| fViewer.getDropDownShell() != null && !fViewer.getDropDownShell().isDisposed())) {
			flag = true;

			ReportRequest r = new ReportRequest(this);

			r.setSelectionObject(list);
			r.setType(ReportRequest.SELECTION);

			SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);
		}

		return flag;
	}

	@Override
	protected boolean open(Object element) {
		boolean flag = false;
		List list = getEditor().getGraphicalViewer().getSelectedEditParts();
		if (list.size() == 1) {
			if (getBreadcrumbNodeProvider(getEditor().getGraphicalViewer()).getEditPart(element) != list.get(0)) {
				return false;
			}
			EditPart editPart = (EditPart) list.get(0);
			Request request = new Request(org.eclipse.gef.RequestConstants.REQ_OPEN);
			if (editPart.understandsRequest(request)) {
				editPart.performRequest(request);
				return true;
			}
		}
		return flag;
	}

	@Override
	protected ActionGroup createContextMenuActionGroup(ISelectionProvider selectionProvider) {
		return null;
	}

	@Override
	protected void activateBreadcrumb() {

	}

	@Override
	protected void deactivateBreadcrumb() {

	}

	@Override
	protected void createContextMenu(Object element, MenuManager manager) {
		Object model = getBreadcrumbNodeProvider(getEditor().getGraphicalViewer()).getRealModel(element);
		ProviderFactory.createProvider(model).createContextMenu(null, model, manager);
	}

	@Override
	public void elementValidated(DesignElementHandle targetElement, ValidationEvent ev) {
		if (fBreadcrumbViewer != null && fBreadcrumbViewer.getControl() != null
				&& !fBreadcrumbViewer.getControl().isDisposed()) {
			fBreadcrumbViewer.refresh();
		}
	}
}
