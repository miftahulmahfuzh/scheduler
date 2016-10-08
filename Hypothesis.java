import java.util.*;

public class Hypothesis
{
  private Matkul[] representation; 
  private int fitness;

  // CONSTRUCTOR
  public Hypothesis(Matkul[] matkuls)
  {
    int length = Matkul.getTotalMatkul();
    this.representation = new Matkul[length]; 
    this.setRepresentation(matkuls);
    this.initRepresentation();
    this.fitness = 0;
  }

  // SETTER
  public void initRepresentation()
  {
    for (int i=0;i<Matkul.getTotalMatkul();i++) {
      this.representation[i].setActive();
    }
  }

  public void setRepresentation(Matkul[] matkuls)
  {
    for (int i=0;i<Matkul.getTotalMatkul();i++) {
      this.representation[i] = new Matkul(matkuls[i]);
    } 
  }

  // GETTER
  public Matkul[] getRepresentation()
  {
    return this.representation;
  }

  public int getFitness()
  {
    return this.fitness;
  } 

  public int computeFitness()
  {
    if (this.fitness > 0) {
      this.fitness = 0;
    }

    int N = Matkul.getTotalMatkul();
    Matkul[] r = this.representation;

    for (int i=0;i<N-1;i++) {
      for (int j=i+1;j<N;j++) {
        this.fitness += r[i].computeIntersection(r[j]);
      }
    }

    return this.fitness;
  }

  public void crossOver(Hypothesis h)
  {
    for (int i=0;i<Matkul.getTotalMatkul();i++) {
      if (Math.random() > 0.5) {
        this.representation[i].copyActive(h.representation[i]);
      }
    }
  }

  public void mutate()
  {
    int index = (int) Math.random() * Matkul.getTotalMatkul();
    this.representation[index].setActive();
  }

  public int getTotalTimeMatkul()
  {
    int total = 0;
    for (Matkul m : this.representation) {
      for (int i=m.getStartHourActive();i<m.getEndHourActive();i++) {
        boolean flag = Matkul.ruangans.get(m.getRuanganActive().getName()).activeHours[m.getDayActive()][i]; 
        if (!flag) {
          Matkul.ruangans.get(m.getRuanganActive().getName()).activeHours[m.getDayActive()][i] = true;
          total++;
        } 
      }
    }

    return total;
  }

}
