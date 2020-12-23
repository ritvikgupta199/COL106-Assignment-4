import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class assignment4 {
    public static void main(String[] args) throws FileNotFoundException {
        ArgParser argparser = new ArgParser();
        argparser.parse(args);

        Graph graph = new Graph(argparser.nodesFile, argparser.edgesFile);
        graph.makeGraph();

        if (argparser.function.equals("average")) {
            graph.calcAverage();
        } else if (argparser.function.equals("rank")) {
            graph.calcRank();
        } else if (argparser.function.equals("independent_storylines_dfs")) {
            graph.calcIndependentStorylines();
        } else {
            System.out.println("Wrong arugument");
        }
    }
}

class Graph {
    HashMap<String, Vector<Pair<Integer, String>>> graph;
    HashMap<String, Boolean> vis;
    ArrayList<ComparableArrayList<String>> components;
    String nodesFile;
    String edgesFile;

    Graph(String nodesFile, String edgesFile) {
        this.graph = new HashMap<>();
        this.vis = new HashMap<>();
        this.components = new ArrayList<>();
        this.nodesFile = nodesFile;
        this.edgesFile = edgesFile;
    }

    void makeGraph() throws FileNotFoundException {
        readNodes();
        readEdges();
    }

    void calcAverage() {
        int sum = 0;
        int count = 0;
        for (Map.Entry<String, Vector<Pair<Integer, String>>> mapElement : graph.entrySet()) {
            sum += (mapElement.getValue().size());
            count++;
        }
        double avg = count == 0 ? 0.0 : (double) sum / count;
        System.out.println(String.format("%.2f", avg));
    }

    void calcRank() {
        ArrayList<Pair<Integer, String>> ranklist = new ArrayList<>();
        for (Map.Entry<String, Vector<Pair<Integer, String>>> mapElement : graph.entrySet()) {
            int sum = 0;
            for (Pair<Integer, String> p : mapElement.getValue()) {
                sum += p.first;
            }
            ranklist.add(new Pair<Integer, String>(sum, mapElement.getKey()));
        }
        QuickSort.revSort(ranklist);
        int n = ranklist.size();
        for (int i = 0; i < n; i++) {
            System.out.print(ranklist.get(i).second);
            if (i != n - 1) {
                System.out.print(",");
            }
        }
        System.out.print("\n");
    }

    void DFS(String v, int count) {
        Vector<Pair<Integer, String>> adj = graph.get(v);
        components.get(count).add(v);
        vis.put(v, true);
        for (int i = 0; i < adj.size(); i++) {
            Pair<Integer, String> u = adj.get(i);
            if (vis.get(u.second) != null && !vis.get(u.second)) {
                DFS(u.second, count);
            }
        }
    }

    void calcIndependentStorylines() {
        int count = 0;
        for (Map.Entry<String, Vector<Pair<Integer, String>>> mapElement : graph.entrySet()) {
            boolean v = vis.get(mapElement.getKey());
            if (!v) {
                components.add(new ComparableArrayList<>());
                DFS(mapElement.getKey(), count);
                count++;
            }
        }
        for (int i = 0; i < components.size(); i++) {
            QuickSort.revSort(components.get(i).list);
        }
        QuickSort.revSort(components);
        for (int i = 0; i < components.size(); i++) {
            ComparableArrayList<String> list = components.get(i);
            int n = list.size();
            for (int j = 0; j < n; j++) {
                System.out.print(list.get(j));
                if (j != n - 1) {
                    System.out.print(",");
                }
            }
            System.out.print("\n");
        }
    }

    void readNodes() throws FileNotFoundException {
        FileReader reader = new FileReader(nodesFile);
        reader.nextLine();
        while (reader.hasNext()) {
            String s = reader.nextLine();
            String[] row = s.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            graph.put(row[0], new Vector<>());
            vis.put(row[0], false);
        }
    }

    void readEdges() throws FileNotFoundException {
        FileReader reader = new FileReader(edgesFile);
        reader.nextLine();
        while (reader.hasNext()) {
            String s = reader.nextLine();
            String[] row = s.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            graph.get(row[0]).add(new Pair<>(Integer.parseInt(row[2]), row[1]));
            graph.get(row[1]).add(new Pair<>(Integer.parseInt(row[2]), row[0]));
        }
    }

}

class ComparableArrayList<T extends Comparable<T>> implements Comparable<ComparableArrayList<T>> {
    ArrayList<T> list;

    ComparableArrayList() {
        this.list = new ArrayList<>();
    }

    int size() {
        return list.size();
    }

    boolean add(T e) {
        return list.add(e);
    }

    T get(int index) {
        return list.get(index);
    }

    public int compareTo(ComparableArrayList<T> l) {
        int n1 = this.list.size();
        int n2 = l.list.size();
        if (n1 != n2) {
            return n1 - n2;
        } else {
            for (int i = 0; i < n1; i++) {
                if (list.get(i).compareTo(l.list.get(i)) != 0) {
                    return list.get(i).compareTo(l.list.get(i));
                }
            }
            return 0;
        }
    }
}

class ArgParser {
    String nodesFile;
    String edgesFile;
    String function;

    ArgParser() {
        this.nodesFile = "";
        this.edgesFile = "";
        this.function = "";
    }

    void parse(String[] args) {
        if (args.length != 3) {
            System.out.println("Error!");
        } else {
            this.nodesFile = args[0];
            this.edgesFile = args[1];
            this.function = args[2];
        }
    }
}

class FileReader {
    Scanner sc;
    String filename;

    FileReader(String filename) throws FileNotFoundException {
        this.filename = filename;
        File file = new File(filename);
        sc = new Scanner(file);
    }

    boolean hasNext() {
        return sc.hasNext();
    }

    String next() {
        return sc.next();
    }

    String nextLine() {
        return sc.nextLine();
    }

    int nextInt() {
        return Integer.parseInt(sc.next());
    }
}

class Pair<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Pair<K, V>> {
    public K first;
    public V second;

    Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(Pair<K, V> p) {
        int res = this.first.compareTo(p.first);
        if (res == 0) {
            res = this.second.compareTo(p.second);
        }
        return res;
    }
}

class QuickSort<T> {

    public static <T extends Comparable<T>> int partition(ArrayList<T> arr, int l, int r, int p) {
        T pivot = arr.get(p);
        int i = l;
        int j = r;
        while (i < j && i <= r) {
            while (i <= r && arr.get(i).compareTo(pivot) >= 0) {
                i++;
            }
            while (arr.get(j).compareTo(pivot) < 0) {
                j--;
            }
            if (i < j) {
                T tmp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, tmp);
            }
        }
        return j;
    }

    public static <T extends Comparable<T>> void quicksort(ArrayList<T> arr, int l, int r) {
        if (l < r) {
            int pivot = l + (int) ((r - l + 1) * Math.random());
            int p = partition(arr, l, r, pivot);
            quicksort(arr, l, p);
            quicksort(arr, p + 1, r);
        }
    }

    public static <T extends Comparable<T>> void revSort(ArrayList<T> arr) {
        quicksort(arr, 0, arr.size() - 1);
    }
}