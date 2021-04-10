/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.tests.example.matrix;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.core.runtime.Assert;

/**
 *  
 */

public class TestingMatrixUI implements IReportItemFigureProvider {

	public static final String TEST_ELEMENT = "TestingBall"; //$NON-NLS-1$
	public static final String TEST_NAME = "Just for test"; //$NON-NLS-1$
	public static final String[] TEST_PROPERTY = { "test1", "test2", }; //$NON-NLS-1$ //$NON-NLS-2$
	public static final Object[] TEST_ELEMENT_CONTENT = { "default test", new Integer(1024) }; //$NON-NLS-1$
	public static final Object[] TEST_ELEMENT_CONTENT_EDITED = { "edit test", new Integer(1025) }; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider#
	 * getFigure(org.eclipse.birt.report.model.api.ReportItemHandle)
	 */
	public IFigure createFigure(ExtendedItemHandle handle) {
		IFigure figure = new LabelFigure();
		if (handle != null) {
			((LabelFigure) figure).setText(
					handle.getProperty(TEST_PROPERTY[0]) + ":" + handle.getProperty(TEST_PROPERTY[1]).toString());
		}
		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider#
	 * updateFigure(org.eclipse.birt.report.model.api.ReportItemHandle,
	 * org.eclipse.draw2d.IFigure)
	 */
	public void updateFigure(ExtendedItemHandle handle, IFigure figure) {
		Assert.isNotNull(handle);
		((LabelFigure) figure)
				.setText(handle.getProperty(TEST_PROPERTY[0]) + ":" + handle.getProperty(TEST_PROPERTY[1]).toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider#
	 * disposeFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 * org.eclipse.draw2d.IFigure)
	 */
	public void disposeFigure(ExtendedItemHandle handle, IFigure figure) {
	}

}