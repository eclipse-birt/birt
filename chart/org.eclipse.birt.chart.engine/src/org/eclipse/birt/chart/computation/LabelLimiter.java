/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * Helper class to limit the size of a label by wrapping its text or shorten its
 * text with ellipsis.
 */

public class LabelLimiter
{

	private double maxWidth;
	private double maxHeight;
	private double wrapping;

	/**
	 * @param maxSize
	 * @param dWrapping
	 */
	public LabelLimiter( double maxWidth, double maxHeight, double wrapping )
	{
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.wrapping = wrapping;
	}

	public double computeWrapping( IDisplayServer xs, Label la )
	{
		return computeWrapping( xs, la, this );
	}

	/**
	 * Returns a bounding box using maxWidth and maxHeight
	 * 
	 * @param bb
	 *            will be updated and returned if not null, otherwise a new
	 *            bounding box will be created.
	 * @return
	 */
	public BoundingBox getBounding( BoundingBox bb )
	{
		if ( bb == null )
		{
			bb = new BoundingBox( 0, 0, 0, maxWidth, maxHeight, 0 );
		}
		return bb;
	}

	/**
	 * Compute the wrapping with maxWidth, maxHeight. If the wrapping is set to
	 * 0, namely auto, this method should be called before calling
	 * limitLabelSize.
	 * 
	 * @param xs
	 * @param la
	 * @param lbLimit
	 * @return
	 */
	public static final double computeWrapping( IDisplayServer xs, Label la,
			LabelLimiter lbLimit )
	{
		double dWrapping = 0;
		final double dSafe = 10;

		if ( lbLimit != null )
		{
			double dScale = xs.getDpiResolution( ) / 72d;
			double fRotation = la.getCaption( ).getFont( ).getRotation( );
			Insets insets = la.getInsets( ).scaledInstance( dScale );
			double dInsetsWidth = insets.getLeft( ) + insets.getRight( );
			double dInsetsHeight = insets.getTop( ) + insets.getBottom( );

			if ( ChartUtil.mathEqual( fRotation, 0 ) )
			{
				dWrapping = Math.floor( lbLimit.maxWidth - dInsetsWidth )
						- dSafe;
			}
			else if ( ChartUtil.mathEqual( fRotation, 90 ) )
			{
				dWrapping = Math.floor( lbLimit.maxHeight - dInsetsHeight )
						- dSafe;
			}
			else
			{
				fRotation %= 180;
				if ( fRotation < 0 )
				{
					fRotation += 180;
				}
				double rad = Math.toRadians( fRotation % 90 );
				double tg = Math.tan( rad );
				double m = 1 - tg * tg;
				double r = 2 * tg / ( 1 + tg * tg );
				// double wd1 = lbLimit.maxWidth - dInsetsWidth;
				// double ht1 = lbLimit.maxHeight - dInsetsHeight;
				double wd1 = lbLimit.maxWidth;
				double ht1 = lbLimit.maxHeight;
				double b, d;
				double wd2;

				if ( wd1 < ht1 )
				{
					if ( ( ( tg < 1 ) && ( r < wd1 / ht1 ) )
							|| ( ( tg > 1 ) && ( r < wd1 / ht1 ) ) )
					{
						b = ( wd1 - ht1 * tg ) / m;
						d = ( ht1 - wd1 * tg ) / m;
					}
					else
					{
						b = wd1 / 2;
						d = b / tg;
					}

				}
				else
				{
					if ( ( ( tg < 1 ) && ( r < ht1 / wd1 ) )
							|| ( ( tg > 1 ) && ( r < ht1 / wd1 ) ) )
					{
						b = ( wd1 - ht1 * tg ) / m;
						d = ( ht1 - wd1 * tg ) / m;
					}
					else
					{
						d = ht1 / 2;
						b = d / tg;
					}
				}

				double cos = Math.cos( rad );

				if ( fRotation < 90 )
				{
					wd2 = b / cos;
					// ht2 = d / cos;
				}
				else
				{
					wd2 = d / cos;
					// ht2 = b / cos;
				}

				dWrapping = Math.floor( wd2 ) - dInsetsWidth - dSafe;
			}
			lbLimit.wrapping = dWrapping;
		}

		return dWrapping;
	}

	/**
	 * modify the text of la to fit the limit size.
	 * 
	 * @param xs
	 * @param la
	 * @return
	 * @throws ChartException
	 */
	public LabelLimiter limitLabelSize( IDisplayServer xs, Label la )
			throws ChartException
	{
		return limitLabelSize( xs, la, this );
	}

	/**
	 * To compute the text of the label with a limited size, the label text will
	 * be wrapped and shortened with ellipsis if required, the size of the label
	 * bound will be returned.
	 * 
	 * @param xs
	 * @param la
	 * @param maxSize
	 * @param lbLimit
	 * @return
	 * @throws ChartException
	 */
	public static final LabelLimiter limitLabelSize( IDisplayServer xs,
			Label la, LabelLimiter lbLimit ) throws ChartException
	{
		double maxWidth, maxHeight, wrapping;
		if ( lbLimit != null )
		{
			EllipsisHelper eHelper = EllipsisHelper.simpleInstance( xs,
					la,
					null );
			eHelper.checkLabelEllipsis( la.getCaption( ).getValue( ), lbLimit );
			maxWidth = eHelper.getTester( ).getWidth( );
			maxHeight = eHelper.getTester( ).getHeight( );
			wrapping = lbLimit.getWrapping( );
		}
		else
		{
			BoundingBox bb = Methods.computeLabelSize( xs, la, 0, null );
			maxWidth = bb.getWidth( );
			maxHeight = bb.getHeight( );
			wrapping = 0;
		}
		return new LabelLimiter( maxWidth, maxHeight, wrapping );
	}

	/**
	 * @return Returns the maxWidth.
	 */
	public final double getMaxWidth( )
	{
		return maxWidth;
	}

	/**
	 * @param maxWidth
	 *            The maxWidth to set.
	 */
	public final void setMaxWidth( double maxWidth )
	{
		this.maxWidth = maxWidth;
	}

	/**
	 * @return Returns the maxHeight.
	 */
	public final double getMaxHeight( )
	{
		return maxHeight;
	}

	/**
	 * @param maxHeight
	 *            The maxHeight to set.
	 */
	public final void setMaxHeight( double maxHeight )
	{
		this.maxHeight = maxHeight;
	}

	/**
	 * @return Returns the wrapping.
	 */
	public final double getWrapping( )
	{
		return wrapping;
	}

	/**
	 * @param wrapping
	 *            The wrapping to set.
	 */
	public final void setWrapping( double wrapping )
	{
		this.wrapping = wrapping;
	}

}