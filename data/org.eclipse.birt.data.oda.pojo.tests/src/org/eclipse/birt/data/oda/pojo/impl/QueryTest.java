
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.impl;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.impl.Driver;
import org.eclipse.birt.data.oda.pojo.impl.Query;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.testutil.PojoQueryCreator;
import org.eclipse.birt.data.oda.pojo.util.PojoQueryWriter;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class QueryTest {

	@SuppressWarnings("nls")
	@Test
	public void testGetParameterMetaData() throws OdaException {
		PojoQuery pq = PojoQueryCreator.createWithParameters();
		Query q = new Query(null);
		q.prepare(PojoQueryWriter.write(pq));
		IParameterMetaData pmd = q.getParameterMetaData();
		assertEquals(2, pmd.getParameterCount());
		assertEquals("id", pmd.getParameterName(1));
		assertEquals("sex", pmd.getParameterName(2));
		assertEquals(Driver.getNativeDataTypeCode(Constants.ODA_TYPE_Integer), pmd.getParameterType(1));
		assertEquals(Driver.getNativeDataTypeCode(Constants.ODA_TYPE_Boolean), pmd.getParameterType(2));

	}

}
