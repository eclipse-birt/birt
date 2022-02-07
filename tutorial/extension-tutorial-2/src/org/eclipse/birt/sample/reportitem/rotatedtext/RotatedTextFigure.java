/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.sample.reportitem.rotatedtext;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.sample.reportitem.rotatedtext.util.SwtGraphicsUtil;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

/**
 * RotatedTextFigure
 */
public class RotatedTextFigure extends Figure {

	private String lastText;
	private int lastAngle;

	private Image cachedImage;

	private RotatedTextItem textItem;

	RotatedTextFigure(RotatedTextItem textItem) {
		super();

		this.textItem = textItem;

		addMouseListener(new MouseListener.Stub() {

			public void mousePressed(MouseEvent me) {
				if (me.button == 2) {
					try {
						RotatedTextFigure.this.textItem
								.setRotationAngle(normalize(RotatedTextFigure.this.textItem.getRotationAngle() + 45));
					} catch (SemanticException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private int normalize(int angle) {
		angle = angle % 360;

		if (angle < 0) {
			angle += 360;
		}

		return angle;
	}

	public Dimension getMinimumSize(int hint, int hint2) {
		return getPreferredSize(hint, hint2);
	}

	public Dimension getPreferredSize(int hint, int hint2) {
		Display display = Display.getCurrent();

		GC gc = null;

		try {
			String text = textItem.getText();
			int angle = textItem.getRotationAngle();

			gc = new GC(display);

			Point pt = gc.textExtent(text == null ? "" : text); //$NON-NLS-1$

			double[] info = SwtGraphicsUtil.computedRotatedInfo(pt.x, pt.y, angle);

			if (getBorder() != null) {
				Insets bdInsets = getBorder().getInsets(this);

				return new Dimension((int) info[0] + bdInsets.getWidth(), (int) info[1] + bdInsets.getHeight());
			}
			return new Dimension((int) info[0], (int) info[1]);
		} finally {
			if (gc != null && !gc.isDisposed()) {
				gc.dispose();
			}
		}
	}

	protected void paintClientArea(Graphics graphics) {
		final Rectangle r = getClientArea().getCopy();

		String text = textItem.getText();
		int angle = textItem.getRotationAngle();

		if (text == null) {
			text = ""; //$NON-NLS-1$
		}

		if (!text.equals(lastText) || angle != lastAngle || cachedImage == null || cachedImage.isDisposed()) {
			lastText = text;
			lastAngle = angle;

			if (cachedImage != null && !cachedImage.isDisposed()) {
				cachedImage.dispose();
			}

			cachedImage = SwtGraphicsUtil.createRotatedTextImage(text, angle, null);
		}

		if (cachedImage != null && !cachedImage.isDisposed()) {
			graphics.drawImage(cachedImage, r.x, r.y);
		}
	}

	void setRotatedTextItem(RotatedTextItem item) {
		this.textItem = item;
	}

	void dispose() {
		if (cachedImage != null && !cachedImage.isDisposed()) {
			cachedImage.dispose();
		}
	}
}
