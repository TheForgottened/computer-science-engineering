# MetaPD - the chat app that looks like a mail client

<img src="./metapd_logo.png" width="200dp">

The objective was to develop an application divided into client, server and load balancer. For the main part of the application we couldn't use any helpful technology, only Sockets and DatagramSockets.

We planned a database for the application that suffered some iterations through the development. The physical model and the ER can be found in the db-diagrams folder. We used Docker to run the database, you can check the `docker-compose.yml` file.

For the last part, we used Java RMI to write a small observer for the load balancer and Spring Boot to write a small RESTAPI.

The report (called report.pptx) and in the summary report (found inside the summary-report folder) explain (in portuguese) the implementation. The specification of the RESTAPI can be found in the [rest-api-specification.md](./rest-api-specification.md).