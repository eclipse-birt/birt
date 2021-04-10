
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.EngineException;
import java.util.List;
import junit.framework.TestCase;

/**
 * 
 */

public class PageSequenceParseTest extends TestCase {

	public void testIncorrectPageNumberRange() {
		boolean exceptionFlag = false;
		try {
			PageSequenceParse.parsePageSequence("20-25", 15);
		} catch (EngineException e) {
			exceptionFlag = true;
		}
		assert (exceptionFlag);

		exceptionFlag = false;
		try {
			PageSequenceParse.parsePageSequence("12-10", 15);
		} catch (EngineException e) {
			exceptionFlag = true;
		}
		assert (exceptionFlag);

		exceptionFlag = false;
		try {
			PageSequenceParse.parsePageSequence("10-", 15);
		} catch (EngineException e) {
			exceptionFlag = true;
		}
		assert (exceptionFlag);

		exceptionFlag = false;
		try {
			PageSequenceParse.parsePageSequence("16", 15);
		} catch (EngineException e) {
			exceptionFlag = true;
		}
		assert (exceptionFlag);
	}

	public void testCorrectPageNumberRange() {
		try {
			List pageRangeList = PageSequenceParse.parsePageSequence("13-15,3,8-11", 15);
			assert (null != pageRangeList);
			assert (3 == pageRangeList.size());
			long[] pageRange = (long[]) pageRangeList.get(0);
			assert (3 == pageRange[0]);
			assert (3 == pageRange[1]);
			pageRange = (long[]) pageRangeList.get(1);
			assert (8 == pageRange[0]);
			assert (11 == pageRange[1]);
			pageRange = (long[]) pageRangeList.get(2);
			assert (13 == pageRange[0]);
			assert (15 == pageRange[1]);
		} catch (EngineException e) {
			// TODO Auto-generated catch block
			assert (false);
		}
	}
}
