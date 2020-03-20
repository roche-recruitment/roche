
###How to:
######  build the application
Navigate to the project's root directory and type: 
```./gradlew build``` 

###### test the application
Navigate to the project's root directory and type: 
```./gradlew test```. The application will internally start the in-memory database and perform tests. 

###### run the application
In console/terminal navigate to project root directory and execute the following command: ```./gradlew bootRun```
The application will start up and starts to listen on port `8080`


###API: 
###### Creating product
Product creation can be done by executing `/products` endpoint with `POST` HTTP request:

```
curl -H "Content-Type: application/json" -X POST -d '{"sku":"some-sku", "name":"some-name", "price": 200}' localhost:8080/products
```
**If the product with given `sku` was softly deleted, the execution of this method will override previously stored product.**
If the product with given `sku` already exists in the database, the request will return error code `400`.

###### Updating the product
Product can be updated by executing `/products` endpoint with `PUT` HTTP request'
```
curl -H "Content-Type: application/json" -X PUT -d '{"sku":"some-sku", "name":"some-new-name", "price": 200}' localhost:8080/products
```
If the product with given `sku` does not exist in the database, the request will return error code `404`.

###### Getting the product
Product can be fetched by executing `/products/{sku}` endpoint with `GET` HTTP request. The parameter `{sku}` is required.
```
curl -H "Content-Type: application/json" -X GET localhost:8080/products/some-sku
```
If the product with given `sku` does not exist in the database, the request will return error code `404`.

###### Deleting the product
Product can be softly deleted by executing `/products/{sku}` endpoint with `DELETE` HTTP request. The parameter `{sku}` is required.
```
curl -H "Content-Type: application/json" -X DELETE localhost:8080/products/some-sku
```
If the product with given `sku` does not exist in the database, the request will return error code `404`.

###### Getting list of products
List of products can be fetched by executing `/products/{sku}` endpoint with `GET` HTTP request. 
```
curl -H "Content-Type: application/json" -X GET localhost:8080/products/ 
```