package php.codeGolf.training.learn.lesson.java

import com.intellij.psi.JavaTokenType
import com.intellij.psi.tree.IElementType
import php.codeGolf.training.lang.JavaLangSupport
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.general.SingleLineCommentLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonSample
import php.codeGolf.training.learn.lesson.kimpl.parseLessonSample

class JavaCommentLesson(module: Module) : SingleLineCommentLesson(module, JavaLangSupport.lang) {
  override val commentElementType: IElementType
    get() = JavaTokenType.END_OF_LINE_COMMENT
  override val sample: LessonSample
    get() = parseLessonSample("""import java.awt.Color;

class CommentDemo {
    public static void main() {

        float hue = 5;
        float saturation = 10;
        float brightness = 10;

        int rgb = Color.HSBtoRGB(hue, saturation, brightness);
        <caret>int red = (rgb >> 16) &0xFF;
        int green = (rgb >> 8) &0xFF;
        int blue = rgb &0xFF;

    }
}""")
}