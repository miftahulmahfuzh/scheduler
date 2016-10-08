public class Ruangan
{
  private String name;
  private int openTime;
  private int closeTime;
  private int[] workingDays;
  public boolean[][] activeHours;

  public Ruangan(String _name, int _openTime, int _closeTime, int[] _workingDays)
  {
    this.name = _name;
    this.openTime = _openTime;
    this.closeTime = _closeTime;
    this.workingDays = _workingDays;
    this.activeHours = new boolean[6][_closeTime+1];
    for (int i=0;i<6;i++) {
      for (int j=0;j<_closeTime+1;j++) {
        this.activeHours[i][j] = false;
      }
    }
  }

  public Ruangan(Ruangan r)
  {
    this.name = r.name;
    this.openTime = r.openTime;
    this.closeTime = r.closeTime;
    this.workingDays = new int[r.workingDays.length];
    for (int i=0;i<r.workingDays.length;i++) {
      this.workingDays[i] = r.workingDays[i];
    }  
    this.activeHours = new boolean[6][r.closeTime+1];
    for (int i=0;i<6;i++) {
      for (int j=0;j<r.closeTime+1;j++) {
        this.activeHours = r.activeHours;
      }
    }
  }

  public String getName()
  {
    return this.name;
  }

  public int getOpenTime()
  {
    return this.openTime;
  }

  public int getCloseTime()
  {
    return this.closeTime;
  }

  public int[] getWorkingDays()
  {
    return this.workingDays;
  }
}
