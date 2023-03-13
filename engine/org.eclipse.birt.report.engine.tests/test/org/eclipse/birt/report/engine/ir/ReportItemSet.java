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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * Generate random ReportItem Set for test
 */

public class ReportItemSet {

	private Random random = new Random();

	final public int length = random.nextInt(20) + 1; // Random
	// generate
	// set
	// length

	private ReportItemDesign[] items = new ReportItemDesign[length];

	public ReportItemSet() {
		for (int i = 0; i < length; i++) {
			// Random generate Report item
			switch (random.nextInt(8)) {
			case 0:
				items[i] = new DataItemDesign();
				items[i].setName("DataItem" + i);
				break;
			case 1:
				items[i] = new FreeFormItemDesign();
				items[i].setName("FreeFormItem" + i);
				break;
			case 2:
				items[i] = new GridItemDesign();
				items[i].setName("GridItem" + i);
				break;
			case 3:
				items[i] = new ImageItemDesign();
				items[i].setName("ImageItem" + i);
				break;
			case 4:
				items[i] = new LabelItemDesign();
				items[i].setName("LabelItem" + i);
				break;
			case 5:
				items[i] = new ListItemDesign();
				items[i].setName("ListItem" + i);
				break;
			case 6:
				items[i] = new TableItemDesign();
				items[i].setName("TableItem" + i);
				break;
			default:
				items[i] = new TextItemDesign();
				items[i].setName("TextItem" + i);
				break;
			}
		}
	}

	public ReportItemDesign getItem(int index) {
		return items[index];
	}

	public ArrayList getItems() {
		ArrayList list = new ArrayList(length);
		for (int i = 0; i < length; i++) {
			list.add(items[i]);
		}
		return list;

	}
}
