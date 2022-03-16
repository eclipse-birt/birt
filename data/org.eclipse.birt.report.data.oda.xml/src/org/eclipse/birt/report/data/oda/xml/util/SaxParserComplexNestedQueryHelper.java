/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.xml.util;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.enablement.oda.xml.util.IXMLSource;
import org.eclipse.datatools.enablement.oda.xml.util.RelationInformation;
import org.eclipse.datatools.enablement.oda.xml.util.SaxParserConsumer;

/**
 * This is a helper class used by SaxParserConsumer to generate nested xml
 * columns related infomation.
 *
 * @deprecated Please use DTP xml driver
 */
@Deprecated
public class SaxParserComplexNestedQueryHelper
		extends org.eclipse.datatools.enablement.oda.xml.util.SaxParserNestedQueryHelper {

	/**
	 *
	 * @param consumer
	 * @param rinfo
	 * @param xdis
	 * @param tName
	 */
	SaxParserComplexNestedQueryHelper(SaxParserConsumer consumer, RelationInformation rinfo, IXMLSource xmlSource,
			String tName) throws OdaException {
		super(consumer, rinfo, xmlSource, tName);
	}
}
