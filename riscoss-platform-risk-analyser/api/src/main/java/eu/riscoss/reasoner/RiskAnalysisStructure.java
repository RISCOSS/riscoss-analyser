package eu.riscoss.reasoner;

public interface RiskAnalysisStructure {
	
	/**
	 * Loads a model in the engine
	 * 
	 * @param xmlModel Is responsibility of the engine to check that the xml model model is correct
	 */
	public abstract void				loadModel(String xmlModel);
	
	/**
	 * Get a list of Chunks; currently, only a list of input Chunks and output Chunks are supported
	 * 
	 * @param slice The part of the model to retrieve (input or output chunks)
	 * @return
	 */
	public abstract Iterable<Chunk>		queryModel(ModelSlice slice);
	
	/**
	 * Reads a Field of a Chunk
	 * 
	 * @param chunk
	 * @param field
	 * @return
	 */
	public abstract Field				getField(Chunk chunk, FieldType field);
	
	/**
	 * Writes into a Field of a Chunk
	 * 
	 * @param chunk
	 * @param type describes the data type of the field value
	 * @param field carries the value of the field
	 */
	public abstract void				setField(Chunk chunk, FieldType type, Field field);
	
	/**
	 * Likely to be removed and replaced by getField(...)
	 * Retrieve a generic 'default' value for fields (typically 0)
	 * 
	 * @param chunk
	 * @return
	 */
	public abstract <T> T				getDefaultValue(Chunk chunk);
	
	/**
	 * In case of errors, this may be used to report the error to the user
	 * 
	 * @return
	 */
	public abstract String				getLastMessage();
	
}
