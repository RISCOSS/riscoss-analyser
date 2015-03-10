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

public enum AnalysisResponse {
	DONE,			// The analysis was one-shot and has completed successfully
	FAILED,			// The analysis failed, the error message has to be retrieved
	PENDING,		// The analysis is taking time and needs to be checked again later for completion
	PARTIAL,		// The is part of a sequence; results are available for one step of the sequence but more steps have to be performed
	INPUT_REQUIRED	// More input needs to be asked to the user
}
