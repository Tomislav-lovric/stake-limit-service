# stake-limit-service Project

Using Java (Spring Boot framework) and PostgreSQL database, implemented REST APIs which enable user to create devices and use them to send stakes to our service.

## About application
You can create device/devices, then you can add stake limits to the said devices (all fields are validated here) as well change said stake limits (also validated) and finally send stakes through ticket message (also validated and you can choose to either send tickets id (UUID) or let service create it for you (project required only the former but I created the latter for easier testing). There are also some additional CRUD endpoints like get device, get stake limit etc. Some code is commented but I created commentedCode branch to contain all comments to not clutter main brunch with unnecessary comments.

## commentedCode branch
I added this branch mainly so the code doesn't look messy and unreadable because i made comments for most lines of code

## Prerequisites

JAVA

IDE (IntelliJ or eclipse)

POSTGRESQL

POSTMAN

DOCKER (optional)

# How to use app

## Set up database
Before starting you will need to create postgres db named stake_limit_service (or you can name it differently, in that case though you will also need to change the name of the db in application.yml)

## Running the application
Open application folder using some java IDE (like intellij or eclipse) and run it through said IDE or you can use docker

## Testing Endpoints
There is postman collection file in main application folder which you can import and use to test different endpoints. Since most endpoints already contain id's you will need to change those when you create devices yourself

## TODO Unit tests

## Authors

* **Tomislav Lovrić** - [stake-limit-service](https://github.com/Tomislav-lovric/stake-limit-service)
