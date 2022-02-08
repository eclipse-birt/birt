/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import org.apache.poi.ss.usermodel.Font;

/**
 * <p>
 * Class to capture the RichText information needed for nested (and HTML) cells.
 * </p>
 * <p>
 * In theory this information could be captured using the RichTextString class
 * from POI, but experiments found that to produce NullPoiiunterExceptions and
 * multiple entries in the XLSX files.
 * </p>
 * 
 * @author jtalbut
 *
 */
public class RichTextRun {
	/**
	 * The index of the first character to be formatted using this font.
	 */
	public int startIndex;
	/**
	 * The font to apply to characters following this.
	 */
	public Font font;

	public RichTextRun(int startIndex, Font font) {
		super();
		this.startIndex = startIndex;
		this.font = font;
	}

	/**
	 * For debug purposes.
	 */
	@Override
	public String toString() {
		return "RichTextRun [" + startIndex + ", " + font.toString().replaceAll("\n", "") + "]";
	}

}
