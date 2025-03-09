import java.util.*;

public class Hm1 {
    private boolean[][] graph;
    private int nodes;

    public Hm1(int n){
        nodes = n;
        graph = new boolean[n][n];
    }

    public void addEdge(int u, int v){
        graph[u][v] = true;
        graph[v][u] = true;
    }

    private boolean[][] getComplement(){
        boolean[][] complement = new boolean[nodes][nodes];
        for(int i = 0; i<nodes; i++){
            for(int j = 0; j < nodes; j++){
                if(i != j){
                    complement[i][j] = !graph[i][j];
                }
            }
        }
        return complement;
    }

    public boolean hasStableSet(int k){
        boolean[][] complement = getComplement();
        List<Set<Integer>> kClica = findKClica(complement, k);
        return !kClica.isEmpty();
    }

    private List<Set<Integer>> findKClica(boolean[][] matrix, int sizeK){
        List<Set<Integer>> cliques = new ArrayList<>();
        List<Set<Integer>> kCliques = new ArrayList<>();
        for(int i = 0; i < nodes; i++){
            for(int j = i + 1; j < nodes; j++){
                if(matrix[i][j]){
                    kCliques.add(new HashSet<>(Arrays.asList(i, j)));
                }
            }
        }
        int k = 2;
        while(!kCliques.isEmpty()){
            if(k == sizeK){
                cliques.addAll(kCliques);
            }
            List<Set<Integer>> newKCliques = new ArrayList<>();
            for(int i = 0; i < kCliques.size(); i++){
                for (int j = i + 1; j < kCliques.size(); j++){
                    Set<Integer> u = kCliques.get(i);
                    Set<Integer> v = kCliques.get(j);

                    Set<Integer> w = new HashSet<>(u);
                    w.addAll(v);

                    if(w.size() == k + 1 && isClique(matrix, w)){
                        newKCliques.add(w);
                    }
                }
            }
            kCliques = newKCliques;
            k++;
        }
        return cliques;
    }

    private boolean isClique(boolean matrix[][], Set<Integer> nodes){
        for(Integer v1: nodes){
            for(Integer v2: nodes){
                if(!v1.equals(v2) && !matrix[v1][v2]){
                    return false;
                }
            }
        }
        return true;
    }

    public void generateRandomG(int n, int m){
        Random rand = new Random();
        for(int i = 0; i < m; i++){
            int u = rand.nextInt(n);
            int v = rand.nextInt(n);
            addEdge(u, v);
        }
    }
    public static void main(String[] args){
        int n = 6;
        int m = 10;
        int k = 3;
        Hm1 graph = new Hm1(n);
        graph.generateRandomG(n, m);
        System.out.println("Matricea de adiacenta:");
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print(graph.graph[i][j] ? "1 " : "0 ");
            }
            System.out.println();
        }

        boolean hasClique = graph.findKClica(graph.graph, k).size() > 0;
        System.out.println("Graful are un clique de dimensiune " + k + ": " + hasClique);
        boolean hasStableSet = graph.hasStableSet(k);
        System.out.print("Graful are un set stabil de dimensiune " + k + ": " + hasStableSet);
    }

}
