/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.ir.BandDesign;

public class AbstractBandContent extends ContainerContent implements IBandContent {

	int bandType = -1;

	AbstractBandContent(IBandContent content) {
		super(content);
		this.bandType = content.getBandType();
	}

	AbstractBandContent(IReportContent report) {
		super(report);
	}

	public int getBandType() {
		if (bandType == -1) {
			if (generateBy instanceof BandDesign) {
				BandDesign bandDesign = (BandDesign) generateBy;
				return bandDesign.getBandType();
			}
		}
		return bandType;
	}

	public void setBandType(int bandType) {
		if (generateBy instanceof BandDesign) {
			BandDesign bandDesign = (BandDesign) generateBy;
			if (bandType == bandDesign.getBandType()) {
				bandType = -1;
				return;
			}
		}
		this.bandType = bandType;
	}

	public String getGroupID() {
		int bandType = getBandType();
		if (bandType == IBandContent.BAND_GROUP_HEADER || bandType == IBandContent.BAND_GROUP_FOOTER) {
			Object parent = getParent();
			if (parent instanceof IGroupContent) {
				IGroupContent group = (IGroupContent) parent;
				return group.getGroupID();
			}
		}
		return null;
	}

	static final protected short FIELD_TYPE = 900;

	protected void writeFields(DataOutputStream out) throws IOException {
		super.writeFields(out);
		IOUtil.writeShort(out, FIELD_TYPE);
		IOUtil.writeInt(out, getBandType());
	}

	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_TYPE:
			bandType = IOUtil.readInt(in);
			break;
		default:
			super.readField(version, filedId, in, loader);
		}
	}

	public boolean needSave() {
		if (bandType != -1) {
			return true;
		}
		return super.needSave();
	}
}
