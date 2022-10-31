package LKManager;

import LKManager.controllers.Options;

public  class algorytmKomiwojazer {
    int MAXINT = 2147483647;
    int n, m, v0, d, dh, sptr, shptr;
    // Macierz sąsiedztwa

    // Macierz wag krawędzi
    int[] S;
    int[] Sh;                     // Stosy w tablicy
                  // Tablica odwiedzin
                  boolean[][] A ;
    int[][] W ;

    boolean[] visited;

    public int[] getS() {
        return S;
    }

    public algorytmKomiwojazer(Options.Macierze m  ) {



        this. A =m.getA();
        this.W =m.getW();
        this.n = m.getN();
       this.visited = new boolean [n];



      //  n=8;
    //    m=16;




        S       = new int [ n ];
        Sh      = new int [ n ];


        sptr = shptr = 0;


        // Odczytujemy dane wejściowe
      /*  A[0][1]= A[1][0]=true;
        W[0][1]=W[1][0]=2;

        A[0][2]= A[2][0]=true;
        W[0][2]=W[2][0]=2;

        A[0][3]= A[3][0]=true;
        W[0][3]=W[3][0]=4;

        A[0][4]= A[4][0]=true;
        W[0][4]=W[4][0]=3;

        A[1][2]= A[2][1]=true;
        W[1][2]=W[2][1]=2;

        A[1][5]= A[5][1]=true;
        W[1][5]=W[5][1]=1;

        A[1][6]= A[6][1]=true;
        W[1][6]=W[6][1]=1;

        A[2][4]= A[4][2]=true;
        W[2][4]=W[4][2]=2;

        A[2][5]= A[5][2]=true;
        W[2][5]=W[5][2]=1;

        A[3][5]= A[5][3]=true;
        W[3][5]=W[5][3]=2;

        A[3][7]= A[7][3]=true;
        W[3][7]=W[7][3]=3;

        A[4][6]= A[6][4]=true;
        W[4][6]=W[6][4]=4;

        A[4][7]= A[7][4]=true;
        W[4][7]=W[7][4]=5;

        A[5][6]= A[6][5]=true;
        W[5][6]=W[6][5]=2;

        A[5][7]= A[7][5]=true;
        W[5][7]=W[7][5]=2;

        A[6][7]= A[7][6]=true;
        W[6][7]=W[7][6]=2;
*/

        d  = MAXINT;
        dh = v0 = 0;
    //    this.wykonaj();
    }

    public void wykonaj()
    {




        TSP( v0 );
        if( sptr>0 )
        {
            for(int i = 0; i < sptr; i++ )
            {

                System.out.println(S [ i ]+ " -");
                  if(i%2!=0)
            {
                System.out.println("para= "+S [i-1 ]+" z "+S [ i ]);
            }
              //  System.out.println(v0+"\n");


            }
            System.out.println("d = " +d);
          /*  if(i%2)
            {

            }*/

        }
else
        System.out.println("NO HAMILTONIAN CYCLE \n" );

    }




    private    void TSP (int v ) {

        int u;
        Sh[shptr] = v; // zapamiętujemy na stosie bieżący wierzchołek
      shptr++; //???

        if (shptr < n)
        {
            visited[v] = true;
            for (u = 0; u < n ; u++)
                if (A[v][u] && !visited[u])
                {
                    dh += W[v][u];
                    TSP(u);
                    dh -= W[v][u];
                }
                visited[v] = false;

        }
        else if (A[v0][v])         // Jeśli znaleziona ścieżka jest cyklem Hamiltona
        {
            dh += W[v][v0];           // to sprawdzamy, czy ma najmniejszą sumę wag
            if (dh < d)                   // Jeśli tak,
            {
                d = dh;                      // To zapamiętujemy tę sumę
                for (u = 0; u < shptr; u++) // oraz kopiujemy stos Sh do S
                    S[u] = Sh[u];
                sptr = shptr;
            }
            dh -= W[v][v0];           // Usuwamy wagę krawędzi v-v0 z sumy
        }
        shptr--;                         // Usuwamy bieżący wierzchołek ze ścieżki
    }

}
