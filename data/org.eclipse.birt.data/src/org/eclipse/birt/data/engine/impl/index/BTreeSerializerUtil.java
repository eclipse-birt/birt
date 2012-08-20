package org.eclipse.birt.data.engine.impl.index;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.birt.core.btree.BTreeSerializer;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class BTreeSerializerUtil
{
	public static BTreeSerializer createSerializer( Class dataType )
	{
		if( dataType == String.class )
		{
			return new StringSerializer( );
		}
		else if ( dataType == BigDecimal.class )
		{
			return new BigDecimalSerializer( );
		}
		else if ( dataType == Integer.class )
		{
			return new IntegerSerializer( );
		}
		else if ( dataType == Double.class )
		{
			return new DoubleSerializer( );
		}
		else if ( dataType == java.util.Date.class )
		{
			return new DateTimeSerializer( );
		}
		else if ( dataType == java.sql.Date.class )
		{
			return new DateSerializer( );
		}
		else if ( dataType == Time.class )
		{
			return new TimeSerializer( );
		}
		else if ( dataType == Timestamp.class )
		{
			return new DateTimeSerializer( );
		}
		else if ( dataType == Boolean.class )
		{
			return new BooleanSerializer( );
		}
		return new JavaSerializer( );
	}	
}

class ByteArraySerializer implements BTreeSerializer<byte[]>
{

	@Override
	public byte[] getBytes( byte[] object ) throws IOException
	{
		// TODO Auto-generated method stub
		return object;
	}

	@Override
	public byte[] getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		// TODO Auto-generated method stub
		return bytes;
	}
	
}

//class ConciseSetSerializer implements BTreeSerializer<ConciseSet>
//{
//	ClassLoader classLoader = null;
//	
//	public void setClassLoader( ClassLoader classLoader )
//	{
//		this.classLoader = classLoader;
//	}
	
//	private int[] byteArrayToIntArray( byte[] b )
//	{
//		int[] result = new int[(b.length-1)/4];
//		for( int i = 0; i < result.length; i++ )
//		{
//			result[i] = byteArrayToInt( b, i*4+1 );
//		}
//		return result;
//	}
//	
//	private int byteArrayToInt(byte[] b, int offset) {
//	    int value = 0;
//	    for (int i = 0; i < 4; i++) {
//	        int shift = (4 - 1 - i) * 8;
//	        value += (b[i + offset] & 0x000000FF) << shift;
//	    }
//	    return value;
//	}
//	
//	private byte[] intArrayToByteArray( int[] array )
//	{
//		ByteBuffer byteBuffer = ByteBuffer.allocate( array.length * 4 );
//		IntBuffer intBuffer = byteBuffer.asIntBuffer( );
//		intBuffer.put( array );
//		return byteBuffer.array( );
//	}
//	
//	private void concatByteArrays( byte[] source, List<byte[]> addedBytes )
//	{
//		int pos = 0;
//		for( int i = 0 ; i < addedBytes.size( ) ; i++ )
//		{
//			System.arraycopy( addedBytes.get( i ), 0, source, pos, addedBytes.get( i ).length );
//			pos += addedBytes.get( i ).length;
//		}
//	}
//	
//	private byte[] constructDeltaCompressionBytes( ConciseSet set )
//	{
//		int lastValue = 0;
//		List<byte[]> result = new ArrayList<byte[]>();
//		int resultLength = 1;
//		result.add( new byte[1] );
//		for( int i =0;i<set.size( );i++)
//		{
//			byte[] valueBytes = BTreeUtil.getIncrementBytes( set.get( i ), lastValue );
//			result.add( valueBytes );
//			resultLength += valueBytes.length;
//			lastValue = set.get( i );
//		}
//		byte[] resultBytes = new byte[resultLength];
//		concatByteArrays(resultBytes,result);
//		return resultBytes;
//	}
//	@Override
//	public byte[] getBytes( ConciseSet object ) throws IOException
//	{
//		
//		int[] array = object.getWords( );
//	    byte[] conciseBytes = intArrayToByteArray( array );
//	    
//	    double compressionRatio = object.collectionCompressionRatio( );
//	   
//	    if( compressionRatio > 0.25 )
//	    {
//	    	byte[] deltaBytes = constructDeltaCompressionBytes( object );
//	    	double deltaCompressionRatio = ((double)(deltaBytes.length-1))/(object.size( )*4);
//	    	// if the leading byte is 0, then the delta compression bytes are saved.
//	    	if(deltaCompressionRatio < compressionRatio )
//	    	{
//	    		return deltaBytes;
//	    	}
//	    }
//	    	
//	    byte[] resultBytes = new byte[conciseBytes.length+1];
//	    //the leading byte is 1, then the conciseSet compression is applied.
//	    resultBytes[0] = (byte)0x01;
//	    System.arraycopy( conciseBytes, 0, resultBytes, 1, conciseBytes.length );
//        return resultBytes;
//	}
//	
//	public static int computeInt ( byte[] b,int size)
//	{
//		int LeftMoveCount = 9;
//		int rightBits = 0;
//
//		int result = 0;
//		for ( int i = size; i >= 0; i-- )
//		{
//			byte newByte = b[i];
//			switch ( size - i )
//			{
//				case 0 :
//					rightBits = 0x00;
//					break;
//				case 1 :
//					rightBits = 0x01;
//					LeftMoveCount = 7;
//					break;
//				case 2 :
//					rightBits = 0x03;
//					LeftMoveCount = 6;
//					break;
//				case 3 :
//					rightBits = 0x07;
//					LeftMoveCount = 5;
//					break;
//			}
//			newByte &= rightBits;
//			if ( size == i )
//				result += b[i];
//			else
//				result = result
//						+ ( ( ( newByte << LeftMoveCount ) ) << ( size
//								- i - 1 ) * 8 )
//						+ ( ( ( b[i] >> ( 8 - LeftMoveCount ) ) ) << ( size - i ) * 8 );
//		}
//
//		return result;
//	}
//	
//
//	@Override
//	public ConciseSet getObject( byte[] bytes ) throws IOException,
//			ClassNotFoundException
//	{
//		ConciseSet set = null;
//		if ( bytes[0] == 0x00 )
//		{
//
//			set = new ConciseSet( );
//			int pos = 1;
//			int currentValue = 0;
//			int resultInt = 0;
//			while ( pos < bytes.length )
//			{
//				byte b = bytes[pos];
//				pos++;
//
//				if ( b < 0 )
//				{
//					byte[] compressedBytes = new byte[4];
//					int size = 0;
//					while ( b < 0 )
//					{
//						b &= 0x7F;
//						compressedBytes[size] = b;
//						b = bytes[pos];
//						pos++;
//						size++;
//					}
//					compressedBytes[size] = b;
//
//					resultInt = computeInt( compressedBytes, size )
//							+ currentValue;
//				}
//				else
//				{
//					byte[] intBytes = new byte[4];
//					intBytes[3] = b;
//					resultInt = BTreeUtil.bytesToInteger( intBytes )
//							+ currentValue;
//				}
//
//				set.add( resultInt );
//				currentValue = resultInt;
//			}
//		}
//		else
//		{
//			set = new ConciseSet( byteArrayToIntArray(bytes));
//		}
//
//		return set;
//	}
//	
//}

class JavaSerializer implements BTreeSerializer<Object>
{
	ClassLoader classLoader = null;
	
	public void setClassLoader( ClassLoader classLoader )
	{
		this.classLoader = classLoader;
	}
	
	public byte[] getBytes( Object object ) throws IOException
	{
		if( ! ( object instanceof Serializable ) )
		{
			throw new NotSerializableException(
					CoreMessages.getString( ResourceConstants.NOT_SERIALIZABLE ) );
		}
		ByteArrayOutputStream buff = new ByteArrayOutputStream( );
		ObjectOutputStream oo = new ObjectOutputStream( buff );
		oo.writeObject( object );
		oo.close( );
		return buff.toByteArray( );
	}

	public Object getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		final ClassLoader loader = classLoader;
		ObjectInputStream oo = new ObjectInputStream(
					new ByteArrayInputStream( bytes ) ) {

				protected Class resolveClass( ObjectStreamClass desc )
						throws IOException, ClassNotFoundException
				{
					return Class.forName( desc.getName( ), false,
							loader );
				}
			};
		return oo.readObject( );
	}
}

class BooleanSerializer implements BTreeSerializer<Boolean>
{

	public byte[] getBytes( Boolean object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeBoolean( object );
		return out.toByteArray( );
	}

	public Boolean getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return input.readBoolean( );
	}
}

class IntegerSerializer implements BTreeSerializer<Integer>
{

	public byte[] getBytes( Integer object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeInt( object );
		return out.toByteArray( );
	}

	public Integer getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return input.readInt( );
	}
}

class DoubleSerializer implements BTreeSerializer<Double>
{

	public byte[] getBytes( Double object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeDouble( object );
		return out.toByteArray( );
	}

	public Double getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return input.readDouble( );
	}
}

class StringSerializer implements BTreeSerializer<String>
{

	public byte[] getBytes( String object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeUTF( object );
		return out.toByteArray( );
	}

	public String getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return input.readUTF( );
	}
}

class DateTimeSerializer<K> implements BTreeSerializer<java.util.Date>
{

	public byte[] getBytes( java.util.Date object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeLong( object.getTime( ) );
		return out.toByteArray( );
	}

	public java.util.Date getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new Date( input.readLong( ) );
	}
}

class TimeStampSerializer<K> implements BTreeSerializer<Timestamp>
{

	public byte[] getBytes( Timestamp object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeLong( object.getTime( ) );
		return out.toByteArray( );
	}

	public Timestamp getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new Timestamp( input.readLong( ) );
	}
}

class DateSerializer<K> implements BTreeSerializer<java.sql.Date>
{

	public byte[] getBytes( java.sql.Date object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeLong( object.getTime( ) );
		return out.toByteArray( );
	}

	public java.sql.Date getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new java.sql.Date( input.readLong( ) );
	}
}

class TimeSerializer<K> implements BTreeSerializer<java.sql.Time>
{

	public byte[] getBytes( java.sql.Time object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeLong( object.getTime( ) );
		return out.toByteArray( );
	}

	public java.sql.Time getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new java.sql.Time( input.readLong( ) );
	}
}

class BigDecimalSerializer<K> implements BTreeSerializer<BigDecimal>
{

	public byte[] getBytes( BigDecimal object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		DataOutput oo = new DataOutputStream( out );
		oo.writeUTF( object.toString( ) );
		return out.toByteArray( );
	}

	public BigDecimal getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		DataInput input = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		return new BigDecimal( input.readUTF( ) );
	}
}