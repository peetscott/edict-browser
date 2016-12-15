import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

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
   *
   * @param args There is a single optional argument: -min
   * Select entries marked with the .../(P)/ tag.
   */
  public static void main(String[] args)  {
    processOptions(args);
    edictToJavaScript();
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
              splitLine[1] = splitLine[1].replace("\"", "'");
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
        br.close();
        bw.close();
    }
    catch (java.io.FileNotFoundException ex)  {
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
}
