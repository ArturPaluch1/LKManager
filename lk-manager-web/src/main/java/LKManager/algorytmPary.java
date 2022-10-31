package LKManager;

import LKManager.controllers.LKsystemSzwajcarski.Mecz;
import LKManager.controllers.Options;

import java.util.*;

public class algorytmPary {
    private final List<Integer> wejscie;

    // kojarzeniePar.cpp : This file contains the 'main' function. Program execution begins and ends there.
//



/*    struct slistEl
    {
        slistEl* next;
        int v;
    };*/

 int MAXINT = -2147483647;

    // Definicja typu obiektowego queue
//---------------------------------

// **********************
// *** PROGRAM GŁÓWNY ***
// **********************

class slistEl
{
    slistEl next;
    int v;
}

    //   Options.GrajekPrzeciwnicy[] tablica = tab;
    Queue<Integer> q
            = new LinkedList<>();


    int n, m, i, v, x, y;// * matching, * augment;
    int[]matching;
    int[]augment;
    //  slistEl* p, * r, ** graf;
    List<Options.GrajekTurniejowy> listaGrajekTurniejowy;

public algorytmPary(List<Integer> wejscie, List<Options.GrajekTurniejowy> lgt) {
this.n= lgt.size()*2;
this.wejscie= wejscie;
this.listaGrajekTurniejowy=lgt;
}

public  List<Mecz> ZrobTerminarz(int runda)
{
    m=wejscie.size();


    List<Integer> wejscie1= Arrays.asList(new Integer[]{0 ,8 ,0 ,7,
            1 ,8 ,1, 6, 1, 5,
            2, 5,
            3, 9 ,3 ,8, 3, 6,
            4, 8, 4 ,7});
    //  n=wejscie.size();

//n=10;


    slistEl p;
    slistEl r;

    slistEl[]graf;
    boolean[] visited,color;
    Queue <Integer> Q = new LinkedList<Integer>();

    //    bool* visited, * color;
    //   queue Q;
//todo
    // cin >> n >> m;                  // Czytamy liczbę wierzchołków i krawędzi
    //   n=wejscie.size();
    // m=11;
    // Tworzymy zmienne dynamiczne

    color = new boolean[n];      // Kawalerowie ( true ), panny ( false )
    matching = new int[n];       // Skojarzenia
    augment = new int[n];       // Ścieżka rozszerzająca
    visited = new boolean[n];      // Odwiedziny
    // graf = new slistEl * [n]; // Tworzymy tablicę list sąsiedztwa
    graf = new slistEl[n]; // Tworzymy tablicę list sąsiedztwa
    // Tablicę wypełniamy pustymi listami

    for (i = 0; i < n; i++) graf[i] = null;

    // Odczytujemy kolejne definicje krawędzi








    for (i = 0; i < m; i++) {
        if (i % 2 != 0&&i>0) {

            //todo
            //   cin >> x >> y;              // Krawędź panna --- kawaler
            p = new slistEl();         // Tworzymy nowy element
            p.v = wejscie.get(i);       // Numerujemy go jako kawaler
            p.next = graf[wejscie.get(i - 1)];
            //  p->next = graf[x];          // Dodajemy go na początek listy panny
            graf[wejscie.get(i - 1)] = p;
            p = new slistEl();            // Tworzymy nowy element
            p.v = wejscie.get(i - 1);                   // Numerujemy go jako pannę
            p.next = graf[wejscie.get(i)];       // Dodajemy go na początek listy kawalera
            graf[wejscie.get(i)] = p;
            color[wejscie.get(i - 1)] = false;        // Panna
            color[wejscie.get(i)] = true;         // Kawaler
        }
    }
    //     cout << endl;

    // Algorytm znajdowania maksymalnego skojarzenia

    for (i = 0; i < n; i++)       // Elementy tablicy matching ustawiamy na -1
        matching[i] = -1;         // Co oznacza brak skojarzenia

    for (v = 0; v < n; v++)       // Przechodzimy przez kolejne wierzchołki
        if ((matching[v] == -1) && !color[v])
        {
            for (i = 0; i < n; i++)
                visited[i] = false;   // Zerujemy tablicę odwiedzin

            while (!Q.isEmpty ()) Q.remove(); // Zerujemy kolejkę

            visited[v] = true;      // Oznaczamy v jako wierzchołek odwiedzony
            augment[v] = -1;        // Poprzednikiem v jest korzeń drzewa rozpinającego
            Q.add(v);              // Umieszczamy v w kolejce

            while (!Q.isEmpty())       // Uruchamiamy BFS
            {
                x = Q.peek();          // Pobieramy x z kolejki
                Q.remove();                // Pobrany wierzchołek usuwamy z kolejki

                if (color[x])
                {                        // Kawalerowie
                    if (matching[x] == -1)
                    {                      // Kawaler wolny
                        while (augment[x] > -1)
                        {
                            if (color[x])
                            {                  // Zamieniamy krawędzie skojarzone z nieskojarzonymi
                                matching[x] = augment[x];
                                matching[augment[x]] = x;
                            }
                            x = augment[x]; // Cofamy się po ścieżce rozszerzającej
                        }
                        break;               // Wracamy do pętli głównej
                    }
                    else
                    {                      // Kawaler skojarzony
                        augment[matching[x]] = x;
                        visited[matching[x]] = true;
                        Q.add(matching[x]); // W kolejce umieszczamy skojarzoną pannę

                    }
                }
                else
                {                        // Panny
                    p = graf[x];        // Przeglądamy kawalerów
                    while (p!=null)
                    {
                        y = p.v;            // Numer kawalera
                        if (!visited[y]) // Tylko kawalerowie nieskojarzeni
                        {                    // są umieszczani w kolejce
                            visited[y] = true;
                            augment[y] = x; // Tworzymy ścieżkę rozszerzającą
                            Q.add(y);
                        }
                        p = p.next;
                    }
                }
            }
        }

    // Wyświetlamy skojarzenia panna --- kawaler
List<Mecz>meczeList= new ArrayList<>();
    for (i = 0; i < n; i++)
        if (!color[i]) System.out.println(i+" --- "+matching[i]+"\n");



    for (int i = 0; i < matching.length; i++) {



        Mecz tempMecz= new Mecz(listaGrajekTurniejowy.get(i),listaGrajekTurniejowy.get( matching[i]-listaGrajekTurniejowy.size()),runda);
        meczeList.add(tempMecz);
        listaGrajekTurniejowy.get(i).getListaPrzeciwnikow().add(listaGrajekTurniejowy.get( matching[i]-listaGrajekTurniejowy.size()).getGrajek().getUsername());
        //   listaGrajekTurniejowy.get(i+1).getListaPrzeciwnikow().add(listaGrajekTurniejowy.get( matching[i+1]-listaGrajekTurniejowy.size()).getGrajek().getUsername());
    }
return meczeList;

}

}
