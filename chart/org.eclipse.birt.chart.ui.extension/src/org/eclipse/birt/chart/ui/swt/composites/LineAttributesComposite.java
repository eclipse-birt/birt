/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import org.eclipse.birt.chart.ui.swt.AbstractLineStyleChooserComposite;
import org.eclipse.birt.chart.ui.swt.AbstractLineWidthChooserComposite;
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

/**
 * @author Actuate Corporation
 *
 */
public class LineAttributesComposite extends Composite implements SelectionListener, Listener {

	private transient Composite cmpContent = null;

	private transient Label lblStyle = null;

	private transient Label lblWidth = null;

	private transient Label lblColor = null;

	private transient AbstractLineStyleChooserComposite cmbStyle = null;

	private transient AbstractLineWidthChooserComposite cmbWidth = null;

	private transient FillChooserComposite cmbColor = null;

	protected ChartCheckbox btnVisible = null;

	private transient LineAttributes laCurrent = null;

	private transient boolean bEnableWidths = true;

	private transient boolean bEnableStyles = true;

	private transient boolean bEnableVisibility = true;

	private transient boolean bEnableAutoColor = false;

	private transient Vector<Listener> vListeners = null;

	public static final int STYLE_CHANGED_EVENT = 1;

	public static final int WIDTH_CHANGED_EVENT = 2;

	public static final int COLOR_CHANGED_EVENT = 3;

	public static final int VISIBILITY_CHANGED_EVENT = 4;

	private transient boolean bEnabled = true;

	private transient boolean bEnableColor = true;

	private transient ChartWizardContext wizardContext;

	private LineAttributes defLineAttributes;

	/**
	 * @param parent
	 * @param style
	 */
	public LineAttributesComposite(Composite parent, int style, ChartWizardContext wizardContext,
			LineAttributes laCurrent, boolean bEnableWidths, boolean bEnableStyles, boolean bEnableVisibility,
			LineAttributes defLineAttributes) {
		super(parent, style);
		this.laCurrent = laCurrent;
		if (laCurrent == null) {
			// Create a default line attributes instance
			this.laCurrent = AttributeFactory.eINSTANCE.createLineAttributes();
		}
		this.bEnableStyles = bEnableStyles;
		this.bEnableWidths = bEnableWidths;
		this.bEnableVisibility = bEnableVisibility;
		this.wizardContext = wizardContext;
		this.defLineAttributes = defLineAttributes;
		init();
		placeComponents();
	}

	public LineAttributesComposite(Composite parent, int style, ChartWizardContext wizardContext,
			LineAttributes laCurrent, boolean bEnableWidths, boolean bEnableStyles, boolean bEnableVisibility,
			boolean bEnableColor, LineAttributes defLineAttributes) {
		super(parent, style);
		this.laCurrent = laCurrent;
		if (laCurrent == null) {
			// Create a default line attributes instance
			this.laCurrent = AttributeFactory.eINSTANCE.createLineAttributes();
		}
		this.bEnableStyles = bEnableStyles;
		this.bEnableWidths = bEnableWidths;
		this.bEnableVisibility = bEnableVisibility;
		this.bEnableColor = bEnableColor;
		this.wizardContext = wizardContext;
		this.defLineAttributes = defLineAttributes;
		init();
		placeComponents();
	}

	public LineAttributesComposite(Composite parent, int style, ChartWizardContext wizardContext,
			LineAttributes laCurrent, boolean bEnableWidths, boolean bEnableStyles, boolean bEnableVisibility,
			boolean bEnableColor, boolean bEnableAutoColor, LineAttributes defLineAttributes) {
		super(parent, style);
		this.laCurrent = laCurrent;
		if (laCurrent == null) {
			// Create a default line attributes instance
			this.laCurrent = AttributeFactory.eINSTANCE.createLineAttributes();
		}
		this.bEnableStyles = bEnableStyles;
		this.bEnableWidths = bEnableWidths;
		this.bEnableVisibility = bEnableVisibility;
		this.bEnableColor = bEnableColor;
		this.bEnableAutoColor = bEnableAutoColor;
		this.wizardContext = wizardContext;
		this.defLineAttributes = defLineAttributes;
		init();
		placeComponents();
	}

	public static final int ENABLE_WIDTH = 1;

	public static final int ENABLE_STYLES = 1 << 1;

	public static final int ENABLE_VISIBILITY = 1 << 2;

	public static final int ENABLE_COLOR = 1 << 3;

	public static final int ENABLE_AUTO_COLOR = 1 << 4;

	public LineAttributesComposite(Composite parent, int style, int optionalStyles, ChartWizardContext wizardContext,
			LineAttributes laCurrent, LineAttributes defLineAttributes) {
		super(parent, style);
		this.laCurrent = laCurrent;
		if (laCurrent == null) {
			// Create a default line attributes instance
			this.laCurrent = AttributeFactory.eINSTANCE.createLineAttributes();
		}
		this.bEnableStyles = ENABLE_STYLES == (optionalStyles & ENABLE_STYLES);
		this.bEnableWidths = ENABLE_WIDTH == (optionalStyles & ENABLE_WIDTH);
		this.bEnableVisibility = ENABLE_VISIBILITY == (optionalStyles & ENABLE_VISIBILITY);
		this.bEnableColor = ENABLE_COLOR == (optionalStyles & ENABLE_COLOR);
		this.bEnableAutoColor = ENABLE_AUTO_COLOR == (optionalStyles & ENABLE_AUTO_COLOR);
		this.wizardContext = wizardContext;
		this.defLineAttributes = defLineAttributes;
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
	protected void placeComponents() {
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

		bEnabled = !wizardContext.getUIFactory().isSetInvisible(laCurrent);
		boolean bEnableUI = bEnabled;
		if (bEnableVisibility) {
			btnVisible = wizardContext.getUIFactory().createChartCheckbox(cmpContent, SWT.NONE,
					this.defLineAttributes.isVisible());
			GridData gdCBVisible = new GridData(GridData.FILL_HORIZONTAL);
			gdCBVisible.horizontalSpan = 6;
			btnVisible.setLayoutData(gdCBVisible);
			btnVisible.setText(Messages.getString("LabelAttributesComposite.Lbl.IsVisible")); //$NON-NLS-1$

			int state = laCurrent.isSetVisible()
					? (laCurrent.isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED;
			btnVisible.setSelectionState(state);
			btnVisible.addSelectionListener(this);
			if (bEnabled) {
				bEnableUI = wizardContext.getUIFactory().canEnableUI(btnVisible);
			}
		}

		if (bEnableStyles) {
			lblStyle = new Label(cmpContent, SWT.NONE);
			GridData gdLStyle = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			lblStyle.setLayoutData(gdLStyle);
			lblStyle.setText(Messages.getString("LineAttributesComposite.Lbl.Style")); //$NON-NLS-1$
			lblStyle.setEnabled(bEnableUI);

			cmbStyle = wizardContext.getUIFactory().createLineStyleChooserComposite(cmpContent,
					SWT.DROP_DOWN | SWT.READ_ONLY, getSWTLineStyle(laCurrent.getStyle()), getLineStyleItems(),
					laCurrent, "style");//$NON-NLS-1$
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

			cmbWidth = wizardContext.getUIFactory().createLineWidthChooserComposite(cmpContent,
					SWT.DROP_DOWN | SWT.READ_ONLY, laCurrent.getThickness(),
					new Integer[] { 1, 2, 3, 4 },
					laCurrent, "thickness");//$NON-NLS-1$
			GridData gdCBWidth = new GridData(GridData.FILL_HORIZONTAL);
			gdCBWidth.horizontalSpan = 5;
			cmbWidth.setLayoutData(gdCBWidth);
			cmbWidth.addListener(LineWidthChooserComposite.SELECTION_EVENT, this);
			cmbWidth.setEnabled(bEnableUI);
		}

		if (bEnableColor) {
			lblColor = new Label(cmpContent, SWT.NONE);
			GridData gdLColor = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
			lblColor.setLayoutData(gdLColor);
			lblColor.setText(Messages.getString("LineAttributesComposite.Lbl.Color")); //$NON-NLS-1$
			lblColor.setEnabled(bEnableUI);

			int iFillOption = FillChooserComposite.DISABLE_PATTERN_FILL | FillChooserComposite.ENABLE_TRANSPARENT
					| FillChooserComposite.ENABLE_TRANSPARENT_SLIDER;

			if (bEnableAutoColor) {
				iFillOption |= FillChooserComposite.ENABLE_AUTO;
			}

			cmbColor = new FillChooserComposite(cmpContent, SWT.DROP_DOWN | SWT.READ_ONLY, iFillOption, wizardContext,
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

	/**
	 * @deprecated To use {@link #setAttributesEnabled(boolean)}
	 */
	@Deprecated
	@Override
	public void setEnabled(boolean bState) {
		setAttributesEnabled(bState);
	}

	public void setAttributesEnabled(boolean bState) {
		if (this.bEnableVisibility) {
			btnVisible.setEnabled(bState);
		}
		boolean bEnableUI = btnVisible != null ? wizardContext.getUIFactory().canEnableUI(btnVisible)
				: !wizardContext.getUIFactory().isSetInvisible(laCurrent);
		if (this.bEnableStyles) {
			lblStyle.setEnabled(bState && bEnableUI);
			cmbStyle.setEnabled(bState && bEnableUI);
		}
		if (this.bEnableWidths) {
			lblWidth.setEnabled(bState && bEnableUI);
			cmbWidth.setEnabled(bState && bEnableUI);
		}
		if (this.bEnableColor) {
			lblColor.setEnabled(bState && bEnableUI);
			cmbColor.setEnabled(bState && bEnableUI);
		}
		this.bEnabled = bState;
	}

	public boolean isAttributesEnabled() {
		return this.bEnabled;
	}

	public void addListener(Listener listener) {
		vListeners.add(listener);
	}

	public void setLineAttributes(LineAttributes attributes) {
		if (attributes == null) {
			// Create a default line attributes instance
			attributes = AttributeFactory.eINSTANCE.createLineAttributes();
		}
		laCurrent = attributes;

		if (bEnableVisibility) {
			int state = laCurrent.isSetVisible()
					? (laCurrent.isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED;
			btnVisible.setSelectionState(state);
			boolean bUIEnabled = (wizardContext.getUIFactory().canEnableUI(btnVisible));
			if (bEnableStyles) {
				cmbStyle.setEnabled(bUIEnabled);
				lblStyle.setEnabled(bUIEnabled);
			}
			if (bEnableWidths) {
				cmbWidth.setEnabled(bUIEnabled);
				lblWidth.setEnabled(bUIEnabled);
			}
			if (bEnableColor) {
				cmbColor.setEnabled(bUIEnabled);
				lblColor.setEnabled(bUIEnabled);
			}
		}
		if (bEnableStyles) {
			cmbStyle.setLineStyle(laCurrent.getStyle(), laCurrent);
		}
		if (this.bEnableWidths) {
			cmbWidth.setLineWidth(laCurrent.getThickness(), laCurrent);
		}
		if (this.bEnableColor) {
			cmbColor.setFill(laCurrent.getColor());
			cmbColor.redraw();
		}
		redraw();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		Object oSource = e.getSource();
		if (oSource.equals(btnVisible)) {
			// Notify Listeners that a change has occurred in the value
			int state = btnVisible.getSelectionState();
			fireValueChangedEvent(LineAttributesComposite.VISIBILITY_CHANGED_EVENT,
					Boolean.valueOf(state == ChartCheckbox.STATE_SELECTED),
					(state == ChartCheckbox.STATE_GRAYED) ? ChartUIExtensionUtil.PROPERTY_UNSET
							: ChartUIExtensionUtil.PROPERTY_UPDATE);
			// Notification may cause this class disposed
			if (isDisposed()) {
				return;
			}
			// Enable/Disable UI Elements
			boolean bEnableUI = (wizardContext.getUIFactory().canEnableUI(btnVisible));
			if (bEnableStyles) {
				lblStyle.setEnabled(bEnableUI);
				cmbStyle.setEnabled(bEnableUI);
			}
			if (bEnableWidths) {
				lblWidth.setEnabled(bEnableUI);
				cmbWidth.setEnabled(bEnableUI);
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
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
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

	/*
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

	/*
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
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if (cmbColor != null && cmbColor.equals(event.widget)) {
			fireValueChangedEvent(LineAttributesComposite.COLOR_CHANGED_EVENT, cmbColor.getFill(),
					cmbColor.getFill() == null ? ChartUIExtensionUtil.PROPERTY_UNSET
							: ChartUIExtensionUtil.PROPERTY_UPDATE);
		} else if (cmbStyle != null && cmbStyle.equals(event.widget)) {
			LineStyle ls = getModelLineStyle(cmbStyle.getLineStyle());
			fireValueChangedEvent(LineAttributesComposite.STYLE_CHANGED_EVENT, ls,
					ls == null ? ChartUIExtensionUtil.PROPERTY_UNSET : ChartUIExtensionUtil.PROPERTY_UPDATE);
		} else if (cmbWidth != null && cmbWidth.equals(event.widget)) {
			fireValueChangedEvent(LineAttributesComposite.WIDTH_CHANGED_EVENT, Integer.valueOf(cmbWidth.getLineWidth()),
					cmbWidth.getLineWidth() < 1 ? ChartUIExtensionUtil.PROPERTY_UNSET
							: ChartUIExtensionUtil.PROPERTY_UPDATE);
		}
	}

	/**
	 * Provides a method to override line styles list
	 *
	 * @return style identifier array
	 */
	protected Integer[] getLineStyleItems() {
		return new Integer[] { SWT.LINE_SOLID, SWT.LINE_DASH, SWT.LINE_DASHDOT, SWT.LINE_DOT };
	}
}
