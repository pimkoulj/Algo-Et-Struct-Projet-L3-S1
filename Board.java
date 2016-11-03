import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Color; 
import java.awt.Font; 
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.util.Random;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JOptionPane;

class GameParameters
{
    private int matrix_size_;
    private int star_cardinal_;
    private int tile_size_;
    private int border_size_;
    
    public GameParameters(int matsize, int stars, int tsize)
    {
        matrix_size_ = matsize;
        star_cardinal_ = rectifyStarCardinal(stars);
        tile_size_ = tsize;
    }

    public GameParameters()
    {
        this(10, 3, 50);
    }
    
    private int rectifyStarCardinal(int star_card){return (star_card > tileCardinal()/ 2 ) ? tileCardinal()/2 : star_card;}

    public int matrixSize() { return matrix_size_; }
    public int starCardinal() { return star_cardinal_; }
    public int tileSize() { return tile_size_; }
    public int borderSize() {return border_size_; }
    public int comprehensiveTileSize() { return border_size_ + tile_size_; }
    public int boardSize() { return (border_size_ + tile_size_) * matrix_size_;}
    public int tileCardinal() { return matrix_size_ * matrix_size_; }

    public void setMatrixSize(int new_mat_size) { matrix_size_ = new_mat_size; }
    public void setStarCardinal(int new_star_card) { star_cardinal_ = rectifyStarCardinal(new_star_card);}
    public void setTileSize(int new_tile_size) { tile_size_ = new_tile_size;}
    public void setBorderSize(int new_border_size) { border_size_ = new_border_size; }


}

class Coordonnee
{
	private int x_;
	private int y_;
	
	public Coordonnee(int x, int y)
	{
		x_ = x;
		y_ = y;
	}
	
	public int x() { return x_; }
	public int y() { return y_; }
}

class Board extends JPanel implements MouseListener
{
    private int turn;
    private Matrix matrix_;
    private GameParameters params_;
    private ArrayList<int[]> red_star_tiles;
    private ArrayList<int[]> blue_star_tiles;
    private State state_;
   
    
    public Board()
    {
        this(new GameParameters());//charge les parametres par defaut
    }

    public Board(GameParameters params)
    {
        params_ = params;
        turn = 0;
        matrix_ = new Matrix(params_.matrixSize());
        red_star_tiles = new ArrayList<int[]>();
		blue_star_tiles = new ArrayList<int[]>();
        initialiseStarTiles();
        
        addMouseListener(this);

        initialiseEmptyTiles();
		int comprehensive_size = params_.comprehensiveTileSize() * params_.matrixSize() - params_.borderSize();
        super.setPreferredSize(new Dimension(comprehensive_size ,comprehensive_size ));
        state_ = new ColorerCase(this);
    }
	
	public void	addRedStarTile(int x, int y)
    {
		matrix_.set(x, y, new StarTile(Color.RED));
		red_star_tiles.add(new int[]{x,y});
		matrix_.joinNeighbours(x,y);
    }
	
	public void set_etat_courant(State state)
	{
		state_ = state;
	}
	
	public Coordonnee getCoord(MouseEvent e)
	{
		return new Coordonnee(e.getX() / (params_.tileSize() + params_.borderSize()),
			e.getY() / (params_.tileSize() + params_.borderSize()));
	}
	
	public void set_tile_color(int x, int y, Color color)
	{
		matrix_.get(x,y).setColor(color);
		matrix_.joinNeighbours(x,y);
	}
	
	public boolean hasNeighbour(Color c, int x, int y)  //une fonction qui du coup n'est plus forcément utile vu qu'on peut colorer une case n'importe ou
	{
		return(matrix_.hasNeighbour(c,x,y));
	}
	
	public void fillRect(int x, int y , int width, int height, Graphics g)
	{
		g.fillRect(x,y,width, height);
	}
	 
	public Tile get_tile(int x, int y)
	{
		return(matrix_.get(x,y));
	}
	
	public Tile get_tile(Coordonnee c)
	{
		return get_tile(c.x(), c.y());
	}
	
	public void next_turn()
	{
		++turn;
	}
	
	public boolean valid_coordinates(int x, int y)
	{
		return(x >= 0 && y >= 0 && x < params_.boardSize() && y <  params_.boardSize());
	}
	
	public void	addBlueStarTile(int x, int y)
    {
		matrix_.set(x, y, new StarTile(Color.BLUE));
		blue_star_tiles.add(new int[]{x,y});
		matrix_.joinNeighbours(x,y);
    }
 
 
    private void initialiseStarTiles()
    {
        int tmp_x;
        int tmp_y;
        Random rng = new Random();
        for(int i = 0 ; i < params_.starCardinal() ; ++i)
        {
            while(matrix_.get(tmp_x = rng.nextInt(matrix_.size()), tmp_y = rng.nextInt(matrix_.size())) != null){}
			
			addRedStarTile(tmp_x, tmp_y);
        }
        for(int i = 0 ; i < params_.starCardinal() ; ++i)
        {
            while(matrix_.get(tmp_x = rng.nextInt(matrix_.size()), tmp_y = rng.nextInt(matrix_.size())) != null){}
			
			addBlueStarTile(tmp_x, tmp_y);
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
	
	public int nbEtoiles(int x, int y)
	{
		int ret = 0;
		if(matrix_.get(x, y).getColor() == Color.RED)
		{
			for(int[] tmp : red_star_tiles)
			{
				if(matrix_.get(tmp[0], tmp[1]).memeClasse(matrix_.get(x,y)))
					++ret;
			}
		}
		else if(matrix_.get(x, y).getColor() == Color.BLUE)
		{
			for(int[] tmp : blue_star_tiles)
			{
				if(matrix_.get(tmp[0], tmp[1]).memeClasse(matrix_.get(x,y)))
					++ret;
			}
		}
		return(ret);
	}
	
    public void paintComponent(Graphics g)
    {
		state_.paintComponent(g);
		//~ Color c = turnColor();
        //~ g.setColor(Color.WHITE);
      
        //~ g.fillRect(0,0, 10000, 10000);
        //~ int x;
        //~ int y;
        //~ final Point mousePos = this.getMousePosition();
        //~ Font font = new Font("Serif", Font.BOLD, params_.tileSize() );
        //~ g.setFont(font);
		
        //~ for(int i = 0 ; i < matrix_.size() ; ++i)
        //~ {
            //~ for(int j = 0 ; j < matrix_.size() ; ++j)
            //~ {
                //~ g.setColor(matrix_.get(i, j).getColor());
                //~ if(mousePos != null 
				//~ && ((x = mousePos.x / params_.comprehensiveTileSize()) == i)
				//~ && ((y = mousePos.y / params_.comprehensiveTileSize()) == j) 
				//~ & matrix_.hasNeighbour(c,x,y) 
				//~ && matrix_.get(x,y).getColor() == Color.WHITE) 	
				//~ {
                    //~ g.setColor(turn%2 == 0 ? Color.RED : Color.BLUE);
				//~ }
                //~ g.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize());
                //~ if(matrix_.get(i,j).isStarTile())
                //~ {
					//~ g.setColor(Color.BLACK);
					//~ g.drawString("*",i * params_.comprehensiveTileSize() + params_.tileSize() / 3, j*params_.comprehensiveTileSize() + params_.tileSize() / 1);
				//~ }
            //~ }
        //~ }

    }
	 
    public void mousePressed(MouseEvent e)
    {
        //~ Color c = turnColor();
        //~ x = e.getX() / (params_.tileSize() + params_.borderSize());
        //~ y = e.getY() / (params_.tileSize() + params_.borderSize());
        
        //~ if(matrix_.valid_coordinates(x,y) && matrix_.get(x, y).isEmpty() && matrix_.hasNeighbour(c, x, y))
        //~ {
            //~ matrix_.get(x, y).setColor(c);
            //~ matrix_.joinNeighbours(x,y);
            
            //~ if(nbEtoiles(x,y) == params_.starCardinal())
            //~ {
				//~ JOptionPane.showMessageDialog(this, 
											 //~ (matrix_.get(x,y).getColor() == Color.RED ? "Rouge a gagné !" : "Bleu a gagné !"),
											 //~ " Fin du game",
											 //~ JOptionPane.INFORMATION_MESSAGE);
			//~ }
            //~ ++turn;
        //~ }
        state_.mousePressed(e);
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
