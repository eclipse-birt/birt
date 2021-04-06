package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;

public class TableGroupContent extends GroupContent implements ITableGroupContent {
	TableGroupContent(ITableGroupContent group) {
		super(group);
	}

	TableGroupContent(IReportContent report) {
		super(report);
	}

	public int getContentType() {
		return TABLE_GROUP_CONTENT;
	}

	public Object accept(IContentVisitor visitor, Object value) throws BirtException {
		return visitor.visitTableGroup(this, value);
	}

	protected IContent cloneContent() {
		return new TableGroupContent(this);
	}
}
