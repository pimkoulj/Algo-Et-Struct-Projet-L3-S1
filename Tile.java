import java.awt.Color;
class Tile
{
	protected Color color_;
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
