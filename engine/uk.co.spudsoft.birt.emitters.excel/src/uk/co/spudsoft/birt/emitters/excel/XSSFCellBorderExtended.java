package uk.co.spudsoft.birt.emitters.excel;

import java.util.Objects;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.model.ThemesTable;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;

/**
 * Intermediate class to extend the cell style information of XSSFCellBorder to
 * extend the behavior of the diagonal and antidiagonal handling
 *
 * extended class based on usage of apache POI 4.1.1, later apache POI versions
 * have improvements and with the usage of newer version this class should be
 * verified of validity
 *
 * @author Thomas Gutmann
 * @since 4.13
 *
 */
public class XSSFCellBorderExtended extends XSSFCellBorder {

	private Boolean diagonalBorder = false;
	private Boolean antidiagonalBorder = false;
	private XSSFColor diagonalColor = null;
	private XSSFColor antidiagonalColor = null;
	private BorderStyle diagonalStyle = null;
	private BorderStyle antidiagonalStyle = null;

	/**
	 * Constructor
	 *
	 * @param ct       cell table border object
	 * @param theme    themes of sheet
	 * @param colorMap color map of sheet
	 */
	public XSSFCellBorderExtended(CTBorder ct, ThemesTable theme, IndexedColorMap colorMap) {
		super(ct, theme, colorMap);
	}

	/**
	 * Set the usage of diagonal line
	 *
	 * @param use is diagonal used
	 */
	public void setDiagonal(Boolean use) {
		this.diagonalBorder = use;
	}

	/**
	 * Set the usage of antidiagonal line
	 *
	 * @param use is antidiagonal used
	 */
	public void setAntidiagonal(Boolean use) {
		this.antidiagonalBorder = use;
	}

	/**
	 * Get the usage of the diagonal
	 *
	 * @return Return the usage of the diagonal
	 */
	public Boolean isSetDiagonal() {
		return this.diagonalBorder;
	}

	/**
	 * Get the usage of the antidiagonal
	 *
	 * @return Return the usage of the antidiagonal
	 */
	public Boolean isSetAntidiagonal() {
		return this.antidiagonalBorder;
	}

	/**
	 * Set the color of the diagonal
	 *
	 * @param color color of the diagonal
	 */
	public void setDiagonalColor(XSSFColor color) {
		this.diagonalColor = color;
	}

	/**
	 * Set the color of the antidiagonal
	 *
	 * @param color color of the antidiagonal
	 */
	public void setAntidiagonalColor(XSSFColor color) {
		this.antidiagonalColor = color;
	}

	/**
	 * Set the style of the diagonal
	 *
	 * @param style style of the diagonal
	 */
	public void setDiagonalStyle(BorderStyle style) {
		this.diagonalStyle = style;
	}

	/**
	 * Set the style of the antidiagonal
	 *
	 * @param style style of the antidiagonal
	 */
	public void setAntidiagonalStyle(BorderStyle style) {
		this.antidiagonalStyle = style;
	}

	/**
	 * Get the color of the diagonal
	 *
	 * @return Return the color of the diagonal
	 */
	public XSSFColor getDiagonalColor() {
		return this.diagonalColor;
	}

	/**
	 * Get the color of the antidiagonal
	 *
	 * @return Return the color of the antidiagonal
	 */
	public XSSFColor getAntidiagonalColor() {
		return this.antidiagonalColor;
	}

	/**
	 * Get the style of the antidiagonal
	 *
	 * @return Return the style of the diagonal
	 */
	public BorderStyle getDiagonalStyle() {
		return this.diagonalStyle;
	}

	/**
	 * Get the style of the antidiagonal
	 *
	 * @return Return the style of the antidiagonal
	 */
	public BorderStyle getAntidiagonalStyle() {
		return this.antidiagonalStyle;
	}

	/*
	 * compare the given instance with the given object, due to missing diagonal
	 * handling on compare side of api apache POI version 4.1.1
	 *
	 * @return Return the result of the compare
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof XSSFCellBorderExtended))
			return false;
		XSSFCellBorderExtended cf = (XSSFCellBorderExtended) o;

		boolean equal = true;
		for (BorderSide side : BorderSide.values()) {
			if (!Objects.equals(this.getBorderColor(side), cf.getBorderColor(side))
					|| !Objects.equals(this.getBorderStyle(side), cf.getBorderStyle(side))) {
				equal = false;
				break;
			}
		}

		if (equal) {
			if (!Objects.equals(this.getDiagonalColor(), cf.getDiagonalColor())
					|| !Objects.equals(this.getDiagonalStyle(), cf.getDiagonalStyle())
					|| !Objects.equals(this.getAntidiagonalColor(), cf.getAntidiagonalColor())
					|| !Objects.equals(this.getAntidiagonalStyle(), cf.getAntidiagonalStyle())) {
				equal = false;
			}
		}
		return equal;
	}
}
