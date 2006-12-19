/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.birt.report.engine.content.IStyle;

public class EmitterContext
{	
	public void startInline()
	{
		inline = true;
	}
	
	public boolean isInline()
	{
		return inline;
	}
	
	public void endInline()
	{
		inline = false;
	}
	
	public void startCell()
	{
		cellind.push( Boolean.TRUE );
	}
	
	public void endCell()
	{
		cellind.pop( );
	}
	
	public boolean needEmptyP()
	{
		return ((Boolean)cellind.peek( )).booleanValue( );
	}
	
	public void addContainer(boolean isContainer)
	{
		if(!cellind.isEmpty( ))
		{
			cellind.pop( );
			cellind.push(new Boolean(isContainer));
		}	
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
	
	Stack cellind = new Stack();
	
	boolean inline = false;
}
