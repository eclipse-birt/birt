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
package org.eclipse.birt.report.designer.core.commands;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.designer.util.MetricUtility;

/**
 * @author xzhang
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class MoveGuideCommandTest extends BaseTestCase {
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		getReportDesignHandle().setDefaultUnits(DesignChoiceConstants.UNITS_IN);
	}

	public void testMoveGuildRightMargin() {

		int rightMargin = 100;
		MasterPageHandle page = SessionHandleAdapter.getInstance().getMasterPageHandle();

		MoveGuideCommand moveGuideCmd = new MoveGuideCommand(rightMargin, MasterPageHandle.RIGHT_MARGIN_PROP);
		moveGuideCmd.execute();

		assertTrue(page.getRightMargin().getUnits().equals(DesignChoiceConstants.UNITS_IN));
		double value = MetricUtility.inchToPixel(page.getRightMargin().getMeasure());
		assertEquals((int) value, rightMargin);

	}

	public void testMoveGuildLeftMargin() throws SemanticException {

		int leftMargin = 200;
		MasterPageHandle page = SessionHandleAdapter.getInstance().getMasterPageHandle();
		getReportDesignHandle().setDefaultUnits(DesignChoiceConstants.UNITS_CM);

		MoveGuideCommand moveGuideCmd = new MoveGuideCommand(leftMargin, MasterPageHandle.LEFT_MARGIN_PROP);
		moveGuideCmd.execute();

		DimensionValue dim = page.getLeftMargin().getAbsoluteValue();
		assertTrue(dim.getUnits().equals(DesignChoiceConstants.UNITS_CM));
		DimensionValue dimInch = DimensionUtil.convertTo(dim, DesignChoiceConstants.UNITS_CM,
				DesignChoiceConstants.UNITS_IN);
		double value = MetricUtility.inchToPixel(dimInch.getMeasure());
		assertEquals((int) value, leftMargin);

	}

	public void testMoveGuildTopMargin() {

		int topMargin = 300;
		MasterPageHandle page = SessionHandleAdapter.getInstance().getMasterPageHandle();

		MoveGuideCommand moveGuideCmd = new MoveGuideCommand(topMargin, MasterPageHandle.TOP_MARGIN_PROP);
		moveGuideCmd.execute();

		assertTrue(page.getTopMargin().getUnits().equals(DesignChoiceConstants.UNITS_IN));
		double value = MetricUtility.inchToPixel(page.getTopMargin().getMeasure());
		assertEquals((int) value, topMargin);
	}

	public void testMoveGuildBottomMargin() {

		int bottomMargin = 400;
		MasterPageHandle page = SessionHandleAdapter.getInstance().getMasterPageHandle();

		MoveGuideCommand moveGuideCmd = new MoveGuideCommand(bottomMargin, MasterPageHandle.BOTTOM_MARGIN_PROP);
		moveGuideCmd.execute();

		assertTrue(page.getBottomMargin().getUnits().equals(DesignChoiceConstants.UNITS_IN));
		double value = MetricUtility.inchToPixel(page.getBottomMargin().getMeasure());
		assertEquals((int) value, bottomMargin);
	}

	public void testMoveGuildLittleBottomMargin() {

		int bottomMargin = 1;
		MasterPageHandle page = SessionHandleAdapter.getInstance().getMasterPageHandle();

		MoveGuideCommand moveGuideCmd = new MoveGuideCommand(bottomMargin, MasterPageHandle.BOTTOM_MARGIN_PROP);
		moveGuideCmd.execute();

		assertTrue(page.getBottomMargin().getUnits().equals(DesignChoiceConstants.UNITS_IN));
		double value = MetricUtility.inchToPixel(page.getBottomMargin().getMeasure());
		assertEquals((int) value, bottomMargin);
	}

	public void testMoveGuildLargeBottomMargin() {

		int bottomMargin = 0x7fffffff;
		MasterPageHandle page = SessionHandleAdapter.getInstance().getMasterPageHandle();

		MoveGuideCommand moveGuideCmd = new MoveGuideCommand(bottomMargin, MasterPageHandle.BOTTOM_MARGIN_PROP);
		moveGuideCmd.execute();

		assertTrue(page.getBottomMargin().getUnits().equals(DesignChoiceConstants.UNITS_IN));
		double value = MetricUtility.inchToPixel(page.getBottomMargin().getMeasure());
		assertEquals((int) value, bottomMargin);
	}

	public void testMoveGuildNegtiveBottomMargin() {

		int bottomMargin = -100;
		MasterPageHandle page = SessionHandleAdapter.getInstance().getMasterPageHandle();

		MoveGuideCommand moveGuideCmd = new MoveGuideCommand(bottomMargin, MasterPageHandle.BOTTOM_MARGIN_PROP);
		moveGuideCmd.execute();

		assertTrue(page.getBottomMargin().getUnits().equals(DesignChoiceConstants.UNITS_IN));
		double value = MetricUtility.inchToPixel(page.getBottomMargin().getMeasure());
		assertEquals((int) value, 0);
	}

}