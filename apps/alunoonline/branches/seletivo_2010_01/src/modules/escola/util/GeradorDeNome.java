
package modules.escola.util;

import java.util.Random;

/**
 *
 * @author Jorge Rafael
 */
public class GeradorDeNome {

	public static final GeradorDeNome INSTANCE = new GeradorDeNome();
	
	private Random rdm;

    private GeradorDeNome() {
         rdm = new Random(System.currentTimeMillis());
    }

	public GeradorDeNome getInstance(){
		return INSTANCE;
	}

    private String preNomeFeminino[] = {
                "Abigail",	"Ac�cia",	"Ada",          "Adalgisa",     "Adelaide",     "Adele",	"Ad�lia",
                "Adelina",	"Adelinda",	"Adonir�",      "Adorinda",     "Adosinda",     "Adriana",	"Agnes",
                "Agnete",	"Agostinha",	"Aida",         "Ala�de",       "Alana",        "Alanis",	"Alba",
                "Alberta",	"Albertina",	"Albina",       "Alcina",       "Alcione",      "Alda",         "Aldara",
                "Aldina",	"Alessandra",	"Alexandra",	"Alexandrina",	"Alice",	"Al�cia",	"Aline",
                "Alison",	"Alma",         "Altina",	"Alva",         "Alzira",	"Am�lia",	"Amanda",
                "Am�lia",	"Am�rica",	"Ana",          "Anabela",	"Ana�sa",	"Anast�cia",	"Andrea",
                "Andreia",	"Andr�ia",	"Andressa",	"Andreza",	"Angela",	"�ngela",	"Ang�lica",
                "Angelina",	"Ania",         "Anita",	"Anna",         "Anne",         "Anny",         "Ant�nia",
                "Ant�nia",	"Antonieta",	"Aparecida",	"Apol�nia",	"Ariana",	"Ariane",	"Ariela",
                "Arlete",	"Armanda",	"Arminda",	"Assun��o",	"Assunta",	"Ataliba",	"Augusta",
                "Aura",         "�urea",	"Aur�lia",	"Auria",	"Aurora",	"Avelina",	"Azal�ia",
                "B�rbara",	"Batista",	"Beatriz",	"Bebiana",	"Bela",         "Belarmina",	"Belinda",
                "Belisa",	"Belmira",	"Benedita",	"Benvinda",	"Berenice",	"Bernadete",	"Bernarda",
                "Bernardete",	"Berta",	"Bet�nia",	"Betina",	"Bia",          "Bianca",	"Biatriz",
                "Blanca",	"Blenda",	"Branca",	"Brenda",	"Brigida",	"Br�gida",	"Brigite",
                "Brisa",	"Bruna",	"Brunna",	"Cacilda",	"Caetana",	"Caiane",	"Camila",
                "Camile",	"C�ndida",	"Capitolina",	"Carina",	"Carine",	"Cariny",	"Carla",
                "Carlota",	"Carmelinda",	"Carmem",	"Carmen",	"Carminda",	"Carmo",	"Carol",
                "Carolina",	"Caroline",	"Cassandra",	"C�ssia",	"Cassilda",	"Catarina",	"C�tia",
                "Cec�lia",	"Celeste",	"Celestina",	"C�lia",	"Celina",	"Ces�ria",	"Cibele",
                "Cibelli",	"Cid�lia",	"Cinara",	"Cinira",	"Cintia",	"C�ntia",	"Clara",
                "Clarissa",	"Clarisse",	"Cl�udia",	"Cleide",	"Cl�lia",	"Clementina",	"Cl�o",
                "Cleusa",	"Cloe",         "Clotilde",	"Concei��o",	"Constan�a",	"Consuela",	"Cora",
                "Cord�lia",	"Corina",	"Corn�lia",	"Cremilda",	"Cremilde",	"Cristela",	"Cristiana",
                "Cristiane",	"Cristina",	"Cust�dia",	"Cynthia",	"Dagmar",	"Daiana",	"Daisy",
                "D�lia",	"Dalila",	"Dalva",	"Damiana",	"Dana",         "D�nia",	"Daniela",
                "Daniele",	"Danielly",	"Dara",         "Darcy",	"Darlene",	"Dayane",	"Dayani",
                "Dayse",	"D�bora",	"D�ia",         "Dejanira",	"Delfina",	"Delores",	"Denise",
                "Deolinda",	"Deonilde",	"D�siree",	"Diana",	"Dina",         "Din�",         "Dinah",
                "Dinora",	"Dinorah",	"Dione",	"Dirce",	"Disa",         "Diva",         "Djalma",
                "Djamila",	"Dolores",	"Dora",         "Dores",	"D�ria",	"D�ris",	"Dorisse",
                "Dorot�ia",	"Du�lha",	"Dulce",	"Dulcineia",	"D�nia",	"Edelina",	"Edite",
                "Edith",	"Edna",         "Eduarda",	"Efig�nia",	"Elaine",	"Elda",         "Elen",
                "Eleonora",	"Eli",          "�lia",         "Eliana",	"Elisa",	"Elisabete",	"Elisabeth",
                "Elizabete",	"Elizabeth",	"Eloisa",	"Elsa",         "Elvira",	"Elza", 	"Ema",
                "Emanuela",	"Em�lia",	"Emilly",	"Erc�lia",	"Erica",	"�rica",	"Ermelinda",
                "Ermengarda",	"Erm�nia",	"Erotides",	"Esmeralda",	"Estef�nia",	"Estela",	"Ester",
                "Estrela",	"Etelvina",	"Eug�nia",	"Eul�lia",	"Eunice",	"Eust�cia",	"Eva",
                "Evangelina",	"Evelina",	"Evelise",	"Evelyn",	"Evilyn",	"F�bia",	"Fabiana",
                "Fab�ola",	"F�tima",	"Faustina",	"Fedra",	"Felicia",	"Felicidade",	"Felismina",
                "Ferdinanda",	"Fernanda",	"Filipa",	"Filomena",	"Firmina",	"Fl�via",	"Flaviana",
                "Flor",         "Fl�r",         "Flora",	"Florbela",	"Flor�ncia",	"Florinda",	"Francelina",
                "Francesca",	"Franciane",	"Francileia",	"Francisca",	"Fransy",	"Frederica",	"F�lvia",
                "Gabriela",	"Gabrielly",	"Gedeanne",	"Ge�sa",	"Genaina",	"Genoveva",	"Georgina",
                "Geralda",	"Geraldina",	"Gerlane",	"Gertrudes",	"Gilberta",	"Gilda",	"Gilsiara",
                "Gina",         "Giovana",	"Gisela",	"Gisele",	"Giselly",	"Gizela",	"Glaucia",
                "Glena",	"Glenda",	"Gl�ria",	"Gorete",	"Gra�a",	"Graciete",	"Graziela",
                "Grazielli",	"Greta",	"Guida",	"Guilhermina",	"Hagar",	"Hanna",	"Hebe",
                "Heide",	"Heidi",	"Helena",	"Helga",	"H�lia",	"Heloisa",	"Helo�sa",
                "Henriqueta",	"Herc�lia",	"Herm�nia",	"Heronilda",	"Hildegarda",	"Hirondina",	"Hon�ria",
                "Hort�ncia",	"Hort�nsia",	"Iami",         "Ianara",	"Iara",         "Iarine",	"Iasmim",
                "Idalina",	"Idalinda",	"Ieda",     	"Ifig�nia",	"Ilara",	"Ilda",         "Imaculada",
                "�ndia",	"Indyara",	"In�s",         "Ingrid",	"Iolanda",	"Iracema",	"Iraci",
                "Irene",	"Irina",	"Iris",         "�ris",         "Irma",         "Isa",          "Isabel",
                "Isabela",	"Isabelle",	"Isabelly",	"Isadora",	"Isaura",	"Isilda",	"Isis",
                "Isolda",	"Isolina",	"Ivana",	"Ivani",	"Ivete",	"Ivone",	"Jacinta",
                "Jacy",         "Jade",         "Jamile",	"Jana�na",	"Jandira",	"Janete",	"Jaqueline",
                "Jasmim",	"Jasmin",	"Jenifer",	"Jesabel",	"Jess�",	"Jessica",	"J�ssica",
                "Jesus",	"Joana",	"Joaquina",	"Jocasta",	"Jocelina",	"Jocosa",	"Joice",
                "Jolinda",	"Jonata",	"Josefa",	"Josefina",	"Jos�lia",	"Josisbel",	"Jouse",
                "Joyce",	"Juca",         "Judite",	"Judith",	"J�lia",	"Juliana",	"Juliane",
                "Julieta",	"Junia",	"Jussara",	"Justina",	"Juvelina",	"Ka�",          "Kalinne",
                "Kallyana",	"Karen",	"Karina",	"Karleay",	"Karol",	"Kassandra",	"Katia",
                "Katiane",	"Kati�ccia",	"Kauana",	"Kayla",	"Kaylane",	"Keila",	"Kelly",
                "K�sia",	"Kiara",	"Kira",         "Kirina",	"Laila",	"La�s",         "Laiza",
                "Lana",         "Lanai",	"Lanna",	"Lara",         "Larissa",	"Latipha",	"Laura",
                "Laurentina",	"Laurinda",	"Lav�nia",	"Lea",          "Leandra",	"Leda",         "L�ia",
                "Leila",	"Lena",         "Lenah",	"Leni",         "Leoc�dia",	"Leonor",	"Leopoldina",
                "Leticia",	"Let�cia",	"Levina",	"Leydiane",	"Lia",          "Liah",         "Liana",
                "Liane",	"Lib�nia",	"Lidia",	"L�dia",	"Lidiane",	"Ligia",	"L�gia",
                "Lilia",	"L�lia",	"Lilian",	"Liliana",	"Liliany",	"Lina",         "Linda",
                "Lindalva",	"Lindomar",	"Lis",          "Lisandra",	"Lissa",	"Lita",         "L�via",
                "Liz",          "Loide",	"Lorena",	"Loreta",	"Lourdes",	"Louyse",	"Lua",
                "Luana",	"Luane",	"Luani",	"Luca",         "Lucas",	"L�cia",	"Luciana",
                "Lucilene",	"Luc�lia",	"Lucimara",	"Lucimeire",	"Lucinda",	"Lucr�cia",	"Lucy",
                "Lucyobello",	"Luisa",	"Lu�sa",	"Luly",         "Luma",         "Lurdes",	"Luzia",
                "Lyana",	"Mabel",	"Madalena",	"Mafalda",	"Magali",	"Magda",	"Ma�sa",
                "Mait�",	"Malvina",	"Manuela",	"Manuelina",	"Mara",         "Marcela",	"Marcelle",
                "M�rcia",	"Marcilene",	"Margarete",	"Margarida",	"Maria",	"Mariah",	"Mariana",
                "Mariel",	"Marieta",	"Mariete",	"Marilena",	"Mar�lia",	"Marina",	"Marinela",
                "Maris",	"Marisa",	"Marisela",	"Marisol",	"Maristela",	"Marlene",	"Marta",
                "Martina",	"Matilde",	"Maura",	"Maur�cia",	"Mavilde",	"M�xima",	"Mayara",
                "Mayra",	"Mayt�",	"Meire",	"Melania",	"Melanie",	"Melinda",	"Melissa",
                "Micaela",	"Micaelly",	"Micheli",	"Michelle",	"Mikaela",	"Milena",	"Milene",
                "Mileny",	"Mira",         "Miracema",	"Miranda",	"Miriam",	"Misael",	"M�nica",
                "Morgana",	"Muriel",	"N�dia",	"Nadina",	"Nadine",	"Naielly",	"Naila",
                "Nair",         "Nana",         "Nancy",	"Naomi",	"Nara",         "Natacha",	"Nat�lia",
                "Natalina",	"Nat�rcia",	"Nathally",	"Nathiele",	"Natividade",	"Nazar�",	"Neandro",
                "Nei",          "Neide",	"Neiva",	"Neli",         "N�lia",	"N�lida",	"Nelma",
                "Neusa",	"Neuza",	"Nicia",	"Nicole",	"Nicolie",	"Nicolle",	"Nicolli",
                "Nicolly",	"N�dia",	"Nilda",	"Nilsa",	"Nina",         "Nisa",         "Nivalda",
                "N�vea",	"Nivia",	"N�via",	"No�lia",	"Noemi",	"Noemia",	"No�mia",
                "Norma",	"N�bia",	"N�ria",	"Obede",	"Oct�via",	"Odete",	"Od�lia",
                "Of�lia",	"Olga",         "Ol�mpia",	"Olinda",	"Olivia",	"Ol�via",	"Ondina",
                "Orlanda",	"Ot�via",	"Ot�lia",	"Palmira",	"Paloma",	"Pamela",	"Paola",
                "Patr�cia",	"Paula",	"Paulina",	"P�rola",	"Petra",	"Pietra",	"Pilar",
                "Pl�cida",	"Poliana",	"Pollyanna",	"Priscila",	"Pr�spera",	"Prud�ncia",	"Quirina",
                "Quit�ria",	"Rafaela",	"Raiane",	"Raica",	"Railma",	"Raimunda",	"Raissa",
                "Raquel",	"Ravena",	"Rayane",	"Rebeca",	"Rebecca",	"Regina",	"Renata",
                "Ricarda",	"Ricardina",	"Rita",         "Roberta",	"Romana",	"Rosa",         "Ros�lia",
                "Rosana",	"Ros�ngela",	"Ros�ria",	"Ros�rio",	"Rose",         "Rosi",         "Rosinha",
                "Rosy",         "R�bia",	"Rubina",	"Rute",         "Sabina",	"Sabrina",	"Salema",
                "Salete",	"Salom�",	"Samanta",	"Samara",	"Sandra",	"Sara",         "Sarita",
                "Sasha",	"Saskya",	"Savina",	"Selma",	"Serena",	"Sheila",	"Shirley",
                "Silva",	"Silvana",	"Silv�ria",	"Silvia",	"S�lvia",	"Simone",	"Sofia",
                "Sol",          "Solange",	"S�nia",	"Sophia",	"Soraia",	"Stela",	"Stephanie",
                "Suelen",	"Sueli",	"Suelly",	"Susana",	"Susete",	"Tabata",	"Tabita",
                "Taciana",	"Ta�s",         "Talita",	"Tamara",	"T�mara",	"Tamires",	"T�nia",
                "T�nia",	"Tarcila",	"Tasha",	"T�ssia",	"Tatiana",	"Tatiane",	"Telma",
                "Teodora",	"Teresa",	"Tha�s",	"Tha�sa",	"Thalita",	"Tharcila",	"Thays",
                "Umblina",	"Urraca",	"Ursula",	"�rsula",	"Valentina",	"Val�ria",	"Valesca",
                "Valqu�ria",	"Vanda",	"Vanessa",	"V�nia",	"Vanusa",	"Vera",         "Verena",
                "Veridiana",	"Ver�nica",	"Ver�nica",	"Vilma",	"Violante",	"Violeta",	"Virginia",
                "Virg�nia",	"Virgolina",	"Vit�ria",	"Vivian",	"Viviana",	"Viviane",	"Wanessa",
                "Wilma",	"X�nia",	"Yara",         "Yasmin",	"Yasmine",	"Yolanda",	"Zaira"
    };

    private String preNomeMasc[] = {
                "Aar�o",	"Abel",         "Ab�lio",	"Abra�o",	"Ac�cio",	"Adalberto",	"Ad�o",
                "Adauto",	"Adelino",	"Adelmiro",	"Ademar",	"Ademir",	"Ad�rito",	"Ado",
                "Adolfo",	"Adosindo",	"Adriano",	"Adri�o",	"A�cio",	"Afonso",	"Agostinho",
                "Aguinaldo",	"A�rton",	"Alan",         "Albano",	"Alberto",	"Albino",	"Alcir",
                "Aldino",	"Aldir",	"Aldo",         "Alencar",	"Alessandro",	"Alex", 	"Alexander",
                "Alexandre",	"Alfeu",	"Alfredo",	"Al�rio",	"Altair",	"Altino",	"�lvaro",
                "Amadeu",	"Amado",	"Amador",	"Am�ndio",	"Amaro",	"Am�vel",	"Am�rico",
                "Amilcar",	"Anacleto",	"Anast�cio",	"Anderson",	"Andr�",	"Andrei",	"Andrew",
                "Andrey",	"�ngelo",	"An�bal",	"Anselmo",	"Antenor",	"Antero",	"Ant�nio",
                "Ant�nio",	"Apolin�rio",	"Apolo",	"Ap�lo",	"Aponino",	"Aquiles",	"Arcelino",
                "Argeu",	"Ari",          "Arist�teles",	"Arlindo",	"Armando",	"Arm�nio",	"Armindo",
                "Arnaldo",	"Arthur",	"Artur",	"Ary",          "Asdr�bal",	"Atan�sio",	"At�lio",
                "Augusto",	"Aur�lio",	"Avelino",	"Ayrton",	"Baltasar",	"Barnab�",	"Bartolo",
                "Bartolomeu",	"Bas�lio",	"Batista",	"Belarmino",	"Belmiro",	"Benedito",	"Benito",
                "Benjamim",	"Benjamin",	"Bento",	"Bernardo",	"Bonif�cio",	"Brandon",	"Br�s",
                "Br�ulio",	"Brayan",	"Bren",         "Brian",	"Br�gido",	"Brivaldo",	"Bruno",
                "Bryan",	"Caetano",	"Caim",         "Caio",         "Ca�que",	"Calebe",	"Calisto",
                "Calvin",	"Calvino",	"Camilo",	"C�ndido",	"Capitolino",	"Carlos",	"Carmelo",
                "Casimiro",	"C�ssio",	"Cau�",         "C�lio",	"Celso",	"C�sar",	"Ces�rio",
                "Christian",	"C�cero",	"Cid",          "Ciro",         "Claudemir",	"Claudimir",	"Claudino",
                "Cl�udio",	"Cl�ber",	"Clemente",	"Clementino",	"Cl�stenes",	"Clodoaldo",	"Cl�vis",
                "Conrado",	"Const�ncio",	"Constantino",	"Consuelo",	"Corn�lio",	"Cosme",	"Cristhian",
                "Cristian",	"Cristiano",	"Crist�v�o",	"Cust�dio",	"D�cio",	"Dami�o",	"Daniel",
                "Danilo",	"D�rio",	"Davi",         "David",	"Delfim",	"Delfino",	"Diego",
                "Dinarte",	"Dinis",	"Diniz",	"Di�genes",	"Diogo",	"Dion�sio",	"Domingos",
                "Donaldo",	"Donato",	"Douglas",	"Duarte",	"D�lio",	"Durval",	"Edelmiro",
                "Edgar",	"Edgard",	"Ediberto",	"Edmar",	"Edmundo",	"Edson",	"Eduardo",
                "Eg�dio",	"Egon",         "Eleut�rio",	"Eli",          "Elian",	"Elias",	"�lio",
                "Eliseu",	"Elizeu",	"El�i",         "Eloy",         "Elton",	"Elvis",	"Emanuel",
                "Emerson",	"Em�dio",	"Emilio",	"Em�lio",	"�nio",         "Erasmo",	"Eric",
                "Erico",	"Erm�nio",	"Ernane",	"Ernani",	"Ernesto",	"Esa�",         "Esdras",
                "Estev�o",	"Eug�nio",	"Eurico",	"Eus�bio",	"Eut�mio",	"Evaldo",	"Evandro",
                "Ezequias",	"Ezequiel",	"Fabiano",	"F�bio",	"Fabr�cio",	"Fagner",	"Faustino",
                "Fausto",	"Fel�cio",	"Felipe",	"Felisberto",	"F�lix",	"Ferdinando",	"Fernando",
                "Filipe",	"Firmino",	"Flaviano",	"Fl�vio",	"Flor�ncio",	"Florival",	"Francesco",
                "Francis",	"Francisco",	"Franklin",	"Frederico",	"Gabriel",	"Gaspar",	"Gast�o",
                "George",	"Geraldino",	"Geraldo",	"Germano",	"Gerson",	"Gerv�sio",	"Get�lio",
                "Gianluca",	"Gianpietro",	"Gide�o",	"Gil",          "Gilberto",	"Gildo",	"Giulio",
                "Glauco",	"Godofredo",	"Golias",	"Gon�alo",	"Greg�rio",	"Guido",	"Guilherme",
                "Guilhermino",	"Gustavo",	"Hamilton",	"Heitor",	"Helder",	"H�lder",	"H�lio",
                "Henrico",	"Henrique",	"Henry",	"Herculano",	"H�rcules",	"Hermano",	"Hermes",
                "Hern�ni",	"Herodes",	"Heyder",	"Higino",	"Hil�rio",	"Hildebrando",	"Hildefonso",
                "Hilton",	"Hip�lito",	"Hon�rio",	"Hor�cio",	"Hugo",         "Humberto",	"Ian",
                "Idal�cio",	"Igo",          "Igor",         "Il�rio",	"Il�dio",	"In�cio",	"Irineu",
                "Isaac",	"Isaias",	"Isa�as",	"Ismael",	"Israel",	"�talo",	"Iuan",
                "Iuri",         "Iv�",          "Ivan",         "Ivo",          "Jacinto",	"Jac�",         "Jacob",
                "Jaime",	"Jairo",	"Janu�rio",	"Jeferson",	"Jefferson",	"Jeremias",	"Jer�nimo",
                "Jesse",	"Jesus",	"J�",           "Jo�o",         "Joaquim",	"Joel",         "Jonas",
                "Jonatas",	"Jonathan",	"Jorge",	"Josaf�",	"Jos�",         "Joselindo",	"Josualdo",
                "Josu�",	"Joviano",	"Judas",	"Juliano",	"Julio",	"J�lio",	"Julius",
                "Justino",	"Kailson",	"Kau�",         "Kauai",	"Kau�",         "Kelvin",	"Kenedy",
                "Kevin",	"Kilson",	"Kleber",	"Kleisom",	"Ladislau",	"Lancelote",	"Laurindo",
                "Lauro",	"Leandro",	"Leo",          "Leoc�dio",	"Leonardo",	"Le�ncio",	"Leonel",
                "Le�nidas",	"Leopoldo",	"Levi",         "Lib�nio",	"Lic�nio",	"Lino",         "Lisandro",
                "L�vio",	"Lorenzo",	"Louren�o",	"Lourival",	"Lucas",	"Luciano",	"L�cio",
                "Ludovico",	"Luis",         "Lu�s",         "Luiz",         "Manuel",	"Marcelo",	"Marciano",
                "M�rcio",	"Marco",	"Marcos",	"Marcus",	"Mariano",	"M�rio",	"Marlon",
                "Martim",	"Martin",	"Martinho",	"Mateus",	"Matheus",	"Matias",	"Maur�cio",
                "Mauro",	"Maximiliano",	"M�ximo",	"Meninos",	"Michael",	"Michel",	"Miguel",
                "Milo",         "Milton",	"Moacir",	"Mois�s",	"Muriel",	"Murilo",	"Nabal",
                "Nadir",	"Narciso",	"Nat�lio",	"Natanael",	"Nataniel",	"Naum",         "Naz�rio",
                "N�lio",	"Nelson",	"N�lson",	"Nero",         "Nestor",	"Nico",         "Nicolas",
                "Nicolau",	"Nildo",	"Nilson",	"Nilton",	"Nivaldo",	"Norberto",	"Normando",
                "Nuno",         "Odair",	"Od�cio",	"Odir",         "Odorico",	"Olavo",	"Oleg�rio",
                "Ol�mpio",	"Olivier",	"Ol�vio",	"Omar",         "Onofre",	"Orfeu",	"Orlando",
                "Oscar",	"�scar",	"Osias",	"Osmar",	"Os�rio",	"Osvaldo",	"Ot�vio",
                "Otoniel",	"Pablo",	"Pascoal",	"Patr�cio",	"Paulino",	"Paulo",	"Pedro",
                "Pel�gio",	"Perceu",	"P�ricles",	"Petrucio",	"Pl�cido",	"Pl�nio",	"Priscilo",
                "Querubim",	"Quintim",	"Quintino",	"Quirino",	"Quixote",	"Rafael",	"Raimundo",
                "Ramiro",	"Ramom",	"Ramon",	"Raphael",	"Raul",         "Ra�l",         "Reginaldo",
                "Reinaldo",	"Renan",	"Renato",	"Ricardino",	"Ricardo",	"Roberto",	"Robson",
                "Rodolfo",	"Rodrigo",	"Rog�rio",	"Romeu",	"Romualdo",	"R�mulo",	"Ronaldo",
                "Ruben",	"R�ben",	"Rubens",	"Rudolf",	"Rui",          "Sabino",	"Salom�o",
                "Salvador",	"Samuel",	"Sandro",	"Saul",         "Sa�l",         "Sebasti�o",	"Serafim",
                "S�rgio",	"Sidney",	"Sid�nio",	"Silas",	"Silvano",	"Silv�rio",	"Silvestre",
                "Silvino",	"Silvio",	"S�lvio",	"Sim�o",	"S�crates",	"Tadeu",	"Tarcisio",
                "Tarc�sio",	"Telmo",	"T�o",          "Teobaldo",	"Teodoro",	"Te�filo",	"Theo",
                "Tiago",	"Tib�rcio",	"Tim�teo",	"Tito",         "Tom�s",	"Tom�",         "Torquato",
                "Trist�o",	"T�lio",	"Ubaldino",	"Ubirat�",	"Ulisses",	"Umbelino",	"Umberto",
                "Urbano",	"Urbino",	"Ursulino",	"Vagner",	"Valadares",	"Valber",	"Valdecir",
                "Valdeir",	"Valdemar",	"Valdemiro",	"Valdir",	"Valdo",	"Valentim",	"Valentino",
                "Val�rio",	"Valmir",	"Valter",	"Vanderlei",	"Vanderley",	"Vasco",	"Veloso",
                "Ven�ncio",	"Venceslau",	"Vicente",	"Victor",	"Vidigal",	"Vieira",	"Vilela",
                "Vilmar",	"Vinicios",	"Vinicius",	"Vin�cius",	"Virg�lio",	"Virgolino",	"Viriato",
                "Vital",	"Vitor",	"Vitorino",	"Vivaldo",	"Vladimir",	"Vlamir",	"Volney",
                "Wagner",	"Waldemar",	"Wallace",	"Walter",	"Wilian",	"Wilker",	"William",
                "Willian",	"Wilson",	"Wilton",	"Wladimir",	"Xavier",	"Yuri",         "Zacarias"
    };

    private String sobreNomes[] = {
                "Abreu",	"Acioli",	"Adams",	"Agostinho",	"Aguiar",	"Aires",	"Albino",
                "Alencar",	"Almeida",	"Alvarenga",	"Alves",	"Amadeu",	"Amado",	"Amaral",
                "Amaro",	"Ambr�sio",	"Am�rico",	"Amora",	"Amorim",	"Andrada",	"Andrade",
                "Antunes",	"Aparicio",	"Apar�cio",	"Araujo",	"Ara�jo",	"Assis",	"Assun��o",
                "Augusto",	"Auxiliadora",	"�vila",	"Azeredo",	"Azevedo",	"Bacelar",	"Baltazar",
                "Bandeira",	"Barbosa",	"Barcellos",	"Barcelos",	"Barra",	"Barreto",	"Barros",
                "Barroso",	"Bastos",	"Batista",	"Belem",	"Benevides",	"Benvindo",	"Beretta",
                "Berger",	"Bergmann",	"Bertran",	"Bettencourt",	"Bezerra",	"Bicudo",	"Bittencourt",
                "Boaventura",	"Bona",         "Borba",	"Borges",	"Botelho",	"Braga",	"Branco",
                "Brand�o",	"Br�s",         "Brasil",	"Brito",	"Bueno",	"Cabral",	"Cachoeira",
                "Caldas",	"Calmon",	"Camargo",	"Campos",	"Canto",	"Cardoso",	"Carmo",
                "Carneiro",	"Carraro",	"Carvalho",	"Castanho",	"Castilho",	"Castro",	"Cavalcanti",
                "Cerqueira",	"Chagas",	"Chaves",	"Claudino",	"Clemente",	"Cleto",	"Coelho",
                "Coimbra",	"Concei��o",	"Cordeiro",	"Cordova",	"Correa",	"Corr�a",	"Correia",
                "Costa",	"Costela",	"Coutinho",	"Cruz",         "Cunha",	"Cust�dia",	"Cust�dio",
                "Damann",	"Dam�sio",	"D�vila",	"Dias",         "Dinis",	"Domingues",	"D�rea",
                "D�ria",	"Dorneles",	"Dorta",	"Duarte",	"Durante",	"Dutra",	"Espindola",
                "Esp�rito",	"Estrela",	"Fagundes",	"Faria",	"Farias",	"Feij�",	"Feliciano",
                "Fernandes",	"Ferreira",	"Ferretti",	"Figueira",	"Figueiredo",	"Filgueira",	"Flor",
                "Fonseca",	"Fortes",	"Fraga",	"Fragas",	"Fran�a",	"Freire",	"Freitas",
                "Frey",         "Furtado",	"Gadelha",	"Garcia",	"Gil",          "Gomes",	"Gon�alves",
                "Gonzalez",	"Goulart",	"Guedes",	"Guerra",	"Guimar�es",	"Gurgel",	"Gusm�o",
                "Hering",	"Holanda",	"Homem",	"Ibiapina",	"In�cio",	"Jesus",	"Jord�o",
                "Ladeira",	"Laranja",	"Laranjeira",	"Lauben",	"Laurentino",	"Laurindo",	"Leal",
                "Le�o",         "Leit�o",	"Leite",	"Lemos",	"Levi",         "Levy",         "Lima",
                "Limas",	"Linhares",	"Lisboa",	"Liz",          "Lobato",	"Loiola",	"Lombardi",
                "Lopes",	"Lopez",	"Macedo",	"Mac�do",	"Machado",	"Maciel",	"Magalh�es",
                "Maia",         "Malafaia",	"Malta",	"Maluf",	"Marcolla",	"Maria",	"Marques",
                "Marquez",	"Marschalk",	"Martinez",	"Martins",	"Mascarenhas",	"Mata",         "Matias",
                "Matos",	"Mazzotti",	"Medeiros",	"Medina",	"Meira",	"Meireles",	"Melo",
                "Mendel",	"Mendes",	"Mendon�a",	"Menezes",	"Mesquita",	"Mestre",	"Miranda",
                "Molina",	"Monteiro",	"Montenegro",	"Moraes",	"Morais",	"Moreira",	"Moreno",
                "Mota",         "Moura",	"Mour�o",	"Muniz",	"Nascimento",	"Navarro",	"Neri",
                "Neto",         "Neves",	"Nogueira",	"Noronha",	"Nunes",	"Oliveira",	"Onofre",
                "Ortiz",	"Os�rio",	"Otto",         "Pacheco",	"Padilha",	"Paiva",	"Parente",
                "Paulino",	"Paz",          "Peixe",	"Penha",	"Pereira",	"Peres",	"Pessoa",
                "Pimenta",	"Pimentel",	"Pina",         "Pinheiro",	"Pinho",	"Pinto",	"Pires",
                "Pontes",	"Prado",	"Prestes",	"Quadros",	"Queir�s",	"Queiroz",	"Rabelo",
                "Ramalho",	"Ramos",	"Rebelo",	"Rego",         "Reis",         "Rengel",	"Resende",
                "Rezende",	"Ribeiro",	"Rios",         "Rocha",	"Rodrigues",	"Rom�o",	"Rosa",
                "Rossi",	"S�",           "Sabino",	"Sales",	"Salgado",	"Salgueiro",	"Salim",
                "Salles",	"Salvador",	"Salvadori",	"Santa",	"Santiago",	"Santo",	"Santos",
                "Saraiva",	"Sardinha",	"Seixas",	"Selva",	"Serafim",	"Serra",	"Severino",
                "Severo",	"Silva",	"Silveira",	"Silv�rio",	"Sim�o",	"Simones",	"Simpl�cio",
                "Siqueira",	"Sirino",	"Soares",	"Sousa",	"Souto",	"Souza",	"Spindola",
                "Tavares",	"Teixeira",	"Teles",	"Texeira",	"Toledo",	"Torres",	"Tourinho",
                "Trindade",	"Uchoa",	"Valadares",	"Vale",         "Val�rio",	"Vargas",	"Vasconcelos",
                "Vaz",          "Veber",	"Veiga",	"Velasco",	"Veloso",	"Ventura",	"Venturi",
                "Viana",	"Vicente",	"Vieira",	"Vilalobos",	"Walter",	"Werner",	"Xavier"
    };

    // As strings com espa�o s�o pra evitar de ter sempre
    // uma preposi��o e j� serve como espa�o entre os nomes.
    private String preposicao[] = {
                " da ", " ", " ", " ", " " , " ",
                " de ", " ", " ", " ", " " , " ",
                " e ",  " ", " ", " ", " " , " ",
                //" ", "", "", "",
    };

    /**
     * Este m�todo gera aleatoriamente um nome feminino contendo apenas
     * prenome e sobrenome.
     * @return nome simples
     */
    public String nomeFeminino(){
        String nome = null;


        // Pega o prenome
        int i = rdm.nextInt(preNomeFeminino.length);
        nome = preNomeFeminino[i];

        // Coloca uma preposi��o ou n�o
        i = rdm.nextInt(preposicao.length);
        nome = nome + preposicao[i];

        // pega o sobrenome
        i = rdm.nextInt(sobreNomes.length);
        nome = nome + sobreNomes[i];

        return nome;
    }

    /**
     * Cria um nome feminino onde � poss�vel indicar se haver� nome composto
     * e se ter� dois sobrenomes. Quando os par�metros forem falsos, funcionar�
     * igual ao m�todo <i>nomeFeminino()</i>.
     * @param nomeComposto igual a <b>true</b> se quiser nome composto.
     * @param doisSobrenomes igual a <b>true</b> se quiser dois sobrenomes.
     * @return
     */
    public String nomeFeminino(boolean nomeComposto, boolean doisSobrenomes){
        String nome = null;

        // Pega o prenome
        int i = rdm.nextInt(preNomeFeminino.length);
        nome = preNomeFeminino[i];

         if (nomeComposto){
            i = rdm.nextInt(preNomeFeminino.length);
            // Pega o segundo nome e testa para que ele
            // n�o seja igual ao primeiro
            while(nome.equals(preNomeFeminino[i])){
                i = rdm.nextInt(preNomeFeminino.length);   // pega uma nova posi��o para teste
            }
            nome = nome + " " + preNomeFeminino[i];
        }

        if (doisSobrenomes){
            // Coloca uma preposi��o ou n�o para o primeiro sobrenome
            i = rdm.nextInt(preposicao.length);
            nome = nome + preposicao[i];

            // pega o primeiro sobrenome
            i = rdm.nextInt(sobreNomes.length);
            nome = nome + sobreNomes[i];
        }
        // Coloca uma preposi��o ou n�o para o �ltimo sobrenome
        i = rdm.nextInt(preposicao.length);
        nome = nome + preposicao[i];

        // pega o �ltimo sobrenome
        i = rdm.nextInt(sobreNomes.length);
        nome = nome + sobreNomes[i];

        return nome;
    }

        /**
     * Este m�todo gera aleatoriamente um nome feminino contendo apenas
     * prenome e sobrenome.
     * @return nome simples
     */
    public String nomeMasculino(){
        String nome = null;

        // Pega o prenome
        int i = rdm.nextInt(preNomeMasc.length);
        nome = preNomeMasc[i];

        // Coloca uma preposi��o ou n�o
        i = rdm.nextInt(preposicao.length);
        nome = nome + preposicao[i];

        // pega o sobrenome
        i = rdm.nextInt(sobreNomes.length);
        nome = nome + sobreNomes[i];

        return nome;
    }

    /**
     * Cria um nome feminino onde � poss�vel indicar se haver� nome composto
     * e se ter� dois sobrenomes. Quando os par�metros forem falsos, funcionar�
     * igual ao m�todo <i>nomeFeminino()</i>.
     * @param nomeComposto igual a <b>true</b> se quiser nome composto.
     * @param doisSobrenomes igual a <b>true</b> se quiser dois sobrenomes.
     * @return
     */
    public String nomeMasculino(boolean nomeComposto, boolean doisSobrenomes){
        String nome = null;

        // Pega o prenome
        int i = rdm.nextInt(preNomeMasc.length);
        nome = preNomeMasc[i];

         if (nomeComposto){
            i = rdm.nextInt(preNomeMasc.length);
            // Pega o segundo nome e testa para que ele
            // n�o seja igual ao primeiro
            while(nome.equals(preNomeMasc[i])){
                i = rdm.nextInt(preNomeMasc.length);   // pega uma nova posi��o para teste
            }
            nome = nome + " " + preNomeMasc[i];
        }

        if (doisSobrenomes){
            // Coloca uma preposi��o ou n�o para o primeiro sobrenome
            i = rdm.nextInt(preposicao.length);
            nome = nome + preposicao[i];

            // pega o primeiro sobrenome
            i = rdm.nextInt(sobreNomes.length);
            nome = nome + sobreNomes[i];
        }
        // Coloca uma preposi��o ou n�o para o �ltimo sobrenome
        i = rdm.nextInt(preposicao.length);
        nome = nome + preposicao[i];

        // pega o �ltimo sobrenome
        i = rdm.nextInt(sobreNomes.length);
        nome = nome + sobreNomes[i];

        return nome;
    }
}
