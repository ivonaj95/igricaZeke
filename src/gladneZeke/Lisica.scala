package gladneZeke
import scala.util.Random
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import java.io.File

//75x73
object Lisica extends Thread {
	var x1 = 0;
	var y1 = 225;
	var slika1 = new Image("slike"+File.separator+"lisica1.png");
	var slika2 = new Image("slike"+File.separator+"lisica2.png");
	var slika = slika2;
	var ind1 = true;
	def move1(): Unit = {
			if(x1<630 && ind1 == true){
				x1 = x1+1;
				if(x1 == 630){
					ind1 = false;
					slika=slika1;
				}
			}
			if(x1>0 && ind1 == false){
				x1 = x1 - 1;
				if (x1 == 0){
					ind1 = true;
					slika=slika2;
				}
			}
	} 
	def draw1(gcX:GraphicsContext): Unit ={
			gcX.drawImage(slika,x1,y1);
	};
	override  def run: Unit ={
			while(true){
				try{
					if(Pocetna.krajIgre==false){
						move1();
						eat();
					}
					Thread.sleep(10);
				}
				catch{
				case _: Throwable => println("LISICA :: IZUZETAK!!! ")
				}
			}

	}
	def eat(): Unit ={
			if((Zec.x > x1-15) && (Zec.x < x1 +75 - 42 +15))
				if((Zec.y > y1-15) && (Zec.y < y1 + 73 - 44 + 15))
					Zec.pojeden=true;
			if((Zec2.x > x1-15) && (Zec2.x < x1 +75 - 42 +15))
				if((Zec2.y > y1-15) && (Zec2.y < y1 + 73 - 44 + 15))
					Zec2.pojeden=true;
	};
}
