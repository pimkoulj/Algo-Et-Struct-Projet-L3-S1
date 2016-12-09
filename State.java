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

abstract class PaintState extends State
{

    public PaintState(Board target)
    {
        super(target);
    }
    
    public void paintComponent(Graphics g)
    {
        draw_background(g);
		
        for(int i = 0 ; i < params_.matrixSize() ; ++i)
            for(int j = 0 ; j < params_.matrixSize() ; ++j)
            {
                g.setColor(target_.get_tile(i, j).getColor());
                target_.draw_tile(g, i, j);
            }
    }

}

class AfficherScore extends State
{
    public AfficherScore(Board target)
    {
        super(target);
    }
	
    public void	switch_to_state()
    {
        JOptionPane.showMessageDialog(
            target_,
            "Score de Rouge : " + target_.get_red_score() + "\n Score de Bleu : " + target_.get_blue_score());   
    }
    public void process_event(MouseEvent e){}
    public void reset(){}
    public void paintComponent(Graphics g){}
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

class RelieComposantes extends MouseOverState
{
    public RelieComposantes(Board target)
    {
        super(target);
    }
    
    public void process_event(MouseEvent e)
    {
		Coordinate tmp = target_.locate(e);
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
        Color c = (n == 0) ? Color.RED : Color.BLUE;
        boolean reliecomposantes = target_.relie_composantes(tmp, c);
		JOptionPane.showMessageDialog(
        target_,
        (reliecomposantes ? "Oui, colorer cette case relierait deux composantes" : "Non, colorer cette case ne relireait pas deux composantes"));  
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
        g.setFont(params_.star_font());
        for(int i = 0 ; i < params_.matrixSize() ; ++i)
        {
            for(int j = 0 ; j < params_.matrixSize() ; ++j)
            {
                g.setColor(target_.get_tile(i, j).getColor());
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


abstract class Pathfinder extends PaintState
{
    protected int clicked_;
    protected int[][] tab_;

    protected Coordinate case_origine_;
    protected Coordinate case_destination_;

    public static final int VIDE = -1;
    public static final int DESTINATION = -2;
    public static final int ORIGINE = 0;
    public static final int OBSTACLE = -3;
    public static final int SAME_COLOR = -4;
    
    protected int path_length_;
    protected boolean path_was_found_;

    protected abstract Color path_color();
	
    public Pathfinder(Board target)
    {
        super(target);
        tab_ = new int[params_.matrixSize()][params_.matrixSize()];
        reset();
    }
	
    public void reset()
    {
        clicked_ = 0;
        path_was_found_ = false;
    }
    public void process_event(MouseEvent e)
    {
        if(clicked_ == 0)
        {
            handle_first_click(e);
        }
        else if(clicked_ == 1)
        {
            handle_second_click(e);

        }
        else//2 supposement
        {
            handle_first_click(e);
        }
    }

    protected abstract void handle_first_click(MouseEvent e);
    protected abstract void handle_second_click(MouseEvent e);
    
    protected void find_path()
    {
        path_length_ = 0;

        if (target_.tile(case_origine_).memeClasse(target_.tile(case_destination_)))//origine et destination sont déjà reliés
        {
            path_was_found_ = true;
            return;
        }

        path_was_found_ = false;
            
        ArrayList<Coordinate> origine = origine_composante();
        ArrayList<Coordinate> destination = destination_composante();

        boolean path_was_extended = true;

        init_tab(origine, destination);
        
        while(!path_was_found_ && path_was_extended)
        {
            ArrayList<Coordinate> exterieur = new ArrayList<Coordinate>();
            int end = origine.size();
            path_was_extended = false;
            ++path_length_;
            for(int indice = 0; indice < end; ++indice)
            {
                int x = origine.get(indice).x();
                int y = origine.get(indice).y();
                
                for(int i = (x == 0 ? 0 : x - 1);
                    i <= (x == params_.matrixSize() - 1 ? params_.matrixSize() - 1 : x + 1); ++i)
                    for(int j = (y == 0 ? 0 : y - 1);
                        j <= (y == params_.matrixSize() - 1 ? params_.matrixSize() - 1 : y + 1) ; ++j)
                    {
                        if(tab_[i][j] == VIDE)//case vide, on l'ajoute au ruban extérieur
                        {
                            tab_[i][j] = path_length_;
                            exterieur.add(new Coordinate(i, j));
                            path_was_extended = true;
                        }
                        else if(tab_[i][j] == SAME_COLOR)
                        {
                            ArrayList<Coordinate> tmp = target_.composante(new Coordinate(i,j));
                            for(Coordinate coord : tmp)
                            {
                                tab_[coord.x()][coord.y()] = path_length_-1;
                                origine.add(new Coordinate(coord.x(),coord.y()));
                                ++end;
                            }
                        }
                        else if(tab_[i][j] == DESTINATION)
                        {
                            path_was_found_ = true;
                        }
                    }
                //System.out
            }
            origine = exterieur;
            //afficher_matrice();
        }

        path_length_-=1;
    }

    protected abstract ArrayList<Coordinate> origine_composante();
    protected abstract ArrayList<Coordinate> destination_composante();

    private void init_tab(ArrayList<Coordinate> origine, ArrayList<Coordinate> destination)
    {
        for( int i = 0; i < params_.matrixSize(); ++i )
            for(int j = 0; j < params_.matrixSize(); ++j)
            {
                Color col = target_.get_tile(i, j).getColor();
                if(col == Color.WHITE)
                    tab_[i][j] = VIDE;
                else if(col == path_color())
                    tab_[i][j] = SAME_COLOR;
                else
                    tab_[i][j] = OBSTACLE;

            }
        
        for(Coordinate el : origine)
            tab_[el.x()][el.y()] = ORIGINE;

        for(Coordinate el : destination)
            tab_[el.x()][el.y()] = DESTINATION;
        
    }
    
    private void wipe_tab()
    {
        for(int i = 0; i < params_.matrixSize(); ++i)
            for(int j = 0; j < params_.matrixSize(); ++j)
                tab_[i][j] = 0;
    }

    private void afficher_matrice()
    {
        System.out.println("");
        for(int j = 0; j < tab_.length; ++j)
            System.out.print("________");
        System.out.println("");
        

        for(int i = 0; i < tab_.length; ++i)
        {
            // for(int j = 0; j < tab_.length; ++j)
            //     System.out.print("|\t");
            // System.out.println("|");

            for(int j = 0; j < tab_.length; ++j)
                System.out.print("|\t");
            System.out.println("|");


            for(int j = 0; j < tab_.length; ++j)
                System.out.print("|   " + tab_[j][i] + "\t");
            System.out.println("|");

            for(int j = 0; j < tab_.length; ++j)
                System.out.print("|_______");
            System.out.println("|");
        }
    }
}

class RelierCasesMin extends Pathfinder
{
    public RelierCasesMin(Board target)
    {
        super(target);
    }

    protected ArrayList<Coordinate> origine_composante()
    {
        return target_.composante(case_origine_);
    }

    protected ArrayList<Coordinate> destination_composante()
    {
        return target_.composante(case_destination_);
    }


    protected Color path_color()
    {
        return target_.tile(case_origine_).getColor();
    }

    protected void handle_first_click(MouseEvent e)
    {
        case_origine_ = target_.locate(e);
        if(!target_.tile(case_origine_).isEmpty())
            clicked_ = 1;
    }

    protected void handle_second_click(MouseEvent e)
    {
        case_destination_ = target_.locate(e);
        if(target_.tile(case_origine_).getColor() == target_.tile(case_destination_).getColor() )
        {
            clicked_ = 2;
            reset();
            find_path();
            if(path_was_found_)
            {
                JOptionPane.showMessageDialog(target_, "Plus court chemin trouvé : " + path_length_);  
            }
            else
            {
                JOptionPane.showMessageDialog(target_, "Pas de chemin trouvé");  
            }
        }
    }
}

class ExisteCheminCases extends PaintState
{
    public static final int ROUGE = 0;
    public static final int BLEU = 1;

    protected Coordinate case_origine_;
    protected Coordinate case_destination_;
    private int clicked_;
    
    private int path_color_;
    public ExisteCheminCases(Board target)
    {
        super(target);
        clicked_ = 0;
    }

    public void process_event(MouseEvent e)
    {
        if(clicked_ == 0)
        {
            handle_first_click(e);
        }
        else if(clicked_ == 1)
        {
            handle_second_click(e);

        }
        else//2 supposement
        {
            handle_first_click(e);
        }
    }

    protected Color path_color()
    {
        if(path_color_ == BLEU)
            return Color.BLUE;
        return Color.RED;
    }

    protected void handle_first_click(MouseEvent e)
    {
        case_origine_ = target_.locate(e);
        clicked_ = 1;
    }

    protected void handle_second_click(MouseEvent e)
    {
        case_destination_ = target_.locate(e);
        clicked_ = 2;

        Object[] options = {"Rouge","Bleu"};
        int n = JOptionPane.showOptionDialog(
            target_,
            "selectionnez couleur ?",
            "Question",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        path_color_ = n;

        String colorS = (path_color_ == BLEU) ? "bleu" : "rouge";
        if(target_.tile(case_origine_).memeClasse(target_.tile(case_destination_)) && target_.tile(case_origine_).getColor() == path_color())
        {
            JOptionPane.showMessageDialog(target_, "Un chemin " + colorS + " existe");  
        }
        else
        {
            JOptionPane.showMessageDialog(target_, "Il n'existe pas de chemin " + colorS);  
        }

        reset();
    }
}
