public class Tutor extends User{


    public Tutor(String PID, String username){
        super(PID, username);

    }

    public boolean answerQuestion(Post p, String response){
        if (!(p instanceof Question)){
            return false;
        }
        ((Question) p).answerQuestion(response);
        if (!this.posts.contains(p)) {
            this.posts.add(p);
        } //TODO: need to add some more when doing PE
        this.numOfPostsAnswered++; //TODO: if question is alr answered, do we still answer it?
        return true;
    }

    @Override
    public String displayName() {
        return "Tutor: " + this.username + ", PID: " + this.PID;
    }

    @Override
    public boolean endorsePost(Post p){
        if (!p.endorsedByCourseStaff){
            p.endorsementCount++;
            p.poster.numOfEndorsement++;
            p.endorsedByCourseStaff = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean editPost(Post p, String newText) {
        p.editText(newText);
        if (!this.posts.contains(p)){
            this.numOfPostSubmitted++; // TODO: should we add p to posts?
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
