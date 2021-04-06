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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.impl.AttributeFactoryImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.render.MarkerRenderer;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * 
 */

public class MarkerEditorComposite extends Composite implements MouseListener {
	/** Holds the width of each marker UI block */
	private final static int MARKER_BLOCK_WIDTH = 20;

	/** Holds the width of each marker UI block */
	private final static int MARKER_BLOCK_HEIGHT = 20;

	/** Holds the max number of each row */
	private final static int MARKER_ROW_MAX_NUMBER = 6;

	private transient Marker editingMarker;

	private transient IDeviceRenderer idrSWT = null;

	private transient Canvas cnvMarker;

	private transient Button btnDropDown;

	private transient Composite cmpDropDown;

	private NameSet markerTypeSet = LiteralHelper.markerTypeSet;

	private String outlineText = null;

	private Marker defaultMarker;

	private ChartWizardContext context;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 * @param marker
	 * @param defaultMarker
	 */
	public MarkerEditorComposite(Composite parent, Marker marker, ChartWizardContext context, Marker defaultMarker) {
		super(parent, SWT.BORDER);
		this.editingMarker = marker;
		this.context = context;
		this.defaultMarker = defaultMarker;
		placeComponents();
		initAccessible();
		updateMarkerPreview();

	}

	private void placeComponents() {
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		setLayout(layout);

		cnvMarker = new Canvas(this, SWT.DOUBLE_BUFFERED);
		{
			GridData gd = new GridData();
			gd.heightHint = MARKER_BLOCK_HEIGHT;
			gd.widthHint = MARKER_BLOCK_WIDTH;
			gd.verticalAlignment = SWT.CENTER;
			gd.grabExcessVerticalSpace = true;
			cnvMarker.setLayoutData(gd);
			cnvMarker.addMouseListener(this);
			cnvMarker.setToolTipText(getMarker().getType().getName());

			Listener listener = new Listener() {

				public void handleEvent(Event event) {
					canvasEvent(event);
				}
			};

			int[] textEvents = { SWT.KeyDown, SWT.KeyUp, SWT.Traverse, SWT.FocusIn, SWT.FocusOut, SWT.Paint };
			for (int i = 0; i < textEvents.length; i++) {
				cnvMarker.addListener(textEvents[i], listener);
			}
		}

		btnDropDown = new Button(this, SWT.ARROW | SWT.DOWN);
		{
			GridData gd = new GridData();
			gd.heightHint = 20;
			gd.widthHint = 16;
			btnDropDown.setLayoutData(gd);
			btnDropDown.addMouseListener(this);
		}

		try {
			idrSWT = ChartEngine.instance().getRenderer("dv.SWT"); //$NON-NLS-1$
		} catch (ChartException pex) {
			WizardBase.displayException(pex);
		}

		addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				if (idrSWT != null) {
					idrSWT.dispose();
					idrSWT = null;
				}
			}
		});
	}

	private void canvasEvent(Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			cnvMarker.redraw();
			break;
		}
		case SWT.FocusOut: {
			cnvMarker.redraw();
			break;
		}
		case SWT.KeyDown: {
			// At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed())
				break;

			if (event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
				event.doit = true;
				toggleDropDown();
			}
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
			case SWT.TRAVERSE_RETURN:
			case SWT.TRAVERSE_TAB_NEXT:
			case SWT.TRAVERSE_TAB_PREVIOUS:
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				event.doit = true;
				cnvMarker.redraw();
			}

			break;
		}
		case SWT.Paint:
			paintMarker(event.gc, getMarker(), LocationImpl.create(10, 10));
			break;
		}
	}

	public void setMarker(Marker marker) {
		this.editingMarker = marker;
		updateMarkerPreview();
	}

	private void updateMarkerPreview() {
		this.cnvMarker.setToolTipText(getMarker().getType().getName());
		this.cnvMarker.redraw();
	}

	public Marker getMarker() {
		return editingMarker;
	}

	private void toggleDropDown() {
		if (cmpDropDown == null || cmpDropDown.isDisposed()) {
			createDropDownComponent();
		} else {
			cmpDropDown.getShell().close();
		}
	}

	private void createDropDownComponent() {
		Point pLoc = UIHelper.getScreenLocation(btnDropDown.getParent());
		int iXLoc = pLoc.x;
		int iYLoc = pLoc.y + btnDropDown.getParent().getSize().y;
		int iShellWidth = MARKER_BLOCK_HEIGHT * MARKER_ROW_MAX_NUMBER + 15;

		if ((getStyle() & SWT.RIGHT_TO_LEFT) != 0) {
			iXLoc -= iShellWidth;
		}

		// Avoid the right boundary out of screen
		if (iXLoc + iShellWidth > this.getDisplay().getClientArea().width) {
			iXLoc = this.getDisplay().getClientArea().width - iShellWidth;
		}

		Shell shell = new Shell(this.getShell(), SWT.NONE);
		shell.setLayout(new FillLayout());
		// shell.setSize( iShellWidth, iShellHeight );
		shell.setLocation(iXLoc, iYLoc);

		cmpDropDown = new MarkerDropDownEditorComposite(shell, SWT.NONE);

		shell.layout();
		shell.pack();
		shell.setFocus(); // Set focus to this shell to receive key event.
		shell.open();
		cmpDropDown.setFocus();
	}

	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseDown(MouseEvent e) {
		toggleDropDown();
	}

	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void paintMarker(GC gc, Marker currentMarker, Location location) {
		// Paint an icon sample, not a real icon in the Fill
		Marker renderMarker = currentMarker;
		int markerSize = 4;
		if (currentMarker.getType() == MarkerType.ICON_LITERAL) {
			renderMarker = currentMarker.copyInstance();
			renderMarker.setFill(ImageImpl.create(UIHelper.getURL("icons/obj16/marker_icon.gif").toString())); //$NON-NLS-1$
			// To prevent the icon being too small in UI, use the original size
			// for icon
			markerSize = 0;
		}

		idrSWT.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, gc);
		final MarkerRenderer mr = new MarkerRenderer(idrSWT, StructureSource.createUnknown(null), location,
				LineAttributesImpl
						.create((getMarker().isSetVisible() && getMarker().isVisible()) ? ColorDefinitionImpl.BLUE()
								: ColorDefinitionImpl.GREY(), LineStyle.SOLID_LITERAL, 1),
				isMarkerTypeEnabled() ? ColorDefinitionImpl.create(80, 168, 218) : ColorDefinitionImpl.GREY(),
				renderMarker, markerSize, null, false, false);
		try {
			mr.draw(idrSWT);
			ChartWizard.removeException(ChartWizard.MarkerEdiCom_ID);
		} catch (ChartException ex) {
			ChartWizard.showException(ChartWizard.MarkerEdiCom_ID, ex.getLocalizedMessage());
		}

		// Render a boundary line to indicate focus
		if (cnvMarker.isFocusControl()) {
			gc.setLineStyle(SWT.LINE_DOT);
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.drawRectangle(0, 0, getSize().x - 21, this.getSize().y - 5);
		}

	}

	void initAccessible() {
		getAccessible().addAccessibleListener(new AccessibleAdapter() {

			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(new Point(e.x, e.y));
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(new Point(location.x, location.y));
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
			}

			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}
		});

		ChartUIUtil.addScreenReaderAccessibility(this, cnvMarker);
	}

	/**
	 * Set supported marker types.
	 * 
	 * @param markerTypeSet
	 */
	public void setSupportedMarkerTypes(NameSet markerTypeSet) {
		this.markerTypeSet = markerTypeSet;
	}

	/**
	 * Set outline text.
	 * 
	 * @param text
	 */
	public void setOutlineText(String text) {
		this.outlineText = text;
	}

	private boolean isMarkerTypeEnabled() {
		return getMarker().isSetVisible() && getMarker().isVisible() && getMarker().isSetType();
	}

	private class MarkerDropDownEditorComposite extends Composite implements PaintListener, Listener {

		private ChartSpinner iscMarkerSize;

		private ChartCheckbox btnMarkerVisible;

		private ChartCheckbox btnOutline;

		private Composite cmpType;

		private Group grpSize;

		boolean isPressingKey = false;

		private Widget focusedComposite;

		private final String[] typeDisplayNameSet = markerTypeSet.getDisplayNames();
		private final String[] typeNameSet = markerTypeSet.getNames();

		private int markerTypeIndex = -1;

		private Button btnAutotype;

		MarkerDropDownEditorComposite(Composite parent, int style) {
			super(parent, style);
			placeComponents();
			pack();
		}

		private void placeComponents() {
			GridLayout glDropDown = new GridLayout(2, false);
			this.setLayout(glDropDown);

			btnMarkerVisible = context.getUIFactory().createChartCheckbox(this, SWT.NONE,
					MarkerEditorComposite.this.defaultMarker.isVisible());
			{
				btnMarkerVisible.setText(Messages.getString("LineSeriesAttributeComposite.Lbl.IsVisible")); //$NON-NLS-1$
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				btnMarkerVisible.setLayoutData(gd);
				btnMarkerVisible.setSelectionState(getMarker().isSetVisible()
						? (getMarker().isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
						: ChartCheckbox.STATE_GRAYED);
				btnMarkerVisible.addListener(SWT.Selection, this);
				btnMarkerVisible.addListener(SWT.FocusOut, this);
				btnMarkerVisible.addListener(SWT.KeyDown, this);
				btnMarkerVisible.addListener(SWT.Traverse, this);
				btnMarkerVisible.addListener(SWT.FocusIn, this);
				btnMarkerVisible.setFocus();
			}

			Group grpType = new Group(this, SWT.NONE);
			{
				grpType.setLayout(new GridLayout(1, false));
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				grpType.setLayoutData(gd);
				grpType.setText(Messages.getString("MarkerEditorComposite.Label.MarkerType")); //$NON-NLS-1$
			}

			btnAutotype = new Button(grpType, SWT.CHECK);
			btnAutotype.setText(Messages.getString("ItemLabel.Auto"));//$NON-NLS-1$
			btnAutotype.setSelection(!getMarker().isSetType());
			btnAutotype.addListener(SWT.Selection, this);
			btnAutotype.addListener(SWT.FocusOut, this);
			btnAutotype.addListener(SWT.Traverse, this);
			btnAutotype.addListener(SWT.FocusIn, this);
			btnAutotype.setVisible(context.getUIFactory().supportAutoUI());

			cmpType = new Composite(grpType, SWT.NONE);
			{
				GridLayout layout = new GridLayout();
				layout.numColumns = MARKER_ROW_MAX_NUMBER;
				layout.marginHeight = 0;
				layout.marginWidth = 0;
				layout.horizontalSpacing = 0;
				layout.verticalSpacing = 0;
				cmpType.setLayout(layout);
				GridData gd = new GridData(GridData.FILL_BOTH);
				gd.horizontalSpan = 2;
				cmpType.setLayoutData(gd);
				cmpType.addListener(SWT.Traverse, this);
				cmpType.addListener(SWT.KeyDown, this);
				cmpType.addListener(SWT.FocusOut, this);
				cmpType.addListener(SWT.FocusIn, this);
			}

			int modifiedSize = (typeDisplayNameSet.length / MARKER_ROW_MAX_NUMBER + 1) * MARKER_ROW_MAX_NUMBER;
			for (int i = 0; i < modifiedSize; i++) {
				Canvas cnvType = new Canvas(cmpType, SWT.DOUBLE_BUFFERED);
				GridData gd = new GridData();
				gd.heightHint = MARKER_BLOCK_HEIGHT;
				gd.widthHint = MARKER_BLOCK_WIDTH;
				cnvType.setLayoutData(gd);
				cnvType.setData(Integer.valueOf(i));
				cnvType.addPaintListener(this);

				if (i < typeDisplayNameSet.length) {
					// Fake node to make borders more smooth
					cnvType.setToolTipText(typeDisplayNameSet[i]);
					cnvType.addListener(SWT.MouseDown, this);
				}
			}

			grpSize = new Group(this, SWT.NONE);
			{
				grpSize.setLayout(new GridLayout(2, false));
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				grpSize.setLayoutData(gd);
				grpSize.addListener(SWT.Traverse, this);
				grpSize.setText(Messages.getString("LineSeriesAttributeComposite.Lbl.Size")); //$NON-NLS-1$
			}

			iscMarkerSize = context.getUIFactory().createChartSpinner(grpSize, SWT.BORDER, getMarker(), "size", //$NON-NLS-1$
					context.getUIFactory().canEnableUI(btnMarkerVisible));
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				iscMarkerSize.setLayoutData(gd);
				iscMarkerSize.getWidget().setMinimum(0);
				iscMarkerSize.getWidget().setMaximum(100);
				iscMarkerSize.getWidget().setSelection(getMarker().getSize());
				iscMarkerSize.addListener(SWT.Selection, this);
				iscMarkerSize.addListener(SWT.FocusOut, this);
				iscMarkerSize.addListener(SWT.FocusIn, this);
				iscMarkerSize.addListener(SWT.Traverse, this);
			}

			btnOutline = context.getUIFactory().createChartCheckbox(this, SWT.NONE,
					MarkerEditorComposite.this.defaultMarker.getOutline().isVisible());
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				btnOutline.setLayoutData(gd);

				if (outlineText != null) {
					btnOutline.setText(outlineText + ":");//$NON-NLS-1$
				} else {
					btnOutline.setText(Messages.getString("MarkerEditorComposite.Button.Outline") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
				}

				btnOutline.addListener(SWT.Selection, this);
				btnOutline.addListener(SWT.FocusOut, this);
				btnOutline.addListener(SWT.FocusIn, this);
				btnOutline.addListener(SWT.KeyDown, this);
				btnOutline.addListener(SWT.Traverse, this);

				LineAttributes la = getMarker().getOutline();
				if (la == null) {
					ChartAdapter.beginIgnoreNotifications();
					la = AttributeFactoryImpl.eINSTANCE.createLineAttributes();
					la.eAdapters().addAll(getMarker().eAdapters());
					EObject o = getMarker();
					while (!(o instanceof LineSeries)) {
						o = o.eContainer();
						if (o == null)
							break;
					}
					if (o instanceof LineSeries) {
						la.setVisible(((LineSeries) o).getLineAttributes().isVisible());
					}
					ChartAdapter.endIgnoreNotifications();
				}

				getMarker().setOutline(la);
				btnOutline.setSelectionState(la.isSetVisible()
						? (la.isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
						: ChartCheckbox.STATE_GRAYED);
				updateOutlineBtn();
			}
			setEnabledState(context.getUIFactory().canEnableUI(btnMarkerVisible));
			if (btnAutotype.isVisible()) {
				setMarkerTypesState(
						context.getUIFactory().canEnableUI(btnMarkerVisible) && !btnAutotype.getSelection());
			} else {
				setMarkerTypesState(context.getUIFactory().canEnableUI(btnMarkerVisible));
			}
		}

		void widgetSelected(SelectionEvent e) {
			if (e.widget.equals(btnMarkerVisible)) {
				ChartElementUtil.setEObjectAttribute(getMarker(), "visible", //$NON-NLS-1$
						btnMarkerVisible.getSelectionState() == ChartCheckbox.STATE_SELECTED,
						btnMarkerVisible.getSelectionState() == ChartCheckbox.STATE_GRAYED);
				setEnabledState(context.getUIFactory().canEnableUI(btnMarkerVisible));
				cnvMarker.redraw();
				updateOutlineBtn();
			} else if (e.widget == btnOutline) {
				// Initialize default outline visible state to true.
				LineAttributes la = getMarker().getOutline();
				ChartElementUtil.setEObjectAttribute(la, "visible", //$NON-NLS-1$
						btnOutline.getSelectionState() == ChartCheckbox.STATE_SELECTED,
						btnOutline.getSelectionState() == ChartCheckbox.STATE_GRAYED);
				cnvMarker.redraw();
			} else if (e.widget == btnAutotype) {
				if (btnAutotype.getSelection()) {
					getMarker().unsetType();
					setMarkerTypesState(false);
				} else {
					if (defaultMarker != null && defaultMarker.isSetType()) {
						switchMarkerTypeImpl(defaultMarker.getType());
					} else {
						switchMarkerType(0);
					}

					setMarkerTypesState(true);
				}
				cnvMarker.redraw();
			}
		}

		private void setEnabledState(boolean isEnabled) {
			grpSize.setEnabled(isEnabled);
			btnAutotype.setEnabled(isEnabled);
			iscMarkerSize.setEnabled(isEnabled);
			setMarkerTypesState(isEnabled);
		}

		protected void setMarkerTypesState(boolean isEnabled) {
			cmpType.setEnabled(isEnabled);
			Control[] cnvTypes = cmpType.getChildren();
			for (int i = 0; i < cnvTypes.length; i++) {
				cnvTypes[i].setEnabled(isEnabled);
				cnvTypes[i].redraw();
			}
		}

		void focusLost(FocusEvent e) {
			Control currentControl = isPressingKey ? Display.getCurrent().getFocusControl()
					: Display.getCurrent().getCursorControl();
			// Set default value back
			isPressingKey = false;

			// If current control is the dropdown button, that means users want
			// to close it manually. Otherwise, close it silently when clicking
			// other areas.
			if (currentControl != btnDropDown && currentControl != cnvMarker && !isChildrenOfThis(currentControl)) {
				this.getShell().close();
			}
		}

		private boolean isChildrenOfThis(Control control) {
			while (control != null) {
				if (control == this) {
					return true;
				}
				control = control.getParent();
			}
			return false;
		}

		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			int markerIndex = ((Integer) e.widget.getData()).intValue();
			int markerLength = typeNameSet.length;
			String typeName = null;
			if (markerIndex < markerLength) {
				typeName = typeNameSet[markerIndex];
				gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
				gc.fillRectangle(0, 0, MARKER_BLOCK_WIDTH, MARKER_BLOCK_HEIGHT);
			}

			int lineWidth = 1;
			if (isMarkerTypeEnabled()) {
				gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			} else {
				gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
			}
			gc.setLineWidth(lineWidth);

			int x = lineWidth - 1;
			int y = lineWidth - 1;
			int width = MARKER_BLOCK_WIDTH + 1 - 2 * lineWidth;
			int height = MARKER_BLOCK_HEIGHT + 1 - 2 * lineWidth;

			if (markerIndex / MARKER_ROW_MAX_NUMBER < markerLength / MARKER_ROW_MAX_NUMBER) {
				// Remove the bottom border if not in the last row
				height++;
			}
			if ((markerIndex + 1) % MARKER_ROW_MAX_NUMBER != 0) {
				// Remove the right border if not in the rightmost column
				width++;
			}
			if (typeName == null) {
				if (markerIndex > markerLength) {
					// Remove the left and right border of the fake node unless
					// it's next to the last
					x = -1;
					width += 2;
				}
				// Remove the bottom border of the fake node
				height++;
			}
			// Draw the border
			gc.drawRectangle(x, y, width, height);

			// Draw the boarder of current marker
			if (getMarker().getType().getName().equals(typeName)) {
				markerTypeIndex = markerIndex;
				gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				gc.drawRectangle(1, 1, MARKER_BLOCK_WIDTH - 2, MARKER_BLOCK_HEIGHT - 2);
			}

			// Draw the marker
			if (typeName != null) {
				paintMarker(gc, MarkerImpl.create(MarkerType.getByName(typeName), 4),
						LocationImpl.create(MARKER_BLOCK_WIDTH / 2, MARKER_BLOCK_HEIGHT / 2));
			}
		}

		private void switchMarkerType(int newMarkerTypeIndex) {
			MarkerType newType = MarkerType.getByName(typeNameSet[newMarkerTypeIndex]);
			switchMarkerTypeImpl(newType);
		}

		private void switchMarkerTypeImpl(MarkerType newType) {
			if (newType == MarkerType.ICON_LITERAL) {
				ImageDialog iconDialog = (ImageDialog) context.getUIFactory().createChartMarkerIconDialog(new Shell(),
						getMarker().getFill(), context);
				if (iconDialog.open() == Window.OK) {
					Fill resultFill = iconDialog.getResult();
					if (resultFill.eAdapters().isEmpty()) {
						// Add adapters to new EObject
						resultFill.eAdapters().addAll(getMarker().eAdapters());
					}
					getMarker().setFill(resultFill);
				} else {
					// Without saving
					return;
				}
			}

			getMarker().setType(newType);

			updateMarkerPreview();
		}

		/**
		 * redraw only in windows platform according to bugzilla 276447
		 * 
		 * @param oldIndex
		 */
		private void redrawMarkers(int newMarkerTypeIndex) {
			// 276447, there's no need to redraw the markers composite since it
			// will be closed after type changed, and it will be disposed when
			// the icon dialog overlaps the markers composite on linux.
			if (cmpType != null && !cmpType.isDisposed()) {
				Control[] children = cmpType.getChildren();
				if (children == null || children[newMarkerTypeIndex] == null || children[markerTypeIndex] == null) {
					return;
				}
				children[newMarkerTypeIndex].redraw();
				children[markerTypeIndex].redraw();
			}
		}

		void mouseDown(MouseEvent e) {
			if (e.widget instanceof Canvas) {
				if (e.widget.getData() != null) {
					int markerIndex = ((Integer) e.widget.getData()).intValue();
					switchMarkerType(markerIndex);

					if (!this.isDisposed() && !this.getShell().isDisposed()) {
						this.getShell().close();
					}
				}
			}
		}

		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.FocusOut:
				focusLost(new FocusEvent(event));
				break;

			case SWT.FocusIn:
				focusedComposite = event.widget;
				break;

			case SWT.MouseDown:
				mouseDown(new MouseEvent(event));
				break;

			case SWT.Selection:
				widgetSelected(new SelectionEvent(event));
				break;

			case SWT.KeyDown:
				if (event.keyCode == SWT.ESC) {
					getShell().close();
				} else if (event.widget == cmpType) {
					int newIndex;
					if (event.keyCode == SWT.ARROW_LEFT) {
						if (markerTypeIndex - 1 >= 0) {
							newIndex = markerTypeIndex - 1;
							switchMarkerType(newIndex);
							redrawMarkers(newIndex);
						}
					} else if (event.keyCode == SWT.ARROW_RIGHT) {
						if (markerTypeIndex + 1 < typeNameSet.length) {
							newIndex = markerTypeIndex + 1;
							switchMarkerType(newIndex);
							redrawMarkers(newIndex);
						}
					} else if (event.keyCode == SWT.ARROW_UP) {
						if (markerTypeIndex - MARKER_ROW_MAX_NUMBER >= 0) {
							newIndex = markerTypeIndex - MARKER_ROW_MAX_NUMBER;
							switchMarkerType(newIndex);
							redrawMarkers(newIndex);
						}
					} else if (event.keyCode == SWT.ARROW_DOWN) {
						if (markerTypeIndex + MARKER_ROW_MAX_NUMBER < typeNameSet.length) {
							newIndex = markerTypeIndex + MARKER_ROW_MAX_NUMBER;
							switchMarkerType(newIndex);
							redrawMarkers(newIndex);
						}
					}
				}
				break;

			case SWT.Traverse:
				// Indicates getting focus control rather than
				// cursor control
				if (event.detail == SWT.TRAVERSE_TAB_NEXT || event.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
					event.doit = true;
					isPressingKey = true;
				} else if (event.keyCode == getMnemonicByText(grpSize.getText())) {
					updateIsPressingKey(iscMarkerSize);
				} else if (event.keyCode == getMnemonicByText(btnOutline.getButton().getText())) {
					updateIsPressingKey(btnOutline);
				} else if (event.keyCode == getMnemonicByText(btnMarkerVisible.getButton().getText())) {
					updateIsPressingKey(btnMarkerVisible);
				}
			}
		}

		private char getMnemonicByText(String string) {
			int index = 0;
			int length = string.length();
			do {
				while (index < length && string.charAt(index) != '&')
					index++;
				if (++index >= length)
					return '\0';
				if (string.charAt(index) != '&')
					return string.toLowerCase().charAt(index);
				index++;
			} while (index < length);
			return '\0';
		}

		private void updateIsPressingKey(Widget widget) {
			if (widget != focusedComposite) {
				isPressingKey = true;
			}
		}

		private void updateOutlineBtn() {
			btnOutline.setEnabled(context.getUIFactory().canEnableUI(btnMarkerVisible));
		}

	}

}
