package php.codeGolf.training.learn.lesson.general

import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.kimpl.KLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonContext
import php.codeGolf.training.learn.lesson.kimpl.LessonSample

abstract class SingleLineCommentLesson(module: Module, lang: String) :
    KLesson("Comment Line", module, lang) {

  abstract val commentElementType : IElementType
  abstract val sample: LessonSample

  override val lessonContent: LessonContext.() -> Unit
    get() = {
      fun countCommentedLines() : Int =
          calculateComments(PsiDocumentManager.getInstance(project).getPsiFile(editor.document)!!)

      prepareSample(sample)

      actionTask("CommentByLineComment") {
        "Comment out any line with ${action(it)}."
      }
      task("CommentByLineComment") {
        text("Uncomment the commented line with the same shortcut, ${action(it)}.")
        trigger(it, {countCommentedLines()}, { _, now -> now == 0 } )
        test { actions("EditorUp", it) }
      }
      task("CommentByLineComment") {
        text("Select several lines and then comment them with ${action(it)}.")
        trigger(it, {countCommentedLines()}, { before, now -> now >= before + 2 } )
        test { actions("EditorDownWithSelection", "EditorDownWithSelection", it) }
      }
    }

  private fun calculateComments(psiElement: PsiElement): Int {
    return when {
      psiElement.node.elementType === commentElementType -> 1
      psiElement.children.isEmpty() -> 0
      else -> {
        var result = 0
        for (psiChild in psiElement.children) {
          result += calculateComments(psiChild)
        }
        result
      }
    }
  }
}
