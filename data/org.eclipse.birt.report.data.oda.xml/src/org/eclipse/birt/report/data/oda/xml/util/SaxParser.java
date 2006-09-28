/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.xml.util;

/**
 * This instance interacts with a SaxParserConsumer instance to populate the
 * ResultSet data.
 * @deprecated Please use DTP xml driver
 */
public class SaxParser
		extends
			org.eclipse.datatools.enablement.oda.xml.util.SaxParser
{

	/**
	 * 
	 * @param stream
	 * @param consumer
	 */
	public SaxParser( XMLDataInputStream stream, ISaxParserConsumer consumer )
	{
		super( stream, consumer );
	}
}