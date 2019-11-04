package php.codeGolf.training.learn.lesson.kimpl

import php.codeGolf.training.learn.interfaces.Lesson
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.LessonListener
import php.codeGolf.training.learn.lesson.LessonState
import php.codeGolf.training.learn.lesson.LessonStateManager

abstract class KLesson(final override val name  : String,
                             override var module: Module,
                             override val lang  : String) : Lesson {

  abstract val lessonContent: LessonContext.() -> Unit
  final override val id: String = name
  @Volatile override var passed = LessonStateManager.getStateFromBase(id) == LessonState.PASSED
  @Volatile override var isOpen = false
  override val lessonListeners: MutableList<LessonListener> = mutableListOf()
}
