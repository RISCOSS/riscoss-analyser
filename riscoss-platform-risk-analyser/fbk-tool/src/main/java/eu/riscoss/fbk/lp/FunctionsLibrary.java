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

package eu.riscoss.fbk.lp;

import java.util.List;

import eu.riscoss.reasoner.Evidence;

public class FunctionsLibrary {
	
	private static FunctionsLibrary instance = new FunctionsLibrary();
	
	public static FunctionsLibrary get() {
		return instance;
	}
	
	public static class Transform {
		
		public List<Evidence> asynt( List<Evidence> list ) {
			for( Evidence e : list ) {
				e.set( 
						FunctionsLibrary.get().asynt( e.getPositive() ), 
						FunctionsLibrary.get().asynt( e.getNegative() ) );
			}
			return list;
		}
		
		public List<Evidence> asynt( List<Evidence> list, double threshold ) {
			for( Evidence e : list ) {
				e.set( 
						FunctionsLibrary.get().asynt( e.getPositive(), threshold ), 
						FunctionsLibrary.get().asynt( e.getNegative(), threshold ) );
			}
			return list;
		}
		
		public List<Evidence> tanh( List<Evidence> list ) {
			for( Evidence e : list ) {
				e.set( 
						FunctionsLibrary.get().tanh( e.getPositive() ), 
						FunctionsLibrary.get().tanh( e.getNegative() ) );
			}
			return list;
		}
		
		public List<Evidence> tanh( List<Evidence> list, double rate ) {
			for( Evidence e : list ) {
				e.set( 
						FunctionsLibrary.get().tanh( e.getPositive(), rate ), 
						FunctionsLibrary.get().tanh( e.getNegative(), rate ) );
			}
			return list;
		}
		
		public List<Evidence> tanh( List<Evidence> list, double t, double s ) {
			for( Evidence e : list ) {
				e.set( 
						FunctionsLibrary.get().tanh( e.getPositive(), t, s ), 
						FunctionsLibrary.get().tanh( e.getNegative(), t, s ) );
			}
			return list;
		}
		
		public List<Evidence> range( List<Evidence> list, double min, double max ) {
			for( Evidence e : list ) {
				e.set(
						(2 - e.getPositive()) /4,
						1- ((2 - e.getPositive()) /4)
//						(e.getPositive() - 2) /4
						);
			}
			return list;
		}
		
		public List<Evidence> nonempty( List<Evidence> list ) {
			for( Evidence e : list ) {
				if( e.nonZero() == false )
					return zero( list );
			}
			return list;
		}
		
		private List<Evidence> zero( List<Evidence> list ) {
			for( Evidence e : list ) {
				e.set( 0, 0 );
			}
			return list;
		}
	}
	
	public static class Join {
		
		public Evidence or( List<Evidence> list ) {
			double p = 0, m = 0;
			for( Evidence e : list ) {
				p = sum( p, e.getPositive() );
				m = sum( m, e.getNegative() );
//				m = product( m, e.getNegative() );
			}
			return new Evidence( p, m );
		}
		
		public Evidence and( List<Evidence> list ) {
			double p = 1, m = 1;
			for( Evidence e : list ) {
				p = product( p, e.getPositive() );
				m = product( m, e.getNegative() );
			}
			return new Evidence( p, m );
		}
		
		private double sum( double p1, double p2 ) {
			return Math.abs( p1 + p2 - p1 * p2 );
		}
		
		private double product( double p1, double p2 ) {
			return p1 * p2;
		}
		
		public Evidence avg( List<Evidence> list ) {
			double p = 0, m = 0;
			for( Evidence e : list ) {
				p += e.getPositive();
				m += e.getNegative();
			}
			return new Evidence( p / list.size(), m / list.size() );
		}
		
		public Evidence max( List<Evidence> list ) {
			double p = 0, m = 0;
			for( Evidence e : list ) {
				if( e.getPositive() > p )
					p = e.getPositive();
				if( e.getNegative() > m ) 
					m = e.getNegative();
			}
			return new Evidence( p, m );
		}
		
		public Evidence min( List<Evidence> list ) {
			double p = 1, m = 1;
			for( Evidence e : list ) {
				if( e.getPositive() < p )
					p = e.getPositive();
				if( e.getNegative() < m ) 
					m = e.getNegative();
			}
			return new Evidence( p, m );
		}
		
	}
	
	public static class EvidencePropagator {
		
		public Evidence swap( Evidence e ) {
			return new Evidence( e.getNegative(), e.getPositive() );
		}
		
		public Evidence inverse( Evidence e ) {
			return new Evidence( 1 - e.getPositive(), 1 - e.getNegative() );
		}
		
		public Evidence pos( Evidence e ) {
			return new Evidence( e.getPositive(), 0 );
		}
		
		public Evidence neg( Evidence e ) {
			return new Evidence( 0, e.getNegative() );
		}
	}
	
	public final Transform			transform = new Transform();
	public final Join				join = new Join();
	public final EvidencePropagator	evidence = new EvidencePropagator();
	
	public Evidence e( double p, double m ) {
		return new Evidence( p, m );
	}
	
	public double beyond_threshold( double val, double t ) {
		if( val < t ) return 0;
		return val;
	}
	
	public double below_threshold( double val, double t ) {
		if( val < t ) return 0;
		return val;
	}
	
	public double tanh( double val ) {
		return Math.tanh( val );
	}
	
	public double tanh( int val ) {
		return tanh( (double)val );
	}
	
	public double tanh( double val, double rate ) {
		return (1 + Math.tanh( (val/2) -(rate/2) )) /2;
	}
	
	public double tanh( double val, double threshold, double smoothness ) {
		return (1 + Math.tanh( (val/smoothness) -(threshold/smoothness) )) /2;
	}
	
	public double tanh2( double val, double threshold ) {
		return (Math.tanh( (val/(threshold)) ));
	}
	
	public double tanh( int val, double rate ) {
		return tanh( (double)val, rate );
	}
	
	public double asynt( double val ) {
		return 1 - (1/(1+val));
	}
	
	public double asynt2( double val, double threshold ) {
		return 1 - (1/(1+val));
	}
	
	public double asynt( double val, double threshold ) {
		return 1 - (1/(1+(val/(threshold))));
	}
	
	public double asynt( int val ) {
		return asynt( (double)val );
	}
	
}
