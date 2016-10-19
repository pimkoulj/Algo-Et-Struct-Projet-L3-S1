import java.awt.Color;
class Tile
{
    protected Color color_;
    protected Tile pere;
    public Tile()
    {
        color_ = Color.WHITE;
    }
	
    public Color getColor()
    {
        return(color_);
    }
	
    public void setColor(Color col)
    {
        color_ = col;
    }
	
    public boolean isStarTile()
    {
        return (false);
    }

    public boolean isEmpty()
    {
        return color_ == Color.WHITE;
    }
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
