import java.util.HashSet;
import java.util.Set;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputGenerator{

  public static class SetCoverInput{
        private Set<Integer> universe;
        private Set<Set<Integer>> sets;
        private List<Set<Integer>> setList;
        private int k;

        public void setUniverse(Set<Integer> universe){
          this.universe.addAll(universe);
        }
        public Set<Integer> getUniverse(){
            return this.universe;
        }
        public Set<Set<Integer>> getSets(){
            return this.sets;
        }
        public int getK(){
            return this.k;
        }
        public List<Set<Integer>> getSetList(){
          return this.setList;
        }
        public SetCoverInput(Set<Integer> universe, Set<Set<Integer>> sets, int k, List<Set<Integer>> setList){
            this.universe = universe;
            this.sets = sets;
            this.k = k;
            this.setList = setList;
        }
        public SetCoverInput(){}
    }

    // Used for the brute force algorithm - returns all combinations of sets
    public static class Combinations<E> {
        private final List<E> sets;
        private final long combos;
        private int index;

        public Combinations(List<E> setList) {
            index = 0;
            sets = setList;
            combos = (1 << sets.size()) - 1;
            System.out.println("Set combinations: " + combos);
        }
        public boolean hasNext() {
            return index <= combos;
        }
        public List<E> next() {
            List<E> newSet = new ArrayList<E>();
            int flag = 1;
            for (E set : sets) {
                if ((index & flag) != 0) {
                    newSet.add(set);
                }
                flag <<= 1;
            }
            ++index;
            return newSet;
        }
    }

    private static Set<Integer> setGenerator(int setSize, int universeSize){
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while(set.size() < setSize){
            set.add(random.nextInt(universeSize));
        }
        return set;
    }

    private static boolean validateInput(Set<Integer> universe, Set<Set<Integer>> sets){
        Set<Integer> allSets = new HashSet<>();
        for(Set<Integer> set : sets){
            allSets.addAll(set);
        }
        if(allSets.containsAll(universe)){
            return true;
        } else {
            return false;
        }
    }

    public static SetCoverInput inputGen(int universeSize, int numOfSets, int k){
        Random random = new Random();
        Set<Set<Integer>> sets = new HashSet<>();
        List<Set<Integer>> setList = new ArrayList<>();
        // Generate universe
        Set<Integer> universe = new HashSet<>();
        for(int i = 0; i < universeSize; i++) {
            universe.add(i);
        }
        // Generate sets
        while(sets.size() < numOfSets){
            Set<Integer> set = setGenerator(random.nextInt(universeSize), universeSize);
            sets.add(set);
        }
        while(!validateInput(universe, sets)){
            Set<Integer> set = setGenerator(random.nextInt(universeSize), universeSize);
            sets.add(set);
        }
        setList.addAll(sets);
        SetCoverInput input = new SetCoverInput(universe, sets, k, setList);
        return input;
    }
}
