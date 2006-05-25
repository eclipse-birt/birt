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
package org.eclipse.birt.data.engine.impl.document;

import java.io.OutputStream;

/**
 * 
 */
public class StreamWrapper
{
	private OutputStream streamForExprValue;
	private OutputStream streamForRowLen;
	private OutputStream streamForResultClass;
	private OutputStream streamForDataSet;
	private OutputStream streamForGroupInfo;
	
	/**
	 * @param streamForExprValue
	 * @param streamForRowLen
	 * @param streamForResultClass
	 * @param streamForDataSet
	 */
	public StreamWrapper( OutputStream streamForExprValue,
			OutputStream streamForRowLen, OutputStream streamForResultClass,
			OutputStream streamForDataSet, OutputStream streamForGroupInfo )
	{
		this.streamForExprValue = streamForExprValue;
		this.streamForRowLen = streamForRowLen;
		this.streamForResultClass = streamForResultClass;
		this.streamForDataSet = streamForDataSet;
		this.streamForGroupInfo = streamForGroupInfo;
	}
	
	/**
	 * @return
	 */
	public OutputStream getStreamForExprValue()
	{
		return this.streamForExprValue;		
	}
	
	/**
	 * @return
	 */
	public OutputStream getStreamForRowLen()
	{
		return this.streamForRowLen;		
	}
	
	/**
	 * @return
	 */
	public OutputStream getStreamForResultClass()
	{
		return this.streamForResultClass;		
	}
	
	/**
	 * @return
	 */
	public OutputStream getStreamForDataSet()
	{
		return this.streamForDataSet;		
	}
	
	/**
	 * @return
	 */
	public OutputStream getStreamForGroupInfo()
	{
		return this.streamForGroupInfo;		
	}
	
}
