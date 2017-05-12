import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FastScanner fs = new FastScanner("input.in");
        PrintWriter pw = new PrintWriter("output.out");

        int townsNum = fs.nextInt();
        int roadsNum = fs.nextInt();

        Town[] towns = new Town[townsNum + 1];
        for (int i = 1; i <= townsNum; i++) {
            towns[i] = new Town();
        }
        int tmpFrom;
        int tmpTo;
        int tmpLength;
        for (int i = 1; i <= roadsNum; i++) {
            tmpFrom = fs.nextInt();
            tmpTo = fs.nextInt();
            tmpLength = fs.nextInt();
            towns[tmpFrom].roads.add(new Road(tmpFrom, tmpTo, tmpLength));
        }

        int begin = fs.nextInt();
        int end = fs.nextInt();

        Queue<Vertex> binaryHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.mark, b.mark));

        binaryHeap.add(new Vertex(begin, 0, null));

        Vertex currentVertex;
        int currentIndex;
        int currentMark;
        Vertex lastVertex = null;

        while (towns[end].count != 2 && !binaryHeap.isEmpty()) {
            currentVertex = binaryHeap.poll();

            currentIndex = currentVertex.value;
            currentMark = currentVertex.mark;
            if (towns[currentIndex].count != 2) {
                towns[currentIndex].count++;

                for (Road currentRoad : towns[currentIndex].roads) {
                    if (towns[currentRoad.to].count != 2) {
                        binaryHeap.add(new Vertex(currentRoad.to, currentMark + currentRoad.length, currentVertex));
                    }
                }

                if (towns[currentIndex].count == 2) {
                    towns[currentIndex].mark = currentMark;
                    if (currentIndex == end) {
                        lastVertex = currentVertex;
                    }
                }
            }
        }

        List<Integer> route = new ArrayList<>();
        Vertex current = lastVertex;
        while (current != null) {
            route.add(current.value);
            current = current.prev;
        }

        pw.println(towns[end].mark);
        for (int i = route.size() - 1; i >=0; i--) {
            pw.print(route.get(i) + " ");
        }
        pw.println();
        fs.close();
        pw.close();
    }
}

class Vertex {
    public int value;
    public int mark;
    public Vertex prev;

    public Vertex(int value, int mark, Vertex prev) {
        this.value = value;
        this.mark = mark;
        this.prev = prev;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(value).append(" ").append(mark).append(" ").append(prev);
        return sb.toString();
    }
}

class Road {
    public int from;
    public int to;
    public int length;

    public Road(int from, int to, int length) {
        this.from = from;
        this.to = to;
        this.length = length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(from).append(" ").append(to).append(" ").append(length);
        return sb.toString();
    }
}

class Town {
    public int mark;
    public int count;
    public List<Road> roads;

    public Town() {
        this.mark = -1;
        count = 0;
        roads = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(mark).append(" ").append(count).append(" ").append(roads);
        return sb.toString();
    }
}

class FastScanner {
    private BufferedReader reader;
    private StringTokenizer tokenizer;

    public FastScanner(String fileName) throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
    }

    public String next() throws IOException {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            String line = reader.readLine();
            if (line == null) {
                throw new EOFException();
            }
            tokenizer = new StringTokenizer(line);
        }
        return tokenizer.nextToken();
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public void close() throws IOException {
        reader.close();
    }
}