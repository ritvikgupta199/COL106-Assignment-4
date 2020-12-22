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

        Graph graph = new Graph("nodes.csv", "edges.csv");
        graph.makeGraph();

        if (argparser.function.equals("average")) {
            graph.printAverage();
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

class Pair {
    public String first;
    public int second;

    Pair(String first, int second) {
        this.first = first;
        this.second = second;
    }
}