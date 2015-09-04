
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
                "Abigail",	"Acácia",	"Ada",          "Adalgisa",     "Adelaide",     "Adele",	"Adélia",
                "Adelina",	"Adelinda",	"Adonirã",      "Adorinda",     "Adosinda",     "Adriana",	"Agnes",
                "Agnete",	"Agostinha",	"Aida",         "Alaíde",       "Alana",        "Alanis",	"Alba",
                "Alberta",	"Albertina",	"Albina",       "Alcina",       "Alcione",      "Alda",         "Aldara",
                "Aldina",	"Alessandra",	"Alexandra",	"Alexandrina",	"Alice",	"Alícia",	"Aline",
                "Alison",	"Alma",         "Altina",	"Alva",         "Alzira",	"Amália",	"Amanda",
                "Amélia",	"América",	"Ana",          "Anabela",	"Anaísa",	"Anastácia",	"Andrea",
                "Andreia",	"Andréia",	"Andressa",	"Andreza",	"Angela",	"Ângela",	"Angélica",
                "Angelina",	"Ania",         "Anita",	"Anna",         "Anne",         "Anny",         "Antónia",
                "Antônia",	"Antonieta",	"Aparecida",	"Apolónia",	"Ariana",	"Ariane",	"Ariela",
                "Arlete",	"Armanda",	"Arminda",	"Assunção",	"Assunta",	"Ataliba",	"Augusta",
                "Aura",         "Áurea",	"Aurélia",	"Auria",	"Aurora",	"Avelina",	"Azaléia",
                "Bárbara",	"Batista",	"Beatriz",	"Bebiana",	"Bela",         "Belarmina",	"Belinda",
                "Belisa",	"Belmira",	"Benedita",	"Benvinda",	"Berenice",	"Bernadete",	"Bernarda",
                "Bernardete",	"Berta",	"Betânia",	"Betina",	"Bia",          "Bianca",	"Biatriz",
                "Blanca",	"Blenda",	"Branca",	"Brenda",	"Brigida",	"Brígida",	"Brigite",
                "Brisa",	"Bruna",	"Brunna",	"Cacilda",	"Caetana",	"Caiane",	"Camila",
                "Camile",	"Cândida",	"Capitolina",	"Carina",	"Carine",	"Cariny",	"Carla",
                "Carlota",	"Carmelinda",	"Carmem",	"Carmen",	"Carminda",	"Carmo",	"Carol",
                "Carolina",	"Caroline",	"Cassandra",	"Cássia",	"Cassilda",	"Catarina",	"Cátia",
                "Cecília",	"Celeste",	"Celestina",	"Célia",	"Celina",	"Cesária",	"Cibele",
                "Cibelli",	"Cidália",	"Cinara",	"Cinira",	"Cintia",	"Cíntia",	"Clara",
                "Clarissa",	"Clarisse",	"Cláudia",	"Cleide",	"Clélia",	"Clementina",	"Cléo",
                "Cleusa",	"Cloe",         "Clotilde",	"Conceição",	"Constança",	"Consuela",	"Cora",
                "Cordélia",	"Corina",	"Cornélia",	"Cremilda",	"Cremilde",	"Cristela",	"Cristiana",
                "Cristiane",	"Cristina",	"Custódia",	"Cynthia",	"Dagmar",	"Daiana",	"Daisy",
                "Dália",	"Dalila",	"Dalva",	"Damiana",	"Dana",         "Dânia",	"Daniela",
                "Daniele",	"Danielly",	"Dara",         "Darcy",	"Darlene",	"Dayane",	"Dayani",
                "Dayse",	"Débora",	"Déia",         "Dejanira",	"Delfina",	"Delores",	"Denise",
                "Deolinda",	"Deonilde",	"Désiree",	"Diana",	"Dina",         "Diná",         "Dinah",
                "Dinora",	"Dinorah",	"Dione",	"Dirce",	"Disa",         "Diva",         "Djalma",
                "Djamila",	"Dolores",	"Dora",         "Dores",	"Dória",	"Dóris",	"Dorisse",
                "Dorotéia",	"Duílha",	"Dulce",	"Dulcineia",	"Dúnia",	"Edelina",	"Edite",
                "Edith",	"Edna",         "Eduarda",	"Efigênia",	"Elaine",	"Elda",         "Elen",
                "Eleonora",	"Eli",          "Élia",         "Eliana",	"Elisa",	"Elisabete",	"Elisabeth",
                "Elizabete",	"Elizabeth",	"Eloisa",	"Elsa",         "Elvira",	"Elza", 	"Ema",
                "Emanuela",	"Emília",	"Emilly",	"Ercília",	"Erica",	"Érica",	"Ermelinda",
                "Ermengarda",	"Ermínia",	"Erotides",	"Esmeralda",	"Estefânia",	"Estela",	"Ester",
                "Estrela",	"Etelvina",	"Eugénia",	"Eulália",	"Eunice",	"Eustácia",	"Eva",
                "Evangelina",	"Evelina",	"Evelise",	"Evelyn",	"Evilyn",	"Fábia",	"Fabiana",
                "Fabíola",	"Fátima",	"Faustina",	"Fedra",	"Felicia",	"Felicidade",	"Felismina",
                "Ferdinanda",	"Fernanda",	"Filipa",	"Filomena",	"Firmina",	"Flávia",	"Flaviana",
                "Flor",         "Flôr",         "Flora",	"Florbela",	"Florência",	"Florinda",	"Francelina",
                "Francesca",	"Franciane",	"Francileia",	"Francisca",	"Fransy",	"Frederica",	"Fúlvia",
                "Gabriela",	"Gabrielly",	"Gedeanne",	"Geísa",	"Genaina",	"Genoveva",	"Georgina",
                "Geralda",	"Geraldina",	"Gerlane",	"Gertrudes",	"Gilberta",	"Gilda",	"Gilsiara",
                "Gina",         "Giovana",	"Gisela",	"Gisele",	"Giselly",	"Gizela",	"Glaucia",
                "Glena",	"Glenda",	"Glória",	"Gorete",	"Graça",	"Graciete",	"Graziela",
                "Grazielli",	"Greta",	"Guida",	"Guilhermina",	"Hagar",	"Hanna",	"Hebe",
                "Heide",	"Heidi",	"Helena",	"Helga",	"Hélia",	"Heloisa",	"Heloísa",
                "Henriqueta",	"Hercília",	"Hermínia",	"Heronilda",	"Hildegarda",	"Hirondina",	"Honória",
                "Hortência",	"Hortênsia",	"Iami",         "Ianara",	"Iara",         "Iarine",	"Iasmim",
                "Idalina",	"Idalinda",	"Ieda",     	"Ifigénia",	"Ilara",	"Ilda",         "Imaculada",
                "Índia",	"Indyara",	"Inês",         "Ingrid",	"Iolanda",	"Iracema",	"Iraci",
                "Irene",	"Irina",	"Iris",         "Íris",         "Irma",         "Isa",          "Isabel",
                "Isabela",	"Isabelle",	"Isabelly",	"Isadora",	"Isaura",	"Isilda",	"Isis",
                "Isolda",	"Isolina",	"Ivana",	"Ivani",	"Ivete",	"Ivone",	"Jacinta",
                "Jacy",         "Jade",         "Jamile",	"Janaína",	"Jandira",	"Janete",	"Jaqueline",
                "Jasmim",	"Jasmin",	"Jenifer",	"Jesabel",	"Jessé",	"Jessica",	"Jéssica",
                "Jesus",	"Joana",	"Joaquina",	"Jocasta",	"Jocelina",	"Jocosa",	"Joice",
                "Jolinda",	"Jonata",	"Josefa",	"Josefina",	"Josélia",	"Josisbel",	"Jouse",
                "Joyce",	"Juca",         "Judite",	"Judith",	"Júlia",	"Juliana",	"Juliane",
                "Julieta",	"Junia",	"Jussara",	"Justina",	"Juvelina",	"Kaê",          "Kalinne",
                "Kallyana",	"Karen",	"Karina",	"Karleay",	"Karol",	"Kassandra",	"Katia",
                "Katiane",	"Katiúccia",	"Kauana",	"Kayla",	"Kaylane",	"Keila",	"Kelly",
                "Késia",	"Kiara",	"Kira",         "Kirina",	"Laila",	"Laís",         "Laiza",
                "Lana",         "Lanai",	"Lanna",	"Lara",         "Larissa",	"Latipha",	"Laura",
                "Laurentina",	"Laurinda",	"Lavínia",	"Lea",          "Leandra",	"Leda",         "Léia",
                "Leila",	"Lena",         "Lenah",	"Leni",         "Leocádia",	"Leonor",	"Leopoldina",
                "Leticia",	"Letícia",	"Levina",	"Leydiane",	"Lia",          "Liah",         "Liana",
                "Liane",	"Libânia",	"Lidia",	"Lídia",	"Lidiane",	"Ligia",	"Lígia",
                "Lilia",	"Lília",	"Lilian",	"Liliana",	"Liliany",	"Lina",         "Linda",
                "Lindalva",	"Lindomar",	"Lis",          "Lisandra",	"Lissa",	"Lita",         "Lívia",
                "Liz",          "Loide",	"Lorena",	"Loreta",	"Lourdes",	"Louyse",	"Lua",
                "Luana",	"Luane",	"Luani",	"Luca",         "Lucas",	"Lúcia",	"Luciana",
                "Lucilene",	"Lucília",	"Lucimara",	"Lucimeire",	"Lucinda",	"Lucrécia",	"Lucy",
                "Lucyobello",	"Luisa",	"Luísa",	"Luly",         "Luma",         "Lurdes",	"Luzia",
                "Lyana",	"Mabel",	"Madalena",	"Mafalda",	"Magali",	"Magda",	"Maísa",
                "Maitê",	"Malvina",	"Manuela",	"Manuelina",	"Mara",         "Marcela",	"Marcelle",
                "Márcia",	"Marcilene",	"Margarete",	"Margarida",	"Maria",	"Mariah",	"Mariana",
                "Mariel",	"Marieta",	"Mariete",	"Marilena",	"Marília",	"Marina",	"Marinela",
                "Maris",	"Marisa",	"Marisela",	"Marisol",	"Maristela",	"Marlene",	"Marta",
                "Martina",	"Matilde",	"Maura",	"Maurícia",	"Mavilde",	"Máxima",	"Mayara",
                "Mayra",	"Maytê",	"Meire",	"Melania",	"Melanie",	"Melinda",	"Melissa",
                "Micaela",	"Micaelly",	"Micheli",	"Michelle",	"Mikaela",	"Milena",	"Milene",
                "Mileny",	"Mira",         "Miracema",	"Miranda",	"Miriam",	"Misael",	"Mónica",
                "Morgana",	"Muriel",	"Nádia",	"Nadina",	"Nadine",	"Naielly",	"Naila",
                "Nair",         "Nana",         "Nancy",	"Naomi",	"Nara",         "Natacha",	"Natália",
                "Natalina",	"Natércia",	"Nathally",	"Nathiele",	"Natividade",	"Nazaré",	"Neandro",
                "Nei",          "Neide",	"Neiva",	"Neli",         "Nélia",	"Nélida",	"Nelma",
                "Neusa",	"Neuza",	"Nicia",	"Nicole",	"Nicolie",	"Nicolle",	"Nicolli",
                "Nicolly",	"Nídia",	"Nilda",	"Nilsa",	"Nina",         "Nisa",         "Nivalda",
                "Nívea",	"Nivia",	"Nívia",	"Noélia",	"Noemi",	"Noemia",	"Noémia",
                "Norma",	"Núbia",	"Núria",	"Obede",	"Octávia",	"Odete",	"Odília",
                "Ofélia",	"Olga",         "Olímpia",	"Olinda",	"Olivia",	"Olívia",	"Ondina",
                "Orlanda",	"Otávia",	"Otília",	"Palmira",	"Paloma",	"Pamela",	"Paola",
                "Patrícia",	"Paula",	"Paulina",	"Pérola",	"Petra",	"Pietra",	"Pilar",
                "Plácida",	"Poliana",	"Pollyanna",	"Priscila",	"Próspera",	"Prudência",	"Quirina",
                "Quitéria",	"Rafaela",	"Raiane",	"Raica",	"Railma",	"Raimunda",	"Raissa",
                "Raquel",	"Ravena",	"Rayane",	"Rebeca",	"Rebecca",	"Regina",	"Renata",
                "Ricarda",	"Ricardina",	"Rita",         "Roberta",	"Romana",	"Rosa",         "Rosália",
                "Rosana",	"Rosângela",	"Rosária",	"Rosário",	"Rose",         "Rosi",         "Rosinha",
                "Rosy",         "Rúbia",	"Rubina",	"Rute",         "Sabina",	"Sabrina",	"Salema",
                "Salete",	"Salomé",	"Samanta",	"Samara",	"Sandra",	"Sara",         "Sarita",
                "Sasha",	"Saskya",	"Savina",	"Selma",	"Serena",	"Sheila",	"Shirley",
                "Silva",	"Silvana",	"Silvéria",	"Silvia",	"Sílvia",	"Simone",	"Sofia",
                "Sol",          "Solange",	"Sónia",	"Sophia",	"Soraia",	"Stela",	"Stephanie",
                "Suelen",	"Sueli",	"Suelly",	"Susana",	"Susete",	"Tabata",	"Tabita",
                "Taciana",	"Taís",         "Talita",	"Tamara",	"Tâmara",	"Tamires",	"Tánia",
                "Tânia",	"Tarcila",	"Tasha",	"Tássia",	"Tatiana",	"Tatiane",	"Telma",
                "Teodora",	"Teresa",	"Thaís",	"Thaísa",	"Thalita",	"Tharcila",	"Thays",
                "Umblina",	"Urraca",	"Ursula",	"Úrsula",	"Valentina",	"Valéria",	"Valesca",
                "Valquíria",	"Vanda",	"Vanessa",	"Vânia",	"Vanusa",	"Vera",         "Verena",
                "Veridiana",	"Verónica",	"Verônica",	"Vilma",	"Violante",	"Violeta",	"Virginia",
                "Virgínia",	"Virgolina",	"Vitória",	"Vivian",	"Viviana",	"Viviane",	"Wanessa",
                "Wilma",	"Xênia",	"Yara",         "Yasmin",	"Yasmine",	"Yolanda",	"Zaira"
    };

    private String preNomeMasc[] = {
                "Aarão",	"Abel",         "Abílio",	"Abraão",	"Acácio",	"Adalberto",	"Adão",
                "Adauto",	"Adelino",	"Adelmiro",	"Ademar",	"Ademir",	"Adérito",	"Ado",
                "Adolfo",	"Adosindo",	"Adriano",	"Adrião",	"Aécio",	"Afonso",	"Agostinho",
                "Aguinaldo",	"Aírton",	"Alan",         "Albano",	"Alberto",	"Albino",	"Alcir",
                "Aldino",	"Aldir",	"Aldo",         "Alencar",	"Alessandro",	"Alex", 	"Alexander",
                "Alexandre",	"Alfeu",	"Alfredo",	"Alírio",	"Altair",	"Altino",	"Álvaro",
                "Amadeu",	"Amado",	"Amador",	"Amândio",	"Amaro",	"Amável",	"Américo",
                "Amilcar",	"Anacleto",	"Anastácio",	"Anderson",	"André",	"Andrei",	"Andrew",
                "Andrey",	"Ângelo",	"Aníbal",	"Anselmo",	"Antenor",	"Antero",	"António",
                "Antônio",	"Apolinário",	"Apolo",	"Apólo",	"Aponino",	"Aquiles",	"Arcelino",
                "Argeu",	"Ari",          "Aristóteles",	"Arlindo",	"Armando",	"Arménio",	"Armindo",
                "Arnaldo",	"Arthur",	"Artur",	"Ary",          "Asdrúbal",	"Atanásio",	"Atílio",
                "Augusto",	"Aurélio",	"Avelino",	"Ayrton",	"Baltasar",	"Barnabé",	"Bartolo",
                "Bartolomeu",	"Basílio",	"Batista",	"Belarmino",	"Belmiro",	"Benedito",	"Benito",
                "Benjamim",	"Benjamin",	"Bento",	"Bernardo",	"Bonifácio",	"Brandon",	"Brás",
                "Bráulio",	"Brayan",	"Bren",         "Brian",	"Brígido",	"Brivaldo",	"Bruno",
                "Bryan",	"Caetano",	"Caim",         "Caio",         "Caíque",	"Calebe",	"Calisto",
                "Calvin",	"Calvino",	"Camilo",	"Cândido",	"Capitolino",	"Carlos",	"Carmelo",
                "Casimiro",	"Cássio",	"Cauê",         "Célio",	"Celso",	"César",	"Cesário",
                "Christian",	"Cícero",	"Cid",          "Ciro",         "Claudemir",	"Claudimir",	"Claudino",
                "Cláudio",	"Cléber",	"Clemente",	"Clementino",	"Clístenes",	"Clodoaldo",	"Clóvis",
                "Conrado",	"Constâncio",	"Constantino",	"Consuelo",	"Cornélio",	"Cosme",	"Cristhian",
                "Cristian",	"Cristiano",	"Cristóvão",	"Custódio",	"Dácio",	"Damião",	"Daniel",
                "Danilo",	"Dário",	"Davi",         "David",	"Delfim",	"Delfino",	"Diego",
                "Dinarte",	"Dinis",	"Diniz",	"Diógenes",	"Diogo",	"Dionísio",	"Domingos",
                "Donaldo",	"Donato",	"Douglas",	"Duarte",	"Dúlio",	"Durval",	"Edelmiro",
                "Edgar",	"Edgard",	"Ediberto",	"Edmar",	"Edmundo",	"Edson",	"Eduardo",
                "Egídio",	"Egon",         "Eleutério",	"Eli",          "Elian",	"Elias",	"Élio",
                "Eliseu",	"Elizeu",	"Elói",         "Eloy",         "Elton",	"Elvis",	"Emanuel",
                "Emerson",	"Emídio",	"Emilio",	"Emílio",	"Énio",         "Erasmo",	"Eric",
                "Erico",	"Ermínio",	"Ernane",	"Ernani",	"Ernesto",	"Esaú",         "Esdras",
                "Estevão",	"Eugênio",	"Eurico",	"Eusébio",	"Eutímio",	"Evaldo",	"Evandro",
                "Ezequias",	"Ezequiel",	"Fabiano",	"Fábio",	"Fabrício",	"Fagner",	"Faustino",
                "Fausto",	"Felício",	"Felipe",	"Felisberto",	"Félix",	"Ferdinando",	"Fernando",
                "Filipe",	"Firmino",	"Flaviano",	"Flávio",	"Florêncio",	"Florival",	"Francesco",
                "Francis",	"Francisco",	"Franklin",	"Frederico",	"Gabriel",	"Gaspar",	"Gastão",
                "George",	"Geraldino",	"Geraldo",	"Germano",	"Gerson",	"Gervásio",	"Getúlio",
                "Gianluca",	"Gianpietro",	"Gideão",	"Gil",          "Gilberto",	"Gildo",	"Giulio",
                "Glauco",	"Godofredo",	"Golias",	"Gonçalo",	"Gregório",	"Guido",	"Guilherme",
                "Guilhermino",	"Gustavo",	"Hamilton",	"Heitor",	"Helder",	"Hélder",	"Hélio",
                "Henrico",	"Henrique",	"Henry",	"Herculano",	"Hércules",	"Hermano",	"Hermes",
                "Hernâni",	"Herodes",	"Heyder",	"Higino",	"Hilário",	"Hildebrando",	"Hildefonso",
                "Hilton",	"Hipólito",	"Honório",	"Horácio",	"Hugo",         "Humberto",	"Ian",
                "Idalécio",	"Igo",          "Igor",         "Ilário",	"Ilídio",	"Inácio",	"Irineu",
                "Isaac",	"Isaias",	"Isaías",	"Ismael",	"Israel",	"Ítalo",	"Iuan",
                "Iuri",         "Ivã",          "Ivan",         "Ivo",          "Jacinto",	"Jacó",         "Jacob",
                "Jaime",	"Jairo",	"Januário",	"Jeferson",	"Jefferson",	"Jeremias",	"Jerônimo",
                "Jesse",	"Jesus",	"Jó",           "João",         "Joaquim",	"Joel",         "Jonas",
                "Jonatas",	"Jonathan",	"Jorge",	"Josafá",	"José",         "Joselindo",	"Josualdo",
                "Josué",	"Joviano",	"Judas",	"Juliano",	"Julio",	"Júlio",	"Julius",
                "Justino",	"Kailson",	"Kauã",         "Kauai",	"Kauê",         "Kelvin",	"Kenedy",
                "Kevin",	"Kilson",	"Kleber",	"Kleisom",	"Ladislau",	"Lancelote",	"Laurindo",
                "Lauro",	"Leandro",	"Leo",          "Leocádio",	"Leonardo",	"Leôncio",	"Leonel",
                "Leónidas",	"Leopoldo",	"Levi",         "Libânio",	"Licínio",	"Lino",         "Lisandro",
                "Lívio",	"Lorenzo",	"Lourenço",	"Lourival",	"Lucas",	"Luciano",	"Lúcio",
                "Ludovico",	"Luis",         "Luís",         "Luiz",         "Manuel",	"Marcelo",	"Marciano",
                "Márcio",	"Marco",	"Marcos",	"Marcus",	"Mariano",	"Mário",	"Marlon",
                "Martim",	"Martin",	"Martinho",	"Mateus",	"Matheus",	"Matias",	"Maurício",
                "Mauro",	"Maximiliano",	"Máximo",	"Meninos",	"Michael",	"Michel",	"Miguel",
                "Milo",         "Milton",	"Moacir",	"Moisés",	"Muriel",	"Murilo",	"Nabal",
                "Nadir",	"Narciso",	"Natálio",	"Natanael",	"Nataniel",	"Naum",         "Nazário",
                "Nélio",	"Nelson",	"Nélson",	"Nero",         "Nestor",	"Nico",         "Nicolas",
                "Nicolau",	"Nildo",	"Nilson",	"Nilton",	"Nivaldo",	"Norberto",	"Normando",
                "Nuno",         "Odair",	"Odécio",	"Odir",         "Odorico",	"Olavo",	"Olegário",
                "Olímpio",	"Olivier",	"Olívio",	"Omar",         "Onofre",	"Orfeu",	"Orlando",
                "Oscar",	"Óscar",	"Osias",	"Osmar",	"Osório",	"Osvaldo",	"Otávio",
                "Otoniel",	"Pablo",	"Pascoal",	"Patrício",	"Paulino",	"Paulo",	"Pedro",
                "Pelágio",	"Perceu",	"Péricles",	"Petrucio",	"Plácido",	"Plínio",	"Priscilo",
                "Querubim",	"Quintim",	"Quintino",	"Quirino",	"Quixote",	"Rafael",	"Raimundo",
                "Ramiro",	"Ramom",	"Ramon",	"Raphael",	"Raul",         "Raúl",         "Reginaldo",
                "Reinaldo",	"Renan",	"Renato",	"Ricardino",	"Ricardo",	"Roberto",	"Robson",
                "Rodolfo",	"Rodrigo",	"Rogério",	"Romeu",	"Romualdo",	"Rômulo",	"Ronaldo",
                "Ruben",	"Rúben",	"Rubens",	"Rudolf",	"Rui",          "Sabino",	"Salomão",
                "Salvador",	"Samuel",	"Sandro",	"Saul",         "Saúl",         "Sebastião",	"Serafim",
                "Sérgio",	"Sidney",	"Sidónio",	"Silas",	"Silvano",	"Silvério",	"Silvestre",
                "Silvino",	"Silvio",	"Sílvio",	"Simão",	"Sócrates",	"Tadeu",	"Tarcisio",
                "Tarcísio",	"Telmo",	"Téo",          "Teobaldo",	"Teodoro",	"Teófilo",	"Theo",
                "Tiago",	"Tibúrcio",	"Timóteo",	"Tito",         "Tomás",	"Tomé",         "Torquato",
                "Tristão",	"Túlio",	"Ubaldino",	"Ubiratã",	"Ulisses",	"Umbelino",	"Umberto",
                "Urbano",	"Urbino",	"Ursulino",	"Vagner",	"Valadares",	"Valber",	"Valdecir",
                "Valdeir",	"Valdemar",	"Valdemiro",	"Valdir",	"Valdo",	"Valentim",	"Valentino",
                "Valério",	"Valmir",	"Valter",	"Vanderlei",	"Vanderley",	"Vasco",	"Veloso",
                "Venâncio",	"Venceslau",	"Vicente",	"Victor",	"Vidigal",	"Vieira",	"Vilela",
                "Vilmar",	"Vinicios",	"Vinicius",	"Vinícius",	"Virgílio",	"Virgolino",	"Viriato",
                "Vital",	"Vitor",	"Vitorino",	"Vivaldo",	"Vladimir",	"Vlamir",	"Volney",
                "Wagner",	"Waldemar",	"Wallace",	"Walter",	"Wilian",	"Wilker",	"William",
                "Willian",	"Wilson",	"Wilton",	"Wladimir",	"Xavier",	"Yuri",         "Zacarias"
    };

    private String sobreNomes[] = {
                "Abreu",	"Acioli",	"Adams",	"Agostinho",	"Aguiar",	"Aires",	"Albino",
                "Alencar",	"Almeida",	"Alvarenga",	"Alves",	"Amadeu",	"Amado",	"Amaral",
                "Amaro",	"Ambrósio",	"Américo",	"Amora",	"Amorim",	"Andrada",	"Andrade",
                "Antunes",	"Aparicio",	"Aparício",	"Araujo",	"Araújo",	"Assis",	"Assunção",
                "Augusto",	"Auxiliadora",	"Ávila",	"Azeredo",	"Azevedo",	"Bacelar",	"Baltazar",
                "Bandeira",	"Barbosa",	"Barcellos",	"Barcelos",	"Barra",	"Barreto",	"Barros",
                "Barroso",	"Bastos",	"Batista",	"Belem",	"Benevides",	"Benvindo",	"Beretta",
                "Berger",	"Bergmann",	"Bertran",	"Bettencourt",	"Bezerra",	"Bicudo",	"Bittencourt",
                "Boaventura",	"Bona",         "Borba",	"Borges",	"Botelho",	"Braga",	"Branco",
                "Brandão",	"Brás",         "Brasil",	"Brito",	"Bueno",	"Cabral",	"Cachoeira",
                "Caldas",	"Calmon",	"Camargo",	"Campos",	"Canto",	"Cardoso",	"Carmo",
                "Carneiro",	"Carraro",	"Carvalho",	"Castanho",	"Castilho",	"Castro",	"Cavalcanti",
                "Cerqueira",	"Chagas",	"Chaves",	"Claudino",	"Clemente",	"Cleto",	"Coelho",
                "Coimbra",	"Conceição",	"Cordeiro",	"Cordova",	"Correa",	"Corrêa",	"Correia",
                "Costa",	"Costela",	"Coutinho",	"Cruz",         "Cunha",	"Custódia",	"Custódio",
                "Damann",	"Damásio",	"Dávila",	"Dias",         "Dinis",	"Domingues",	"Dórea",
                "Dória",	"Dorneles",	"Dorta",	"Duarte",	"Durante",	"Dutra",	"Espindola",
                "Espírito",	"Estrela",	"Fagundes",	"Faria",	"Farias",	"Feijó",	"Feliciano",
                "Fernandes",	"Ferreira",	"Ferretti",	"Figueira",	"Figueiredo",	"Filgueira",	"Flor",
                "Fonseca",	"Fortes",	"Fraga",	"Fragas",	"França",	"Freire",	"Freitas",
                "Frey",         "Furtado",	"Gadelha",	"Garcia",	"Gil",          "Gomes",	"Gonçalves",
                "Gonzalez",	"Goulart",	"Guedes",	"Guerra",	"Guimarães",	"Gurgel",	"Gusmão",
                "Hering",	"Holanda",	"Homem",	"Ibiapina",	"Inácio",	"Jesus",	"Jordão",
                "Ladeira",	"Laranja",	"Laranjeira",	"Lauben",	"Laurentino",	"Laurindo",	"Leal",
                "Leão",         "Leitão",	"Leite",	"Lemos",	"Levi",         "Levy",         "Lima",
                "Limas",	"Linhares",	"Lisboa",	"Liz",          "Lobato",	"Loiola",	"Lombardi",
                "Lopes",	"Lopez",	"Macedo",	"Macêdo",	"Machado",	"Maciel",	"Magalhães",
                "Maia",         "Malafaia",	"Malta",	"Maluf",	"Marcolla",	"Maria",	"Marques",
                "Marquez",	"Marschalk",	"Martinez",	"Martins",	"Mascarenhas",	"Mata",         "Matias",
                "Matos",	"Mazzotti",	"Medeiros",	"Medina",	"Meira",	"Meireles",	"Melo",
                "Mendel",	"Mendes",	"Mendonça",	"Menezes",	"Mesquita",	"Mestre",	"Miranda",
                "Molina",	"Monteiro",	"Montenegro",	"Moraes",	"Morais",	"Moreira",	"Moreno",
                "Mota",         "Moura",	"Mourão",	"Muniz",	"Nascimento",	"Navarro",	"Neri",
                "Neto",         "Neves",	"Nogueira",	"Noronha",	"Nunes",	"Oliveira",	"Onofre",
                "Ortiz",	"Osório",	"Otto",         "Pacheco",	"Padilha",	"Paiva",	"Parente",
                "Paulino",	"Paz",          "Peixe",	"Penha",	"Pereira",	"Peres",	"Pessoa",
                "Pimenta",	"Pimentel",	"Pina",         "Pinheiro",	"Pinho",	"Pinto",	"Pires",
                "Pontes",	"Prado",	"Prestes",	"Quadros",	"Queirós",	"Queiroz",	"Rabelo",
                "Ramalho",	"Ramos",	"Rebelo",	"Rego",         "Reis",         "Rengel",	"Resende",
                "Rezende",	"Ribeiro",	"Rios",         "Rocha",	"Rodrigues",	"Romão",	"Rosa",
                "Rossi",	"Sá",           "Sabino",	"Sales",	"Salgado",	"Salgueiro",	"Salim",
                "Salles",	"Salvador",	"Salvadori",	"Santa",	"Santiago",	"Santo",	"Santos",
                "Saraiva",	"Sardinha",	"Seixas",	"Selva",	"Serafim",	"Serra",	"Severino",
                "Severo",	"Silva",	"Silveira",	"Silvério",	"Simão",	"Simones",	"Simplício",
                "Siqueira",	"Sirino",	"Soares",	"Sousa",	"Souto",	"Souza",	"Spindola",
                "Tavares",	"Teixeira",	"Teles",	"Texeira",	"Toledo",	"Torres",	"Tourinho",
                "Trindade",	"Uchoa",	"Valadares",	"Vale",         "Valério",	"Vargas",	"Vasconcelos",
                "Vaz",          "Veber",	"Veiga",	"Velasco",	"Veloso",	"Ventura",	"Venturi",
                "Viana",	"Vicente",	"Vieira",	"Vilalobos",	"Walter",	"Werner",	"Xavier"
    };

    // As strings com espaço são pra evitar de ter sempre
    // uma preposição e já serve como espaço entre os nomes.
    private String preposicao[] = {
                " da ", " ", " ", " ", " " , " ",
                " de ", " ", " ", " ", " " , " ",
                " e ",  " ", " ", " ", " " , " ",
                //" ", "", "", "",
    };

    /**
     * Este método gera aleatoriamente um nome feminino contendo apenas
     * prenome e sobrenome.
     * @return nome simples
     */
    public String nomeFeminino(){
        String nome = null;


        // Pega o prenome
        int i = rdm.nextInt(preNomeFeminino.length);
        nome = preNomeFeminino[i];

        // Coloca uma preposição ou não
        i = rdm.nextInt(preposicao.length);
        nome = nome + preposicao[i];

        // pega o sobrenome
        i = rdm.nextInt(sobreNomes.length);
        nome = nome + sobreNomes[i];

        return nome;
    }

    /**
     * Cria um nome feminino onde é possível indicar se haverá nome composto
     * e se terá dois sobrenomes. Quando os parâmetros forem falsos, funcionará
     * igual ao método <i>nomeFeminino()</i>.
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
            // não seja igual ao primeiro
            while(nome.equals(preNomeFeminino[i])){
                i = rdm.nextInt(preNomeFeminino.length);   // pega uma nova posição para teste
            }
            nome = nome + " " + preNomeFeminino[i];
        }

        if (doisSobrenomes){
            // Coloca uma preposição ou não para o primeiro sobrenome
            i = rdm.nextInt(preposicao.length);
            nome = nome + preposicao[i];

            // pega o primeiro sobrenome
            i = rdm.nextInt(sobreNomes.length);
            nome = nome + sobreNomes[i];
        }
        // Coloca uma preposição ou não para o último sobrenome
        i = rdm.nextInt(preposicao.length);
        nome = nome + preposicao[i];

        // pega o último sobrenome
        i = rdm.nextInt(sobreNomes.length);
        nome = nome + sobreNomes[i];

        return nome;
    }

        /**
     * Este método gera aleatoriamente um nome feminino contendo apenas
     * prenome e sobrenome.
     * @return nome simples
     */
    public String nomeMasculino(){
        String nome = null;

        // Pega o prenome
        int i = rdm.nextInt(preNomeMasc.length);
        nome = preNomeMasc[i];

        // Coloca uma preposição ou não
        i = rdm.nextInt(preposicao.length);
        nome = nome + preposicao[i];

        // pega o sobrenome
        i = rdm.nextInt(sobreNomes.length);
        nome = nome + sobreNomes[i];

        return nome;
    }

    /**
     * Cria um nome feminino onde é possível indicar se haverá nome composto
     * e se terá dois sobrenomes. Quando os parâmetros forem falsos, funcionará
     * igual ao método <i>nomeFeminino()</i>.
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
            // não seja igual ao primeiro
            while(nome.equals(preNomeMasc[i])){
                i = rdm.nextInt(preNomeMasc.length);   // pega uma nova posição para teste
            }
            nome = nome + " " + preNomeMasc[i];
        }

        if (doisSobrenomes){
            // Coloca uma preposição ou não para o primeiro sobrenome
            i = rdm.nextInt(preposicao.length);
            nome = nome + preposicao[i];

            // pega o primeiro sobrenome
            i = rdm.nextInt(sobreNomes.length);
            nome = nome + sobreNomes[i];
        }
        // Coloca uma preposição ou não para o último sobrenome
        i = rdm.nextInt(preposicao.length);
        nome = nome + preposicao[i];

        // pega o último sobrenome
        i = rdm.nextInt(sobreNomes.length);
        nome = nome + sobreNomes[i];

        return nome;
    }
}
