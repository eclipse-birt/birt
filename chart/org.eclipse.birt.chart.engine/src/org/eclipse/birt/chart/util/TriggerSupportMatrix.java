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

package org.eclipse.birt.chart.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Trigger;

/**
 * Provides a utility class to check supported Trigger combination of condition
 * event and action type.
 */

public class TriggerSupportMatrix {

	// Supported renderer
	protected static final int SVG = 1;
	protected static final int SWING = 2;
	protected static final int ALL = 127;

	// Supported interactivity type
	public static final int TYPE_DATAPOINT = 1;
	public static final int TYPE_CHARTTITLE = 2;
	public static final int TYPE_CHARTAREA = 4;
	public static final int TYPE_AXIS = 8;
	public static final int TYPE_LEGEND = 16;
	public static final int TYPE_MARKERLINE = 32;
	public static final int TYPE_MARKERRANGE = 64;
	private static final int TYPE_ALL = 127;

	// These constants indicate supported interactivity types for certain action
	// type
	private static final int VALID_TYPES_HIGHLIGHT = TYPE_DATAPOINT | TYPE_AXIS | TYPE_LEGEND;
	private static final int VALID_TYPES_TOOGLE_VISIBILITY = TYPE_DATAPOINT | TYPE_AXIS | TYPE_LEGEND | TYPE_CHARTTITLE;
	private static final int VALID_TYPES_TOOGLE_DP_VISIBILITY = TYPE_DATAPOINT;

	protected static class TriggerCombination {

		private final TriggerCondition condition;
		private final ActionType actionType;
		private final int renderer;
		private final int type;

		public TriggerCombination(TriggerCondition condition, ActionType actionType) {
			this(condition, actionType, ALL, TYPE_ALL);
		}

		public TriggerCombination(TriggerCondition condition, ActionType actionType, int iRenderer) {
			this(condition, actionType, iRenderer, TYPE_ALL);
		}

		public TriggerCombination(TriggerCondition tCondition, ActionType actionType, int iRenderer, int iType) {
			this.condition = tCondition;
			this.actionType = actionType;
			this.renderer = iRenderer;
			this.type = iType;
		}

		/**
		 * Tests if current trigger condition is supported in this combination.
		 * 
		 * @param tCondition
		 * @param iRenderer
		 * @param iType
		 * @return supported result
		 */
		public boolean test(TriggerCondition tCondition, int iRenderer, int iType) {
			return this.condition == tCondition && (this.renderer & iRenderer) == iRenderer
					&& (this.type & iType) == iType;
		}

		public ActionType getActionType() {
			return this.actionType;
		}
	}

	protected static List<TriggerCombination> supportedTriggers = new ArrayList<TriggerCombination>();
	static {
		// click
		addTriggersLikeOnclick(TriggerCondition.ONCLICK_LITERAL);

		// double click (like click)
		addTriggersLikeOnclick(TriggerCondition.ONDBLCLICK_LITERAL);

		// focus (like click)
		addTriggersLikeOnclick(TriggerCondition.ONFOCUS_LITERAL);

		// blur (like click)
		addTriggersLikeOnclick(TriggerCondition.ONBLUR_LITERAL);

		// mouse over
		supportedTriggers.add(
				new TriggerCombination(TriggerCondition.ONMOUSEOVER_LITERAL, ActionType.SHOW_TOOLTIP_LITERAL, SWING));
		supportedTriggers.add(
				new TriggerCombination(TriggerCondition.ONMOUSEOVER_LITERAL, ActionType.INVOKE_SCRIPT_LITERAL, SWING));
		addTriggersLikeMouseDown(TriggerCondition.ONMOUSEOVER_LITERAL);

		// mouse down
		addTriggersLikeMouseDown(TriggerCondition.ONMOUSEDOWN_LITERAL);

		// mouse up
		addTriggersLikeMouseDown(TriggerCondition.ONMOUSEUP_LITERAL);

		// mouse move
		addTriggersLikeMouseDown(TriggerCondition.ONMOUSEMOVE_LITERAL);

		// mouse out
		addTriggersLikeMouseDown(TriggerCondition.ONMOUSEOUT_LITERAL);

		// key down
		addTriggersLikeMouseDown(TriggerCondition.ONKEYDOWN_LITERAL);

		// key up
		addTriggersLikeMouseDown(TriggerCondition.ONKEYUP_LITERAL);

		// key press
		addTriggersLikeMouseDown(TriggerCondition.ONKEYPRESS_LITERAL);

		// load
		addTriggersLikeMouseDown(TriggerCondition.ONLOAD_LITERAL);
	};

	private static void addTriggersLikeOnclick(TriggerCondition condition) {
		supportedTriggers.add(new TriggerCombination(condition, ActionType.URL_REDIRECT_LITERAL));
		supportedTriggers.add(new TriggerCombination(condition, ActionType.INVOKE_SCRIPT_LITERAL));
		supportedTriggers.add(new TriggerCombination(condition, ActionType.SHOW_TOOLTIP_LITERAL, SVG));
		supportedTriggers
				.add(new TriggerCombination(condition, ActionType.HIGHLIGHT_LITERAL, SVG, VALID_TYPES_HIGHLIGHT));
		supportedTriggers.add(new TriggerCombination(condition, ActionType.TOGGLE_VISIBILITY_LITERAL, SVG,
				VALID_TYPES_TOOGLE_VISIBILITY));
		supportedTriggers.add(new TriggerCombination(condition, ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL, SVG,
				VALID_TYPES_TOOGLE_DP_VISIBILITY));
	}

	private static void addTriggersLikeMouseDown(TriggerCondition condition) {
		supportedTriggers.add(new TriggerCombination(condition, ActionType.URL_REDIRECT_LITERAL, SVG));
		supportedTriggers.add(new TriggerCombination(condition, ActionType.SHOW_TOOLTIP_LITERAL, SVG));
		supportedTriggers.add(new TriggerCombination(condition, ActionType.INVOKE_SCRIPT_LITERAL, SVG));
		supportedTriggers
				.add(new TriggerCombination(condition, ActionType.HIGHLIGHT_LITERAL, SVG, VALID_TYPES_HIGHLIGHT));
		supportedTriggers.add(new TriggerCombination(condition, ActionType.TOGGLE_VISIBILITY_LITERAL, SVG,
				VALID_TYPES_TOOGLE_VISIBILITY));
		supportedTriggers.add(new TriggerCombination(condition, ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL, SVG,
				VALID_TYPES_TOOGLE_DP_VISIBILITY));
	}

	protected int iRenderer;
	protected final int iType;

	/**
	 * Constructor.
	 * 
	 * @param outputFormat output type of chart renderer
	 * @param iType        the type of interactivity support
	 */
	public TriggerSupportMatrix(String outputFormat, int iInteractivityType) {
		this.iRenderer = "SVG".equalsIgnoreCase(outputFormat) ? SVG : SWING; //$NON-NLS-1$
		this.iType = iInteractivityType;
	}

	/**
	 * Returns the interactivity type
	 * 
	 * @return interactivity type
	 */
	public int getType() {
		return this.iType;
	}

	/**
	 * Gets supported Action types according to current trigger condition.
	 * 
	 * @param condition event condition
	 * @return a string array of supported action type display names
	 */
	public String[] getSupportedActionsDisplayName(TriggerCondition condition) {
		List<String> actions = new ArrayList<String>();
		for (TriggerCombination tc : supportedTriggers) {
			// Tests if current trigger condition is supported in this
			// combination
			if (tc.test(condition, iRenderer, iType)) {
				actions.add(LiteralHelper.actionTypeSet.getDisplayNameByName(tc.getActionType().getName()));
			}
		}
		return actions.toArray(new String[actions.size()]);
	}

	/**
	 * Checks the trigger combination of condition and action type is supported in
	 * current renderer.
	 * 
	 * @param trigger
	 * @return supported result
	 */
	public boolean check(Trigger trigger) {
		if (trigger != null) {
			return check(trigger.getCondition(), trigger.getAction().getType());
		}
		return false;
	}

	/**
	 * Checks the trigger combination of condition and action type is supported in
	 * current renderer.
	 * 
	 * @param condition
	 * @param actionType
	 * @return supported result
	 */
	public boolean check(TriggerCondition condition, ActionType actionType) {
		for (int i = 0; i < supportedTriggers.size(); i++) {
			TriggerCombination tc = supportedTriggers.get(i);
			if (tc.test(condition, iRenderer, iType)) {
				if (tc.getActionType() == actionType) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns supported trigger conditions for specified type of interactivity
	 * triggers.
	 * 
	 * @return supported trigger conditions. Null means no filter.
	 */
	public TriggerCondition[] getConditionFilters() {
		return null;
	}

	/**
	 * Returns supported cursor for specified type of interactivity triggers.
	 * 
	 * @return supported cursor array. Null means no filter.
	 */
	public CursorType[] getCursorFilters() {
		return null;
	}
}
