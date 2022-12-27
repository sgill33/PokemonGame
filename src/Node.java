public class Node implements Comparable<Node>{

    private double g ;
    private double h;
    private double f;
    private Node prior;
    private Point p;

    Node(Point cur, Point goal){
        this.p = cur;
        calcH(goal);
    }

    public void setG(double g) {
        this.g = g;
        this.f = this.h + this.g;
    }

    public void setPrior(Node prior) {
        this.prior = prior;
    }

    public void setH(double h) {
        this.h = h;
        this.f = this.h + this.g;
    }

    public void calcH(Point po) {
        this.h = Math.abs(p.x - po.x) + Math.abs(p.y - po.y);
        this.f = this.h + this.g;
    }

    public double getF() {
        return f;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

    public Point getP() {
        return p;
    }

    public Node getPrior() {
        return prior;
    }

    public int compareTo(Node o) {
       return Double.compare(this.f,o.getF());
    }

    public int hashCode()
    {
        return p.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Node &&
                this.p.equals(((Node)other).getP());
    }
}
