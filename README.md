# geolite2
Just a couple of useful classes over http://dev.maxmind.com/geoip/geoip2/geolite2/ and geonames.org but recently we added the dataset from https://github.com/mmarmol/geonames since genonames.org decided to make private the datasets.
  - It has a class to automatically re-load the database from a url and store it on memory.
  - The database will be re-loaded one time by day if a different MD5 checksum found on destination, if not will keep the existen one.
  - It adds a Guava Cache implementation for the database so it can replace the HashMap based offered by default by geolite2.
  - gets cities details from geonames
  - gets timezones from geonames
  - gets country information from geonames
  - gets continents from geonames
  - Success and Failure listeners for loading databases
  - Failsafe urls for databases by properties
  - we search also locations based on latitude and longitude from the closer location on geonames dataset

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

    reader.databaseReader().location("201.250.60.126");
    reader.databaseReader().location(-34.6033,-58.3817);
	{
		"ip": "201.250.60.126",
		"latitude": -34.6033,
		"longitude": -58.3817,
		"timeZone": {
			"countryIso": "AR",
			"id": "America/Argentina/Buenos_Aires",
			"janOffset": -3.0,
			"julOffset": -3.0,
			"rawOffset": -3.0
		},
		"subdivisions": [{
			"name": "Buenos Aires F.D.",
			"geonNameId": 3433955
		}],
		"country": {
			"area": 2766890,
			"capital": "Buenos Aires",
			"currencyCode": "ARS",
			"currencyName": "Peso",
			"language": "es-AR",
			"name": "Argentina",
			"phone": "54",
			"population": 41343201,
			"iso": "AR",
			"geoNameId": 3865483
		},
		"continent": {
			"geonameId": 6255150,
			"iso": "SA",
			"name": "South America"
		},
		"city": {
			"name": "Buenos Aires",
			"geoNameId": 3435910,
			"population": 13076300
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
    	<version>0.5.2</version>
    </dependency>

For Gradle

    maven {
        url "http://repository.gromit.io.s3.amazonaws.com/"
    }
    
    dependencies {
    	compile 'io.gromit:geolite2:0.5.2'
    }
    

License
----
Apache License http://www.apache.org/licenses/LICENSE-2.0
