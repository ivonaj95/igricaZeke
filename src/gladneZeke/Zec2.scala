package gladneZeke

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import java.io.File
//44x45
object Zec2 extends Thread {
	var x = 10;
	var y = 70;
	var pojeden = false;
	var slika1 = new Image("slike"+File.separator+"zec2l.png");
	var slika2 = new Image("slike"+File.separator+"zec2d.png");
	var slika = slika2;





	override  def run: Unit = {
			while (true){
				try{
					if(Pocetna.krajIgre==false){
						move();
					}

					Thread.sleep(10);

				}

				catch{
				case _: Throwable => println("Zec2 :: IZUZETAK!!! ")
				}

			};
	};

	def draw(gcX:GraphicsContext): Unit ={
			if(pojeden==false)
				gcX.drawImage(slika,x,y);
	};


	def move(): Unit ={
			if(Pocetna.levo2) {
				slika=slika1;
				if(x>4)
					x -= 1;
			}
			if(Pocetna.desno2){
				slika=slika2;
				if(x<650)
					x += 1;
			}
			if(Pocetna.gore2) {
				if(y>4)
					y -= 1;
			}
			if(Pocetna.dole2){
				if(y<450)
					y += 1;
			}

			if(Pocetna.skoci2){
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


				Pocetna.skoci2=false;
			}




	};

	//slike zeka i sargarepa, odnosno kupus su slicne velicine pa funkcija eat moze ovako da se definise


	def eat2(): Unit ={

			var i = 0;
			while (i<Pocetna.brKupus){
				if((Pocetna.kupusNiz)(i).live){

					var px = (Pocetna.kupusNiz)(i).x;
					var py = (Pocetna.kupusNiz)(i).y;

					if((x > px-15) && (x < px +50 - 44 +15))
						if((y > py-15) && (y < py + 37 - 45 + 15)){


							(Pocetna.kupusNiz)(i).live = false;
							Pocetna.bodovi2 += 50;
							if(Pocetna.ukljucenZvuk)
								Pocetna.eatMP.play();

						}
				}
				i+=1;
			}

	};





}