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

import java.util.logging.Level;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.HighlightRuleDesign;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.MapRuleDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.eclipse.birt.report.engine.ir.VisibilityRuleDesign;
import org.eclipse.birt.report.engine.util.FileUtil;

/**
 * Defines an abstract base class for all styled element executors, including
 * <code>DataItemExecutor</code>,<code>TextItemExecutor</code>, etc.. The
 * class provides methods for style manipulation, such as applying highlight and
 * mapping rules, calculating flattened (merged) styles, and so on.
 * 
 * @version $Revision: 1.20 $ $Date: 2005/11/15 02:49:02 $
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
			IReportItemVisitor visitor )
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
	protected void processStyle( ReportItemDesign design, IContent content )
	{
		content.setX( design.getX( ) );
		content.setY( design.getY( ) );
		content.setWidth( design.getWidth( ) );
		content.setHeight( design.getHeight( ) );
		content.setStyleClass( design.getStyleName( ) );
		StyleDeclaration inlineStyle = createHighlightStyle( design
				.getHighlight( ) );
		content.setInlineStyle( inlineStyle );
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
	private StyleDeclaration createHighlightStyle( HighlightDesign highlight )
	{
		if ( highlight == null )
		{
			return null;
		}

		StyleDeclaration style = null;
		for ( int i = 0; i < highlight.getRuleCount( ); i++ )
		{
			HighlightRuleDesign rule = highlight.getRule( i );
			if ( rule != null )
			{
				Object value = context.evaluate( rule.getConditionExpr( ) );
				if ( ( value != null ) && ( value instanceof Boolean )
						&& ( ( (Boolean) value ).booleanValue( ) ) )
				{
					StyleDeclaration highlightStyle = new StyleDeclaration(
							(StyleDeclaration) rule.getStyle( ) );
					if ( style != null )
					{
						style.setProperties( highlightStyle );
					}
					else
					{
						style = highlightStyle;
					}
				}
			}
		}

		if ( style != null )
		{
			processBackgroundImage( style );
		}

		return style;
	}

	/**
	 * process the mapped rules.
	 * 
	 * @param item
	 *            the design element used to create the data obj.
	 * @param dataObj
	 *            Data object.
	 */
	protected void processMappingValue( StyledElementDesign item,
			IDataContent dataObj )
	{
		MapDesign map = item.getMap( );
		if ( map != null )
		{
			for ( int i = 0; i < map.getRuleCount( ); i++ )
			{
				MapRuleDesign rule = map.getRule( i );
				if ( rule != null )
				{
					Object value = context.evaluate( rule.getConditionExpr( ) );
					if ( ( value != null ) && ( value instanceof Boolean )
							&& ( ( (Boolean) value ).booleanValue( ) ) )
					{
						dataObj.setLabelText( rule.getDisplayText( ) );
						dataObj.setLabelKey( rule.getDisplayKey( ) );
					}
				}
			}
		}
	}

	/**
	 * Checks the background image property. If it is given as a relative path,
	 * gets its absolute path and sets it back to the style.
	 * 
	 * @param style
	 *            the style that defines background image related properties
	 */
	protected void processBackgroundImage( IStyle style )
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
				style.setBackgroundImage( image );
			}
		}
	}

	/**
	 * Sets the visibility property for ReportItem.
	 * 
	 * @param design
	 *            The <code>ReportItemDesign</code> object.
	 * @param content
	 *            The <code>ReportItemContent</code> object.
	 */
	protected void processVisibility( ReportItemDesign design, IContent content )
	{
		VisibilityDesign visibility = design.getVisibility( );
		boolean isFirst = true;
		if ( visibility != null )
		{
			StringBuffer buffer = new StringBuffer( );
			for ( int i = 0; i < visibility.count( ); i++ )
			{
				VisibilityRuleDesign rule = visibility.getRule( i );
				Expression expr = rule.getExpression( );
				Object result = null;
				if ( expr != null )
				{
					result = context.evaluate( expr );
				}
				if ( result == null || !( result instanceof Boolean ) )
				{
					logger
							.log(
									Level.WARNING,
									"The following visibility expression does not evaluate to a legal boolean value: {0}", //$NON-NLS-1$
									rule.getExpression( ).getExpr( ) );
					continue;
				}
				boolean isHidden = ( (Boolean) result ).booleanValue( );
				// The report element appears by default and if the result is
				// not hidden, then ignore it.
				if ( !isHidden )
				{
					continue;
				}
				// we should use rule as the string as
				if ( isFirst )
				{
					isFirst = false;
				}
				else
				{
					buffer.append( ", " ); //$NON-NLS-1$
				}
				buffer.append( rule.getFormat( ) );
			}
			content.getStyle( ).setVisibleFormat( buffer.toString( ) );
		}
	}

}