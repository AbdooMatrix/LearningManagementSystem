package com.lms.LearningManagementSystem.Services;
import com.lms.LearningManagementSystem.Models.Assessment.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
public class AssessmentService {
    private List<Quiz> quizzes = new ArrayList<>();
    private List<Assignment> assignments = new ArrayList<>();
    private List<Grading> gradings = new ArrayList<>();
    private Long idCounter = 1L;
    private Long idCount=1L;
    private Long idAssignmentCounter = 1L;
   // List<Question> selectedQuestions = new ArrayList<>();
    public List<Question> Questions= new ArrayList<>();
    public Map<String, String> submission = new HashMap<>();

    // Create Quiz
    public Quiz createQuiz(String title, int num, int totalMarks) {
        // Ensure the requested number of questions does not exceed the available questions
        if (num > Questions.size()) {
            throw new IllegalArgumentException("Requested number of questions exceeds the available question bank size.");
        }
        // Shuffle the question bank
        Collections.shuffle(Questions);
        // Select the first 'num' questions from the shuffled list
        List<Question> selectedQuestions = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Question originalQuestion = Questions.get(i);
            // Create a new Question object (deep copy)
            Question copiedQuestion = new Question(
                    originalQuestion.getId(),
                    originalQuestion.getText(),
                    originalQuestion.getOptions(),
                    null // Remove the correct answer for the quiz
            );
            selectedQuestions.add(copiedQuestion);
        }
        // Create the quiz with selected questions
        Quiz quiz = new Quiz(idCounter++, title, totalMarks, selectedQuestions);

        // Add the quiz to the list of quizzes
        quizzes.add(quiz);

        return quiz;
    }
    public void SubmitQuiz(Long quizId, Map<String, String> answers) {
        // Iterate over the Map's entry set
        // Iterate over the entries in each map
            for (Map.Entry<String, String> ans : answers.entrySet()) {
                String questionID = ans.getKey();  // Get the question ID (key)
                String answer = ans.getValue();  // Get the answer (value)
                submission.put(questionID,answer);  // Add the new map to the list

        }
    }

    public int correctAnswersCount (Long quizId,Long studentId) {
        int count = 0;
        for (Map.Entry<String, String> ans : submission.entrySet()) {
            String questionID = ans.getKey();  // Get the question ID (key)
            String answer = ans.getValue();  // Get the answer (value)
            for(Question q: Questions){
                if(q.getId().equals(Long.parseLong(questionID))){
                    if(q.getCorrectAnswer().equals(answer)) {
                        count++;
                    }
                }
            }
        }
        gradings.add(new Grading(quizId, studentId, count));
        return count;
    }



    // create Questions bank
    public void addQuestions(List<Question> questions) {
        if (questions != null && !questions.isEmpty()) {
            for (Question question : questions) {
                // Add each question to the question bank or database
                Questions.add(question);
            }
        } else {
            // Handle empty or null list gracefully
            throw new IllegalArgumentException("No questions provided!");
        }
    }

    public List<Question> GetQuestions() {
        return new ArrayList<>(Questions);
    }

        // Get Quiz by ID
        public Quiz findQuizById (Long id){
            for (Quiz quiz : quizzes) {
                if (quiz.getId().equals(id)) {
                    return quiz;
                }
            }
            return null; // Return null if no quiz is found
        }

        public List<Quiz> GetAllquizzes () {
            return new ArrayList<>(quizzes);
        }

        // Create Assignment
        public Assignment createAssignment (String title, String description){
            Assignment assignment = new Assignment(idCounter++, title, description);
            assignments.add(assignment);
            return assignment;
        }

        // Submit Assignment
        public void submitAssignment (Long assignmentId, String fileName, Long studID){
            Assignment assignment = findAssignmentById(assignmentId);
            if (assignment != null) {
                assignment.submitFile(fileName,studID);
            }
        }

        // Get Assignment by ID
        public Assignment findAssignmentById (Long id){
            for (Assignment a : assignments) {
                if (a.getId().equals(id)) {
                    return a;
                }
            }
            return null; // Return null if no quiz is found
        }
        public List<Assignment> GetAllAssignments () {
            return new ArrayList<>(assignments);
        }

        // Grade Assessment
        public void gradeAssignment (Long studentId, Long assessmentId,int marks, String feedback){
            gradings.add(new Grading(idAssignmentCounter++, studentId, assessmentId, marks, feedback));
        }

        // Get Gradings
        public List<Grading> getGradingsForStudent (Long studentId){
            List<Grading> result = new ArrayList<>();
            for (Grading grading : gradings) {
                if (grading.getStudentId().equals(studentId)) {
                    result.add(grading);
                }
            }
            return result;
        }
    }