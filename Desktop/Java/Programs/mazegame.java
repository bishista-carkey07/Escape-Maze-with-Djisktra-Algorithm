import java.util.Random;
import java.util.Scanner;

public class mazegame 
{
    static String[][][] box;
    static void createGrid(int r,int c)
    {
        box = new String[r][c][2];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                box[i][j][0] = "|     ";
                box[i][j][1] = "|_____";
            }
        }

    }
    
    static int grid(int r,int c)
    {
        box[0][0][0]="|Start";
        box[r-1][c-1][0]="| End ";
        int i,j;
        for(j=0;j<c;j++)
        {
            System.out.print(" _____");
        }
        System.out.println();
        for(i=0;i<r;i++)
        {
            StringBuilder line1= new StringBuilder();
            StringBuilder line2= new StringBuilder();
            for(j=0;j<c;j++)
            {
                line1.append(box[i][j][0]);
                line2.append(box[i][j][1]);
            }
            line1.append("|");
            line2.append("|");

            System.out.print(line1+"\n");
            System.out.print(line2+"\n");
        } 
        return 0;
    }
    
    static int possible_paths(int i,int j,int [][] trap_map)
    {
        if (i < 0 || j < 0)
        {
            return 0;
        }
        if (trap_map[i][j] == 0)
        {
            return 0;
        }
        if (i == 0 && j == 0)
        {
            return 1;
        }

        return possible_paths(i - 1, j, trap_map) + possible_paths(i, j - 1, trap_map);
    }
    
    static int shortestSafePath(int[][] trap_map, int r, int c, int[][][] parent) {
        int[][] dist = new int[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (trap_map[i][j] == 0)
                    dist[i][j] = Integer.MAX_VALUE;
                else
                    dist[i][j] = Integer.MAX_VALUE;
                parent[i][j][0] = -1;
                parent[i][j][1] = -1;
            }
        }

        dist[0][0] = 0;

        for (int k = 0; k < r + c; k++) {
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    if (trap_map[i][j] == 0 || dist[i][j] == Integer.MAX_VALUE)
                        continue;

                    // Move right
                    if (j + 1 < c && trap_map[i][j + 1] == 1 && dist[i][j] + 1 < dist[i][j + 1]) {
                        dist[i][j + 1] = dist[i][j] + 1;
                        parent[i][j + 1][0] = i;
                        parent[i][j + 1][1] = j;
                    }

                    // Move down
                    if (i + 1 < r && trap_map[i + 1][j] == 1 && dist[i][j] + 1 < dist[i + 1][j]) {
                        dist[i + 1][j] = dist[i][j] + 1;
                        parent[i + 1][j][0] = i;
                        parent[i + 1][j][1] = j;
                    }
                }
            }
        }
        return dist[r - 1][c - 1];
    }

    public static void main(String[] args) 
    {
        Scanner sc= new Scanner(System.in);
        Random rand=new Random();
        System.out.println("Enter the number of rows and column for the grid: ");
        int r,c,i,j;
        r=sc.nextInt();
        c=sc.nextInt();
        createGrid(r, c);
        int trap_map[][]=new int[r][c];
        for(i=0;i<r;i++)
        {
            for(j=0;j<c;j++)
            {
                trap_map[i][j]=1;
            }
        }

        int count_boxes=0,limit;
        for(i=0;i<r;i++)
        {
            for(j=0;j<c;j++)
            {
                count_boxes++;
            }
        }
        limit=(int)(0.3*count_boxes);

        int num_of_traps=rand.nextInt(limit);
        if(num_of_traps< ((int)(0.15*count_boxes)) )
        {
            num_of_traps=num_of_traps+(int)0.15*count_boxes;
        }

        int trap_count=0;
        int[] row_trap=new int[limit];
        int[] col_trap=new int[limit];
        i=0;
        j=0;
        while(trap_count<limit)
        {
            row_trap[trap_count]=rand.nextInt(r);
            col_trap[trap_count]=rand.nextInt(c);
            if((row_trap[trap_count]==0 && col_trap[trap_count]==0) || (row_trap[trap_count]==r-1 && col_trap[trap_count]==c-1) )
            {
                continue;
            }
            else if((row_trap[trap_count]==r-1 && col_trap[trap_count]==c-2) && (row_trap[trap_count]==r-2 && col_trap[trap_count]==c-1))
            {
                continue;
            }
            else if((row_trap[trap_count]==0 && col_trap[trap_count]==1) && (row_trap[trap_count]==1 && col_trap[trap_count]==0))
            {
                continue;
            }
            else
            {
                box[row_trap[trap_count]][col_trap[trap_count]][1]="|__X__";
                trap_map[row_trap[trap_count]][col_trap[trap_count]]=0;
            }
            trap_count++;
        } 

        for ( i = 0; i < trap_count; i++) 
        {
            if (row_trap[i] + 1 < r && row_trap[i]>=0 &&col_trap[i] - 1 >= 0 && col_trap[i]<c)
            {
                if (box[row_trap[i]][col_trap[i]][1].equals("|__X__") && box[row_trap[i] + 1][col_trap[i] - 1][1].equals("|__X__")) 
                {
                    if (rand.nextBoolean()) 
                    {
                        box[row_trap[i]][col_trap[i]][1] = "|_____";
                        trap_map[row_trap[i]][col_trap[i]] = 1;
                    }
                    else 
                    {
                        box[row_trap[i] + 1][col_trap[i] - 1][1] = "|_____";
                        trap_map[row_trap[i] + 1][col_trap[i] - 1]=1;
                    }
                }
            }
        }

        // Display maze with traps first
        grid(r,c);
        System.out.println("\n\n");

        int no_of_Paths=possible_paths(r-1, c-1, trap_map);
        System.out.println("There are total "+no_of_Paths+" of paths to reach the 'End' from the 'Start'. ");

        int[][][] parent = new int[r][c][2];
        int shortestPath=shortestSafePath(trap_map, r, c, parent);

        int x = r - 1, y = c - 1;
        while (!(x == 0 && y == 0)) {
            int px = parent[x][y][0];
            int py = parent[x][y][1];
            if (px == -1 || py == -1) break;

            if (px == x && py < y) // right move
                box[x][y][0] = "| ..  ";
            else if (py == y && px < x) // down move
                box[x][y][0] = "|  :  ";

            x = px;
            y = py;
        }
        grid(r,c);
    }   
}
