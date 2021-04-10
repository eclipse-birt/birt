/*
 *****************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 ******************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;

import org.junit.Before;
import static org.junit.Assert.*;

public class DataSetCapabilitiesTest extends ConnectionTest {
	private DataSetCapabilities m_dsCapabilties;

	@Before
	public void dataSetCapabilitiesSetUp() throws Exception {
		m_dsCapabilties = getConnection().getMetaData("JDBC");
	}

	public final void testGetSortMode() {
		int sortMode = m_dsCapabilties.getSortMode();
		assertEquals(IDataSetMetaData.sortModeNone, sortMode);
	}

	public final void testSupportsMultipleOpenResults() throws DataException {
		boolean supportsMultipleOpenResults = m_dsCapabilties.supportsMultipleOpenResults();
		assertFalse(supportsMultipleOpenResults);
	}

	public final void testSupportsMultipleResultSets() throws DataException {
		boolean supportsMultipleResultSets = m_dsCapabilties.supportsMultipleResultSets();
		assertFalse(supportsMultipleResultSets);
	}

	public final void testSupportsNamedResultSets() throws DataException {
		boolean supportsNamedResultSets = m_dsCapabilties.supportsNamedParameters();
		assertFalse(supportsNamedResultSets);
	}

	public final void testSupportsNamedParameters() throws DataException {
		boolean supportsNamedParameters = m_dsCapabilties.supportsNamedParameters();
		assertFalse(supportsNamedParameters);
	}

	public final void testSupportsInParameters() throws DataException {
		boolean supportsInParameters = m_dsCapabilties.supportsInParameters();
		assertTrue(supportsInParameters);
	}

	public final void testSupportsOutParameters() throws DataException {
		boolean supportsOutParameters = m_dsCapabilties.supportsOutParameters();
		assertTrue(supportsOutParameters);
	}
}
