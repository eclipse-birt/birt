/***********************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.core.ui.swt.custom;

import java.util.Vector;

import org.eclipse.birt.core.ui.utils.UIHelper;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * CustomChooserComposite
 */
public abstract class CustomChooserComposite extends Composite {

	/**
	 * This event occurs when the selection is set.
	 */
	public static final int SELECTION_EVENT = 1;

	/**
	 * This event occures when the dropdown is about to show.
	 */
	public static final int DROPDOWN_EVENT = 2;

	private Composite cmpDropDown = null;

	private Composite cmpContent = null;

	protected ICustomChoice cnvSelection = null;

	private Button btnDown = null;

	private Object iCurrentValue = null;

	private Vector<Listener> vSelectionListeners = new Vector<Listener>();

	private Vector<Listener> vDropDownListeners = new Vector<Listener>();

	private boolean bEnabled = true;

	private boolean bJustFocusLost = false;

	private ICustomChoice popupSelection;

	private ICustomChoice[] popupCanvases;

	private Object[] items;

	protected int itemHeight;

	private Listener canvasListener = new Listener() {

		public void handleEvent(Event event) {
			if (event.widget == cnvSelection) {
				handleEventCanvasSelection(event);
			} else {
				handleEventCanvasPopup(event);
			}
		}
	};

	private AccessibleListener accessibleListener = new AccessibleAdapter() {

		public void getHelp(AccessibleEvent e) {
			e.result = getToolTipText();
		}
	};

	private AccessibleControlListener accessibleControlListener = new AccessibleControlAdapter() {

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
	};

	private ScrolledComposite container;

	public CustomChooserComposite(Composite parent, int style) {
		super(parent, style);
		itemHeight = 18;
	}

	protected CustomChooserComposite(Composite parent, int style, Object choiceValue) {
		this(parent, style);

		iCurrentValue = choiceValue;
	}

	private void initControls() {
		// THE LAYOUT OF THIS COMPOSITE (FILLS EVERYTHING INSIDE IT)
		FillLayout flMain = new FillLayout();
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;
		setLayout(flMain);

		// THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
		// THE CANVAS + BUTTON)
		cmpContent = new Composite(this, SWT.BORDER);
		GridLayout glContentInner = new GridLayout();
		glContentInner.verticalSpacing = 0;
		glContentInner.horizontalSpacing = 0;
		glContentInner.marginHeight = 0;
		glContentInner.marginWidth = 0;
		glContentInner.numColumns = 2;
		cmpContent.setLayout(glContentInner);

		final int iSize = itemHeight;
		// THE CANVAS
		cnvSelection = createChoice(cmpContent, null);
		GridData gdCNVSelection = new GridData(GridData.FILL_BOTH);
		gdCNVSelection.heightHint = iSize;
		cnvSelection.setLayoutData(gdCNVSelection);
		cnvSelection.setValue(iCurrentValue);
		cnvSelection.addListener(SWT.KeyDown, canvasListener);
		cnvSelection.addListener(SWT.Traverse, canvasListener);
		cnvSelection.addListener(SWT.FocusIn, canvasListener);
		cnvSelection.addListener(SWT.FocusOut, canvasListener);
		cnvSelection.addListener(SWT.MouseDown, canvasListener);

		// THE BUTTON
		btnDown = new Button(cmpContent, SWT.ARROW | SWT.DOWN);
		GridData gdBDown = new GridData(GridData.FILL);
		gdBDown.verticalAlignment = GridData.BEGINNING;
		gdBDown.widthHint = iSize - 1;
		gdBDown.heightHint = iSize;
		btnDown.setLayoutData(gdBDown);
		btnDown.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				toggleDropDown();
			}
		});

		layout();

		initAccessible();
	}

	protected abstract ICustomChoice createChoice(Composite parent, Object choiceValue);

	public void setItems(Object[] items) {
		this.items = items == null ? new Object[0] : items;

		if (cmpContent == null) {
			initControls();
		}

		if (findChoiceIndex(iCurrentValue) == -1) {
			setChoiceValue(null);
		}
	}

	public Object[] getItems() {
		return items;
	}

	public int getItemCount() {
		return items.length;
	}

	public Object getItem(int index) {
		if (index >= 0 && index < items.length) {
			return items[index];
		}

		return null;
	}

	public void setEnabled(boolean bState) {
		btnDown.setEnabled(bState);
		cnvSelection.setEnabled(bState);
		this.bEnabled = bState;
	}

	public boolean isEnabled() {
		return this.bEnabled;
	}

	private void createDropDownComponent(int iXLoc, int iYLoc) {
		if (!bEnabled) {
			return;
		}

		int shellWidth = this.getSize().x;
		Shell shell = new Shell(this.getShell(), SWT.NONE);
		shell.setLayout(new FillLayout(SWT.FILL));
		if ((getStyle() & SWT.RIGHT_TO_LEFT) != 0) {
			iXLoc -= shellWidth;
		}
		shell.setLocation(iXLoc, iYLoc);

		container = new ScrolledComposite(shell, SWT.V_SCROLL);
		container.setAlwaysShowScrollBars(false);
		container.setExpandHorizontal(true);

		cmpDropDown = new Composite(container, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		cmpDropDown.setLayout(gl);

		Listener listenerCmpDropDown = new Listener() {

			public void handleEvent(Event event) {
				handleEventCmpDropDown(event);
			}
		};

		cmpDropDown.addListener(SWT.KeyDown, listenerCmpDropDown);
		cmpDropDown.addListener(SWT.FocusOut, listenerCmpDropDown);

		popupCanvases = new ICustomChoice[this.items.length];

		for (int iC = 0; iC < items.length; iC++) {
			ICustomChoice cnv = createChoice(cmpDropDown, items[iC]);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			cnv.setLayoutData(gd);
			cnv.addListener(SWT.MouseDown, canvasListener);
			cnv.addListener(SWT.MouseEnter, canvasListener);
			cnv.addListener(SWT.KeyDown, canvasListener);

			popupCanvases[iC] = cnv;
			if (cnvSelection.getValue().equals(cnv.getValue())) {
				cnv.notifyListeners(SWT.FocusIn, new Event());
				popupSelection = cnv;
			}
		}

		int width = 0;
		int height = 0;

		int maxWidth = 0;
		Control[] children = container.getChildren();
		for (int i = 0; i < children.length; i++) {
			Point point = children[i].computeSize(SWT.DEFAULT, SWT.DEFAULT);
			maxWidth = point.x > maxWidth ? point.x : maxWidth;
			height += point.y;
		}

		width = getSize().x > maxWidth ? getSize().x : maxWidth;
		height = 18 > height ? 18 : height;

		cmpDropDown.setBounds(0, 0, width, height);

		container.setContent(cmpDropDown);

		if (height >= 298) {
			int containerWidth = maxWidth + container.getVerticalBar().getSize().x;
			width = width > containerWidth ? getSize().x : containerWidth;
		}

		shell.setSize(width, height < 298 ? height + 2 : 300);

		shell.layout();
		shell.open();
	}

	void handleEventCmpDropDown(Event event) {
		switch (event.type) {
		case SWT.KeyDown: {
			keyPressed(new KeyEvent(event));
			break;
		}
		case SWT.FocusOut: {
			Control cTmp = Display.getCurrent().getCursorControl();

			if (cTmp != null) {
				if (cTmp.equals(cnvSelection) || cTmp.equals(btnDown)) {
					bJustFocusLost = true;
				}
			}
			if (!isPopupControl(cTmp)) {
				cmpDropDown.getShell().close();
			}

			break;
		}
		}
	}

	public void select(int index) {
		setChoiceValue(items[index]);
	}

	public int getChoiceIndex() {
		return findChoiceIndex(iCurrentValue);
	}

	/**
	 * Returns the current selected choice
	 * 
	 */
	public Object getChoiceValue() {
		return iCurrentValue;
	}

	/**
	 * Sets the value as selected choice, and redraws UI.
	 * 
	 * @param iValue value as selected choice
	 */
	public void setChoiceValue(Object iValue) {
		iCurrentValue = iValue;
		cnvSelection.setValue(iCurrentValue);
		cnvSelection.redraw();
	}

	public void addListener(int eventType, Listener listener) {
		switch (eventType) {
		case SELECTION_EVENT:
			vSelectionListeners.add(listener);
			break;
		case DROPDOWN_EVENT:
			vDropDownListeners.add(listener);
			break;
		}
	}

	public void removeListener(int eventType, Listener listener) {
		switch (eventType) {
		case SELECTION_EVENT:
			vSelectionListeners.remove(listener);
			break;
		case DROPDOWN_EVENT:
			vDropDownListeners.remove(listener);
			break;
		}
	}

	private void toggleDropDown() {
		// fix for Linux, since it not send the event correctly to other than
		// current shell.
		if (bJustFocusLost) {
			bJustFocusLost = false;
			return;
		}

		if (cmpDropDown == null || cmpDropDown.isDisposed() || !cmpDropDown.isVisible()) {
			fireDropDownEvent();

			Point pLoc = UIHelper.getScreenLocation(this);
			// It seems the Mac-OSX event mechanism is special different from
			// windows, if the pop-up shell don't cover drop-down component, it
			// will trigger focus out event on pop-up component, it will cause
			// unexpected behavior and close pop-up component. To avoid this
			// result, make pop-up component still covers drop-down component,
			// it is like implementation of standard Combo.
			if (Platform.OS_MACOSX.equals(Platform.getOS())) {
				createDropDownComponent(pLoc.x, pLoc.y);
			} else {
				createDropDownComponent(pLoc.x, pLoc.y + this.getSize().y);
			}
		} else {
			cmpDropDown.getShell().close();
		}
	}

	private void fireSelectionEvent() {
		Event e = new Event();
		e.widget = this;
		e.data = this.iCurrentValue;
		e.type = SELECTION_EVENT;
		for (int i = 0; i < vSelectionListeners.size(); i++) {
			vSelectionListeners.get(i).handleEvent(e);
		}
	}

	private void fireDropDownEvent() {
		Event e = new Event();
		e.widget = this;
		e.type = DROPDOWN_EVENT;
		for (int i = 0; i < vDropDownListeners.size(); i++) {
			vDropDownListeners.get(i).handleEvent(e);
		}
	}

	void keyPressed(KeyEvent event) {
		if (cmpDropDown != null && !cmpDropDown.getShell().isDisposed()) {
			if (event.keyCode == SWT.ESC) {
				cmpDropDown.getShell().close();
			} else if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
				ICustomChoice currentControl = popupSelection == null
						? popupCanvases[findChoiceIndex((cnvSelection).getValue())]
						: popupSelection;
				setChoiceValue(currentControl.getValue());
				fireSelectionEvent();
				cmpDropDown.getShell().close();
			} else if (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN) {
				ICustomChoice currentControl = popupSelection == null
						? popupCanvases[findChoiceIndex((cnvSelection).getValue())]
						: popupSelection;
				int styleIndex = findChoiceIndex(currentControl.getValue());
				if (styleIndex >= 0) {
					currentControl.notifyListeners(SWT.FocusOut, new Event());
					currentControl.redraw();

					if (event.keyCode == SWT.ARROW_UP) {
						if (styleIndex > 0) {
							styleIndex--;
							// !only redraw, don't change selection
							// setChoiceValue( items[styleIndex] );
							// fireSelectionEvent( );
						}
					} else if (event.keyCode == SWT.ARROW_DOWN) {
						if (styleIndex < items.length - 1) {
							styleIndex++;
							// !only redraw, don't change selection
							// setChoiceValue( items[styleIndex] );
							// fireSelectionEvent( );
						}
					}

					popupSelection = popupCanvases[styleIndex];
					popupSelection.notifyListeners(SWT.FocusIn, new Event());
					popupSelection.redraw();

					if (popupSelection instanceof Control)
						container.showControl((Control) popupSelection);
				}
			}
		}
	}

	private boolean isPopupControl(Object control) {
		return control != null && control instanceof Control
				&& ((Control) control).getShell() == cmpDropDown.getShell();
	}

	void handleEventCanvasSelection(Event event) {
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
			// At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed())
				break;

			if (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN) {
				toggleDropDown();
			}
			break;
		}
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
		case SWT.MouseDown:
			toggleDropDown();
			break;
		}
	}

	void handleEventCanvasPopup(Event event) {
		switch (event.type) {
		case SWT.MouseDown:
			setChoiceValue(((ICustomChoice) event.widget).getValue());
			this.cmpDropDown.getShell().close();
			fireSelectionEvent();
			break;

		case SWT.MouseEnter:
			if (popupSelection != null) {
				// Redraw the selection canvas in popup
				popupSelection.notifyListeners(SWT.FocusOut, new Event());
				popupSelection.redraw();
				// popupSelection = null;
			}
			popupSelection = (ICustomChoice) event.widget;
			popupSelection.notifyListeners(SWT.FocusIn, event);
			popupSelection.redraw();
			break;

		}
	}

	private int findChoiceIndex(Object value) {
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				if (items[i].equals(value)) {
					return i;
				}
			}
		}

		return -1;
	}

	protected void initAccessible() {
		// A workaround to resolve NullPointerException bug in SWT 4.3 M7, will
		// restore when swt solve the missing of NPE check in Accessible class
		try {
			getAccessible().removeAccessibleListener(accessibleListener);
			getAccessible().addAccessibleListener(accessibleListener);
			getAccessible().removeAccessibleControlListener(accessibleControlListener);
			getAccessible().addAccessibleControlListener(accessibleControlListener);
		} catch (NullPointerException e) {

		}
	}
}
