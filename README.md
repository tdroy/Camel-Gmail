# Camel-Gmail
Apache camel read from gmail and route based on email domain.
The main class is CamelStarter. 
The camel route is define in IntegrationRoute class.

			from("imaps://imap.gmail.com?username=acs.wls.icici@gmail.com&password=*******"
					+ "&delete=false&unseen=true&consumer.delay=2000&debugMode=false").routeId("TroyEmailPoller")
					.choice()
					.when(header("from").contains("@gmail.com")).to("file:c:/delete/inbox/Gmail/")
					.when(header("from").contains("@oracle.com")).process(new OracleUserProcessor()).to("smtps://smtp.gmail.com:465?username=acs.wls.icici@gmail.com&password=****")
					.otherwise().to("file:c:/delete/inbox/else/")
					.end();
          
 From route is to fetch from gmail account using imaps (for reading).
 routeID is to identify the route context, if multiple route defined.
 choice() is for condition based routing.
 when email header 'from' conatians for '@gmail.com' then route to file
 or when header from '@oracle.com' then send email using smtp 
 Otherwise dump email body to folder.
