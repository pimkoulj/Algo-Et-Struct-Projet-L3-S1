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
    
    public boolean coordinates_are_valid(int x, int y)
    {
        return(x >= 0 && y >= 0 && x < size() && y < size());
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
    
	public void joinNeighbours(int x, int y)
    {
		Color c = tab_[x][y].getColor();
        for(int i = (x == 0 ? 0 : x - 1); i <= (x == size() - 1 ? size() - 1 : x + 1) ; ++i)
            for(int j = (y == 0 ? 0 : y - 1); j <= (y == size() - 1 ? size() - 1 : y + 1) ; ++j)
                if(tab_[i][j] != null && tab_[i][j].getColor() == c)
                {
                    tab_[x][y].union(tab_[i][j]);
				}
    }
    
    public void afficher()
    {
		  for(int i = 0 ; i < tab_.length ; ++i)
        {
            for(int j = 0 ; j < tab_.length ; ++j)
            {
				System.out.print((tab_[j][i].nbFils()) + " ");
			}
			System.out.println("");
		}
	}
}
