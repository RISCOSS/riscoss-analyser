package eu.riscoss.reasoner;

/**
 * Describes the nature of a given Chunk
 * 
 * @author albertosiena
 *
 */
public enum FieldType {
	INPUT_VALUE,		// The field is an input value
	OUTPUT_VALUE,		// The field is an output value
	WEIGHT,				// The field contains an arc's weight value (FBK implementation)
	DESCRIPTION,		// The field contains the description of the chunk
	QUESTION,			// The field contains the question to be asked to the user for input
	TYPE,				// Goal, Risk or Indicator
	MIN,				// Minimum value in a range, if needed
	MAX					// Maximumvalue in a range, if needed
}
