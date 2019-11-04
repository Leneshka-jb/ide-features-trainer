package php.codeGolf.training.learn.lesson.ruby.basic

import com.intellij.psi.tree.IElementType
import org.jetbrains.plugins.ruby.ruby.lang.lexer.RubyTokenTypes
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.general.SingleLineCommentLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonSample
import php.codeGolf.training.learn.lesson.kimpl.parseLessonSample

class RubyCommentLesson(module: Module) : SingleLineCommentLesson(module, "ruby") {
  override val commentElementType: IElementType
    get() = RubyTokenTypes.TLINE_COMMENT
  override val sample: LessonSample
    get() = parseLessonSample("""
def foo
  p "Some method"
end

def hello
  p "Hello world"
end
""")
}