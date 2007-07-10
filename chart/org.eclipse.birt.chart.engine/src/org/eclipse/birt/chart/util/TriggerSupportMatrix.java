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
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Trigger;

/**
 * Provides a utility class to check supported Trigger combination of condition
 * event and action type.
 */

public class TriggerSupportMatrix
{

	// Support criteria
	private static final int SVG = 1;
	private static final int SWING = 2;
	private static final int ALL = SVG | SWING;

	static class TriggerCombination
	{

		private final TriggerCondition condition;
		private final ActionType actionType;
		private final int renderer;

		TriggerCombination( TriggerCondition condition, ActionType actionType )
		{
			this( condition, actionType, ALL );
		}

		TriggerCombination( TriggerCondition tCondition, ActionType actionType,
				int iRenderer )
		{
			this.condition = tCondition;
			this.actionType = actionType;
			this.renderer = iRenderer;
		}

		/**
		 * Tests if current trigger condition is supported in this combination.
		 * 
		 * @param tCondition
		 * @param iRenderer
		 * @return
		 */
		public boolean test( TriggerCondition tCondition, int iRenderer )
		{
			return this.condition == tCondition
					&& ( this.renderer & iRenderer ) == iRenderer;
		}

		public ActionType getActionType( )
		{
			return this.actionType;
		}
	}

	private static List supportedTriggers = new ArrayList( );
	static
	{
		// click
		addTriggersLikeOnclick( TriggerCondition.ONCLICK_LITERAL );

		// double click (like click)
		addTriggersLikeOnclick( TriggerCondition.ONDBLCLICK_LITERAL );

		// focus (like click)
		addTriggersLikeOnclick( TriggerCondition.ONFOCUS_LITERAL );

		// blur (like click)
		addTriggersLikeOnclick( TriggerCondition.ONBLUR_LITERAL );

		// mouse over
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionType.SHOW_TOOLTIP_LITERAL,
				SWING ) );
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONMOUSEOVER_LITERAL,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SWING ) );
		addTriggersLikeMouseDown( TriggerCondition.ONMOUSEOVER_LITERAL );

		// mouse down
		addTriggersLikeMouseDown( TriggerCondition.ONMOUSEDOWN_LITERAL );

		// mouse up
		addTriggersLikeMouseDown( TriggerCondition.ONMOUSEUP_LITERAL );

		// mouse move
		addTriggersLikeMouseDown( TriggerCondition.ONMOUSEMOVE_LITERAL );

		// mouse out
		addTriggersLikeMouseDown( TriggerCondition.ONMOUSEOUT_LITERAL );

		// key down
		addTriggersLikeMouseDown( TriggerCondition.ONKEYDOWN_LITERAL );

		// key up
		addTriggersLikeMouseDown( TriggerCondition.ONKEYUP_LITERAL );

		// key press
		addTriggersLikeMouseDown( TriggerCondition.ONKEYPRESS_LITERAL );

		// load
		addTriggersLikeMouseDown( TriggerCondition.ONLOAD_LITERAL );
	};

	private static void addTriggersLikeOnclick( TriggerCondition condition )
	{
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.URL_REDIRECT_LITERAL ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.INVOKE_SCRIPT_LITERAL ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.SHOW_TOOLTIP_LITERAL,
				SVG ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.HIGHLIGHT_LITERAL,
				SVG ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.TOGGLE_VISIBILITY_LITERAL,
				SVG ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL,
				SVG ) );
	}

	private static void addTriggersLikeMouseDown( TriggerCondition condition )
	{
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.URL_REDIRECT_LITERAL,
				SVG ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.SHOW_TOOLTIP_LITERAL,
				SVG ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SVG ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.HIGHLIGHT_LITERAL,
				SVG ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.TOGGLE_VISIBILITY_LITERAL,
				SVG ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL,
				SVG ) );
	}

	private final int iRenderer;
	private final boolean isDataPointEnabled;

	/**
	 * Constructor.
	 * 
	 * @param outputFormat
	 *            output type of chart renderer
	 * @param isDataPointEnabled
	 *            if the data point visibility can be enabled
	 */
	public TriggerSupportMatrix( String outputFormat, boolean isDataPointEnabled )
	{
		this.iRenderer = "SVG".equalsIgnoreCase( outputFormat ) ? SVG : SWING; //$NON-NLS-1$
		this.isDataPointEnabled = isDataPointEnabled;
	}

	/**
	 * Gets supported Action types according to current trigger condition.
	 * 
	 * @param condition
	 *            event condition
	 * @return a string array of supported action type display names
	 */
	public String[] getSupportedActionsDisplayName( TriggerCondition condition )
	{
		List actions = new ArrayList( );
		for ( int i = 0; i < supportedTriggers.size( ); i++ )
		{
			TriggerCombination tc = (TriggerCombination) supportedTriggers.get( i );
			// Tests if current trigger condition is supported in this
			// combination
			if ( tc.test( condition, iRenderer )
					&& isDPEnabled( tc.getActionType( ) ) )
			{
				actions.add( LiteralHelper.actionTypeSet.getDisplayNameByName( tc.getActionType( )
						.getName( ) ) );
			}
		}
		return (String[]) actions.toArray( new String[actions.size( )] );
	}

	/**
	 * Checks the trigger combination of condition and action type is supported
	 * in current renderer.
	 * 
	 * @param trigger
	 * @return
	 */
	public boolean check( Trigger trigger )
	{
		if ( trigger != null )
		{
			return check( trigger.getCondition( ), trigger.getAction( )
					.getType( ) );
		}
		return false;
	}

	/**
	 * Checks the trigger combination of condition and action type is supported
	 * in current renderer.
	 * 
	 * @param condition
	 * @param actionType
	 * @return
	 */
	public boolean check( TriggerCondition condition, ActionType actionType )
	{
		for ( int i = 0; i < supportedTriggers.size( ); i++ )
		{
			TriggerCombination tc = (TriggerCombination) supportedTriggers.get( i );
			if ( tc.test( condition, iRenderer ) )
			{
				if ( tc.getActionType( ) == actionType
						&& isDPEnabled( actionType ) )
				{
					return true;
				}
			}
		}
		return false;
	}

	private boolean isDPEnabled( ActionType actionType )
	{
		return isDataPointEnabled
				|| actionType != ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL;
	}
}
