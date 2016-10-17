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

class GameParameters
{
    private int matrix_size_;
    private int star_cardinal_;
    private int tile_size_;
    
    public GameParameters(int matsize, int stars, int tsize)
    {
        matrix_size_ = matsize;
        star_cardinal_ = stars;
        tile_size_ = tsize;
    }

    public GameParameters()
    {
        this(10, 3, 80);
    }

    public int matrixSize() { return matrix_size_; }
    public int starCardinal() { return star_cardinal_; }
    public int tileSize() { return tile_size_; }

}

class Board extends JPanel implements MouseListener
{
    private int x;
    private int y;
    private int turn;
    private IMatrix matrix_;
    private GameParameters params_;
    
    public Board()
    {
        turn = 0;
        params_ = new GameParameters();
        matrix_ = new Matrix(params_.matrixSize());
        
        initialiseStarTiles();
        
        addMouseListener(this);

        initialiseEmptyTiles();
    }

    private void initialiseStarTiles()
    {
        int tmp_x;
        int tmp_y;
        Random rng = new Random();
        for(int i = 0 ; i < params_.starCardinal() ; ++i)
        {
            while(matrix_.get(tmp_x = rng.nextInt(matrix_.size()), tmp_y = rng.nextInt(matrix_.size())) != null){}
            matrix_.set(tmp_x, tmp_y, new StarTile(Color.RED));
        }
        for(int i = 0 ; i < params_.starCardinal() ; ++i)
        {
            while(matrix_.get(tmp_x = rng.nextInt(matrix_.size()), tmp_y = rng.nextInt(matrix_.size())) != null){}
            matrix_.set(tmp_x, tmp_y, new StarTile(Color.BLUE));
        }
    }

    private void initialiseEmptyTiles()
    {
        for(int i = 0 ; i < matrix_.size(); ++i)
        {
            for(int j = 0 ; j < matrix_.size(); ++j)
            {
                if(matrix_.get(i, j) == null)
                    matrix_.set(i, j, new Tile());
            }
        }
    }
	
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, 10000, 10000);
        final Point mousePos = this.getMousePosition();
		
        for(int i = 0 ; i < matrix_.size() ; ++i)
        {
            for(int j = 0 ; j < matrix_.size() ; ++j)
            {
                g.setColor(matrix_.get(i, j).getColor());
                if((mousePos != null && mousePos.x / (params_.tileSize() + 2) == i && mousePos.y / (params_.tileSize() + 2) == j)) 	
                    g.setColor(turn%2 == 0 ? Color.RED : Color.BLUE);
                g.fillRect(i*params_.tileSize() + 2 * i, j*params_.tileSize() + 2 * j, params_.tileSize(), params_.tileSize());
            }
        }

    }
	 
    public void mousePressed(MouseEvent e)
    {
        Color c = turnColor();
        x = e.getX() / (params_.tileSize() + 2);
        y = e.getY() / (params_.tileSize() + 2);
        if(matrix_.get(x, y).isEmpty() && matrix_.hasNeighbour(c, x, y))
        {
            matrix_.get(x, y).setColor(c);
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

    public GameParameters params()
    {
        return params_;
    }
}
