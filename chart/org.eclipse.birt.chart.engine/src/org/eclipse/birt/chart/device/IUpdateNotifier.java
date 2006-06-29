/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.model.Chart;

/**
 * Defines methods that allow model updating via a device event handler used in
 * conjunction with a device renderer.
 * 
 * @see IDeviceRenderer
 */
public interface IUpdateNotifier
{

	/**
	 * Requests the container to regenerate the chart using the design-time
	 * model
	 */
	void regenerateChart( );

	/**
	 * Requests the container to repaint the last generated chart
	 */
	void repaintChart( );

	/**
	 * Returns an instance of the peer (component) used for device-specific
	 * actions
	 * 
	 * @return An instance of the peer (component) used for device-specific
	 *         actions
	 */
	Object peerInstance( );

	/**
	 * Returns an instance of the chart design-time model
	 * 
	 * @return An instance of the chart design-time model
	 */
	Chart getDesignTimeModel( );

	/**
	 * Returns an instance of the chart run-time model for the last generated
	 * instance
	 * 
	 * @return An instance of the chart run-time model for the last generated
	 *         instance
	 */
	Chart getRunTimeModel( );

	/**
	 * Returns the object from context by given key.
	 * 
	 * @since 2.1
	 * 
	 * @param key
	 *            Key object.
	 * @return
	 */
	Object getContext( Object key );

	/**
	 * Puts specified key,value to context.
	 * 
	 * @since 2.1
	 * 
	 * @param key
	 *            Key object.
	 * @param value
	 *            Value object.
	 * @return The previous value associated with given key.
	 */
	Object putContext( Object key, Object value );

	/**
	 * Removes the entry from context by given key if exists.
	 * 
	 * @since 2.1
	 * 
	 * @param key
	 *            Key object.
	 * @return The previous value associated with given key
	 */
	Object removeContext( Object key );
}
