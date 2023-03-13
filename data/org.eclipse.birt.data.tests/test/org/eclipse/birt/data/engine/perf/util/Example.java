/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.perf.util;

import java.math.BigDecimal;

import org.eclipse.birt.data.engine.perf.util.SizeOfUtil.SizePoint;
import org.eclipse.birt.data.engine.perf.util.TimeUtil.TimePoint;

/**
 * Demonstrate how to use the utility classes: TimeUtil and SizeOfUtil
 */
public class Example {
	/**
	 * Test function of TimeUtil
	 */
	public void testTimeUtil() {
		// TimeUtil#getTime
		System.out.println(TimeUtil.instance.getTime());

		// TimeUtil#getTimePointSpan
		TimePoint time1 = TimeUtil.instance.getTimePoint();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		TimePoint time2 = TimeUtil.instance.getTimePoint();

		System.out.println(TimeUtil.instance.getTimePointSpan(time1, time2));
	}

	/**
	 * Test function of SizeOfUtil
	 */
	public void testSizeOfUtil() {
		// SizeOfUtil#getObjectSize
		try {
			ObjectInstance oi = getObjectInstance();
			int size = SizeOfUtil.instance.getObjectSize(oi);
			System.out.println(oi.newInstance().getClass().getName() + ": " + size + " bytes.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// SizeOfUtil#getSizePointSpan
		try {
			SizePoint sp1 = SizeOfUtil.instance.getUsedMemorySizePoint();
			int[] i = new int[1000];
			i[0] = 0;
			SizePoint sp2 = SizeOfUtil.instance.getUsedMemorySizePoint();

			String str = "used memory is: " + SizeOfUtil.instance.getSizePointSpan(sp1, sp2) + " bytes";
			System.out.println(str);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @return objectInstance needs to be computed memory consumed
	 */
	private static ObjectInstance getObjectInstance() {
		return new ObjectInstance() {

			@Override
			public Object newInstance() {
				return new BigDecimal(1);
				// new BigDecimal[]{
				// new BigDecimal( 1 ), new BigDecimal( 1 ), new BigDecimal( 1 )
				// };
				// return new Integer(1);
				// BigDecimal
				// Double
				// Date
				// SizeOfUtil.newString(10)
			}
		};
	}

}
