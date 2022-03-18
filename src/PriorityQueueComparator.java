import java.util.Comparator;

public class PriorityQueueComparator implements Comparator<Post> {
    @Override
    public int compare(Post p1, Post p2){
        int p1Priority = p1.calculatePriority();
        int p2Priority = p2.calculatePriority();
        return p1Priority < p2Priority ? 1 : -1;
    }
}
