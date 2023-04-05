import javax.swing.JFrame;

import javax.swing.*;
import java.awt.event.*;

/* 
 * Proyecto: Caída libre. El elemento principal o Monito tendrá que ir saltando de plataforma en plataforma 
 * mientras cada plataforma y background se mueve a cierta velocidad hacia atrás. El monito podrá moverse hacia adelante y hacia atrás, 
 * también podrá dar saltos cortos o largos, agrégale velocidad al monito para dar saltos largos. Ganarás si llegas a la meta.
 * 
 * Alumno: Aldair Alexis Suarez Juarez (CUATROLITRO)
 * 
 * Materia: Videojuegos
 * 
*/

public class Ventana extends JFrame {

	Sonido sonido; // creamos el objeto sonido
	boolean bandera = true; // para poder hacer los cambios en el hilo
	int xMax = 3500; // limite maximo para generar las plataformas en X
	int xMin = 200; // limite minimo para generar las plataformas en X
	int yMax = 550; // limite maximo para generar las plataformas en Y
	int yMin = 400; // limite minimo para generar las plataformas en y

	int xP; // posicion en X de la plataforma1
	int yP; // posicion en Y de la plataforma1
	int x = 10; // posicion en x del personaje
	int y = 300; // posicion en y del personaje

	public Ventana() {
		initValues();
	}

	public void initValues() {

		// monito
		String[] spritesDerecho = new String[10];

		spritesDerecho[0] = "images/linkderecho1.png";
		spritesDerecho[1] = "images/linkderecho2.png";
		spritesDerecho[2] = "images/linkderecho3.png";
		spritesDerecho[3] = "images/linkderecho4.png";
		spritesDerecho[4] = "images/linkderecho5.png";
		spritesDerecho[5] = "images/linkderecho6.png";
		spritesDerecho[6] = "images/linkderecho7.png";
		spritesDerecho[7] = "images/linkderecho8.png";
		spritesDerecho[8] = "images/linkderecho9.png";
		spritesDerecho[9] = "images/linkderecho10.png";

		String[] spritesIzquierdo = new String[10];

		spritesIzquierdo[0] = "images/linkizquierdo1.png";
		spritesIzquierdo[1] = "images/linkizquierdo2.png";
		spritesIzquierdo[2] = "images/linkizquierdo3.png";
		spritesIzquierdo[3] = "images/linkizquierdo4.png";
		spritesIzquierdo[4] = "images/linkizquierdo5.png";
		spritesIzquierdo[5] = "images/linkizquierdo6.png";
		spritesIzquierdo[6] = "images/linkizquierdo7.png";
		spritesIzquierdo[7] = "images/linkizquierdo8.png";
		spritesIzquierdo[8] = "images/linkizquierdo9.png";
		spritesIzquierdo[9] = "images/linkizquierdo10.png";

		Monito personaje = new Monito(spritesDerecho, spritesIzquierdo, x, y);

		// añadimos las plataformas flotantes moviles
		Plataforma[] plataformas = new Plataforma[30];

		// este ciclo es para que genere las estrucuturas de manera aleatoria
		// excepetuando a la primera plataforma
		for (int i = 0; i < plataformas.length; i++) {
			// max-min min
			xP = (int) (Math.random() * ((xMax - xMin) + 1)) + xMin;
			yP = (int) (Math.random() * ((yMax - yMin) + 1)) + yMin;

			plataformas[i] = new Plataforma("images/bloque.png", xP, yP);
			plataformas[i].setBounds(xP, yP, 136, 26);

		}

		// añadimos las plataformas estaticas
		JLabel base = new JLabel();
		ImageIcon icon2 = new ImageIcon(this.getClass().getResource("images/base.png"));
		base.setIcon(icon2);

		JLabel meta = new JLabel();
		ImageIcon icon3 = new ImageIcon(this.getClass().getResource("images/plataforma.png"));
		meta.setIcon(icon3);

		// stage escenarios
		JLabel stage1 = new JLabel();
		ImageIcon icon1 = new ImageIcon(this.getClass().getResource("images/fondo.png"));
		stage1.setIcon(icon1);

		// llevamos los objetos a la clase personaje para manipularlos
		personaje.background = stage1;
		personaje.base = base;
		personaje.plataformas = plataformas;
		personaje.meta = meta;

		// buttons
		JButton btnStart = new JButton("Start");
		JButton btnPause = new JButton("Pause");
		JButton btnStop = new JButton("Stop");

		// setBounds
		btnStart.setBounds(10, 600, 75, 25);
		btnPause.setBounds(90, 600, 75, 25);
		btnStop.setBounds(170, 600, 75, 25);
		personaje.setBounds(x, y, 40, 40);
		meta.setBounds(3500, 500, 136, 26);
		base.setBounds(0, 400, 160, 17);
		stage1.setBounds(0, -100, 3712, 1024);

		// actionListener
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				

				if (ae.getSource() == btnStart) {
					Thread t = new Thread(personaje);
					t.start();
					
					sonido = new Sonido("sound/kirby.wav");
					sonido.playM();

					personaje.sonido = sonido; 

					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
					btnPause.setEnabled(true);

					/*
					 * for (int i = 0; i < plataformas.length; i++) {
					 * Thread t1 = new Thread(plataformas[i]);
					 * t1.start();
					 * }
					 */

				}
				if (ae.getSource() == btnPause) {
					if (bandera) {
						personaje.pausarHilo();
						sonido.pauseM();
						bandera = false;
						btnPause.setText("Reanudar");
					} else {
						personaje.reanudarHilo();
						bandera = true;
						sonido.resumeM();
						btnPause.setText("Pausar");
					}
				}

				if (ae.getSource() == btnStop) {
					personaje.stopHilo();

					for (int i = 0; i < plataformas.length; i++) {
						xP = (int) (Math.random() * ((xMax - xMin) + 1)) + xMin;
						yP = (int) (Math.random() * ((yMax - yMin) + 1)) + yMin;
						plataformas[i].setBounds(xP, yP, 136, 26); 
					}

					bandera = true;
					sonido.stopM();
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					btnPause.setEnabled(false);
				}

			} // end actionPerformed
		};

		// setFocusuable
		personaje.setFocusable(true);
		btnStart.setFocusable(false);
		btnPause.setFocusable(false);
		btnStop.setFocusable(false);

		// desabilitamos el boton pause y el boton stop para que no haya problemas
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		btnPause.setEnabled(false);

		// addActionListener
		btnStart.addActionListener(listener);
		btnPause.addActionListener(listener);
		btnStop.addActionListener(listener);
		personaje.addKeyListener(personaje);

		add(personaje);
		// este ciclo lo usamos para añadir los objetos a la ventana
		for (int i = 0; i < plataformas.length; i++) {
			add(plataformas[i]);
		}
		add(meta);
		add(base);
		add(btnStart);
		add(btnPause);
		add(btnStop);
		add(stage1);

		setTitle("PlatformGame-4lts");
		setSize(1000, 700);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
