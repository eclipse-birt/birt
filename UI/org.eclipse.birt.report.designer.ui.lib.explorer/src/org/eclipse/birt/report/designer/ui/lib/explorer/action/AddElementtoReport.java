/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;

/**
 *
 */

public class AddElementtoReport extends Action {

	private StructuredViewer viewer;
	private Object element;
	private int canContain;
	private Object target;

	private static final String ACTION_TEXT = Messages.getString("AddElementtoAction.Text"); //$NON-NLS-1$

	public void setSelectedElement(Object element) {
		if (element instanceof ReportResourceEntry) {
			this.element = ((ReportResourceEntry) element).getReportElement();
		} else {
			this.element = element;
		}

	}

	/**
	 * @param text
	 * @param style
	 */
	public AddElementtoReport(StructuredViewer viewer) {
		super(ACTION_TEXT);
		this.viewer = viewer;
		canContain = DNDUtil.CONTAIN_NO;
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	@Override
	public boolean isEnabled() {
		Object target = getTarget();
		this.target = target;

		if (canContain(target, element)) {
			return true;
		} else {
			return false;
		}
	}

	public Object getTarget() {
		IViewPart viewPart = UIUtil.getView(IPageLayout.ID_OUTLINE);
		if (!(viewPart instanceof ContentOutline)) {
			return null;
		}
		ContentOutline outlineView = (ContentOutline) viewPart;

		ISelection selection = outlineView.getSelection();
		if (selection instanceof StructuredSelection) {
			StructuredSelection strSelection = (StructuredSelection) selection;
			if (strSelection.size() == 1) {
				return strSelection.getFirstElement();
			}
		}
		return null;
	}

	@Override
	public void run() {
		SessionHandleAdapter.getInstance().getCommandStack().startTrans(ACTION_TEXT);
		try {
			copyData(target, element);
			SessionHandleAdapter.getInstance().getCommandStack().commit();
		} catch (Exception e) {
			SessionHandleAdapter.getInstance().getCommandStack().rollback();
		}

	}

	protected boolean canContain(Object target, Object transfer) {
		// bug#192319
		if (transfer instanceof DataSetHandle || transfer instanceof DataSourceHandle
				|| transfer instanceof ParameterHandle || transfer instanceof ParameterGroupHandle
				|| transfer instanceof CascadingParameterGroupHandle || transfer instanceof CubeHandle
				|| transfer instanceof MasterPageHandle) {
			return true;
		}

		if (target instanceof LibraryHandle && transfer instanceof EmbeddedImageHandle) {
			EmbeddedImageHandle imageHandle = (EmbeddedImageHandle) transfer;
			if (imageHandle.getModule() instanceof Library
					&& !imageHandle.getModule().getFileName().equals(((LibraryHandle) target).getFileName())) {
				return true;
			}
		}
		if (DNDUtil.handleValidateTargetCanContainMore(target, DNDUtil.getObjectLength(transfer))) {
			canContain = DNDUtil.handleValidateTargetCanContain(target, transfer, true);
			return canContain == DNDUtil.CONTAIN_THIS;
		}
		return false;

	}

	private int getPosition(Object target) {

		int position = DNDUtil.calculateNextPosition(target, canContain);
		if (position > -1) {
			this.target = DNDUtil.getDesignElementHandle(target).getContainerSlotHandle();
		}
		return position;
	}

	protected boolean copyData(Object target, Object transfer) {

		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();

		// bug#192319
		if (transfer instanceof DataSetHandle) {
			target = moduleHandle.getDataSets();
		} else if (transfer instanceof DataSourceHandle) {
			target = moduleHandle.getDataSources();
		} else if (transfer instanceof ParameterHandle || transfer instanceof ParameterGroupHandle
				|| transfer instanceof CascadingParameterGroupHandle) {
			target = moduleHandle.getParameters();
		} else if (transfer instanceof CubeHandle) {
			target = moduleHandle.getCubes();
		} else if (transfer instanceof MasterPageHandle) {
			target = moduleHandle.getMasterPages();
		} else if (transfer instanceof DesignElementHandle && getAdapter() != null
				&& getAdapter().resolveExtendedData((DesignElementHandle) transfer) != null) {
			target = getAdapter().getDetailHandle(moduleHandle);
		}

		// When get position, change target value if need be
		int position = getPosition(target);
		boolean result = false;

		if (transfer instanceof DesignElementHandle) {
			DesignElementHandle sourceHandle;
			if ((sourceHandle = (DesignElementHandle) transfer).getRoot() instanceof LibraryHandle) {
				// transfer element from a library.
				LibraryHandle library = (LibraryHandle) sourceHandle.getRoot();
				try {
					if (moduleHandle != library) {
						// element from other library not itself, create a new
						// extended element.
						if (UIUtil.includeLibrary(moduleHandle, library)) {
							DNDUtil.addElementHandle(target, moduleHandle.getElementFactory()
									.newElementFrom(sourceHandle, sourceHandle.getName()));
							result = true;
						}
					} else {
						result = DNDUtil.copyHandles(transfer, target, position);
					}
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			} else {
				result = DNDUtil.copyHandles(transfer, target, position);
			}
		} else if (transfer instanceof EmbeddedImageHandle) {
			EmbeddedImageHandle sourceEmbeddedImageHandle;
			if ((sourceEmbeddedImageHandle = (EmbeddedImageHandle) transfer).getElementHandle()
					.getRoot() instanceof LibraryHandle) {
				LibraryHandle library = (LibraryHandle) sourceEmbeddedImageHandle.getElementHandle().getRoot();
				try {
					if (moduleHandle != library) {
						// create a new embeddedimage from other library and
						// extend it.
						if (UIUtil.includeLibrary(moduleHandle, library)) {
							EmbeddedImage image = StructureFactory.newEmbeddedImageFrom(sourceEmbeddedImageHandle,
									moduleHandle);
							image.setType(sourceEmbeddedImageHandle.getType());
							DNDUtil.addEmbeddedImageHandle(target, image);
							result = true;
						}
					} else {
						result = DNDUtil.copyHandles(transfer, target, position);
					}
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			} else {
				result = DNDUtil.copyHandles(transfer, target, position);
			}
		}

		if (result) {
			viewer.reveal(target);
		}

		return result;
	}

	private IExtendedDataModelUIAdapter getAdapter() {
		return ExtendedDataModelUIAdapterHelper.getInstance().getAdapter();
	}
}
