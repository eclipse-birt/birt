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

package org.eclipse.birt.report.engine.presentation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.nLayout.area.impl.SizeBasedContent;

public class SizeBasedPageSection extends PageSection {
	public SizeBasedContent start;
	public SizeBasedContent end;

	public SizeBasedPageSection() {
		start = new SizeBasedContent();
		end = new SizeBasedContent();
	}

	public void write(DataOutputStream out) throws IOException {
		IOUtil.writeInt(out, TYPE_FIXED_LAYOUT_PAGE_SECTION);
		IOUtil.writeInt(out, start.floatPos);
		IOUtil.writeInt(out, start.offsetInContent);
		IOUtil.writeInt(out, start.dimension);
		IOUtil.writeInt(out, start.width);
		IOUtil.writeInt(out, end.floatPos);
		IOUtil.writeInt(out, end.offsetInContent);
		IOUtil.writeInt(out, end.dimension);
		IOUtil.writeInt(out, end.width);
		writeInstanceIndex(out, starts);
		writeInstanceIndex(out, ends);
	}

	public void read(DataInputStream in) throws IOException {
		start.floatPos = IOUtil.readInt(in);
		start.offsetInContent = IOUtil.readInt(in);
		start.dimension = IOUtil.readInt(in);
		start.width = IOUtil.readInt(in);
		end.floatPos = IOUtil.readInt(in);
		end.offsetInContent = IOUtil.readInt(in);
		end.dimension = IOUtil.readInt(in);
		end.width = IOUtil.readInt(in);
		starts = readInstanceIndex(in);
		ends = readInstanceIndex(in);
		startOffset = starts[starts.length - 1].getOffset();
		endOffset = ends[ends.length - 1].getOffset();
	}

}
