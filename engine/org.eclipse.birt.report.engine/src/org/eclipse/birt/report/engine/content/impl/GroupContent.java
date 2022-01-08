/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.ir.GroupDesign;

public class GroupContent extends AbstractContent implements IGroupContent {

	Boolean headerRepeat;

	String groupId;

	GroupContent(IGroupContent group) {
		super(group);
		this.headerRepeat = Boolean.valueOf(group.isHeaderRepeat());
		this.groupId = group.getGroupID();
	}

	public int getContentType() {
		return GROUP_CONTENT;
	}

	GroupContent(IReportContent report) {
		super(report);
	}

	public IBandContent getHeader() {
		return getBand(IBandContent.BAND_GROUP_HEADER);
	}

	public IBandContent getFooter() {
		return getBand(IBandContent.BAND_GROUP_FOOTER);
	}

	protected IBandContent getBand(int type) {
		if (children == null) {
			return null;
		}
		Iterator iter = children.iterator();
		while (iter.hasNext()) {
			Object child = iter.next();
			if (child instanceof IBandContent) {
				IBandContent band = (IBandContent) child;
				if (band.getBandType() == type) {
					return band;
				}
			}
		}
		return null;
	}

	public String getGroupID() {
		return groupId;
	}

	public void setGroupID(String groupId) {
		this.groupId = groupId;
	}

	public boolean isHeaderRepeat() {
		if (headerRepeat != null) {
			return headerRepeat.booleanValue();
		}
		if (generateBy instanceof GroupDesign) {
			GroupDesign design = (GroupDesign) generateBy;
			return design.isHeaderRepeat();
		}
		return false;
	}

	transient int groupLevel = -1;

	public int getGroupLevel() {
		if (groupLevel == -1) {
			if (generateBy instanceof GroupDesign) {
				GroupDesign design = (GroupDesign) generateBy;
				groupLevel = design.getGroupLevel();
			} else if (parent instanceof GroupContent) {
				groupLevel = ((GroupContent) parent).getGroupLevel();
			} else {
				return 0;
			}
		}
		return groupLevel;
	}

	public void setHeaderRepeat(boolean repeat) {
		headerRepeat = Boolean.valueOf(repeat);
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitGroup(this, value);
	}

	static final protected short FIELD_HEADER_REPEAT = 1500;

	protected void writeFields(DataOutputStream out) throws IOException {
		super.writeFields(out);
		if (headerRepeat != null) {
			IOUtil.writeShort(out, FIELD_HEADER_REPEAT);
			IOUtil.writeBool(out, headerRepeat.booleanValue());
		}
	}

	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_HEADER_REPEAT:
			headerRepeat = Boolean.valueOf(IOUtil.readBool(in));
			break;
		default:
			super.readField(version, filedId, in, loader);
		}
	}

	public boolean needSave() {
		if (headerRepeat != null) {
			return true;
		}
		return super.needSave();
	}

	protected IContent cloneContent() {
		return new GroupContent(this);
	}
}
