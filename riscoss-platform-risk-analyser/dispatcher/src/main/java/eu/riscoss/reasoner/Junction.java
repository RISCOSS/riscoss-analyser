/*
   (C) Copyright 2013-2016 The RISCOSS Project Consortium
   
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package eu.riscoss.reasoner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Junction {
	
	interface Fx {
		Field transform( Field srcfield );
	}
	
	Fx nullFx = new Fx() {
		@Override
		public Field transform( Field srcfield ) {
			return srcfield;
		}
	};
	
	HashMap<DataType,HashMap<DataType,Fx>> funcs = new HashMap<DataType,HashMap<DataType,Fx>>();
	
	RiskAnalysisEngine source;
	RiskAnalysisEngine target;
	
	public Junction() {
		init();
	}
	
	public Junction( String xmlString ) {
	}
	
	void init() {
		addFunction( DataType.DISTRIBUTION, DataType.EVIDENCE, new Fx() {
			@Override
			public Field transform(Field srcfield ) {
				double plus = 0;
				double minus = 0;
				Distribution d = srcfield.getValue();
				int count = d.getValues().size() -1;
				double p = 0;
				for( Double val : d.getValues() ) {
					plus += val.doubleValue() * p;
					p += (1.0 / count);
				}
				p = 1;
				for( Double val : d.getValues() ) {
					minus += val.doubleValue() * p;
					p -= (1.0 / count);
				}
				Evidence e = new Evidence(plus, minus);
				return new Field( DataType.EVIDENCE, e );
			}} );
//		addFunction( DataType.EVIDENCE, DataType.DISTRIBUTION, new Fx() {
//			@Override
//			public Field transform(Field srcfield) {
//				return null;
//			}
//		} );
		addFunction( DataType.EVIDENCE, DataType.EVIDENCE, new Fx() {
			@Override
			public Field transform(Field srcfield ) {
				Evidence e = srcfield.getValue();
				return new Field( DataType.EVIDENCE, new Evidence( e.getPositive(), e.getNegative() ) );
			}} );
	}
	
	private void addFunction(DataType src, DataType trg, Fx fx) {
		
		HashMap<DataType,Fx> m = funcs.get( src );
		
		if( m == null ) {
			m = new HashMap<DataType,Fx>();
			funcs.put( src, m );
		}
		
		m.put( trg, fx );
	}

	Fx getFx( DataType srcType, DataType trgType ) {
		HashMap<DataType,Fx> m = funcs.get( srcType );
		if( m == null ) return nullFx;
		Fx f = m.get( trgType );
		if( f == null ) return nullFx;
		return f;
	}
	
	public Iterable<Chunk> filterInputFields() {
		
		Map<String,Chunk> out = new HashMap<String,Chunk>();
		for( Chunk c : source.queryModel( ModelSlice.OUTPUT_DATA ) ) out.put( c.getId(), c );
		
		ArrayList<Chunk> in = new ArrayList<Chunk>();
		for( Chunk c : target.queryModel( ModelSlice.INPUT_DATA ) ) {
			if( !out.containsKey( c.getId() ) )
				in.add( c );
		}
		return in;
	}
	
	public void apply() {
		for( Chunk c : source.queryModel( ModelSlice.OUTPUT_DATA ) ) {
			Field srcfield = source.getField( c, FieldType.OUTPUT_VALUE );
			Field trgfield = target.getField( c, FieldType.INPUT_VALUE );
			if( trgfield == null ) continue;
			Fx fx = getFx( srcfield.getDataType(), trgfield.getDataType() );
			if( fx == null ) continue;
			target.setField( c, FieldType.INPUT_VALUE, fx.transform( srcfield ) );
		}
	}
}
