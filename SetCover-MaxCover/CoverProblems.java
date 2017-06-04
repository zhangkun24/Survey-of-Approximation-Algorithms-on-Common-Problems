import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;
import java.nio.file.Files;
import java.util.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;

class CoverProblems {
    static long totalTime;
    static Set<Integer> numEleCovered = new HashSet<>();

    // Calculates number of covered elements
    static int elementsCovered(Set<Integer> universe, Set<Integer> set){
        int covered = 0;
        for (int element: set) {
            if(universe.contains(element)){
                covered++;
            }
        }
        return covered;
    }

    // Finds set that covers most items
    static Set<Integer> maxCoverSet(InputGenerator.SetCoverInput input){
        int maxCovered = 0;
        int elementsCovered = 0;
        Set<Integer> selectedSet = new HashSet<>();
        for (Set<Integer> set: input.getSets()){
            elementsCovered = elementsCovered(input.getUniverse(), set);
            if(elementsCovered >= maxCovered) {
                maxCovered = elementsCovered;
                selectedSet = set;
            }
        }
        // Save number of elements covered
        numEleCovered.addAll(selectedSet);
        // Mark elements covered i.e., remove them
        input.getUniverse().removeAll(selectedSet);
        return selectedSet;
    }

    // Set Cover and Maximum K Coverage greedy algos
    static Set<Set<Integer>> greedy(InputGenerator.SetCoverInput input){
        Set<Set<Integer>> greedySolution = new HashSet<>();
        numEleCovered.clear();
        long startTime;
        // Set cover
        if(input.getK() == 0){
          startTime = System.nanoTime();
          while(!input.getUniverse().isEmpty()){
              greedySolution.add(maxCoverSet(input));
          }
        } else {  // Maximum coverage
          startTime = System.nanoTime();
          while(greedySolution.size() < input.getK()){
              if(input.getUniverse().isEmpty()){
                break;
              }
              greedySolution.add(maxCoverSet(input));
          }
        }
        totalTime = System.nanoTime() - startTime;
        return greedySolution;
    }

    // Finds an optimal solution via iterating through all combinations of sets
    // If k = 0, Set cover, else Max Cover
    static Set<Set<Integer>> bruteForce(InputGenerator.SetCoverInput input){
      InputGenerator.Combinations<Set<Integer>> sets = new InputGenerator.Combinations<>(input.getSetList());
      Set<Set<Integer>> solution = new HashSet<>();
      Set<Set<Integer>> tempSolution = new HashSet<>();
      Set<Integer> universe = new HashSet<>();
      Set<Integer> eleCovered = new HashSet<>();
      int minSolSize = Integer.MAX_VALUE;
      long startTime = System.nanoTime();
      numEleCovered.clear();

      int statusIndex = 0;

      // Find combinations that cover universe
      while(sets.hasNext()) {
        if(statusIndex % 100000 == 0){
          System.out.println("Processed " + String.valueOf(statusIndex) + " combinations");
        }
        universe.addAll(input.getUniverse());
        for(Set<Integer> set : sets.next()){
          universe.removeAll(set);
          tempSolution.add(set);
          eleCovered.addAll(set);
        }
        statusIndex++;
        if(input.getK() == 0){
          if(universe.isEmpty() && tempSolution.size() <= minSolSize){
            minSolSize = tempSolution.size();
            solution.clear();
            solution.addAll(tempSolution);
          }
        } else {
          if(universe.isEmpty() && tempSolution.size() < input.getK()){
            numEleCovered.clear();
            numEleCovered.addAll(eleCovered);
            solution.clear();
            solution.addAll(tempSolution);
            break;
          } else if(eleCovered.size() >= numEleCovered.size() && tempSolution.size() <= input.getK()){
            numEleCovered.clear();
            numEleCovered.addAll(eleCovered);
            solution.clear();
            solution.addAll(tempSolution);
          }
        }
        eleCovered.clear();
        tempSolution.clear();
      }
      totalTime = System.nanoTime() - startTime;
      return solution;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Set<Integer> bruteUniverse = new HashSet<>();
        boolean runSetCover = true;
        boolean showDetail = false;
        int universe = 0;
        int k = 0;
        int minSets = 0;

        try {
            int output = Integer.parseInt(args[0]);
            if(output > 0){
              showDetail = true;
            }
        } catch (Exception e) {
            System.err.println("Usage: java CoverProblems 0 (for minimum output)");
            System.err.println("Usage: java CoverProblems 1 (for more detailed output)");
            System.exit(1);
        }

        while(true){
            boolean validInput = false;
            System.out.println("Please enter total size of universe: ");
            while(!validInput){
              try{
                universe = scanner.nextInt();
                break;
              } catch (Exception ex){
                System.out.println("Please enter a numerical amount.");
                scanner.nextLine();
              }
            }
            System.out.println("Please enter the minimum number of sets to be generated: ");
            while(!validInput){
              try{
                minSets = scanner.nextInt();
                System.out.println("Note: more sets may be generated to ensure a feasible solution exists.");
                break;
              } catch (Exception ex){
                System.out.println("Please enter a numerical amount.");
                scanner.nextLine();
              }
            }
            // clear buffer
            scanner.nextLine();
            System.out.println("Are we doing SetCover? Y for yes, N for Max Coverage.");
            while(!validInput){
              try{
                String charInput = scanner.nextLine();
                if(charInput.trim().toLowerCase().equals("n")){
                  runSetCover = false;
                  System.out.println("Please choose number of maximum sets to be used greater than 0.");
                  while(!validInput){
                    try{
                      k = scanner.nextInt();
                      if(k > 0){
                        break;
                      } else {
                        System.out.println("Please enter a numerical amount greater than 0.");
                      }
                    } catch (Exception ex){
                      System.out.println("Please enter a numerical amount greater than 0.");
                      scanner.nextLine();
                    }
                  }
                  break;
                } else if(!charInput.trim().toLowerCase().equals("y")){
                  System.out.println("Please enter either Y or N.");
                } else {
                  validInput = true;
                  runSetCover = true;
                  k = 0;
                }
              } catch (Exception ex){
                System.out.println("Please enter either Y or N.");
                scanner.nextLine();
              }
            }
            System.out.println();

            long[] greedyTime = new long[10];
            int greedySols = 0;
            long greedyRunTime = 0;
            long bruteRunTime = 0;
            long[] bruteTime = new long[10];
            int bruteSols = 0;
            List<String> lines = new ArrayList<>();
            List<String> bLines = new ArrayList<>();
            InputGenerator.SetCoverInput input = new InputGenerator.SetCoverInput();

            if(runSetCover){
              lines.add("Set Cover");
            } else {
              lines.add("Maximum Coverage");
            }
            bLines.add("Brute Force:");

            for(int i = 0; i < 10; i++){
              // Generate input for the algo's based on input
              InputGenerator generator = new InputGenerator();
              input = generator.inputGen(universe, minSets, k);
              System.out.println(input.getSets().size() + " sets initialized in a universe of " + input.getUniverse().size() + " elements.");
              System.out.println();
              System.out.println("**************************************");
              bruteUniverse.addAll(input.getUniverse()); // needed as input object's universe member is cleared in greedy algo

              // Run greedy algo (Set cover or Max K Cover)
              Set<Set<Integer>> greedySol = greedy(input);
              if(runSetCover){
                System.out.println("Set Cover complete.");
                System.out.println(greedySol.size() + " sets used to cover all elements.");
              } else {
                System.out.println("Maximum Coverage complete.");
                System.out.println(numEleCovered.size() + " out of " + universe + " elements covered with " + greedySol.size() + " sets.");
              }
              if(showDetail){
                System.out.println("Sets: ");
                for(Set<Integer> set : greedySol){
                  System.out.println(set);
                }
              }
              System.out.println("In " + totalTime + " nanoseconds.");
              greedyTime[i] = totalTime;
              greedyRunTime = greedyRunTime + totalTime;
              greedySols = greedySols + greedySol.size();

              // Run brute force algo
              System.out.println();
              System.out.println("**************************************");
              System.out.println("Brute Force Complete: ");
              input.setUniverse(bruteUniverse);
              Set<Set<Integer>> solution = bruteForce(input);
              if(runSetCover){
                System.out.println(solution.size() + " sets used to cover all elements.");
                if(showDetail){
                  System.out.println("Sets: ");
                  for(Set<Integer> set : solution){
                    System.out.println(set);
                  }
                }
              } else {
                System.out.println(numEleCovered.size() + " out of " + universe + " elements covered with " + solution.size() + " sets.");
                if(showDetail){
                  System.out.println("Sets: ");
                  for(Set<Integer> set : solution){
                    System.out.println(set);
                  }
                }
              }
              System.out.println("In " + totalTime + " nanoseconds.");
              bruteTime[i] = totalTime;
              bruteRunTime = bruteRunTime + totalTime;

              lines.add(String.valueOf(greedyTime[i]));
              bLines.add(String.valueOf(bruteTime[i]));
            }
            greedyRunTime = greedyRunTime/10;
            greedySols = greedySols/10;
            bruteRunTime = bruteRunTime/10;

            lines.add("");
            lines.addAll(bLines);
            lines.add("");
            lines.add("Universe Size: " + String.valueOf(universe));
            lines.add("Set Size: " + String.valueOf(input.getSets().size()));
            if(!runSetCover){
              lines.add("K = " + String.valueOf(input.getK()));
            }
            lines.add("");
            lines.add("Average Greedy Run Time: " + String.valueOf(greedyRunTime));
            lines.add("Average Greedy Solution: " + String.valueOf(greedySols));
            lines.add("Average Brute Force Run Time: " + String.valueOf(bruteRunTime));
            lines.add("");
            lines.add("");

            Path file = Paths.get("cover_problems_data.txt");
            try{
              Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch(Exception ex){
              System.out.println("ERROR with writing file" + ex.toString());
            }

            // Ask to run program again
            //input = null; // try to force JVM to delete input object
            validInput = false;
            System.out.println();

            System.out.println("Run again? Y for yes, N for no.");
            while(!validInput){
              try{
                String quit = scanner.nextLine();
                if(quit.trim().toLowerCase().equals("n")){
                  System.exit(0);
                } else if(!quit.trim().toLowerCase().equals("y")){
                  System.out.println("Please enter either Y or N.");
                } else {
                  validInput = true;
                }
              } catch (Exception ex){
                System.out.println("Please enter either Y or N.");
                scanner.nextLine();
              }
            }
            System.out.println();
        }
    }
}
