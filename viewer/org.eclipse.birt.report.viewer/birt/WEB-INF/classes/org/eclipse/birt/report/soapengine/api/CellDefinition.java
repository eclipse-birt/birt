package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CellDefinition")
@XmlAccessorType(XmlAccessType.FIELD)
public class CellDefinition implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Level")
	private int level;
	@XmlElement(name = "IsHeader")
	private boolean isHeader;
	@XmlElement(name = "RowIndex")
	private int rowIndex;
	@XmlElement(name = "CellIndex")
	private int cellIndex;
	@XmlElement(name = "Font")
	private Font font;
	@XmlElement(name = "Format")
	private Format format;
	@XmlElement(name = "Properties")
	private ColumnProperties properties;
	@XmlElement(name = "Alignment")
	private Alignment alignment;

	public CellDefinition() {
	}

	public CellDefinition(int level, boolean isHeader, int rowIndex, int cellIndex, Font font, Format format,
			ColumnProperties properties, Alignment alignment) {
		this.level = level;
		this.isHeader = isHeader;
		this.rowIndex = rowIndex;
		this.cellIndex = cellIndex;
		this.font = font;
		this.format = format;
		this.properties = properties;
		this.alignment = alignment;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getCellIndex() {
		return cellIndex;
	}

	public void setCellIndex(int cellIndex) {
		this.cellIndex = cellIndex;
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

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof CellDefinition))
			return false;
		CellDefinition that = (CellDefinition) o;
		return level == that.level && isHeader == that.isHeader && rowIndex == that.rowIndex
				&& cellIndex == that.cellIndex && Objects.equals(font, that.font) && Objects.equals(format, that.format)
				&& Objects.equals(properties, that.properties) && Objects.equals(alignment, that.alignment);
	}

	@Override
	public int hashCode() {
		return Objects.hash(level, isHeader, rowIndex, cellIndex, font, format, properties, alignment);
	}

	@Override
	public String toString() {
		return "CellDefinition{" + "level=" + level + ", isHeader=" + isHeader + ", rowIndex=" + rowIndex
				+ ", cellIndex=" + cellIndex + ", font=" + font + ", format=" + format + ", properties=" + properties
				+ ", alignment=" + alignment + '}';
	}
}
