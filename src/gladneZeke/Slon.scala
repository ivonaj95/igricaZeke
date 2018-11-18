package gladneZeke
import scala.util.Random
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import java.io.File
//100x93
object Slon extends Thread{
	var x = 300;
	var y = 200;
	var slika = new Image("slike"+File.separator+"slon.png");

	def draw(gcX:GraphicsContext): Unit ={
			gcX.drawImage(slika,x,y);
	};
	override  def run: Unit ={
			while(true){
				try{
					if(Pocetna.krajIgre==false){
						eat();
					}
					Thread.sleep(10);
				}
				catch{
				case _: Throwable => println("SLON :: IZUZETAK!!! ")
				}
			}
	}
	def eat(): Unit ={
			
			if((Zec.x > x-15) && (Zec.x < x +100 - 42 +15))
				if((Zec.y > y-15) && (Zec.y < y + 93 - 44 + 15))
					Zec.pojeden=true;
			if((Zec2.x > x-15) && (Zec2.x < x +100 - 42 +15))
				if((Zec2.y > y-15) && (Zec2.y < y + 93 - 44 + 15))
					Zec2.pojeden=true;
	};
	
	def poklapanje(x1:Int, y1:Int, w1:Int, h1:Int, tol:Int) : Boolean = {
			var sirina = false;
			var visina = false;
			var w2=100;
			var h2=93;
				// prosledjuje se prvo kraca
				if(poklapanjeDuzi(x1, w1, x, w2, tol))
					sirina = true;

			// proveravamo da li se i visina poklapa ali samo ako se duzina vec poklopila
			if (sirina == true){
				if(h1<=h2){
					// prosledjuje se prvo kraca
					if(poklapanjeDuzi(y1, h1, y, h2, tol))
						visina = true;
				}

				
			}

			return sirina && visina;
	}

	// a je kraca duz duzine da
	def poklapanjeDuzi(a: Int, da: Int, b:Int, db:Int, tol:Int) : Boolean = {

			if((a > b - tol) && (a + da < b + db + tol))
				return true;

			return false;
	}
}