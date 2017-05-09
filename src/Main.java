import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        FastScanner fs = new FastScanner("input.in");
        PrintWriter pw = new PrintWriter("output.out");

        int vertexesNum = fs.nextInt();
        int arcsNum = fs.nextInt();

        int[][] matrix = new int[vertexesNum + 1][vertexesNum + 1];
        int[] count = new int[vertexesNum + 1];
        int[] marks = new int[vertexesNum + 1];
        for (int i = 1; i <= vertexesNum; i++) {
            marks[i] = -1;
        }
        boolean[] blocked = new boolean[vertexesNum + 1];

        int[][] insertedValues = new int[vertexesNum + 1][2];
        for (int i = 1; i <= vertexesNum; i++) {
            insertedValues[i][0] = Integer.MAX_VALUE;
            insertedValues[i][1] = Integer.MAX_VALUE;
        }
        BinaryHeap binaryHeap = new BinaryHeap();

        for (int i = 1; i <= arcsNum; i++) {
            int tmpBeginVertex = fs.nextInt();
            int tmpEndVertex = fs.nextInt();
            int tmpMark = fs.nextInt();
            matrix[tmpBeginVertex][tmpEndVertex] = tmpMark;
        }

        int beginVertex = fs.nextInt();
        int endVertex = fs.nextInt();

        binaryHeap.add(new Vertex(beginVertex, 0, null));
        Vertex lastVertex = null;
        while (!blocked[endVertex] && !binaryHeap.isEmpty()) {
            Vertex currentVertex = binaryHeap.remove();

            int currentIndex = currentVertex.getValue();
            int currentMark = currentVertex.getMark();

            count[currentIndex]++;

            for (int i = 1; i <= vertexesNum; i++) {
                int tmpMark = matrix[currentIndex][i];
                if (tmpMark != 0 && !blocked[i]) {
                    int newMark = currentMark + tmpMark;
                    if (newMark < insertedValues[i][0]) {
                        insertedValues[i][1] = insertedValues[i][0];
                        insertedValues[i][0] = newMark;
                        binaryHeap.add(new Vertex(i, newMark, currentVertex));
                    } else if (newMark < insertedValues[i][1]) {
                        insertedValues[i][1] = newMark;
                        binaryHeap.add(new Vertex(i, newMark, currentVertex));
                    }
                }
            }

            if (count[currentIndex] == 2) {
                blocked[currentIndex] = true;
                marks[currentIndex] = currentMark;
                if (currentIndex == endVertex) {
                    lastVertex = currentVertex;
                }
            }
        }

        List<Integer> route = new ArrayList<>();
        Vertex current = lastVertex;
        while (current != null) {
            route.add(current.getValue());
            current = current.getPrev();
        }

        pw.println(marks[endVertex]);
        for (int i = route.size() - 1; i >=0; i--) {
            pw.print(route.get(i) + " ");
        }
        pw.println();
        fs.close();
        pw.close();
    }
}

class Vertex {
    private int value;
    private int mark;
    private Vertex prev;

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

    public int getValue() {

        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public Vertex getPrev() {
        return prev;
    }

    public void setPrev(Vertex prev) {
        this.prev = prev;
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

class BinaryHeap {
    private List<Vertex> list;
    private int size;

    public BinaryHeap() {
        list = new ArrayList<>();
        size = 0;
    }

    public Vertex peek() {
        return list.get(0);
    }

    public Vertex remove() {
        Vertex result = list.get(0);
        list.set(0, list.get(size - 1));
        list.remove(size - 1);
        size = list.size();
        heapify(0);
        return result;
    }

    public void add(Vertex value) {
        list.add(value);
        size = list.size();
        int i = size - 1;
        int parent = (i - 1) / 2;

        while (i > 0 && list.get(parent).getMark() >= list.get(i).getMark()) {
            Vertex temp = list.get(i);
            list.set(i, list.get(parent));
            list.set(parent, temp);

            i = parent;
            parent = (i - 1) / 2;
        }
    }

    public void build(Vertex[] initArray) {
        clear();
        for (Vertex current : initArray) {
            list.add(current);
        }
        size = list.size();
        for (int i = 0; i <= size / 2; i++) {
            heapify(size / 2 - i);
        }
    }

    public void set(int index, Vertex value) {
        if (value.getMark() >= list.get(index).getMark()) {
            list.set(index, value);
            heapify(index);
        } else {
            list.set(index, value);
            for (int i = 0; i <= (index - 1) / 2; i++) {
                heapify((index - 1) / 2 - i);
            }
        }
    }

    private void heapify(int i) {
        int leftChild;
        int rightChild;
        int smallestChild;

        while (true) {
            leftChild = 2 * i + 1;
            rightChild = 2 * i + 2;
            smallestChild = i;
            if (leftChild < size && list.get(leftChild).getMark() < list.get(smallestChild).getMark()) {
                smallestChild = leftChild;
            }
            if (rightChild < size && list.get(rightChild).getMark() < list.get(smallestChild).getMark()) {
                smallestChild = rightChild;
            }
            if (smallestChild == i) {
                break;
            }
            Vertex temp = list.get(i);
            list.set(i, list.get(smallestChild));
            list.set(smallestChild, temp);
            i = smallestChild;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
        size = 0;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
