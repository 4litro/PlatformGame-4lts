import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Monito extends JLabel implements Runnable, KeyListener {

	private String[] spritesDerecho = new String[10];
	private String[] spritesIzquierdo = new String[10];
	private boolean runStatus = false, right = false, shift = false, up = false, left = false, colison = false;
	private boolean pausar, stop; // estos los meti para detener y pausar las teclas
	private int n = 0, posX, posY, posXBack = 0, posXBase = 0, posXMeta = 3500, salto;

	private ImageIcon icon;
	Plataforma[] plataformas;
	JLabel background, base, meta;
	JButton btnPause, btnStop, btnStart;
	Sonido sonido;

	public Monito(String[] spritesDerecho, String[] spritesIzquierdo, int x, int y) {
		this.spritesDerecho = spritesDerecho;
		this.spritesIzquierdo = spritesIzquierdo;
		this.posX = x;
		this.posY = y;

		icon = new ImageIcon(this.getClass().getResource(spritesDerecho[0]));
		setIcon(icon);
	}

	public void run() {
		runStatus = true;
		stop = false;
		// fin = false;
		while (runStatus) {

			quieto();
			if (interseccion() || interseccionA()) {

				movimiento();
			} else if (interseccionM()) {
				victoria();
			} else {
				gravedad(5, 30);
			}

			try {
				synchronized (this) {
					while (pausar) {
						wait();
					}
					if (stop) {
						runStatus = true; // Puedes establecer "runStatus" a "true" para reiniciar el ciclo
						stop = false; // Restablecer "stop" a "false"

						// Otras variables y configuración inicial aquí
						break; // Salir del ciclo actual
					}
				}
			} catch (Exception e) {

			}
		}

	}

	// ME DIO FLOJERA METERLO EN CADA LINEA ASI QUE LO HICE UN METODO xD
	public void traicatch(int tiempo) {
		try {
			Thread.sleep(tiempo);
		} catch (Exception e) {
		}
	}

	public void victoria() {
		sonido.stopM();
		sonido = new Sonido("sound/kirbyVictoria.wav");
		stop = true;
		sonido.playM();
		JOptionPane.showMessageDialog(null, "LLegaste a la meta ya ganaste");
		sonido.stopM();
		btnStart.setEnabled(false);
		btnPause.setEnabled(false);
		btnStop.setEnabled(true);
		
		
	}

	public void lose() {
		if (this.posY >= 650) {
			this.posY = 651;
			sonido.stopM();
			sonido = new Sonido("sound/kirbyLose.wav");
			stop = true;
			sonido.playM();
			JOptionPane.showMessageDialog(null, "te caiste PIPIPI");
			System.out.println(posY);
			sonido.stopM();
			btnStart.setEnabled(false);
			btnPause.setEnabled(false);
			btnStop.setEnabled(true);
		}
	}

	public boolean interseccionM() {
		Area areaBase = new Area(meta.getBounds()); // para la base sola
		Area monito = new Area(this.getBounds());
		if (areaBase.intersects(monito.getBounds2D())) {

			colison = true;
			if (!(areaBase.getBounds2D().getMaxY() + 1 == monito.getBounds2D().getMinY())) {
				posY--;

			}

		} else {
			colison = false;
		}

		return colison;
	}

	public boolean interseccionA() {
		Area areaBase = new Area(base.getBounds()); // para la base sola
		Area monito = new Area(this.getBounds()); // el area del monito

		if (areaBase.intersects(monito.getBounds2D())) {

			colison = true;
			if (!(areaBase.getBounds2D().getMaxY() + 1 == monito.getBounds2D().getMinY())) {
				this.posY--;
			}

		} else {
			colison = false;
		}

		return colison;
	}

	public boolean interseccion() {

		// obtenemos las areas de todas las plataformas
		Area[] aPlataformas = new Area[plataformas.length]; // para el array de plataformas

		Area monito = new Area(this.getBounds()); // el area del monito

		// obtenemos las dimesiones de todas las plataformas
		for (int i = 0; i < aPlataformas.length; i++) {
			aPlataformas[i] = new Area(plataformas[i].getBounds());
		}

		for (int i = 0; i < aPlataformas.length; i++) {
			if (aPlataformas[i].intersects(monito.getBounds2D())) {
				colison = true;
				if (!(aPlataformas[i].getBounds2D().getMaxY() == monito.getBounds2D().getMinY())) {

					this.posY--;

				}
				break;
			} else {
				colison = false;
			}
		}

		return colison;

	}

	private void gravedad(int presion, int tiempo) {

		posY += presion;
		
		lose();

		traicatch(tiempo);

	}

	public void movimiento() {

		if (right && shift) {
			moveImageDerecho(15, posY, 5, spritesDerecho);

		}
		if (right) {
			moveImageDerecho(5, posY, 10, spritesDerecho);

		}
		if (left && shift) {
			moveImageIzquierdo(15, posY, 5, spritesIzquierdo);

		}
		if (left) {
			moveImageIzquierdo(5, posY, 10, spritesIzquierdo);

		}

		if (up) {
			saltito(5);
		}

		

	}

	// movimiento derecho
	private void moveImageDerecho(int velocidad, int posY, int time, String[] sprites) {

		animacion(time, sprites); // meti este funcion que se ira cambiando para hacer una animacion mas fluido

		limiteDerecho(velocidad, posY, time);

		if (up) {
			salto(velocidad, time);
		}

	}

	private void limiteDerecho(int velocidad, int posY, int tiempo) {

		if (posX > 500 && background.getX() > -2655) {
			if (!(posXBack <= -2655)) {
				posXBack -= velocidad;
				posXBase -= velocidad;
				posXMeta -= velocidad;
				for (int i = 0; i < plataformas.length; i++) {
					plataformas[i].movObj(velocidad);
				}

			}
			setBounds(posX, posY, 40, 40);
			meta.setBounds(posXMeta, 500, 136, 26);
			base.setBounds(posXBase, 400, 160, 17);
			background.setBounds(posXBack, -100, 3712, 1024);
		} else if (!(posX >= 944)) {
			posX += velocidad;
			setBounds(posX, posY, 40, 40);

		} else {
			setBounds(posX, posY, 40, 40);
		}

		lose();

		setIcon(icon);

		traicatch(tiempo);

	}

	// movimiento izquierdo
	private void moveImageIzquierdo(int velocidad, int posY, int time, String[] sprites) {

		animacion(time, sprites); // meti este funcion que se ira cambiando para hacer una animacion mas fluido

		limiteIzquierdo(velocidad, posY, time);

		if (up) {
			salto(velocidad, time);
		}

	}

	private void limiteIzquierdo(int velocidad, int posY, int tiempo) {

		if (posX < 500 && background.getX() < 0) {
			if (!(posXBack >= 0)) {
				posXBack += velocidad;
				posXBase += velocidad;
				posXMeta += velocidad;
				for (int i = 0; i < plataformas.length; i++) {
					plataformas[i].movObj(velocidad * -1);
				}
			}
			setBounds(posX, posY, 40, 40);
			meta.setBounds(posXMeta, 500, 136, 26);
			base.setBounds(posXBase, 400, 160, 17);
			background.setBounds(posXBack, -100, 3712, 1024);
		} else if (!(posX <= 1)) {
			posX -= velocidad;
			setBounds(posX, posY, 40, 40);

		} else {
			setBounds(posX, posY, 40, 40);
		}

		lose();

		setIcon(icon);

		traicatch(tiempo);
	}

	// metodo saltoleft
	private void salto(int velocidad, int time) {

		if(shift){
			salto = (posY - 200);
		} else {
			salto = (posY - 150);
		}

		for (int y = posY; y >= salto; y -= velocidad) {

			if (right) {
				limiteDerecho(velocidad, y, 10);
			} else if (left) {
				limiteIzquierdo(velocidad, y, 10);
			} else {
				setBounds(getX(), y, 40, 40);
			}
			traicatch(time);
		}
		for (int y = getY(); y <= posY; y += velocidad) {

			if (right) {
				limiteDerecho(velocidad, y, 10);
			} else if (left) {
				limiteIzquierdo(velocidad, y, 10);
			} else {
				setBounds(getX(), y, 40, 40);
			}

			traicatch(time);
		}
	}

	// Saltito
	private void saltito(int time) {

		salto = (posY - 100);

		for (int y = posY; y >= salto; y--) {
			setBounds(getX(), y, 40, 40);

			traicatch(time);
		}
		for (int y = getY(); y <= posY; y++) {
			setBounds(getX(), y, 40, 40);

			traicatch(time);
		}
	}

	// metodo quieto
	private void quieto() {
		setBounds(posX, posY, 40, 40);
	}

	private void cordsOrigen() { //metodo de reinicio de cordenadas o bounds
		setBounds(posX = 10, posY = 300, 40, 40);
		background.setBounds(posXBack = 0, -100, 3712, 1024);
		meta.setBounds(posXMeta = 3500, 500, 136, 26);
		base.setBounds(posXBase = 0, 400, 160, 17);
	}

	// Metodos sincronizados 
	synchronized void pausarHilo() {
		pausar = true;
	}

	synchronized void reanudarHilo() {
		pausar = false;
		notify(); // Despertamos al thread
	}

	synchronized void stopHilo() {
		//para cuando se reinicie las cords vuelvan a su lugar de origen y no haya conflicto
		cordsOrigen(); 
		stop = true;
		pausar = false;
		notify();
	}

	public void keyTyped(KeyEvent ke) {
	}

	public void keyPressed(KeyEvent ke) {
		if (runStatus) {
			if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = true;
			}
			if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
				shift = true;
			}
			if (ke.getKeyCode() == KeyEvent.VK_UP) {
				up = true;
			}
			if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
				left = true;
			}

		}
	} // end keyPressed

	public void keyReleased(KeyEvent ke) {
		if (runStatus) {
			if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = false;
			}
			if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
				shift = false;
			}
			if (ke.getKeyCode() == KeyEvent.VK_UP) {
				up = false;
			}
			if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
				left = false;
			}
		}
	}

	public void animacion(int time, String[] sprites) {
		switch (n) {
			case 1:
				icon = new ImageIcon(this.getClass().getResource(sprites[1]));
				n++;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;

			case 2:
				icon = new ImageIcon(this.getClass().getResource(sprites[2]));
				n++;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;
			case 3:
				icon = new ImageIcon(this.getClass().getResource(sprites[3]));
				n++;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;
			case 4:
				icon = new ImageIcon(this.getClass().getResource(sprites[4]));
				n++;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;
			case 5:
				icon = new ImageIcon(this.getClass().getResource(sprites[5]));
				n++;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;
			case 6:
				icon = new ImageIcon(this.getClass().getResource(sprites[6]));
				n++;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;
			case 7:
				icon = new ImageIcon(this.getClass().getResource(sprites[7]));
				n++;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;
			case 8:
				icon = new ImageIcon(this.getClass().getResource(sprites[8]));
				n++;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;
			case 9:
				icon = new ImageIcon(this.getClass().getResource(sprites[9]));
				n = 0;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;

			default:
				icon = new ImageIcon(this.getClass().getResource(sprites[0]));
				n++;
				try {
					Thread.sleep(time);
				} catch (Exception e) {
				}
				break;

		}

	}

}
