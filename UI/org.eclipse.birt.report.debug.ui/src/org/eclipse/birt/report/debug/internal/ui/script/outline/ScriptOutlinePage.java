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

package org.eclipse.birt.report.debug.internal.ui.script.outline;

import org.eclipse.birt.report.debug.internal.ui.script.actions.ScriptEditAction;
import org.eclipse.birt.report.debug.internal.ui.script.outline.node.DebugScriptElementNode;
import org.eclipse.birt.report.debug.internal.ui.script.outline.node.DebugScriptObjectNode;
import org.eclipse.birt.report.designer.internal.ui.views.RenameListener;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ItemSorter;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * Script debugger outline page
 */

public class ScriptOutlinePage extends ContentOutlinePage {

	/**
	 * Show the outline level.
	 */
	public final static int SHOW_LEVEL = 3;
	private ModuleHandle reportHandle;

	/**
	 * Constructor
	 *
	 * @param reportHandle
	 */
	public ScriptOutlinePage(ModuleHandle reportHandle) {
		this.reportHandle = reportHandle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#createControl(org.
	 * eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		createContextMenu();
		ScriptOutlineTreeProvider provider = new ScriptOutlineTreeProvider();

		getTreeViewer().setContentProvider(provider);

		getTreeViewer().setLabelProvider(provider);

		getTreeViewer().addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object obj = event.getSelection();
				ScriptEditAction action = new ScriptEditAction(obj);
				if (action.isEnabled()) {
					action.run();
				}
			}
		});

		// add inline renaming support
		new RenameListener(getTreeViewer()).apply();

		getTreeViewer().setSorter(new ItemSorter());

		init(reportHandle);

		getTreeViewer().expandToLevel(SHOW_LEVEL);

		final Tree tree = getTreeViewer().getTree();

		tree.addMouseTrackListener(new MouseTrackAdapter() {

			@Override
			public void mouseHover(MouseEvent event) {
				Widget widget = event.widget;
				if (widget == tree) {
					Point pt = new Point(event.x, event.y);
					TreeItem item = tree.getItem(pt);
					if (item == null || item.getData() == null) {
						tree.setToolTipText(null);
					} else {
						tree.setToolTipText(getTooltip(item.getData()));
					}
				}
			}
		});
	}

	private void init(ModuleHandle reportHandle) {
		setTreeInput(reportHandle);
	}

	private void setTreeInput(ModuleHandle reportHandle) {
		getTreeViewer().setInput(new Object[] { reportHandle });
	}

	private void createContextMenu() {
		MenuManager menuManager = new ScriptViewContextMenuProvider(getTreeViewer());

		Menu menu = menuManager.createContextMenu(getTreeViewer().getControl());

		getTreeViewer().getControl().setMenu(menu);

		getSite().registerContextMenu("outlinemenu", menuManager, //$NON-NLS-1$
				getSite().getSelectionProvider());
		getSite().setSelectionProvider(getTreeViewer());
	}

	private String getTooltip(Object element) {
		return ScriptProviderFactory.createProvider(element).getNodeTooltip(element);
	}

	/**
	 * Select the item from the id.
	 *
	 * @param id
	 */
	public void selectionItem(String id) {
		if (getTreeViewer() == null || getTreeViewer().getTree() == null) {
			return;
		}
		Object obj = ModuleUtil.getScriptObject(reportHandle, id);
		if (obj instanceof PropertyHandle) {
			PropertyHandle handle = (PropertyHandle) obj;

			DebugScriptObjectNode node = new DebugScriptObjectNode(handle);
			DebugScriptElementNode parent = new DebugScriptElementNode(handle.getElementHandle());

			node.setNodeParent(parent);
			IStructuredSelection selection = new StructuredSelection(node);

			setSelection(selection);
		}
	}
}
