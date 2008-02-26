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

package org.eclipse.birt.report.item.crosstab.core.script.internal.handler;

import java.util.WeakHashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * CrosstabHandlerCache
 */
public class CrosstabHandlerCache
{

	private WeakHashMap<DesignElementHandle, DesignElementHandle> cell2crosstabCache;
	private WeakHashMap<DesignElementHandle, String> cell2createScriptCache;
	private WeakHashMap<DesignElementHandle, CrosstabCreationHandler> crosstab2createHandlerCache;
	private WeakHashMap<DesignElementHandle, String> cell2renderScriptCache;
	private WeakHashMap<DesignElementHandle, CrosstabRenderingHandler> crosstab2renderHandlerCache;

	public CrosstabHandlerCache( )
	{
		cell2crosstabCache = new WeakHashMap<DesignElementHandle, DesignElementHandle>( );
		cell2createScriptCache = new WeakHashMap<DesignElementHandle, String>( );
		crosstab2createHandlerCache = new WeakHashMap<DesignElementHandle, CrosstabCreationHandler>( );
		cell2renderScriptCache = new WeakHashMap<DesignElementHandle, String>( );
		crosstab2renderHandlerCache = new WeakHashMap<DesignElementHandle, CrosstabRenderingHandler>( );
	}

	public DesignElementHandle getCrosstabHandle( DesignElementHandle cellHandle )
	{
		DesignElementHandle crosstab = cell2crosstabCache.get( cellHandle );

		if ( crosstab == null )
		{
			DesignElementHandle e = cellHandle;
			while ( e != null )
			{
				if ( ICrosstabConstants.CROSSTAB_EXTENSION_NAME.equals( e.getStringProperty( ExtendedItemHandle.EXTENSION_NAME_PROP ) ) )
				{
					crosstab = e;
					cell2crosstabCache.put( cellHandle, crosstab );
					break;
				}
				e = e.getContainer( );
			}
		}

		return crosstab;
	}

	public String getOnCreateScript( DesignElementHandle cellHandle )
	{
		String onCreate = cell2createScriptCache.get( cellHandle );

		if ( onCreate == null )
		{
			ExtendedItemHandle crosstabHandle = (ExtendedItemHandle) getCrosstabHandle( cellHandle );

			if ( crosstabHandle != null )
			{
				onCreate = crosstabHandle.getOnCreate( );

				onCreate = onCreate == null ? "" //$NON-NLS-1$
						: onCreate.trim( );

				cell2createScriptCache.put( cellHandle, onCreate );
			}
		}

		return onCreate;
	}

	public String getOnRenderScript( DesignElementHandle cellHandle )
	{
		String onRender = cell2renderScriptCache.get( cellHandle );

		if ( onRender == null )
		{
			ExtendedItemHandle crosstabHandle = (ExtendedItemHandle) getCrosstabHandle( cellHandle );

			if ( crosstabHandle != null )
			{
				onRender = crosstabHandle.getOnRender( );

				onRender = onRender == null ? "" //$NON-NLS-1$
						: onRender.trim( );

				cell2renderScriptCache.put( cellHandle, onRender );
			}
		}

		return onRender;
	}

	public synchronized CrosstabCreationHandler getCreateHandler(
			ExtendedItemHandle crosstab, ClassLoader contextLoader )
			throws BirtException
	{
		CrosstabCreationHandler handler = crosstab2createHandlerCache.get( crosstab );

		if ( handler == null )
		{
			handler = new CrosstabCreationHandler( crosstab, contextLoader );

			crosstab2createHandlerCache.put( crosstab, handler );
		}

		return handler;
	}

	public synchronized CrosstabRenderingHandler getRenderHandler(
			ExtendedItemHandle crosstab, ClassLoader contextLoader )
			throws BirtException
	{
		CrosstabRenderingHandler handler = crosstab2renderHandlerCache.get( crosstab );

		if ( handler == null )
		{
			handler = new CrosstabRenderingHandler( crosstab, contextLoader );

			crosstab2renderHandlerCache.put( crosstab, handler );
		}

		return handler;
	}

}
