
# CosmosDB Apache Camel example
 
This example demonstrates how you can integrate CosmosDB into your Apache Camel
application and use it as a key value store (aka as a cache)

## Provisioning CosmosDB 

As this example requires CosmosDB you will need to provision it. Run the
following commands below to provision it.

```
  az login
  az account set --subscription "<your-azure-subscription>"
  provision.sh
```

NOTE: You can use either a subscription name or id when specifying which 
subscription to use; to obtain a list of your subscriptions, type `az account list`. 

## Deploying the web application

To deploy the web application successfully you will need to set the following 
environment variables:

```
COSMOSDB_URI           -> to the URI of the CosmosDB location.
COSMOSDB_PRIMARY_KEY   -> to the PrimaryKey to be used.
COSMOSDB_DATABASE_NAME -> to the Database Name to be used.
```

And then you can start the example using Tomcat by using Maven

```
mvn cargo:run
```

Then the example will be running on http://localhost:8080/cosmosdb-camel/

## Getting a value from the key value store

To get a value from the key value store you can use:

```
  http://localhost:8080/cosmosdb-camel/get?name=myname
```

You can replace myname with the name of a key of your choice.

## Setting a key in the key value store

To set a key in the key value store you can use:

```
  http://localhost:8080/cosmosdb-camel/set?name=myname&value=myvalue.
```

You can replace myname with the name of a key of your choice.

You can replace myvalue with the value of your choice.

## Changing the RU (throughput) for the key value store

If you need more throughput (which will cost more) you can use the one liner below and substitute the ${} values with your own.

```
az cosmosdb collection update --resource-group ${RESOURCE_GROUP_NAME}
   --name ${COSMOSDB_ACCOUNT_NAME} --db-name ${COSMOSDB_DATABASE_NAME}
   --collection-name kvs --throughput ${RU_UNITS}
```

## Changing TTL for the key value store

If you want to change the default TTL (Time-to-live) you can use the one liner below
and substitute the ${} values with your own.

```
az cosmosdb collection update --resource-group ${RESOURCE_GROUP_NAME}
  --name ${COSMOSDB_ACCOUNT_NAME} --db-name ${COSMOSDB_DATABASE_NAME}         
  --collection-name kvs --default-ttl ${TTL_SECONDS}
```
