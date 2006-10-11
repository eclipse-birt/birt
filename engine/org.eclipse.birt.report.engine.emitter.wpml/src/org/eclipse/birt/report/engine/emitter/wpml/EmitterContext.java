package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IStyle;

public class EmitterContext
{	
	public void addForeign(IForeignContent content)
	{
		foreign = content;
	}
	
	public IForeignContent getForeignContent()
	{
		return foreign;
	}
	
	public void clearForeignContent()
	{
		foreign = null;
	}	

	public void addWidth ( int w )
	{
		wlist.addLast ( new Integer ( w ) );
	}

	public int getCurrentWidth ( )
	{
		return ((Integer) wlist.getLast ( )).intValue ( );
	}

	public void removeWidth ( )
	{
		wlist.removeLast ( );
	}

	public int[] getCurrentTblCols ( )
	{
		return ((TableInfo) tbls.getLast ( )).getCWidths ( );
	}

	public void addTable ( int[] cols, IStyle style )
	{
		tbls.addLast ( new TableInfo ( cols, style ) );
	}
	
	public IStyle getTableStyle()
	{
		return ((TableInfo) tbls.getLast ( )).getTableStyle ( );
	}

	public void newRow ( )
	{
		((TableInfo) tbls.getLast ( )).newRow ( );
	}

	public void addSpan ( int col, int cs, int cw, int height, IStyle style )
	{
		((TableInfo) tbls.getLast ( )).addSpan ( col, cs, cw, height, style );
	}

	public int getCurrentRow ( )
	{
		return ((TableInfo) tbls.getLast ( )).getRow ( );
	}

	public void removeTable ( )
	{
		tbls.removeLast ( );
	}

	public List getSpans ( int col )
	{
		return ((TableInfo) tbls.getLast ( )).getSpans ( col );
	}

	public int getCellWidth ( int col, int cs )
	{
		int[] cols = getCurrentTblCols ( );

		int w = 0;

		for (int i = col; i < col + cs; i++)
		{
			w += cols[i];
		}

		return w;
	}	
	
	class TableInfo
	{
		TableInfo ( int[] cols, IStyle style )
		{
			this.cols = cols;
			this.style = style;
		}

		void newRow ( )
		{
			this.crow++;
		}

		void addSpan ( int y, int cs, int cw, int height, IStyle style )
		{
			for (int i = 1; i < height; i++)
			{
				Integer key = new Integer ( crow + i );

				if (spans.containsKey ( key ))
				{
					List rSpan = (List) spans.get ( key );
					rSpan.add ( new SpanInfo ( y, cs, cw, false, style ) );
					Collections.sort ( rSpan, new Comparator ( )
					{
						public int compare ( Object obj1, Object obj2 )
						{
							SpanInfo r1 = (SpanInfo) obj1;
							SpanInfo r2 = (SpanInfo) obj2;

							return r1.x - r2.x;
						}
					} );
				}
				else
				{
					Vector rSpan = new Vector ( );
					rSpan.add ( new SpanInfo ( y, cs, cw, false, style ) );
					spans.put ( key, rSpan );
				}
			}
		}

		List getSpans ( int end )
		{
			List cSpans = (List) spans.get ( new Integer ( crow ) );

			if (cSpans == null)
			{
				return null;
			}

			Vector cList = new Vector ( );

			int pos = -1;

			for (int i = 0; i < cSpans.size ( ); i++)
			{
				SpanInfo r = (SpanInfo) cSpans.get ( i );

				if ((r.x + r.cs - 1) <= end)
				{

					cList.add ( r );

					pos = i;
				}
				else
				{
					break;
				}
			}

			for (int i = 0; i <= pos; i++)
			{
				cSpans.remove ( 0 );
			}

			if (cSpans.size ( ) == 0)
			{
				removeSpan ( );
			}

			return cList.size ( ) == 0 ? null : cList;
		}

		public void removeSpan ( )
		{
			spans.remove ( new Integer ( crow ) );
		}

		int[] getCWidths ( )
		{
			return cols;
		}

		int getRow ( )
		{
			return crow;
		}
		
		IStyle getTableStyle() 
		{
			return this.style;
		}
		

		private Hashtable spans = new Hashtable ( );

		private int[] cols;

		private int crow = 0;
		
		IStyle style = null;
	}

	private LinkedList tbls = new LinkedList ( );

	boolean empty = false;

	private LinkedList wlist = new LinkedList ( );
	
	private IForeignContent foreign = null;	
	
}
