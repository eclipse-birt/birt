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

package org.eclipse.birt.chart.tests.engine.util;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;

public class TriggerSupportMatrixTest extends TestCase {

	private static final String HTML_PATH = "org/eclipse/birt/chart/tests/engine/util/TriggerSupportMatrix_golden.htm"; //$NON-NLS-1$

	private static final ActionType[] ACTION_TYPES = new ActionType[] { ActionType.URL_REDIRECT_LITERAL,
			ActionType.SHOW_TOOLTIP_LITERAL, ActionType.INVOKE_SCRIPT_LITERAL, ActionType.TOGGLE_VISIBILITY_LITERAL,
			ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL, ActionType.HIGHLIGHT_LITERAL,
			ActionType.CALL_BACK_LITERAL };

	private static final String SPLITOR = "\n"; //$NON-NLS-1$

	public void testMatrixAll() {
		StringBuffer sb = new StringBuffer();
		sb.append(new InteractivityTypeInnerTest(TriggerSupportMatrix.TYPE_DATAPOINT).getMatrixGeneratedString());
		sb.append(new InteractivityTypeInnerTest(TriggerSupportMatrix.TYPE_AXIS).getMatrixGeneratedString());
		sb.append(new InteractivityTypeInnerTest(TriggerSupportMatrix.TYPE_LEGEND).getMatrixGeneratedString());
		sb.append(new InteractivityTypeInnerTest(TriggerSupportMatrix.TYPE_CHARTTITLE).getMatrixGeneratedString());
		sb.append(new InteractivityTypeInnerTest(TriggerSupportMatrix.TYPE_CHARTAREA).getMatrixGeneratedString());
		sb.append(new InteractivityTypeInnerTest(TriggerSupportMatrix.TYPE_MARKERLINE).getMatrixGeneratedString());
		sb.append(new InteractivityTypeInnerTest(TriggerSupportMatrix.TYPE_MARKERRANGE).getMatrixGeneratedString());

		String[] populatedArray = sb.toString().split(SPLITOR);
		String[] goldenArray = getMatrixGoldenString().split(SPLITOR);

		assertEquals("check line of code", //$NON-NLS-1$
				goldenArray.length, populatedArray.length);

		if (goldenArray.length == populatedArray.length) {
			for (int i = 0; i < populatedArray.length; i++) {
				// Need to remove additional splitor in windows
				String golden = goldenArray[i].replaceAll("\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals("check line " + (i + 1) + ":", //$NON-NLS-1$ //$NON-NLS-2$
						golden, populatedArray[i]);
			}
		}
	}

	private static class InteractivityTypeInnerTest {

		private TriggerSupportMatrix matSVG = null;
		private TriggerSupportMatrix matSwing = null;

		private final int iInteractivityType;

		public InteractivityTypeInnerTest(int iInteractivityType) {
			this.iInteractivityType = iInteractivityType;
			matSVG = new TriggerSupportMatrix("svg", iInteractivityType); //$NON-NLS-1$
			matSwing = new TriggerSupportMatrix("png", iInteractivityType); //$NON-NLS-1$
		}

		public String getMatrixGeneratedString() {
			StringBuffer sb = new StringBuffer();
			// Add table headers
			sb.append("Type: " + getInteractivityTypeName() + SPLITOR);//$NON-NLS-1$
			sb.append("<table border=\"1\">" + SPLITOR); //$NON-NLS-1$
			sb.append(" <tr>" + SPLITOR); //$NON-NLS-1$
			printTd(sb, "&nbsp;"); //$NON-NLS-1$
			for (int i = 0; i < ACTION_TYPES.length; i++) {
				printTd(sb, ACTION_TYPES[i].getLiteral());
			}
			sb.append(" </tr>" + SPLITOR); //$NON-NLS-1$

			// Add table details
			printTriggerCondition(sb, TriggerCondition.ONCLICK_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONDBLCLICK_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONMOUSEDOWN_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONMOUSEUP_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONMOUSEOVER_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONMOUSEMOVE_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONMOUSEOUT_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONFOCUS_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONBLUR_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONKEYDOWN_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONKEYUP_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONKEYPRESS_LITERAL);
			printTriggerCondition(sb, TriggerCondition.ONLOAD_LITERAL);

			sb.append("</table>" + SPLITOR); //$NON-NLS-1$
			return sb.toString();
		}

		private void printTd(StringBuffer sb, String str) {
			sb.append("  <td>" + str + "</td>" + SPLITOR); //$NON-NLS-1$ //$NON-NLS-2$
		}

		private void printTriggerCondition(StringBuffer sb, TriggerCondition condition) {
			sb.append(" <tr>" + SPLITOR); //$NON-NLS-1$
			printTd(sb, condition.getLiteral());
			for (int i = 0; i < ACTION_TYPES.length; i++) {
				printTd(sb, getSupportRenderer(condition, ACTION_TYPES[i]));
			}
			sb.append(" </tr>" + SPLITOR); //$NON-NLS-1$
		}

		private String getSupportRenderer(TriggerCondition condition, ActionType actionType) {
			boolean supportSVG = matSVG.check(condition, actionType);
			boolean supportSwing = matSwing.check(condition, actionType);
			if (supportSVG && supportSwing) {
				return "All"; //$NON-NLS-1$
			}
			if (supportSVG) {
				return "SVG"; //$NON-NLS-1$
			}
			if (supportSwing) {
				return "Swing"; //$NON-NLS-1$
			}
			return "&nbsp;"; //$NON-NLS-1$
		}

		private String getInteractivityTypeName() {
			switch (iInteractivityType) {
			case TriggerSupportMatrix.TYPE_AXIS:
				return "Axis"; //$NON-NLS-1$
			case TriggerSupportMatrix.TYPE_CHARTAREA:
				return "Chart Area"; //$NON-NLS-1$
			case TriggerSupportMatrix.TYPE_CHARTTITLE:
				return "Chart Title"; //$NON-NLS-1$
			case TriggerSupportMatrix.TYPE_DATAPOINT:
				return "Data Point"; //$NON-NLS-1$
			case TriggerSupportMatrix.TYPE_LEGEND:
				return "Legend"; //$NON-NLS-1$
			case TriggerSupportMatrix.TYPE_MARKERLINE:
				return "Marker Line"; //$NON-NLS-1$
			case TriggerSupportMatrix.TYPE_MARKERRANGE:
				return "Marker Range"; //$NON-NLS-1$
			}
			return ""; //$NON-NLS-1$
		}
	}

	private String getMatrixGoldenString() {
		StringBuffer sb = new StringBuffer();
		try {
			InputStream is = TriggerSupportMatrixTest.class.getClassLoader().getResourceAsStream(HTML_PATH);
			int c = 0;
			while ((c = is.read()) != -1) {
				sb.append((char) c);
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public void testSupportedActionsDisplayName() {
		TriggerSupportMatrix matSVG = new TriggerSupportMatrix("svg", TriggerSupportMatrix.TYPE_DATAPOINT); //$NON-NLS-1$
		TriggerSupportMatrix matSwing = new TriggerSupportMatrix("png", TriggerSupportMatrix.TYPE_DATAPOINT); //$NON-NLS-1$

		String[] actions = matSVG.getSupportedActionsDisplayName(TriggerCondition.ONCLICK_LITERAL);
		assertEquals(6, actions.length);

		actions = matSwing.getSupportedActionsDisplayName(TriggerCondition.ONCLICK_LITERAL);
		assertEquals(2, actions.length);
		assertEquals(LiteralHelper.actionTypeSet.getDisplayNameByName(ActionType.URL_REDIRECT_LITERAL.getName()),
				actions[0]);
		assertEquals(LiteralHelper.actionTypeSet.getDisplayNameByName(ActionType.INVOKE_SCRIPT_LITERAL.getName()),
				actions[1]);

	}
}
