import java.util.Random;

public class GraphGenerator {
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);

        Random rand = new Random();
        if(k > 2 * n){
            System.out.println("k mai mic ca 2 * n");
            return;
        }
        int[][] m = new int[n][n];

        for(int i = 0; i < k; i++){
            for(int j = i + 1; j < k; j++) {
                m[i][j] = 1;
                m[j][i] = 1;
            }
        }

        int[] set = new int[k];
        for(int i = k; i < 2*k; i++){
            set[i-k] = i;
            int randomNod = rand.nextInt(k);
            m[randomNod][i] = 1;
            m[i][randomNod] = 1;
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(m[i][j] == 0 && !IsSet(i,set,k) && IsSet(j,set,k)){
                    m[i][j] = rand.nextBoolean() ? 1 : 0;
                    m[j][i] = m[i][j];
                }
            }
        }
        if(n <= 30_000) {
            printare(m);
        }
        int muchii = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i <= j && m[i][j] == 1)
                    muchii ++;
            }

        }
        System.out.println("Numarul de muchii este " + muchii);

        int[] grade = new int[n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(m[i][j] == 1){
                    grade[i]++;
                }
            }
        }
        int maxi = 0;
        int mini = 9_999_999;
        int sum = 0;
        for(int i = 0; i < n; i++){
            sum += grade[i];
            if(grade[i] > maxi){
                maxi = grade[i];
            }
            if(grade[i] < mini){
                mini = grade[i];
            }
        }
        System.out.println("(Δ(G)): " + maxi);
        System.out.println("(δ(G)): " + mini);
        System.out.println("Suma " + sum);

        long endTime = System.currentTimeMillis();
        if(n > 30_000){
            System.out.println("Execution time: " + (endTime - startTime));
        }

    }

    private static boolean IsSet(int nod, int[] set, int k){
        for(int i = 0; i < k; i++){
            if(nod == set[i])
                return true;
        }
        return false;
    }
    private static void printare(int[][] m){
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[i].length; j++){
                System.out.print(m[i][j] == 1 ? "■ " : "□ ");
            }
            System.out.println();
        }
    }
}
