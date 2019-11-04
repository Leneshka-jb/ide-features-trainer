package php.codeGolf.training.learn.lesson.general

import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.kimpl.KLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonContext
import php.codeGolf.training.learn.lesson.kimpl.LessonSample

abstract class CompletionWithTabLesson(module: Module, lang: String, private val proposal: String) :
    KLesson("Completion with Tab", module, lang) {

  abstract val sample: LessonSample

  override val lessonContent: LessonContext.() -> Unit
    get() = {
      prepareSample(sample)

      actionTask("CodeCompletion") { "Press ${action(it)} to show completion options." }

      actionTask("EditorChooseLookupItemReplace") {
        "Choose <code>$proposal</code>, for example, and press ${action("EditorTab")}. " +
          "This overwrites the word at the caret rather than simply inserting it."
      }
    }
}
