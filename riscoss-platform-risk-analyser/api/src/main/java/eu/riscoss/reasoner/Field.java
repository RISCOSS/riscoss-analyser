package eu.riscoss.reasoner;

/**
 * A field of a Chunk; it is responsibility of the engine to ensure that
 * the DataType matches the class type of the value
 * 
 * @author albertosiena
 *
 */
public class Field {
	DataType	datatype;
	Object		value;
	
	public <T> Field( DataType dt, T value ) {
		this.datatype = dt;
		setValue( value );
	}
	
	public <T> T getValue() {
		return getValue( null );
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue( T def ) {
		try {
			return (T)value;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return def;
		}
	}
	
	public DataType getDataType() {
		return datatype;
	}
	
	public <T> void setValue( T value ) {
		this.value = value;
	}
}
