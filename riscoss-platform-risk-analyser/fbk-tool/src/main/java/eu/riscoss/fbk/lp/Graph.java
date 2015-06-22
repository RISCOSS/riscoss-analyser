///*
//   (C) Copyright 2013-2016 The RISCOSS Project Consortium
//   
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
//
//*/
//
//package eu.riscoss.fbk.lp;
//
//
//import java.util.LinkedList;
//
//public class Graph {
//	
//	LinkedList<Node> nodes;
//	
//	LinkedList<Relation> relations;
//	
//	Graph() {
//		nodes     = new LinkedList<Node>();
//		relations = new LinkedList<Relation>();
//	}
//	
//	public Iterable<Relation> relations() {
//		return relations;
//	}
//	
//	public Iterable<Node> nodes() {
//		return nodes;
//	}
//	
//	int getNodeCount() {
//		return nodes.size();
//	}
//	
//	
//	public void addNode( Node n ) {
//		nodes.add( n );
//	}
//	
//	public void addRelation( Relation r ) {
//		relations.add( r );
//	}
//	
//	public int propagate() {
//		int repetitions = 0;
//		
//		boolean graphChanged;
//		
//		do {
//			++repetitions;
//			
//			for( Node node : nodes )
//				node.syncLabels();
//			
//			graphChanged = false;
//			
//			for( Node currentNode : nodes ) {
//				
//				if (currentNode.getIncomingRelations().isEmpty()) continue;
//				
//				for( Relation currentRelation : currentNode.getIncomingRelations() ) {
//					currentRelation.propagate();
//				}
//				if (currentNode.hasChanged()) graphChanged = true;
//			}
//			
//		}
//		while (graphChanged == true);
//		return repetitions;
//	}
//}
