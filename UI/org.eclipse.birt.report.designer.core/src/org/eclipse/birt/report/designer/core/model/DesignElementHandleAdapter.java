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

package org.eclipse.birt.report.designer.core.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.DimensionValue;
import org.eclipse.birt.report.model.util.ColorUtil;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement DesignElementHandleAdapter responds to model
 * DesignElmentHandle
 * 
 *  
 */

public abstract class DesignElementHandleAdapter
{

	private DesignElementHandle elementHandle;

	private IModelAdapterHelper helper;

	/**
	 * constructor
	 * 
	 * @param element
	 * @param mark
	 */
	public DesignElementHandleAdapter( DesignElementHandle element,
			IModelAdapterHelper mark )
	{
		this.elementHandle = element;
		this.helper = mark;
	}

	/**
	 * Gets the Children iterator. This children relationship is determined by
	 * GUI requirement. This is not the model children relationship.
	 * 
	 * @return Children iterator
	 */
	public List getChildren( )
	{
		return Collections.EMPTY_LIST;

	}

	/**
	 * Gets display name of report element
	 * 
	 * @return Display name
	 */

	public String getDisplayName( )
	{
		return getHandle( ).getDisplayLabel( );
	}

	/**
	 * Inserts the iterator to the given list
	 * 
	 * @param iterator
	 *            the iterator
	 * @param list
	 *            the list
	 */

	protected void insertIteratorToList( Iterator iterator, List list )
	{
		for ( Iterator it = iterator; it.hasNext( ); )
		{
			list.add( it.next( ) );
		}

	}

	/**
	 * @return Returns the handle.
	 */
	public DesignElementHandle getHandle( )
	{
		return elementHandle;
	}

	/**
	 * Gets the report element,which is presented by this adapter.
	 * 
	 * @return
	 */
	public DesignElement getElement( )
	{
		return elementHandle.getElement( );
	}

	protected ReportDesign getReportDesign( )
	{
		return SessionHandleAdapter.getInstance( ).getReportDesign( );
	}

	/**
	 * Reloads all properties from model
	 */
	public void reload( )
	{

	}

	/**
	 * @return true if the cached data is dirty
	 */
	public boolean checkDirty( )
	{
		if ( helper != null )
		{
			return helper.isDirty( );
		}
		return true;
	}

	/**
	 * @return
	 */
	protected IModelAdapterHelper getModelAdaptHelper( )
	{
		return helper;
	}

	/**
	 * @return
	 */
	protected Dimension getDefaultSize( )
	{
		return helper.getPreferredSize( ).shrink(
				helper.getInsets( ).getWidth( ),
				helper.getInsets( ).getHeight( ) );
	}

	/**
	 * Sets the handle this adapter
	 * 
	 * @param handle
	 */
	public void setElementHandle( DesignElementHandle handle )
	{
		this.elementHandle = handle;
	}

	/**
	 * Starts a transaction on the current activity stack.
	 * 
	 * @param name
	 */
	public void transStar( String name )
	{
		CommandStack stack = getReportDesign( ).handle( ).getCommandStack( );
		//start trans
		stack.startTrans( name );
	}

	/**
	 * Ends a transaction on the current activity stack
	 */
	public void transEnd( )
	{
		CommandStack stack = getReportDesign( ).handle( ).getCommandStack( );
		stack.commit( );
	}

	/**
	 * Get the foreground color.
	 * 
	 * @param handle
	 *            The handle of design element.
	 * @return fore ground color
	 */
	public int getForegroundColor( DesignElementHandle handle )
	{
		Object obj = handle.getProperty( Style.COLOR_PROP );

		if ( obj == null )
		{
			return 0x0;
		}

		if ( obj instanceof String )
		{
			return ColorUtil.parseColor( (String) obj );
		}

		return ( (Integer) obj ).intValue( );
	}

	/**
	 * Get the background color.
	 * 
	 * @param handle
	 *            The handle of design element.
	 * @return back ground color
	 */
	public int getBackgroundColor( DesignElementHandle handle )
	{
		Object obj = handle.getProperty( Style.BACKGROUND_COLOR_PROP );

		if ( obj == null )
		{
			return 0xFFFFFF;
		}

		if ( obj instanceof String )
		{
			return ColorUtil.parseColor( (String) obj );
		}

		return ( (Integer) obj ).intValue( );
	}

	/**
	 * Get background image.
	 * 
	 * @param handle
	 *            The handle of design element.
	 * @return background image
	 */
	public String getBackgroundImage( DesignElementHandle handle )
	{
		return handle.getStringProperty( Style.BACKGROUND_IMAGE_PROP );
	}

	/**
	 * Get background position.
	 * 
	 * @param handle
	 *            The handle of design element.
	 * @return background position
	 */
	public Object getBackgroundPosition( DesignElementHandle handle )
	{
		int x = 0;
		int y = 0;

		Object px = handle.getProperty( Style.BACKGROUND_POSITION_X_PROP );
		Object py = handle.getProperty( Style.BACKGROUND_POSITION_Y_PROP );

		//left, center, right, top, bottom
		if ( px instanceof String && py instanceof String )
		{
			return new int[]{getPosition( (String) px ),
					getPosition( (String) py )};
		}

		// {1cm,1cm} or {0%,0%}
		if ( px instanceof DimensionValue && py instanceof DimensionValue )
		{
			// {0%,0%}
			if ( DesignChoiceConstants.UNITS_PERCENTAGE
					.equals( ( (DimensionValue) px ).getUnits( ) ) )
			{
				if ( !( DesignChoiceConstants.UNITS_PERCENTAGE
						.equals( ( (DimensionValue) py ).getUnits( ) ) ) )
				{
					return new int[]{PositionConstants.CENTER,
							PositionConstants.CENTER};
				}

				return new DimensionValue[]{(DimensionValue) px,
						(DimensionValue) py};
			}

			// {1cm,1cm}
			x = (int) DEUtil.convertoToPixel( px );
			y = (int) DEUtil.convertoToPixel( py );

			return new Point( x, y );
		}

		return new int[]{PositionConstants.CENTER, PositionConstants.CENTER};
	}

	/**
	 * Get background repeat property.
	 * 
	 * @param handle
	 *            The handle of design element.
	 * @return background repeat property
	 */
	public int getBackgroundRepeat( DesignElementHandle handle )
	{
		return getRepeat( handle
				.getStringProperty( Style.BACKGROUND_REPEAT_PROP ) );
	}

	private int getPosition( String position )
	{
		if ( DesignChoiceConstants.BACKGROUND_POSITION_LEFT.equals( position ) )
		{
			return PositionConstants.WEST;
		}
		if ( DesignChoiceConstants.BACKGROUND_POSITION_RIGHT.equals( position ) )
		{
			return PositionConstants.EAST;
		}
		if ( DesignChoiceConstants.BACKGROUND_POSITION_TOP.equals( position ) )
		{
			return PositionConstants.NORTH;
		}
		if ( DesignChoiceConstants.BACKGROUND_POSITION_BOTTOM.equals( position ) )
		{
			return PositionConstants.SOUTH;
		}
		return PositionConstants.CENTER;
	}

	private int getRepeat( String repeat )
	{
		if ( DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_X.equals( repeat ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_Y
				.equals( repeat ) )
		{
			return 2;
		}
		else if ( DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT
				.equals( repeat ) )
		{
			return 3;
		}
		return 0;
	}
}