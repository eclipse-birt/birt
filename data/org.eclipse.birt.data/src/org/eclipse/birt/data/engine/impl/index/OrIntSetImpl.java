/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.index;

import it.uniroma3.mat.extendedset.intset.ConciseSet;
import it.uniroma3.mat.extendedset.intset.IntSet;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * This class use IntSet iterator to enhance batch OR operations speed.
 * 
 * For Non-batch OR operation upon massive data, the IntSet's own OR operation should be used to optimize the performance.
 *  
 * @author lzhu
 *
 */
public class OrIntSetImpl implements IntSet
{
	private IntSet[] data;
	public OrIntSetImpl( IntSet[] data )
	{
		this.data = data;
		
	}
	@Override
	public int compareTo(IntSet o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntSet intersection(IntSet other) {
		if( this.data.length == 1 )
		{
			return this.data[0].intersection( other );
		}
		
		IntIterator it = this.iterator();
		ConciseSet cs = new ConciseSet();
		
	
		while( it.hasNext() )
		{
			cs.add(it.next());
		}
	
		IntSet result =  cs.intersection( other );
	
		return result;
	}

	@Override
	public IntSet union(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntSet difference(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntSet symmetricDifference(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntSet complemented() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void complement() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAny(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAtLeast(IntSet other, int minElements) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int intersectionSize(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int unionSize(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int symmetricDifferenceSize(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int differenceSize(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int complementSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntSet empty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double bitmapCompressionRatio() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double collectionCompressionRatio() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntIterator iterator() {
		
		IntIterator[] its = new IntIterator[this.data.length];
		for( int i = 0; i < its.length; i++ )
		{
			its[i] = this.data[i].iterator( );
		}
		
		IntIterator[] result = getMergeNode( its );
		while( !(result.length == 1 || (result.length == 2 && result[1] == null)) )
		{
			result = getMergeNode( result );
		}
		IntIterator it =  result[0];
		if( it == null )
		{
			it = new ConciseSet().iterator( );
		}
		return it;
	}
	
	/**
	 * Create a merge join tree to ensure least number of comparing needed
	 * @param data
	 * @return
	 */
	private IntIterator[] getMergeNode( IntIterator[] data )
	{
		IntIterator[] result = new IntIterator[data.length/2+1];
		for( int i = 0; i < data.length; i++ )
		{
			IntIterator left = data[i];
			i++;
			if( i >= data.length || data[i] == null )
			{
				result[(i-1)/2] = left;
			}
			else
			{
				result[(i-1)/2] = new MergeNode( left,data[i]);
			}
		}
		return result;
	}
	
	
	@Override
	public IntIterator descendingIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String debugInfo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void fill(int from, int to) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear(int from, int to) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void flip(int e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int get(int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(int e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntSet convert(int... a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntSet convert(Collection<Integer> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int first() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int last() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		for( IntSet temp : this.data )
		{
			if( !temp.isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public boolean contains(int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(IntSet c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(IntSet c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(IntSet c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(IntSet c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
		
	}
	
	public IntSet clone()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int[] toArray(int[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<? extends IntSet> powerSet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<? extends IntSet> powerSet(int min, int max) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int powerSetSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int powerSetSize(int min, int max) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double jaccardSimilarity(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double jaccardDistance(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double weightedJaccardSimilarity(IntSet other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double weightedJaccardDistance(IntSet other) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Warning: Do not change the implementation of this class unless absolutely sure. Any change to it may lead to 
	 * serious performance regression.
	 * 
	 * After change this class, make sure that the BTree index performance test cases are run by Perf Team.
	 * 
	 * @author lzhu
	 *
	 */
	private class MergeNode implements IntIterator
	{
		private IntIterator left;
		private IntIterator right;
		private int leftValue;
		private int rightValue;
		private boolean notStart = true;
		
		public MergeNode( IntIterator left, IntIterator right )
		{
			this.left = left;
			this.right = right;

		}
		
		public int next( )
		{
			if( notStart )
			{
				leftValue = this.left.next();
				rightValue = this.right.next();
				notStart = false;
			}

			if ( this.leftValue > this.rightValue && right != null )
			{
				int result = this.rightValue;
				try
				{
					this.rightValue = right.next( );
				}
				catch ( NoSuchElementException e )
				{
					this.right = null;
					this.rightValue = Integer.MAX_VALUE;
				}
				return result;
			}
			
			if( left != null )
			{
				int result = this.leftValue;
				try
				{
					this.leftValue = left.next( );
				}
				catch ( NoSuchElementException e )
				{
					this.left = null;
					this.leftValue = Integer.MAX_VALUE;
				}
				return result;
			}	
			
			throw new NoSuchElementException();
		}
		
		

		/* (non-Javadoc)
		 * @see it.uniroma3.mat.extendedset.intset.IntSet.IntIterator#hasNext()
		 */
		@Override
		public boolean hasNext( )
		{
			return !( (this.leftValue == this.rightValue) && (this.leftValue == Integer.MAX_VALUE) );
		}

		/* (non-Javadoc)
		 * @see it.uniroma3.mat.extendedset.intset.IntSet.IntIterator#remove()
		 */
		@Override
		public void remove( )
		{
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see it.uniroma3.mat.extendedset.intset.IntSet.IntIterator#skipAllBefore(int)
		 */
		@Override
		public void skipAllBefore( int element )
		{
			throw new UnsupportedOperationException();
		}

	}

}