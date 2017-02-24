package com.fullmob.jiraapi;

public class DiscoveryGraphConceptTest {
//    @Test
//    public void TestRecreatingTheGraph() {
//        DiscoveryGraph g = createGraph();
//        DiscoveryGraph cloned = cloneGraph(g.findById("5"));
//        for (Map.Entry<String, Node> n : g.visited.entrySet()) {
////            if (!cloned.isVisited(n)) {
////                Assert.fail();
////            }
//        }
//    }
//
//    private DiscoveryGraph cloneGraph(Node n5) {
//        DiscoveryGraph newGraph = new DiscoveryGraph();
//        findAllNodes(n5, newGraph);
//
//        return newGraph;
//    }
//
//    private Node findAllNodes(Node n, DiscoveryGraph g) {
//        Node cloned = null;
//        if (!g.isVisited(n)) {
//            cloned = n.clone();
//            g.add(cloned);
//            for (Node vertex : findAllVertices(n)) {
//                if (g.isVisited(vertex)) {
//                    cloned.vertices.add(vertex);
//                } else {
//                    cloned.vertices.add(findAllNodes(vertex, g));
//                }
//            }
//        }
//
//        return cloned;
//    }
//
//    /**
//     * This method is just extract to mock the network request that should get the next possible network request
//     */
//    private List<Node> findAllVertices(Node n) {
//        return n.vertices;
//    }
//
//    private DiscoveryGraph createGraph() {
//    /*
//           ____________________________________________________
//          /       _____________________________________________|
//         /       /                   \                         |
//        /    /--(1)--\                \         /---(8)--\     |
//     (10)---+         +---(3)---(4)---(5)--(6)--+          +---(9)
//             \--(2)--/           /              \---(7)--/
//                 \______________/                   /
//                  \________________________________/
//     */
//        Node n1 = new Node("n1", "1", "toN1", "toid1");
//        Node n2 = new Node("n2", "2", "toN2", "toid2");
//        Node n3 = new Node("n3", "3", "toN3", "toid3");
//        Node n4 = new Node("n4", "4", "toN4", "toid4");
//        Node n5 = new Node("n5", "5", "toN5", "toid5");
//        Node n6 = new Node("n6", "6", "toN6", "toid6");
//        Node n7 = new Node("n7", "7", "toN7", "toid7");
//        Node n8 = new Node("n8", "8", "toN8", "toid8");
//        Node n9 = new Node("n9", "9", "toN9", "toid9");
//        Node n10 = new Node("n10", "10", "toN10", "toid10");
//
//        n1.vertices.add(n10);   n1.vertices.add(n3);    n1.vertices.add(n5);   n1.vertices.add(n9);
//        n2.vertices.add(n10);   n2.vertices.add(n3);    n2.vertices.add(n4);   n2.vertices.add(n7);
//        n3.vertices.add(n1);    n3.vertices.add(n2);    n3.vertices.add(n4);
//        n4.vertices.add(n2);    n4.vertices.add(n4);    n4.vertices.add(n5);
//        n5.vertices.add(n1);    n5.vertices.add(n4);    n5.vertices.add(n6);
//        n6.vertices.add(n5);    n6.vertices.add(n7);    n6.vertices.add(n7);
//        n7.vertices.add(n6);    n7.vertices.add(n9);    n7.vertices.add(n2);
//        n8.vertices.add(n6);    n8.vertices.add(n9);
//        n9.vertices.add(n1);    n9.vertices.add(n7);    n9.vertices.add(n8);   n9.vertices.add(n10);
//        n10.vertices.add(n1);   n10.vertices.add(n2);   n10.vertices.add(n9);
//
//        DiscoveryGraph gr = new DiscoveryGraph();
//        gr.add(n1).add(n2).add(n3).add(n4).add(n5).add(n6).add(n7).add(n8).add(n9).add(n10);
//
//        return gr;
//    }


}