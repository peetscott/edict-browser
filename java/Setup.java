import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * The setup program for edict-browser.
 */
public class Setup  {

  // Select a subset of the dictionary? ..
  private static boolean min = false;

  /**
   * Usage: java Setup [-min]
   *
   * For now edict must be located in the current directory.
   * Produces a file, edict.js.
   * The dictionary is in Edict.entries, a two-dimensional array.
   * The entries are (re-)ordered by reading.
   *
   * @param args There is a single optional argument: -min
   * Select entries marked with the .../(P)/ tag.
   */
  public static void main(String[] args)  {
    processOptions(args);
    //edictToJavaScript();
    edictToJavaScriptSorted();  // pre-sort the dictionary
  }

  /**
   * Transforms Edict into a two-dimensional JavaScript Array.
   * Edict.entries = [..., ["entry", "reading", "gloss"], ...];
   */
  public static void edictToJavaScript()  {

    BufferedReader br = null;
    BufferedWriter bw = null;
    String line;
    String subset = "/(P)/";  // Flag that marks a minimal subset in edict.
    String[] splitLine;
    boolean first = true;

    try  {
      br = new BufferedReader(
             new InputStreamReader(
               new FileInputStream("edict"),
               "ISO-8859-1"
             )
           );
      bw = new BufferedWriter(
             new OutputStreamWriter(
               new FileOutputStream("edict.js"),
               "ISO-8859-1"
             )
           );
      line = br.readLine();
      bw.write("var Edict = {};\n");
      bw.write("Edict.version = \"");
      bw.write(line);
      bw.write("\";\n");
      // Start the array
      bw.write("Edict.entries = [\n");
      // Assemble the entries
      line = br.readLine();
      while (line != null)  {
          if (min)  {  // Filtering the minimal subset? ..
            // Look for ".../(P)/"
            if (!line.substring(line.length() - 5).equals(subset))  {
              line = br.readLine();
              continue;
            }
          }
          splitLine = line.split(" ", 2);
          // Terminate previous array literal (skip on first entry):
          if (!first)  {
              bw.write(",");
          }
          // Start array literal:
          bw.write("[");
          bw.write("\"");
          bw.write(splitLine[0]);
          bw.write("\",");
          bw.write("\"");
          // Test to see if there's a reading(it's optional):
          if (splitLine[1].charAt(0) == '[')  {
              splitLine = splitLine[1].split(" ", 2);
              String reading = splitLine[0].substring(1, splitLine[0].length() - 1);
              bw.write(reading);
              bw.write("\",");
              bw.write("\"");
              // Double quotes won't work in JavaScript string.
              // Convert to single quote:
              //splitLine[1] = splitLine[1].replace("\"", "'");
              splitLine[1] = splitLine[1].replace("\"", "\\\"");
              bw.write(splitLine[1]);
              bw.write("\"");
          }
          else  {  // No reading
              bw.write("\",");
              bw.write("\"");
              // Double quotes won't work in JavaScript string.
              // Convert to single quote:
              //splitLine[1] = splitLine[1].replace("\"", "'");
              // Or escape:
              splitLine[1] = splitLine[1].replace("\"", "\\\"");
              bw.write(splitLine[1]);
              bw.write("\"");
          }
          // End array literal:
          bw.write("]");
          first = false;
          line = br.readLine();
        }

        // End the array
        bw.write("];\n");
        // Flush buffer...
        //br.close();
        //bw.close();
    }
    catch (FileNotFoundException ex)  {
      System.out.println("Error: edict file not found.");
    }
    catch (Exception ex) {
      System.out.println(ex.toString());
    }
    finally  {
      try  {
        if (br != null) br.close();
        if (bw != null) bw.close();
      }
      catch (IOException ex)  {}
    }
  }

  /**
   * Same as edictToJavaScript() except that the lines are sorted
   * by reading before building edict.js.
   */
  public static void edictToJavaScriptSorted()  {

    RandomAccessFile raf = null;
    BufferedWriter bw = null;
    EdictLine[] lines;
    String line;
    String subset = "/(P)/";  // Flag that marks a minimal subset in edict.
    String[] splitLine;
    boolean first = true;

    try  {
      // This reads the dictionary and sorts the lines by reading ..
      lines = getLines();
      // re-read the file using the sort order in lines ..
      raf = new RandomAccessFile("edict", "r");
      bw = new BufferedWriter(
             new OutputStreamWriter(
               new FileOutputStream("edict.js"),
               "ISO-8859-1"
             )
           );
      line = raf.readLine();
      bw.write("var Edict = {};\n");
      bw.write("Edict.version = \"");
      bw.write(line);
      bw.write("\";\n");
      // Start the array
      bw.write("Edict.entries = [\n");
      for (int i = 0; i < lines.length; ++i)  {
        // lines is sorted.
        // EdictLine.pos is the position of the line in the file.
        raf.seek(lines[i].pos);
        line = raf.readLine();
        if (min)  {  // Filtering the minimal subset? ..
          // Look for ".../(P)/"
          if (!line.substring(line.length() - 5).equals(subset))  {
            //line = br.readLine();
            continue;
          }
        }
        splitLine = line.split(" ", 2);
        // Terminate previous array literal (skip on first entry):
        if (!first)  {
            bw.write(",");
        }
        // Start array literal:
        bw.write("[");
        bw.write("\"");
        bw.write(splitLine[0]);
        bw.write("\",");
        bw.write("\"");
        // Test to see if there's a reading(it's optional):
        if (splitLine[1].charAt(0) == '[')  {
            splitLine = splitLine[1].split(" ", 2);
            String reading = splitLine[0].substring(1, splitLine[0].length() - 1);
            bw.write(reading);
            bw.write("\",");
            bw.write("\"");
            // Double quotes won't work in JavaScript string.
            // Convert to single quote:
            //splitLine[1] = splitLine[1].replace("\"", "'");
            splitLine[1] = splitLine[1].replace("\"", "\\\"");
            bw.write(splitLine[1]);
            bw.write("\"");
        }
        else  {  // No reading
            bw.write("\",");
            bw.write("\"");
            // Double quotes won't work in JavaScript string.
            // Convert to single quote:
            //splitLine[1] = splitLine[1].replace("\"", "'");
            // Or escape:
            splitLine[1] = splitLine[1].replace("\"", "\\\"");
            bw.write(splitLine[1]);
            bw.write("\"");
        }
        // End array literal:
        bw.write("]");
        first = false;
      }

      // End the array
      bw.write("];\n");
    }
    catch (java.io.FileNotFoundException ex)  {
      System.out.println("Error: edict file not found.");
    }
    catch (Exception ex) {
      System.out.println(ex.toString());
    }
    finally  {
      try  {
        if (raf != null) raf.close();
        if (bw != null) bw.close();
      }
      catch (java.io.IOException ex)  {}
    }
  }

  /*
   * There is a single option.
   * -min  Take only a subset of the dictionary:
   *       entries marked with /(P)/.
   */
  private static void processOptions(String[] args)  {
    //String option;
    for (int i = 0; i < args.length; ++i)  {
      if (args[i].equals("-min"))  {
        min = true;
      }
    }
  }

  /*
   * Reads edict and orders the lines by reading.
   *
   * @return An array of EdictLine, sorted by reading, entry.
   */
  private static EdictLine[] getLines()
    throws java.io.IOException,
           java.io.FileNotFoundException  {
    long pos;
    String line;
    RandomAccessFile raf =
      new RandomAccessFile("edict", "r");;
    ArrayList<EdictLine> al =
      new ArrayList<EdictLine>(250000);;
    EdictLine[] lines;

    raf.readLine();  // skip header
    pos = raf.getFilePointer();
    line = raf.readLine();
    while (line != null)  {
      EdictLine el = new EdictLine();
      el.pos = pos;
      String[] splitLine = line.split(" ", 2);
      el.entry =  // want unicode ordering ..
        new String(splitLine[0].getBytes(), "EUC-JP");
      if (splitLine[1].charAt(0) == '[')  {
        splitLine = splitLine[1].split(" ", 2);
        el.reading =  // strip brackets [..], convert from euc to utf ..
          new String(splitLine[0].
                     substring(1, splitLine[0].length() - 1).
                     getBytes(),
                     "EUC-JP");
      }
      else  {
        el.reading = "";
      }
      al.add(el);
      pos = raf.getFilePointer();
      line = raf.readLine();
    }
    if (raf != null) raf.close();
    lines = al.toArray(new EdictLine[0]);
    java.util.Arrays.sort(lines, new EdictLine());
    return lines;
  }

  /*
   * A slice from a line in edict, with its position in the file.
   * Used to order the lines by reading.
   */
  static class EdictLine implements Comparator<EdictLine>  {
    long pos;  // The start of the line.
    String entry;
    String reading;

    public int compare(EdictLine el1, EdictLine el2)  {
      String s1 = el1.reading;
      if (s1.equals(""))  {
        s1 = el1.entry;
      }
      String s2 = el2.reading;
      if (s2.equals(""))  {
        s2 = el2.entry;
      }
      int v = s1.compareTo(s2);
      if (v == 0)  {
        v = el1.entry.compareTo(el2.entry);
      }
      return v;
    }
  }
}
