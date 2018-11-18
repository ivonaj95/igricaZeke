package gladneZeke

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import java.io.File
//42x44
object Zec extends Thread {
	var x = 10;
	var y = 10;
	var pojeden = false;
	var slika = new Image("slike"+File.separator+"zekaMali.png");
	var slikaT = new Image("slike"+File.separator+"tunel.png");
	var indikator = false;
	var xt = 0;
	var yt = 0;



	override  def run: Unit = {
			while (true){
				try{
					if(Pocetna.krajIgre==false){
						move();
					}

					Thread.sleep(10);

				}

				catch{
				case _: Throwable => println("Zec :: IZUZETAK!!! ")
				}

			};
	};
	def teleport(): Unit ={
			if (indikator == false){
				xt = x;
				yt = y+25;
				indikator = true;
				drawT(Pocetna.gcTunel);
				Thread.sleep(300)
			}
			else{
				var pomx = xt;
				var pomy = yt;
				xt=x;
				yt=y+25;
				x = -100;
				y = -100;
				indikator = false;
				drawT(Pocetna.gcTunel);

				Thread.sleep(3000)
				x = pomx;
				y = pomy-25;
				Thread.sleep(300)
				(Pocetna.gcTunel).clearRect(x-20,y-20,80,80);


				(Pocetna.gcTunel).clearRect(xt-20,yt-20,80,80);


			}

	}
	def draw(gcX:GraphicsContext): Unit ={
			if(pojeden==false)
				gcX.drawImage(slika,x,y);
	};

	def drawT(gcX:GraphicsContext): Unit ={
			gcX.drawImage(slikaT,xt,yt);
	};
	def move(): Unit ={
			if(Pocetna.levo) {
				if(x>4)
					x -= 1;
			}
			if(Pocetna.desno){
				if(x<650)
					x += 1;
			}
			if(Pocetna.gore) {
				if(y>4)
					y -= 1;
			}
			if(Pocetna.dole){
				if(y<450)
					y += 1;
			}

			if(Pocetna.skoci){
				var i = 0;
				var pom = 5;
				var skok = 100;
				// pomeranje navise
				while((y-pom)>pom && i<(skok/pom)){
					i += 1;
					y -= pom;
					Thread.sleep(10);
				}
				//pomeranje nanize
				while (i>0) {
					i -= 1;
					y += pom;
					Thread.sleep(10);
				}


				Pocetna.skoci=false;
			}

			if(Pocetna.teleport){
				teleport();
			}


	};

	//slike zeka i sargarepa, odnosno kupus su slicne velicine pa funkcija eat moze ovako da se definise

	def eat1(): Unit ={

			var i = 0;
			while (i<Pocetna.brSargarepa){
				if((Pocetna.sargarepaNiz)(i).live){

					var px = x - (Pocetna.sargarepaNiz)(i).x;
					var py = y - (Pocetna.sargarepaNiz)(i).y;

					if(Math.abs(px)<=10 && Math.abs(py)<=10){

						(Pocetna.sargarepaNiz)(i).live = false;
						Pocetna.bodoviS += 50;
						if(Pocetna.ukljucenZvuk)
							Pocetna.eatMP.play();

					}
				}
				i+=1;
			}

	};

	def eat2(): Unit ={

			var i = 0;
			while (i<Pocetna.brKupus){
				if((Pocetna.kupusNiz)(i).live){

					var px = (Pocetna.kupusNiz)(i).x;
					var py = (Pocetna.kupusNiz)(i).y;

					if((x > px-15) && (x < px +50 - 42 +15))
						if((y > py-15) && (y < py + 37 - 44 + 15)){


							(Pocetna.kupusNiz)(i).live = false;
							Pocetna.bodoviS += 50;
							if(Pocetna.ukljucenZvuk)
								Pocetna.eatMP.play();
						}
				}
				i+=1;
			}

	};





}