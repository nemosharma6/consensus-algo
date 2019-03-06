import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Master {

    public static void main(String[] args) {

        ParseInput.readFile(args[0]);
        Log.init(args[1]);
        List<Node> nodes = new ArrayList<Node>();

        ArrayList<Link> linkList = new ArrayList<>();
        for (int i = 0; i < ParseInput.processCount; i++) {
            for (int j = i + 1; j < ParseInput.processCount; j++) {
                if (i != j)
                    linkList.add(new Link(i, j));
            }
        }

        for (int i = 0; i < ParseInput.processCount; i++) {
            List<Link> l = new ArrayList<>();
            for (Link link : linkList) {
                if (link.getNode1() == i || link.getNode2() == i)
                    l.add(link);
            }

            nodes.add(new Node(i, l, ParseInput.values[i]));
        }

        int msgCount = 1;
        int prev = 0;
        int msgPerNode = ParseInput.processCount - 1;
        int drop = ParseInput.dropInterval;

        try {

            for (int i = 0; i < ParseInput.rounds; i++) {

                for (Node node : nodes) {
                    if (msgCount - prev + msgPerNode > drop) {
                        Set<Integer> set = new HashSet<>();
                        for (int j = 0; j < msgPerNode; j++)
                            if (msgCount + j - prev == drop) {
                                set.add(j);
                                prev = msgCount + j;
                            }

                        msgCount += msgPerNode;
                        System.out.println("Skip List -> " + node.getName() + " round-" + i + " set-" + set.toString());
                        Log.write(Master.class.getName(), "Skip List -> " + node.getName() + " round-" + i
                                + " set-" + set.toString());
                        node.setSkip(set);
                    } else
                        msgCount += msgPerNode;

                    node.start();
                }

                for (Node node : nodes)
                    node.join();
            }
        } catch (Exception e) {
            Log.write(Master.class.getName(), e.getMessage());
        }

        for (Node node : nodes) {
            Log.write(Master.class.getName(), "Node : " + node.getId() + ", Values : " + node.getValues());
            System.out.println("Node : " + node.getId() + ", Values: {" + node.getValues() + "}");
        }
    }
}
