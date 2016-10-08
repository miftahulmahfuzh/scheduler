import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class InputProcessor 
{
  // ATTRIBUTE
  private String filePath;
  private List<String> rawData;
  private HashMap<String, String[]> ruangan;
  private HashMap<String, String[]> jadwal;

  // CONSTRUCTOR
  public InputProcessor(String _filePath)
  {
    this.setFilePath(_filePath);
    this.setRawData(null);
    this.setRuangan(null);
    this.setJadwal(null);
  }

  // SETTER
  public void setFilePath(String _filePath)
  {
    this.filePath = _filePath;
  }

  public void setRawData(String _filePath)
  {
    if (_filePath != null) {
      this.setFilePath(_filePath);
    }

    List<String> result = new ArrayList<>();

    try {
      result = Files.readAllLines(Paths.get(this.filePath), StandardCharsets.UTF_8);
    } catch(IOException e) {
      System.out.println(e);
    }

    this.rawData = result;
  }

  public void setRuangan(HashMap<String, String[]> _ruangan)
  {
    if (_ruangan == null) {
      this.ruangan = this.parseRaw(true);
    } else {
      this.ruangan = _ruangan;
    }
  }

  public void setJadwal(HashMap<String, String[]> _jadwal)
  {
    if (_jadwal == null) {
      this.jadwal = this.parseRaw(false);
    } else {
      this.jadwal = _jadwal;
    }
  }

  // PARSER
  private int[] stringToInt(String[] s) 
  {
    int[] result = new int[s.length]; 

    for(int i=0;i<s.length;i++) {
      result[i] = Math.round(Float.parseFloat(s[i]));
    }

    return result;
  }

  private HashMap<String, String[]> parseRaw(boolean isRuangan)
  {
    List<String[]> result = new ArrayList<>();
    boolean foundJadwal = false;

    for(String data: this.rawData) {
      if (isRuangan) {
        if (data.equals("Ruangan")) {
          continue;
        } else if (data.equals("Jadwal")) {
          break;
        }
      } else {
        if (!data.equals("Jadwal") && !foundJadwal) {
          continue;
        } else if (data.equals("Jadwal")) {
          foundJadwal = true;
        }
      }

      String[] attr = data.split(";");
      result.add(attr);
    }

    if (isRuangan) {
      result.remove(result.size() - 1); 
    } else {
      result.remove(0); 
    }

    HashMap<String, String[]> hm = new HashMap<String, String[]>();

    for(String[] attr: result) {
      String[] content = new String[attr.length];

      for(int i=1;i<attr.length;i++) {
        content[i-1] = attr[i];
      }

      String tmp = attr[0];

      if (!isRuangan) {
        content[attr.length-1] = attr[0];
        int inc = 0;

        while (true) {
          String[] value = hm.get(tmp);
          if (value == null) {
            break;
          }

          tmp = attr[0] + Integer.toString(inc++);
        }
      }
      
      hm.put(tmp, content);
    }
     
    return hm;
  }

  // GETTER
  public List<String> getRawData()
  {
    return this.rawData;
  }

  public Set<String> getListNamaRuangan()
  {
    return this.ruangan.keySet(); 
  }

  public int getOpenTimeRuangan(String ruanganName) 
  {
    String[] content = this.ruangan.get(ruanganName); 

    if (content != null) {
      int openTime = Math.round(Float.parseFloat(content[0]));
      return openTime;
    } 

    return -1;
  }

  public int getCloseTimeRuangan(String ruanganName) 
  {
    String[] content = this.ruangan.get(ruanganName); 

    if (content != null) {
      int closeTime = Math.round(Float.parseFloat(content[1]));
      return closeTime;
    } 

    return -1;
  }

  public int[] getWorkingDaysRuangan(String ruanganName) 
  {
    String[] content = this.ruangan.get(ruanganName); 

    if (content != null) {
      int[] days = this.stringToInt(content[2].split(","));
      return days;
    } 

    return null;
  }

  public int getTotalTimeRuangan()
  {
    int total = 0;

    for (String namaRuangan : this.getListNamaRuangan()) {
      total += this.getWorkingDaysRuangan(namaRuangan).length * 
        (this.getCloseTimeRuangan(namaRuangan) - this.getOpenTimeRuangan(namaRuangan));
    }

    return total;
  }

  public Set<String> getListNamaMatkul()
  {
    return this.jadwal.keySet();
  }

  public String getRuanganMatkul(String matkulName)
  {
    String[] content = this.jadwal.get(matkulName);

    if (content != null) {
      return content[0];
    }

    return null;
  }

  public String getNamaMatkul(String matkulName)
  {
    String[] content = this.jadwal.get(matkulName);

    if (content != null) {
      return content[5];
    }

    return "";
  }

  public int getStartTimeMatkul(String matkulName)
  {
    String[] content = this.jadwal.get(matkulName);

    if (content != null) {
      int startTime = Math.round(Float.parseFloat(content[1]));
      return startTime;
    }

    return -1;
  }

  public int getEndTimeMatkul(String matkulName)
  {
    String[] content = this.jadwal.get(matkulName);

    if (content != null) {
      int endTime = Math.round(Float.parseFloat(content[2]));
      return endTime;
    }

    return -1;
  }

  public int getDurationMatkul(String matkulName)
  {
    String[] content = this.jadwal.get(matkulName);

    if (content != null) {
      int duration = Integer.parseInt(content[3]);
      return duration;
    }

    return -1;
  }

  public int[] getDaysMatkul(String matkulName)
  {
    String[] content = this.jadwal.get(matkulName);

    if (content != null) {
      int[] days = this.stringToInt(content[4].split(","));
      return days;
    }

    return null;
  }
}
