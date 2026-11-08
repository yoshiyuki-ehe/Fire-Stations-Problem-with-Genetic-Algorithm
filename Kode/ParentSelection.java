import java.util.Random;

public abstract class ParentSelection{

    public abstract Individual[] selectParents(Population population, Random random, int nOfParents);

}