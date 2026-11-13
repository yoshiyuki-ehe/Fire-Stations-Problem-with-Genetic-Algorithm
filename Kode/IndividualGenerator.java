import java.util.Random;

public abstract class IndividualGenerator
{
    // updateContext() untuk Generator mencatat ulang nilai nilai pada FireStationSingleton semisal diupdate
    public abstract void updateContext();
    public abstract Individual generateRandomIndividual();
    public abstract Individual generateSpecificIndividual(Chromosome chromosome);
}