/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.examples.view.description.Messages;
import org.eclipse.birt.chart.examples.view.util.ChartPreview;
import org.eclipse.birt.chart.examples.view.util.ImportChartModel;
import org.eclipse.birt.chart.examples.view.util.ItemContentProvider;
import org.eclipse.birt.chart.examples.view.util.Tools;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ChartExamples implements SelectionListener {

	private Composite mainComposite;

	private ItemContentProvider icp;

	private Canvas paintCanvas;

	private ChartPreview preview;

	private Text description;

	private static Chart chart;

	private static String className;

	static final int Save_tool = 0;

	static final int Open_tool = 1;

	private Map<TreeItem, String> hmItemToKey = new HashMap<TreeItem, String>();

	public static final Tools[] tools = { new Tools(Save_tool, "Save", "xml", SWT.RADIO), //$NON-NLS-1$ //$NON-NLS-2$
			new Tools(Open_tool, "Open", "java", SWT.RADIO) //$NON-NLS-1$ //$NON-NLS-2$
	};

	public ChartExamples(Composite parent) {
		icp = ItemContentProvider.instance();
		mainComposite = parent;
		placeComponent();
	}

	private void placeComponent() {
		/** * Create principal GUI layout elements ** */
		mainComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL));
		mainComposite.setLayout(new GridLayout(3, true));

		// selector frame
		Group sGroup = new Group(mainComposite, SWT.NONE);
		sGroup.setLayout(new GridLayout());
		sGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL));
		sGroup.setText(Messages.getString("ChartExamples.SelectExamples")); //$NON-NLS-1$
		createTree(sGroup);

		// preview and description frame
		Composite rComp = new Composite(mainComposite, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		rComp.setLayout(new GridLayout());
		rComp.setLayoutData(gridData);

		// preview canvas
		Group preGroup = new Group(rComp, SWT.NONE);
		preGroup.setLayout(new GridLayout());
		preGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL));
		preGroup.setText(Messages.getString("ChartExamples.Preview")); //$NON-NLS-1$
		gridData = new GridData(GridData.FILL_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL);
		preGroup.setLayoutData(gridData);

		paintCanvas = new Canvas(preGroup, SWT.BORDER);
		paintCanvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		paintCanvas.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		preview = new ChartPreview();
		paintCanvas.addPaintListener(preview);
		paintCanvas.addControlListener(preview);
		preview.setPreview(paintCanvas);

		// description
		Group desGroup = new Group(rComp, SWT.NONE);
		desGroup.setLayout(new GridLayout());
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 60;
		desGroup.setLayoutData(gridData);
		desGroup.setText(Messages.getString("ChartExamples.Description")); //$NON-NLS-1$

		description = new Text(desGroup, SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
		description.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL));
		description.setText(icp.getDefaultDescription());
	}

	public void setFocus() {
		mainComposite.setFocus();
	}

	/**
	 * Disposes of all resources associated with a particular instance of the
	 * ChartExample.
	 */
	public void dispose() {
		for (int i = 0; i < tools.length; ++i) {
			Tools tool = tools[i];
			final Image image = tool.image;
			if (image != null)
				image.dispose();
			tool.image = null;
		}
	}

	/**
	 * Create the selection tree.
	 * 
	 * @param parent sGroup
	 */
	private void createTree(Composite parent) {
		Tree tree = new Tree(parent, SWT.SINGLE | SWT.BORDER);
		tree.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL));
		tree.addSelectionListener(this);
		fillTree(tree);
	}

	/**
	 * Fill in the tree items.
	 * 
	 * @param tree The selection tree.
	 */
	private void fillTree(Tree tree) {
		ArrayList<String> cTypes = icp.getCategoryTypes();
		Iterator<String> iter = cTypes.iterator();
		tree.setRedraw(false);

		while (iter.hasNext()) {
			TreeItem cItem = new TreeItem(tree, SWT.NONE); // For
			// Categories
			String sKeyCate = iter.next();
			cItem.setText(Messages.getString(sKeyCate));

			ArrayList<String> iTypes = icp.getItemTypes(sKeyCate);
			Iterator<String> iter2 = iTypes.iterator();

			while (iter2.hasNext()) {
				TreeItem iItem = new TreeItem(cItem, SWT.NONE); // For Items
				String sKey = iter2.next();
				iItem.setText(Messages.getString(sKey));
				hmItemToKey.put(iItem, sKey);
			}
		}

		tree.setRedraw(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {

		if (e.getSource() instanceof Tree) {
			if (((TreeItem) e.item).getItemCount() != 0) {
				preview.renderModel(null);
				description.setText(icp.getDefaultDescription());
				ChartExamplesView.setActionsEnabled(false);
			} else {
				String sKey = hmItemToKey.get(e.item);
				setClassName(icp.getClassName(sKey));
				String methodName = icp.getMethodName(className);
				setChartModel(ImportChartModel.getChartModel(className, methodName));
				preview.renderModel(getChartModel().copyInstance());
				description.setText(icp.getDescription(className));
				ChartExamplesView.setActionsEnabled(true);
			}
		}

	}

	private void setChartModel(Chart cm) {
		chart = cm;
	}

	public static Chart getChartModel() {
		return chart;
	}

	private void setClassName(String name) {
		className = name;
	}

	public static String getClassName() {
		return className;
	}
}
