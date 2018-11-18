package gladneZeke
import scala.util.Random
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import java.io.File
//100x65 88x70
object Vuk extends Thread{
	var x = 600; // ide iz donjeg desnog ugla ka gore levo
	var y = 500;
	var x2 = 650; // ide iz gornjeg desnog ugla ka dole desno
	var y2 = 0;
	var slikaDD = new Image("slike"+File.separator+"pasDD.png");
	var slikaLD = new Image("slike"+File.separator+"pasLD.png");
	var slikaLG = new Image("slike"+File.separator+"pasLG.png");
	var slikaDG = new Image("slike"+File.separator+"pasDG.png");
	var slika1=slikaLG;
	var slika2=slikaLD;
	var ind = true;
	var ind2 = true;
	var wd = 100;
	var hd = 65;
	var wg = 88;
	var hg = 70;
	
	override  def run: Unit ={
			while(true){
				try{
					if(Pocetna.krajIgre==false){
						move();
						eat();	 
						move2();
						eat2();
					}
					Thread.sleep(10);

				}
				catch{
				case _: Throwable => println("VUK :: IZUZETAK!!! ")
				}
			}
	}
	def draw2(gcX:GraphicsContext): Unit ={
			gcX.drawImage(slika2,x2,y2);
	};

	def draw(gcX:GraphicsContext): Unit ={
			gcX.drawImage(slika1,x,y);
	};
	def move(): Unit = {
			if(y>0 && ind == true){
				x = x-1;
				y = y-1;
				if(y == 0){
					ind = false;
					slika1 = slikaDD;
				}
			}
			if(y<420 && ind == false){
				x = x+1;
				y = y+1;
				if (y == 420){
					ind = true;
					slika1 = slikaLG; 
				}
			}
	}
	def move2(): Unit = {
			if(y2<420 && ind2 == true){
				x2 = x2-1;
				y2 = y2+1;
				if(y2 == 420){
					ind2 = false;
					slika2= slikaDG;
				}
			}
			if(y2>80 && ind2 == false){
				x2 = x2 + 1;
				y2 = y2 - 1;
				if (y2 == 80){
					ind2 = true;
					slika2 = slikaLD; 
				}
			}
	}
	def eat(): Unit ={
	    var w = wd;
	    var h = hd;
	    if(slika1 == slikaLG || slika1 == slikaDG){
	        w=wg;
	        h=hg;
	    }
	    if(poklapanje(x, y, w, h, Zec.x, Zec.y, 42, 44, 5)){
	        Zec.pojeden = true;
	    }
	    if(poklapanje(x, y, w, h, Zec2.x, Zec2.y, 44, 45, 5))
	        Zec2.pojeden = true;
	    
	};
	
	def eat2(): Unit ={
	    var w = wd;
	    var h = hd;
	    if(slika2 == slikaLG || slika2 == slikaDG){
	        w=wg;
	        h=hg;
	    }
	    if(poklapanje(x2, y2, w, h, Zec.x, Zec.y, 42, 44, 5)){
	        Zec.pojeden = true;
	    }
	    if(poklapanje(x2, y2, w, h, Zec2.x, Zec2.y, 44, 45, 5))
	        Zec2.pojeden = true;
	    
	};
	
	def poklapanje(x1:Int, y1:Int, w1:Int, h1:Int, x2:Int, y2:Int, w2:Int, h2:Int, tol:Int) : Boolean = {
			var sirina = false;
			var visina = false;

			// prvo poredimo sirine, izaberemo onu koja ima manju sirinu (w)

			if(poklapanjeDuzi(x2, w2, x1, w1, tol))
				sirina = true;

			if(poklapanjeDuzi(y2, h2, y1, h1, tol))
				visina = true;


			return sirina && visina;
	}

	// a je kraca duz duzine da
	def poklapanjeDuzi(a: Int, da: Int, b:Int, db:Int, tol:Int) : Boolean = {

			if((a > b - tol) && (a + da < b + db + tol))
				return true;

			return false;
	}
}