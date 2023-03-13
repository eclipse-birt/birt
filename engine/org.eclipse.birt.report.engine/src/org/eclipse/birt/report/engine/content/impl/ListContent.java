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
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.ir.ListItemDesign;

public class ListContent extends ContainerContent implements IListContent {

	Boolean headerRepeat;

	ListContent(IReportContent report) {
		super(report);
	}

	ListContent(IListContent listContent) {
		super(listContent);
		this.headerRepeat = listContent.isHeaderRepeat();
	}

	@Override
	public int getContentType() {
		return LIST_CONTENT;
	}

	@Override
	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitList(this, value);
	}

	@Override
	public void setHeaderRepeat(boolean headerRepeat) {
		if (generateBy instanceof ListItemDesign) {
			boolean repeatHeader = ((ListItemDesign) generateBy).isRepeatHeader();
			if (repeatHeader == headerRepeat) {
				this.headerRepeat = null;
				return;
			}
		}
		this.headerRepeat = headerRepeat;
	}

	@Override
	public boolean isHeaderRepeat() {
		if (headerRepeat != null) {
			return headerRepeat.booleanValue();
		}
		if (generateBy instanceof ListItemDesign) {
			return ((ListItemDesign) generateBy).isRepeatHeader();
		}

		return false;
	}

	@Override
	public IListBandContent getHeader() {
		return getListBand(IListBandContent.BAND_HEADER);
	}

	protected IListBandContent getListBand(int type) {
		IListBandContent listBand;
		if (children == null) {
			return null;
		}
		Iterator iter = children.iterator();
		while (iter.hasNext()) {
			Object child = iter.next();
			if (child instanceof IListBandContent) {
				listBand = (IListBandContent) child;
				if (listBand.getBandType() == type) {
					return listBand;
				}
			}
		}
		return null;
	}

	static final protected short FIELD_HEADER_REPEAT = 1300;

	@Override
	protected void writeFields(DataOutputStream out) throws IOException {
		super.writeFields(out);
		if (headerRepeat != null) {
			IOUtil.writeShort(out, FIELD_HEADER_REPEAT);
			IOUtil.writeBool(out, headerRepeat.booleanValue());
		}
	}

	@Override
	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_HEADER_REPEAT:
			headerRepeat = IOUtil.readBool(in);
			break;
		default:
			super.readField(version, filedId, in, loader);
		}
	}

	@Override
	public boolean needSave() {
		if (headerRepeat != null) {
			return true;
		}
		return super.needSave();
	}

	@Override
	protected IContent cloneContent() {
		return new ListContent(this);
	}

}
