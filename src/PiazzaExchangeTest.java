//import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PiazzaExchangeTest {
    Note thisFinal, announcement, checkingIn;
    Question whenFinalDue, curve, anyQuestions;
    Student prabina, courtney;
    Tutor scott, brian;
    Instructor sooh, marina;
    PiazzaExchange dsc30, dsc20;

    @Before
    public void setUp() throws Exception {
        thisFinal = new Note(prabina, "hello","01");
        announcement = new Note(scott, "final deadline", "final project is due soon!", "remind", "dsc30", "02" );
        checkingIn = new Note(sooh, "just checking in", "03");
        whenFinalDue = new Question(courtney, "final question", "when is the final project due?", "final", "A00000", "04");
        curve = new Question(brian, "curve", "will there be a curve?", "final", "dsc30", "05");
        anyQuestions = new Question(marina, "questions?", "Do you have any questions?", "final", "dsc20", "06");
        prabina = new Student("A16679415", "prabina-p");
        courtney = new Student("A00000", "courtney-c");
        scott = new Tutor("B00000", "scott-y");
        brian = new Tutor("C00000", "brian-w");
        sooh = new Instructor("soohyun-l");
        marina = new Instructor("marina-l");
        ArrayList<User> roster = new ArrayList<>();
        roster.add(prabina);
        roster.add(brian);
        roster.add(courtney);
        roster.add(sooh);
        roster.add(brian);
        dsc30 = new PiazzaExchange(sooh, roster);
        dsc20 = new PiazzaExchange(marina, "dsc20", true);
    }

    @Test
    public void getKeywordForest() {
    }

    @Test
    public void computeTopTwoEndorsedPosts() {
    }

    @Test
    public void getStudentContributions() {
    }

    @Test
    public void computeDailyPostStats() {
    }

    @Test
    public void computeMonthlyPostStats() {
    }

    @Test
    public void activatePiazza() {
    }

    @Test
    public void deactivatePiazza() {
    }

    @Test
    public void enrollUserToDatabase() {
    }

    @Test
    public void testEnrollUserToDatabase() {
    }

    @Test
    public void addPostToDatabase() throws OperationDeniedException {
        dsc30.activatePiazza(sooh);
        dsc30.addPostToDatabase(courtney, whenFinalDue);
//        System.out.println(dsc30.posts);
//        System.out.println(dsc30.unanswered);
//        System.out.println(courtney.numOfPostSubmitted);
//        System.out.println(courtney.displayName());
        System.out.println(courtney.courses);
        assertTrue(dsc30.users.contains(courtney));
        assertTrue(courtney.courses.contains(dsc30));
        for (PiazzaExchange pe: courtney.courses){
            System.out.println(pe.courseID);
        }
        System.out.println(Arrays.toString(dsc30.retrievePost(courtney)));
    }


    @Test
    public void retrievePost() {

    }

    @Test
    public void testRetrievePost() {
    }

    @Test
    public void testRetrievePost1() {
    }

    @Test
    public void deletePostFromDatabase() {
    }

    @Test
    public void computeMostUrgentQuestion() {
    }

    @Test
    public void computeTopKUrgentQuestion() {
    }

    @Test
    public void answerQuestion() {
    }

    @Test
    public void viewStats() {
    }

    @Test
    public void retrieveLog() {
    }

    @Test
    public void testRetrieveLog() {
    }

    @Test
    public void computeKSimilarPosts() {
    }

    @Test
    public void testComputeKSimilarPosts() {
    }

    @Test
    public void testToString() {
    }
}