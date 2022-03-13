import java.util.ArrayList;

public class Instructor extends User{

    public Instructor(String username) {
        super("A0000", username);
    }

    public boolean answerQuestion(Post p, String response) {
        if (!(p instanceof Question)){
            return false;
        }
        ((Question) p).answerQuestion(response);
        if (!this.posts.contains(p)) {
            this.posts.add(p);
        }
        this.numOfPostsAnswered++;
        String userCourseID = p.parentPEID;
        PiazzaExchange postPE;
        for (PiazzaExchange pe : courses){
            if (pe.courseID.equals(userCourseID)){
                postPE = pe;
                postPE.unanswered.remove(p);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean endorsePost(Post p) {
        if (!p.endorsedByCourseStaff){
            p.endorsementCount++;
            p.poster.numOfEndorsement++;
            p.endorsedByCourseStaff = true;
            return true;
        }
        return false;
    }

    @Override
    public String displayName() {
        return "Instructor: " + this.username + ", PID: " + this.PID;
    }

    public Post deletePost(Post p, PiazzaExchange piazza) {
        try {
            boolean deletionSuccessful = piazza.deletePostFromDatabase(this,p);
            if (!deletionSuccessful){
                return null;
            }
        }
        catch (OperationDeniedException err){
            return null;
        }
        return p;
    }

    public boolean inactivatePiazza(PiazzaExchange piazza) {
        return piazza.deactivatePiazza(this);
    }

    public boolean editPost(Post p, String newText){
        p.editText(newText);
        if (!this.posts.contains(p)){
            this.numOfPostSubmitted++;
        }
        return true;
    }

    /**
     * get the top k urgent questions from a specific piazza
     *
     * @param pe the target Piazza
     * @param k the amount of urgent post that we want to get
     * @return the k urgent posts
     * @throws OperationDeniedException when the operation is denied
     */
    public Post[] getTopKUrgentQuestion(PiazzaExchange pe, int k) throws OperationDeniedException {
        Post[] arr = new Post[1]; // TODO: in the writeup, method signature is flipped
        if (k == 1){
            arr[0] = pe.computeMostUrgentQuestion();
            return arr;
        }
        else {
            return pe.computeTopKUrgentQuestion(k);
        }
    }
}
