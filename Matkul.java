import java.util.*;

public class Matkul
{
  // ATTRIBUTE
  private static int totalMatkul;
  public static HashMap<String, Ruangan> ruangans;
  private String ruangan;
  private String name;
  private int startTime;
  private int endTime;
  private int duration;
  private int[] days;
  private Ruangan ruanganActive;
  private int dayActive;
  private int startHourActive;

  // CONSTRUCTOR
  public Matkul(String _name, String _ruangan, int _startTime, int _endTime, int _duration, int[] _days)
  {
    this.name = _name;
    this.ruangan = _ruangan;
    this.startTime = _startTime;
    this.endTime = _endTime;
    this.duration = _duration;
    this.days = _days;
    this.ruanganActive = null;
    this.dayActive = 0;
    this.startHourActive = 0;
  }

  public Matkul(Matkul m)
  {
    this.name = m.name;
    this.ruangan = m.ruangan;
    this.startTime = m.startTime;
    this.endTime = m.endTime;
    this.duration = m.duration;

    this.days = new int[m.days.length];
    for (int i=0;i<m.days.length;i++) {
      this.days[i] = m.days[i];
    }

    if (m.ruanganActive != null) {
      this.ruanganActive = new Ruangan(m.ruanganActive);
    }

    this.dayActive = m.dayActive;
    this.startHourActive = m.startHourActive;
  }

  // SETTER
  public static void setTotalMatkul(int _totalMatkul)
  {
    Matkul.totalMatkul = _totalMatkul;
  }

  public static void setRuangans(HashMap<String, Ruangan> _ruangans)
  {
    Matkul.ruangans = _ruangans;
  }

  
  public void copyActive(Matkul _matkul)
  {
    this.ruanganActive = new Ruangan(_matkul.ruanganActive);
    this.dayActive = _matkul.dayActive;
    this.startHourActive = _matkul.startHourActive; 
  }

  public boolean isRuanganStrict()
  {
    return !this.ruangan.equals("-");
  }

  public void setActive()
  {
    Random gen = new Random();
    if (!this.isRuanganStrict()) {
      Object[] _ruangans = Matkul.ruangans.values().toArray();
      this.ruanganActive = (Ruangan)_ruangans[gen.nextInt(_ruangans.length)];
    } else {
      this.ruanganActive = Matkul.ruangans.get(this.ruangan);
    }

    this.setDayActive(gen.nextInt(5) + 1);
    this.setStartHourActive(gen.nextInt(11) + 1);
  }

  public boolean isDayValid(int _day)
  {
    int[] days = this.ruanganActive.getWorkingDays();
    for (int day : days) {
      if (day == _day) {
        return true;
      }
    }
    return false;
  }

  public void setDayActive(int _dayActive)
  {
    if (this.isDayValid(_dayActive)) {
      this.dayActive = _dayActive;
    } else {
      Random gen = new Random();
      int[] days = this.ruanganActive.getWorkingDays();
      this.dayActive = days[gen.nextInt(days.length)]; 
    }
  }

  public boolean isHourValid(int _hour)
  {
    int max = this.ruanganActive.getCloseTime(); 
    int min = this.ruanganActive.getOpenTime(); 
    return (min <= _hour && _hour <= max);
  }

  public void setStartHourActive(int _startHourActive)
  {
    if (this.isHourValid(_startHourActive)) {
      this.startHourActive = _startHourActive;
    } else {
      Random gen = new Random();
      int max = this.ruanganActive.getCloseTime(); 
      int min = this.ruanganActive.getOpenTime(); 
      int range = max - this.duration - min + 1;
      if (range <= 0) {
        range = 2;
      }

      this.startHourActive = gen.nextInt(range) + min;
    }
  }

  // GETTER
  public String getNamaMatkul()
  {
    return this.name;
  }

  public static int getTotalMatkul()
  {
    return Matkul.totalMatkul;
  }

  public static int getTotalRuangan()
  {
    return Matkul.ruangans.size();
  }

  public int getDayActive()
  {
    return this.dayActive;
  }

  public int getStartHourActive()
  {
    return this.startHourActive;
  }

  public int getEndHourActive()
  {
    return this.startHourActive + this.duration;
  }

  public Ruangan getRuanganActive()
  {
    return this.ruanganActive;
  }

  public String toString()
  {
    String result = "Nama Matkul : " + this.name + ", ";
    result += "Ruangan : " + this.ruanganActive.getName() + ", ";
    result += "Hari : " + this.dayActive + ", ";
    result += "Pukul : " + this.startHourActive + " - " + this.getEndHourActive();
    return result;
  }

  // MOVE OBJECT
  public void moveHour(int _hour)
  {
    int max = this.ruanganActive.getCloseTime();
    int min = this.ruanganActive.getOpenTime();
    if (this.getEndHourActive() + _hour > max) {
      this.startHourActive = _hour%this.duration + min;
    } else {
      this.startHourActive += _hour;
    }
  } 

  public void moveDay(int _day)
  {
    this.setDayActive(this.dayActive + _day);
  }

  public void moveRuangan()
  {
    this.setActive();
  }

  // CHECK CONFLICT
  public int computeIntersection(Matkul m) 
  {
    if (this.dayActive == m.dayActive && this.ruanganActive.getName().equals(m.ruanganActive.getName())) {
      boolean before = (this.startHourActive <= m.startHourActive && this.getEndHourActive() >= m.startHourActive);
      if (before) {
        if (this.getEndHourActive() < m.getEndHourActive()) {
          return this.getEndHourActive() - m.startHourActive + 1;
        } else {
          return m.duration;
        }
      }

      boolean after = (m.startHourActive <= this.startHourActive && m.getEndHourActive() >= this.startHourActive);
      if (after) {
        if (m.getEndHourActive() < this.getEndHourActive()) {
          return m.getEndHourActive() - this.startHourActive + 1;
        } else {
          return this.duration;
        }
      }
    }

    return 0;
  }
}
