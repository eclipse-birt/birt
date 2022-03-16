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

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IFontDefinitionDialog;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 *
 */
public class FontDefinitionComposite extends Composite {

	private static final String TOOLTIP = Messages.getString("FontDefinitionComposite.Tooltip.FontDialog"); //$NON-NLS-1$

	private Composite cmpContent = null;

	private FontCanvas cnvSelection = null;

	private Button btnFont = null;

	private FontDefinition fdCurrent = null;

	private ColorDefinition cdCurrent = null;

	private Vector<Listener> vListeners = null;

	public static final int FONT_CHANTED_EVENT = 1;

	public static final int FONT_DATA = 0;

	public static final int COLOR_DATA = 1;

	public static final int ENABLE_ALIGNMENT = 1;

	public static final int ENABLE_ROTATION = 1 << 1;

	public static final int DEFAULT_NONE = 0;

	private int iSize = 18;

	private int optionalStyle = DEFAULT_NONE;

	private boolean bEnabled = true;

	private ChartWizardContext wizardContext;

	public FontDefinitionComposite(Composite parent, int style, ChartWizardContext wizardContext,
			FontDefinition fdSelected, ColorDefinition cdSelected, int optionalStyle) {
		super(parent, style);
		this.wizardContext = wizardContext;
		this.fdCurrent = fdSelected;
		this.cdCurrent = cdSelected;
		this.optionalStyle = optionalStyle;
		init();
		placeComponents();
		initAccessible();
	}

	public FontDefinitionComposite(Composite parent, int style, ChartWizardContext wizardContext,
			FontDefinition fdSelected, ColorDefinition cdSelected, boolean isAlignmentEnabled) {
		super(parent, style);
		this.wizardContext = wizardContext;
		this.fdCurrent = fdSelected;
		this.cdCurrent = cdSelected;
		if (isAlignmentEnabled) {
			this.optionalStyle |= ENABLE_ALIGNMENT;
		}
		// this Rotation is enabled to compatible with old behavior of this
		// constructor.
		this.optionalStyle |= ENABLE_ROTATION;

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
		glContent.verticalSpacing = 0;
		glContent.horizontalSpacing = 2;
		glContent.marginHeight = 0;
		glContent.marginWidth = 0;
		glContent.numColumns = 2;

		this.setLayout(flMain);

		cmpContent = new Composite(this, SWT.NONE);
		cmpContent.setLayout(glContent);

		cnvSelection = new FontCanvas(cmpContent, SWT.BORDER, fdCurrent, cdCurrent, false, true, false);
		GridData gdCNVSelection = new GridData(GridData.FILL_HORIZONTAL);
		gdCNVSelection.heightHint = iSize;
		cnvSelection.setLayoutData(gdCNVSelection);
		cnvSelection.setToolTipText(TOOLTIP);
		cnvSelection.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				openFontDialog();
			}

		});
		cnvSelection.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				cnvSelection.traverse(SWT.TRAVERSE_TAB_NEXT);
			}
		});

		btnFont = new Button(cmpContent, SWT.NONE);
		GridData gdBEllipsis = new GridData();
		ChartUIUtil.setChartImageButtonSizeByPlatform(gdBEllipsis);
		btnFont.setLayoutData(gdBEllipsis);
		btnFont.setText("A"); //$NON-NLS-1$
		btnFont.setFont(new Font(Display.getCurrent(), "Times New Roman", 14, SWT.BOLD)); //$NON-NLS-1$
		// btnFont.setImage( UIHelper.getImage( "icons/obj16/fonteditor.gif" ) );
		// //$NON-NLS-1$
		btnFont.setToolTipText(TOOLTIP);
		btnFont.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				openFontDialog();
			}

		});
	}

	@Override
	public void setEnabled(boolean bState) {
		this.btnFont.setEnabled(bState);
		this.cnvSelection.setEnabled(bState);
		cnvSelection.redraw();
		this.bEnabled = bState;
	}

	@Override
	public boolean isEnabled() {
		return this.bEnabled;
	}

	public FontDefinition getFontDefinition() {
		return this.fdCurrent;
	}

	public ColorDefinition getFontColor() {
		return this.cdCurrent;
	}

	public void setFontDefinition(FontDefinition fd) {
		this.fdCurrent = fd;
		cnvSelection.setFontDefinition(fdCurrent);
		cnvSelection.redraw();
	}

	public void setFontColor(ColorDefinition cd) {
		this.cdCurrent = cd;
		cnvSelection.setColor(cdCurrent);
		cnvSelection.redraw();
	}

	public void addListener(Listener listener) {
		vListeners.add(listener);
	}

	void openFontDialog() {
		// Launch the font selection dialog
		IFontDefinitionDialog fontDlg = openFontDefinitionDialog(this.getShell(), fdCurrent, cdCurrent);
		if (fontDlg.open() == Window.OK) {
			fdCurrent = fontDlg.getFontDefinition();
			cdCurrent = fontDlg.getFontColor();
			cnvSelection.setFontDefinition(fdCurrent);
			cnvSelection.setColor(cdCurrent);
			cnvSelection.redraw();
			fireEvent();
		}
	}

	protected IFontDefinitionDialog openFontDefinitionDialog(Shell shellParent, FontDefinition fdCurrent,
			ColorDefinition cdCurrent) {
		return wizardContext.getUIFactory().createFontDefinitionDialog(shellParent, wizardContext, fdCurrent, cdCurrent,
				optionalStyle);
	}

	private void fireEvent() {
		for (int iL = 0; iL < vListeners.size(); iL++) {
			Event se = new Event();
			se.widget = this;
			Object[] data = { fdCurrent, cdCurrent };
			se.data = data;
			se.type = FONT_CHANTED_EVENT;
			vListeners.get(iL).handleEvent(se);
		}
	}

	public Point getPreferredSize() {
		Point bP = btnFont.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		return new Point(cnvSelection.getPreferredWidth() + bP.x + 2, bP.y);
	}

	void initAccessible() {
		getAccessible().addAccessibleListener(new AccessibleAdapter() {

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(new Point(e.x, e.y));
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(new Point(location.x, location.y));
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			@Override
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}
		});
	}
}
