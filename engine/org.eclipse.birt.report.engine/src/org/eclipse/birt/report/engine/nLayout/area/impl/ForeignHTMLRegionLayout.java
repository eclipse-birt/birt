/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.LayoutEngine;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;

public class ForeignHTMLRegionLayout implements ILayout {
	private ContainerArea parent;
	private IForeignContent content;
	private LayoutContext context;

	public ForeignHTMLRegionLayout(ContainerArea parent, LayoutContext context, IForeignContent foreign) {
		this.parent = parent;
		this.content = foreign;
		this.context = context;
	}

	public void layout() throws BirtException {
		LayoutContext regionLayoutContext = new LayoutContext();
		regionLayoutContext.setFormat("pdf");
		regionLayoutContext.setFixedLayout(true);
		regionLayoutContext.setLocale(context.getLocale());
		regionLayoutContext.setHtmlLayoutContext(context.getHtmlLayoutContext());
		regionLayoutContext.setMaxBP(Integer.MAX_VALUE);
		regionLayoutContext.setMaxHeight(Integer.MAX_VALUE);
		regionLayoutContext.setReport(context.getReport());

		ForeignHtmlRegionArea region = new ForeignHtmlRegionArea(content, regionLayoutContext);
		region.setParent(parent);
		ForeignHTMLRegionLayoutEngine regionLayoutEngine = new ForeignHTMLRegionLayoutEngine(region,
				regionLayoutContext);

		regionLayoutEngine.layout(content);

		if (parent != null) {
			parent.add(region);
			if (!parent.isInInlineStacking && context.isAutoPageBreak()) {
				int aHeight = region.getAllocatedHeight();
				if (aHeight + parent.getAbsoluteBP() > context.getMaxBP()) {
					parent.autoPageBreak();
					// The RootArea's autoPageBreak() will update the children.
					// So return to avoid updating current area into RootArea
					// twice.
					if (parent instanceof RootArea) {
						return;
					}
				}
			}
			parent.update(region);
		}

	}

	class ForeignHTMLRegionLayoutEngine extends LayoutEngine
	{

		public ForeignHTMLRegionLayoutEngine( ContainerArea container,
				LayoutContext context )
		{
			super( context );
			current = container;
			if ( parent != null )
			{
				current.setMaxAvaWidth( parent.getMaxAvaWidth( ) );
			}
		}

		public void layout( IContent content ) throws BirtException
		{
			current.initialize( );
			if ( current.getSpecifiedHeight( ) <= 0 )
			{
				visitChildren( content, this );
			}
			current.close( );
		}
	}

}
