# Example Code for client asking for different data

## Description

In this program we are setting up both a TCP and UDP client server
to play a game of trivia.

Youtube Link:

# TCP

## UML Diagram - Sequence Diagram

//www.plantuml.com/plantuml/png/lOynIyH048Nx-nN35NR2sHqMSf3AJZp1UfYyYyjjPkFix0N_lKbSX42mj7kPVFFjcrDsDYzzt9rXDnZTFr5DZsH5Qtxdu44xkYIZ9gb2F2JTDJ50VTwfQTcfkVvT6Sz9hd1AIaCof-FXUAWFhoGLOn4CQtfJYWXnsZ_ioHEzHFwYDmktmDHnZqq_jCtqt7E7UKL-vdV4-PgN6rdp2Dkwh_c0J52uL8P29abca5bGXfOTbAS9fa-PuKKu1bNOLljNV1Pa9yMuDFgZy2_o7rLFqBRqyHi0

<div hidden>
@startuml TCPDiagram
Server -> Server : runServer Waiting for Connection
Client -> Client : runClient 
Client -> Server : Socket on port 8080
Server -> Client : Socket accept on port 8080
Client -> Server : Select "1" to Play Trivia game
Server -> Client : Sends Image to client & asks for answer
Client -> Client : Checks if answer is correct & update score & check for winner
Client -> Server : Request new Image
Server -> Client : Sends new Image & asks for answer
Client -> Client : Checks if answer is correct & update score & check for winner
@enduml

</div>
![](TCPDiagram.svg)

## Running the example

`gradle runServer`
`gradle runClient`


### Simple protocol

Client  

```
{ 
	"selected": <int: 1=image, 4=random>
}
```
   
Server sends the data type of "data" and will reply back with an Image for user to guess
   
```
{
   "datatype": <int: 1-string, 2-byte array>, 
   "type": <"joke", "quote", "image">,
   "data": <thing to return> 
}
```
   
Server sends error if something goes wrong

```
{
	"error": <error string> 
}
```
   
   
## Issues in the code that were not included on purpose



# UDP

Client and server are very similar to the TCP example just the connection of course is UDP instead of TCP. The UDP version has the same issues as the TCP example and that is again on purpose. 

