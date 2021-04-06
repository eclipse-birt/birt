/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * The <code>ColorSelector</code> is a wrapper for a button that displays a
 * selected <code>Color</code> and allows the user to change the selection.
 * 
 * Note: this is a patched version from jface ColorSelector since 3.3 to fix
 * bugzilla#139368, once jface has fixed that, we should switch back.
 */
public class ColorSelector extends EventManager {

	/**
	 * Property name that signifies the selected color of this
	 * <code>ColorSelector</code> has changed.
	 * 
	 * @since 3.0
	 */
	public static final String PROP_COLORCHANGE = "colorValue"; //$NON-NLS-1$

	private Button fButton;

	private Color fColor;

	private RGB fColorValue;

	private Point fExtent;

	private Image fImage;

	/**
	 * Create a new instance of the reciever and the button that it wrappers in the
	 * supplied parent <code>Composite</code>.
	 * 
	 * @param parent The parent of the button.
	 */
	public ColorSelector(Composite parent) {
		fButton = new Button(parent, SWT.PUSH);
		fExtent = computeImageSize(parent);
		fImage = new Image(parent.getDisplay(), fExtent.x, fExtent.y);
		GC gc = new GC(fImage);
		gc.setBackground(fButton.getBackground());
		gc.fillRectangle(0, 0, fExtent.x, fExtent.y);
		gc.dispose();
		fButton.setImage(fImage);
		fButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				open();
			}
		});
		fButton.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent event) {
				if (fImage != null) {
					fImage.dispose();
					fImage = null;
				}
				if (fColor != null) {
					fColor.dispose();
					fColor = null;
				}
			}
		});
		fButton.getAccessible().addAccessibleListener(new AccessibleAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.accessibility.AccessibleAdapter#getName
			 * (org.eclipse.swt.accessibility.AccessibleEvent)
			 */
			public void getName(AccessibleEvent e) {
				e.result = JFaceResources.getString("ColorSelector.Name"); //$NON-NLS-1$
			}
		});
	}

	/**
	 * Adds a property change listener to this <code>ColorSelector</code>. Events
	 * are fired when the color in the control changes via the user clicking an
	 * selecting a new one in the color dialog. No event is fired in the case where
	 * <code>setColorValue(RGB)</code> is invoked.
	 * 
	 * @param listener a property change listener
	 * @since 3.0
	 */
	public void addListener(IPropertyChangeListener listener) {
		addListenerObject(listener);
	}

	/**
	 * Compute the size of the image to be displayed.
	 * 
	 * @param window - the window used to calculate
	 * @return <code>Point</code>
	 */
	private Point computeImageSize(Control window) {
		GC gc = new GC(window);
		Font f = JFaceResources.getFontRegistry().get(JFaceResources.DIALOG_FONT);
		gc.setFont(f);
		int height = gc.getFontMetrics().getHeight();
		gc.dispose();
		Point p = new Point(height * 3 - 6, height);
		return p;
	}

	/**
	 * Get the button control being wrappered by the selector.
	 * 
	 * @return <code>Button</code>
	 */
	public Button getButton() {
		return fButton;
	}

	/**
	 * Return the currently displayed color.
	 * 
	 * @return <code>RGB</code>
	 */
	public RGB getColorValue() {
		return fColorValue;
	}

	/**
	 * Removes the given listener from this <code>ColorSelector</code>. Has no
	 * affect if the listener is not registered.
	 * 
	 * @param listener a property change listener
	 * @since 3.0
	 */
	public void removeListener(IPropertyChangeListener listener) {
		removeListenerObject(listener);
	}

	/**
	 * Set the current color value and update the control.
	 * 
	 * @param rgb The new color.
	 */
	public void setColorValue(RGB rgb) {
		fColorValue = rgb;
		updateColorImage();
	}

	/**
	 * Set whether or not the button is enabled.
	 * 
	 * @param state the enabled state.
	 */
	public void setEnabled(boolean state) {
		getButton().setEnabled(state);
	}

	/**
	 * Update the image being displayed on the button using the current color
	 * setting.
	 */
	protected void updateColorImage() {
		Display display = fButton.getDisplay();
		GC gc = new GC(fImage);

		if (fColor != null) {
			fColor.dispose();
		}

		if (fColorValue == null) {
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.drawRectangle(0, 2, fExtent.x - 1, fExtent.y - 4);
			gc.setBackground(fButton.getBackground());
			gc.fillRectangle(1, 3, fExtent.x - 2, fExtent.y - 5);

		} else {
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.drawRectangle(0, 2, fExtent.x - 1, fExtent.y - 4);
			fColor = new Color(display, fColorValue);
			gc.setBackground(fColor);
			gc.fillRectangle(1, 3, fExtent.x - 2, fExtent.y - 5);
		}
		gc.dispose();
		fButton.setImage(fImage);
	}

	/**
	 * Activate the editor for this selector. This causes the color selection dialog
	 * to appear and wait for user input.
	 * 
	 * @since 3.2
	 */
	public void open() {
		/*
		 * Bugzilla #269139 - Issue about color editor dialog in Highlight Editor
		 * dialog. (After the fixing of Bugizlla #223759, the change is not applicable
		 * on Linux and causes #269139. The workaround is to detect what type the OS is
		 * and only appply the change of #223759 on Windows.)
		 */
		Shell shell = null;
		Boolean isWin32 = Platform.getOS().equals(Platform.OS_WIN32);
		if (isWin32) {
			shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM);
			shell.setLocation(fButton.toDisplay(0, 0).x + fButton.getBounds().width,
					fButton.toDisplay(0, 0).y - fButton.getBounds().height);
		}
		ColorDialog colorDialog = new ColorDialog(isWin32 ? shell : fButton.getShell(), SWT.APPLICATION_MODAL);
		RGB[] rgbs = ReportPlugin.getDefault().getCustomColorsPreference();
		if (rgbs != null) {
			colorDialog.setRGBs(rgbs);
		}
		colorDialog.setRGB(fColorValue);
		colorDialog.setText(Messages.getString("ColorSelector.ColorDialog.Title")); //$NON-NLS-1$
		RGB newColor = colorDialog.open();
		ReportPlugin.getDefault().setCustomColorsPreference(colorDialog.getRGBs());
		if (newColor != null) {
			RGB oldValue = fColorValue;
			fColorValue = newColor;
			final Object[] finalListeners = getListeners();
			if (finalListeners.length > 0) {
				PropertyChangeEvent pEvent = new PropertyChangeEvent(this, PROP_COLORCHANGE, oldValue, newColor);
				for (int i = 0; i < finalListeners.length; ++i) {
					IPropertyChangeListener listener = (IPropertyChangeListener) finalListeners[i];
					listener.propertyChange(pEvent);
				}
			}
			updateColorImage();
		}
		if (isWin32) {
			shell.dispose();
		}
	}
}
