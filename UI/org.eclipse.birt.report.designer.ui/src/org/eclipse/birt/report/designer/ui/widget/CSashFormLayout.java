/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.widget;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides the layout for CSashForm
 *
 * @see CSashForm
 */
public class CSashFormLayout extends Layout {

	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		CSashForm sashForm = (CSashForm) composite;
		Control[] cArray = sashForm.getControls(true);
		int width = 0;
		int height = 0;
		if (cArray.length == 0) {
			if (wHint != SWT.DEFAULT)
				width = wHint;
			if (hHint != SWT.DEFAULT)
				height = hHint;
			return new Point(width, height);
		}
		// determine control sizes
		boolean vertical = sashForm.getOrientation() == SWT.VERTICAL;
		int maxIndex = 0;
		int maxValue = 0;
		for (int i = 0; i < cArray.length; i++) {
			if (vertical) {
				Point size = cArray[i].computeSize(wHint, SWT.DEFAULT, flushCache);
				if (size.y > maxValue) {
					maxIndex = i;
					maxValue = size.y;
				}
				width = Math.max(width, size.x);
			} else {
				Point size = cArray[i].computeSize(SWT.DEFAULT, hHint, flushCache);
				if (size.x > maxValue) {
					maxIndex = i;
					maxValue = size.x;
				}
				height = Math.max(height, size.y);
			}
		}
		// get the ratios
		long[] ratios = new long[cArray.length];
		long total = 0;
		int obligatedWidth = 0;

		for (int i = 0; i < cArray.length; i++) {
			Object data = cArray[i].getLayoutData();
			if (data != null && data instanceof CSashFormData) {
				ratios[i] = ((CSashFormData) data).weight;
			} else {
				data = new CSashFormData();
				cArray[i].setLayoutData(data);
				((CSashFormData) data).weight = ratios[i] = ((200 << 16) + 999) / 1000;

			}
			if (ratios[i] != CSashFormData.NOT_SET) {
				total += ratios[i];
			} else {
				obligatedWidth += ((CSashFormData) data).exactWidth;
			}
		}

		if (ratios[maxIndex] > 0) {
			int sashwidth = sashForm.sashes.length > 0 ? sashForm.SASH_WIDTH + sashForm.sashes[0].getBorderWidth() * 2
					: sashForm.SASH_WIDTH;
			if (vertical) {
				height += (int) (total * maxValue / ratios[maxIndex]) + (cArray.length - 1) * sashwidth
						+ obligatedWidth;
			} else {
				width += (int) (total * maxValue / ratios[maxIndex]) + (cArray.length - 1) * sashwidth + obligatedWidth;
			}
		}

		width += sashForm.getBorderWidth() * 2;
		height += sashForm.getBorderWidth() * 2;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		return new Point(width, height);
	}

	protected boolean flushCache(Control control) {
		return true;
	}

	protected void layout(Composite composite, boolean flushCache) {
		CSashForm sashForm = (CSashForm) composite;
		Rectangle area = sashForm.getClientArea();
		if (area.width <= 1 || area.height <= 1)
			return;

		Control[] newControls = sashForm.getControls(true);
		if (sashForm.controls.length == 0 && newControls.length == 0)
			return;
		sashForm.controls = newControls;

		Control[] controls = sashForm.controls;

		if (sashForm.maxControl != null && !sashForm.maxControl.isDisposed()) {
			for (int i = 0; i < controls.length; i++) {
				if (controls[i] != sashForm.maxControl) {
					controls[i].setBounds(-200, -200, 0, 0);
				} else {
					controls[i].setBounds(area);
				}
			}
			return;
		}

		// keep just the right number of sashes
		if (sashForm.sashes.length < controls.length - 1) {
			Sash[] newSashes = new Sash[controls.length - 1];
			System.arraycopy(sashForm.sashes, 0, newSashes, 0, sashForm.sashes.length);
			for (int i = sashForm.sashes.length; i < newSashes.length; i++) {
				newSashes[i] = new Sash(sashForm, sashForm.sashStyle);
				newSashes[i].setBackground(sashForm.background);
				newSashes[i].setForeground(sashForm.foreground);
				newSashes[i].addListener(SWT.Selection, sashForm.sashListener);
			}
			sashForm.sashes = newSashes;
		}
		if (sashForm.sashes.length > controls.length - 1) {
			if (controls.length == 0) {
				for (int i = 0; i < sashForm.sashes.length; i++) {
					sashForm.sashes[i].dispose();
				}
				sashForm.sashes = new Sash[0];
			} else {
				Sash[] newSashes = new Sash[controls.length - 1];
				System.arraycopy(sashForm.sashes, 0, newSashes, 0, newSashes.length);
				for (int i = controls.length - 1; i < sashForm.sashes.length; i++) {
					sashForm.sashes[i].dispose();
				}
				sashForm.sashes = newSashes;
			}
		}
		if (controls.length == 0)
			return;
		Sash[] sashes = sashForm.sashes;
		// get the ratios
		long[] ratios = new long[controls.length];
		long total = 0;
		// obligated width
		int obligatedWidth = 0;

		for (int i = 0; i < controls.length; i++) {
			Object data = controls[i].getLayoutData();
			if (data != null && data instanceof CSashFormData) {
				ratios[i] = ((CSashFormData) data).weight;
			} else {
				data = new CSashFormData();
				controls[i].setLayoutData(data);
				((CSashFormData) data).weight = ratios[i] = ((200 << 16) + 999) / 1000;

			}
			if (ratios[i] != CSashFormData.NOT_SET) {
				total += ratios[i];
			} else {
				obligatedWidth += ((CSashFormData) data).exactWidth;
			}

		}

		int sashwidth = sashes.length > 0 ? sashForm.SASH_WIDTH + sashes[0].getBorderWidth() * 2 : sashForm.SASH_WIDTH;
		if (sashForm.getOrientation() == SWT.HORIZONTAL) {
			int remainWidth = area.width - obligatedWidth;

			int width = 0;
			if (ratios[0] != CSashFormData.NOT_SET) {
				width = (int) (ratios[0] * (remainWidth - sashes.length * sashwidth) / total);
			} else {
				CSashFormData cData = (CSashFormData) controls[0].getLayoutData();
				width = cData.exactWidth;
			}

			int x = area.x;
			controls[0].setBounds(x, area.y, width, area.height);
			x += width;
			for (int i = 1; i < controls.length - 1; i++) {
				sashes[i - 1].setBounds(x, area.y, sashwidth, area.height);
				x += sashwidth;

				if (ratios[i] != CSashFormData.NOT_SET) {
					width = (int) (ratios[i] * (remainWidth - sashes.length * sashwidth) / total);
				} else {
					CSashFormData cData = (CSashFormData) controls[i].getLayoutData();
					width = cData.exactWidth;
				}

				controls[i].setBounds(x, area.y, width, area.height);
				x += width;
			}
			if (controls.length > 1) {
				sashes[sashes.length - 1].setBounds(x, area.y, sashwidth, area.height);
				x += sashwidth;
				width = area.width - x;
				controls[controls.length - 1].setBounds(x, area.y, width, area.height);
			}
		} else {
			int remainHeight = area.height - obligatedWidth;

			int height = 0;
			if (ratios[0] != CSashFormData.NOT_SET) {
				height = (int) (ratios[0] * (remainHeight - sashes.length * sashwidth) / total);
			} else {
				CSashFormData cData = (CSashFormData) controls[0].getLayoutData();
				height = cData.exactWidth;
			}

			int y = area.y;
			controls[0].setBounds(area.x, y, area.width, height);
			y += height;
			for (int i = 1; i < controls.length - 1; i++) {
				sashes[i - 1].setBounds(area.x, y, area.width, sashwidth);
				y += sashwidth;

				if (ratios[i] != CSashFormData.NOT_SET) {
					height = (int) (ratios[i] * (remainHeight - sashes.length * sashwidth) / total);
				} else {
					CSashFormData cData = (CSashFormData) controls[i].getLayoutData();
					height = cData.exactWidth;
				}

				controls[i].setBounds(area.x, y, area.width, height);
				y += height;
			}
			if (controls.length > 1) {
				sashes[sashes.length - 1].setBounds(area.x, y, area.width, sashwidth);
				y += sashwidth;
				height = area.height - y;
				controls[controls.length - 1].setBounds(area.x, y, area.width, height);
			}

		}
	}
}
