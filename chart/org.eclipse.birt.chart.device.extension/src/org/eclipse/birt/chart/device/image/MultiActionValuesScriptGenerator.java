/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.image;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.birt.chart.device.util.CSSHelper;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.MenuStylesKeyType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.MultipleActions;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.emf.common.util.EMap;

/**
 * The class generates JavaScript contents for multiple URL values.
 * 
 * @since 2.5
 */

public class MultiActionValuesScriptGenerator
{
	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/image" ); //$NON-NLS-1$
	
	private static String MENU_JS_CODE;
	
	/**
	 * Returns javascript content.
	 * 
	 * @return
	 */
	public static String getJSContent( ActionValue values )
	{
		StringBuilder sb = getJSContext( values );
		
		sb.append( "var popMenu = BirtChartMenuHelper.createPopupMenu(evt, menuInfo);\n");//$NON-NLS-1$
		sb.append( "popMenu.show();\n");//$NON-NLS-1$
		return sb.toString( );
	}

	public static String getJSContent( MultipleActions actions )
	{
		StringBuilder sb = getJSContext( actions );
		
		sb.append( "var popMenu = BirtChartMenuHelper.createPopupMenu(evt, menuInfo);\n");//$NON-NLS-1$
		sb.append( "popMenu.show();\n");//$NON-NLS-1$
		return sb.toString( );
	}
	
	/**
	 * Returns javascript key for current URL values.
	 * 
	 * @return
	 */
	public static String getJSKey( ActionValue values )
	{
		return getJSContext( values ).toString( );
	}
	
	public static String getJSKey( MultipleActions actions )
	{
		return getJSContext( actions ).toString( );
	}
	
	private static StringBuilder getJSContext( ActionValue values  )
	{
		StringBuilder sb = new StringBuilder( );
		if ( values instanceof MultiURLValues )
		{
			MultiURLValues muv = (MultiURLValues) values;
			sb.append( "\n\t var menuInfo = new BirtChartMenuInfo();\n" ); //$NON-NLS-1$

			EMap<String, String> propMap = muv.getPropertiesMap( );
			sb.append( getPropertiesJS( propMap ).toString( ) );
			int i = 0;
			for ( URLValue uv : getValidURLValues( muv ) )
			{
				sb = getURLValueJS( sb, i, uv );
				i++;
			}
		}

		appendInteractivityVariables( sb );

		return sb;
	}

	/**
	 * Appends chart variable values to scripts.
	 * 
	 * @param sb
	 */
	public static StringBuilder appendInteractivityVariables( StringBuilder sb )
	{
		sb.append( "\t if ( typeof categoryData != 'undefined' ) menuInfo.categoryData = categoryData;\n" );//$NON-NLS-1$
		sb.append( "\t if ( typeof valueData != 'undefined' ) menuInfo.valueData = valueData;\n" );//$NON-NLS-1$
		sb.append( "\t if ( typeof valueSeriesName != 'undefined' ) menuInfo.valueSeriesName = valueSeriesName;\n" );//$NON-NLS-1$

		sb.append( "\t if ( typeof legendItemText != 'undefined' ) menuInfo.legendItemText = legendItemText;\n" );//$NON-NLS-1$
		sb.append( "\t if ( typeof legendItemValue != 'undefined' ) menuInfo.legendItemValue = legendItemValue;\n" );//$NON-NLS-1$
		sb.append( "\t if ( typeof axisLabel != 'undefined' ) menuInfo.axisLabel = axisLabel;\n" );//$NON-NLS-1$
		
		sb.append( "\t if ( typeof id != 'undefined' ) menuInfo.id2 = id;\n");//$NON-NLS-1$
		sb.append( "\t if ( typeof compList != 'undefined' ) menuInfo.compList = compList;\n");//$NON-NLS-1$
		sb.append( "\t if ( typeof labelList != 'undefined' ) menuInfo.labelList = labelList;\n");//$NON-NLS-1$
		
		sb.append( "\t menuInfo.id = " + sb.toString( ).hashCode( ) + ";\n" );//$NON-NLS-1$//$NON-NLS-2$
		return sb;
	}
	
	private static StringBuilder getJSContext( MultipleActions actions )
	{
		StringBuilder sb = new StringBuilder( );
		sb.append( "\n\t var menuInfo = new BirtChartMenuInfo();\n" ); //$NON-NLS-1$

		EMap<String, String> propMap = actions.getPropertiesMap( );
		sb.append( getPropertiesJS( propMap ).toString( ) );

		int i = 0;
		for ( Action subAction : getValidActions( actions ) )
		{
			ActionValue av = subAction.getValue( );
			if ( av instanceof URLValue )
			{
				sb = getURLValueJS( sb, i, (URLValue) av );
			}
			else if ( av instanceof ScriptValue )
			{
				sb = getScriptValueJS( sb, i, (ScriptValue) av );
			}
			i++;
		}

		appendInteractivityVariables( sb );
		
		return sb;
	}

	/**
	 * @param sv
	 * @return
	 */
	public static StringBuilder getScriptValueJS( StringBuilder sb, int index, ScriptValue sv )
	{
		if ( index == 0 )
		{
			sb.append( "\t var mii = new BirtChartMenuItemInfo();\n" );//$NON-NLS-1$
		}
		else
		{
			sb.append( "\t mii = new BirtChartMenuItemInfo();\n" );//$NON-NLS-1$
		}

		sb.append( "\t mii.text = '" + sv.getLabel( ).getCaption( ).getValue( ) + "';\n" );//$NON-NLS-1$//$NON-NLS-2$
		sb.append( "\t mii.actionType = BirtChartInteractivityActions.INVOKE_SCRIPTS;\n" ); //$NON-NLS-1$
		String script = sv.getScript( );
		sb.append( "\t mii.actionValue = " + JavascriptEvalUtil.transformToJsExpression( script ) + ";\n" ); //$NON-NLS-1$//$NON-NLS-2$

		sb.append( "\t menuInfo.addItemInfo(mii);\n" ); //$NON-NLS-1$

		return sb;
	}
	 
	/**
	 * @param sv
	 * @return
	 */
	public static StringBuilder getVisualJS( StringBuilder sb, int index, ActionValue av, String scriptActionType )
	{
		if ( index == 0 )
		{
			sb.append( "\t var mii = new BirtChartMenuItemInfo();" );//$NON-NLS-1$
		}
		else
		{
			sb.append( "\t mii = new BirtChartMenuItemInfo();" );//$NON-NLS-1$
		}

		sb.append( "\t mii.text = '" + av.getLabel( ).getCaption( ).getValue( ) + "';\n" );//$NON-NLS-1$//$NON-NLS-2$
		sb.append( "\t mii.actionType = " + scriptActionType + ";\n" ); //$NON-NLS-1$ //$NON-NLS-2$

		sb.append( "\t menuInfo.addItemInfo(mii);\n" ); //$NON-NLS-1$

		return sb;
	}
	
	/**
	 * @param index
	 * @param uv
	 * @return
	 */
	public static StringBuilder getURLValueJS( StringBuilder sb, int index, URLValue uv )
	{
		if ( index == 0 )
		{
			sb.append( "\t var mii = new BirtChartMenuItemInfo();\n" );//$NON-NLS-1$
		}
		else
		{
			sb.append( "\t mii = new BirtChartMenuItemInfo();\n" );//$NON-NLS-1$
		}
		sb.append( "\t mii.text = '" + uv.getLabel( ).getCaption( ).getValue( ) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
		String url = uv.getBaseUrl( );
		if ( !( url.startsWith( "\"" ) || url.endsWith( "\"" ) ) )//$NON-NLS-1$ //$NON-NLS-2$
		{
			url = "\"" + url + "\"";//$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append( "\t mii.actionType = BirtChartInteractivityActions.HYPER_LINK;\n"); //$NON-NLS-1$
		sb.append( "\t mii.actionValue = " + url + ";\n"); //$NON-NLS-1$//$NON-NLS-2$
		
		String target = uv.getTarget( ) == null ? "" : uv.getTarget( );//$NON-NLS-1$
		sb.append( "\t mii.target = '" + target + "';\n"); //$NON-NLS-1$//$NON-NLS-2$
		if ( uv.getTooltip( ) != null
				&& uv.getTooltip( ).trim( ).length( ) > 0 )
		{
			if ( uv.getTooltip( ).startsWith( "\"" ) || uv.getTooltip( ).startsWith( "'" ) ) //$NON-NLS-1$//$NON-NLS-2$
				sb.append( "\t mii.tooltip = " + uv.getTooltip( ) + ";\n"); //$NON-NLS-1$//$NON-NLS-2$
			else
				sb.append( "\t mii.tooltip = \"" + uv.getTooltip( ) + "\";\n"); //$NON-NLS-1$//$NON-NLS-2$
		}
		
		sb.append ( "\t menuInfo.addItemInfo(mii);\n");  //$NON-NLS-1$
		
		return sb;
	}

	/**
	 * @param sb
	 * @param propMap
	 */
	private static StringBuilder getPropertiesJS( 
			EMap<String, String> propMap )
	{
		StringBuilder sb = new StringBuilder();
		for ( Entry<String, String> entry : propMap.entrySet( ) )
		{
			String key = entry.getKey();
			String properties = entry.getValue( );
			if ( MenuStylesKeyType.MENU.getName().equals( key ) )
			{
				if ( !properties.matches( ".*position[ ]*:.*" )) //$NON-NLS-1$
				{
					properties = properties + ";position:absolute"; //$NON-NLS-1$
				}
				sb.append( "\t menuInfo.menuStyles = '" + CSSHelper.getStylingNonHyphenFormat( properties ) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			}
			else if ( MenuStylesKeyType.MENU_ITEM.getName( ).equals( key  ) )
			{
				if ( !properties.matches( ".*cursor[ ]*:.*" )) //$NON-NLS-1$
				{
					properties = properties + ";cursor:default"; //$NON-NLS-1$
				}
				sb.append( "\t menuInfo.menuItemStyles = '" + CSSHelper.getStylingNonHyphenFormat( properties ) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			}
			else if ( MenuStylesKeyType.ON_MOUSE_OVER.getName( ).equals(key))
			{
				sb.append( "\t menuInfo.mouseOverStyles = '" + CSSHelper.getStylingNonHyphenFormat( properties ) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			}
			else if (MenuStylesKeyType.ON_MOUSE_OUT.getName( ).equals(key)) 
			{
				sb.append( "\tmenuInfo.mouseOutStyles = '" + CSSHelper.getStylingNonHyphenFormat( properties )  + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			}
		}
		return sb;
	}

	/**
	 * @param multiUrlValue
	 * @return
	 */
	public static List<URLValue> getValidURLValues( MultiURLValues multiUrlValue )
	{
		List<URLValue> validURLValues = new ArrayList<URLValue>( );
		if ( multiUrlValue == null )
		{
			return validURLValues;
		}
		
		for ( URLValue uv : multiUrlValue.getURLValues( ) )
		{
			if ( uv.getBaseUrl( ) == null
					|| uv.getBaseUrl( ).length( ) <= 0
					|| uv.getBaseUrl( ).equals( "\"\"" ) )//$NON-NLS-1$
			{
				continue;
			}
			validURLValues.add( uv );
		}
		return validURLValues;
	}
	
	/**
	 * @param multiActions
	 * @return
	 */
	public static List<Action> getValidActions( MultipleActions multiActions )
	{
		List<Action> validActions = new ArrayList<Action>( );
		if ( multiActions == null )
		{
			return validActions;
		}
		
		for ( Action subAction : multiActions.getActions( ))
		{
			ActionValue av = subAction.getValue( );
			if ( av instanceof URLValue )
			{
				URLValue uv = (URLValue) av;
				if ( uv.getBaseUrl( ) == null
						|| uv.getBaseUrl( ).length( ) <= 0
						|| uv.getBaseUrl( ).equals( "\"\"" ) )//$NON-NLS-1$
				{
					continue;
				}

			}
			else if ( av instanceof ScriptValue )
			{
				ScriptValue sv = (ScriptValue) av;
				if ( sv.getScript( ) == null
						|| sv.getScript( ).length( ) == 0
						|| sv.getScript( ).equals( "\"\"" ) )//$NON-NLS-1$
				{
					continue;
				}
			}

			validActions.add( subAction );
		}
		return validActions;
	}
	
	public static String getBirtChartMenuLib( )
	{
		if ( MENU_JS_CODE == null )
		{
			StringBuilder sb = new StringBuilder( );
			try
			{
				InputStream is = MultiActionValuesScriptGenerator.class.getResourceAsStream( "/org/eclipse/birt/chart/device/util/ImageActionMenu.js" ); //$NON-NLS-1$
				BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
				String s = null;
				while ( true )
				{
					s = br.readLine( );
					if ( s == null )
					{
						break;
					}
					sb.append( s );
					sb.append( "\n" ); //$NON-NLS-1$
				}
				br.close( );
			}
			catch ( FileNotFoundException e )
			{
				logger.log( e );
			}
			catch ( IOException e )
			{
				logger.log( e );
			}

			MENU_JS_CODE = sb.toString( );
		}
		return MENU_JS_CODE;
	}
}
