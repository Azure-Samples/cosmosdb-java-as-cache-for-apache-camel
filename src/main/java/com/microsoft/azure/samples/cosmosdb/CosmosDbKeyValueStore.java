package com.microsoft.azure.samples.cosmosdb;

import com.azure.data.cosmos.ConnectionPolicy;
import com.azure.data.cosmos.ConsistencyLevel;
import com.azure.data.cosmos.FeedOptions;
import com.azure.data.cosmos.FeedResponse;
import com.azure.data.cosmos.PartitionKey;
import com.azure.data.cosmos.PartitionKeyDefinition;
import com.azure.data.cosmos.internal.AsyncDocumentClient;
import com.azure.data.cosmos.internal.Database;
import com.azure.data.cosmos.internal.Document;
import com.azure.data.cosmos.internal.DocumentCollection;
import com.azure.data.cosmos.internal.RequestOptions;
import com.azure.data.cosmos.internal.ResourceResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * A bean that uses a simple wrapper around the Cosmos Java SDK so we can
 * demonstrate a minimal example on how you can use it in Apache Camel.
 *
 * @author Manfred Riem (manfred.riem@microsoft.com)
 */
public class CosmosDbKeyValueStore {

    /**
     * Stores the database name.
     */
    private String databaseName;

    /**
     * Stores the client.
     */
    private AsyncDocumentClient client;

    /**
     * Constructor.
     *
     * @param uri the URI.
     * @param primaryKey the primary key.
     * @param databaseName the database name.
     */
    public CosmosDbKeyValueStore(String uri, String primaryKey, String databaseName) {
        this.databaseName = databaseName;

        AsyncDocumentClient.Builder builder = new AsyncDocumentClient.Builder();
        client = builder.
                withServiceEndpoint(uri).
                withMasterKeyOrResourceToken(primaryKey).
                withConnectionPolicy(new ConnectionPolicy()).
                withConsistencyLevel(ConsistencyLevel.SESSION).build();

        FeedResponse<Database> dbResponse = client.queryDatabases(
                "SELECT * FROM r where r.id = '" + databaseName + "'", new FeedOptions()).
                blockFirst();

        if (dbResponse != null && dbResponse.results().isEmpty()) {
            Database database = new Database();
            database.id(databaseName);
            client.createDatabase(database, null).blockFirst();
        }

        FeedResponse<DocumentCollection> collectionResponse = client.queryCollections(
                "/dbs/" + databaseName, "SELECT * FROM r where r.id = 'kvs'",
                new FeedOptions()).blockFirst();

        if (collectionResponse != null && collectionResponse.results().isEmpty()) {
            DocumentCollection collectionInfo = new DocumentCollection();
            PartitionKeyDefinition partitionKey = new PartitionKeyDefinition();
            List<String> paths = new ArrayList<>();
            paths.add("/id");
            partitionKey.paths(paths);
            collectionInfo.id("kvs");
            collectionInfo.setPartitionKey(partitionKey);
            client.createCollection("/dbs/" + databaseName, collectionInfo, null).
                    blockFirst();
        }
    }

    /**
     * Get the value.
     *
     * @param key the key.
     * @return the attribute, or null if not found.
     */
    public String get(String key) {
        String result = null;
        try {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.setPartitionKey(new PartitionKey(key));
            ResourceResponse<Document> response = client.readDocument(
                    "/dbs/" + databaseName + "/colls/kvs/docs/" + key, requestOptions).
                    blockFirst();
            if (response != null) {
                Document document = response.getResource();
                CosmosDbKeyValue kv = document.toObject(CosmosDbKeyValue.class);
                result = kv.getValue();
            }
        } catch (Exception exception) {
        }
        return result;
    }

    /**
     * Remove the key/value pair.
     *
     * @param key the key.
     */
    public void remove(String key) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setPartitionKey(new PartitionKey(key));
        client.deleteDocument("/dbs/" + databaseName + "/colls/kvs/docs/" + key,
                requestOptions).blockFirst();
    }

    /**
     * Set the key/value pair.
     *
     * @param key the key.
     * @param value the value.
     */
    public void set(String key, String value) {
        if (value != null && !value.trim().equals("null")) {
            CosmosDbKeyValue kv = new CosmosDbKeyValue();
            kv.setId(key);
            kv.setValue(value);
            client.upsertDocument("/dbs/" + databaseName + "/colls/kvs", kv,
                    null, true).blockFirst();
        } else {
            remove(key);
        }
    }
}
