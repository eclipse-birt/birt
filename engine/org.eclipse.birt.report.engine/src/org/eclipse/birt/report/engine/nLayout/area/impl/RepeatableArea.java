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

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;

/**
 * Class to define repeatable container area
 *
 * @since 3.3
 *
 */
public abstract class RepeatableArea extends BlockContainerArea {

	protected List<AbstractArea> repeatList = null;

	protected int repeatHeight = 0;

	protected boolean inHeaderBand = false;

	/**
	 * Constructor
	 *
	 * @param parent
	 * @param context
	 * @param content
	 */
	public RepeatableArea(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
		if (needRepeat()) {
			repeatList = new ArrayList<AbstractArea>();
		}
	}

	/**
	 * Set the flag of header band
	 *
	 * @param inHeaderBand
	 */
	public void setInHeaderBand(boolean inHeaderBand) {
		this.inHeaderBand = inHeaderBand;
	}

	// TODO refactor
	protected boolean isFirstChildInHeaderBand() {
		// check if the first child is in head band;
		if (children.size() > 0) {
			AbstractArea first = (AbstractArea) children.get(0);
			if (isInRepeatHeader(first)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void addRepeatedItem() throws BirtException {
		if (repeatList != null && repeatList.size() > 0) {
			if (!inHeaderBand && !isFirstChildInHeaderBand()) {
				if (getRepeatedHeight() < getMaxAvaHeight()) {
					for (int i = 0; i < repeatList.size(); i++) {
						ContainerArea row = (ContainerArea) repeatList.get(i);
						ContainerArea cloneRow = row.deepClone();
						if (cloneRow instanceof RowArea) {
							((RowArea) cloneRow).needResolveBorder = true;
						}
						cloneRow.finished = true;
						children.add(i, cloneRow);
						cloneRow.setParent(this);
						update(cloneRow);
						cloneRow.setAllocatedY(currentBP);
					}
				} else {
					// remove repeat list.
					repeatList = null;
				}
			}
		}
	}

	@Override
	public int getMaxAvaHeight() {
		return super.getMaxAvaHeight() - getRepeatedHeight();
	}

	protected int getRepeatedHeight() {
		if (inHeaderBand) {
			return 0;
		}
		if (repeatHeight != 0) {
			return repeatHeight;
		} else if (repeatList != null) {
			for (int i = 0; i < repeatList.size(); i++) {
				AbstractArea area = repeatList.get(i);
				repeatHeight += area.getAllocatedHeight();
			}
			return repeatHeight;
		}
		return 0;
	}

	@Override
	public SplitResult split(int height, boolean force) throws BirtException {
		// repeat header can not be split.
		if (!force && repeatList != null && repeatList.size() > 0) {
			Iterator<IArea> i = children.iterator();
			boolean firstHeaderRow = true;
			while (i.hasNext()) {
				ContainerArea area = (ContainerArea) i.next();
				if (isInRepeatHeader(area)) {
					if (firstHeaderRow) {
						area.setPageBreakInside(CSSValueConstants.AVOID_VALUE);
						firstHeaderRow = false;
					} else {
						area.setPageBreakInside(CSSValueConstants.AVOID_VALUE);
						area.setPageBreakBefore(CSSValueConstants.AVOID_VALUE);
					}
				}
			}
		}
		SplitResult ret = super.split(height, force);
		if (ret.status == SplitResult.SPLIT_SUCCEED_WITH_PART) {
			// This mean
			Iterator<IArea> i = children.iterator();
			while (i.hasNext()) {
				ContainerArea area = (ContainerArea) i.next();
				if (isInRepeatHeader(area) || "Caption".equals(area.getTagType())) {
					area.setArtifact();
				}
			}
		}
		return ret;
	}

	@Override
	protected boolean isValidResult(List<ContainerArea> result) {
		assert result != null;
		if (repeatList != null && !repeatList.isEmpty()) {
			if (result.size() > repeatList.size()) {
				return true;
			}
			int index = result.indexOf(repeatList.get(repeatList.size() - 1));
			if (index != -1 && result.size() - 1 > index) {
				return true;
			}
			return false;
		}
		return super.isValidResult(result);
	}

	protected abstract boolean needRepeat();

	/**
	 * Constructor
	 *
	 * @param area
	 */
	public RepeatableArea(RepeatableArea area) {
		super(area);
	}

	@Override
	public void add(AbstractArea area) {
		super.add(area);
		// cache repeat list;
		if (repeatList != null && isInRepeatHeader(area)) {
			repeatList.add(area);
		}
	}

	private boolean isInRepeatHeader(AbstractArea area) {
		IContent content = ((ContainerArea) area).getContent();
		if (content != null) {
			IElement parent = content.getParent();
			if (parent instanceof IBandContent) {
				int type = ((IBandContent) parent).getBandType();
				if (type == IBandContent.BAND_HEADER || type == IBandContent.BAND_GROUP_HEADER) {
					if (content instanceof IRowContent) {
						RowDesign rowDesign = (RowDesign) content.getGenerateBy();
						if (rowDesign == null || rowDesign.getRepeatable()) {
							return true;
						}
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

}
