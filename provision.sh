PREFIX=sample-cosmosdb-camel
LOCATION="West US 2"

az group create --name $PREFIX-rg --location $LOCATION

az network vnet create --resource-group $PREFIX-rg --name $PREFIX-vnet --location $LOCATION

az cosmosdb create --resource-group $PREFIX-rg --name $PREFIX-cosmosdb

az cosmosdb show --resource-group $PREFIX-rg --name $PREFIX-cosmosdb

az cosmosdb keys list --resource-group $PREFIX-rg --name $PREFIX-cosmosdb
