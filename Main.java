import java.util.*;

public class Main 
{
  public static void main(String args[]) {
    InputProcessor parser = new InputProcessor(args[0]);

    Set<String> listNamaRuangan = parser.getListNamaRuangan();
    HashMap<String, Ruangan> ruangans = new HashMap<String, Ruangan>();
    for (String namaRuangan : listNamaRuangan) {
      ruangans.put(namaRuangan, new Ruangan(namaRuangan,
           parser.getOpenTimeRuangan(namaRuangan), 
           parser.getCloseTimeRuangan(namaRuangan), 
           parser.getWorkingDaysRuangan(namaRuangan)));
    }

    Set<String> listNamaMatkul = parser.getListNamaMatkul();
    Matkul[] matkuls = new Matkul[listNamaMatkul.size()];
    int i = 0;
    for (String namaMatkul : listNamaMatkul) {
      matkuls[i] = new Matkul(
          parser.getNamaMatkul(namaMatkul), 
          parser.getRuanganMatkul(namaMatkul),
          parser.getStartTimeMatkul(namaMatkul),
          parser.getEndTimeMatkul(namaMatkul),
          parser.getDurationMatkul(namaMatkul),
          parser.getDaysMatkul(namaMatkul));
      i++; 
    }

    int algCode = Integer.parseInt(args[1]);
    long start = System.currentTimeMillis();
    Matkul[] result = new Matkul[matkuls.length];
    int conflict = 0;
    double efficiency = 0.0;

    if (algCode == 1 || algCode == 2) {
      Algorithm solver = new Algorithm();

      if (algCode == 1) {
        System.out.println("HILL CLIMBING\n");
        solver = new HillClimbing(matkuls, ruangans);
      } else if (algCode == 2) {
        System.out.println("SIMULATED ANNEALING\n");
        solver = new SimulatedAnnealing(matkuls, ruangans);
      }

      Node resultNode = solver.solveProblem(); 
      System.out.println("Total nodes generated : " + solver.getTotalGeneratedNodes() + "\n");

      conflict = resultNode.getHeuristic();
      result = resultNode.state;
      for (Matkul matkul : result) {
        System.out.println(matkul.toString());
      }

      efficiency = resultNode.getTotalTimeMatkul() * 100.0 / parser.getTotalTimeRuangan(); 
    } else if (algCode == 3) {
      System.out.println("GENETIC ALGORITHM");
      GeneticAlgorithm solver = new GeneticAlgorithm(matkuls, ruangans); 

      Hypothesis resultHypotesis = solver.solveProblem();
      System.out.println("Total population generated : " + solver.getTotalGeneratedPopulation() + "\n");

      conflict = resultHypotesis.getFitness(); 
      result = resultHypotesis.getRepresentation();
      for (Matkul matkul : result) {
        System.out.println(matkul.toString());
      }

      efficiency = resultHypotesis.getTotalTimeMatkul() * 100.0 / parser.getTotalTimeRuangan(); 
    }

    if (efficiency > 100.0) {
      efficiency = 100.0;
    }

    System.out.println("Total conflict : " + conflict + "\n");
    System.out.println("Efficiency : " + efficiency + " %\n");

    long diff = System.currentTimeMillis() - start;
    System.out.println("\nTime : " + diff + " ms");
  }
}
