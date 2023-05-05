# InMemoryDbProviders

Dies ist ein Beispiel für die Benutzung/Erstellung von ContentProvidern speziell zur Kommunikation zwischen apps.

Das repository enthält 3 Projekte und eine library.

**InMemoryDbProvider** erzeugt einen ContentProvider in einer SQlight-Datenbank, die aber nur im RAM gehalten wird.
Der wird benutzt vom **BasicTest**.

Diese beiden Projekte dienen nur zum Testen der bidirektionalen Kommunikation.
Der InMemoryDbProvider muss installiert sein, damit BasicTest funktioniert (sonst Absturz).


**BhfLiveUtil** wird für BahnhofLive benutzt, um Funktionen in der app von aussen zu steuern.
(ab BahnhofLive 3.22.0)
BhfLiveUtil benutzt einen ContentProvider, der in BhfLive (Host-app) integriert ist.
D.h. das handling von nicht-existierendem bzw. wechselnder Host-app ist realisiert.

**utilities** ist eine library, die diverse Hilfsklassen und Funktionen enthält.

Eine Idee war, die Masse des Codes in eine Library zu packen, die man dann für eigene Projekte möglichst einfach nutzen kann.
Das klappt noch nicht so recht, weil der Providername (=android:authorities), der einen Provider eindeutig identifiziert "hardwired" ist und ich noch keinen Weg gefunden habe, den zu dynamisieren.

D.h. die Dateien im contentprovider-Ordner in den utilities können teilweise nur als Kopiervorlage dienen.


### Grundsätzliches zur Arbeitsweise von ContentProvidern

Am meisten muss man beim Verwenden vom Providernamen und dem package-Pfad der Quelltextdatei, die den Provider enthält, aufpassen.<br>
Grundsätzlich:<br>
authorities = Eindeutiger, systemweiter Name des Providers (hier: de.codeelements.inmemdb.provider)<br>
name = Quelltextdateiname des Providers (hier: de.codeelements.inmemdb.provider)<br>



## 1. app, die den Provider enthält


### 1.1 Manifest.xml

&lt;permission
   android:name="**de.codeelements.inmemorydbprovider.InMemoryDbProvider**.PERMISSION" />
		
dabei entspricht **de.codeelements.inmemorydbprovider.InMemoryDbProvider** dem package-Namen + Quelltextname der Datei,
die die von ContentProvider abgeleitete Klasse enthält.


&lt;provider<br>
	android:label="BhfLiveDebugProvider"<br>
	android:authorities="de.codeelements.inmemdb.provider"<br>
	android:name="de.codeelements.inmemorydbprovider.InMemoryDbProvider"<br>
	android:enabled="true"<br>
	android:exported="true"<br>
	android:multiprocess="true"<br>
	>
&lt;/provider>

wichtigste Einträge:
* label: (optional)
* authorities: beliebiger, aber eindeutiger Name des Providers
* name: package-Namen + Quelltextname der Datei,
die die von ContentProvider abgeleitete Klasse enthält (siehe oben).

### 1.2 Quelltext, der den ContentProvider enthält

überall dort, wo **PROVIDER_NAME** benutzt wird, ist der Text, der in android:authorities angegeben ist, zu benutzen

## 2. app, die den Provider benutzt (kann auch die sein, die den Provider enthält)

### 2.1 Manifest.xml

&lt;queries> <br>
&lt;package android:name="**de.codeelements.inmemorydbprovider**" /> <br>
&lt;/queries> <br>
	

**de.codeelements.inmemorydbprovider** ist dabei der package-Name der app, die den Provider enthält


	


  

		
		
		
