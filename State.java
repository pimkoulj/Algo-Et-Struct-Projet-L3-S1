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
import java.lang.Math.*;

abstract class State
{
    protected Board target_;
    protected GameParameters params_;
	
    public State(Board target)
    {
        target_ = target;
        params_ = target.params();
    }
	
    public void	switch_to_state()
    {
        reset();
        target_.set_etat_courant(this);
    }
    public abstract void process_event(MouseEvent e);
    public void reset(){}
    public abstract void paintComponent(Graphics g);

    protected void draw_background(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, 10000, 10000);
    }
}

abstract class MouseOverState extends State
{

    public MouseOverState(Board target)
    {
        super(target);
    }
    
    public void paintComponent(Graphics g)
    {
        draw_background(g);
        
        Coordinate mouse_coord = target_.locate_mouse();
        boolean mouseover_valid = target_.coordinates_are_valid(mouse_coord);
		
        for(int i = 0 ; i < params_.matrixSize() ; ++i)
            for(int j = 0 ; j < params_.matrixSize() ; ++j)
            {
                g.setColor(target_.get_tile(i, j).getColor());
                if( mouseover_valid
                    && mouse_coord.equals(i, j)
                    && target_.tile(mouse_coord).isEmpty() )
                { g.setColor(mouseover_color()); }
                
                target_.draw_tile(g, i, j);
            }
    }
    protected abstract Color mouseover_color();
}

class JoueDeuxHumains extends MouseOverState
{
    public JoueDeuxHumains(Board target)
    {
        super(target);
    }
    
    public void process_event(MouseEvent e)
    {
        Coordinate click_pos = target_.locate(e);
        
        if(target_.coordinates_are_valid(click_pos) && target_.tile(click_pos).isEmpty())
        {
            target_.colorise_tile(click_pos, target_.turn_color());
            target_.next_turn();
            target_.checkwin(click_pos.x(),click_pos.y());
        }
    }

    protected Color mouseover_color()
    {
        Color moc = target_.turn_color();
        return new Color(Math.min(moc.getRed() + 100, 255),
                         Math.min(moc.getGreen() + 100, 255),
                         Math.min(moc.getBlue() + 100,255),
                         255);
    }
    
}//class JoueDeuxHumains
	

class ColorerCase extends MouseOverState
{
    public ColorerCase(Board target)
    {
        super(target);
    }
    
    public void process_event(MouseEvent e)
    {
        Object[] options = {"Red","Blue"};
        int n = JOptionPane.showOptionDialog(
            target_,
            "selectionnez couleur ?",
            "Question",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        Color c = (n == 0) ? Color.RED : Color.BLUE; /////*******
        int x = e.getX() / (params_.tileSize() + params_.borderSize());
        int y = e.getY() / (params_.tileSize() + params_.borderSize());
        
        if(target_.coordinates_are_valid(x,y) && target_.get_tile(x, y).isEmpty())
        {
            target_.set_tile_color(x,y,c);
			target_.next_turn();
			target_.checkwin(x,y);
        }
    }

    protected Color mouseover_color()
    {
        return new Color(200,200,200,100);
    }

}//class ColorerCase

class NombreEtoiles extends MouseOverState
{
    public NombreEtoiles(Board target)
    {
        super(target);
    }
    
    public void process_event(MouseEvent e)
    {
        int x = e.getX() / (params_.tileSize() + params_.borderSize());
        int y = e.getY() / (params_.tileSize() + params_.borderSize());
        JOptionPane.showMessageDialog(
        target_,
        "Cette composante possède " + target_.nb_etoiles(x,y) + " cases étoiles");    
    }

    protected Color mouseover_color()
    {
        return new Color(200,200,200,100);
    }

}

class AfficheComposante extends State
{
    private int x;
    private int y;
    public AfficheComposante(Board target)
    {
        super(target);
        x =  -1;
        y =  -1;
    }
    public void reset()
    {
        x = -1;
        y = -1;
    }
    public void process_event(MouseEvent e)
    {
        x = e.getX() / (params_.tileSize() + params_.borderSize());
        y = e.getY() / (params_.tileSize() + params_.borderSize());
    }
    public void paintComponent(Graphics g)
    {
        draw_background(g);
        Color greyed = new Color(0,0,0,180);
        final Point mousePos = target_.getMousePosition();
        Font font = new Font("Serif", Font.BOLD, params_.tileSize() );
        g.setFont(font);
        for(int i = 0 ; i < params_.matrixSize() ; ++i)
        {
            for(int j = 0 ; j < params_.matrixSize() ; ++j)
            {
                g.setColor(target_.get_tile(i, j).getColor());
                //target_.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize(), g);
                target_.fillRect(
                    i*params_.comprehensiveTileSize(),
                    j*params_.comprehensiveTileSize(),
                    params_.tileSize(),
                    params_.tileSize(),
                    g);

                if(target_.get_tile(i,j).isStarTile())
                {
                    g.setColor(Color.BLACK);
                    g.drawString("*",i * params_.comprehensiveTileSize() + params_.tileSize() / 3, j*params_.comprehensiveTileSize() + params_.tileSize() / 1);
                }
                if(target_.coordinates_are_valid(x,y) && !target_.get_tile(i,j).memeClasse(target_.get_tile(x,y)))
                {
                    g.setColor(greyed);
                    target_.fillRect(i*params_.tileSize() + params_.borderSize() * i, j*params_.tileSize() + params_.borderSize() * j, params_.tileSize(), params_.tileSize(), g);
                }
            }
        }
    }
}


//~ class RelierCasesMin extends State
//~ {
    //~ private int clicked_;
    //~ private int[][] tab_;
//~ 
    //~ Coordinate case_origine_;
    //~ Coordinate case_destination_;
//~ 
    //~ private ArrayList<Coordinate> origine_;
    //~ private ArrayList<Coordinate> destination_; 
	//~ 
    //~ public RelierCasesMin(Board target)
    //~ {
        //~ super(target);
        //~ clicked_ = 0;
        //~ tab_ = new int[params_.matrixSize()][params_.matrixSize()];
        //~ origine_ = new ArrayList<Coordinate>();
        //~ destination_ = new ArrayList<Coordinate>();
    //~ }
	//~ 
    //~ public void reset()
    //~ {
        //~ origine_.clear();
        //~ destination_.clear();
        //~ clicked_ = 0;
    //~ }
    //~ public void process_event(MouseEvent e)
    //~ {
        //~ if(clicked_ == 0)
        //~ {
            //~ handle_first_click(e);
        //~ }
        //~ else if(clicked_ == 1)
        //~ {
            //~ case_destination_ = target_.locate(e);
            //~ if(target_.tile(case_origine_).getColor() == target_.tile(case_destination_).getColor())
            //~ {
                //~ target_.composantes(case_origine_, case_destination_).tie(origine_, destination_);
                //~ find_path();
                //~ clicked_ = 2;
            //~ }
            //~ // else if(target_.tile(case_origine_).isEmpty())
            //~ // {
            //~ //     clicked_ = 0;
            //~ // }
        //~ }
        //~ else//2 supposement
        //~ {
            //~ origine_.clear();
            //~ destination_.clear();
            //~ handle_first_click(e);
        //~ }
    //~ }
//~ 
    //~ private void handle_first_click(MouseEvent e)
    //~ {
        //~ case_origine_ = target_.locate(e);
        //~ if(!target_.tile(case_origine_).isEmpty())
            //~ clicked_ = 1;
    //~ }
    //~ 
    //~ private void find_path()
    //~ {
        //~ boolean path_was_found = false;
//~ 
        //~ init_tab();
        //~ 
        //~ int indice = 0;
        //~ int end;
        //~ int length = 1;
        //~ while(!path_was_found)
        //~ {
            //~ end = origine_.size();
            //~ for(; indice < end; ++indice)
            //~ {
                //~ int x = origine_[indice].x();
                //~ int y = origine_[indice].y();
//~ 
                //~ for(int i = (x == 0 ? 0 : x - 1);
                    //~ i <= (x == params_.matrixSize() - 1 ? params_.matrixSize() - 1 : x + 1); ++i)
                    //~ for(int j = (y == 0 ? 0 : y - 1);
                        //~ j <= (y == params_.matrixSize() - 1 ? params_.matrixSize() - 1 : y + 1) ; ++j)
                    //~ {
                        //~ if(tab_[x][y] == 0)
                        //~ {
                            //~ tab_[x][y] = length;
                            //~ origine_.add(new Coordinate(x, y));
                        //~ }
                        //~ else if(tab_[x][y] == -1)
                        //~ {
                            //~ path_was_found = true;
                            //~ goto fin;
                        //~ }
                    //~ }
            //~ }
            //~ ++length;
        //~ }
    //~ }
//~ 
    //~ private void init_tab()
    //~ {
        //~ for(Coordinate el : origine_)
            //~ tab_[el.x()][el.y()] = 1;
//~ 
        //~ for(Coordinate el : destination_)
            //~ tab[el.x()][el.y()] = -1;
    //~ }
    //~ 
    //~ private void wipe_tab()
    //~ {
        //~ for(int i = 0; i < params.matrixSize(); ++i)
            //~ for(int j = 0; j < params.matrixSize(); ++j)
                //~ tab[i][j] = 0;
    //~ }
//~ 
    //~ public void paintComponent(Graphics g)
    //~ {
        //~ draw_background(g);
        //~ 
        //~ Color greyed = new Color(0,0,0,180);
        //~ final Point mousePos = target_.getMousePosition();
        //~ Font font = new Font("Serif", Font.BOLD, params_.tileSize() );
        //~ g.setFont(font);
        //~ for(int i = 0 ; i < params_.matrixSize() ; ++i)
        //~ {
            //~ for(int j = 0 ; j < params_.matrixSize() ; ++j)
            //~ {
                //~ 
            //~ }
        //~ }
    //~ }
//~ }
