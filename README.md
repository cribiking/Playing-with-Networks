Primera pràctica 
Xarxes — Curs 2024-2025 
Grau en Enginyeria Informàtica 

===============================
## Chatín: «el xat pequeñín» 
Implementeu una aplicació client-servidor per xatejar. 

El servidor esperarà una única connexió a un port de la vostra elecció (e.g.: 1234). 

El client intentarà connectar-se al servidor i, si no és possible (bé sigui perquè aquest no s’ha iniciat o perquè ja no accepta més connexions) 
ho indicarà amb un missatge de "Servidor no disponible." i finalitzarà la seva execució. 

Un cop establerta la connexió, el servidor escriurà per pantalla i enviará pel socket el missatge "Connexió acceptada." (no necessàriament des del mateix 
fil d’execució). 

A partir d’aquest moment, tant el client com el servidor esperaran a que, o bé els hi arribi un missatge pel socket o bé l’usuari escrigui quelcom al terminal.  
Si  arriba  un  missatge, l’escriuran  per  pantalla  indicant  que  aquest  prové  de l’altra part (e.g.: si el servidor rep el missatge  "Hola",  pot  escriure  
per  pantalla  "Client:  «Hola»".  Si  l’usuari escriu alguna cosa, ho ha d’enviar pel socket. 

Per escriure i llegir pel socket, podeu fer servir writeUTF (+ flush) i readUTF. Per llegir de teclat podeu fer servir un BufferedReader i el mètode readLine. 

**BufferedReader br = new BufferedReader (new InputStreamReader (System.in));** 

Els missatges buits no s’han d’enviar. Per seguretat, verifiqueu aquest fet tant en el fil que llegeix de teclat com en el que rep del socket.       

Tingueu en compte que no es tracta d’escriure per torns, és a dir, no podeu anar alternant l’espera  al  socket  i  a  l’entrada  estàndard.  
Qualsevol  de les dues parts podria escriure uns quants missatges seguits. Per tant, haureu de fer servir threads. 

La conversa s’acaba quan qualsevol dels membres escriu (i envia) el missatge "FI". 

Assegureu que el programa finalitzi correctament tant en aquest cas com si un dels dos acaba abruptament amb un control-C o similar (tancant el socket). 


## Exemples / Joc de proves públic 
Un client intenta connectar-se sense èxit: 



|<p>**Servidor** </p><p>usuari@awserver:~$ </p>|<p>**Client** </p><p>usuari@haliax:~$ java ChatinClient   Servidor no disponible. usuari@haliax:~$ </p>|
| - | - |

Una conversa normal finalitzada pel servidor: 



|<p>**Servidor** </p><p>usuari@awserver:~$ java ChatinServer Connexió acceptada. </p><p>Client: «Hola Don Pepito» Hola Don José </p><p>Client: «¿Pasó usted por mi casa?» Por su casa yo pasé </p><p>Client: «Adios Don Pepito» Adios Don José </p><p>FI </p><p>usuari@awserver:~$ </p>|<p>**Client** </p><p>usuari@haliax:~$ java ChatinClient </p><p>Server: «Connexió acceptada.» Hola Don Pepito </p><p>Server: «Hola Don José» ¿Pasó usted por mi casa? </p><p>Server: «Por su casa yo pasé» Adios Don Pepito </p><p>Server: «Adios Don José» Server: «FI» usuari@haliax:~$ </p>|
| - | - |

Una altra finalitzada pel client: 



|<p>**Servidor** </p><p>usuari@awserver:~$ java ChatinServer </p><p>Connexió acceptada. Hola </p><p>Client: «Adeu» Client: «FI» usuari@awserver:~$ </p>|<p>**Client** </p><p>usuari@haliax:~$ java ChatinClient </p><p>Server: «Connexió acceptada.» Server: «Hola» </p><p>Adeu </p><p>FI </p><p>usuari@haliax:~$ </p>|
| - | - |

Una conversa finalitzada abruptament pel client: 



|<p>**Servidor** </p><p>usuari@awserver:~$ java ChatinServer Connexió acceptada. </p><p>Client:  «Vols  que  et  punxi  amb  un punxó?» </p><p>Punxa'm, però a la panxa no. ;) </p><p>Connexió tancada. usuari@awserver:~$ </p>|<p>**Client** </p><p>usuari@haliax:~$ java ChatinClient   </p><p>Server: «Connexió acceptada.» Vols que et punxi amb un punxó? </p><p>Server: «Punxa'm, però a la panxa no. ;)» </p><p>^C </p><p>usuari@haliax:~$ </p>|
| - | - |

Una altra finalitzada abruptament pel servidor: 



|<p>**Servidor** </p><p>usuari@awserver:~$ java ChatinServer   Connexió acceptada. </p><p>Client: «SPAM» </p><p>Client: «SPAM SPAM SPAM» Client: «SPAM!!!1!!1!» ^C </p><p>usuari@awserver:~$ </p>|<p>**Client** </p><p>usuari@haliax:~$ java ChatinClient   </p><p>Server: «Connexió acceptada.» SPAM </p><p>SPAM SPAM SPAM </p><p>SPAM!!!1!!1!    </p><p>Connexió tancada. usuari@haliax:~$ </p>|
| - | - |

## Mètodes útils 
- El mètode .close de la classe ServerSocket. 
- Els mètodes .get i .set de la classe AtomicBoolean. 
- El mètode .ready de la classe BufferedReader. 
- El mètode .sleep de la classe Thread (que espera el temps en ms). 
- L’excepció ConnectException per detectar que el servidor no està disponible. 
- La paraula clau finally per posar codi després del codi normal i el codi excepcional. 
- Els dos mètodes .debug de la classe Debugger que trobareu al campus virtual. Si la feu servir, assegureu-vos d’executar Debugger.debug = true; abans de començar.
- 
## Aspectes a tenir en compte 
Alguns aspectes que heu de tenir en compte a l'hora de realitzar les vostres pràctiques i que **puntuaran negativament** si no els teniu en compte, **encara que la pràctica funcioni**: 

- Nitidesa en el codi (tabulació correcta, no fer càlculs innecessaris, utilització correcta dels recursos de la màquina, etc.). 
- En cas de necessitar importar alguna biblioteca, aquesta ha de pertànyer al package de java (import java.\*). No està permès l’ús de llibreries externes. 
- Utilitzar estructures algorísmiques adients per als problemes a resoldre. 
- Utilitzeu noms de variables entenedors. 
- Llegiu atentament l'enunciat i no implementeu funcionalitats diferents a les que se us demana. 
- Si es detecta que la pràctica és copiada, la nota és un 0, tant pel que copia com pel copiat. 

Per poder enviar tots els fitxers sense problemes els podeu comprimir utilitzant la comanda 'tar' de la següent forma: 

Suposant que teniu el codi dins la carpeta 'prac1' podeu fer: 

cd prac1 

tar cvzf prac1.tar.gz \* 

El resultat és el fitxer prac1.tar.gz que conté tots els fitxers de la carpeta 'prac1'. Abans de comprimir la carpeta, esborreu els fitxers .class. 

## Consideracions 
- La pràctica s'ha d'implementar en llenguatge Java i s'ha d'executar correctament en una plataforma Linux. La versió de java ha de ser OpenJDK 17 o superior.
- La pràctica es lliurarà sempre via el Campus Virtual (CV), dins l'apartat Activitats. 
- Opcionalment la pràctica és pot lliurar via GitHub. Revisar apartat ‘Punt extra’. 
- Es recomana posar comentaris dins els fitxers .java que ajudin a interpretar el codi. 
- La pràctica s'ha de resoldre individualment o en grups de màxim 2 persones. 
- Com a comentaris de l'activitat heu d'indicar: 
- si la pràctica s'ha realitzat de forma individual o en grup.  
- heu d'indicar els membres que composen el grup.  
- comenteu breument l'estratègia emprada en la implementació. 
- link al repositori GitHub i branca a evaluar (si s’escau). 
## Punt extra 
Es valora amb 1 punt extra l’ús i presentació de la pràctica via GitHub. 

La publicació via aquest canal NO exclou la presentació via Campus Virtual. 

Aquest punt extra se suma a la nota global de la pràctica essent com a màxim un 10 (no 11). 

En cas de la realització de la pràctica en grup, només és necessari que el repositori estigui en un dels dos membres. 

El  repositori  ha  de  ser  en  tot  moment  de  caràcter  privat  i  pot  contenir  els  commits  que s’hagin  anat  realitzant  durant  el  seu  desenvolupament. 
No  es  valora  (ni  negativament  ni positivament) qualsevol commit intermig. 

No es permet la realització de commits posteriors a la data d’entrega de la pràctica. Aquest fet invalida directament el punt extra. Si es desitja fer-ho cal 
crear una nova branca que no sigui la de la entrega. 

Per aconseguir el punt extra cal: 

- Realitzar  una  invitació  del  repositori  a  <albert.rovira@udl.cat>  amb  permisos d’administració abans de la data límit d’entrega de la pràctica. 
- Que el repositori sigui clonable i executable sense haver de configurar ni canviar cap fitxer. 
- Que  el  repositori  inclogui  a  l’arrel  un  fitxer  readme.md  formatejat  correctament indicant  la  mateixa informació que en l’entrega de l’activitat dins el 
campus virtual. Inclou una frase pel record. 
