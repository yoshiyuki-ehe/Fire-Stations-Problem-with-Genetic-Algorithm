public interface TerminateCondition {

    boolean shouldTerminate(Population population,  int currentGeneration);
}