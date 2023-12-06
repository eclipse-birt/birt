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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IFontDefinitionDialog;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 *
 */
public class FontDefinitionComposite extends Composite {

	private static final String TOOLTIP = Messages.getString("FontDefinitionComposite.Tooltip.FontDialog"); //$NON-NLS-1$

	private FontCanvas cnvSelection = null;

	private Button btnFont = null;

	private ResourceManager resourceManager;

	private FontDefinition fdCurrent = null;

	private ColorDefinition cdCurrent = null;

	private List<Listener> vListeners = null;

	public static final int FONT_CHANTED_EVENT = 1;

	public static final int FONT_DATA = 0;

	public static final int COLOR_DATA = 1;

	public static final int ENABLE_ALIGNMENT = 1;

	public static final int ENABLE_ROTATION = 1 << 1;

	public static final int DEFAULT_NONE = 0;

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
		this.resourceManager = new LocalResourceManager(JFaceResources.getResources(), this);
		if (Display.getCurrent().getHighContrast()) {
//			GC gc = new GC(this);
//			iSize = gc.getFontMetrics().getHeight() + 2;
		}

		this.vListeners = new ArrayList<>();
	}

	/**
	 *
	 */
	private void placeComponents() {

		this.setLayout(new FillChooserInternalLayout());

		cnvSelection = new FontCanvas(this, SWT.BORDER, fdCurrent, cdCurrent, false, true, false);
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

		btnFont = new Button(this, SWT.NONE);
		btnFont.setText("A"); //$NON-NLS-1$

		btnFont.setFont(this.resourceManager.createFont(FontDescriptor.createFrom("Times New Roman", 14, SWT.BOLD))); //$NON-NLS-1$
		btnFont.setToolTipText(TOOLTIP);
		btnFont.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				openFontDialog();
			}

		});
	}

	private class FillChooserInternalLayout extends Layout {
		@Override
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point buttonSize = btnFont.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point selectionSize = cnvSelection.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);

			return new Point(selectionSize.x + buttonSize.x, buttonSize.y);
		}

		@Override
		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point buttonSize = btnFont.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);

			cnvSelection.setBounds(0, 0, bounds.width - buttonSize.x, buttonSize.y - 1);
			btnFont.setBounds(bounds.width - buttonSize.x, 0, buttonSize.x, buttonSize.y);
		}
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
		this.cnvSelection.setColor(cdCurrent);
		this.cnvSelection.redraw();
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
