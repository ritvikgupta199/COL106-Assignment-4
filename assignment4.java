import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class assignment4 {
    public static void main(String[] args) throws FileNotFoundException {
        ArgParser argparser = new ArgParser();
        argparser.parse(args);

        Graph graph = new Graph("nodes.csv", "edges.csv");
        graph.makeGraph();

        if (argparser.function.equals("average")) {
            graph.printAverage();
        } else if (argparser.function.equals("rank")) {
            graph.printRank();
        } else {
            System.out.println("Wrong arugument");
        }
    }
}

class Graph {
    HashMap<String, Vector<Pair>> graph;
    String nodesFile;
    String edgesFile;

    Graph(String nodesFile, String edgesFile) {
        this.graph = new HashMap<>();
        this.nodesFile = nodesFile;
        this.edgesFile = edgesFile;
    }

    void makeGraph() throws FileNotFoundException {
        readNodes();
        readEdges();
    }

    void printAverage() {
        int sum = 0, count = 0;
        for (Map.Entry<String, Vector<Pair>> mapElement : graph.entrySet()) {
            sum += (mapElement.getValue().size());
            count++;
        }
        double avg = count == 0 ? 0.0 : (double) sum / count;
        System.out.println(String.format("%.2f", avg));
    }

    void printRank() {
        ArrayList<Pair> ranklist = new ArrayList<>();
        for (Map.Entry<String, Vector<Pair>> mapElement : graph.entrySet()) {
            int sum = 0;
            for (Pair p : mapElement.getValue()) {
                sum += p.second;
            }
            ranklist.add(new Pair(mapElement.getKey(), sum));
        }
        Collections.sort(ranklist, Collections.reverseOrder());
        int n = ranklist.size();
        for (int i = 0; i < n; i++) {
            System.out.print(ranklist.get(i).first);
            if (i != n - 1) {
                System.out.print(",");
            }
        }
    }

    void readNodes() throws FileNotFoundException {
        FileReader reader = new FileReader(nodesFile);
        reader.nextLine();
        while (reader.hasNext()) {
            String s = reader.nextLine();
            String[] row = s.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            graph.put(row[0], new Vector<>());
        }
    }

    void readEdges() throws FileNotFoundException {
        FileReader reader = new FileReader(edgesFile);
        reader.nextLine();
        while (reader.hasNext()) {
            String s = reader.nextLine();
            String[] row = s.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            graph.get(row[0]).add(new Pair(row[1], Integer.parseInt(row[2])));
            graph.get(row[1]).add(new Pair(row[0], Integer.parseInt(row[2])));
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

class Pair implements Comparable<Pair> {
    public String first;
    public Integer second;

    Pair(String first, int second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(Pair p) {
        int res = this.second.compareTo(p.second);
        if (res == 0) {
            res = this.first.compareTo(p.first);
        }
        return res;
    }
}