
package org.eclipse.birt.report.engine.layout.pdf;

/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.content.ItemExecutorWrapper;
import org.eclipse.birt.report.engine.layout.content.LineStackingExecutor;

public class PDFTextBlockContainerLM extends PDFBlockContainerLM implements IBlockStackingLayoutManager {
	protected int widows = 0;
	protected int orphans = 0;

	protected int current = 0;
	protected int size = 0;

	protected ArrayList lines = new ArrayList();

	protected boolean finished = false;

	public PDFTextBlockContainerLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		child = new PDFLineAreaLM(context, this,
				new LineStackingExecutor(new ItemExecutorWrapper(executor, content), executor));
		widows = getWidows();
		orphans = getOrphans();
	}

	protected boolean traverseChildren() throws BirtException {
		// To support widows/orphans, we need cache all lines.
		if (!finished) {
			if (!traverseSingleChild()) {
				finished = true;
			} else {
				return true;
			}
		}
		if (!layoutLines()) {
			clearCache();
			return false;
		}
		return true;
	}

	protected boolean layoutLines() {
		Iterator iter = lines.iterator();
		while (iter.hasNext()) {
			boolean keepWithNext = false;
			IArea area = (IArea) iter.next();
			if (current < orphans - 1 || current > size - widows - 1) {
				keepWithNext = true;
			}
			if (!super.addArea(area, false, keepWithNext)) {
				return true;
			} else {
				iter.remove();
				current++;
			}
		}
		return false;
	}

	protected void closeExecutor() {

	}

	public boolean addArea(IArea area, boolean keepWithPrevious, boolean keepWithNext) {
		lines.add(area);
		size++;
		return true;
	}

	protected static class WOManager {
		protected int widows;
		protected int orphans;
		protected boolean isFirst;
		protected int maxHeight;

		protected CompositeArea lines = new CompositeArea();

		public WOManager(int widows, int orphans, int maxHeight) {
			this.widows = widows;
			this.orphans = orphans;
			this.maxHeight = maxHeight;
		}

		public void add(IArea area) {
			lines.add(area);
		}

		public boolean isEmpty() {
			return lines.isEmpty();
		}

		protected void makeWOAvailable() {

		}

		protected CompositeArea getAreas(int minCount, int maxCount, int minHeight, int maxHeight) {
			return null;
		}

		public CompositeArea getAreas(int heightHint) {
			int size = lines.size();
			if (isFirst) {
				isFirst = false;
				if (size <= widows + orphans) {
					return getAreas(size, size, heightHint, maxHeight);
				} else {
					return getAreas(widows, size - orphans, heightHint, maxHeight);
				}
			} else {
				if (size <= orphans) {
					return getAreas(orphans, orphans, heightHint, maxHeight);
				} else {
					return getAreas(1, size - orphans, heightHint, maxHeight);
				}
			}

		}
	}

	protected int getWidows() {
		IStyle style = content.getStyle();
		String widows = style.getWidows();
		if (widows != null) {
			try {
				return Integer.parseInt(widows);
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, "invalid widows: {0}", widows); //$NON-NLS-1$
			}
		}
		return 0;
	}

	protected int getOrphans() {
		IStyle style = content.getStyle();
		String orphans = style.getOrphans();
		if (orphans != null) {
			try {
				return Integer.parseInt(orphans);
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, "invalid orphans: {0}", orphans); //$NON-NLS-1$
			}
		}
		return 0;
	}
}
