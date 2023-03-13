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

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartIntSpinner;
import org.eclipse.birt.chart.ui.swt.AbstractLineStyleChooserComposite;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class GanttLineAttributesComposite extends Composite implements SelectionListener, Listener {

	private transient Composite cmpContent = null;

	private transient Label lblStyle = null;

	private transient Label lblWidth = null;

	private transient Label lblColor = null;

	private transient AbstractLineStyleChooserComposite cmbStyle = null;

	private transient AbstractChartIntSpinner iscWidth = null;

	private transient FillChooserComposite cmbColor = null;

	private transient ChartCheckbox btnVisible = null;

	private transient LineAttributes laCurrent = null;

	private transient boolean bEnableWidths = true;

	private transient boolean bEnableStyles = true;

	private transient boolean bEnableVisibility = true;

	private transient Vector<Listener> vListeners = null;

	public static final int STYLE_CHANGED_EVENT = 1;

	public static final int WIDTH_CHANGED_EVENT = 2;

	public static final int COLOR_CHANGED_EVENT = 3;

	public static final int VISIBILITY_CHANGED_EVENT = 4;

	public static final int ENABLE_WIDTH = 1;
	public static final int ENABLE_STYLES = 1 << 1;
	public static final int ENABLE_VISIBILITY = 1 << 2;

	private transient boolean bEnabled = true;

	private transient boolean bEnableColor = true;

	private transient ChartWizardContext context;

	private LineAttributes defaultLineAttributes;

	/**
	 * @param parent
	 * @param style
	 */
	public GanttLineAttributesComposite(Composite parent, ChartWizardContext context, int style,
			LineAttributes laCurrent, int optionalStyles, LineAttributes defaultLineAttributes) {
		super(parent, style);
		this.context = context;
		this.laCurrent = laCurrent;
		if (laCurrent == null) {
			// Create a default line attributes instance
			this.laCurrent = AttributeFactory.eINSTANCE.createLineAttributes();
		}
		this.bEnableStyles = (ENABLE_STYLES == (optionalStyles & ENABLE_STYLES));
		this.bEnableWidths = (ENABLE_WIDTH == (optionalStyles & ENABLE_WIDTH));
		this.bEnableVisibility = (ENABLE_VISIBILITY == (optionalStyles & ENABLE_VISIBILITY));
		this.defaultLineAttributes = defaultLineAttributes;
		init();
		placeComponents();
	}

	/**
	 *
	 */
	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
		vListeners = new Vector<>();
	}

	/**
	 *
	 */
	private void placeComponents() {
		FillLayout flMain = new FillLayout();
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;

		GridLayout glContent = new GridLayout();
		glContent.verticalSpacing = 5;
		glContent.horizontalSpacing = 5;
		glContent.marginHeight = 4;
		glContent.marginWidth = 4;
		glContent.numColumns = 6;

		this.setLayout(flMain);

		cmpContent = new Composite(this, SWT.NONE);
		cmpContent.setLayout(glContent);

		bEnabled = !context.getUIFactory().isSetInvisible(laCurrent);
		boolean bEnableUI = bEnabled;
		if (bEnableVisibility) {
			btnVisible = context.getUIFactory().createChartCheckbox(cmpContent, SWT.NONE,
					this.defaultLineAttributes.isVisible());
			GridData gdCBVisible = new GridData(GridData.FILL_HORIZONTAL);
			gdCBVisible.horizontalSpan = 6;
			btnVisible.setLayoutData(gdCBVisible);
			btnVisible.setText(Messages.getString("LineAttributesComposite.Lbl.IsVisible")); //$NON-NLS-1$
			btnVisible.setSelectionState(laCurrent.isSetVisible()
					? (laCurrent.isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED);
			btnVisible.addSelectionListener(this);
			if (bEnabled) {
				bEnableUI = context.getUIFactory().canEnableUI(btnVisible);
			}
		}

		if (bEnableStyles) {
			lblStyle = new Label(cmpContent, SWT.NONE);
			GridData gdLStyle = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			lblStyle.setLayoutData(gdLStyle);
			lblStyle.setText(Messages.getString("LineAttributesComposite.Lbl.Style")); //$NON-NLS-1$
			lblStyle.setEnabled(bEnableUI);

			cmbStyle = context.getUIFactory().createLineStyleChooserComposite(cmpContent, SWT.DROP_DOWN | SWT.READ_ONLY,
					getSWTLineStyle(laCurrent.getStyle()),
					new Integer[] { SWT.LINE_SOLID, SWT.LINE_DASH, SWT.LINE_DASHDOT, SWT.LINE_DOT }, laCurrent,
					"style"); //$NON-NLS-1$
			GridData gdCBStyle = new GridData(GridData.FILL_HORIZONTAL);
			gdCBStyle.horizontalSpan = 5;
			cmbStyle.setLayoutData(gdCBStyle);
			cmbStyle.addListener(LineStyleChooserComposite.SELECTION_EVENT, this);
			cmbStyle.setEnabled(bEnableUI);
		}

		if (bEnableWidths) {
			lblWidth = new Label(cmpContent, SWT.NONE);
			GridData gdLWidth = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			lblWidth.setLayoutData(gdLWidth);
			lblWidth.setText(Messages.getString("LineAttributesComposite.Lbl.Width")); //$NON-NLS-1$
			lblWidth.setEnabled(bEnableUI);

			iscWidth = context.getUIFactory().createChartIntSpinner(cmpContent, SWT.NONE, laCurrent.getThickness(),
					laCurrent, "thickness", //$NON-NLS-1$
					bEnableUI);
			GridData gdISCWidth = new GridData(GridData.FILL_HORIZONTAL);
			gdISCWidth.horizontalSpan = 5;
			iscWidth.setLayoutData(gdISCWidth);
			iscWidth.setMinimum(1);
			iscWidth.setMaximum(100);
			iscWidth.addListener(this);
			if (iscWidth instanceof IntegerSpinControl) {
				((IntegerSpinControl) iscWidth).addScreenreaderAccessbility(lblWidth.getText());
			}
		}

		if (bEnableColor) {
			lblColor = new Label(cmpContent, SWT.NONE);
			GridData gdLColor = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			lblColor.setLayoutData(gdLColor);
			lblColor.setText(Messages.getString("LineAttributesComposite.Lbl.Color")); //$NON-NLS-1$
			lblColor.setEnabled(bEnableUI);
			int fillStyles = FillChooserComposite.ENABLE_TRANSPARENT | FillChooserComposite.ENABLE_TRANSPARENT_SLIDER
					| FillChooserComposite.DISABLE_PATTERN_FILL;
			fillStyles |= context.getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : fillStyles;
			cmbColor = new FillChooserComposite(cmpContent, SWT.DROP_DOWN | SWT.READ_ONLY, fillStyles, context,
					this.laCurrent.getColor());
			GridData gdCBColor = new GridData(GridData.FILL_HORIZONTAL);
			gdCBColor.horizontalSpan = 5;
			cmbColor.setLayoutData(gdCBColor);
			cmbColor.addListener(this);
			cmbColor.setEnabled(bEnableUI);
		}
	}

	public Point getPreferredSize() {
		Point ptSize = new Point(250, 40);
		if (bEnableVisibility) {
			ptSize.y += 30;
		}
		if (bEnableStyles) {
			ptSize.y += 30;
		}
		if (bEnableWidths) {
			ptSize.y += 30;
		}
		return ptSize;
	}

	@Override
	public void setEnabled(boolean bState) {
		boolean bEnableUI = true;
		if (this.bEnableVisibility) {
			btnVisible.setEnabled(bState);
			bEnableUI = context.getUIFactory().canEnableUI(btnVisible);
		}
		if (this.bEnableStyles) {
			lblStyle.setEnabled(bState && bEnableUI);
			cmbStyle.setEnabled(bState && bEnableUI);
		}
		if (this.bEnableWidths) {
			lblWidth.setEnabled(bState && bEnableUI);
			iscWidth.setEnabled(bState && bEnableUI);
		}
		if (this.bEnableColor) {
			lblColor.setEnabled(bState && bEnableUI);
			cmbColor.setEnabled(bState && bEnableUI);
		}
		this.bEnabled = bState;
	}

	@Override
	public boolean isEnabled() {
		return this.bEnabled;
	}

	public void addListener(Listener listener) {
		vListeners.add(listener);
	}

	public void setLineAttributes(LineAttributes attributes) {
		laCurrent = attributes;
		if (bEnableVisibility) {
			if (laCurrent == null || !laCurrent.isSetVisible()) {
				btnVisible.setSelectionState(ChartCheckbox.STATE_GRAYED);
			} else {
				btnVisible.setSelectionState(
						attributes.isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED);
			}
			boolean bUIEnabled = context.getUIFactory().canEnableUI(btnVisible);
			if (bEnableStyles) {
				cmbStyle.setEnabled(bUIEnabled);
				lblStyle.setEnabled(bUIEnabled);
			}
			if (bEnableWidths) {
				iscWidth.setEnabled(bUIEnabled);
				lblWidth.setEnabled(bUIEnabled);
			}
			if (bEnableColor) {
				cmbColor.setEnabled(bUIEnabled);
				lblColor.setEnabled(bUIEnabled);
			}
		}
		if (bEnableStyles) {
			cmbStyle.setLineStyle(attributes.getStyle(), attributes);
		}
		if (this.bEnableWidths) {
			if (laCurrent == null || !laCurrent.isSetThickness()) {
				iscWidth.setValue(0);
			} else {
				iscWidth.setValue(attributes.getThickness());
			}
		}
		if (this.bEnableColor) {
			if (laCurrent == null || laCurrent.getColor() == null) {
				cmbColor.setFill(null);
			} else {
				cmbColor.setFill(attributes.getColor());
			}
			cmbColor.redraw();
		}
		redraw();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		Object widget = e.widget;
		if (widget == btnVisible) {
			// Notify Listeners that a change has occurred in the value
			fireValueChangedEvent(GanttLineAttributesComposite.VISIBILITY_CHANGED_EVENT,
					Boolean.valueOf(btnVisible.getSelectionState() == ChartCheckbox.STATE_SELECTED),
					(btnVisible.getSelectionState() == ChartCheckbox.STATE_GRAYED) ? ChartUIExtensionUtil.PROPERTY_UNSET
							: ChartUIExtensionUtil.PROPERTY_UPDATE);
			// Notification may cause this class disposed
			if (isDisposed()) {
				return;
			}
			// Enable/Disable UI Elements
			boolean bEnableUI = context.getUIFactory().canEnableUI(btnVisible);
			if (bEnableStyles) {
				lblStyle.setEnabled(bEnableUI);
				cmbStyle.setEnabled(bEnableUI);
			}
			if (bEnableWidths) {
				lblWidth.setEnabled(bEnableUI);
				iscWidth.setEnabled(bEnableUI);
			}
			if (bEnableColor) {
				lblColor.setEnabled(bEnableUI);
				cmbColor.setEnabled(bEnableUI);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private void fireValueChangedEvent(int iEventType, Object data, int detail) {
		for (int iL = 0; iL < vListeners.size(); iL++) {
			Event se = new Event();
			se.widget = this;
			se.data = data;
			se.type = iEventType;
			se.detail = detail;
			vListeners.get(iL).handleEvent(se);
		}
	}

	/**
	 * Converts the specified SWT line style constant to a chart model's LineStyle
	 * object
	 */
	private LineStyle getModelLineStyle(int iStyle) {
		switch (iStyle) {
		case SWT.LINE_SOLID:
			return LineStyle.SOLID_LITERAL;
		case SWT.LINE_DASH:
			return LineStyle.DASHED_LITERAL;
		case SWT.LINE_DASHDOT:
			return LineStyle.DASH_DOTTED_LITERAL;
		case SWT.LINE_DOT:
			return LineStyle.DOTTED_LITERAL;
		default:
			return null;
		}
	}

	/**
	 * Converts the specified model line style to an appropriate SWT line style
	 * constant
	 */
	private int getSWTLineStyle(LineStyle style) {
		if (style.equals(LineStyle.DASHED_LITERAL)) {
			return SWT.LINE_DASH;
		} else if (style.equals(LineStyle.DASH_DOTTED_LITERAL)) {
			return SWT.LINE_DASHDOT;
		} else if (style.equals(LineStyle.DOTTED_LITERAL)) {
			return SWT.LINE_DOT;
		} else {
			return SWT.LINE_SOLID;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if (cmbColor != null && cmbColor.equals(event.widget)) {
			fireValueChangedEvent(GanttLineAttributesComposite.COLOR_CHANGED_EVENT, cmbColor.getFill(),
					(cmbColor.getFill() == null) ? ChartUIExtensionUtil.PROPERTY_UNSET
							: ChartUIExtensionUtil.PROPERTY_UPDATE);
		} else if (cmbStyle != null && cmbStyle.equals(event.widget)) {
			fireValueChangedEvent(GanttLineAttributesComposite.STYLE_CHANGED_EVENT,
					getModelLineStyle(cmbStyle.getLineStyle()),
					(cmbStyle.getLineStyle() < 1) ? ChartUIExtensionUtil.PROPERTY_UNSET
							: ChartUIExtensionUtil.PROPERTY_UPDATE);
		} else if (iscWidth != null && iscWidth.equals(event.widget)) {
			int action = ChartUIExtensionUtil.PROPERTY_UNSET;
			if (event.detail != ChartUIExtensionUtil.PROPERTY_UNSET) {
				action = (iscWidth.getValue() == 0) ? ChartUIExtensionUtil.PROPERTY_UNSET
						: ChartUIExtensionUtil.PROPERTY_UPDATE;
			}
			fireValueChangedEvent(GanttLineAttributesComposite.WIDTH_CHANGED_EVENT,
					Integer.valueOf(iscWidth.getValue()), action);
		}
	}
}
