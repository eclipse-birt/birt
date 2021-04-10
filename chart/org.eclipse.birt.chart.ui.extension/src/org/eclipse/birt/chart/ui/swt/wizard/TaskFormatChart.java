/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSheetEntry;
import org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSubtaskEntry;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPreviewable;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIManager;
import org.eclipse.birt.chart.ui.swt.type.PieChart;
import org.eclipse.birt.chart.ui.swt.wizard.preview.ChartLivePreviewThread;
import org.eclipse.birt.chart.ui.swt.wizard.preview.LivePreviewTask;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.TreeCompoundTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.composites.NavTree;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Task sheet for formatting chart
 */

public class TaskFormatChart extends TreeCompoundTask implements IUIManager, ITaskChangeListener, ITaskPreviewable {

	protected IChartPreviewPainter previewPainter = null;

	private Canvas previewCanvas;

	private Label lblNodeTitle;

	// registered collections of sheets.
	// Key:collectionName; Value:nodePath array
	private Hashtable<String, String[]> htSheetCollections = null;

	// visible UI Sheets (Order IS important)
	// Key: nodePath; Value: subtask or Vector
	protected LinkedHashMap<String, Object> htVisibleSheets = null;

	protected int iBaseSeriesCount = 0;

	protected int iOrthogonalSeriesCount = 0;

	protected int iBaseAxisCount = 0;

	protected int iOrthogonalAxisCount = 0;

	protected int iAncillaryAxisCount = 0;

	protected static final String ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES = "OrthogonalSeriesSheetsCWA"; //$NON-NLS-1$

	protected static final String BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES = "BaseSeriesSheetsCWOA"; //$NON-NLS-1$

	protected static final String ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES = "OrthogonalSeriesSheetsCWOA"; //$NON-NLS-1$

	protected static final String BASE_AXIS_SHEET_COLLECTION = "BaseAxisSheets"; //$NON-NLS-1$

	protected static final String ORTHOGONAL_AXIS_SHEET_COLLECTION = "OrthogonalAxisSheets"; //$NON-NLS-1$

	protected static final String ANCILLARY_AXIS_SHEET_COLLECTION = "AncillaryAxisSheets"; //$NON-NLS-1$

	protected static final String DIAL_SERIES_SHEET_COLLECTION = "NeedleSheets"; //$NON-NLS-1$

	protected Map<String, String[]> subtasksRegistry = new HashMap<String, String[]>(7);

	private IRegisteredSubtaskEntry rootSubtaskEntry = null;

	protected int subtaskHeightHint = 500;
	protected int subtaskWidthHint = SWT.DEFAULT;

	public TaskFormatChart() {
		super(Messages.getString("TaskFormatChart.TaskExp"), true); //$NON-NLS-1$
		setDescription(Messages.getString("TaskFormatChart.Task.Description")); //$NON-NLS-1$
		registerSubtasks();
	}

	protected void registerSubtasks() {
		subtasksRegistry.put(BASE_AXIS_SHEET_COLLECTION, new String[] { "Chart.Axis", "Chart.Axis.X Axis"//$NON-NLS-1$ //$NON-NLS-2$
		});
		subtasksRegistry.put(ORTHOGONAL_AXIS_SHEET_COLLECTION, new String[] { "Chart.Axis.Y Axis" //$NON-NLS-1$
		});
		subtasksRegistry.put(ANCILLARY_AXIS_SHEET_COLLECTION, new String[] { "Chart.Axis.Z Axis" //$NON-NLS-1$
		});
		subtasksRegistry.put(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES, new String[] { "Series.Y Series" //$NON-NLS-1$
		});
		subtasksRegistry.put(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES,
				new String[] { "Series.Category Series" //$NON-NLS-1$
				});
		subtasksRegistry.put(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES,
				new String[] { "Series.Value Series" //$NON-NLS-1$
				});
		subtasksRegistry.put(DIAL_SERIES_SHEET_COLLECTION, new String[] { "Series.Value Series.Needle" //$NON-NLS-1$
		});
	}

	private Collection<IRegisteredSubtaskEntry> getRegisteredSubtasks() {
		// Gets all registered subtasks from extension id.
		Collection<IRegisteredSubtaskEntry> cRegisteredEntries = new ArrayList<IRegisteredSubtaskEntry>();
		Class<?> clazz = this.getClass();
		if (rootSubtaskEntry != null) {
			cRegisteredEntries.add(rootSubtaskEntry);
		}
		while (clazz != TreeCompoundTask.class) {
			Collection<IRegisteredSubtaskEntry> cSheets = ChartUIExtensionsImpl.instance()
					.getUISheetExtensions(clazz.getSimpleName());
			if (cSheets != null && cSheets.size() > 0) {
				for (IRegisteredSubtaskEntry subtaskEntry : cSheets) {
					if (rootSubtaskEntry != null
							&& clazz.getSimpleName().equals(TaskFormatChart.class.getSimpleName())) {
						cRegisteredEntries
								.add(new DefaultRegisteredSubtaskEntryImpl(String.valueOf(subtaskEntry.getNodeIndex()),
										getNodePathWithRoot(subtaskEntry.getNodePath()), subtaskEntry.getDisplayName(),
										subtaskEntry.getSheet()));
					} else {
						cRegisteredEntries.add(subtaskEntry);
					}
				}
				// If current id has registered subtasks, return them.
				return cRegisteredEntries;
			}
			clazz = clazz.getSuperclass();
		}
		return cRegisteredEntries;
	}

	private String getNodePathWithRoot(String nodePath) {
		if (rootSubtaskEntry != null) {
			String prefix = rootSubtaskEntry.getNodePath() + "."; //$NON-NLS-1$
			if (!nodePath.startsWith(prefix)) {
				return prefix + nodePath;
			}
		}
		return nodePath;
	}

	/**
	 * Sets the root node for all format chart nodes
	 * 
	 * @param rootSubtaskEntry
	 */
	protected void setRootNode(IRegisteredSubtaskEntry rootSubtaskEntry) {
		this.rootSubtaskEntry = rootSubtaskEntry;
	}

	protected void populateSubtasks() {
		super.populateSubtasks();

		htVisibleSheets = new LinkedHashMap<String, Object>(12);
		htSheetCollections = new Hashtable<String, String[]>();

		// Get collection of registered Sheets
		Iterator<IRegisteredSubtaskEntry> iterEntries = getRegisteredSubtasks().iterator();

		// Vector to be used to build a sorted list of registered sheets (sorted
		// on provided node index)
		Vector<IRegisteredSubtaskEntry> vSortedEntries = new Vector<IRegisteredSubtaskEntry>();
		while (iterEntries.hasNext()) {
			IRegisteredSubtaskEntry entry = iterEntries.next();
			if (vSortedEntries.isEmpty()) {
				vSortedEntries.add(entry);
			} else {
				// Find location where the new entry needs to be inserted
				int iNewIndex = entry.getNodeIndex();

				// Try the last entry
				if ((vSortedEntries.get(vSortedEntries.size() - 1)).getNodeIndex() <= iNewIndex) {
					vSortedEntries.add(entry);
				} else if ((vSortedEntries.get(0)).getNodeIndex() > iNewIndex)
				// Try the first entry
				{
					vSortedEntries.add(0, entry);
				} else {
					vSortedEntries = addEntrySorted(vSortedEntries, entry, 0, vSortedEntries.size() - 1);
				}
			}
		}

		for (int i = 0; i < vSortedEntries.size(); i++) {
			IRegisteredSubtaskEntry entry = vSortedEntries.get(i);
			ISubtaskSheet sheet = entry.getSheet();

			String sNodePath = entry.getNodePath();
			sheet.setParentTask(this);
			sheet.setNodePath(sNodePath);

			// Provide customization for subtask visibility
			if (!checkSubtaskVisibility(sheet)) {
				continue;
			}

			// Initially ALL registered sheets are visible
			htVisibleSheets.put(sNodePath, sheet);
			sheet.setTitle(entry.getDisplayName());

			addSubtask(sNodePath, sheet);
		}

		if (getCurrentModelState() != null) {
			initialize(getCurrentModelState());
		}
	}

	/**
	 * Checks if current subtask is visible.
	 * 
	 * @param subtask subtask
	 * @return visible or not
	 */
	protected boolean checkSubtaskVisibility(ISubtaskSheet subtask) {
		// Always visible in OS chart builder.
		return true;
	}

	/**
	 * Returns if tree view can support multiple value series
	 * 
	 * @return
	 */
	protected boolean isMultipleValueSeriesAllowed() {
		// OS chart builder supports UI for multiple value series.
		return true;
	}

	public void updateTreeItem() {
		super.updateTreeItem();

		getNavigatorTree().removeAll();
		Iterator<String> itKeys = htVisibleSheets.keySet().iterator();
		while (itKeys.hasNext()) {
			String sKey = itKeys.next();
			Object oVal = htVisibleSheets.get(sKey);
			if (oVal instanceof Vector<?>) {
				Vector<ISubtaskSheet> vector = (Vector<ISubtaskSheet>) oVal;
				for (int i = 0; i < vector.size(); i++) {
					String sSuffix = ""; //$NON-NLS-1$
					if (vector.size() > 1) {
						sSuffix = INDEX_SEPARATOR + String.valueOf(i + 1);
					}
					// // If parent is dynamic as well
					// String sParentKey = sKey.substring( 0,
					// sKey.lastIndexOf( NavTree.SEPARATOR ) );
					// Object oParentVal = htVisibleSheets.get( sParentKey );
					// if ( oParentVal != null && oParentVal instanceof Vector )
					// {
					// getNavigatorTree( ).addNode( sParentKey
					// + sSuffix
					// + NavTree.SEPARATOR
					// + sKey.substring( sKey.lastIndexOf( NavTree.SEPARATOR ) )
					// + sSuffix );
					// }
					// else
					{
						String displayName = vector.get(i).getTitle();
						if (displayName != null && displayName.trim().length() > 0) {
							getNavigatorTree().addNode(sKey + sSuffix, displayName + sSuffix);
						} else {
							getNavigatorTree().addNode(sKey + sSuffix);
						}
					}
				}
			} else {
				getNavigatorTree().addNode(sKey, ((ISubtaskSheet) oVal).getTitle());
			}
		}
	}

	private Vector<IRegisteredSubtaskEntry> addEntrySorted(Vector<IRegisteredSubtaskEntry> vSortedEntries,
			IRegisteredSubtaskEntry entry, int iStart, int iEnd) {
		int iNewIndex = entry.getNodeIndex();
		if (iStart == iEnd) {
			if (((IRegisteredSheetEntry) vSortedEntries.get(iStart)).getNodeIndex() > iNewIndex) {
				vSortedEntries.add(iStart, entry);
			} else {
				vSortedEntries.add(iEnd + 1, entry);
			}
		} else if ((iEnd - iStart) == 1) {
			vSortedEntries.add(iEnd, entry);
		} else {
			if ((vSortedEntries.get(iStart)).getNodeIndex() == iNewIndex) {
				vSortedEntries.add(iStart + 1, entry);
			} else {
				int iHalfwayPoint = (iEnd - iStart) / 2;
				if ((vSortedEntries.get(iStart + iHalfwayPoint)).getNodeIndex() > iNewIndex) {
					addEntrySorted(vSortedEntries, entry, iStart, (iStart + iHalfwayPoint));
				} else {
					addEntrySorted(vSortedEntries, entry, (iStart + iHalfwayPoint), iEnd);
				}
			}
		}
		return vSortedEntries;
	}

	public boolean registerSheetCollection(String sCollection, String[] saNodePaths) {
		if (saNodePaths == null) {
			return false;
		}
		try {
			if (rootSubtaskEntry != null) {
				String[] newNodePaths = new String[saNodePaths.length];
				for (int i = 0; i < saNodePaths.length; i++) {
					newNodePaths[i] = getNodePathWithRoot(saNodePaths[i]);
				}
				htSheetCollections.put(sCollection, newNodePaths);
			} else {
				htSheetCollections.put(sCollection, saNodePaths);
			}

			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	protected boolean registerSheetCollection(String sCollection) {
		return registerSheetCollection(sCollection, subtasksRegistry.get(sCollection));
	}

	public String[] getRegisteredCollectionValue(String sCollection) {
		return htSheetCollections.get(sCollection);
	}

	public boolean addCollectionInstance(String sCollection) {
		if (!htSheetCollections.containsKey(sCollection)) {
			return false;
		}
		String[] saNodes = htSheetCollections.get(sCollection);
		for (int iN = 0; iN < saNodes.length; iN++) {
			addVisibleSubtask(saNodes[iN]);
		}
		return true;
	}

	private void addVisibleSubtask(String sNodeName) {
		Vector<ISubtaskSheet> vSheets = new Vector<ISubtaskSheet>();
		// check if node exists in tree
		if (htVisibleSheets.containsKey(sNodeName)) {
			Object oSheets = htVisibleSheets.get(sNodeName);
			if (oSheets instanceof Vector<?>) {
				vSheets = (Vector<ISubtaskSheet>) oSheets;
			} else if (oSheets instanceof ISubtaskSheet) {
				vSheets.add((ISubtaskSheet) oSheets);
			} else {
				return;
			}
			vSheets.add(getSubtask(sNodeName));
			htVisibleSheets.put(sNodeName, vSheets);
		} else {
			if (containSubtask(sNodeName)) {
				vSheets.add(getSubtask(sNodeName));
				htVisibleSheets.put(sNodeName, vSheets);
			}
		}
	}

	private void removeVisibleTask(String sNodeName) {
		Vector<ISubtaskSheet> vSheets = new Vector<ISubtaskSheet>();
		// check if node exists in tree
		if (htVisibleSheets.containsKey(sNodeName)) {
			Object oSheets = htVisibleSheets.get(sNodeName);
			if (oSheets instanceof Vector<?>) {
				vSheets = (Vector<ISubtaskSheet>) oSheets;
			} else if (oSheets instanceof ISubtaskSheet) {
				vSheets.add((ISubtaskSheet) oSheets);
			} else {
				return;
			}

			int iLast = vSheets.lastIndexOf(getSubtask(sNodeName));
			vSheets.remove(iLast);
			htVisibleSheets.put(sNodeName, vSheets);
		} else {
			if (containSubtask(sNodeName)) {
				int iLast = vSheets.lastIndexOf(getSubtask(sNodeName));
				vSheets.remove(iLast);
				htVisibleSheets.put(sNodeName, vSheets);
			}
		}
	}

	public boolean removeCollectionInstance(String sCollection) {
		if (!htSheetCollections.containsKey(sCollection)) {
			return false;
		}
		String[] saNodes = htSheetCollections.get(sCollection);
		for (int iN = 0; iN < saNodes.length; iN++) {
			removeVisibleTask(saNodes[iN]);
		}
		return true;
	}

	public Chart getCurrentModelState() {
		if (getContext() == null) {
			return null;
		}
		return ((ChartWizardContext) getContext()).getModel();
	}

	public void createControl(Composite parent) {
		// Initialize all components.
		initControl(parent);

		if (previewPainter == null) {
			// Invoke this only once
			previewPainter = createPreviewPainter();
		}
		if (!isSutaskPreviewable(getCurrentSubtask())) {
			// If current subtask supports preview by itself, do not preview as
			// a whole page
			doPreview();
		}
	}

	private void initDetailHeader(Composite parent) {

		lblNodeTitle = new Label(parent, SWT.NONE);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			lblNodeTitle.setLayoutData(gd);

			lblNodeTitle.setFont(JFaceResources.getBannerFont());
		}

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			separator.setLayoutData(gd);
		}
	}

	/**
	 * Initialize components in these shell.
	 * 
	 * @param parent parent composite.
	 */
	private void initControl(Composite parent) {
		// Initialize chart types first.
		ChartUIUtil.populateTypeTable(getContext());

		if (topControl == null || topControl.isDisposed()) {
			// 1. Create top level composite of the shell.
			topControl = new Composite(parent, SWT.NONE);
			{
				GridLayout layout = new GridLayout(2, false);
				layout.marginHeight = 0;
				layout.marginWidth = 0;
				layout.horizontalSpacing = 0;
				topControl.setLayout(layout);
				GridData gridData = new GridData();
				gridData.horizontalAlignment = SWT.FILL;
				gridData.verticalAlignment = SWT.FILL;
				topControl.setLayoutData(gridData);
			}

			// 2. Create left navigator tree.
			navTree = new NavTree(topControl, SWT.BORDER);
			{
				final GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_VERTICAL);
				gridData.widthHint = 127;
				navTree.setLayoutData(gridData);
				navTree.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						switchToTreeItem((TreeItem) event.item);
					}
				});
			}

			// 3. Create right sash form, it contains chart preview canvas and
			// detail sub-task sheet.
			SashForm foRightSashForm = new SashForm(topControl, SWT.VERTICAL) {

				@Override
				public Control[] getChildren() {
					Control[] children = super.getChildren();
					List<Control> visibleChildren = new ArrayList<Control>();
					for (Control child : children) {
						if (child.isVisible()) {
							visibleChildren.add(child);
						}
					}
					if (visibleChildren.size() == 0 || visibleChildren.size() == children.length) {
						// In first loading, no visible controls
						return children;
					}
					return visibleChildren.toArray(new Control[visibleChildren.size()]);
				}
			};
			{
				foRightSashForm.SASH_WIDTH = 1;
				foRightSashForm.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));

				GridLayout layout = new GridLayout();
				foRightSashForm.setLayout(layout);
				GridData gridData = new GridData(GridData.FILL_BOTH);
				gridData.heightHint = subtaskHeightHint;
				gridData.widthHint = subtaskWidthHint;
				foRightSashForm.setLayoutData(gridData);
			}
			// 3.1 Create preview canvas and add to right sash form.
			createContainer(foRightSashForm);

			// 3.2 Create detail sheet composite and add to right sash form.
			createDetailComposite(foRightSashForm);
		}

		updateTree();
		switchToDefaultItem();
	}

	/**
	 * Create detail sheet composite.
	 * 
	 * @param parent parent composite.
	 */
	private void createDetailComposite(Composite parent) {
		Composite detailComposite = new Composite(parent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			detailComposite.setLayout(layout);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			detailComposite.setLayoutData(gridData);
		}

		initDetailHeader(detailComposite);

		createSubDetailComposite(detailComposite, !isSutaskPreviewable(getCurrentSubtask()));
	}

	/**
	 * Create scrolled composite which contains detail sub-task sheet.
	 * 
	 * @param detailComposite parent composite.
	 * @param bScrolled       indicate if scrolled composite is needed
	 */
	private void createSubDetailComposite(Composite detailComposite, boolean bScrolled) {
		if (!bScrolled) {
			// If current subtask can preview, do not set scrolled composite for
			// it.
			cmpSubtaskContainer = new Composite(detailComposite, SWT.NONE);
			GridLayout layout = new GridLayout();
			cmpSubtaskContainer.setLayout(layout);
			GridData gridData = new GridData(GridData.FILL_BOTH);
			cmpSubtaskContainer.setLayoutData(gridData);
		} else {
			ScrolledComposite foScrolledDetailComposite = new ScrolledComposite(detailComposite,
					SWT.V_SCROLL | SWT.H_SCROLL);
			{
				GridLayout layout = new GridLayout();
				foScrolledDetailComposite.setLayout(layout);
				GridData gridData = new GridData(GridData.FILL_BOTH);
				foScrolledDetailComposite.setLayoutData(gridData);

				foScrolledDetailComposite.setExpandHorizontal(true);
				foScrolledDetailComposite.setExpandVertical(true);
			}

			cmpSubtaskContainer = new Composite(foScrolledDetailComposite, SWT.NONE);
			{
				GridLayout layout = new GridLayout();
				cmpSubtaskContainer.setLayout(layout);
				GridData gridData = new GridData(GridData.FILL_BOTH);
				cmpSubtaskContainer.setLayoutData(gridData);
			}

			foScrolledDetailComposite.setContent(cmpSubtaskContainer);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.ui.frameworks.taskwizard.TreeCompoundTask#switchTo
	 * (java.lang.String, boolean)
	 */
	protected void switchTo(String sSubtaskPath, boolean needSelection) {
		boolean bPreviewableOld = isSutaskPreviewable(getCurrentSubtask());
		boolean bPreviewableNew = isSutaskPreviewable(getSubtask(sSubtaskPath));

		if (bPreviewableOld != bPreviewableNew) {
			Composite cmpParent = cmpSubtaskContainer.getParent();

			// Update preview canvas and switch between scrolled composite and
			// normal composite
			previewCanvas.getParent().getParent().setVisible(!bPreviewableNew);
			previewCanvas.getParent().getParent().getParent().layout();
			if (!bPreviewableNew) {
				// Re-render preview if previous preview is standalone
				doPreview();

				cmpSubtaskContainer.dispose();
			} else {
				cmpParent = cmpParent.getParent();
				cmpSubtaskContainer.getParent().dispose();
			}
			createSubDetailComposite(cmpParent, !bPreviewableNew);
		}

		super.switchTo(sSubtaskPath, needSelection);

		if (!bPreviewableNew) {
			// Compute minimum size for scrolled composite to let correct scroll
			// behavior.
			Point childSize = cmpSubtaskContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			((ScrolledComposite) cmpSubtaskContainer.getParent()).setMinSize(childSize);
		}
		cmpSubtaskContainer.getParent().getParent().layout();
		cmpSubtaskContainer.getParent().layout();
	}

	protected Composite createTitleArea(Composite parent) {
		Composite cmpTitle = super.createTitleArea(parent);
		// ( (GridData) cmpTitle.getLayoutData( ) ).heightHint = 250;

		previewCanvas = new Canvas(cmpTitle, SWT.BORDER);
		{
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			previewCanvas.setLayoutData(gd);
			previewCanvas.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		}
		return cmpTitle;
	}

	protected String getTitleAreaString() {
		return Messages.getString("TaskFormatChart.Label.Preview"); //$NON-NLS-1$
	}

	protected void createSubtaskArea(Composite parent, ISubtaskSheet subtask) {
		if (getNavigatorTree().getSelection().length > 0) {
			lblNodeTitle.setText(getNavigatorTree().getSelection()[0].getText());
		}
		super.createSubtaskArea(parent, subtask);

		// If the subtask is registered as disabled state, disable it.
		boolean bEnabled = ((ChartWizardContext) getContext()).isEnabled(subtask.getNodePath());
		if (!bEnabled) {
			disableControl(parent);
		}
		parent.setEnabled(bEnabled);
		lblNodeTitle.setEnabled(bEnabled);
	}

	private void disableControl(Control control) {
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			for (int i = 0; i < children.length; i++) {
				disableControl(children[i]);
			}
		}
		control.setEnabled(false);
	}

	public IChartPreviewPainter createPreviewPainter() {
		ChartPreviewPainter painter = new ChartPreviewPainter((ChartWizardContext) getContext());
		getPreviewCanvas().addPaintListener(painter);
		getPreviewCanvas().addControlListener(painter);
		painter.setPreview(getPreviewCanvas());
		return painter;
	}

	public void changeTask(Notification notification) {
		if (isSutaskPreviewable(getCurrentSubtask())) {
			if (getCurrentSubtask() instanceof ITaskChangeListener) {
				// Delegate notification to previewable subtask
				((ITaskChangeListener) getCurrentSubtask()).changeTask(notification);
			}
		} else {
			if (previewPainter != null) {
				doPreview();
			}
		}
	}

	protected IDataServiceProvider getDataServiceProvider() {
		return ((ChartWizardContext) getContext()).getDataServiceProvider();
	}

	protected void initialize(Chart chartModel) {
		// Register sheet collections
		registerSheetCollection(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
		registerSheetCollection(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
		registerSheetCollection(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
		registerSheetCollection(BASE_AXIS_SHEET_COLLECTION);
		registerSheetCollection(ORTHOGONAL_AXIS_SHEET_COLLECTION);
		registerSheetCollection(ANCILLARY_AXIS_SHEET_COLLECTION);
		registerSheetCollection(DIAL_SERIES_SHEET_COLLECTION);

		if (chartModel instanceof ChartWithAxes) {
			iBaseAxisCount = ((ChartWithAxes) chartModel).getAxes().size();
			iOrthogonalAxisCount = 0;
			iAncillaryAxisCount = 0;
			iOrthogonalSeriesCount = 0;
			for (int i = 0; i < iBaseAxisCount; i++) {
				iOrthogonalAxisCount += ((ChartWithAxes) chartModel).getAxes().get(i).getAssociatedAxes().size();
				if (chartModel.getDimension().getValue() == ChartDimension.THREE_DIMENSIONAL) {
					iAncillaryAxisCount += ((ChartWithAxes) chartModel).getAxes().get(i).getAncillaryAxes().size();
				}
				if (isMultipleValueSeriesAllowed()) {
					for (int iS = 0; iS < iOrthogonalAxisCount; iS++) {
						iOrthogonalSeriesCount += ((ChartWithAxes) chartModel).getAxes().get(i).getAssociatedAxes()
								.get(iS).getSeriesDefinitions().size();
					}
				} else {
					iOrthogonalSeriesCount = 1;
				}
			}
			// Start from 1 because there will always be at least 1 entry for
			// each registered sheet when this method is called
			for (int iBA = 1; iBA < iBaseAxisCount; iBA++) {
				addCollectionInstance(BASE_AXIS_SHEET_COLLECTION);
			}

			for (int iOA = 1; iOA < iOrthogonalAxisCount; iOA++) {
				addCollectionInstance(ORTHOGONAL_AXIS_SHEET_COLLECTION);
			}

			// Remove Z axis by default
			removeCollectionInstance(ANCILLARY_AXIS_SHEET_COLLECTION);
			// Must start from 0 because default is 0
			for (int iOA = 0; iOA < iAncillaryAxisCount; iOA++) {
				addCollectionInstance(ANCILLARY_AXIS_SHEET_COLLECTION);
			}
			// Remove series sheets (for charts with axes) since they are not
			// needed for Charts Without Axes
			removeCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
			removeCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
			removeCollectionInstance(DIAL_SERIES_SHEET_COLLECTION);

			for (int iOS = 1; iOS < iOrthogonalSeriesCount; iOS++) {
				addCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
			}
		} else {
			iBaseAxisCount = 0;
			iOrthogonalAxisCount = 0;
			iBaseSeriesCount = ((ChartWithoutAxes) chartModel).getSeriesDefinitions().size();

			if (isMultipleValueSeriesAllowed()) {
				iOrthogonalSeriesCount = 0;
				for (int iS = 0; iS < iBaseSeriesCount; iS++) {
					iOrthogonalSeriesCount += ((ChartWithoutAxes) chartModel).getSeriesDefinitions().get(iS)
							.getSeriesDefinitions().size();
				}
			} else {
				iOrthogonalSeriesCount = 1;
			}

			// Remove axis sheets since they are not needed for Charts Without
			// Axes
			removeCollectionInstance(ANCILLARY_AXIS_SHEET_COLLECTION);
			removeCollectionInstance(ORTHOGONAL_AXIS_SHEET_COLLECTION);
			removeCollectionInstance(BASE_AXIS_SHEET_COLLECTION);
			// Remove series sheets (for charts with axes) since they are not
			// needed for Charts Without Axes
			removeCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
			removeCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
			removeCollectionInstance(DIAL_SERIES_SHEET_COLLECTION);

			if (chartModel instanceof DialChart) {
				if (!((DialChart) chartModel).isDialSuperimposition()) {
					for (int iOS = 1; iOS < iOrthogonalSeriesCount; iOS++) {
						addCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
					}
				} else {
					for (int iOS = 0; iOS < iOrthogonalSeriesCount; iOS++) {
						addCollectionInstance(DIAL_SERIES_SHEET_COLLECTION);
					}
				}
			} else {
				// Only add category series page for Pie chart, since default
				// behavior is no category series settings.
				if (((ChartWizardContext) getContext()).getChartType() instanceof PieChart) {
					for (int iBS = 0; iBS < iBaseSeriesCount; iBS++) {
						addCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
					}
				}
				for (int iOS = 1; iOS < iOrthogonalSeriesCount; iOS++) {
					addCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
				}
			}
		}
	}

	public void dispose() {
		super.dispose();

		if (htVisibleSheets != null) {
			htVisibleSheets.clear();
		}
		if (htSheetCollections != null) {
			htSheetCollections.clear();
		}

		previewCanvas = null;
		if (previewPainter != null) {
			previewPainter.dispose();
		}
		previewPainter = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask#getImage()
	 */
	public Image getImage() {
		return UIHelper.getImage(ChartUIConstants.IMAGE_TASK_FORMAT);
	}

	protected boolean isSutaskPreviewable(ISubtaskSheet subtask) {
		return subtask instanceof ITaskPreviewable && ((ITaskPreviewable) subtask).isPreviewable();
	}

	protected Chart getPreviewChartModel() throws ChartException {
		return getCurrentModelState();
	}

	public void doPreview() {
		try {
			final Chart chart = getPreviewChartModel();
			LivePreviewTask lpt = new LivePreviewTask(Messages.getString("TaskFormatChart.LivePreviewTask.BindData"), //$NON-NLS-1$
					null);
			// Add a task to retrieve data and bind data to chart.
			lpt.addTask(new LivePreviewTask() {
				public void run() {
					if (previewPainter != null) {
						setParameter(ChartLivePreviewThread.PARAM_CHART_MODEL, ChartUIUtil.prepareLivePreview(chart,
								getDataServiceProvider(), ((ChartWizardContext) context).getActionEvaluator()));
					}
				}
			});

			// Add a task to render chart.
			lpt.addTask(new LivePreviewTask() {
				public void run() {
					if (previewCanvas != null && previewCanvas.getDisplay() != null
							&& !previewCanvas.getDisplay().isDisposed()) {
						previewCanvas.getDisplay().syncExec(new Runnable() {

							public void run() {
								// Repaint chart.
								if (previewPainter != null) {
									previewPainter.renderModel(
											(IChartObject) getParameter(ChartLivePreviewThread.PARAM_CHART_MODEL));
								}
							}
						});
					}
				}
			});

			// Add live preview tasks to live preview thread.
			((ChartLivePreviewThread) ((ChartWizardContext) context).getLivePreviewThread())
					.setParentShell(getPreviewCanvas().getShell());
			((ChartLivePreviewThread) ((ChartWizardContext) context).getLivePreviewThread()).add(lpt);
		} catch (ChartException e) {
			WizardBase.showException(e.getMessage());
		}
	}

	public Canvas getPreviewCanvas() {
		return previewCanvas;
	}

	public boolean isPreviewable() {
		return true;
	}

}
