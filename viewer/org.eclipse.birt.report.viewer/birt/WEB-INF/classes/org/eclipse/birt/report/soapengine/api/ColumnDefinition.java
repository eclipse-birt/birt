package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Arrays;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ColumnDefinition")
@XmlAccessorType(XmlAccessType.FIELD)
public class ColumnDefinition implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Index")
	private Integer index;
	@XmlElement(name = "Iid")
	private String iid;
	@XmlElement(name = "Header")
	private String header;
	@XmlElement(name = "DataType")
	private Integer dataType;
	@XmlElement(name = "Expr")
	private String expr;
	@XmlElement(name = "NewIndex")
	private Integer newIndex;
	@XmlElement(name = "IsGrouped")
	private Boolean isGrouped;
	@XmlElement(name = "SortDir")
	private SortingDirection sortDir;
	@XmlElement(name = "Aggregate")
	private AggregateDefinition[] aggregate;
	@XmlElement(name = "Font")
	private Font font;
	@XmlElement(name = "Format")
	private Format format;
	@XmlElement(name = "Properties")
	private ColumnProperties properties;
	@XmlElement(name = "FormatRuleSet")
	private FormatRuleSet formatRuleSet;
	@XmlElement(name = "BoundDataColumn")
	private BoundDataColumn boundDataColumn;

	public ColumnDefinition() {
	}

	public ColumnDefinition(Integer index, String iid, String header, Integer dataType, String expr, Integer newIndex,
			Boolean isGrouped, SortingDirection sortDir, AggregateDefinition[] aggregate, Font font, Format format,
			ColumnProperties properties, FormatRuleSet formatRuleSet, BoundDataColumn boundDataColumn) {
		this.index = index;
		this.iid = iid;
		this.header = header;
		this.dataType = dataType;
		this.expr = expr;
		this.newIndex = newIndex;
		this.isGrouped = isGrouped;
		this.sortDir = sortDir;
		this.aggregate = aggregate;
		this.font = font;
		this.format = format;
		this.properties = properties;
		this.formatRuleSet = formatRuleSet;
		this.boundDataColumn = boundDataColumn;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public Integer getNewIndex() {
		return newIndex;
	}

	public void setNewIndex(Integer newIndex) {
		this.newIndex = newIndex;
	}

	public Boolean getIsGrouped() {
		return isGrouped;
	}

	public void setIsGrouped(Boolean isGrouped) {
		this.isGrouped = isGrouped;
	}

	public SortingDirection getSortDir() {
		return sortDir;
	}

	public void setSortDir(SortingDirection sortDir) {
		this.sortDir = sortDir;
	}

	public AggregateDefinition[] getAggregate() {
		return aggregate;
	}

	public void setAggregate(AggregateDefinition[] aggregate) {
		this.aggregate = aggregate;
	}

	public AggregateDefinition getAggregate(int i) {
		return this.aggregate[i];
	}

	public void setAggregate(int i, AggregateDefinition value) {
		this.aggregate[i] = value;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public ColumnProperties getProperties() {
		return properties;
	}

	public void setProperties(ColumnProperties properties) {
		this.properties = properties;
	}

	public FormatRuleSet getFormatRuleSet() {
		return formatRuleSet;
	}

	public void setFormatRuleSet(FormatRuleSet formatRuleSet) {
		this.formatRuleSet = formatRuleSet;
	}

	public BoundDataColumn getBoundDataColumn() {
		return boundDataColumn;
	}

	public void setBoundDataColumn(BoundDataColumn boundDataColumn) {
		this.boundDataColumn = boundDataColumn;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ColumnDefinition))
			return false;
		if (this == obj)
			return true;
		ColumnDefinition other = (ColumnDefinition) obj;
		return (index == null ? other.index == null : index.equals(other.index))
				&& (iid == null ? other.iid == null : iid.equals(other.iid))
				&& (header == null ? other.header == null : header.equals(other.header))
				&& (dataType == null ? other.dataType == null : dataType.equals(other.dataType))
				&& (expr == null ? other.expr == null : expr.equals(other.expr))
				&& (newIndex == null ? other.newIndex == null : newIndex.equals(other.newIndex))
				&& (isGrouped == null ? other.isGrouped == null : isGrouped.equals(other.isGrouped))
				&& (sortDir == null ? other.sortDir == null : sortDir.equals(other.sortDir))
				&& Arrays.equals(aggregate, other.aggregate)
				&& (font == null ? other.font == null : font.equals(other.font))
				&& (format == null ? other.format == null : format.equals(other.format))
				&& (properties == null ? other.properties == null : properties.equals(other.properties))
				&& (formatRuleSet == null ? other.formatRuleSet == null : formatRuleSet.equals(other.formatRuleSet))
				&& (boundDataColumn == null ? other.boundDataColumn == null
						: boundDataColumn.equals(other.boundDataColumn));
	}

	@Override
	public int hashCode() {
		int _hashCode = 1;
		_hashCode += (index != null ? index.hashCode() : 0);
		_hashCode += (iid != null ? iid.hashCode() : 0);
		_hashCode += (header != null ? header.hashCode() : 0);
		_hashCode += (dataType != null ? dataType.hashCode() : 0);
		_hashCode += (expr != null ? expr.hashCode() : 0);
		_hashCode += (newIndex != null ? newIndex.hashCode() : 0);
		_hashCode += (isGrouped != null ? isGrouped.hashCode() : 0);
		_hashCode += (sortDir != null ? sortDir.hashCode() : 0);
		if (aggregate != null) {
			for (AggregateDefinition a : aggregate) {
				_hashCode += (a != null ? a.hashCode() : 0);
			}
		}
		_hashCode += (font != null ? font.hashCode() : 0);
		_hashCode += (format != null ? format.hashCode() : 0);
		_hashCode += (properties != null ? properties.hashCode() : 0);
		_hashCode += (formatRuleSet != null ? formatRuleSet.hashCode() : 0);
		_hashCode += (boundDataColumn != null ? boundDataColumn.hashCode() : 0);
		return _hashCode;
	}
}
