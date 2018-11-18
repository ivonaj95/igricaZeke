package gladneZeke

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.canvas._
import scalafx.scene.control.Button
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.image.Image
import scalafx.scene.control.RadioButton
import scalafx.scene.control.ToggleGroup
import java.io.File
import scalafx.scene.Cursor
import scalafx.scene.control.Label
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.scene.control.TextField
import scalafx.animation.AnimationTimer
import scala.collection.immutable.ListMap
import java.io.PrintWriter
import scalafx.scene.control.TextArea
import scalafx.scene.media.MediaPlayer
import scalafx.scene.media.Media
import scalafx.util.Duration
import java.io.BufferedWriter
import java.io.FileWriter
import scalafx.geometry.Insets
import scalafx.scene.layout.Background



object Pocetna extends JFXApp {
	var krajIgre=false;
	val sargarepa = new RadioButton(" Sargarepa ");
	val kupus = new RadioButton("   Kupus   ");
	// indikatori da igrac prvi put zapocinje igru
	var prvaIgraS = true;
	var prvaIgraK = true;
	var timerS:AnimationTimer = _;
	var timerK:AnimationTimer = _;
	var igrac = new String();
	var igrac1 = new String();
	var igrac2 = new String();
	var bodoviS = 0;
	var bodovi2 = 0;
	//deklarisanje zvuka
	var media = new Media(new File("src"+File.separator+"muzika"+File.separator+"uvodna.mp3").toURI().toString());
	var mediaPlayer = new MediaPlayer(media);
	var izgubio = new Media(new File("src"+File.separator+"muzika"+File.separator+"izgubio.mp3").toURI().toString());
	var izgubioMP = new MediaPlayer(izgubio);
	var zavrseno = new Media(new File("src"+File.separator+"muzika"+File.separator+"izgubio (2).mp3").toURI().toString());
	var zavrsenoMP = new MediaPlayer(zavrseno);
	var eatZ = new Media(new File("src"+File.separator+"muzika"+File.separator+"eat.mp3").toURI().toString());
	var eatMP = new MediaPlayer(eatZ);
	//kazemo da po zavrsetku melodije treba da se ugase
	var indZ=0; // indikator za zvuk(2 igraca)
	izgubioMP.setOnEndOfMedia(new Runnable(){
		def run:Unit={
				izgubioMP.stop; 
	}
	});
	zavrsenoMP.setOnEndOfMedia(new Runnable(){
		def run:Unit={
				zavrsenoMP.stop; 
	}
	});
	eatMP.setOnEndOfMedia(new Runnable(){
		def run:Unit={
				eatMP.stop; 
	}
	});
	//promjenjiva on/off
	var ukljucenZvuk = true;
	// pravac kretanja zeca
	var levo = false;
	var desno = false;
	var gore = false;
	var dole = false;
	var skoci = false;
	var teleport = false;
	// pravac kretanja zeca2
	var levo2 = false;
	var desno2 = false;
	var gore2 = false;
	var dole2 = false;
	var skoci2 = false;
	// glavna scena
	var pocetak = new Scene();
	//okvir za tunel
	var canvasTunel = new Canvas(700,500);
	var gcTunel = canvasTunel.graphicsContext2D;
	// okvir, olovka i tekst za upustva
	val canvasTekst = new Canvas(700, 500);
	var gcTekst = canvasTekst.graphicsContext2D;
	var tekstS = "\"SARGAREPA\" je igrica za jednog igraca.\n\n"
			tekstS += "Ovde zeka pokusava da skupi sto vise sargarepica, ali pri tome mora izbegavati ostale zivotinje.\n"
			tekstS += "Sve zivotinje su neprijatelji.\n"
			tekstS += "Slon je zivotinja koja se ne pomera, ali takodje je neprijatelj.\n\n"
			tekstS += "Zeka se krece strelicama (levo, desno, gore, dole), a skace na SPACE.\n"
			tekstS += "Takodje, moze da iskopa tunel na odredjenom mestu pritiskom na ALT.\n"
			tekstS += "Kada se sledeci put pritisne ALT, vraca se (kroz tunel) na vec obelezeno mesto.\n"
			tekstS += "Nakon toga taj tunel nestaje, ali moze da napravi drugi tunel na isti nacin.\n\n"
			tekstS += "Sto vise sargarepica skupite, dobicete vise poena.\n\n"
			tekstS += "Na kraju igre mozete pogledati listu najboljih 10 igraca.\n\n"
			tekstS += "Zvuk, ako zelite, mozete da iskljucite pritiskom na dugme u gornjem, levom uglu.\n"
			tekstS += "SRECNO!!!";
	var tekstK = "\"KUPUS\" je igrica za dva igraca.\n\n"
			tekstK += "Svaki zeka pokusava da skupi sto vise kupusa, ali pri tome mora izbegavati ostale zivotinje.\n"
			tekstK += "Sve zivotinje su neprijatelji.\n"
			tekstK += "Slon je zivotinja koja se ne pomera, ali takodje je neprijatelj.\n\n"
			tekstK += "Prvi igrac se krece strelicama (levo, desno, gore, dole), a skace na 0.\n"
			tekstK += "Drugi igrac se krece slovima A-levo, W-gore, D-desno, S-dole, a skace na SPACE.\n\n"
			tekstK += "Cilj je skupiti 2500 poena pre protivnika.\n"
			tekstK += "Igra se zavrsava kada izgube oba igraca ili kada jedan od njih sakupi 2500 poena.\n\n"
			tekstK += "Zvuk, ako zelite, mozete da iskljucite pritiskom na dugme u gornjem, levom uglu.\n"
			tekstK += "SRECNO!!!";
	// za ispis poena
	var canvasPoeni = new Canvas(700, 500);
	var gcPoeni = canvasPoeni.graphicsContext2D;
	var sviIgraci = new TextArea;
	var sviBodovi = new TextArea;
	val rangB = new Button();
	// br_sargarepa i njihov niz
	var brSargarepa = 10;
	var sargarepaNiz = new Array[Sargarepa](brSargarepa);
	//br_kupus
	var brKupus = 10;
	var kupusNiz = new Array[Kupus](brKupus);

	// mapa za cuvanje rezultata
	var mapa = Map[String, Int]();
	val linije = scala.io.Source.fromFile("Rezultati.txt").getLines().toList;
	for(linija <- linije){
		val l = linija.split(" <==> ");
		mapa += ((l(0), (l(1).toInt)));
	};
	var sadrzaj = new String();
	var sadrzaj2 = new String();

	//definisanje bine
	var stejdz  = new PrimaryStage{

		//naslov
		title = "Gladne zeke";
		resizable = false; // da ne moze da se prosiri
		sizeToScene();


		//pocinje muzika
		mediaPlayer.play();
		mediaPlayer.setOnEndOfMedia(new Runnable(){
			def run:Unit={
					mediaPlayer.seek(Duration.ZERO); 
		}
		});


		// postavljanje scene na binu
		pocetak = new Scene(824,543) {

			// okvir za postavljanje slika 
			val canvas = new Canvas(824,543);

			// pozadina
			var gc = canvas.graphicsContext2D;
			gc.drawImage(new Image("slike"+File.separator+"suma.jpg"), 0, 0);

			//sunce
			gc.drawImage(new Image("slike"+File.separator+"sunce6.png"), 30, 20);

			// zeka
			gc.drawImage(new Image("slike"+File.separator+"zeka1.png"), 250, 310);

			// sargarepa
			gc.drawImage(new Image("slike"+File.separator+"sargarepa1.png"), 520, 80);

			// kupus
			gc.drawImage(new Image("slike"+File.separator+"kupus1.png"), 520, 200);


			// dugme za pocetak igre
			val b=new Button("  Igraj  ")
					b.layoutX = 30
					b.layoutY = 420
					b.prefHeight = 60
					b.cursor = Cursor.HAND
					b.style ="""-fx-background-color: 
					linear-gradient(#5ba4ff, #0d69b0),
					linear-gradient(#84e9ff, #4475f2),
					linear-gradient(#6ae1ff, #2274ef),
					linear-gradient(#57d2ff 0%, #029dff 50%, #0c75e0 100%),
					linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0)); 
					-fx-background-radius: 30;
					-fx-background-insets: 0,1,2,3,0;
					-fx-text-fill: #fff;
					-fx-font-weight: bold;
					-fx-font-size: 30;
					-fx-padding: 10 50 10 50;""";

			gc.drawImage(new Image("slike"+File.separator+"rsz_up.png"), 360, 285);
			//(upustva i pravila igre)
			val pdugme = new Button ("Pravila\n  igre")
					pdugme.layoutX = 415
					pdugme.layoutY = 275
					pdugme.cursor = Cursor.HAND
					pdugme.style = """-fx-background-color:
					linear-gradient(#411f00, #5a2b02),
					linear-gradient(#46311e, #4e412b),
					linear-gradient(#4b3c23, #4e370e),
					linear-gradient(#5a3b06 0%, #66450a 50%, #4e3d1e 100%),
					linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0)); 
					-fx-background-radius: 30;
					-fx-text-fill: #fff;
					-fx-font-weight: bold;
					-fx-font-size: 10;
					-fx-padding: 10 20 10 20;""";

			//dugme za ukljucivanje/iskljucivanje zvuka
			var zvuk = new Button ()
					zvuk.layoutX = 0
					zvuk.layoutY = 0
					zvuk.cursor = Cursor.HAND
					zvuk.prefHeight = 30
					zvuk.prefWidth = 30
					zvuk.style = """-fx-background-image: url('/slike/on.png'); 
					-fx-background-position : center; 
					-fx-background-radius: 30;""";

			// dugme za odabir igrice sa sargarepom
			sargarepa.cursor = Cursor.HAND
					sargarepa.layoutX = 635
					sargarepa.layoutY = 140
					sargarepa.prefWidth = 160
					sargarepa.selected = true
					sargarepa.style = """-fx-font-size: 20;-fx-font-weight: bold;-fx-text-fill: #fff;
					-fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 ); 
					-fx-background-color:
					linear-gradient(#EE7621, #FF8C00),
					linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));
					-fx-padding: 4 8 4 8;					
					-fx-background-radius: 30;""";


			//  dugme za odabir igrice sa kupusom
			kupus.cursor = Cursor.HAND
					kupus.layoutX = 635
					kupus.layoutY = 220
					kupus.selected = false
					kupus.style = """-fx-font-size: 20;-fx-font-weight: bold;-fx-text-fill: #fff;
					-fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );
					-fx-background-color:
					linear-gradient(#32CD32, #2E8B57),
					linear-gradient( #2E8B57, #32CD32),
					linear-gradient(#32cd32 0%, #2e8b57 50%, #32cd32 100%),
					linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0)); 
					-fx-padding: 4 8 4 8;					
					-fx-background-radius: 30;""";

			// ovo sluzi da ne bi istovremeno obe igre bile odabrane
			val togGrupa = new ToggleGroup;
			togGrupa.toggles= List(kupus, sargarepa);

			//polje gde igrac unosi svoje ime
			var labela = new Label(" ime: ")
					labela.layoutX = 480
					labela.layoutY = 425
					labela.style="""-fx-text-fill: #fff;
					-fx-font-weight: bold;
					-fx-font-size: 20;""";
			var labela1 = new Label(" igrac1: ")
					labela1.layoutX = 450
					labela1.layoutY = 400
					labela1.style="""-fx-text-fill: #fff;
					-fx-font-weight: bold;
					-fx-font-size: 20;""";
			var labela2 = new Label(" igrac2: ")
					labela2.layoutX = 450
					labela2.layoutY = 450
					labela2.style="""-fx-text-fill: #fff;
					-fx-font-weight: bold;
					-fx-font-size: 20;""";
			var imeI = new TextField()
					imeI.prefWidth = 200
					imeI.prefHeight = 30
					imeI.layoutX = 550
					imeI.layoutY = 425
					imeI.editable = true
					imeI.style="""-fx-background-color:
					linear-gradient(#EE7621, #FF8C00);
					-fx-background-radius: 30;
					-fx-text-fill: #fff;
					-fx-font-weight: bold;
					-fx-padding: 6 20 6 20;
					-fx-font-size: 20px;""";
			var ime1 = new TextField()
					ime1.prefWidth = 200
					ime1.prefHeight = 30
					ime1.layoutX = 550
					ime1.layoutY = 400
					ime1.editable = true
					ime1.style="""-fx-background-color:
					linear-gradient(#32CD32, #2E8B57),
					linear-gradient( #2E8B57, #32CD32);
					-fx-background-radius: 30;
					-fx-text-fill: #fff;
					-fx-padding: 6 20 6 20;
					-fx-font-weight: bold;
					-fx-font-size: 20;""";

			var ime2 = new TextField()
					ime2.prefWidth = 200
					ime2.prefHeight = 30
					ime2.layoutX = 550
					ime2.layoutY = 450
					ime2.editable = true
					ime2.style="""-fx-background-color:
					linear-gradient(#32CD32, #2E8B57),
					linear-gradient( #2E8B57, #32CD32);
					-fx-background-radius: 30;
					-fx-padding: 6 20 6 20;
					-fx-text-fill: #fff;
					-fx-font-weight: bold;
					-fx-font-size: 20px;""";


			// slike zeka ako su u pitanju dva igraca
			val canvasZeke12 = new Canvas(70, 110);
			canvasZeke12.layoutX = 750;
			canvasZeke12.layoutY = 398;
			var gcZeke12 = canvasZeke12.graphicsContext2D;
			gcZeke12.drawImage(new Image("slike"+File.separator+"zekaMali.png"), 10, 0);
			gcZeke12.drawImage(new Image("slike"+File.separator+"zec2l.png"), 10, 50);

			// dodavanje svih objekata u sadrzaj scene
			content = List(canvas, b, sargarepa, kupus, pdugme, zvuk, imeI, labela);
			sargarepa.onAction = (e: ActionEvent) => {
				content = List(canvas, b, sargarepa, kupus, pdugme, zvuk, imeI, labela);
				gcTekst.clearRect(0, 0, 700, 500);
				gcTekst.fillText(tekstS, 20, 50);
			};		
			kupus.onAction = (e: ActionEvent) => {
				content = List(canvas, b, sargarepa, kupus, pdugme, zvuk, ime1, ime2, labela1, labela2, canvasZeke12);
				gcTekst.clearRect(0, 0, 700, 500);
				gcTekst.fillText(tekstK, 20, 50);
			};


			//prikaz pravila igre
			pdugme.onAction = (e: ActionEvent) => {
				mediaPlayer.pause();
				scene = scenaU;
			};

			zvuk.onAction = (e: ActionEvent) => {
				if(ukljucenZvuk){
					mediaPlayer.pause();
					zvuk.style = """-fx-background-image: url('/slike/off.png'); -fx-background-radius: 30; -fx-background-position : center; """;
					ukljucenZvuk = false;
				} 
				else{
					mediaPlayer.play();
					zvuk.style = """-fx-background-image: url('/slike/on.png'); -fx-background-radius: 30; -fx-background-position : center; """;
					ukljucenZvuk = true;
				}
			};

			//pritiskom na dugme "Igraj pocinje igrica
			b.onAction = (e: ActionEvent) => {
				mediaPlayer.pause();
				krajIgre=false;
				Zec.pojeden = false; // ako pocinjemo drugu igru (da zac ponovo ozivi)

				// da ne izbacuje izuzetak ako neko prvo igra prvu, pa tek onda drugu igricu (ne mozemo vise puta pozvati start metod za isti objekat)
				if(prvaIgraS && prvaIgraK){
					Zec.start;
					Tigar.start;
					Vuk.start;
					Slon.start;
					Lisica.start;
				}
				if (sargarepa.selected.value )	{
					igrac = imeI.getText;
					if(prvaIgraS){

						var i = 0;
						while (i<brSargarepa){
							sargarepaNiz(i) = new Sargarepa();
							(sargarepaNiz(i)).rb = i;
							i = i+1;
						}

						i = 0;
						while (i<brSargarepa){
							(sargarepaNiz(i)).start;
							i= i+1;
						}
						prvaIgraS=false;
					}

					timerS.start;

					scene=scenaS;
				}	
				else if (kupus.selected.value )	{
					igrac1=ime1.getText;
					igrac2=ime2.getText;
					Zec2.pojeden=false;

					if(prvaIgraK){
						Zec2.start;
						var i = 0;
						while (i<brKupus){
							kupusNiz(i) = new Kupus();
							(kupusNiz(i)).rb = i;
							i = i+1;
						}

						i = 0;
						while (i<brKupus){
							(kupusNiz(i)).start;
							i= i+1;
						}
						prvaIgraK=false;
					}
					indZ=0; // za zvuk indikator
					timerK.start;

					scene=scenaK;
				}	
			};

			var scenaS = new Scene(700, 500){
				val canvasS = new Canvas(700,500);

				timerS = AnimationTimer(t=>{
					gc = canvasS.graphicsContext2D;
					gc.clearRect(0,0,700,500);
					gc.drawImage(new Image("slike"+File.separator+"trava.jpg"), 0, 0);


					Zec.draw(gc);
					Zec.eat1();
					Tigar.draw(gc);
					Tigar.draw2(gc);

					Vuk.draw(gc);
					Lisica.draw1(gc)
					Vuk.draw2(gc);
					Slon.draw(gc);

					var i = 0;
					while (i<brSargarepa){
						sargarepaNiz(i).draw(gc);
						i+=1;
					}

					gc.setFill(Color.WHITE);
					gc.font = Font.font(20);
					gc.fillText(igrac+" : "+bodoviS, 10, 480);

					if(Zec.pojeden){
						if(ukljucenZvuk)
							zavrsenoMP.play();
						krajIgre=true;
						var j = 0;
						while (j<brSargarepa){
							sargarepaNiz(j).live=false;
							j+=1;
						}
						levo = false;
						desno = false;
						gore = false;
						dole = false;
						teleport = false;
						Zec.indikator = false;
						gcTunel.clearRect(0, 0, 700, 500);

						//dodavanje rezultata u mapu i sortiranje
						mapa += ((igrac, bodoviS));
						mapa = ListMap(mapa.toSeq.sortWith(_._2 > _._2):_*);

						// sadrzaj rang liste (ispisujemo samo prvih 10 igraca), a sve igrace upisujemo u datoteku
						var i = 1;
						// da bude lepo poravnato
						val padding = "%1$-10s %2$-20s";
						val padding2 = "%1$15s"; 
						sadrzaj = padding.format("rb.", "ime");
						sadrzaj2 = padding2.format("bodovi");
						sadrzaj2 += "\n-------------------\n";
						sadrzaj += "\n------------------------\n";


						val pw = new PrintWriter("Rezultati.txt");	

						for(((k, v)) <- mapa){
							pw.println(k+" <==> "+v);
							if(i<=10){
								sadrzaj += padding.format(i, k);
								sadrzaj += "\n";
								sadrzaj2 += padding2.format(v);
								sadrzaj2 += "\n";
								i+=1;
							}

						}
						pw.close();	

						// na krajnju scenu ispisati ovo i dugme za prikaz rang liste postane vidljivo
						gcPoeni.clearRect(0, 0, 700, 500);
						gcPoeni.setFill(Color.WHITE);
						gcPoeni.font = Font.font(20);
						rangB.visible = true;
						rangB.disable = false;
						gcPoeni.fillText(igrac+",\nOsvojili ste "+bodoviS+" poena.", 30, 30);
						sviIgraci.text = sadrzaj;
						sviBodovi.text=sadrzaj2;

						//prelazak na krajnju scenu
						scene=krajS;

						timerS.stop;

					}


				});



				content =List(canvasS, canvasTunel);
				if(Zec.pojeden==false)	{
					onKeyPressed = (e: KeyEvent) => {
						if (e.code == KeyCode.LEFT) levo = true; 
						if (e.code == KeyCode.RIGHT) desno = true;
						if (e.code == KeyCode.UP) gore = true;
						if (e.code == KeyCode.DOWN) dole = true;
						if (e.code == KeyCode.SPACE) skoci = true;
						if (e.code == KeyCode.ALT) teleport = true;	
						if (e.code == KeyCode.ALT_GRAPH) teleport = true;	


					}
					onKeyReleased = (e: KeyEvent) => {
						if (e.code == KeyCode.LEFT) levo = false;
						if (e.code == KeyCode.RIGHT) desno = false;
						if (e.code == KeyCode.UP) gore = false;
						if (e.code == KeyCode.DOWN) dole = false;
						if (e.code == KeyCode.ALT) teleport = false;
						if (e.code == KeyCode.ALT_GRAPH) teleport = false;

					}
				}
			};

			var scenaK = new Scene(700, 500){

				val canvasK = new Canvas(700,500);

				timerK = AnimationTimer(t=>{
					gc = canvasK.graphicsContext2D;
					gc.clearRect(0,0,700,500);
					gc.drawImage(new Image("slike"+File.separator+"trava.jpg"), 0, 0);

					Zec.draw(gc);
					Zec2.draw(gc);
					Zec.eat2();
					Zec2.eat2();
					Tigar.draw(gc);
					Tigar.draw2(gc);
					Vuk.draw(gc);
					Lisica.draw1(gc)
					Vuk.draw2(gc);
					Slon.draw(gc);

					var i = 0;
					while (i<brKupus){
						kupusNiz(i).draw(gc);
						i+=1;
					}

					gc.setFill(Color.WHITE);
					gc.font = Font.font(15);
					gc.fillText(igrac1+" : "+bodoviS, 10, 460);
					gc.fillText(igrac2+" : "+bodovi2, 10, 480);



					if((Zec.pojeden && Zec2.pojeden) || bodoviS == 50*50 || bodovi2 == 50*50){
						if(ukljucenZvuk)
							zavrsenoMP.play();
						krajIgre=true;
						var j = 0;
						while (j<brKupus){
							kupusNiz(j).live=false;
							j+=1;
						}
						levo = false;
						desno = false;
						gore = false;
						dole = false;
						levo2 = false;
						desno2 = false;
						gore2 = false;
						dole2 = false;

						// postavljanje teksta na zadnju scenu i dugme za prikaz rang liste izbacujemo, jer je ovde nepotrebno
						gcPoeni.clearRect(0, 0, 700, 500);
						gcPoeni.setFill(Color.WHITE);
						gcPoeni.font = Font.font(20);
						rangB.visible = false;
						rangB.disable = true;
						gcPoeni.fillText(igrac1+",\nOsvojili ste "+bodoviS+" poena.\n", 30, 30);
						gcPoeni.fillText(igrac2+",\nOsvojili ste "+bodovi2+" poena.\n", 30, 100);
						gcPoeni.setFill(Color.RED);
						gcPoeni.font = Font.font(40);
						if(bodoviS==bodovi2)
							gcPoeni.fillText("NERESENO!!!\n", 30, 200);
						else if(bodoviS>bodovi2)
							gcPoeni.fillText("Pobednik je "+igrac1+".\n", 30, 200);
						else
							gcPoeni.fillText("Pobednik je "+igrac2+".\n", 30, 200);
						sviIgraci.text = sadrzaj;
						sviBodovi.text=sadrzaj2;


						//prelazak na krajnju scenu
						scene=krajS;

						timerK.stop;

					}
					else if(Zec.pojeden || Zec2.pojeden){
						if(indZ==0 && ukljucenZvuk)
							izgubioMP.play();
						indZ = 1;
					}



				});



				content =List(canvasK);
				if(krajIgre == false && bodoviS<50*50 && bodovi2<50*50)	{
					onKeyPressed = (e: KeyEvent) => {
						if (e.code == KeyCode.LEFT) levo = true; 
						if (e.code == KeyCode.RIGHT) desno = true;
						if (e.code == KeyCode.UP) gore = true;
						if (e.code == KeyCode.DOWN) dole = true;
						if (e.code == KeyCode.NUMPAD0) skoci = true;
						if (e.code == KeyCode.A) levo2 = true; 
						if (e.code == KeyCode.D) desno2 = true;
						if (e.code == KeyCode.W) gore2 = true;
						if (e.code == KeyCode.S) dole2 = true;
						if (e.code == KeyCode.SPACE) skoci2 = true;



					}
					onKeyReleased = (e: KeyEvent) => {
						if (e.code == KeyCode.LEFT) levo = false;
						if (e.code == KeyCode.RIGHT) desno = false;
						if (e.code == KeyCode.UP) gore = false;
						if (e.code == KeyCode.DOWN) dole = false;
						if (e.code == KeyCode.A) levo2 = false;
						if (e.code == KeyCode.D) desno2 = false;
						if (e.code == KeyCode.W) gore2 = false;
						if (e.code == KeyCode.S) dole2 = false;


					}
				}


			};


			var scenaU = new Scene (700, 500){

				val canvasU = new Canvas(700,500);
				gc = canvasU.graphicsContext2D;
				// pozadina 
				gc.drawImage(new Image("slike"+File.separator+"trava.jpg"), 0, 0);


				gcTekst.setFont(Font.font(15));
				gcTekst.setFill(Color.WHITE);

				gcTekst.fillText(tekstS, 20, 50);


				//dugme za povratak na glavnu stranu
				val nazad = new Button("NAZAD")
						nazad.layoutX = 550
						nazad.layoutY = 400
						nazad.prefHeight = 50
						nazad.prefWidth = 120
						nazad.cursor = Cursor.HAND
						nazad.style ="""-fx-background-color: 
						linear-gradient(#9932cc, #bf3eff),
						linear-gradient( #b23aee, #9a32cd); 
						-fx-background-radius: 100;
						-fx-text-fill: #fff;
						-fx-font-weight: bold;
						-fx-padding: 10 20 10 20;					
						-fx-font-size: 20px;""";

				nazad.onAction = (e: ActionEvent) => {
					scene = pocetak;
					// ponovo pocinje zvuk (ako je bio ukljucen)
					if(ukljucenZvuk)
						mediaPlayer.play();
				};

				content = List(canvasU, canvasTekst, nazad);
			};



			// zavrsena igra    
			var krajS = new Scene (700, 500){

				var canvasKS = new Canvas(700, 500);

				var gcKS = canvasKS.graphicsContext2D;
				gcKS.drawImage(new Image("slike"+File.separator+"trava.jpg"), 0, 0);

				sviIgraci.background = Background.EMPTY
						sviIgraci.layoutX = 10
						sviIgraci.layoutY = 10
						sviIgraci.prefHeight = 405
						sviIgraci.prefWidth = 300
						sviIgraci.visible = false
						sviIgraci.editable = false
						sviIgraci.style="""
						-fx-font-weight: bold;
						-fx-font-size: 20px;""";

				sviBodovi.background = Background.EMPTY
						sviBodovi.layoutX=310
						sviBodovi.layoutY=10
						sviBodovi.prefHeight = 405
						sviBodovi.prefWidth = 250
						sviBodovi.visible = false
						sviBodovi.editable = false
						sviBodovi.style="""
						-fx-font-weight: bold;
						-fx-font-size: 20px;""";




				val igrajPonovo = new Button("Nova igra")
						igrajPonovo.layoutX = 30
						igrajPonovo.layoutY = 420
						igrajPonovo.prefHeight = 50
						igrajPonovo.cursor = Cursor.HAND
						igrajPonovo.style ="""-fx-background-color: 
						linear-gradient(#32CD32, #7CFC00),
						linear-gradient( #7CFC00, #ADFF2F),
						linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0)); 
						-fx-background-radius: 30;
						-fx-text-fill: #fff;
						-fx-font-weight: bold;
						-fx-font-size: 30px;
						-fx-padding: 10 50 10 50;""";
				val izadji = new Button("  KRAJ  ")
						izadji.layoutX = 450
						izadji.layoutY = 420
						izadji.prefHeight = 50
						izadji.cursor = Cursor.HAND
						izadji.style ="""-fx-background-color: 
						linear-gradient(#FF2400, #FA8072),
						linear-gradient( #FA8072, #FF8C69),
						linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0)); 
						-fx-background-radius: 30;
						-fx-text-fill: #fff;
						-fx-font-weight: bold;
						-fx-font-size: 30px;
						-fx-padding: 10 50 10 50;""";
				rangB.text = "RangLista"
						rangB.layoutX = 225
						rangB.layoutY = 300
						rangB.prefHeight = 50
						rangB.cursor = Cursor.HAND
						rangB.style ="""-fx-background-color: 
						linear-gradient(#ffff00, #eeee00),
						linear-gradient( #cc3299, #9f5f9f);
						-fx-background-radius: 30;
						-fx-text-fill: #fff;
						-fx-font-weight: bold;
						-fx-font-size: 30px;
						-fx-padding: 10 50 10 50;""";

				igrajPonovo.onAction = (e: ActionEvent) => {
					sviIgraci.visible = false;
					sviBodovi.visible = false;
					Zec.x=10;
					Zec.y=10;
					Zec2.x=10;
					Zec2.y=70;
					imeI.clear();
					ime1.clear();
					ime2.clear();
					scene = pocetak;
					bodoviS = 0;
					bodovi2 = 0;
					Tigar.brKoraka=0;
					Tigar.brKoraka1=0;
					Tigar.indP = true;
					Tigar.indP1 = true;
					Tigar.parametar = 1;
					Tigar.parametar1 = 2;


					if(ukljucenZvuk){
						mediaPlayer.play();

					}

				};
				izadji.onAction = (e: ActionEvent) => {
					System.exit(0);
				};
				rangB.onAction = (e: ActionEvent) => {
					sviIgraci.visible = true;
					sviBodovi.visible = true;
				};

				content = List(canvasKS, canvasPoeni, igrajPonovo, izadji, rangB, sviIgraci, sviBodovi);

			};
		}

		// pocetak postaje glavna scena
		scene = pocetak;
	}    

	// postavljanje bine na odgovarajuci polozaj
	stejdz.onCloseRequest = handle {System.exit(0)};

	// prikazivanje bine
	stage = stejdz;

	
}			   
