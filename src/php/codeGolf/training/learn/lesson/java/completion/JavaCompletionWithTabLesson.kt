package php.codeGolf.training.learn.lesson.java.completion

import php.codeGolf.training.lang.JavaLangSupport
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.general.CompletionWithTabLesson
import php.codeGolf.training.learn.lesson.kimpl.parseLessonSample

class JavaCompletionWithTabLesson(module: Module) :
    CompletionWithTabLesson(module, JavaLangSupport.lang, "DO_NOTHING_ON_CLOSE") {
  override val sample = parseLessonSample("""import javax.swing.*;

class FrameDemo {

    public static void main(String[] args) {
        JFrame frame = new JFrame("FrameDemo");
        frame.setSize(175, 100);

        frame.setDefaultCloseOperation(WindowConstants.<caret>DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}""".trimIndent())
}