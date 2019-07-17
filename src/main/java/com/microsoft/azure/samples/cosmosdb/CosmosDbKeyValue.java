package com.microsoft.azure.samples.cosmosdb;

/**
 * The id (key), value object.
 * 
 * @author Manfred Riem (manfred.riem@microsoft.com)
 */
public class CosmosDbKeyValue {
    
    /**
     * Stores the id.
     */
    private String id;
    
    /**
     * Stores the value.
     */
    private String value;
    
    /**
     * Constructor.
     */
    public CosmosDbKeyValue() {
    }

    /**
     * Get the id.
     * 
     * @return the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the value.
     * 
     * @return the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the id.
     * 
     * @param id the id.
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Set the value.
     * 
     * @param value the value.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
