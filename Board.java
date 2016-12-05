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

class Board extends JPanel implements MouseListener
{
	private int redscore_;
	private int bluescore_;
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
        redscore_ = 0;
        bluescore_ = 0;
        matrix_ = new Matrix(params_.matrixSize());
        red_star_tiles = new ArrayList<int[]>();
        blue_star_tiles = new ArrayList<int[]>();
        initialise_star_tiles();
        
        addMouseListener(this);

        initialise_empty_tiles();
        int comprehensive_size = params_.comprehensiveTileSize() * params_.matrixSize() - params_.borderSize();
        super.setPreferredSize(new Dimension(comprehensive_size ,comprehensive_size ));
        state_ = new ColorerCase(this);
    }

    //#########################################################
    //################### pseudo getters ######################
    //###                                                   ###
    
    public Pair< ArrayList<Coordinate> > composantes(Coordinate ori, Coordinate dest)
    {
        Pair< ArrayList<Coordinate> > result = new Pair< ArrayList<Coordinate> >(
            new ArrayList<Coordinate>(),
            new ArrayList<Coordinate>() );
        
        Tile oriPere = matrix_.get(ori.x(), ori.y()).representant();
        Tile destPere = matrix_.get(dest.x(), dest.y()).representant();
        
        for(int i = 0; i < params_.matrixSize(); ++i)
            for(int j = 0; j < params_.matrixSize(); ++j)
                if( matrix_.get(i, j).memeClasse(oriPere))
                    result.first.add( new Coordinate(i, j) );
                else if( matrix_.get(i, j).memeClasse(destPere))
                    result.second.add( new Coordinate(i, j));
        
        return result;
    }

    public ArrayList<Coordinate> composante(Coordinate ori)
    {
        ArrayList<Coordinate> result = new ArrayList<Coordinate>();
        
        Tile oriPere = matrix_.get(ori.x(), ori.y()).representant();
        
        for(int i = 0; i < params_.matrixSize(); ++i)
            for(int j = 0; j < params_.matrixSize(); ++j)
                if( matrix_.get(i, j).memeClasse(oriPere))
                    result.add( new Coordinate(i, j) );
        return result;
    }

    public Coordinate locate(MouseEvent e)
    {
        return new Coordinate(
            e.getX() / params_.comprehensiveTileSize(),
            e.getY() / params_.comprehensiveTileSize());
    }

    public Coordinate locate(Point pos)
    {
        if(pos == null)//nécessaire ?
            return new Coordinate(-1, -1);
        return new Coordinate(
            pos.x / params_.comprehensiveTileSize(),
            pos.y / params_.comprehensiveTileSize() );
    }

    public Coordinate locate_mouse()
    {
        return locate(getMousePosition());
    }
    
    public Tile get_tile(int x, int y)
    {
        return matrix_.get(x,y);
    }
	
    public int get_red_score()
    {
        return redscore_;
    }
    
    public int get_blue_score()
    {
        return bluescore_;
    }
    
    public Tile tile(Coordinate c)
    {
        return matrix_.get(c.x(), c.y());
    }

    public Color turn_color()
    {
        return (turn %2 == 0) ? Color.RED : Color.BLUE;
    }

    public GameParameters params()
    {
        return params_;
    }

    //#################################################
    //##################### setters ###################
    //###                                           ###
	
    public void set_etat_courant(State state)
    {
        state_ = state;
    }
	
    public void set_tile_color(int x, int y, Color color)
    {
        matrix_.get(x,y).setColor(color);
        matrix_.joinNeighbours(x,y);
    }

    public void colorise_tile(Coordinate coord, Color color)
    {
        set_tile_color(coord.x(), coord.y(), color);
    }
        

    
    //#####################################################
    //####################### tests #######################
    //###                                               ###

	public boolean relie_composantes(Coordinate coord, Color c)
	{
		return(matrix_.relie_composantes(coord.x(), coord.y(), c));
	}

    public boolean has_neighbour(Color c, int x, int y)  //une fonction qui du coup n'est plus forcément utile vu qu'on peut colorer une case n'importe ou
    {
        return(matrix_.has_neighbour(c,x,y));
    }
	
    public boolean coordinates_are_valid(int x, int y)
    {
        return(x >= 0 && y >= 0 && x < params_.boardSize() && y <  params_.boardSize());
    }

    public boolean coordinates_are_valid(Coordinate coord)
    {
        return coord.x() >= 0 && coord.x() < params_.boardSize()
            && coord.y() >= 0 && coord.y() < params_.boardSize();
    }


    //#####################################################
    //##################### other stuff ###################
    //###                                               ###
    
    public void fillRect(int x, int y , int width, int height, Graphics g)
    {
        g.fillRect(x,y,width, height);
    }
	
    public void next_turn()
    {
        ++turn;
    }
	
    public int nb_etoiles(int x, int y) //
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
            //~ if(ret > bluescore_)
				//~ bluescore_ = ret;
        }
        return ret;
    }

    public int nb_etoiles(Coordinate coord)
    {
        return nb_etoiles(coord.x(), coord.y());
    }

    //#############################################################
    //###################### graphical stuff ######################
    //###                                                       ###
	
	public void checkwin(int x, int y)
	{
	
			int tmp= nb_etoiles(x,y);
	        if(tmp == params_.starCardinal())
            {
                JOptionPane.showMessageDialog(this, 
                                              (get_tile(x,y).getColor() == Color.RED ? "Rouge a gagné !" : "Bleu a gagné !"),
                                              " Fin du game",
                                              JOptionPane.INFORMATION_MESSAGE);
            }
            if( get_tile(x,y).getColor() == Color.RED && tmp > redscore_)
				redscore_ = tmp;
			else if(get_tile(x,y).getColor() == Color.BLUE && tmp > bluescore_)
				bluescore_ = tmp;
	}
	
    public void paintComponent(Graphics g)
    {
        g.setFont(params_.star_font());
        state_.paintComponent(g);
    }

    public void draw_tile(Graphics g, int xval, int yval)
    {
        g.fillRect(
            xval * params_.tileSize() + params_.borderSize() * xval,
            yval * params_.tileSize() + params_.borderSize() * yval,
            params_.tileSize(),
            params_.tileSize() );//drawing the background
        
        if(matrix_.get(xval, yval).isStarTile())//draw a star, if needed
        {
            g.setColor(Color.BLACK);
            g.drawString(
                "*",
                xval * params_.comprehensiveTileSize() + params_.tileSize() / 3,
                yval * params_.comprehensiveTileSize() + params_.tileSize());
        }
    }

    //############################################################
    //############### MouseListener implementation ###############
    //###                                                      ###
	 
    public void mousePressed(MouseEvent e)
    {
        state_.process_event(e);
    }
    public void mouseReleased(MouseEvent e) {
 
    }
     
    public void mouseEntered(MouseEvent e) {

    }
     
    public void mouseExited(MouseEvent e) {

    }
     
    public void mouseClicked(MouseEvent e) {

    }

    //######################################################
    //########### private initialisation methods ###########
    //###                                                ###
 
    private void initialise_star_tiles()
    {
        int tmp_x;
        int tmp_y;
        Random rng = new Random();
        for(int i = 0 ; i < params_.starCardinal() ; ++i)
        {
            while(matrix_.get(tmp_x = rng.nextInt(matrix_.size()), tmp_y = rng.nextInt(matrix_.size())) != null){}
			
            add_red_star_tile(tmp_x, tmp_y);
            checkwin(tmp_x, tmp_y);
        }
        for(int i = 0 ; i < params_.starCardinal() ; ++i)
        {
            while(matrix_.get(tmp_x = rng.nextInt(matrix_.size()), tmp_y = rng.nextInt(matrix_.size())) != null){}
			
            add_blue_star_tile(tmp_x, tmp_y);
            checkwin(tmp_x, tmp_y);
        }
    }

    private void initialise_empty_tiles()
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

    private void add_red_star_tile(int x, int y)
    {
        matrix_.set(x, y, new StarTile(Color.RED));
        red_star_tiles.add(new int[]{x,y});
        matrix_.joinNeighbours(x,y);
    }
	
    private void add_blue_star_tile(int x, int y)
    {
        matrix_.set(x, y, new StarTile(Color.BLUE));
        blue_star_tiles.add(new int[]{x,y});
        matrix_.joinNeighbours(x,y);
    }//todo: generalise that. maybe.
}
