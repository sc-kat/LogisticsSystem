# LogisticsSystem

Prerequisites:

Create a REST microservice that simulates the operation of a package delivery management system to various destinations.
Upon starting the application, the DESTINATIONS and ORDERS tables will be automatically created, and they will be populated with data from the destinations.csv and orders.csv files. (H2 database)
An executor with a maximum of 4 active threads and a queue size of 100 tasks will be used to manage deliveries.

Supported endpoints:

POST /shipping/new-day
BODY: empty body
Advances the current date of the application by one day. Upon starting the application, the current date will be December 14th, 2021. (log + console: "New day starting: December 15th, 2021")
At the beginning of each day, all deliveries for that day will be grouped by destination and marked as "In delivery". (log + console: "Today we will be delivering to Ploiesti, Pitesti, Craiova")
For each distinct destination, a task will be submitted to the executor that will make the deliveries to that destination. (log + console: "Starting 2 deliveries for Ploiesti on Thread 0 for 25 km"). (hint: use @Async)
After a number of seconds equal to the number of kilometers to the destination, the delivery thread will mark the deliveries as completed and update the company's profit. (1 lei/km for each delivered order) (log + console: "4 deliveries completed for Ploiesti")

POST /orders/add
BODY: list of orders to be added
Adds a list of new deliveries to the database. The delivery date for the new deliveries must be strictly greater than the current date of the application (e.g., at least December 16th, 2021).

POST /orders/cancel
BODY: list of delivery IDs to be cancelled
Marks a list of deliveries as cancelled. A delivery can be cancelled at any time, even when it is in delivery. A cancelled delivery will not bring any profit. A completed delivery cannot be cancelled.

GET /orders/status?date=15-12-2021&destination=Ploiesti
Returns a list of deliveries for the day and destination provided as parameters. If the date parameter is not provided, it will return information about deliveries for the current day of the application. If the destination parameter is not provided, it will return information about deliveries to all destinations.

GET /actuator/info
Using Spring Boot Actuator, provide information about the current date and the company's profit.
{
current-date: 15-12-2021,
overall-profit: 258
}

POST /destinations/add
Add a new destination to the database.

PUT /destinations/update
Update an existing destination in the database.

GET /destinations
Retrieve a list of all destinations from the database.

GET /destinations/{destinationId}
Retrieve information about a specific destination identified by its ID.

DELETE /destinations/{destinationId}
Delete a specific destination identified by its ID from the database.

ADDITIONAL INFO:

All logs will contain second-level timestamps.
Order status enum: NEW, DELIVERING, DELIVERED, CANCELLED 

destinations.csv
Ploiesti,10
Pitesti,20
Cluj,30
Oradea,35
Satu Mare,40
Giurgiu,12
Craiova,18
Iasi,27
Bacau,16
Constanta,23

orders.csv
Ploiesti,15-12-2021
Ploiesti,15-12-2021
Pitesti,15-12-2021
Pitesti,15-12-2021
Pitesti,15-12-2021
Cluj,15-12-2021
Oradea,15-12-2021
Oradea,15-12-2021
Satu Mare,15-12-2021
Satu Mare,15-12-2021
Satu Mare,15-12-2021
Giurgiu,15-12-2021
Craiova,15-12-2021
Iasi,15-12-2021
Iasi,15-12-2021
Bacau,15-12-2021
Bacau,15-12-2021
Bacau,15-12-2021
Bacau,15-12-2021
Constanta,15-12-2021
Constanta,15-12-2021
Ploiesti,16-12-2021
Ploiesti,16-12-2021
Pitesti,16-12-2021
Pitesti,16-12-2021
Cluj,16-12-2021
Oradea,16-12-2021
Oradea,16-12-2021
Satu Mare,16-12-2021
Satu Mare,16-12-2021
Giurgiu,16-12-2021
Iasi,16-12-2021
Bacau,16-12-2021
Bacau,16-12-2021
Constanta,16-12-2021
Constanta,16-12-2021
Constanta,16-12-2021
