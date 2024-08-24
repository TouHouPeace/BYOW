package byow.Core;

public class UnionFind {
    //class to help two rooms to connect
    int[] parent;

    public UnionFind(int n){
        //initialize a unionfind data structure where all items are disjoint (-1)
        //a unionfind data structure is where every node with a negative sign is a root, and the content at every node is the index of its parent
        //the negative value of the root is the size of the group
        this.parent = new int[n];
        for(int i = 0; i < n; i++){
            parent[i] = -1;
        }
    }

    private void checkValid(int n){
        if(n < 0 || n >= this.parent.length){
            throw new IllegalArgumentException("Invalid vertex");
        }
    }

    public int find(int n){
        checkValid(n);
        //checks if n is a valid vertex
        int temp = n;
        while(parent[temp] >= 0){
            //go to its parent if it is not a root
            temp = parent[temp];
        }
        return temp;
    }

    public int sizeOf(int n){
        //returns the size of the set that n belongs to
        int output = find(n);
        return -1 * parent[n];
    }

    public boolean isConnected(int n1, int n2){
        //checks if two nodes are connected by seeing if their root is the same
        return find(n1) == find(n2);
    }

    public int getParent(int n){
        //returns the parent of a node, if the given node is the root, return the negative value of the size of the root
        checkValid(n);
        return parent[n];
    }

    public void connect(int v1, int v2) {
        //connects two node together
        checkValid(v1);
        checkValid(v2);

        int p1 = find(v1);
        int p2 = find(v2);

        if (p1 == p2) {
            //if the two nodes have the same parent
            return;
        }
        int v = sizeOf(p1) > sizeOf(p2) ? p2 : p1;
        int other = sizeOf(p1) > sizeOf(p2) ? p1 : p2;
        //find which of the two set has the greater size
        parent[other] -= sizeOf(v);
        parent[v] = other;
        //connecting them by decrementing the negative value of the larger root by the negative value of the smaller root
        //and by changing the index value of the smaller root to the index of the bigger set
    }


}
