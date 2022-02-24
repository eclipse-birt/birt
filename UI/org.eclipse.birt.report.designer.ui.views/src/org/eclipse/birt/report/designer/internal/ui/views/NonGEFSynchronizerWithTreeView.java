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

package org.eclipse.birt.report.designer.internal.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.mediator.IMediatorColleague;
import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

/**
 * Provides synchronizing between tree view and graphical views.
 */
public class NonGEFSynchronizerWithTreeView implements IMediatorColleague {

	private AbstractTreeViewer viewer;

	// private ListenerList selectionChangedListeners = new ListenerList( );

	private Object source;

	/**
	 * @return Returns the source.
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @param source The source to set.
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	public NonGEFSynchronizerWithTreeView() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		if (getTreeViewer() == null) {
			return StructuredSelection.EMPTY;
		}
		return getTreeViewer().getSelection();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse
	 * .jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		if (getTreeViewer() != null) {
			getTreeViewer().setSelection(selection, false);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		setSelection(event.getSelection());
	}

	/**
	 * select the node
	 *
	 * @param event
	 */
	protected void treeSelect(SelectionChangedEvent event) {
		fireSelectionChanged(event.getSelection());
	}

	/**
	 * Fires a selection changed event.
	 *
	 * @param selection the new selection
	 */
	protected void fireSelectionChanged(ISelection selection) {
		ReportRequest request = new ReportRequest(getSource());
		List list = new ArrayList();
		if (selection instanceof IStructuredSelection) {
			list = ((IStructuredSelection) selection).toList();
		}
		/**
		 * There is no object selected after delete an element not displayed in layout,
		 * such as data set. Then the request has no object to perform. So add a root
		 * element (ReportDesignHandle) as the object for the request to perform if the
		 * select element objects list is empty.
		 */
		if (list.size() < 1) {
			list = new ArrayList();
			list.add(SessionHandleAdapter.getInstance().getModule());
		}
		request.setSelectionObject(list);
		request.setType(ReportRequest.SELECTION);

		SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);
	}

	/**
	 * gets the Tree viewer that hooks on this synchronizer.
	 *
	 * @return tree viewer.
	 */
	public AbstractTreeViewer getTreeViewer() {
		return viewer;
	}

	/**
	 * Hook the tree view need to synchronized
	 *
	 * @param viewer
	 */
	public void setTreeViewer(AbstractTreeViewer viewer) {
		this.viewer = viewer;
		getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				treeSelect(event);
			}

		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.Page#dispose()
	 */
	public void dispose() {
		viewer = null;
	}

	public boolean isInterested(IMediatorRequest request) {
		return request instanceof ReportRequest;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.core.util.mediator.IColleague#performRequest
	 * ( org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest )
	 */
	public void performRequest(IMediatorRequest request) {
		if (ReportRequest.SELECTION.equals(request.getType())) {
			handleSelectionChange((ReportRequest) request);
		} else if (ReportRequest.CREATE_ELEMENT.equals(request.getType())) {
			handleCreateElement((ReportRequest) request);
		}
	}

	protected void handleCreateElement(ReportRequest request) {
		final List list = request.getSelectionObject();
		if (list.size() == 1) {
			Display.getCurrent().asyncExec(new Runnable() {

				public void run() {

					if (viewer.getControl().isDisposed()) {
						return;
					}
					viewer.refresh();
					StructuredSelection selection = new StructuredSelection(list);
					viewer.setSelection(selection);
					// fireSelectionChanged( selection );
				}

			});
		}

	}

	/**
	 * Handles the selection request
	 *
	 * @param request
	 */
	protected void handleSelectionChange(ReportRequest request) {
		if (request.getSource() == getSource()) {
			return;
		}
		List list = request.getSelectionModelList();
		boolean canSetSelection = false;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (UIUtil.containElement(getTreeViewer(), element)) {
				canSetSelection = true;
				break;
			}
		}
		if (canSetSelection) {
			setSelection(new StructuredSelection(list));
		}
	}

}
