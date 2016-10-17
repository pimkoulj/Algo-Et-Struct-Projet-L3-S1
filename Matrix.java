import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import java.awt.Color; 
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.util.Random;

class Matrix extends JPanel implements MouseListener
{

    private int x;
    private int y;
    private int turn;
    private Tile[][] tab;
	
    public Matrix()
    {
        int tmp_x;
        int tmp_y;
        turn = 0;
        tab = new Tile[Constante.MATRIX_SIZE + 10][Constante.MATRIX_SIZE + 10];
        Random rng = new Random();
        for(int i = 0 ; i < Constante.NB_STAR ; ++i)
        {
            while(tab[tmp_x = rng.nextInt(Constante.MATRIX_SIZE)][tmp_y = rng.nextInt(Constante.MATRIX_SIZE)] != null){}
            tab[tmp_x][tmp_y] = new StarTile(Color.RED);
        }
        for(int i = 0 ; i < Constante.NB_STAR ; ++i)
        {
            while(tab[tmp_x = rng.nextInt(Constante.MATRIX_SIZE)][tmp_y = rng.nextInt(Constante.MATRIX_SIZE)] != null){}
            tab[tmp_x][tmp_y] = new StarTile(Color.BLUE);
        }
		
        addMouseListener(this);
        for(int i = 0 ; i < Constante.MATRIX_SIZE; ++i)
        {
            for(int j = 0 ; j < Constante.MATRIX_SIZE; ++j)
            {
                if(tab[i][j] == null)
                    tab[i][j] = new Tile();
            }
        }
    }
	
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, 10000, 10000);
        final Point mousePos = this.getMousePosition();
		
        for(int i = 0 ; i < Constante.MATRIX_SIZE ; ++i)
        {
            for(int j = 0 ; j < Constante.MATRIX_SIZE ; ++j)
            {
                g.setColor(tab[i][j].getColor());
                if((mousePos != null && mousePos.x / (Constante.TILE_SIZE + 2) == i && mousePos.y / (Constante.TILE_SIZE + 2) == j)) 	
                    g.setColor(turn%2 == 0 ? Color.RED : Color.BLUE);
                g.fillRect(i*Constante.TILE_SIZE + 2 * i, j*Constante.TILE_SIZE + 2 * j, Constante.TILE_SIZE, Constante.TILE_SIZE);
            }
        }

    }
	
    public boolean hasNeighbour(Color c, int x, int y)
    {

        for(int i = (x == 0 ? 0 : x - 1); i <= (x == Constante.MATRIX_SIZE - 1 ? Constante.MATRIX_SIZE - 1 : x + 1) ; ++i)
            for(int j = (y == 0 ? 0 : y - 1); j <= (y == Constante.MATRIX_SIZE - 1 ? Constante.MATRIX_SIZE - 1 : y + 1) ; ++j)
            {
                System.out.println(i + " " + j);
                if(tab[i][j].getColor() == c)
                    return(true);
            }
        return(false);
    }
	 
    public void mousePressed(MouseEvent e)
    {
        Color c = turnColor();
        x = e.getX() / (Constante.TILE_SIZE + 2);
        y = e.getY() / (Constante.TILE_SIZE + 2);
        if(tab[x][y].isEmpty() && hasNeighbour(c, x, y))
        {
            tab[x][y].setColor(c);
            ++turn;
        }
    }

    public Color turnColor()
    {
        return (turn %2 == 0) ? Color.RED : Color.BLUE;
    }
	     
    public void mouseReleased(MouseEvent e) {
 
    }
     
    public void mouseEntered(MouseEvent e) {

    }
     
    public void mouseExited(MouseEvent e) {

    }
     
    public void mouseClicked(MouseEvent e) {

    }
}
