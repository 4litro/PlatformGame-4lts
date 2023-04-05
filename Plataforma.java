import javax.swing.*;

public class Plataforma extends JLabel {

    private String url;
    private ImageIcon icon;
    public int x, y;

    // ME DIO FLOJERA METERLO EN CADA LINEA ASI QUE LO HICE UN METODO xD
    public void traicatch(int tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (Exception e) {
        }
    }

    /* Metodo contructor */
    public Plataforma(String url, int x, int y) {
        this.url = url;
        this.x = x;
        this.y = y;
        icon = new ImageIcon(this.getClass().getResource(url));
        setIcon(icon);
    }

    public void movObj(int x){
        setBounds(this.getX() - x, getY(), getWidth(), getHeight());
    }

    
}
