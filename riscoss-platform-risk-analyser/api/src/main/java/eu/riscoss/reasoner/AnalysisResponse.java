package eu.riscoss.reasoner;

public enum AnalysisResponse {
	DONE,			// The analysis was one-shot and has completed successfully
	FAILED,			// The analysis failed, the error message has to be retrieved
	PENDING,		// The analysis is taking time and needs to be checked again later for completion
	PARTIAL,		// The is part of a sequence; results are available for one step of the sequence but more steps have to be performed
	INPUT_REQUIRED	// More input needs to be asked to the user
}
