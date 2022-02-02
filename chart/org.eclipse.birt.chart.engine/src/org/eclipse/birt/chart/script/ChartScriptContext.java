/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script;

import org.eclipse.birt.chart.model.Chart;

/**
 * An internal implementation for IChartScriptContext
 */
public class ChartScriptContext extends AbstractScriptContext implements IChartScriptContext {

	private static final long serialVersionUID = 1L;

	private transient Chart cm;

	/**
	 * The constructor.
	 */
	public ChartScriptContext() {
		super();
	}

	/*
	 * private void writeObject( java.io.ObjectOutputStream out ) throws IOException
	 * { out.defaultWriteObject( );
	 * 
	 * ByteArrayOutputStream bao = null;
	 * 
	 * try { bao = SerializerImpl.instance( ).asXml( cm, true ); } catch ( Exception
	 * e ) { if ( logger != null ) { logger.log( e ); } bao = new
	 * ByteArrayOutputStream( ); }
	 * 
	 * out.writeObject( bao.toByteArray( ) ); }
	 * 
	 * private void readObject( java.io.ObjectInputStream in ) throws IOException,
	 * ClassNotFoundException { in.defaultReadObject( );
	 * 
	 * ByteArrayInputStream bai = new ByteArrayInputStream( (byte[]) in.readObject(
	 * ) );
	 * 
	 * try { cm = SerializerImpl.instance( ).fromXml( bai, true ); } catch (
	 * IOException e ) { if ( logger != null ) { logger.log( e ); } cm = null; } }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartScriptContext#getChartInstance()
	 */
	public Chart getChartInstance() {
		return cm;
	}

	/**
	 * Binding the script context with the chart instance
	 * 
	 * @param cm Chart
	 */
	public void setChartInstance(Chart cm) {
		this.cm = cm;
	}
}
