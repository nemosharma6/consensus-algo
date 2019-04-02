import java.util.*;

public class Master {

    static int k;

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

        int msgPerNode = ParseInput.processCount - 1;
        int drop = ParseInput.dropInterval + 1;
        int delivered = 0;

        Random random = new Random();
        k = Math.abs(random.nextInt(ParseInput.rounds));
        if (k == 0)
            k = 1;
        System.out.println("Value of k is " + k);

        try {

            for (int i = 0; i < ParseInput.rounds; i++) {

                List<Integer> levelList = new ArrayList<>();

                for (Node node : nodes) {
                    Set<Integer> set = new HashSet<>();
                    for (int j = 0; j <= msgPerNode; j++) {
                        if (j == node.getId())
                            continue;
                        delivered++;
                        if (delivered == drop) {
                            set.add(j);
                            delivered = 0;
                        }
                    }

                    System.out.println("Skip List -> " + node.getName() + " round-" + i + " set-" + set.toString());
                    Log.write(Master.class.getName(), "Skip List -> " + node.getName() + " round-" + i
                            + " set-" + set.toString());
                    node.setSkip(set);

                    node.start();
                }

                for (Node node : nodes)
                    node.join();

                for (Node node : nodes) {
                    levelList.add(node.getLevel());
                    System.out.println(node.getName() + " has the following level info : " + node.getLevelValues());
                    Log.write(node.getName(), "has the following level info : " + node.getLevelValues());
                }

                StringBuilder sb = new StringBuilder();
                for (Integer l : levelList) {
                    sb.append(l);
                    sb.append(" ");
                }

                if (levelList.size() != 0)
                    sb.deleteCharAt(sb.length() - 1);
                System.out.println("Levels -> " + sb.toString());
                Log.write(Master.class.getName(), "Levels : " + sb.toString());
            }
        } catch (Exception e) {
            Log.write(Master.class.getName(), e.getMessage());
        }

        for (Node node : nodes) {
            Log.write(Master.class.getName(), "Node : " + node.getId() + ", Values : " + node.getValues() +
                    ", Decision: {" + node.getDecision() + "}");
            System.out.println("Node : " + node.getId() + ", Values: {" + node.getValues() + "}" + ", Decision: {" +
                    node.getDecision() + "}");
        }
    }
}
