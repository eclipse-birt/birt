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

package org.eclipse.birt.report.engine.executor;

import java.util.Date;
import java.util.logging.Level;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.engine.content.impl.ReportElementContent;
import org.eclipse.birt.report.engine.content.impl.ReportItemContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.StyledElementContent;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.HighlightRuleDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.MapRuleDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.StyleDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.eclipse.birt.report.engine.ir.VisibilityRuleDesign;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.elements.Style;

/**
 * Defines an abstract base class for all styled element executors, including
 * <code>DataItemExecutor</code>,<code>TextItemExecutor</code>, etc.. The
 * class provides methods for style manipulation, such as applying highlight and
 * mapping rules, calculating flattened (merged) styles, and so on.
 * 
 * @version $Revision: 1.15 $ $Date: 2005/05/11 08:18:32 $
 */
public abstract class StyledItemExecutor extends ReportItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param context
	 *            the execution context.
	 * @param visitor
	 *            the visitor for report execution
	 */
	protected StyledItemExecutor( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/**
	 * Gets the style from the original design object, calculates the highlight
	 * style, merges the teo styles and then sets them on the corresponding
	 * content object.
	 * 
	 * @param content
	 *            the target content object.
	 * @param design
	 *            the original design object.
	 */
	protected void setStyles( StyledElementContent content,
			StyledElementDesign design )
	{
		StyleDesign style = design.getStyle( );
		StyleDesign highlightStyle;
		StyleDesign mergedStyle;

		assert style != null;

		content.setStyle( style );

		handleBackgroundImage( style );

		highlightStyle = getStyleFromHighlight( design );
		if ( highlightStyle != null && highlightStyle.entrySet( ).size( ) > 0 )
		{
			handleBackgroundImage( highlightStyle );
			content.setHighlightStyle( highlightStyle );
			mergedStyle = style.mergeWithInlineStyle( highlightStyle );
		}
		else
		{
			mergedStyle = style;
		}

		content.setMergedStyle( mergedStyle );
	}

	/**
	 * Get the real style of the report item.
	 * 
	 * @param item
	 *            The report item design with style.
	 * @return The real style of the report item for presentation.
	 */
	protected StyleDesign getActualStyle( StyledElementDesign item )
	{
		//TODO performance may be enhanced in this function. Also we only
		// handle the highlight of this item's style.
		StyleDesign style = new StyleDesign( );
		//Expression defaultTestExp = null;
		//if ( item instanceof DataItemDesign )
		//{
		//	defaultTestExp = ( (DataItemDesign) item ).getValue( );
		//}
		style = mergeStyle( style, item );
		handleBackgroundImage( style );
		//selector style
		//		StyleDesign selectorStyle=null;
		//		mergeStyle(style,selectorStyle);
		//		//named style
		//		StyleDesign namedStyle=null;
		//		mergeStyle(style,namedStyle);
		//		//inline style
		//		StyleDesign inlineStyle=null;
		//		mergeStyle(style,inlineStyle);

		return style;
	}

	/**
	 * Merges the style and the highlight style.
	 * 
	 * @param mergingStyle
	 *            The style merging with others.
	 * @param mergedStyle
	 *            The merged style with highlight style.
	 * @return The style after merging.
	 */
	private StyleDesign mergeStyle( StyleDesign mergingStyle,
			StyledElementDesign item )
	{
		if ( item.getStyle( ) != null )
		{
			mergingStyle = mergingStyle.mergeWithInlineStyle( item.getStyle( ) );
			StyleDesign highlightStyle = getStyleFromHighlight( item );
			if ( highlightStyle != null )
			{
				mergingStyle = mergingStyle
						.mergeWithInlineStyle( highlightStyle );
			}
		}
		return mergingStyle;
	}

	/**
	 * Get the highlight style.
	 * 
	 * @param style
	 *            The style with highlight.
	 * @param defaultTestExp
	 *            the test expression
	 * @return The highlight style.
	 */
	private StyleDesign getStyleFromHighlight( StyledElementDesign item )
	{

		HighlightDesign highlight = item.getHighlight( );
		if ( highlight == null )
		{
			return null;
		}

		StyleDesign style = null;
		for ( int i = 0; i < highlight.getRuleCount( ); i++ )
		{
			HighlightRuleDesign rule = highlight.getRule( i ) ;
			if ( rule != null )
			{
				Object value = context.evaluate( rule.getConditionExpr( ) );
				if ( ( value != null ) && ( value instanceof Boolean )
						&& ( ( (Boolean) value ).booleanValue( ) ) )
				{
					if (style == null)
					{
						style = new StyleDesign();
					}
					style = style.mergeWithInlineStyle(rule.getStyle());
				}
			}
		}
		return style;
	}

	/**
	 * Get the mapped value.
	 * 
	 * @param oldVal
	 *            the old value.
	 * @param style
	 *            the style with map.
	 * @param defaultTestExp
	 *            the default test expression and only useful for data item
	 * @return the old value if no map rule matched, the mapping value otherwise
	 */
	protected Object getMapVal( Object oldVal, StyledElementDesign item )
	{
		MapDesign map = item.getMap( );
		if ( map == null )
		{
			return oldVal;
		}

		for ( int i = 0; i < map.getRuleCount( ); i++ )
		{
			MapRuleDesign rule = map.getRule( i );
			if ( rule != null )
			{
				Object value = context.evaluate( rule.getConditionExpr( ) );
				if ( ( value != null ) && ( value instanceof Boolean )
						&& ( ( (Boolean) value ).booleanValue( ) ) )
				{
					return getLocalizedString( rule.getDisplayKey( ), rule
							.getDisplayText( ) );
				}
			}
		}
		return oldVal;
	}

	/**
	 * Uses proper format pattern to format input value.
	 * 
	 * @param value
	 *            The input value.
	 * @param formatStr
	 *            The default format pattern string. If it's <code>null</code>,
	 *            it will use the corresponding format pattern set in the
	 *            <code>StyleDesign</code> object instead.
	 * @param style
	 *            The <code>StyleDesign</code> object.
	 * @param formattedStr
	 *            The <code>StringBuffer</code> object to which the formatted
	 *            string will be appended.
	 */
	protected void formatValue( Object value, String formatStr,
			StyleDesign style, StringBuffer formattedStr,ReportElementContent reportContent )
	{
		if ( value == null )
		{
			return;
		}

		assert style != null && formattedStr != null;

		if ( ( value instanceof Number ) )
		{
			if ( formatStr == null || formatStr.length( ) == 0 )
			{
				formatStr = style.getNumberFormat( );
			}

			NumberFormatter numberFormat = context.createNumberFormatter( formatStr );
			formattedStr.append( numberFormat.format( ( ( Number ) value ).doubleValue( ) ) );
			return;
		}
		else if ( value instanceof Date )
		{
			if ( formatStr == null || formatStr.length( ) == 0 )
			{
				formatStr = style.getDateTimeFormat( );
			}

			DateFormatter dateFormat = context.createDateFormatter( formatStr );
			formattedStr.append( dateFormat.format( ( Date ) value ) );
			return;
		}
		else if( value instanceof String )
		{
			if( formatStr == null || formatStr.length( ) == 0 )
			{
				formatStr = style.getStringFormat( ); 				 
			}
			
			if( formatStr == null || formatStr.length( ) == 0 )
			{
				StringFormatter stringFormat = context.createStringFormatter( formatStr);
				formattedStr.append( stringFormat.format( value.toString( ) ) );
				return;
			}
		}

		formattedStr.append( value.toString( ) );
	}




	/**
	 * Checks the background image property. If it is given as a relative path,
	 * gets its absolute path and sets it back to the style.
	 * 
	 * @param style
	 *            the style that defines background image related properties
	 */
	protected void handleBackgroundImage( StyleDesign style )
	{
		if ( style == null )
			return;

		String image = style.getBackgroundImage( );
		if ( image == null )
			return;

		if ( FileUtil.isLocalResource( image ) )
		{
			image = FileUtil.getAbsolutePath( context.getReport( )
					.getBasePath( ), image );
			if ( image != null && image.length( ) > 0 )
			{
				style.put( Style.BACKGROUND_IMAGE_PROP, image );
			}
		}
	}

	/**
	 * Sets the visibility property for RowDesign.
	 * 
	 * @param design
	 *            The <code>RowDesign</code> object.
	 * @param content
	 *            The <code>RowContent</code> object.
	 */
	protected void setVisibility( RowDesign design, RowContent content )
	{
		setVisibility( design.getVisibility( ), content, null );
	}

	/**
	 * Sets the visibility property for ReportItem.
	 * 
	 * @param design
	 *            The <code>ReportItemDesign</code> object.
	 * @param content
	 *            The <code>ReportItemContent</code> object.
	 */
	protected void setVisibility( ReportItemDesign design,
			ReportItemContent content )
	{
		Expression defaultExpr = null;
		if ( design instanceof DataItemDesign )
		{
			defaultExpr = ( (DataItemDesign) design ).getValue( );
		}
		setVisibility( design.getVisibility( ), content, defaultExpr );
	}

	/**
	 * Sets the visibility value to the StyledElementContent which is the base
	 * class of RowContent and ReportItemContent.
	 * 
	 * @param visibility
	 *            The <code>VisibilityDesign</code> object.
	 * @param content
	 *            The StyledElementContent set.
	 * @param defaultExpression
	 *            the default expression if there is not expression in the rule
	 */
	private void setVisibility( VisibilityDesign visibility,
			StyledElementContent content, Expression defaultExpression )
	{
		if ( visibility != null )
		{
			for ( int i = 0; i < visibility.count( ); i++ )
			{
				VisibilityRuleDesign rule = visibility.getRule( i );
				Expression expr = rule.getExpression( );
				//If there is no expression in the rule, then take the default
				// expression (only for the data item)
				if ( expr == null )
				{
					expr = defaultExpression;
				}
				Object result = null;
				if ( expr != null )
				{
					result = context.evaluate( expr );
				}
				if ( result == null || !( result instanceof Boolean ) )
				{
				    logger.log( Level.WARNING, "The following visibility expression does not evaluate to a legal boolean value: {0}",  //$NON-NLS-1$
										rule.getExpression( ).getExpr( ) );
					continue;
				}
				boolean isHidden = ( (Boolean) result ).booleanValue( );
				//The report element appears by default and if the result is
				// not hidden, then ignore it.
				if ( !isHidden )
				{
					continue;
				}
				content.hide( rule.getFormat( ) );
			}
		}
	}
}