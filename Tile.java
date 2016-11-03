import java.awt.Color;
class Tile
{
    protected Color color_;
    protected Tile pere;
    protected int nb_fils;
    public Tile()
    {
        color_ = Color.WHITE;
        nb_fils = 0;
    }
	
    public Color getColor()
    {
        return(color_);
    }
	
    public void setColor(Color col)
    {
        color_ = col;
    }
    
    public void setNbFils(int nb)
    {
        nb_fils = nb;
    }
	
    public void setPere(Tile tile)
    {
        pere = tile;
    }
	
    public boolean isStarTile()
    {
        return false;
    }

    public boolean isEmpty()
    {
        return color_ == Color.WHITE;
    }
    
    public Tile representant()
    {
        if(pere == null)
        {
            return(this);
        }
        else
        {
            pere = pere.representant();
        }
        return pere;
    }
	
    public boolean memeClasse(Tile tile)
    {
        return(representant() == tile.representant());
    }
	
    public void	union(Tile tile)
    {
        Tile rep_this = representant();
        Tile rep_tile = tile.representant();
        if(rep_this != rep_tile)
        {
            if(rep_this.nbFils() < rep_tile.nbFils())
            {
                rep_tile.setPere(this);
                tile.setNbFils(tile.nbFils() + nb_fils + 1);
            }
            else
            {
                rep_this.setPere(rep_tile);
                rep_tile.setNbFils(tile.nbFils() + nb_fils + 1);
            }
        }
    }
	
    public int nbFils()
    {
        return(nb_fils);
    }

    static final Color BLANC = Color.WHITE;
    static final Color ROUGE = Color.RED;
    static final Color BLEU = Color.BLUE;
}

class StarTile extends Tile
{
    public StarTile(Color color)
    {
        color_ = color;
    }	
    public boolean isStarTile()
    {
        return (true);
    }
}
