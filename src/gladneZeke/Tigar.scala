package gladneZeke

//80x50 50x62
import scala.util.Random
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import java.io.File
object Tigar extends Thread {
	var x = 600;
	var y = 450;
	var x1 = 300;
	var y1 = 0;
	var slikaL = new Image("slike"+File.separator+"tigarL.png");
	var slikaD = new Image("slike"+File.separator+"tigarD.png");
	var slikaN = new Image("slike"+File.separator+"tigarN.png");

	var slika = slikaL;
	var slika1 = slikaL;
	var rand = new Random;
	var w1 = 80;
	var h1 = 50;
	var w2 = 50; // dimenzije slika kada gleda u ekran
	var h2 = 62;

	def draw(gcX:GraphicsContext): Unit ={
			gcX.drawImage(slika,x,y);
	};
	def draw2(gcX:GraphicsContext): Unit ={
			gcX.drawImage(slika1,x1,y1);
	};

	override  def run: Unit ={
			while(true){
				try{
					if(Pocetna.krajIgre==false){
						moveRandomly();
						moveRandomly1();
						eat();
						eat1();
					}
					Thread.sleep(10);
				}
				catch{
				case _: Throwable => println("TIGAR :: IZUZETAK!!! ")
				}
			}
	}

	var indP = true; // vec odredjen smer? jeste, namjestamo da tigar ne pojede igraca bas odmah
	var parametar = 1; // smer kretanja
	var brKoraka = 0; // nakon 200 koraka, nek opet promjeni smjer, zato brojimo korake

	def moveRandomly(): Unit ={
			if(indP == false) {
				parametar= Random.nextInt(4);
				indP = true; // odredili smo smjer
				brKoraka = 0;
			}
			else{
				brKoraka += 1;
				//levo
				if(parametar == 0){
					slika=slikaL;
					if (x > 10)
						x = x - 2;

					if( x <= 10)
						indP = false; // sada smo dosli do kraja, treba da se ponovo odredi smjer
				}
				//desno
				else if(parametar == 1){
					slika=slikaD;
					if (x < 620)
						x = x + 2;	
					if(x >= 620)
						indP = false;

				}
				// dole
				else if(parametar == 2){
					slika=slikaN;
					if (y < 430)
						y = y + 2;
					if(y >= 430)
						indP = false;
				}
				// gore
				else {
					slika=slikaN;
					if (y > 10)
						y = y - 1;
					if( y <= 10)
						indP = false;

				}

				if(brKoraka==200){
					indP = false; // napravio 200 koraka, sad moze da ide u drugom pravcu
				}
			}
	};

	var indP1 = true; // vec odredjen smer?
	var parametar1 = 2; // smer kretanja
	var brKoraka1 = 0;

	def moveRandomly1(): Unit ={
			if(indP1 == false) {
				parametar1= Random.nextInt(4);
				indP1 = true; // odredili smo smjer
				brKoraka1 = 0;
			}
			else{
				brKoraka1 += 1;
				//levo
				if(parametar1 == 0){
					slika1=slikaL;
					if (x1 > 10)
						x1 = x1 - 2;

					if( x1 <= 10)
						indP1 = false; // sada smo dosli do kraja, treba da se ponovo odredi smjer
				}
				//desno
				else if(parametar1 == 1){
					slika1=slikaD;
					if (x1 < 620)
						x1 = x1 + 2;	
					if(x1 >= 620)
						indP1 = false;

				}
				// dole
				else if(parametar1 == 2){
					slika1=slikaN;
					if (y1 < 430)
						y1 = y1 + 2;
					if(y1 >= 430)
						indP1 = false;
				}
				// gore
				else {
					slika1=slikaN;
					if (y1 > 10)
						y1 = y1 - 1;
					if( y1 <= 10)
						indP1 = false;

				}

				if(brKoraka1==200){
					indP1 = false; // napravio 200 koraka, sad moze da ide u drugom pravcu
				}
			}
	};
	def eat(): Unit ={
			var w = w1;
			var h = h1;
			if(slika == slikaN){
				var w = w2;
				var h = h2;
			}

			if(poklapanje(x, y, w, h, Zec.x, Zec.y, 42, 44, 15)){
				Zec.pojeden = true;
			}
			if(poklapanje(x, y, w, h, Zec2.x, Zec2.y, 44, 45, 15))
				Zec2.pojeden = true;

	};
	def eat1(): Unit ={
			var w = w1;
			var h = h1;
			if(slika1 == slikaN){
				var w = w2;
				var h = h2;
			}
			if(poklapanje(x1, y1, w, h, Zec.x, Zec.y, 42, 44, 15)){
				Zec.pojeden = true;
			}
			if(poklapanje(x1, y1, w, h, Zec2.x, Zec2.y, 44, 45, 15))
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