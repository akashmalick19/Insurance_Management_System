
Project Name: Insurance Management Platform with Spring Boot and Java

Objective: Build an insurance management platform that allows users to manage insurance policies, clients, and claims using Spring Boot and Java.
Description of the project: 
This is Insurance Management System.
A client belongs to different roles(Super_User,User) based on role a client can update and delete his/her information. But there is a one Super User who can add a client. A client can show his/her details(Insurance,Claim)and update accordingly.
A client can subscribes an insurance.Under insurance many policies are attached. We can insert an insurance and update an insurance( premium,type, coverage amount etc.) also can show insurance details and delete an insurance.
A client can claim insurance from claim moudle. we can update delete and show the claims details.

How to Run this Project in Locally: 
1. Clone this project into local. Then goto the src/main file of the project. 
2. After that Opent it any code editor like Intellij Idea.
3. First add the dependencies-
	i. Spring Web
	ii. Spring Data JPA
	iii. Spring Security
	iv. Lombok Developer Tools dependency
	v. MySQL Diver
	vi. JSON WebToken(jjwt)
 	vii. Spring Validation I/O 
 	viii. H2 Database dependency
4. Add the Database configuration inside 'application.properties' file.
5.You can use API testing tool POSTMAN. 
6.You can use Database MySQL Workbench 8.0 version.


 Requirements for Project:
	1. Set up a new Spring Boot project using the Spring Initializr (https://start.spring.io/) and
	   select the necessary dependencies i.e. Spring Web, Spring Data JPA, Spring Security, Lombok Developer Tools, MySQL Diver, JSON WebToken(jjwt),
	   Spring Validation I/O and an embedded database like H2 Database SQL.

	2. Created the necessary domain models and their relationships:
		a. Client: Represented a client with properties such as  clientId, clientName, clienEmail, ClientPassword, date of birth, address,and contactNumber, enableClient(i.e. 'delete_record_status' field).
		   Each client should be associated with many policies i.e. One_To_Many Relation Ship.

		b. InsurancePolicy: Represented an insurance policy with properties such as policyNumber, type, coverageAmount, premium, startDate, endDate, enableInsurance(i.e.'delete_record_status' field).
		   Each policy should be associated with a client i.e. Many_To_One Relation Ship.

		c. Claim: Represented an insurance claim with properties such as claimNumber, description, claimDate, claimStatus. Each claim should be associated with an insurance policy i.e. One_To_One Relation Ship.
		
		*d. Role: Represented an Role with properties such as roleId and roleName. there have Two Role i.e. Super_User and User. Each Role should be associated with a client and vice versa i.e. Many_To_Many Relation Ship.

	3. Used Spring Data JPA to create repositories for the domain models and interact with the embedded H2 Database and MySQL Database.

	4. Implemented exception handling and validation to ensure proper API usage and data integrity for each method of controller and service classes.

	5. Used PostMan application for Unit tests of the APIs and services purpose.
	
	6. Added the JWT-based authentication to secure the APIs of the project.

	7. There have Two sub application.properties in this project.
	   	i. application-test.properties: Inside this properties H2 Database configuration is defined.  
		
		ii. application-prod.properties: Inside this properties MySQL Database configuration is defined. 

	8. Implemented RESTful APIs for the following actions:

	   a. Clients:
		i. GET/api/clients: Fetch all clients by only Super User and also pass the Jwt Token to the headers of specific client(i.e. jwt token of Super_user).

		ii. GET/api/clients/{id}: Fetch a specific client by pass any specific clientId as parameter with the API and also pass the Jwt Token to the headers of any specific client.

		iii. POST/api/clients: Create a new client by only Super User and also pass the Jwt Token to the headers of specific client(i.e. jwt token of Super_user).

		iv. PUT/api/clients/{id}: Update a client's information by pass any specific clientId as parameter with the API and also pass the Jwt Token to the headers of any specific client.

		v. DELETE/api/clients/{id}: Delete a client information by pass any specific clientId as parameter with the API and also pass the Jwt Token to the headers of any specific client.

	  b. Insurance Policies:
		i. GET/api/policies: Fetch all insurance policies by only Super User client and also pass the Jwt Token to the headers of specific client(i.e. jwt token of Super_user).
		
		ii. GET/api/policies/{id}: Fetch a specific insurance policy by any specific client to pass any specific insurance PolicyNumber as parameter with the API and also pass the Jwt Token to the headers of any specific client.
		
		iii. POST/api/policies: Create a new insurance policy by any specific client and also pass the Jwt Token to the headers of specific client.
		
		iv. PUT/api/policies/{id}: Update an insurance policy information by any specific client to pass any specific insurance PolicyNumber as parameter with the API and also pass the Jwt Token to the headers of specific client.
		
		v. DELETE/api/policies/{id}: Delete an insurance policy by any specific client to pass any specific insurance PolicyNumber as parameter with the API and also pass the Jwt Token to the headers of specific client.

	  c. Claims:
		i. GET/api/claims: Fetch all claims by only Super User client and also pass the Jwt Token to the headers of specific client(i.e. jwt token of Super_user).
		
		ii. GET/api/claims/{id}: Fetch a specific claim by any specific client to pass any specific claimNumber as parameter with the API and also pass the Jwt Token to the headers of any specific client.
		
		iii. POST/api/claims/{policyNumber}: Create a new claim by any specific client to pass any specific insurance PolicyNumber as parameter with the API and also pass the Jwt Token to the headers of specific client.
		
		iv. PUT/api/claims/{id}: Update a claim's information by any specific client to pass any specific claimNumber as parameter with the API and also pass the Jwt Token to the headers of specific client.
		
		v. DELETE/api/claims/{id}: Delete a claim information by any specific client to pass any specific insurance claimNumber as parameter with the API and also pass the Jwt Token to the headers of specific client.


