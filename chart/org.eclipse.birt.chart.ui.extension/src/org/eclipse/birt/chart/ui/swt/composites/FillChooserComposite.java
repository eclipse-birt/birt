/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.text.MessageFormat;
import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.PatternImage;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

/**
 * FillChooserComposite
 */
public class FillChooserComposite extends Composite implements SelectionListener, DisposeListener, Listener {

	private Composite cmpContentInner = null;

	private Composite cmpContentOuter = null;

	private Composite cmpDropDown = null;

	private Composite cmpButtons = null;

	private FillCanvas cnvSelection = null;

	private Button btnDown = null;

	private Label lblTransparency = null;

	private Slider srTransparency = null;

	private Button btnCustom = null;

	private Button btnGradient = null;

	private Button btnImage = null;

	private Button btnPN = null;

	private Button btnPatternFill;

	private Button btnReset = null;

	private Button btnAuto = null;

	private static Color[] colorArray = null;

	private boolean bGradientEnabled = true;

	private boolean bGradientAngleEnabled = true;

	private boolean bImageEnabled = true;

	private boolean bEmbeddedImageEnabled = true;

	private boolean bResourceImageEnabled = false;

	private boolean bTransparentEnabled = true;

	private boolean bAutoEnabled = false;

	private Fill fCurrent = null;

	private boolean bTransparencyChanged = false;

	/** It indicates if the transparency slider is visible. */
	private boolean bTransparencySliderEnable = true;

	private boolean bPositiveNegativeEnabled = false;

	private boolean bPatternFillEnabled = true;

	private int iTransparency = 0;

	private Vector<Listener> vListeners = null;

	public static final int FILL_CHANGED_EVENT = 1;

	public static final int MOUSE_CLICKED_EVENT = 2;

	private boolean bEnabled = true;

	private int iSize = 18;

	private boolean bJustFocusLost = false;

	private ChartWizardContext wizardContext;

	// Indicates the last operation is fired by keyboard or not
	boolean isPressingKey = false;

	// Save the index of selected color
	private int selectedIndex = -1;

	public static final int ENABLE_GRADIENT = 1;
	public static final int ENABLE_IMAGE = 1 << 1;
	public static final int ENABLE_AUTO = 1 << 2;
	public static final int ENABLE_TRANSPARENT = 1 << 3;
	public static final int ENABLE_TRANSPARENT_SLIDER = 1 << 4;
	public static final int ENABLE_POSITIVE_NEGATIVE = 1 << 5;
	public static final int DISABLE_GRADIENT_ANGLE = 1 << 6;
	public static final int DISABLE_PATTERN_FILL = 1 << 7;
	public static final int DISABLE_EMBEDDED_IMAGE = 1 << 8;
	public static final int ENABLE_RESOURCE_IMAGE = 1 << 9;

	/**
	 * @param parent
	 * @param style
	 * @param optionalStyle
	 * @param wizardContext
	 * @param fCurrent
	 */
	public FillChooserComposite(Composite parent, int style, int optionalStyle, ChartWizardContext wizardContext,
			Fill fCurrent) {
		this(parent, style, wizardContext, fCurrent, ((ENABLE_GRADIENT & optionalStyle) == ENABLE_GRADIENT),
				((ENABLE_IMAGE & optionalStyle) == ENABLE_IMAGE), ((ENABLE_AUTO & optionalStyle) == ENABLE_AUTO),
				((ENABLE_TRANSPARENT & optionalStyle) == ENABLE_TRANSPARENT),
				((ENABLE_POSITIVE_NEGATIVE & optionalStyle) == ENABLE_POSITIVE_NEGATIVE),
				!((DISABLE_PATTERN_FILL & optionalStyle) == DISABLE_PATTERN_FILL));
		this.bTransparencySliderEnable = ((ENABLE_TRANSPARENT_SLIDER & optionalStyle) == ENABLE_TRANSPARENT_SLIDER);
		this.bGradientAngleEnabled = !((DISABLE_GRADIENT_ANGLE & optionalStyle) == DISABLE_GRADIENT_ANGLE);
		this.bEmbeddedImageEnabled = !((DISABLE_EMBEDDED_IMAGE & optionalStyle) == DISABLE_EMBEDDED_IMAGE);
		this.bResourceImageEnabled = (ENABLE_RESOURCE_IMAGE & optionalStyle) == ENABLE_RESOURCE_IMAGE;
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param wizardContext
	 * @param fCurrent           If null, create a Fill using adapters from wizard
	 *                           context
	 * @param bEnableGradient
	 * @param bEnableImage
	 * @param bEnableAuto        Indicates whether auto button will be displayed.
	 * @param bEnableTransparent Indicates whether transparent button will be
	 *                           displayed.
	 */
	public FillChooserComposite(Composite parent, int style, ChartWizardContext wizardContext, Fill fCurrent,
			boolean bEnableGradient, boolean bEnableImage, boolean bEnableAuto, boolean bEnableTransparent) {
		super(parent, style);
		this.fCurrent = fCurrent;
		this.bGradientEnabled = bEnableGradient;
		this.bImageEnabled = bEnableImage;
		this.bAutoEnabled = bEnableAuto;
		this.bTransparentEnabled = bEnableTransparent;
		this.wizardContext = wizardContext;
		this.bPatternFillEnabled = bEnableImage;
		init();
		placeComponents();
		initAccessible();
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param wizardContext
	 * @param fCurrent        If null, create a Fill using adapters from wizard
	 *                        context
	 * @param bEnableGradient
	 * @param bEnableImage
	 */
	public FillChooserComposite(Composite parent, int style, ChartWizardContext wizardContext, Fill fCurrent,
			boolean bEnableGradient, boolean bEnableImage) {
		this(parent, style, wizardContext, fCurrent, bEnableGradient, bEnableImage, true);
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param wizardContext
	 * @param fCurrent        If null, create a Fill using adapters from wizard
	 *                        context
	 * @param bEnableGradient
	 * @param bEnableImage
	 */
	public FillChooserComposite(Composite parent, int style, ChartWizardContext wizardContext, Fill fCurrent,
			boolean bEnableGradient, boolean bEnableImage, boolean bEnablePattern) {
		super(parent, style);
		this.fCurrent = fCurrent;
		this.bGradientEnabled = bEnableGradient;
		this.bImageEnabled = bEnableImage;
		this.bPatternFillEnabled = bEnablePattern;
		this.wizardContext = wizardContext;
		init();
		placeComponents();
		initAccessible();
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param wizardContext
	 * @param fCurrent           If null, create a Fill using adapters from wizard
	 *                           context
	 * @param bEnableGradient
	 * @param bEnableImage
	 * @param bEnableAuto        Indicates whether auto button will be displayed.
	 * @param bEnableTransparent Indicates whether transparent button will be
	 *                           displayed.
	 * @param bPositiveNegative  Indicates whether positive/negative button will be
	 *                           displayed.
	 */
	public FillChooserComposite(Composite parent, int style, ChartWizardContext wizardContext, Fill fCurrent,
			boolean bEnableGradient, boolean bEnableImage, boolean bEnableAuto, boolean bEnableTransparent,
			boolean bPositiveNegative, boolean bEnablePattern) {
		super(parent, style);
		this.fCurrent = fCurrent;
		this.bGradientEnabled = bEnableGradient;
		this.bImageEnabled = bEnableImage;
		this.bAutoEnabled = bEnableAuto;
		this.bTransparentEnabled = bEnableTransparent;
		this.bPositiveNegativeEnabled = bPositiveNegative;
		this.bPatternFillEnabled = bEnablePattern;
		this.wizardContext = wizardContext;
		init();
		placeComponents();
		initAccessible();
	}

	/**
	 * 
	 */
	private void init() {
		if (Display.getCurrent().getHighContrast()) {
			GC gc = new GC(this);
			iSize = gc.getFontMetrics().getHeight() + 2;
		}
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
		Display display = Display.getDefault();
		colorArray = this.createColorMap(display);
		vListeners = new Vector<Listener>();
	}

	/**
	 * 
	 */
	private void placeComponents() {
		// THE LAYOUT OF THIS COMPOSITE (FILLS EVERYTHING INSIDE IT)
		FillLayout flMain = new FillLayout();
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;
		setLayout(flMain);

		// THE LAYOUT OF THE OUTER COMPOSITE (THAT GROWS VERTICALLY BUT ANCHORS
		// ITS CONTENT NORTH)
		cmpContentOuter = new Composite(this, SWT.NONE);
		GridLayout glContentOuter = new GridLayout();
		glContentOuter.verticalSpacing = 0;
		glContentOuter.horizontalSpacing = 0;
		glContentOuter.marginHeight = 0;
		glContentOuter.marginWidth = 0;
		glContentOuter.numColumns = 1;
		cmpContentOuter.setLayout(glContentOuter);

		// THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
		// THE CANVAS + BUTTON)
		cmpContentInner = new Composite(cmpContentOuter, SWT.BORDER);
		GridLayout glContentInner = new GridLayout();
		glContentInner.verticalSpacing = 0;
		glContentInner.horizontalSpacing = 0;
		glContentInner.marginHeight = 0;
		glContentInner.marginWidth = 0;
		glContentInner.numColumns = 2;
		cmpContentInner.setLayout(glContentInner);
		GridData gdContentInner = new GridData(GridData.FILL_HORIZONTAL);
		cmpContentInner.setLayoutData(gdContentInner);

		// THE CANVAS
		cnvSelection = new FillCanvas(cmpContentInner, SWT.NONE, this.bAutoEnabled,
				wizardContext == null ? null : wizardContext.getImageServiceProvider());
		cnvSelection.setTextIndent(8);
		GridData gdCNVSelection = new GridData(GridData.FILL_BOTH);
		gdCNVSelection.heightHint = iSize;
		cnvSelection.setLayoutData(gdCNVSelection);

		initFill();

		// THE BUTTON
		btnDown = new Button(cmpContentInner, SWT.ARROW | SWT.DOWN);
		GridData gdBDown = new GridData(GridData.FILL);
		gdBDown.verticalAlignment = GridData.BEGINNING;
		gdBDown.widthHint = iSize - 2;
		gdBDown.heightHint = iSize;
		btnDown.setLayoutData(gdBDown);
		btnDown.addSelectionListener(this);

		addDisposeListener(this);

		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				handleEventCanvas(event);
			}
		};

		int[] textEvents = { SWT.KeyDown, SWT.MouseDown, SWT.Traverse, SWT.FocusIn, SWT.FocusOut };
		for (int i = 0; i < textEvents.length; i++) {
			cnvSelection.addListener(textEvents[i], listener);
		}

	}

	private void initFill() {
		if ((!this.bPatternFillEnabled && fCurrent instanceof PatternImage)) {
			cnvSelection.setFill(null);
		} else {
			cnvSelection.setFill(fCurrent);
		}
	}

	void handleEventCanvas(Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			cnvSelection.redraw();
			break;
		}
		case SWT.FocusOut: {
			cnvSelection.redraw();
			break;
		}
		case SWT.KeyDown: {
			if (event.keyCode == SWT.KEYPAD_CR || event.keyCode == SWT.CR || event.keyCode == ' ') {
				event.doit = true;
				toggleDropDown();
			}
			break;
		}
		case SWT.MouseDown:
			if (!bEnabled) {
				return;
			}
			// fireHandleEvent( MOUSE_CLICKED_EVENT );
			toggleDropDown();
			break;
		case SWT.Traverse: {
			switch (event.detail) {
			case SWT.TRAVERSE_ESCAPE:
				getShell().close();
				break;
			case SWT.TRAVERSE_RETURN:
			case SWT.TRAVERSE_TAB_NEXT:
			case SWT.TRAVERSE_TAB_PREVIOUS:
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				event.doit = true;
				cnvSelection.redraw();
			}
			break;
		}
		}
	}

	private Color[] createColorMap(Display display) {
		return new Color[] { new Color(display, 0, 0, 0), new Color(display, 154, 50, 0), new Color(display, 51, 51, 0),
				new Color(display, 0, 50, 0), new Color(display, 0, 50, 100), new Color(display, 0, 0, 128),
				new Color(display, 51, 51, 153), new Color(display, 51, 51, 51),

				new Color(display, 128, 0, 0), new Color(display, 255, 102, 0), new Color(display, 124, 124, 0),
				new Color(display, 0, 128, 0), new Color(display, 0, 128, 128), new Color(display, 0, 0, 255),
				new Color(display, 102, 102, 153), new Color(display, 128, 128, 128),

				new Color(display, 255, 0, 0), new Color(display, 255, 153, 0), new Color(display, 154, 204, 0),
				new Color(display, 51, 153, 102), new Color(display, 51, 204, 204), new Color(display, 51, 102, 255),
				new Color(display, 128, 0, 128), new Color(display, 145, 145, 145),

				new Color(display, 255, 0, 255), new Color(display, 255, 204, 0), new Color(display, 255, 255, 0),
				new Color(display, 0, 255, 0), new Color(display, 0, 255, 255), new Color(display, 0, 204, 255),
				new Color(display, 154, 50, 102), new Color(display, 192, 192, 192),

				new Color(display, 253, 153, 204), new Color(display, 255, 204, 153), new Color(display, 255, 255, 153),
				new Color(display, 204, 255, 204), new Color(display, 204, 255, 255), new Color(display, 153, 204, 255),
				new Color(display, 204, 153, 255), new Color(display, 255, 255, 255) };
	}

	/**
	 * 
	 */
	private void createDropDownComponent(int iXLoc, int iYLoc) {
		if (!bEnabled) {
			return;
		}
		int iShellHeight = 170;
		int iShellWidth = 190;
		// Reduce the height based on which buttons are to be shown.
		if (bGradientEnabled) {
			iShellHeight += 30;
		}
		if (bImageEnabled) {
			iShellHeight += 30;
		}
		if (bAutoEnabled) {
			iShellHeight += 30;
		}
		if (bTransparentEnabled) {
			iShellHeight += 30;
		}
		if (bPositiveNegativeEnabled) {
			iShellHeight += 30;
		}

		if (bPatternFillEnabled) {
			iShellHeight += 30;
		}

		Shell shell = new Shell(this.getShell(), SWT.NO_FOCUS);
		shell.setLayout(new FillLayout());
		shell.setSize(iShellWidth, iShellHeight);

		if ((getStyle() & SWT.RIGHT_TO_LEFT) != 0) {
			iXLoc -= iShellWidth;
		}
		shell.setLocation(iXLoc, iYLoc);
		shell.addShellListener(new ShellAdapter() {

			@Override
			public void shellClosed(ShellEvent e) {
				clearColorSelection();
			}

		});

		cmpDropDown = new Composite(shell, SWT.NO_FOCUS);
		GridLayout glDropDown = new GridLayout();
		glDropDown.marginHeight = 2;
		glDropDown.marginWidth = 2;
		glDropDown.horizontalSpacing = 1;
		glDropDown.verticalSpacing = 4;
		cmpDropDown.setLayout(glDropDown);

		if (colorArray == null) {
			colorArray = createColorMap(getDisplay());
		}
		ColorSelectionCanvas cnv = new ColorSelectionCanvas(cmpDropDown, SWT.BORDER, colorArray);
		GridData gdCnv = new GridData(GridData.FILL_BOTH);
		gdCnv.widthHint = 190;
		gdCnv.heightHint = 110;
		cnv.setLayoutData(gdCnv);
		cnv.addListener(SWT.Traverse, this);
		cnv.addListener(SWT.FocusOut, this);

		if (this.fCurrent instanceof ColorDefinition) {
			cnv.setColor(new Color(this.getDisplay(), ((ColorDefinition) fCurrent).getRed(),
					((ColorDefinition) fCurrent).getGreen(), ((ColorDefinition) fCurrent).getBlue()));
		}

		cmpButtons = new Composite(cmpDropDown, SWT.NO_FOCUS);
		GridLayout glButtons = new GridLayout();
		glButtons.marginHeight = 3;
		glButtons.marginWidth = 4;
		glButtons.horizontalSpacing = 1;
		glButtons.verticalSpacing = 4;
		glButtons.numColumns = 2;
		cmpButtons.setLayout(glButtons);
		GridData gdButtons = new GridData(GridData.FILL_HORIZONTAL);
		cmpButtons.setLayoutData(gdButtons);

		// Layout for Transparency Composite
		GridLayout glTransparency = new GridLayout();
		glTransparency.numColumns = 1;
		glTransparency.horizontalSpacing = 5;
		glTransparency.verticalSpacing = 3;
		glTransparency.marginHeight = 4;
		glTransparency.marginWidth = 0;

		Composite cmpTransparency = new Composite(cmpButtons, SWT.NONE | SWT.NO_FOCUS);
		GridData gdTransparency = new GridData(GridData.FILL_BOTH);
		gdTransparency.horizontalSpan = 2;
		cmpTransparency.setLayoutData(gdTransparency);
		cmpTransparency.setLayout(glTransparency);

		if (bTransparencySliderEnable) {
			lblTransparency = new Label(cmpTransparency, SWT.NONE);
			GridData gdLBLTransparency = new GridData(GridData.FILL_HORIZONTAL);
			gdLBLTransparency.horizontalIndent = 2;
			lblTransparency.setLayoutData(gdLBLTransparency);
			lblTransparency.setText(Messages.getString("FillChooserComposite.Lbl.Opacity")); //$NON-NLS-1$

			srTransparency = new Slider(cmpTransparency, SWT.HORIZONTAL | SWT.NO_FOCUS);
			GridData gdTransparent = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
			gdTransparent.horizontalSpan = 2;
			srTransparency.setLayoutData(gdTransparent);
			if (fCurrent == null) {
				srTransparency.setValues(0, 0, 256, 1, 1, 10);
				srTransparency.setEnabled(false);
			} else {
				int iValue = 0;
				if (fCurrent instanceof ColorDefinition) {
					iValue = ((ColorDefinition) fCurrent).getTransparency();
					srTransparency.setValues(iValue, 0, 256, 1, 1, 10);
				} else if (fCurrent instanceof Gradient) {
					iValue = ((Gradient) fCurrent).getTransparency();
					srTransparency.setValues(iValue, 0, 256, 1, 1, 10);
				} else {
					srTransparency.setEnabled(false);
				}
			}
			lblTransparency.setText(new MessageFormat(Messages.getString("FillChooserComposite.Lbl.Opacity")) //$NON-NLS-1$
					.format(new Object[] { Integer.valueOf(srTransparency.getSelection()) }));
			srTransparency.setToolTipText(String.valueOf(srTransparency.getSelection()));
			srTransparency.addSelectionListener(this);
			srTransparency.addListener(SWT.FocusOut, this);
			srTransparency.addListener(SWT.KeyDown, this);
			srTransparency.addListener(SWT.Traverse, this);
		}

		final int BUTTON_HEIGHTHINT = 28;
		if (this.bTransparentEnabled) {
			btnReset = new Button(cmpButtons, SWT.NONE);
			GridData gdReset = new GridData(GridData.FILL_BOTH);
			gdReset.heightHint = BUTTON_HEIGHTHINT;
			gdReset.horizontalSpan = 2;
			btnReset.setLayoutData(gdReset);
			btnReset.setText(Messages.getString("FillChooserComposite.Lbl.Transparent")); //$NON-NLS-1$
			btnReset.addSelectionListener(this);
			btnReset.addListener(SWT.FocusOut, this);
			btnReset.addListener(SWT.KeyDown, this);
			btnReset.addListener(SWT.Traverse, this);
		}

		if (this.bAutoEnabled) {
			btnAuto = new Button(cmpButtons, SWT.NONE);
			GridData gdGradient = new GridData(GridData.FILL_BOTH);
			gdGradient.heightHint = BUTTON_HEIGHTHINT;
			gdGradient.horizontalSpan = 2;
			btnAuto.setLayoutData(gdGradient);
			btnAuto.setText(Messages.getString("FillChooserComposite.Lbl.Auto")); //$NON-NLS-1$
			btnAuto.addSelectionListener(this);
			btnAuto.addListener(SWT.FocusOut, this);
			btnAuto.addListener(SWT.KeyDown, this);
			btnAuto.addListener(SWT.Traverse, this);
		}

		if (this.bGradientEnabled) {
			btnGradient = new Button(cmpButtons, SWT.NONE);
			GridData gdGradient = new GridData(GridData.FILL_BOTH);
			gdGradient.heightHint = BUTTON_HEIGHTHINT;
			gdGradient.horizontalSpan = 2;
			btnGradient.setLayoutData(gdGradient);
			btnGradient.setText(Messages.getString("FillChooserComposite.Lbl.Gradient")); //$NON-NLS-1$
			btnGradient.addSelectionListener(this);
			btnGradient.addListener(SWT.FocusOut, this);
			btnGradient.addListener(SWT.KeyDown, this);
			btnGradient.addListener(SWT.Traverse, this);
		}

		btnCustom = new Button(cmpButtons, SWT.NONE);
		GridData gdCustom = new GridData(GridData.FILL_BOTH);
		gdCustom.heightHint = BUTTON_HEIGHTHINT;
		gdCustom.horizontalSpan = 2;
		btnCustom.setLayoutData(gdCustom);
		btnCustom.setText(Messages.getString("FillChooserComposite.Lbl.CustomColor")); //$NON-NLS-1$
		btnCustom.addSelectionListener(this);
		btnCustom.addListener(SWT.FocusOut, this);
		btnCustom.addListener(SWT.KeyDown, this);
		btnCustom.addListener(SWT.Traverse, this);

		if (this.bImageEnabled) {
			btnImage = new Button(cmpButtons, SWT.NONE);
			GridData gdImage = new GridData(GridData.FILL_BOTH);
			gdImage.heightHint = BUTTON_HEIGHTHINT;
			gdImage.horizontalSpan = 2;
			btnImage.setLayoutData(gdImage);
			btnImage.setText(Messages.getString("FillChooserComposite.Lbl.Image")); //$NON-NLS-1$
			btnImage.addSelectionListener(this);
			btnImage.addListener(SWT.FocusOut, this);
			btnImage.addListener(SWT.KeyDown, this);
			btnImage.addListener(SWT.Traverse, this);
		}

		if (this.bPositiveNegativeEnabled) {
			btnPN = new Button(cmpButtons, SWT.NONE);
			GridData gdPN = new GridData(GridData.FILL_BOTH);
			gdPN.heightHint = BUTTON_HEIGHTHINT;
			gdPN.horizontalSpan = 2;
			btnPN.setLayoutData(gdPN);
			btnPN.setText(Messages.getString("FillChooserComposite.Lbl.PositiveNegative")); //$NON-NLS-1$
			btnPN.addSelectionListener(this);
			btnPN.addListener(SWT.FocusOut, this);
			btnPN.addListener(SWT.KeyDown, this);
			btnPN.addListener(SWT.Traverse, this);
		}

		if (bPatternFillEnabled) {
			btnPatternFill = new Button(cmpButtons, SWT.NONE);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.heightHint = BUTTON_HEIGHTHINT;
			gd.horizontalSpan = 2;
			btnPatternFill.setLayoutData(gd);
			btnPatternFill.setText(Messages.getString("FillChooserComposite.Button.Pattern")); //$NON-NLS-1$
			btnPatternFill.addSelectionListener(this);
			btnPatternFill.addListener(SWT.FocusOut, this);
			btnPatternFill.addListener(SWT.KeyDown, this);
			btnPatternFill.addListener(SWT.Traverse, this);
		}

		shell.pack();
		shell.layout();
		shell.open();
	}

	public void setFill(Fill fill) {
		fCurrent = fill;
		cnvSelection.setFill(fill);
		cnvSelection.redraw();
	}

	public Fill getFill() {
		return this.fCurrent;
	}

	@Override
	public void setEnabled(boolean bState) {
		btnDown.setEnabled(bState);
		cnvSelection.setEnabled(bState);
		cnvSelection.redraw();
		this.bEnabled = bState;
	}

	@Override
	public boolean isEnabled() {
		return this.bEnabled;
	}

	public Point getPreferredSize() {
		return new Point(160, 24);
	}

	public void addListener(Listener listener) {
		vListeners.add(listener);
	}

	private void toggleDropDown() {
		// fix for Linux, since it not send the event correctly to other than
		// current shell.
		if (bJustFocusLost) {
			bJustFocusLost = false;
			return;
		}

		if (cmpDropDown == null || cmpDropDown.isDisposed() || !cmpDropDown.isVisible()) {
			Point pLoc = UIHelper.getScreenLocation(cnvSelection);
			createDropDownComponent(pLoc.x, pLoc.y + cnvSelection.getSize().y + 1);

			cmpDropDown.setFocus();
		} else {
			cmpDropDown.getShell().close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		Object oSource = e.getSource();
		if (oSource.equals(btnDown)) {
			fireHandleEvent(MOUSE_CLICKED_EVENT);
			toggleDropDown();
		} else if (oSource.equals(this.btnImage)) {

			ImageDialog idlg = (ImageDialog) wizardContext.getUIFactory().createChartImageDialog(this.getShell(),
					fCurrent, wizardContext, bEmbeddedImageEnabled, bResourceImageEnabled);

			cmpDropDown.getShell().close();
			if (idlg.open() == Window.OK) {
				Fill imgFill = idlg.getResult();

				if (imgFill != null) {
					addAdapters(imgFill);
					this.setFill(imgFill);
					fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
				}
			}
		} else if (oSource.equals(this.btnPN)) {
			PositiveNegativeColorDialog pncd = null;
			cmpDropDown.getShell().close();

			if (fCurrent instanceof MultipleFill) {
				pncd = new PositiveNegativeColorDialog(this.getShell(), wizardContext, (MultipleFill) fCurrent);
			} else if (fCurrent instanceof ColorDefinition) {
				ColorDefinition newCD = ((ColorDefinition) fCurrent).copyInstance();
				newCD.eAdapters().addAll(fCurrent.eAdapters());
				pncd = new PositiveNegativeColorDialog(this.getShell(), wizardContext, null, newCD);
			} else {
				pncd = new PositiveNegativeColorDialog(this.getShell(), wizardContext, null);
			}
			if (pncd.open() == Window.OK) {
				Fill fTmp = pncd.getMultipleColor();
				addAdapters(fTmp);
				if (fCurrent == null || !(fCurrent.equals(fTmp))) {
					this.setFill(fTmp);
					fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
				}
			}
		} else if (oSource == btnPatternFill) {
			PatternImageEditorDialog dialog = new PatternImageEditorDialog(getShell(), fCurrent);
			cmpDropDown.getShell().close();
			if (dialog.open() == Window.OK) {
				Fill fTmp = dialog.getPatternImage();
				addAdapters(fTmp);
				this.setFill(dialog.getPatternImage());
				fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
			}
		} else if (oSource.equals(this.btnReset)) {
			this.setFill(ColorDefinitionImpl.TRANSPARENT());
			fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
			cmpDropDown.getShell().close();
		} else if (oSource.equals(this.btnAuto)) {
			setFill(null);
			fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
			cmpDropDown.getShell().close();
		} else if (oSource.equals(this.btnCustom)) {
			ColorDialog cDlg = new ColorDialog(this.getShell(), SWT.APPLICATION_MODAL);
			cmpDropDown.getShell().close();
			int iTrans = 255;
			if (fCurrent instanceof ColorDefinition) {
				if (!fCurrent.equals(ColorDefinitionImpl.TRANSPARENT())) {
					iTransparency = ((ColorDefinition) fCurrent).getTransparency();
				}
				cDlg.setRGB(new RGB(((ColorDefinition) this.fCurrent).getRed(),
						((ColorDefinition) this.fCurrent).getGreen(), ((ColorDefinition) this.fCurrent).getBlue()));
			}
			RGB rgb = cDlg.open();
			if (rgb != null) {
				ColorDefinition cdNew = AttributeFactory.eINSTANCE.createColorDefinition();
				cdNew.set(rgb.red, rgb.green, rgb.blue);
				cdNew.setTransparency(bTransparencyChanged ? this.iTransparency : iTrans);
				addAdapters(cdNew);
				this.setFill(cdNew);
				fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
			}
		} else if (oSource.equals(this.btnGradient)) {
			GradientEditorDialog ged = null;
			cmpDropDown.getShell().close();

			if (fCurrent instanceof Gradient) {
				ged = new GradientEditorDialog(this.getShell(), wizardContext, (Gradient) fCurrent,
						bGradientAngleEnabled);
			} else if (fCurrent instanceof ColorDefinition) {
				ColorDefinition newCD = (ColorDefinition) fCurrent.copyInstance();
				newCD.eAdapters().addAll(fCurrent.eAdapters());
				ged = new GradientEditorDialog(this.getShell(), wizardContext, newCD, bGradientAngleEnabled);
			} else {
				ged = new GradientEditorDialog(this.getShell(), wizardContext, ColorDefinitionImpl.create(0, 0, 254),
						bGradientAngleEnabled);
			}
			if (ged.open() == Window.OK) {
				Fill fTmp = ged.getGradient();
				addAdapters(fTmp);
				if (fCurrent == null || !(fCurrent.equals(fTmp))) {
					this.setFill(fTmp);
					fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
				}
			}
		} else if (oSource.equals(srTransparency)) {
			iTransparency = srTransparency.getSelection();
			lblTransparency.setText(new MessageFormat(Messages.getString("FillChooserComposite.Lbl.Opacity")) //$NON-NLS-1$
					.format(new Object[] { Integer.valueOf(srTransparency.getSelection()) }));
			srTransparency.setToolTipText(String.valueOf(srTransparency.getSelection()));

			if (fCurrent instanceof ColorDefinition) {
				((ColorDefinition) fCurrent).setTransparency(srTransparency.getSelection());
			}
			setFill(fCurrent);

			bTransparencyChanged = true;
			fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private void fireHandleEvent(int iType) {
		for (int iL = 0; iL < vListeners.size(); iL++) {
			Event se = new Event();
			se.widget = this;
			se.data = fCurrent;
			se.type = iType;
			vListeners.get(iL).handleEvent(se);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt
	 * .events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent e) {
		if (colorArray != null) {
			for (int iC = 0; iC < colorArray.length; iC++) {
				colorArray[iC].dispose();
			}
			colorArray = null;
		}
	}

	private boolean isPopupControl(Object control) {
		return control != null && control instanceof Control
				&& ((Control) control).getShell() == cmpDropDown.getShell();
	}

	private void addAdapters(Notifier notifier) {
		if (wizardContext != null) {
			notifier.eAdapters().addAll(wizardContext.getModel().eAdapters());
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

		ChartUIUtil.addScreenReaderAccessibility(this, cnvSelection);
	}

	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.FocusOut:
			if (event.widget instanceof ColorSelectionCanvas) {
				((ColorSelectionCanvas) event.widget).redraw();
			}
			if (isPopupControl(event.widget)) {
				Control cTmp = isPressingKey ? getDisplay().getFocusControl() : getDisplay().getCursorControl();
				// Set default value back
				isPressingKey = false;
				if (cTmp != null) {
					// Condition added to handle behavior under Linux
					if (isPopupControl(cTmp) || SWT.getPlatform().indexOf("win32") == 0//$NON-NLS-1$
							&& (cTmp.equals(cnvSelection) || cTmp.equals(btnDown))) {
						return;
					}

					if (cTmp.equals(cnvSelection) || cTmp.equals(btnDown)) {
						bJustFocusLost = true;
					}
				}

				cmpDropDown.getShell().close();
			}
			break;

		case SWT.KeyDown:
			if (cmpDropDown != null && !cmpDropDown.getShell().isDisposed()) {
				if (event.keyCode == SWT.ARROW_UP) {
					cmpDropDown.getShell().close();
				} else if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
					if (srTransparency != null) {
						this.iTransparency = srTransparency.getSelection();
					}
					if (fCurrent instanceof ColorDefinition && bTransparencyChanged) {
						((ColorDefinition) fCurrent).setTransparency(this.iTransparency);
					}
					this.setFill(fCurrent);
					cmpDropDown.getShell().close();
				}
			}
			break;

		case SWT.Traverse:
			switch (event.detail) {
			case SWT.TRAVERSE_TAB_NEXT:
			case SWT.TRAVERSE_TAB_PREVIOUS:
				// Indicates getting focus control rather than cursor
				// control
				isPressingKey = true;
				event.doit = true;
			}
			break;
		}

	}

	private void setColorToModel(Color clrTmp) {
		ColorDefinition cTmp = AttributeFactory.eINSTANCE.createColorDefinition();
		cTmp.set(clrTmp.getRed(), clrTmp.getGreen(), clrTmp.getBlue());
		int iTransparency = 255;
		if (fCurrent instanceof ColorDefinition && this.iTransparency != 0) {
			iTransparency = (bTransparencyChanged) ? this.iTransparency
					: ((ColorDefinition) fCurrent).getTransparency();
		}
		cTmp.setTransparency(iTransparency);
		addAdapters(cTmp);
		setFill(cTmp);
		fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
	}

	private void clearColorSelection() {
		selectedIndex = -1;
	}

	/**
	 * Sets text indent of fill canvas.
	 * 
	 * @param indent
	 */
	public void setTextIndent(int indent) {
		if (this.cnvSelection != null) {
			this.cnvSelection.setTextIndent(indent);
		}
	}

	private class ColorSelectionCanvas extends Canvas implements Listener {

		static final int ROW_SIZE = 8;
		static final int COLUMN_SIZE = 5;

		final Color[] colorMap;

		Color colorSelection = null;

		public ColorSelectionCanvas(Composite parent, int iStyle, final Color[] colorMap) {
			super(parent, iStyle);
			this.colorMap = colorMap;
			this.addListener(SWT.Paint, this);
			this.addListener(SWT.KeyDown, this);
			this.addListener(SWT.MouseDown, this);
			this.addListener(SWT.FocusIn, this);
		}

		// public Color getColor( )
		// {
		// return colorSelection;
		// }

		public void setColor(Color color) {
			this.colorSelection = color;
		}

		void paintControl(PaintEvent pe) {
			Color cBlack = new Color(this.getDisplay(), 0, 0, 0);
			Color cWhite = new Color(this.getDisplay(), 255, 255, 255);
			GC gc = pe.gc;
			gc.setForeground(cBlack);

			int iCellWidth = this.getSize().x / ROW_SIZE;
			int iCellHeight = this.getSize().y / COLUMN_SIZE;
			boolean isFound = false;
			for (int iR = 0; iR < COLUMN_SIZE; iR++) {
				for (int iC = 0; iC < ROW_SIZE; iC++) {
					int index = iR * ROW_SIZE + iC;
					try {
						gc.setBackground(colorMap[index]);
					} catch (Throwable e) {
						e.printStackTrace();
					}
					gc.fillRectangle(iC * iCellWidth, iR * iCellHeight, iCellWidth, iCellHeight);
					// Highlight currently selected color if it exists in this
					// list
					if (selectedIndex == index
							|| !isFound && colorSelection != null && colorSelection.equals(colorMap[index])) {
						isFound = true;
						selectedIndex = index;
						if (colorSelection == null) {
							colorSelection = colorMap[index];
						}

						if (isFocusControl()) {
							gc.setLineStyle(SWT.LINE_DOT);
						}
						gc.drawRectangle(iC * iCellWidth, iR * iCellHeight, iCellWidth - 2, iCellHeight - 2);
						gc.setForeground(cWhite);
						gc.drawRectangle(iC * iCellWidth + 1, iR * iCellHeight + 1, iCellWidth - 3, iCellHeight - 3);
						gc.setForeground(cBlack);
					}
				}
			}
			if (!isFound) {
				clearColorSelection();
			}
			cBlack.dispose();
			cWhite.dispose();
			gc.dispose();
		}

		/**
		 * This method assumes a color array of 40 color arranged with equal sizes in a
		 * 8x5 grid.
		 * 
		 * @param x
		 * @param y
		 */
		public Color getColorAt(int x, int y) {
			int iCellWidth = this.getSize().x / 8;
			int iCellHeight = this.getSize().y / 5;
			int iHCell = x / iCellWidth;
			int iVCell = y / iCellHeight;
			int iArrayIndex = ((iVCell) * 8) + iHCell;
			return this.colorMap[iArrayIndex];
		}

		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.Paint:
				paintControl(new PaintEvent(event));
				break;
			case SWT.FocusIn:
				redraw();
				break;
			case SWT.KeyDown:
				keyDown(event);
				break;
			case SWT.MouseDown:
				if (!bEnabled) {
					return;
				}
				fireHandleEvent(MOUSE_CLICKED_EVENT);
				setColorToModel(this.getColorAt(event.x, event.y));
				cmpDropDown.getShell().close();
				break;
			}

		}

		void keyDown(Event event) {
			if (event.keyCode == SWT.ESC) {
				cmpDropDown.getShell().close();
				return;
			}
			if (selectedIndex == -1) {
				if (event.keyCode == SWT.ARROW_LEFT || event.keyCode == SWT.ARROW_RIGHT || event.keyCode == SWT.ARROW_UP
						|| event.keyCode == SWT.ARROW_DOWN) {
					selectedIndex = 0;
				}
			} else {
				switch (event.keyCode) {
				case SWT.ARROW_LEFT:
					if (selectedIndex - 1 >= 0) {
						selectedIndex -= 1;
					}
					break;
				case SWT.ARROW_RIGHT:
					if (selectedIndex + 1 < ROW_SIZE * COLUMN_SIZE) {
						selectedIndex += 1;
					}
					break;
				case SWT.ARROW_UP:
					if (selectedIndex - ROW_SIZE >= 0) {
						selectedIndex -= ROW_SIZE;
					}
					break;
				case SWT.ARROW_DOWN:
					if (selectedIndex + ROW_SIZE < ROW_SIZE * COLUMN_SIZE) {
						selectedIndex += ROW_SIZE;
					}
					break;
				case SWT.CR:
				case SWT.KEYPAD_CR:
					setColorToModel(colorMap[selectedIndex]);
					cmpDropDown.getShell().close();
					break;
				}
			}
			if (!cmpDropDown.isDisposed()) {
				colorSelection = null;
				redraw();
			}
		}
	}

	public void addScreenReaderAccessibility(String description) {
		ChartUIUtil.addScreenReaderAccessbility(cnvSelection, description);
	}
}
