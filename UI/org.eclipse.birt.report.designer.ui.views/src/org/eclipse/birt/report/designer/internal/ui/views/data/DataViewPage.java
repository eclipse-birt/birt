/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.mediator.IMediator;
import org.eclipse.birt.report.designer.core.mediator.IMediatorColleague;
import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.ui.views.data.IDataViewPage;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

/**
 * Represents the data view page.
 * 
 * 
 */
public abstract class DataViewPage extends Page implements IDataViewPage, ISelectionProvider, IMediatorColleague {

	private TreeViewer treeViewer;
	private ModuleHandle model;

	private ListenerList selectionChangedListeners = new ListenerList(ListenerList.IDENTITY);

	/**
	 * Creates the SWT control for this page under the given parent control.
	 * 
	 * @param parent the parent control
	 */
	public void createControl(Composite parent) {
		treeViewer = createTreeViewer(parent);
		getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				treeSelect(event);
			}

		});

		treeViewer.getTree().addListener(SWT.PaintItem, new Listener() {

			public void handleEvent(Event event) {
				// Fix bug 192094
				TreeItem item = (TreeItem) event.item;
				if (item == null)
					return;
				INodeProvider provider = null;
				if (item != null && item.getData() != null) {
					provider = ProviderFactory.createProvider(item.getData());
				}
				if (provider != null && item != null && provider.isReadOnly(item.getData())) {
					Color gray = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
					if (!item.getForeground().equals(gray)) {
						item.setForeground(gray);
					}
				} else {
					Color black = ReportColorConstants.ReportForeground;
					if (item != null && !black.equals(item.getForeground())) {
						item.setForeground(black);
					}
				}
			}
		});

		configTreeViewer();
		hookTreeViewer();
		initPage();

		// suport the mediator
		if (model != null)
			SessionHandleAdapter.getInstance().getMediator(model).addColleague(this);
	}

	protected void initPage() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.IPageBookViewPage#init(org.eclipse.ui.part.IPageSite)
	 */
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		pageSite.setSelectionProvider(this);
	}

	/**
	 * The <code>Page</code> implementation of this <code>IPage</code> method
	 * returns <code>null</code> if the tree viewer is null. Returns the tree
	 * viewer's control if tree viewer is null
	 */
	public Control getControl() {
		if (treeViewer == null)
			return null;
		return treeViewer.getControl();
	}

	/**
	 * Sets the focus to the tree viewer's control
	 */
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	protected void configTreeViewer() {
	}

	protected abstract TreeViewer createTreeViewer(Composite parent);

	protected void hookTreeViewer() {

	}

	/**
	 * Returns the tree viewer
	 * 
	 * @return the tree viewer
	 */
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	/**
	 * Selects the node
	 * 
	 * @param event the selection changed event
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
		final SelectionChangedEvent event = new SelectionChangedEvent(this, selection);
		ReportRequest request = new ReportRequest(this);
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
		// no convert
		// request.setRequestConvert(new EditorReportRequestConvert());
		// SessionHandleAdapter.getInstance().getMediator().pushState();
		SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);

		// create an event
		// fire the event
		Object[] listeners = selectionChangedListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
			SafeRunner.run(new SafeRunnable() {

				public void run() {
					l.selectionChanged(event);
				}
			});
		}
	}

	/**
	 * Notifies that the selection has changed.
	 * 
	 * @param event event object describing the change
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		setSelection(event.getSelection());
	}

	/**
	 * Adds a listener for selection changes in this selection provider. Has no
	 * effect if an identical listener is already registered.
	 * 
	 * @param listener a selection changed listener
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	/**
	 * Returns the current selection for this provider.
	 * 
	 * @return the current selection
	 */
	public ISelection getSelection() {
		if (getTreeViewer() == null) {
			return StructuredSelection.EMPTY;
		}
		return getTreeViewer().getSelection();
	}

	/**
	 * Removes the given selection change listener from this selection provider. Has
	 * no affect if an identical listener is not registered.
	 * 
	 * @param listener a selection changed listener
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	/**
	 * Sets the current selection for this selection provider.
	 * 
	 * @param selection the new selection
	 */
	public void setSelection(ISelection selection) {
		if (getTreeViewer() != null) {
			getTreeViewer().setSelection(selection);
		}

	}

	/**
	 * The <code>Page</code> implementation of this <code>IPage</code> method
	 * disposes of this page's control (if it has one and it has not already been
	 * disposed).
	 */
	public void dispose() {
		selectionChangedListeners.clear();
		treeViewer = null;

		// remove the mediator listener
		if (model != null) {
			IMediator mediator = SessionHandleAdapter.getInstance().getMediator(model, false);
			if (mediator != null) {
				mediator.removeColleague(this);
			}
			// SessionHandleAdapter.getInstance( )
			// .getMediator( reportHandle )
			// .removeColleague( this );
		}

		super.dispose();
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
	public void performRequest(final IMediatorRequest request) {
		if (ReportRequest.SELECTION.equals(request.getType())) {
			handleSelectionChange((ReportRequest) request);
		}
		if (ReportRequest.CREATE_ELEMENT.equals(request.getType())) {
			Display.getCurrent().asyncExec(new Runnable() {

				public void run() {
					if (getTreeViewer() != null) {
						getTreeViewer().refresh();
						handleSelectionChange((ReportRequest) request);
					}
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
		if (request.getSource() == this) {
			return;
		}
		if (getTreeViewer() == null) {
			return;
		}
		final List list = request.getSelectionModelList();

		if (canSetSelection(list)) {

			setSelection(new StructuredSelection(list));

		}
	}

	private boolean canSetSelection(List list) {
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (UIUtil.containElement(getTreeViewer(), element)) {
				return true;
			}
		}
		return false;
	}

	public ModuleHandle getRoot() {
		return model;
	}

	protected void setRoot(ModuleHandle reportHandle) {
		this.model = reportHandle;
	}
}
