Energy Management System

Assignment 1

Pentru implementarea primei teme am folosit arhitectura de mai jos. Din frontend se pot face operatii atat asupra
userilor cat si asupra deviceurilor. Fiecare dintre ele este gestionata de catre un microserviciu separate, aproape
independent. Astfel aplicatia este scalabila. Consistenta celor 2 baze de date este asigurata de call-uri API pe care
microserviciul de useri il face catre microserviciul de device-uri

![A diagram of a software process

Description automatically generated](Aspose.Words.4be467d9-b8f0-43ae-b2fc-ab9d5753aae5.001.png)

Proiectul a fost deploy pe docker conform diagramei de deployment de mai jos. Fiecare din cele 4 containere (
backend\_user, backend\_device, frontend si posgres) functioneaza independent asemanator conditiilor de productie.
Reteaua la care se conecteaza este aceasi si a fost create prin docker-compose

![A screenshot of a computer

Description automatically generated](Aspose.Words.4be467d9-b8f0-43ae-b2fc-ab9d5753aae5.002.png)
