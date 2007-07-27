/*******************************************************************************
 * Copyright (c) 2004, 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.framework.IConfigurationElement;

/**
 * The descriptor of the plugin emitter.
 */

public class EmitterInfo
{

	private String format;
	private String id;
	private String pagination;
	private String mimeType;
	private String icon;
	private String namespace;
	private IConfigurationElement emitter;
	
	/**
	 * whether emitter need to output the display:none or process it in layout
	 * engine.
	 * true: output display:none in emitter and do not process it in layout engine. 
	 * false: process it in layout engine, not output it in emitter.
	 */
	private Boolean outputDisplayNone;

	public EmitterInfo( String format, String id, String pagination,
			String mimeType, String icon, String namespace,
			Boolean outputDisplayNone, IConfigurationElement emitter )
	{
		this.format = format;
		this.id = id;
		this.emitter = emitter;
		this.pagination = pagination;
		this.mimeType = mimeType;
		this.icon = icon;
		this.namespace = namespace;
		this.outputDisplayNone = outputDisplayNone;
	}

	/**
	 * Get the namespace of the emitter.
	 * @return namespace of the emitter
	 */
	public String getNamespace( )
	{
		return namespace;
	}

	/**
	 * Get the icon of the emitter.
	 * @return
	 */
	public String getIcon( )
	{
		return icon;
	}

	/**
	 * Get the format of the emitter.
	 * @return format of the emitter
	 */
	public String getFormat( )
	{
		return format;
	}

	/**
	 * Get the id of the emitter.
	 * @return id of the emitter
	 */
	public String getID( )
	{
		return id;
	}

	/**
	 * Get the emitter instance of the emitter.
	 * @return emitter instance
	 */
	public IConfigurationElement getEmitter( )
	{
		return emitter;
	}

	/**
	 * Get the mimeType of the emitter.
	 * @return mimeType of the emitter
	 */
	public String getMimeType( )
	{
		return mimeType;
	}

	/**
	 * Get the pagination of the emitter.
	 * @return pagination of the emitter
	 */
	public String getPagination( )
	{
		return pagination;
	}
	
	/**
	 * Get the outputDisplayNone of the emitter.
	 * @return outputDisplayNone of the emitter
	 */
	public Boolean getOutputDisplayNone( )
	{
		return outputDisplayNone;
	}
}
