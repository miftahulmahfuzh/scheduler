import  java.util.*;

public class Node implements Comparable<Node>
{
  public Matkul[] state;
  private ArrayList<Node> neighbours;
  private int heuristic;

  // CONSTRUCTOR
  public Node()
  {
    int N = Matkul.getTotalMatkul();
    this.state = new Matkul[N];
    this.neighbours = new ArrayList<Node>();
    this.heuristic = 0;
  }

  public Node(Node n)
  {
    int N = Matkul.getTotalMatkul();
    this.state = new Matkul[N];
    this.heuristic = n.heuristic;
    this.neighbours = new ArrayList<Node>();
    for (int i=0;i<N;i++) {
      this.state[i] = new Matkul(n.state[i]);
    }
  }

  // SETTER
  public void setState(Matkul[] _matkuls)
  {
    for (int i=0;i<Matkul.getTotalMatkul();i++) {
      this.state[i] = new Matkul(_matkuls[i]);
    }
  }

  // GETTER
  public int getHeuristic()
  {
    return this.heuristic;
  }

  public int getTotalTimeMatkul()
  {
    int total = 0;
    for (Matkul m : state) {
      for (int i=m.getStartHourActive();i<=m.getEndHourActive();i++) {
        boolean flag = Matkul.ruangans.get(m.getRuanganActive().getName()).activeHours[m.getDayActive()][i]; 
        if (!flag) {
          Matkul.ruangans.get(m.getRuanganActive().getName()).activeHours[m.getDayActive()][i] = true;
          total++;
        } 
      }
    }

    return total;
  }

  // OTHER
  public int compareTo(Node n)
  {
    if (this.heuristic < n.heuristic) {
      return -1;
    } else if (this.heuristic == n.heuristic) {
      return 0;
    } else { 
      return 1;
    }
  }

  public ArrayList<Node> generateNeighbours()
  {
    int count = 0;

    for (int i=0;i<Matkul.getTotalMatkul();i++) {
      if (!this.state[i].isRuanganStrict()) {
        for (int j=0;j<Matkul.getTotalRuangan();j++) {
          this.neighbours.add(count, new Node(this));
          this.neighbours.get(count).state[i].moveRuangan();
          this.neighbours.get(count).computeHeuristic();
          count++;
        }
      }

      Ruangan ruangan = this.state[i].getRuanganActive(); 

      for (int j : ruangan.getWorkingDays()) {
        this.neighbours.add(count, new Node(this));
        this.neighbours.get(count).state[i].moveDay(j);
        this.neighbours.get(count).computeHeuristic();
        count++;
      }

      for (int j=ruangan.getOpenTime();j<ruangan.getCloseTime();j++) {
        this.neighbours.add(count, new Node(this));
        this.neighbours.get(count).state[i].moveHour(j);
        this.neighbours.get(count).computeHeuristic();
        count++;
      }
    }

    return this.neighbours;
  }

  public int computeHeuristic() 
  {
    int N = Matkul.getTotalMatkul();
    int temp = 0;

    for (int i=0;i<N-1;i++) {
      for (int j=i+1;j<N;j++) {
        this.heuristic += this.state[i].computeIntersection(this.state[j]);
      }
    }

    return this.heuristic;
  }
}
