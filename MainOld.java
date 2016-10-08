import java.util.*;

public class Main 
{
  public static void main(String args[]) {
    InputProcessor parser = new InputProcessor(args[0]);

    // Sample getter ruangan
    System.out.println("Sample Getter Ruangan");

    Set<String> listNamaRuangan = parser.getListNamaRuangan();
    for (String namaRuangan : listNamaRuangan) {
      System.out.print(namaRuangan + ", ");
    }
    System.out.println();

    int openTime = parser.getOpenTimeRuangan("Labdas2");
    System.out.println(openTime);

    int closeTime = parser.getCloseTimeRuangan("Labdas2");
    System.out.println(closeTime);

    int[] workingDays = parser.getWorkingDaysRuangan("Labdas2");
    for (int day: workingDays) {
      System.out.print(day + " ");
    }
    System.out.println();
    
    // Sample getter jadwal
    System.out.println("\nSample Getter Jadwal");

    Set<String> listNamaMatkul = parser.getListNamaMatkul();
    for (String namaMatkul : listNamaMatkul) {
      System.out.print(namaMatkul + ", ");
    }
    System.out.println();

    String ruanganMatkul = parser.getRuanganMatkul("IF2110");
    System.out.println(ruanganMatkul);

    int startTime = parser.getStartTimeMatkul("IF2110");
    System.out.println(startTime);

    int endTime = parser.getEndTimeMatkul("IF2110");
    System.out.println(endTime);
    
    int durationMatkul = parser.getDurationMatkul("IF2110");
    System.out.println(durationMatkul);

    int[] daysMatkul = parser.getDaysMatkul("IF2110");
    for(int day: daysMatkul) {
      System.out.print(day + " ");
    }
    System.out.println();
  }
}
