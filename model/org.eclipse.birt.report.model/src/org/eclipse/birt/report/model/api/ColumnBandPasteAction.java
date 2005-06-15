
package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.TableColumn;

public class ColumnBandPasteAction extends ColumnBandCopyAction
{

	/**
	 * 
	 */

	public ColumnBandPasteAction( ColumnBandAdapter adapter )
	{
		super( adapter );
	}

	/**
	 * Checks whether the paste operation can be done with the given copied
	 * column band data, the column index and the operation flag.
	 * 
	 * @param columnIndex
	 *            the column index
	 * @param inForce
	 *            <code>true</code> indicates to paste the column regardless
	 *            of the different layout of cells. <code>false</code>
	 *            indicates not.
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean canPaste( int columnIndex, boolean inForce,
			ColumnBandData data )
	{
		List cells = data.getCells( );

		List originalCells = getCellsContextInfo( adapter
				.getCellsUnderColumn( columnIndex ) );

		if ( !isRectangleArea( originalCells, 1 ) )
			return false;

		boolean isSameLayout = false;

		try
		{
			isSameLayout = isSameLayout( cells, originalCells );
		}
		catch ( SemanticException e )
		{
			return false;
		}

		if ( !inForce && !isSameLayout )
			return false;

		return true;
	}

	/**
	 * Pastes a column to the given <code>target</code>.
	 * 
	 * @param columnIndex
	 *            the column number
	 * @param inForce
	 *            <code>true</code> if paste regardless of the differece of
	 *            cell layouts, otherwise <code>false</code>.
	 * @return a list containing post-parsing errors. Each element in the list
	 *         is <code>ErrorDetail</code>.
	 * @throws SemanticException
	 *             if layouts of slots are different. Or, <code>inForce</code>
	 *             is <code>false</code> and the layout of cells are
	 *             different.
	 */

	protected List pasteColumnBand( int columnIndex, boolean inForce,
			ColumnBandData data ) throws SemanticException
	{
		boolean canDone = canPaste( columnIndex, inForce, data );

		if ( inForce && !canDone )
			throw new SemanticError( adapter.getElementHandle( ).getElement( ),
					new String[]{adapter.getElementHandle( ).getName( )},
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN );

		if ( !inForce && !canDone )
			throw new SemanticError(
					adapter.getElementHandle( ).getElement( ),
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_DIFFERENT_LAYOUT );

		TableColumn column = data.getColumn( );
		List cells = data.getCells( );
		List originalCells = getCellsContextInfo( adapter
				.getCellsUnderColumn( columnIndex ) );

		try
		{
			adapter.getDesign( ).getActivityStack( ).startTrans( );
			pasteColumn( column, columnIndex, false );
			pasteCells( cells, originalCells, columnIndex, false );
		}
		catch ( SemanticException e )
		{
			adapter.getDesign( ).getActivityStack( ).rollback( );
			throw e;
		}
		adapter.getDesign( ).getActivityStack( ).commit( );

		return doPostPasteCheck( column, cells );
	}

}
