# geolite2
Just a couple of useful classes over http://dev.maxmind.com/geoip/geoip2/geolite2/
  - It has a class to automatically re-load the database from a url and store it on memory.
  - The database will be re-loaded one time by day if a different MD5 checksum found on destination, if not will keep the existen one.
  - It adds a Guava Cache implementation for the database so it can replace the HashMap based offered by default by geolite2.

Usage
----
To start the ScheduledDatabaseReader:

    ScheduledDatabaseReader reader = new ScheduledDatabaseReader()
    .cache(new GuavaCache())
    .start();

To start using it:

    reader.databaseReader().city(....);

To stop the scheduling process:

    reader.stop();

License
----
Apache License http://www.apache.org/licenses/LICENSE-2.0
