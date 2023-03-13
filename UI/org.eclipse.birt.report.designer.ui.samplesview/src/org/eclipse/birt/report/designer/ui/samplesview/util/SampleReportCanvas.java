/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.samplesview.util;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/*
 * Represents the Canva in preview group
 */
public class SampleReportCanvas extends Canvas {

	private Image sampleImage;

	public SampleReportCanvas(Composite parent, int style) {
		super(parent, style);

		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent pe) {
				SampleReportCanvas.this.paintControl(pe);
			}
		});

		addControlListener(new ControlListener() {

			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void controlResized(ControlEvent e) {
				SampleReportCanvas.this.controlResized(e);
			}
		});

		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (sampleImage != null && !sampleImage.isDisposed()) {
					sampleImage.dispose();
				}

			}
		});
	}

	public void setSampleImage(Image sampleImage) {
		Image oldImage = this.sampleImage;
		this.sampleImage = sampleImage;
		if (oldImage != null && oldImage != sampleImage) {
			oldImage.dispose();
		}
	}

	protected void paintControl(PaintEvent pe) {
		GC gc = pe.gc;
		if (sampleImage != null) {
			double srcRatio = (double) sampleImage.getBounds().width / (double) sampleImage.getBounds().height;
			double clntRatio = (double) getClientArea().width / (double) getClientArea().height;

			if (srcRatio >= clntRatio) {
				gc.drawImage(sampleImage, 0, 0, sampleImage.getBounds().width, sampleImage.getBounds().height, 0, 0,
						getClientArea().width, (int) (getClientArea().width / srcRatio));
			} else {
				gc.drawImage(sampleImage, 0, 0, sampleImage.getBounds().width, sampleImage.getBounds().height, 0, 0,
						(int) (getClientArea().height * srcRatio), getClientArea().height);
			}
		}
	}

	protected void controlResized(ControlEvent e) {
		this.redraw();
	}
}
