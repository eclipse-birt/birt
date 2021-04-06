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

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A text angle selector that facilitates text rotation angle specification
 */
public final class AngleSelectorComposite extends Canvas
		implements PaintListener, MouseListener, MouseMoveListener, DisposeListener, ControlListener {
	/**
	 *  
	 */
	private transient final Point p = new Point(0, 0);

	/**
	 *  
	 */
	private transient int iLastAngle = 0, iRadius = 0;

	/**
	 *  
	 */
	private transient boolean bMouseDown = false;

	/**
	 *  
	 */
	private transient IAngleChangeListener iacl = null;

	/**
	 *  
	 */
	private transient Color clrBG = null;

	/**
	 * An offscreen image used to render the palette entries using double buffering.
	 * This image is re-created when a composite resize occurs.
	 */
	private Image imgBuffer = null;

	/**
	 * Associated with the offscreen image and whose lifecycle depends on the
	 * buffered image's lifecycle
	 */
	private GC gcBuffer = null;

	/**
	 * Arrow co-ordinates
	 */
	private transient final int[] iaPolygon = new int[6];

	/**
	 * 
	 * @param coParent
	 * @param iStyle
	 * @param iAngle
	 * @param clrBG
	 */
	public AngleSelectorComposite(Composite coParent, int iStyle, int iAngle, Color clrBG) {
		super(coParent, iStyle);
		this.iLastAngle = iAngle;
		addPaintListener(this);
		addMouseListener(this);
		addDisposeListener(this);
		addControlListener(this);
		addMouseMoveListener(this);
		initAccessible();
		this.clrBG = clrBG;
		setBackground(clrBG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.
	 * PaintEvent)
	 */
	public void paintControl(PaintEvent pev) {
		final Display d = Display.getCurrent();
		final GC gcCanvas = pev.gc;
		final Rectangle rCA = getClientArea();
		final int iWidth = rCA.height / 2 - 8;
		final int iHeight = rCA.height - 16;

		if (imgBuffer == null) {
			imgBuffer = new Image(d, rCA.width, rCA.height);
			gcBuffer = new GC(imgBuffer);
		}

		// PAINT THE CLIENT AREA BLOCK
		if (!this.isEnabled()) {
			gcBuffer.setBackground(d.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		} else {
			gcBuffer.setBackground(clrBG);
		}
		gcBuffer.setForeground(d.getSystemColor(SWT.COLOR_GRAY));
		gcBuffer.fillRectangle(rCA);

		// PAINT THE BIG/SMALL DOTS
		p.x = 10;
		p.y = iHeight / 2 + 8;
		double dRadians;
		int x, y;
		gcBuffer.setForeground(d.getSystemColor(SWT.COLOR_BLACK));
		gcBuffer.setBackground(d.getSystemColor(SWT.COLOR_RED));
		for (int i = -90; i <= 90; i += 15) {
			dRadians = Math.toRadians(i);
			x = (int) (p.x + iWidth * Math.cos(dRadians));
			y = (int) (p.y - iWidth * Math.sin(dRadians));
			if ((i % 45) == 0) // CHECK FOR MULTIPLES OF 45
			{
				bigPoint(d, gcBuffer, x, y, i == iLastAngle);
			} else
			// ALWAYS A MULTIPLE OF 15 DUE TO THE INCREMENT
			{
				smallPoint(d, gcBuffer, x, y, i == iLastAngle);
			}
		}

		// DRAW THE HAND POINTER
		iRadius = iWidth;
		drawHand(d, gcBuffer, p.x, p.y, iRadius - 10, iLastAngle, false);

		gcCanvas.drawImage(imgBuffer, 0, 0);
	}

	/**
	 * 
	 * @param d
	 * @param gc
	 * @param x
	 * @param y
	 * @param bSelected
	 */
	private static final void bigPoint(Display d, GC gc, int x, int y, boolean bSelected) {
		gc.setForeground(d.getSystemColor(SWT.COLOR_BLACK));
		gc.setBackground(d.getSystemColor(bSelected ? SWT.COLOR_RED : SWT.COLOR_BLACK));
		final int[] iaXY = { x, y - 3, x - 3, y, x, y + 3, x + 3, y }; // TBD: REUSE INSTANCE VAR
		gc.fillPolygon(iaXY);
		gc.drawPolygon(iaXY);
	}

	/**
	 * 
	 * @param d
	 * @param gc
	 * @param x
	 * @param y
	 * @param bSelected
	 */
	private static final void smallPoint(Display d, GC gc, int x, int y, boolean bSelected) {
		gc.setForeground(d.getSystemColor(bSelected ? SWT.COLOR_RED : SWT.COLOR_BLACK));
		gc.drawRectangle(x - 1, y - 1, 1, 1);
	}

	/**
	 * 
	 * @param dAngle
	 * @param gc
	 * @param x
	 * @param y
	 */
	private final void drawHand(Display d, GC gc, int x, int y, int r, double dAngleInDegrees, boolean bErase) {
		gc.setForeground(bErase ? clrBG : d.getSystemColor(SWT.COLOR_BLACK));
		gc.setBackground(bErase ? clrBG : d.getSystemColor(SWT.COLOR_RED));

		final double dAngleInRadians = Math.toRadians(dAngleInDegrees);
		final int rMinus = r - 10;
		final double dAngleInRadiansMinus = Math.toRadians(dAngleInDegrees - 3);
		final double dAngleInRadiansPlus = Math.toRadians(dAngleInDegrees + 3);
		final int xTip = (int) (x + r * Math.cos(dAngleInRadians));
		final int yTip = (int) (y - r * Math.sin(dAngleInRadians));

		// DRAW THE STICK
		gc.drawLine(x, y, xTip, yTip);

		// DRAW THE ARROW
		iaPolygon[0] = xTip;
		iaPolygon[1] = yTip;
		iaPolygon[2] = (int) (x + rMinus * Math.cos(dAngleInRadiansMinus));
		iaPolygon[3] = (int) (y - rMinus * Math.sin(dAngleInRadiansMinus));
		iaPolygon[4] = (int) (x + rMinus * Math.cos(dAngleInRadiansPlus));
		iaPolygon[5] = (int) (y - rMinus * Math.sin(dAngleInRadiansPlus));
		gc.fillPolygon(iaPolygon);
		gc.drawPolygon(iaPolygon);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent arg0) {
		// UNUSED
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	public void mouseDown(MouseEvent mev) {
		bMouseDown = true;
		updateAngle(mev.x, mev.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	public void mouseUp(MouseEvent mev) {
		bMouseDown = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	public void mouseMove(MouseEvent mev) {
		if (!bMouseDown)
			return; // MOUSE DRAG FILTER = (DOWN + MOVE)
		updateAngle(mev.x, mev.y);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	private final void updateAngle(int mx, int my) {
		int iAngle = (int) Math.toDegrees(Math.atan2(-(my - p.y), mx - p.x));
		if (iAngle > 90)
			iAngle = 90; // UPPER LIMIT
		if (iAngle < -90)
			iAngle = -90; // LOWER LIMIT
		if (iAngle == iLastAngle) // OPTIMIZED REFRESH
		{
			return;
		}

		// SETUP CONTEXT
		final Display d = Display.getCurrent();
		final GC gc = new GC(this);

		drawHand(d, gc, p.x, p.y, iRadius - 10, iLastAngle, true); // RUB OUT HAND
		if ((iLastAngle % 45) == 0) {
			final int x = (int) (p.x + iRadius * Math.cos(Math.toRadians(iLastAngle)));
			final int y = (int) (p.y - iRadius * Math.sin(Math.toRadians(iLastAngle)));
			bigPoint(d, gc, x, y, false);
		} else if ((iLastAngle % 15) == 0) {
			final int x = (int) (p.x + iRadius * Math.cos(Math.toRadians(iLastAngle)));
			final int y = (int) (p.y - iRadius * Math.sin(Math.toRadians(iLastAngle)));
			smallPoint(d, gc, x, y, false);
		}
		iLastAngle = iAngle;
		drawHand(d, gc, p.x, p.y, iRadius - 10, iLastAngle, false); // REDRAW HAND
		if ((iLastAngle % 45) == 0) {
			final int x = (int) (p.x + iRadius * Math.cos(Math.toRadians(iLastAngle)));
			final int y = (int) (p.y - iRadius * Math.sin(Math.toRadians(iLastAngle)));
			bigPoint(d, gc, x, y, true);
		} else if ((iLastAngle % 15) == 0) {
			final int x = (int) (p.x + iRadius * Math.cos(Math.toRadians(iLastAngle)));
			final int y = (int) (p.y - iRadius * Math.sin(Math.toRadians(iLastAngle)));
			smallPoint(d, gc, x, y, true);
		}
		gc.dispose();

		// NOTIFY LISTENER OF ANGLE CHANGE
		if (iacl != null) {
			iacl.angleChanged(iAngle);
		}
	}

	/**
	 * Associates a listener with this custom widget
	 * 
	 * @param iacl
	 */
	public final void setAngleChangeListener(IAngleChangeListener iacl) {
		this.iacl = iacl;
	}

	/**
	 * 
	 * @param iNewAngle
	 */
	public void setAngle(int iNewAngle) {
		iLastAngle = iNewAngle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.
	 * DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent e) {
		if (imgBuffer != null) {
			gcBuffer.dispose();
			imgBuffer.dispose();
			gcBuffer = null;
			imgBuffer = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	public void controlResized(ControlEvent e) {
		if (imgBuffer != null) {
			gcBuffer.dispose();
			imgBuffer.dispose();
			gcBuffer = null;
			imgBuffer = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	public void controlMoved(ControlEvent e) {
		// NOT USED
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
	}
}