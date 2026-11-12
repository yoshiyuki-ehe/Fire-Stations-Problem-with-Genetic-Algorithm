public abstract class Chromosome {
    public final int length;

    public Chromosome(int length) {
        this.length = length;
    }

    public abstract int getGene(int index);
    public abstract void setGene(int index, int value);
    public abstract void selfRepair();
    public abstract boolean crossable(Chromosome other);
    public abstract Chromosome duplicate();
}
