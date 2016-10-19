import java.awt.Color; 

interface IMatrix
{
    Tile get(int x, int y);
    void set(int x, int y, Tile value);
    boolean hasNeighbour(Color c, int x, int y);
    int size();
}

class Matrix implements IMatrix
{
    private Tile[][] tab_;

    public Matrix(int size)
    {
        tab_ = new Tile[size][size];
    }

    public Tile get(int x, int y)
    {
        return tab_[x][y];
    }

    public void set(int x, int y, Tile value)
    {
        tab_[x][y] = value;
    }

    public int size()
    {
        return tab_.length;
    }
	
    public boolean hasNeighbour(Color c, int x, int y)
    {

        for(int i = (x == 0 ? 0 : x - 1); i <= (x == size() - 1 ? size() - 1 : x + 1) ; ++i)
            for(int j = (y == 0 ? 0 : y - 1); j <= (y == size() - 1 ? size() - 1 : y + 1) ; ++j)
                if(tab_[i][j].getColor() == c)
                    return(true);
        return(false);
    }
}
