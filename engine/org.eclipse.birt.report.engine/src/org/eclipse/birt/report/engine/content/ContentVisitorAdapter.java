
package org.eclipse.birt.report.engine.content;

import org.eclipse.birt.core.exception.BirtException;

public class ContentVisitorAdapter implements IContentVisitor {

	public Object visit(IContent content, Object value) throws BirtException {
		return content.accept(this, value);
	}

	public Object visitContent(IContent content, Object value) throws BirtException {
		return value;
	}

	public Object visitPage(IPageContent page, Object value) throws BirtException {
		return visitContent(page, value);
	}

	public Object visitContainer(IContainerContent container, Object value) throws BirtException {
		return visitContent(container, value);
	}

	public Object visitTable(ITableContent table, Object value) throws BirtException {
		return visitContent(table, value);
	}

	public Object visitTableBand(ITableBandContent tableBand, Object value) throws BirtException {
		return visitContent(tableBand, value);
	}

	public Object visitList(IListContent list, Object value) throws BirtException {
		return visitContainer(list, value);
	}

	public Object visitListBand(IListBandContent listBand, Object value) throws BirtException {
		return visitContainer(listBand, value);
	}

	public Object visitRow(IRowContent row, Object value) throws BirtException {
		return visitContent(row, value);
	}

	public Object visitCell(ICellContent cell, Object value) throws BirtException {
		return visitContainer(cell, value);
	}

	public Object visitText(ITextContent text, Object value) throws BirtException {
		return visitContent(text, value);
	}

	public Object visitLabel(ILabelContent label, Object value) throws BirtException {
		return visitText(label, value);
	}

	public Object visitAutoText(IAutoTextContent autoText, Object value) throws BirtException {
		return visitText(autoText, value);
	}

	public Object visitData(IDataContent data, Object value) throws BirtException {
		return visitText(data, value);
	}

	public Object visitImage(IImageContent image, Object value) throws BirtException {
		return visitContent(image, value);
	}

	public Object visitForeign(IForeignContent content, Object value) throws BirtException {
		return visitContent(content, value);
	}

	public Object visitGroup(IGroupContent group, Object value) throws BirtException {
		return visitContent(group, value);
	}

	public Object visitListGroup(IListGroupContent group, Object value) throws BirtException {
		return visitGroup(group, value);
	}

	public Object visitTableGroup(ITableGroupContent group, Object value) throws BirtException {
		return visitGroup(group, value);
	}

}