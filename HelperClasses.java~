

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
        border_size_ = 5;
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

class Coordinate
{
    private int x_;
    private int y_;
	
    public Coordinate(int x, int y)
    {
        x_ = x;
        y_ = y;
    }
	
    public int x() { return x_; }
    public int y() { return y_; }
}
