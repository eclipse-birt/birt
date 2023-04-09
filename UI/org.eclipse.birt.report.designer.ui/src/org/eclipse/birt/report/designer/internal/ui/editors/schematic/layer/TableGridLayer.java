/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.layer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ImageConstants;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;

/**
 * Paint the grid
 *
 */
public class TableGridLayer extends GridLayer {

	private TableEditPart source;

	private Image img;

	private Dimension size = new Dimension();

	private Dimension propertySize = new Dimension();

	private int backgroundImageDPI = 0;

	/**
	 * Constructor
	 *
	 * @param source
	 */
	public TableGridLayer(TableEditPart source) {
		super();
		this.source = source;
	}

	/**
	 * Get the rows of the table
	 *
	 * @return rows
	 */
	public List<?> getRows() {
		return source.getRows();
	}

	/**
	 * Get the columns of the table
	 *
	 * @return columns
	 */
	public List<?> getColumns() {
		return source.getColumns();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.editparts.GridLayer#paintGrid(org.eclipse.draw2d.Graphics )
	 */
	@Override
	protected void paintGrid(Graphics g) {

		// Collections.sort( getRows( ), new NumberComparator( ) );
		// Collections.sort( getColumns( ), new NumberComparator( ) );

		if (!getColumns().isEmpty()) {
			drawColumns(g);
		}

		if (!getRows().isEmpty()) {
			drawRows(g);
		}

	}

	protected void drawRows(Graphics g) {
		Rectangle clip = g.getClip(Rectangle.SINGLETON);
		List<?> rows = getRows();
		int size = rows.size();
		int height = 0;
		for (int i = 0; i < size; i++) {
			int rowHeight = getRowHeight(rows.get(i));

			// if ( height < clip.y + clip.height )
			{
				// g.fillRectangle( clip.x, height, clip.x + clip.width, height
				// );
				drawBackgroud(rows.get(i), g, clip.x, height, clip.x + clip.width, rowHeight);

				drawBackgroudImage((DesignElementHandle) rows.get(i), g, clip.x, height, clip.x + clip.width,
						rowHeight);
			}
			height = height + rowHeight;
		}

	}

	private void drawBackgroudImage(DesignElementHandle handle, Graphics g, int x, int y, int width, int height) {
		String backGroundImage = getBackgroundImage(handle);

		if (backGroundImage != null) {
			Image image = null;
			String imageSourceType = DesignChoiceConstants.IMAGE_REF_TYPE_EMBED;

			// TODO: columns of table & grid missing the background image type property
			Object obj = handle.getProperty(IStyleModel.BACKGROUND_IMAGE_TYPE_PROP);
			if (obj instanceof String) {
				imageSourceType = obj.toString();
			}
			try {
				if (imageSourceType.equalsIgnoreCase(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED)) {
					// embedded image
					image = ImageManager.getInstance().getEmbeddedImage(this.source.getTableAdapter().getModuleHandle(),
							backGroundImage);
				} else {
					// URL image
					image = ImageManager.getInstance().getImage(this.source.getTableAdapter().getModuleHandle(),
							backGroundImage);
				}
			} catch (SWTException e) {
				// Should not be ExceptionHandler.handle(e), see SCR#73730
				image = null;
			}

			if (image != null) {

				this.backgroundImageDPI = getImageDpi(backGroundImage);

				int pxBackgroundHeight = 0;
				int pxBackgroundWidth = 0;
				String defaultUnit = this.source.getTableAdapter().getModuleHandle().getDefaultUnits(); // default: mm

				// calculate the background image height dimension
				String str = handle.getStringProperty(IStyleModel.BACKGROUND_SIZE_HEIGHT);
				if (!DesignChoiceConstants.BACKGROUND_SIZE_AUTO.equals(str)
						&& !DesignChoiceConstants.BACKGROUND_SIZE_COVER.equals(str)
						&& !DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(str)) {
					DimensionValue propertyBackgroundHeight = (DimensionValue) handle
							.getProperty(IStyleModel.BACKGROUND_SIZE_HEIGHT);
					DimensionValue backgroundHeight = DimensionUtil.convertTo(propertyBackgroundHeight.getMeasure(),
							defaultUnit, DesignChoiceConstants.UNITS_IN);
					pxBackgroundHeight = (int) MetricUtility.inchToPixel(backgroundHeight.getMeasure());
				}

				// calculate the background image width dimension
				str = handle.getStringProperty(IStyleModel.BACKGROUND_SIZE_WIDTH);
				if (!DesignChoiceConstants.BACKGROUND_SIZE_AUTO.equals(str)
						&& !DesignChoiceConstants.BACKGROUND_SIZE_COVER.equals(str)
						&& !DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(str)) {
					DimensionValue propertyBackgroundWidth = (DimensionValue) handle
							.getProperty(IStyleModel.BACKGROUND_SIZE_WIDTH);
					DimensionValue backgroundWidth = DimensionUtil.convertTo(propertyBackgroundWidth.getMeasure(),
							defaultUnit, DesignChoiceConstants.UNITS_IN);
					pxBackgroundWidth = (int) MetricUtility.inchToPixel(backgroundWidth.getMeasure());
				}

				this.setImage(image, pxBackgroundHeight, pxBackgroundWidth);

				Rectangle rectangle = new Rectangle(x, y, width, height);

				Object[] backGroundPosition = getBackgroundPosition(handle);
				int backGroundRepeat = getBackgroundRepeat(handle);

				Rectangle area = rectangle;
				int repeat = backGroundRepeat;
				int alignment = 0;
				Point position = new Point(-1, -1);
				Object xPosition = backGroundPosition[0];
				Object yPosition = backGroundPosition[1];
				org.eclipse.swt.graphics.Rectangle imageArea = image.getBounds();

				if (xPosition instanceof Integer) {
					position.x = ((Integer) xPosition).intValue();
				} else if (xPosition instanceof DimensionValue) {
					int percentX = (int) ((DimensionValue) xPosition).getMeasure();

					position.x = (area.width - imageArea.width) * percentX / 100;
				} else if (xPosition instanceof String) {
					alignment |= DesignElementHandleAdapter.getPosition((String) xPosition);
				}

				if (yPosition instanceof Integer) {
					position.y = ((Integer) yPosition).intValue();
				} else if (yPosition instanceof DimensionValue) {
					int percentY = (int) ((DimensionValue) yPosition).getMeasure();

					position.y = (area.height - imageArea.height) * percentY / 100;
				} else if (yPosition instanceof String) {
					alignment |= DesignElementHandleAdapter.getPosition((String) yPosition);
				}

				int tx, ty;
				Dimension size = this.size; // new Rectangle(image.getBounds()).getSize();

				// Calculates X
				if (position != null && position.x != -1) {
					tx = area.x + position.x;
				} else {
					switch (alignment & PositionConstants.EAST_WEST) {
					case PositionConstants.EAST:
						tx = area.x + area.width - size.width;
						break;
					case PositionConstants.WEST:
						tx = area.x;
						break;
					default:
						tx = (area.width - size.width) / 2 + area.x;
						break;
					}
				}

				// Calculates Y
				if (position != null && position.y != -1) {
					ty = area.y + position.y;
				} else {
					switch (alignment & PositionConstants.NORTH_SOUTH) {
					case PositionConstants.NORTH:
						ty = area.y;
						break;
					case PositionConstants.SOUTH:
						ty = area.y + area.height - size.height;
						break;
					default:
						ty = (area.height - size.height) / 2 + area.y;
						break;
					}
				}

				ArrayList<Point> xyList = createImageList(tx, ty, size, repeat, rectangle);

				Iterator<Point> iter = xyList.iterator();
				Rectangle rect = new Rectangle();
				g.getClip(rect);
				g.setClip(rectangle);

				Dimension imageSize = new Rectangle(image.getBounds()).getSize();
				while (iter.hasNext()) {
					Point point = iter.next();
					g.drawImage(image, 0, 0, imageSize.width, imageSize.height, point.x, point.y, size.width,
							size.height);
				}
				g.setClip(rect);
				xyList.clear();
			}
		}
	}

	private int getImageDpi(String backGroundImage) {
		if (!(this.source.getTableAdapter().getModuleHandle() != null)) {
			return 0;
		}
		int dpi = 96;
		DesignElementHandle model = this.source.getTableAdapter().getModuleHandle();

		InputStream in = null;
		URL temp = null;
		try {
			if (URIUtil.isValidResourcePath(backGroundImage)) {
				temp = ImageManager.getInstance().generateURL(model.getModuleHandle(),
						URIUtil.getLocalPath(backGroundImage));

			} else {
				temp = ImageManager.getInstance().generateURL(model.getModuleHandle(), backGroundImage);
			}
			if (temp != null) {
				in = temp.openStream();
			}

		} catch (IOException e) {
			in = null;
		}

		dpi = UIUtil.getImageResolution(in)[0];
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				ExceptionHandler.handle(e);
			}
		}
		return dpi;
	}

	/**
	 * Sets the Image that this ImageFigure displays.
	 *
	 * @param image                 The Image to be displayed. It can be null.
	 * @param backGroundImageHeight height of the image
	 * @param backGroundImageWidth  width of the image
	 */
	private void setImage(Image image, int backGroundImageHeight, int backGroundImageWidth) {
		if (img == image && propertySize.height == backGroundImageHeight
				&& propertySize.width == backGroundImageWidth) {
			return;
		}
		img = image;
		if (img != null) {
			propertySize.height = backGroundImageHeight;
			propertySize.width = backGroundImageWidth;
			if (backgroundImageDPI > 0 && backGroundImageHeight <= 0 && backGroundImageWidth > 0) {

				double inch = ((double) image.getBounds().height) / backgroundImageDPI;
				size.height = (int) MetricUtility.inchToPixel(inch);
				size.width = backGroundImageWidth;

			} else if (backgroundImageDPI > 0 && backGroundImageWidth <= 0 && backGroundImageHeight > 0) {

				double inch = ((double) image.getBounds().width) / backgroundImageDPI;
				size.width = (int) MetricUtility.inchToPixel(inch);
				size.height = backGroundImageHeight;

			} else if (backgroundImageDPI > 0 && (backGroundImageHeight <= 0 && backGroundImageWidth <= 0)) {

				double inch = ((double) image.getBounds().width) / backgroundImageDPI;
				size.width = (int) MetricUtility.inchToPixel(inch);

				inch = ((double) image.getBounds().height) / backgroundImageDPI;
				size.height = (int) MetricUtility.inchToPixel(inch);

			} else if (backGroundImageHeight > 0 && backGroundImageWidth > 0) {

				size.height = backGroundImageHeight;
				size.width = backGroundImageWidth;

			} else {
				size = new Rectangle(image.getBounds()).getSize();
			}
		} else {
			size = new Dimension();
		}
		revalidate();
		repaint();
	}

	/**
	 * Create the list of all the images to be displayed.
	 *
	 * @param x         the x-cordinator of the base image.
	 * @param y         the y-cordinator of the base image.
	 * @param size
	 * @param repeat
	 * @param rectangle
	 * @return the list of all the images to be displayed.
	 */
	private ArrayList<Point> createImageList(int x, int y, Dimension size, int repeat, Rectangle rectangle) {
		Rectangle area = rectangle;

		ArrayList<Point> yList = new ArrayList<Point>();

		if ((repeat & ImageConstants.REPEAT_Y) == 0) {
			yList.add(new Point(x, y));
		} else {
			int i = 0;
			while (y + size.height * i + size.height > area.y) {
				yList.add(new Point(x, y + size.height * i));
				i--;
			}

			i = 1;
			while (y + size.height * i < area.y + area.height) {
				yList.add(new Point(x, y + size.height * i));
				i++;
			}
		}

		ArrayList<Point> xyList = new ArrayList<Point>();

		Iterator<Point> iter = yList.iterator();
		while (iter.hasNext()) {
			Point point = iter.next();

			if ((repeat & ImageConstants.REPEAT_X) == 0) {
				xyList.add(point);
			} else {
				int i = 0;
				while (point.x + size.width * i + size.width > area.x) {
					xyList.add(new Point(point.x + size.width * i, point.y));
					i--;
				}

				i = 1;
				while (point.x + size.width * i < area.x + area.width) {
					xyList.add(new Point(point.x + size.width * i, point.y));
					i++;
				}
			}
		}
		yList.clear();

		return xyList;
	}

	private RowHandleAdapter getRowAdapter(DesignElementHandle handle) {
		return HandleAdapterFactory.getInstance().getRowHandleAdapter(handle);
	}

	private ColumnHandleAdapter getColumnAdapter(DesignElementHandle handle) {
		return HandleAdapterFactory.getInstance().getColumnHandleAdapter(handle);
	}

	private String getBackgroundImage(DesignElementHandle handle) {
		if (handle instanceof RowHandle && getRowAdapter(handle) != null) {
			return getRowAdapter(handle).getBackgroundImage(handle);
		}

		if (handle instanceof ColumnHandle && getColumnAdapter(handle) != null) {
			return getColumnAdapter(handle).getBackgroundImage(handle);
		}

		return ""; //$NON-NLS-1$
	}

	private Object[] getBackgroundPosition(DesignElementHandle handle) {
		if (handle instanceof RowHandle && getRowAdapter(handle) != null) {
			return getRowAdapter(handle).getBackgroundPosition(handle);
		}

		if (handle instanceof ColumnHandle && getColumnAdapter(handle) != null) {
			return getColumnAdapter(handle).getBackgroundPosition(handle);
		}

		return new Object[] { null, null };
	}

	private int getBackgroundRepeat(DesignElementHandle handle) {
		if (handle instanceof RowHandle && getRowAdapter(handle) != null) {
			return getRowAdapter(handle).getBackgroundRepeat(handle);
		}

		if (handle instanceof ColumnHandle && getColumnAdapter(handle) != null) {
			return getColumnAdapter(handle).getBackgroundRepeat(handle);
		}

		return 0;
	}

	protected void drawColumns(Graphics g) {
		g.setBackgroundColor(ReportColorConstants.greyFillColor);
		Rectangle clip = g.getClip(Rectangle.SINGLETON);
		List<?> columns = getColumns();
		int size = columns.size();
		int width = 0;
		for (int i = 0; i < size; i++) {
			int columnWidth = getColumnWidth(i + 1, columns.get(i));

			// if ( width < clip.x + clip.width )
			{
				// g.fillRectangle( width, clip.y, width, clip.y + clip.height
				// );
				drawBackgroud(columns.get(i), g, width, clip.y, columnWidth, clip.y + clip.height);

				drawBackgroudImage((DesignElementHandle) columns.get(i), g, width, clip.y, columnWidth,
						clip.y + clip.height);
			}
			width = width + columnWidth;
		}

	}

	private int getRowHeight(Object row) {
		return TableUtil.caleVisualHeight(source, row);
	}

	private int getColumnWidth(int columnIndex, Object column) {
		return TableUtil.caleVisualWidth(source, columnIndex, column);
	}

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 */
	private void drawBackgroud(Object model, Graphics g, int x, int y, int width, int height) {
		assert model instanceof DesignElementHandle;

		DesignElementHandle handle = (DesignElementHandle) model;
		Object obj = handle.getProperty(IStyleModel.BACKGROUND_COLOR_PROP);

		if (obj != null) {
			Rectangle rect = new Rectangle(x, y, width, height);

			int color;
			// if ( obj instanceof String )
			// {
			// color = ColorUtil.parseColor( (String) obj );
			// }
			// else
			// {
			// color = ( (Integer) obj ).intValue( );
			// }
			color = handle.getPropertyHandle(IStyleModel.BACKGROUND_COLOR_PROP).getIntValue();
			g.setBackgroundColor(ColorManager.getColor(color));
			g.fillRectangle(rect);
		}
	}

//	/**
//	 * Sorter to be used to sort the rows with row number
//	 *
//	 */
//	public static class NumberComparator implements Comparator
//	{
//
//		/*
//		 * (non-Javadoc)
//		 *
//		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
//		 */
//		public int compare( Object o1, Object o2 )
//		{
//			// TODO: sort the row with row number
//			return 0;
//		}
//	}
}
