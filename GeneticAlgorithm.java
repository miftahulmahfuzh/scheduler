import java.util.*;

public class GeneticAlgorithm
{ 
  private Matkul[] matkuls;
  private final int populationNumber = 74;
  private final double replaceFactor = 0.7;
  private final double mutationRate = 0.3;
  private int totalPopulation;
  private Hypothesis[] population;
  private Hypothesis[] successorPopulation; 

  public GeneticAlgorithm(Matkul[] _matkuls, HashMap<String, Ruangan> _ruangans)
  {
    Matkul.setRuangans(_ruangans);
    Matkul.setTotalMatkul(_matkuls.length);

    this.matkuls = _matkuls;
    this.createInitialPopulation();
  } 

  public Hypothesis[] getPopulation()
  {
    return this.population;
  }

  public int getTotalGeneratedPopulation()
  {
    return this.totalPopulation;
  }

  private void createInitialPopulation()
  {
    int p = this.populationNumber;
    population = new Hypothesis[p];
    successorPopulation = new Hypothesis[p];

    for (int i=0;i<p;i++)
    {
      this.population[i] = new Hypothesis(this.matkuls);
      this.successorPopulation[i] = new Hypothesis(this.matkuls);
    }
  }

  public void computeFitness()
  {
    for (int i=0;i<this.populationNumber;i++) {
      this.population[i].computeFitness();
    }

    HypothesisComparator c = new HypothesisComparator();
    Arrays.sort(this.population, c);
  }

  public double computeAveFitness()
  {
    int sum = 0;

    for (int i=0;i<this.populationNumber;i++) {
      sum += this.population[i].getFitness();
    }

    return sum / (this.populationNumber * 1.0); 
  }

  public void select()
  {
    int p = this.populationNumber;
    int numSelected = 0;
    Matkul[] representation;
    Hypothesis h;
    double prh;
    int rank;

    while (numSelected < (1 - this.replaceFactor) * p) {
      rank = (int) Math.random() * p;
      h = this.population[rank];
      prh = (p - rank) / (p * (p + 1.0) / 2.0);

      if (Math.random() < prh) {
        representation = h.getRepresentation();
        successorPopulation[numSelected].setRepresentation(representation);
        numSelected++;
      }
    }
  }

  public void crossOver()
  {
    Hypothesis parent1 = new Hypothesis(this.matkuls);
    Hypothesis parent2 = new Hypothesis(this.matkuls);
    int p = this.populationNumber;
    double r = this.replaceFactor;
    int j = (int) ((1 - r) * p);
    int numReproduced = 0;
    int rankP1, rankP2;
    double prP1, prP2;

    while (numReproduced < r * p) {
      rankP1 = (int) Math.random() * p;
      prP1 = (p-rankP1) / (p * (p+1.0)/2.0);
      rankP2 = (int) Math.random() * p;
      prP2 = (p-rankP2) / (p * (p+1.0)/2.0);

      if (Math.random() < prP1 && Math.random() < prP2) {
        parent1.setRepresentation(this.population[rankP1].getRepresentation());
        parent2.setRepresentation(this.population[rankP2].getRepresentation());

        parent1.crossOver(parent2);
        this.successorPopulation[j].setRepresentation(parent1.getRepresentation());
        numReproduced++;

        parent2.crossOver(parent1);
        this.successorPopulation[j+1].setRepresentation(parent2.getRepresentation());
        numReproduced++;

        j += 2;
      }
    }
  }

  public void mutate()
  {
    int p = this.populationNumber;
    int index;

    for (int i=0;i<this.mutationRate*p;i++)
    {
      index = (int) Math.random() * p;
      this.successorPopulation[index].mutate();
    }
  }

  public void setNextGeneration()
  {
    for (int i=0;i<this.populationNumber;i++) {
      Matkul[] representation = this.successorPopulation[i].getRepresentation();
      this.population[i].setRepresentation(representation);
    }
  }

  public Hypothesis solveProblem()
  {
    this.computeFitness();

    Hypothesis bestIndividual = (this.getPopulation())[0];
    int bestFitness = bestIndividual.getFitness();
    double aveFitness = this.computeAveFitness();
    int iterationNum = 1;

    while (bestFitness > 0 && iterationNum <= 37) {
      // Select the very best members of the population to survive
      this.select();
      // Make the best members reproduce themselves
      this.crossOver();
      // Add some mutations to new population
      this.mutate();
      // The successor population becomes the current population
      this.setNextGeneration();
      // For each individual compute fitness
      this.computeFitness();

      bestIndividual = (this.getPopulation())[0];
      bestFitness = bestIndividual.getFitness();
      aveFitness = this.computeAveFitness();

      iterationNum++;
    }

    this.totalPopulation = iterationNum * this.populationNumber;

    System.out.println("\nIteration : " + iterationNum + 
                       ",  Average Fitness : " + aveFitness);
    System.out.println("Solution is : " + bestIndividual.toString() + "\n");

    return bestIndividual;
  }
} 
