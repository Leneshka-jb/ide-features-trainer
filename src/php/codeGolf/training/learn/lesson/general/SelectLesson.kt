package php.codeGolf.training.learn.lesson.general

import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.kimpl.KLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonContext
import php.codeGolf.training.learn.lesson.kimpl.LessonSample

class SelectLesson(module: Module, lang: String, private val sample: LessonSample) :
    KLesson("Select", module, lang) {
  override val lessonContent: LessonContext.() -> Unit
    get() = {
      prepareSample(sample)

      actionTask("EditorNextWordWithSelection") {
        "Place the caret before any word. Press ${action(it)} to move the caret to the next word and select everything in between."
      }
      actionTask("EditorSelectWord") {
        "Press ${action(it)} to extend the selection to the next code block."
      }
      task("EditorSelectWord") {
        text("Try increasing your selection with ${action(it)} until your whole file is selected.")
        trigger(it) {
          editor.selectionModel.selectionStart == 0 && editor.document.textLength == editor.selectionModel.selectionEnd
        }
        test {
          for (i in 1..7) {
            actions(it)
          }
        }
      }
      actionTask("EditorUnSelectWord") {
        "${action(it)} shrinks selection. Try pressing it."
      }
      actionTask("\$SelectAll") {
        "Now select the whole file instantly with ${action(it)}."
      }
    }
}