/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;

public interface IImageArea extends IArea {

	String getImageUrl();

	byte[] getImageData();

	String getHelpText();

	String getExtension();

	String getMIMEType();

	HashMap<String, String> getParameters();

	void addImageMap(int[] peak, IHyperlinkAction action);

	ArrayList<IImageMap> getImageMapDescription();

	interface IImageMap {
		public int[] getVertices();

		public IHyperlinkAction getAction();
	}
}
