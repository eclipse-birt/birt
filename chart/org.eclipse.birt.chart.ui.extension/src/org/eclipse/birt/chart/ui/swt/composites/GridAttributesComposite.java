/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.component.Grid;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartCombo;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class GridAttributesComposite extends Composite implements SelectionListener, Listener {

	private transient LineAttributesComposite liacLines = null;

	private transient LineAttributesComposite liacTicks = null;

	protected String lineVisibleLabel = Messages.getString("LabelAttributesComposite.Lbl.IsVisible"); //$NON-NLS-1$

	protected String tickVisibleLabel = Messages.getString("LabelAttributesComposite.Lbl.IsVisible"); //$NON-NLS-1$

	private transient Composite cmpContent = null;

	private transient Composite cmpLines = null;

	private transient Label lblStyle = null;

	private transient ChartCombo cmbTickStyle = null;

	private transient Grid grid = null;

	private transient Vector<Listener> vListeners = null;

	private int orientation;

	private transient boolean bLineGroupEnabled = true;

	private transient ChartWizardContext context;

	private boolean bTicksVisible = true;

	private Grid defGrid;

	private boolean bEnableStyles;

	private boolean bEnableVisibility;

	private boolean bEnableColor;

	// Grid Attribute Change Events
	public static final int LINE_STYLE_CHANGED_EVENT = 1;

	public static final int LINE_WIDTH_CHANGED_EVENT = 2;

	public static final int LINE_COLOR_CHANGED_EVENT = 3;

	public static final int LINE_VISIBILITY_CHANGED_EVENT = 4;

	public static final int TICK_STYLE_CHANGED_EVENT = 5;

	public static final int TICK_COLOR_CHANGED_EVENT = 6;

	public static final int TICK_VISIBILITY_CHANGED_EVENT = 7;

	// Optional Styles
	public static final int ENABLE_STYLES = 1 << 1;

	public static final int ENABLE_VISIBILITY = 1 << 2;

	public static final int ENABLE_COLOR = 1 << 3;

	/**
	 * @param parent
	 * @param style
	 */
	public GridAttributesComposite(Composite parent, int style, int optionalStyles, ChartWizardContext context,
			Grid grid, int orientation, boolean bTicksVisible, Grid defGrid) {
		super(parent, style);
		this.orientation = orientation;
		this.context = context;
		this.bTicksVisible = bTicksVisible;
		this.defGrid = defGrid;
		setOptionalStyles(optionalStyles);
		init(grid);
		placeComponents();
	}

	public GridAttributesComposite(Composite parent, int style, int optionalStyles, ChartWizardContext context,
			Grid grid, boolean bLineGroupEnabled, Grid defGrid) {
		super(parent, style);
		this.bLineGroupEnabled = bLineGroupEnabled;
		this.context = context;
		this.defGrid = defGrid;
		setOptionalStyles(optionalStyles);
		init(grid);
		placeComponents();
	}

	/**
	 * 
	 */
	private void setOptionalStyles(int optionalStyles) {
		this.bEnableStyles = (ENABLE_STYLES == (optionalStyles & ENABLE_STYLES));
		this.bEnableVisibility = (ENABLE_VISIBILITY == (optionalStyles & ENABLE_VISIBILITY));
		this.bEnableColor = (ENABLE_COLOR == (optionalStyles & ENABLE_COLOR));
	}

	/**
	 * 
	 */
	protected void init(Grid grid) {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
		this.grid = grid;
		this.vListeners = new Vector<Listener>();
	}

	/**
	 * 
	 */
	private void placeComponents() {
		// Layout for entire composite
		FillLayout flMain = new FillLayout();
		flMain.marginHeight = 4;
		flMain.marginWidth = 2;

		// Layout for content composite
		GridLayout glContent = new GridLayout();
		glContent.verticalSpacing = 5;
		glContent.horizontalSpacing = 5;
		glContent.marginHeight = 0;
		glContent.marginWidth = 0;
		glContent.numColumns = 2;

		// Layout for Ticks group
		GridLayout glTicks = new GridLayout();
		glTicks.marginHeight = 4;
		glTicks.marginWidth = 4;
		glTicks.verticalSpacing = 2;
		glTicks.horizontalSpacing = 5;
		glTicks.numColumns = 2;

		this.setLayout(flMain);

		// Content Composite
		cmpContent = new Composite(this, SWT.NONE);
		cmpContent.setLayout(glContent);

		// Grid Lines group

		if (bLineGroupEnabled) {
			// Layout for Grid Lines group
			FillLayout flLines = new FillLayout();
			flLines.marginHeight = 1;
			flLines.marginWidth = 1;

			cmpLines = new Composite(cmpContent, SWT.NONE);
			GridData gdCMPLines = new GridData(GridData.FILL_BOTH);
			gdCMPLines.horizontalSpan = 2;
			cmpLines.setLayoutData(gdCMPLines);
			cmpLines.setLayout(flLines);

			// Line Attributes for Grid Lines
			int lineStyels = LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_STYLES
					| LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_COLOR;
			lineStyels |= context.getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR
					: lineStyels;

			final String lineVisibleLabel = this.lineVisibleLabel;
			liacLines = new LineAttributesComposite(cmpLines, SWT.NONE, lineStyels, context, grid.getLineAttributes(),
					defGrid.getLineAttributes()) {
				protected void placeComponents() {
					super.placeComponents();
					if (btnVisible != null) {
						btnVisible.setText(lineVisibleLabel);
					}
				}
			};
			liacLines.addListener(this);
			liacLines.setAttributesEnabled(ChartUIUtil.is3DWallFloorSet(context.getModel()));
		}

		// Ticks group (unsupported in 3D)
		if (bTicksVisible) {
			Group grpTicks = new Group(cmpContent, SWT.NONE);
			{
				GridData gdGRPTicks = new GridData(GridData.FILL_BOTH);
				gdGRPTicks.horizontalSpan = 2;
				grpTicks.setLayoutData(gdGRPTicks);
				grpTicks.setLayout(glTicks);
				grpTicks.setText(Messages.getString("GridAttributesComposite.Lbl.Ticks")); //$NON-NLS-1$
			}

			// Line Attributes for Ticks
			int lineStyles = 0;

			if (bEnableVisibility) {
				lineStyles |= LineAttributesComposite.ENABLE_VISIBILITY;
			}

			if (bEnableColor) {
				lineStyles |= LineAttributesComposite.ENABLE_COLOR;
				lineStyles |= context.getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR
						: lineStyles;
			}

			final String tickVisibleLabel = this.tickVisibleLabel;
			liacTicks = new LineAttributesComposite(grpTicks, SWT.NONE, lineStyles, context, grid.getTickAttributes(),
					defGrid.getTickAttributes()) {
				protected void placeComponents() {
					super.placeComponents();
					if (btnVisible != null) {
						btnVisible.setText(tickVisibleLabel);
					}
				}

			};
			{
				GridData gdLIACTicks = new GridData(GridData.FILL_HORIZONTAL);
				gdLIACTicks.horizontalSpan = 2;
				liacTicks.setLayoutData(gdLIACTicks);
				liacTicks.addListener(this);
			}

			if (bEnableStyles) {
				// Tick Styles
				boolean tickUIEnabled = !context.getUIFactory().isSetInvisible(grid.getTickAttributes());
				lblStyle = new Label(grpTicks, SWT.NONE);
				{
					GridData gdLBLStyle = new GridData();
					gdLBLStyle.horizontalIndent = 4;
					lblStyle.setLayoutData(gdLBLStyle);
					lblStyle.setText(Messages.getString("GridAttributesComposite.Lbl.Style")); //$NON-NLS-1$
					lblStyle.setEnabled(tickUIEnabled);
				}

				cmbTickStyle = context.getUIFactory().createChartCombo(grpTicks, SWT.DROP_DOWN | SWT.READ_ONLY, grid,
						"tickStyle", //$NON-NLS-1$
						defGrid.getTickStyle().getName());
				{
					GridData gdCMBTickStyle = new GridData(GridData.FILL_HORIZONTAL);
					cmbTickStyle.setLayoutData(gdCMBTickStyle);
					cmbTickStyle.addSelectionListener(this);
					cmbTickStyle.setEnabled(tickUIEnabled);
				}
			}

			populateLists();
			setDefaultSelections();
		}

	}

	private void populateLists() {
		if (bEnableStyles) {
			if (orientation == Orientation.HORIZONTAL) {
				cmbTickStyle.setItems(LiteralHelper.horizontalTickStyleSet.getDisplayNames());
				cmbTickStyle.setItemData(LiteralHelper.horizontalTickStyleSet.getNames());
			} else if (orientation == Orientation.VERTICAL) {
				cmbTickStyle.setItems(LiteralHelper.verticalTickStyleSet.getDisplayNames());
				cmbTickStyle.setItemData(LiteralHelper.verticalTickStyleSet.getNames());
			}
		}
	}

	private void setDefaultSelections() {
		if (bEnableStyles) {
			cmbTickStyle.setSelection(grid.getTickStyle().getName());
		}
	}

	public void addListener(Listener listener) {
		vListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(cmbTickStyle)) {
			String selectedTickStyle = cmbTickStyle.getSelectedItemData();
			Event eGrid = new Event();
			eGrid.widget = this;
			eGrid.type = TICK_STYLE_CHANGED_EVENT;
			eGrid.detail = (selectedTickStyle == null) ? ChartUIExtensionUtil.PROPERTY_UNSET
					: ChartUIExtensionUtil.PROPERTY_UPDATE;
			TickStyle tsGrid = TickStyle.getByName(selectedTickStyle);
			eGrid.data = tsGrid;
			fireEvent(eGrid);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public Point getPreferredSize() {
		return new Point(230, 240);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		Event eGrid = new Event();
		eGrid.widget = this;
		eGrid.detail = event.detail;
		if (event.widget.equals(liacLines)) {
			if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				eGrid.type = LINE_STYLE_CHANGED_EVENT;
				eGrid.data = event.data;
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				eGrid.type = LINE_WIDTH_CHANGED_EVENT;
				eGrid.data = event.data;
			} else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				eGrid.type = LINE_COLOR_CHANGED_EVENT;
				eGrid.data = event.data;
			} else {
				eGrid.type = LINE_VISIBILITY_CHANGED_EVENT;
				eGrid.data = event.data;
			}
		} else if (event.widget.equals(liacTicks)) {
			if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				eGrid.type = TICK_COLOR_CHANGED_EVENT;
				eGrid.data = event.data;
			} else {
				eGrid.type = TICK_VISIBILITY_CHANGED_EVENT;
				eGrid.data = event.data;
				boolean enabledUI = !((eGrid.detail != ChartUIExtensionUtil.PROPERTY_UNSET)
						&& !((Boolean) event.data).booleanValue());
				lblStyle.setEnabled(enabledUI);
				cmbTickStyle.setEnabled(enabledUI);
			}
		} else {
			// Unknown event
			return;
		}
		fireEvent(eGrid);
	}

	private void fireEvent(Event event) {
		for (int i = 0; i < vListeners.size(); i++) {
			vListeners.get(i).handleEvent(event);
		}
	}

}
