# geolite2
Just a couple of useful classes over http://dev.maxmind.com/geoip/geoip2/geolite2/ and geonames.org but recently we added the dataset from https://github.com/mmarmol/geonames since genonames.org decided to make private the datasets. It also includes TimeZoneDB information to return current timezones and dts.
  - It has a class to automatically re-load the database from a url and store it on memory.
  - The database will be re-loaded one time by day if a different MD5 checksum found on destination, if not will keep the existen one.
  - searchs also locations based on latitude and longitude from the closer location on geonames dataset
  - It adds a Guava Cache implementation for the database so it can replace the HashMap based offered by default by geolite2.
  - gets cities details from geonames
  - gets country information from geonames
  - gets continents from geonames
  - gets timezones from TimeZoneDB
  - Success and Failure listeners for loading databases
  - Failsafe urls for databases by properties

Usage
----

To start the GeoLocation:

    GeoLocation geolocation = new GeoLocation()
    .cache(new GuavaCache())
    .readDatabase();

To start the GeoLocation with a scheduling:

    GeoLocation geolocation = new GeoLocation()
    .cache(new GuavaCache())
    .start();
    
To stop the scheduling process:

    geolocation.stop();

To start using it:

    reader.databaseReader().location("179.215.124.14");
    reader.databaseReader().location(-23.95,-46.3333);
	{
		"ip": "179.215.124.14",
		"latitude": -23.95,
		"longitude": -46.3333,
		"cityName": "Santos",
		"timeZone": {
			"utcOffset": -3.0,
			"currentOffset": -3.0,
			"name": "America/Sao_Paulo",
			"dtsOffset": -2.0,
			"changedAt": 1456020000
		},
		"subdivisions": ["Santos", "Sao Paulo"],
		"country": {
			"capital": "Brasilia",
			"currencyCode": "BRL",
			"currencyName": "Real",
			"language": "pt-BR",
			"name": "Brazil",
			"phone": "55",
			"iso": "BR"
		},
		"continent": {
			"continentIso": "SA",
			"continentName": "South America"
		}
	}
    
Maven Repo
----
For Maven

    <repository>
		<repository>
			<id>io.gromit.releases</id>
			<url>http://repository.gromit.io.s3.amazonaws.com</url>
		</repository>
    </repository>

    <dependency>
    	<groupId>io.gromit</groupId>
    	<artifactId>geolite2</artifactId>
    	<version>0.6.0</version>
    </dependency>

For Gradle

    maven {
        url "http://repository.gromit.io.s3.amazonaws.com/"
    }
    
    dependencies {
    	compile 'io.gromit:geolite2:0.6.0'
    }
    

License
----
Apache License http://www.apache.org/licenses/LICENSE-2.0
