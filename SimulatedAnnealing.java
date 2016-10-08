import java.util.*;
import java.io.*;

public class SimulatedAnnealing extends Algorithm
{
  private Matkul[] matkuls;
  private Node start;
  private int totalGeneratedNodes;

  public SimulatedAnnealing(Matkul[] _matkuls, HashMap<String, Ruangan> _ruangans)
  {
    Matkul.setRuangans(_ruangans);
    Matkul.setTotalMatkul(_matkuls.length);

    this.matkuls = _matkuls;
    this.start = new Node();
    this.totalGeneratedNodes = 0;
  }

  public int getTotalGeneratedNodes()
  {
    return this.totalGeneratedNodes;
  }

  private void startState()
  {
    Matkul[] startState = this.matkuls;
    for (int i=0;i<this.matkuls.length;i++) {
      startState[i].setActive();
    }
    this.start.setState(startState);
    this.start.computeHeuristic();
  }

  public Node solveProblem()
  {
    double temperature = 13.0;
    double probability = 0.5;
    double step = 0.5;
    int delta = 0;

    this.startState();
    Node currentNode = this.start;
    Node deltaNode = this.start;
    Node resultNode = this.start; 

    while (temperature > 0) {
      ArrayList<Node> successors = currentNode.generateNeighbours();
      this.totalGeneratedNodes += successors.size();
      Node nextNode = null;
      
      for (int i=0;i<successors.size();i++) {
        if (successors.get(i).compareTo(currentNode) < 0) {
          nextNode = successors.get(i);
        } else {
          int nextDelta = Math.abs(currentNode.getHeuristic() - successors.get(i).getHeuristic());
           
          if (nextDelta < delta || delta == 0) {
            delta = nextDelta;
            deltaNode = successors.get(i);
          }
        }
      }

      if (nextNode != null) {
        currentNode = nextNode;
        
        if (currentNode.compareTo(resultNode) < 0) {
          resultNode = currentNode;
        }
      } else {
        probability = Math.exp(delta/temperature);

        if (Math.random() > probability) {
          return resultNode;
        }

        currentNode = deltaNode;
      }

      temperature -= step;
    }

    return resultNode;
  }
}
