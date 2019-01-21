Hi, GE!

Please take a minute to read this note. Thx!
It was very interesting for me to solve this problem.

Use following command to launch test app:

_java -jar geTest.jar_

Jar-file can be found in _out/artifacts_ dir

or you can just open the project in your favourite IDE and launch it.

There is some javadoc inside the code. The code is not difficult, so I decide to provide only high-level
comments and not flood the code with comments. The code must be readable.

**Note 1.**
On app start SystemA launch two generator (producer) threads and four preprocessor thread (moving 
messages to client priority sorted queues), also four instances of SystemB starts with <availableCores> 
to process messages. So every subscriber will get his own message queue no matter how fast it processing
queue.

On the other hand, it is possible to keep offset/position for each client, like Kafka does and save some
memory. I was thinking about locks when multiple threads will try to get message. So I decided to try 
use PUT-TAKE approach of BlockingQueues and create queue for every client (subscriber).

**Note 2.**
Each message producer puts message to unsorted queue, then preprocessor moves message to client-queue
which is sorted. Event consumer get message from sorted queue by clientId. Each thread can correctly 
shutdown on receiving interrupt.

**Note 3.**
There were no requirements for message generation except random. EventProducer generates [a-z]
strings with random length.

**DB tasks:**

1. DELETE FROM cities WHERE id NOT IN (SELECT MIN(id) FROM cities GROUP BY name);
2. SELECT country, city, ratio FROM (
   	SELECT 
   	country, 
   	city,
   	ROUND(citizen / SUM(citizen) OVER (PARTITION BY country),2) as ratio,
   	rank() OVER (PARTITION BY country ORDER BY citizen DESC) as r
   	FROM population
   ) as sorted 
   WHERE r <= 3
   ORDER BY country;