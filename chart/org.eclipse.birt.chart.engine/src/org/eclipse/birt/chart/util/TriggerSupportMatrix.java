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
 * event and action type. Matrix is below: 
 <table border="1">
 <tr>
  <td>&nbsp;</td>
  <td>URL_Redirect</td>
  <td>Show_Tooltip</td>
  <td>Invoke_Script</td>
  <td>Toogle_Visibility</td>
  <td>Toogle_DataPoint_Visibility</td>
  <td>Hilight</td>
  <td>Callback</td>
 </tr>
 <tr>
  <td>Click</td>
  <td>All</td>
  <td>&nbsp;</td>
  <td>All</td>
  <td>SVG</td>
  <td>SVG</td>
  <td>SVG</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Double Click</td>
  <td>All</td>
  <td>&nbsp;</td>
  <td>All</td>
  <td>SVG</td>
  <td>SVG</td>
  <td>SVG</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Mouse Down</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>SVG</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Mouse UP</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>SVG</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Mouse Over</td>
  <td>&nbsp;</td>
  <td>All</td>
  <td>SVG</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Mouse Move</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>SVG</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Mouse Out</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>SVG</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Focus</td>
  <td>All</td>
  <td>&nbsp;</td>
  <td>All</td>
  <td>SVG</td>
  <td>SVG</td>
  <td>SVG</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Blur</td>
  <td>All</td>
  <td>&nbsp;</td>
  <td>All</td>
  <td>SVG</td>
  <td>SVG</td>
  <td>SVG</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Key Down</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>SVG</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Key Up</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>SVG</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Key Press</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>SVG</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Onload</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>SVG</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
 </tr>
</table>
 */

public class TriggerSupportMatrix
{

	// Supported renderer
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
				ActionType.SHOW_TOOLTIP_LITERAL ) );

		// mouse down
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONMOUSEDOWN_LITERAL,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SVG ) );

		// mouse up
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONMOUSEUP_LITERAL,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SVG ) );

		// mouse move
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONMOUSEMOVE_LITERAL,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SVG ) );

		// mouse out
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONMOUSEOUT_LITERAL,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SVG ) );

		// key down
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONKEYDOWN_LITERAL,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SVG ) );

		// key up
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONKEYUP_LITERAL,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SVG ) );

		// key press
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONKEYPRESS_LITERAL,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SVG ) );

		// load
		supportedTriggers.add( new TriggerCombination( TriggerCondition.ONLOAD_LITERAL,
				ActionType.INVOKE_SCRIPT_LITERAL,
				SVG ) );
	};

	private static void addTriggersLikeOnclick( TriggerCondition condition )
	{
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.URL_REDIRECT_LITERAL ) );
		supportedTriggers.add( new TriggerCombination( condition,
				ActionType.INVOKE_SCRIPT_LITERAL ) );
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

	/**
	 * Constructor.
	 * 
	 * @param outputFormat
	 *            output type of chart renderer
	 */
	public TriggerSupportMatrix( String outputFormat )
	{
		this.iRenderer = "SVG".equalsIgnoreCase( outputFormat ) ? SVG : SWING; //$NON-NLS-1$
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
			if ( tc.test( condition, iRenderer ) )
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
			for ( int i = 0; i < supportedTriggers.size( ); i++ )
			{
				TriggerCombination tc = (TriggerCombination) supportedTriggers.get( i );
				if ( tc.test( trigger.getCondition( ), iRenderer ) )
				{
					if ( tc.getActionType( ) == trigger.getAction( ).getType( ) )
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}
