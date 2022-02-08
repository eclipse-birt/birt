/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Actuate Corporation - Copy and change to fit BIRT requirement 
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.script;

import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionTreeSupport;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.Page;

/**
 * The default page for the PaletteView that works in conjunction with a
 * PaletteViewerProvider.
 */
public class TreeViewPalettePage extends Page implements PalettePage, IAdaptable {

	/** Tool tip separator between Usage and Description */
	// private static final String TOOL_TIP_SEP = ": "; //$NON-NLS-1$
	/**
	 * The PaletteViewerProvider that is used to create the PaletteViewer
	 */
	protected PaletteViewerProvider provider;

	/**
	 * The PaletteViewer created for this page
	 */
	protected Tree tree;

	private ExpressionTreeSupport treeCommon;

	private SourceViewer targetViewer;

	/**
	 * Constructor
	 * 
	 */
	public TreeViewPalettePage() {
		treeCommon = new ExpressionTreeSupport();
	}

	/**
	 * Creates the palette viewer and its control.
	 * 
	 * @see Page#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		tree = new Tree(parent, SWT.NONE);
		treeCommon.setTree(tree);
		treeCommon.setExpressionViewer(targetViewer);

		treeCommon.createDefaultExpressionTree();

		treeCommon.addMouseTrackListener();
		treeCommon.addMouseListener();
		treeCommon.addDragSupportToTree();
		treeCommon.addDropSupportToViewer();

		// Add tool tips
		tree.setToolTipText(""); //$NON-NLS-1$

		tree.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				treeCommon.removeDropSupportToViewer();
			}
		});
	}

	/**
	 * get tool tip text from a string array that contains the text, usage and
	 * description
	 * 
	 * @param tuple
	 * @return
	 */
	// private static String getToolTip( String[] tuple )
	// {
	// return tuple[1] + TOOL_TIP_SEP + tuple[2];
	// }
	/**
	 * Releases the palette viewer from the edit domain
	 * 
	 * @see Page#dispose()
	 */
	public void dispose() {
		tree.dispose();
		super.dispose();
	}

	/**
	 * @see IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * @return the palette viewer's control
	 * @see Page#getControl()
	 */
	public Control getControl() {
		return tree;
	}

	/**
	 * Sets focus on the palette's control
	 * 
	 * @see Page#setFocus()
	 */
	public void setFocus() {
		tree.setFocus();
		treeCommon.updateParametersTree();
	}

	public ExpressionTreeSupport getSupport() {
		return this.treeCommon;
	}

	void setViewer(SourceViewer viewer) {
		targetViewer = viewer;
	}

}
