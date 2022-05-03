/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.core.ui.frameworks.taskwizard.composites;

import org.eclipse.birt.core.ui.utils.UIHelper;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 *
 */
public final class MessageComposite extends Composite implements PaintListener {

	/**
	 *
	 */
	transient Image img = null;

	transient boolean bDisableImage = false;

	/**
	 *
	 */
	private String sTitle;

	/**
	 *
	 */
	private String sDescription;

	/**
	 *
	 */
	private Label laTitle = null, laDescription = null;

	/**
	 *
	 */
	private ImageCanvas ic = null;

	private LocalResourceManager resourceManager;

	/**
	 *
	 */
	public MessageComposite(Composite coParent, String sImagePath, String sTitle, String sDescription,
			boolean bDisableImage) {
		super(coParent, SWT.NONE);
		this.sTitle = sTitle;
		this.sDescription = sDescription;
		this.bDisableImage = bDisableImage;
		if (!bDisableImage) {
			img = UIHelper.getImage(sImagePath);
		}
		setup();
	}

	/**
	 *
	 * @param sTitle
	 * @param sDescription
	 */
	void update(String sTitle, String sDescription) {
		this.sTitle = sTitle;
		this.sDescription = sDescription;
		laTitle.setText(sTitle);
		laDescription.setText(sDescription);
	}

	/**
	 *
	 */
	@Override
	public void setBackground(Color cBG) {
		super.setBackground(cBG);

		laTitle.setBackground(cBG);
		laDescription.setBackground(cBG);
		if (!bDisableImage) {
			ic.setBackground(cBG);
		}
	}

	/**
	 *
	 */
	private void setup() {

		this.addPaintListener(this);
		GridLayout gl = new GridLayout();
		if (!bDisableImage) {
			gl.numColumns = 2;
		}
		setLayout(gl);

		laTitle = new Label(this, SWT.WRAP);

		this.resourceManager = new LocalResourceManager(JFaceResources.getResources(), this);
		Font titleFont = this.resourceManager.createFont(FontDescriptor.createFrom(this.getFont()).setStyle(SWT.BOLD));

		laTitle.setFont(titleFont);
		laTitle.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));

		laTitle.setText(sTitle);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		laTitle.setLayoutData(gd);
		laTitle.setAlignment(SWT.CENTER);

		if (!bDisableImage) {
			ic = new ImageCanvas(this);
			gd = new GridData();
			gd.verticalSpan = 2;
			gd.verticalAlignment = GridData.BEGINNING;
			gd.horizontalIndent = 10;
			ic.setLayoutData(gd);
		}

		laDescription = new Label(this, SWT.LEFT | SWT.WRAP | SWT.DRAW_TRANSPARENT);
		laDescription.setText(sDescription);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalIndent = 10;
		laDescription.setLayoutData(gd);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.
	 * PaintEvent)
	 */
	@Override
	public void paintControl(PaintEvent pev) {
		Rectangle rCA = getClientArea();
		rCA.width--;
		rCA.height--;
		GC gc = pev.gc;
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		gc.drawRectangle(rCA);
	}

	/**
	 *
	 */
	private final class ImageCanvas extends Canvas implements PaintListener {

		/**
		 *
		 */
		private final Rectangle rSize;

		/**
		 *
		 * @param coParent
		 */
		private ImageCanvas(Composite coParent) {
			super(coParent, SWT.NONE);
			addPaintListener(this);
			rSize = new Rectangle(0, 0, img.getImageData().width, img.getImageData().height);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.
		 * PaintEvent)
		 */
		@Override
		public void paintControl(PaintEvent pev) {
			GC gc = pev.gc;
			gc.drawImage(img, 0, 0);
		}
	}
}
