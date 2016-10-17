import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import java.awt.Color; 
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

class Matrix extends JPanel implements MouseListener
{

	private int x;
	private int y;
	private int turn;
	private Tile[][] tab;
	
	public Matrix()
	{
		turn = 0;
		tab = new Tile[Constante.MATRIX_SIZE + 10][Constante.MATRIX_SIZE + 10];
		
		addMouseListener(this);
		for(int i = 0 ; i < Constante.MATRIX_SIZE; ++i)
		{
			for(int j = 0 ; j < Constante.MATRIX_SIZE; ++j)
			{
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
				System.out.println("a");
				g.setColor(tab[i][j].getColor());
				if((mousePos != null && mousePos.x / (Constante.TILE_SIZE + 2)== i && mousePos.y / (Constante.TILE_SIZE + 2) == j)) 	
					g.setColor(turn%2 == 0 ? Color.RED : Color.BLUE);
				g.fillRect(i*Constante.TILE_SIZE + 2 * i, j*Constante.TILE_SIZE + 2 * j, Constante.TILE_SIZE, Constante.TILE_SIZE);
			}
		}

	}
	 
	public void mousePressed(MouseEvent e)
	{
		tab[e.getX() / (Constante.TILE_SIZE + 2)][e.getY() / (Constante.TILE_SIZE + 2)].setColor(turn%2 == 0 ? Color.RED : Color.BLUE);
		++turn;
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
