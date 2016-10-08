import java.util.*;

public class HillClimbing extends Algorithm
{
  private Matkul[] matkuls;
  private Node start;
  private int totalGeneratedNodes;

  public HillClimbing(Matkul[] _matkuls, HashMap<String, Ruangan> _ruangans)
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
    this.startState();
    Node currentNode = this.start;

    while (true) {
      ArrayList<Node> successors = currentNode.generateNeighbours();
      this.totalGeneratedNodes += successors.size();
      Node nextNode = null;
      
      for (int i=0;i<successors.size();i++) {
        if (successors.get(i).compareTo(currentNode) < 0) {
          nextNode = successors.get(i);
        }
      }

      if (nextNode == null) {
        return currentNode;
      }

      currentNode = nextNode;
    }
  }
}
