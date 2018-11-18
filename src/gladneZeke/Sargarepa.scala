package gladneZeke

import scala.util.Random
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import java.io.File

//40x45
class Sargarepa extends Thread {
	var rand = new Random;
	var x = rand.nextInt(650);
	var y = rand.nextInt(450);
	var slika = new Image("slike"+File.separator+"malaSargarepa.png");
	var live = false;
	var rb = 0;
	override  def run: Unit = {
			while(true){

				try{
					Thread.sleep(1000*(rb+1)); 
					if(live==false && Pocetna.krajIgre==false && Pocetna.sargarepa.selected.value){
						x = rand.nextInt(650);
						y = rand.nextInt(450);
					    spawn();
						
					}
					
				}
				catch{
				case _: Throwable => println("Sargarepa :: IZUZETAK!!!")
				}
			}
	};
	def draw(gcX:GraphicsContext): Unit ={
			if(live)
			    gcX.drawImage(slika,x,y);
	};
	def spawn(): Unit ={


			var i = 0;
			// proveravamo da li na odabranom mjestu postoji vec neka sargarepa
			while (i<Pocetna.brSargarepa){
				var xi= (Pocetna.sargarepaNiz)(i).x;
				var yi= (Pocetna.sargarepaNiz)(i).y;

				//da li se preklapa sa nekom vidljivom sargarepom
				if((rb!=i) && ((Pocetna.sargarepaNiz)(i).live)){
					if((x>(xi-50)) && (x<(xi+50))){
						if((y>(yi-50)) && (y<(yi+50))){
							x = rand.nextInt(650);
							y = rand.nextInt(450);

							//proveravamo ispocetka 
							i=0;
						}   
					}
				} 

				// ne poklapa se sa ovom, proveravamo za sledecu
				i+=1;
			}

			if(Slon.poklapanje(x, y, 40, 45, 10) == false){
				live=true; // ne poklapaju se
				// ne treba da pozivamo spawn jer ce ga run svakako pozvati i odredice nove koordinate ako ove ne odgovaraju
			}

	}


}